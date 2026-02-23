"use client"
import { useState, useEffect } from "react"
import Link from "next/link"
import Image from "next/image"
import { ArrowLeft, Activity, Target, AlertTriangle } from "lucide-react"
import useValidarAutenticacion from "@/app/lib/validaciones/useValidarAutenticacion"
import axiosClient from "@/app/lib/axiosClient"
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts'
import styles from "./dashboard.module.css"

const COLORES = ['#0284c7', '#16a34a', '#ea580c', '#ca8a04', '#9333ea'];

export default function DashboardPage() {
    const { isCheckingAuth } = useValidarAutenticacion();
    const [indicadores, setIndicadores] = useState([])
    const [objetivos, setObjetivos] = useState([])
    const [isLoading, setIsLoading] = useState(true)

    useEffect(() => {
        if (isCheckingAuth) return;
        const fetchDashboardData = async () => {
            setIsLoading(true)
            try {
                const [resInd, resObj] = await Promise.all([
                    axiosClient.get('/api/indicadores-ambientales/listar'),
                    axiosClient.get('/api/objetivos')
                ])

                setIndicadores(Array.isArray(resInd.data) ? resInd.data : [])
                setObjetivos(Array.isArray(resObj.data) ? resObj.data : [])

            } catch (error) {

                console.error("Error cargando datos del dashboard:", error)
                if (error.response?.status === 401 || error.response?.status === 403) {
                    router.push("/login")
                }
            } finally {
                setIsLoading(false)
            }
        }
        fetchDashboardData()
    }, [isCheckingAuth])
    if (isCheckingAuth) {
        return (
            <div className={styles.loaderContainer}>
                <img src="/icons8-hilandero.gif" alt="Cargando" className={styles.loaderImage} />
                <p className={styles.loaderText}>Cargando...</p>
            </div>
        );
    }
    const datosMedicion = indicadores.map(ind => ({
        name: ind.tipoIndicador.replace(/_/g, ' ').substring(0, 15) + '...',
        ValorMedido: ind.valorMedido || 0,
        Meta: ind.metaValor || 0,
        unidad: ind.metaUnidad
    }))

    const datosObjetivos = [
        { name: 'Completados', value: objetivos.filter(o => parseInt(o.avanceIndicador) >= 100).length },
        { name: 'En Proceso', value: objetivos.filter(o => parseInt(o.avanceIndicador) > 0 && parseInt(o.avanceIndicador) < 100).length },
        { name: 'Sin Iniciar', value: objetivos.filter(o => parseInt(o.avanceIndicador) === 0 || o.avanceIndicador === 'N/A').length },
    ]

    if (isLoading) return <div className={styles.loader}>Cargando Tablero...</div>

    return (
        <main className={styles.main}>
            <div className={styles.backgroundPattern} />

            <header className={styles.topHeader}>
                <Link href="/modulo2" className={styles.backLink}>
                    <ArrowLeft size={24} /> Volver
                </Link>
                <Link href="/">
                    <Image src="/logo2.png" alt="Logo" width={140} height={60} />
                </Link>
            </header>

            <div className={styles.dashboardPage}>

                <div className={styles.pageHeader}>
                    <h1 className={styles.pageTitle}>Panel de Control Ambiental</h1>
                    <p className={styles.pageDescription}>Resumen global de tus indicadores y metas.</p>
                </div>

                <div className={styles.kpiGrid}>
                    <div className={styles.kpiCard}>
                        <div className={styles.iconBlue}><Activity size={28} /></div>
                        <div>
                            <p className={styles.kpiLabel}>Indicadores Activos</p>
                            <h3 className={styles.kpiValue}>{indicadores.length}</h3>
                        </div>
                    </div>
                    <div className={styles.kpiCard}>
                        <div className={styles.iconGreen}><Target size={28} /></div>
                        <div>
                            <p className={styles.kpiLabel}>Objetivos Trazados</p>
                            <h3 className={styles.kpiValue}>{objetivos.length}</h3>
                        </div>
                    </div>
                    <div className={styles.kpiCard}>
                        <div className={styles.iconOrange}><AlertTriangle size={28} /></div>
                        <div>
                            <p className={styles.kpiLabel}>Metas en Riesgo</p>
                            <h3 className={styles.kpiValue}>
                                {indicadores.filter(i => i.valorMedido > i.metaValor).length}
                            </h3>
                        </div>
                    </div>
                </div>

                <div className={styles.chartsGrid}>

                    <div className={styles.chartCard}>
                        <h3 className={styles.chartTitle}>Mediciones vs Metas</h3>
                        {datosMedicion.length > 0 ? (
                            <ResponsiveContainer width="100%" height={300}>
                                <BarChart data={datosMedicion} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                                    <CartesianGrid strokeDasharray="3 3" vertical={false} />
                                    <XAxis dataKey="name" tick={{ fontSize: 12 }} />
                                    <YAxis />
                                    <Tooltip cursor={{ fill: 'transparent' }} />
                                    <Legend />
                                    <Bar dataKey="Meta" fill="#cbd5e1" radius={[4, 4, 0, 0]} />
                                    <Bar dataKey="ValorMedido" fill="#145551" radius={[4, 4, 0, 0]} />
                                </BarChart>
                            </ResponsiveContainer>
                        ) : (
                            <p className={styles.emptyMessage}>No hay indicadores suficientes para graficar.</p>
                        )}
                    </div>

                    <div className={styles.chartCard}>
                        <h3 className={styles.chartTitle}>Estado de Objetivos</h3>
                        {objetivos.length > 0 ? (
                            <ResponsiveContainer width="100%" height={300}>
                                <PieChart>
                                    <Pie
                                        data={datosObjetivos}
                                        cx="50%"
                                        cy="50%"
                                        innerRadius={60}
                                        outerRadius={100}
                                        paddingAngle={5}
                                        dataKey="value"
                                    >
                                        {datosObjetivos.map((entry, index) => (
                                            <Cell key={`cell-${index}`} fill={COLORES[index % COLORES.length]} />
                                        ))}
                                    </Pie>
                                    <Tooltip />
                                    <Legend />
                                </PieChart>
                            </ResponsiveContainer>
                        ) : (
                            <p className={styles.emptyMessage}>Carga objetivos para visualizar su estado.</p>
                        )}
                    </div>

                </div>
            </div>
        </main>
    )
}