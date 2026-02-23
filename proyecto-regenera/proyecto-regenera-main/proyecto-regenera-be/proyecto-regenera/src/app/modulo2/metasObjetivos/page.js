"use client"
import React, { useState, useEffect } from "react"
import Link from "next/link"
import Image from "next/image"
import Select from "react-select"
import { useRouter } from "next/navigation"
import { Target, Plus, Save, Trash2, CheckCircle, Printer } from "lucide-react"
import useValidarAutenticacion from "@/app/lib/validaciones/useValidarAutenticacion"
import axiosClient from "@/app/lib/axiosClient"
import styles from "./metasObjetivos.module.css"

export default function MetasObjetivosPage() {
    const { isCheckingAuth } = useValidarAutenticacion();


    const [filas, setFilas] = useState([])
    const [indicadoresOptions, setIndicadoresOptions] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    const [isGuardando, setIsGuardando] = useState(false)
    const [saveMessage, setSaveMessage] = useState("")

    // Estado para el formulario de "Nuevo Ingreso"
    const [nuevoItem, setNuevoItem] = useState({
        objetivo: "",
        meta: "",
        responsable: "",
        avanceIndicador: "N/A",
        indicadorValue: null
    })

    // 1. Carga inicial de datos
    useEffect(() => {
        if (isCheckingAuth) return;
        const fetchData = async () => {
            setIsLoading(true)
            try {
                const [resInd, resObj] = await Promise.all([
                    axiosClient.get('/api/indicadores-ambientales/listar'),
                    axiosClient.get('/api/objetivos')
                ])

                const options = Array.isArray(resInd.data)
                    ? resInd.data.map(i => ({
                        value: i.idIndicador,
                        label: i.nombre || i.tipoIndicador || `Indicador ${i.idIndicador}`
                    }))
                    : []

                setIndicadoresOptions(options)

                if (Array.isArray(resObj.data)) {
                    setFilas(resObj.data.map(item => ({
                        id: item.id,
                        esNuevo: false,
                        objetivo: item.objetivo,
                        meta: item.meta,
                        responsable: item.responsable,
                        avanceIndicador: item.avanceIndicador || "N/A",
                        indicadorValue: item.idIndicador
                    })))
                }

            } catch (error) {
                console.error(error)
            } finally {
                setIsLoading(false)
            }
        }

        fetchData()
    }, [isCheckingAuth])
    if (isCheckingAuth) {
        return (
            <div className={styles.loaderContainer}>
                <img src="/icons8-hilandero.gif" alt="Cargando" className={styles.loaderImage} />
                <p className={styles.loaderText}>Cargando...</p>
            </div>
        );
    }

    // 2. Manejar inputs del Formulario de Nuevo Ingreso
    const handleNewInputChange = (campo, valor) => {
        setNuevoItem(prev => ({ ...prev, [campo]: valor }))
    }

    const handleNewSelectChange = (option) => {
        setNuevoItem(prev => ({ ...prev, indicadorValue: option }))
    }

    // 3. Agregar lo del formulario a la tabla visual
    const agregarFila = () => {
        // Validacion simple
        if (!nuevoItem.objetivo || !nuevoItem.meta || !nuevoItem.indicadorValue) {
            alert("Por favor complete complete todos los datos.")
            return
        }

        const nuevaFila = {
            id: Date.now(), // ID temporal
            esNuevo: true,  // Marcamos para saber que hay que guardarlo
            objetivo: nuevoItem.objetivo,
            meta: nuevoItem.meta,
            responsable: nuevoItem.responsable,
            avanceIndicador: "N/A",
            indicadorValue: nuevoItem.indicadorValue?.value || null
        }

        setFilas([...filas, nuevaFila])

        // Limpiar el formulario
        setNuevoItem({
            objetivo: "",
            meta: "",
            responsable: "",
            avanceIndicador: "N/A",
            indicadorValue: null
        })
    }

    // 4. Eliminar Fila
    const eliminarFila = async (id) => {
        if (!confirm("¿Eliminar este objetivo?")) return

        const fila = filas.find(f => f.id === id)

        // Si ya existía en BD, borrarlo real de la API
        if (!fila.esNuevo) {
            try {
                await axiosClient.delete(`/api/objetivos/${id}`)
            } catch (e) {
                alert("Error al eliminar el objetivo.")
                return
            }
        }
        // Actualizar estado visual
        setFilas(prev => prev.filter(f => f.id !== id))
    }

    // 5. Guardar Todo (Sincronizar cambios nuevos)
    const guardarTodo = async () => {
        setIsGuardando(true)
        setSaveMessage("")

        try {
            // Enviamos toda la lista actual para asegurar sincronía
            const payload = filas.map(f => ({
                id: f.esNuevo ? null : f.id, // Si es nuevo va null, si no va el ID
                objetivo: f.objetivo,
                meta: f.meta,
                responsable: f.responsable,
                idIndicador: f.indicadorValue
            }))

            await axiosClient.post('/api/objetivos', payload)

            setSaveMessage("Guardado exitosamente")

            // Recargar datos para confirmar IDs reales
            const resObj = await axiosClient.get('/api/objetivos')
            if (resObj.data) {
                setFilas(resObj.data.map(item => ({
                    id: item.id,
                    esNuevo: false,
                    objetivo: item.objetivo,
                    meta: item.meta,
                    responsable: item.responsable,
                    avanceIndicador: item.avanceIndicador || "N/A",
                    indicadorValue: item.idIndicador
                })))
            }

        } catch (error) {
            console.error(error)
            setSaveMessage("Error al guardar")
        } finally {
            setIsGuardando(false)
            setTimeout(() => setSaveMessage(""), 3000)
        }
    }



    // Helper para obtener etiqueta del indicador
    const getIndicadorLabel = (id) => {
        console.log("Buscando label para id:", id)
        console.log("Opciones actuales:", indicadoresOptions)
        const found = indicadoresOptions.find(
            opt => Number(opt.value) === Number(id)
        )

        console.log("Encontrado:", found)
        return found ? found.label : "-"
    }

    return (
        <main className={styles.main}>
            <div className={styles.backgroundPattern} />

            <header className={styles.topHeader}>
                <Link href="/">
                    <Image src="/logo2.png" alt="Logo" width={140} height={60} className={styles.logo} priority />
                </Link>
            </header>

            <div className={styles.matrizPage}>
                <div className={styles.container}>

                    {/* Título */}
                    <div className={styles.pageHeader}>
                        <div className={styles.headerIcon}><Target size={40} /></div>
                        <h1>Matriz de Objetivos y Metas</h1>
                        <p>Definición de objetivos estratégicos e indicadores de seguimiento.</p>
                    </div>

                    {/* SECCIÓN 1: FORMULARIO DE CARGA */}
                    <div className={styles.formSection}>
                        <div className={styles.sectionHeader}>
                            <h2>Definir Nuevo Objetivo</h2>
                        </div>

                        <div className={styles.formGrid}>
                            <div className={styles.formGroup}>
                                <label>1. Objetivo Global</label>
                                <input
                                    type="text"
                                    placeholder="Ej: Reducir consumo eléctrico..."
                                    value={nuevoItem.objetivo}
                                    onChange={(e) => handleNewInputChange('objetivo', e.target.value)}
                                />
                            </div>

                            <div className={styles.formGroup}>
                                <label>2. Meta Específica</label>
                                <input
                                    type="text"
                                    placeholder="Ej: Disminuir un 10% para..."
                                    value={nuevoItem.meta}
                                    onChange={(e) => handleNewInputChange('meta', e.target.value)}
                                />
                            </div>

                            <div className={styles.formGroup}>
                                <label>3. Indicador Asociado</label>
                                <Select
                                    instanceId="select-nuevo-indicador"
                                    options={indicadoresOptions}
                                    value={nuevoItem.indicadorValue}
                                    onChange={handleNewSelectChange}
                                    placeholder="Seleccionar..."
                                    classNamePrefix="rs"
                                    menuPortalTarget={typeof document !== 'undefined' ? document.body : null}
                                    styles={{
                                        menuPortal: base => ({ ...base, zIndex: 9999 }),
                                        control: base => ({ ...base, fontSize: '13px', minHeight: '42px', borderRadius: '12px', border: '2px solid #e2e8f0' })
                                    }}
                                />
                            </div>

                            <div className={styles.formGroup}>
                                <label>Responsable</label>
                                <input
                                    type="text"
                                    placeholder="Nombre..."
                                    value={nuevoItem.responsable}
                                    onChange={(e) => handleNewInputChange('responsable', e.target.value)}
                                />
                            </div>
                        </div>

                        <button className={styles.addButton} onClick={agregarFila} style={{ marginTop: '1rem', width: 'auto' }}>
                            <Plus size={20} /> Agregar a la Matriz
                        </button>
                    </div>

                    {/*  SECCIÓN 2: TABLA DE DATOS */}
                    <div className={styles.tableSection}>
                        <div className={styles.tableHeader}>
                            <h3>Objetivos Definidos</h3>
                        </div>

                        <div className={styles.tableWrapper}>
                            <table className={styles.table}>
                                <thead>
                                    <tr>
                                        <th style={{ width: '30%' }}>Objetivo Global</th>
                                        <th style={{ width: '30%' }}>Meta Específica</th>
                                        <th style={{ width: '25%' }}>Indicador</th>
                                        <th style={{ width: '25%' }}>Avance del Indicador</th>
                                        <th style={{ width: '10%' }}>Responsable</th>
                                        <th style={{ width: '5%' }}></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {isLoading ? (
                                        <tr><td colSpan="5" className={styles.emptyState}>Cargando datos...</td></tr>
                                    ) : filas.length === 0 ? (
                                        <tr>
                                            <td colSpan="5" className={styles.emptyState}>
                                                <Target size={48} style={{ margin: '0 auto', display: 'block', opacity: 0.3 }} />
                                                <p>No hay objetivos definidos aún.</p>
                                                <span>Utilice el formulario de arriba para cargar uno nuevo.</span>
                                            </td>
                                        </tr>
                                    ) : (
                                        filas.map((fila) => (
                                            <tr key={fila.id}>
                                                {/* Mostramos TEXTO, no inputs */}
                                                <td>{fila.objetivo}</td>
                                                <td>{fila.meta}</td>
                                                <td>
                                                    <span className={styles.resultadoBadge} style={{ background: '#f1f5f9', color: '#334155', border: '1px solid #cbd5e1' }}>
                                                        {getIndicadorLabel(fila.indicadorValue)}
                                                    </span>
                                                </td>
                                                <td>
                                                    {fila.avanceIndicador}

                                                </td>
                                                <td>{fila.responsable}</td>
                                                <td style={{ textAlign: 'center' }}>
                                                    <button
                                                        className={styles.deleteButton}
                                                        onClick={() => eliminarFila(fila.id)}
                                                        title="Eliminar fila"
                                                    >
                                                        <Trash2 size={18} />
                                                    </button>
                                                </td>
                                            </tr>
                                        ))
                                    )}
                                </tbody>
                            </table>
                        </div>
                    </div>

                    {/* BARRA DE ACCIONES INFERIOR */}
                    <div className={styles.actionBar}>
                        <button className={styles.printButton} onClick={() => window.print()}>
                            <Printer size={20} /> Imprimir
                        </button>

                        <button
                            className={styles.saveButton}
                            onClick={guardarTodo}
                            disabled={isGuardando}
                        >
                            {isGuardando ? (
                                "Guardando..."
                            ) : (
                                <><Save size={20} /> Guardar Todo</>
                            )}
                        </button>

                        {saveMessage && (
                            <div className={styles.saveMessage}>
                                <CheckCircle size={20} /> {saveMessage}
                            </div>
                        )}
                    </div>

                </div>
            </div>
        </main>
    )
}