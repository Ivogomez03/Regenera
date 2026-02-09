"use client"
import React, { useState, useEffect } from "react"
import Link from "next/link"
import Image from "next/image"
import { Eye, FileText, Scale, Activity, Calendar, Search, FolderClosed } from "lucide-react"

import axiosClient from "@/app/lib/axiosClient"
import styles from "./revisiones.module.css" // Definir estilos similares a tus otros módulos

export default function RevisionesPage() {
    const [historial, setHistorial] = useState([])
    const [loading, setLoading] = useState(true)
    const [filtro, setFiltro] = useState("")

    useEffect(() => {
        const fetchRevisiones = async () => {
            try {
                const res = await axiosClient.get("/api/revisiones")
                setHistorial(res.data)
            } catch (error) {
                console.error("Error cargando revisiones:", error)
            } finally {
                setLoading(false)
            }
        }
        fetchRevisiones()
    }, [])

    const getIcono = (tipo) => {
        switch (tipo) {
            case "MATRIZ_ASPECTOS": return <FileText size={20} color="#0284c7" />
            case "MATRIZ_LEGAL": return <Scale size={20} color="#dc2626" />
            case "INDICADOR": return <Activity size={20} color="#16a34a" />
            default: return <FileText size={20} />
        }
    }

    const getEtiquetaTipo = (tipo) => {
        switch (tipo) {
            case "MATRIZ_ASPECTOS": return "Matriz Ambiental"
            case "MATRIZ_LEGAL": return "Requisito Legal"
            case "INDICADOR": return "Indicador"
            default: return tipo
        }
    }

    // Lógica de redirección según el tipo
    const getLinkDestino = (item) => {
        switch (item.tipo) {
            case "MATRIZ_ASPECTOS":
                // Redirige a la página de edición/vista cargando el ID
                return `/modulo2/matrizAmbiental/vista?id=${item.id}`
            case "MATRIZ_LEGAL":
                // La matriz legal suele ser una lista global, redirigimos a la tabla general
                return `/modulo2/matrizLegal`
            case "INDICADOR":
                // Redirige a indicadores
                return `/modulo2/indicadoresAmbientales`
            default:
                return "#"
        }
    }

    const filteredHistorial = historial.filter(item =>
        item.titulo.toLowerCase().includes(filtro.toLowerCase()) ||
        item.subtitulo.toLowerCase().includes(filtro.toLowerCase()) ||
        getEtiquetaTipo(item.tipo).toLowerCase().includes(filtro.toLowerCase())
    )

    if (loading) return <div className={styles.loader}>Cargando historial...</div>

    return (
        <main className={styles.main}>
            <header className={styles.topHeader}>
                <Link href="/">
                    <Image src="/logo2.png" alt="Regenera Logo" width={140} height={60} className={styles.logo} priority />
                </Link>
            </header>

            <div className={styles.container}>
                <div className={styles.pageHeader}>
                    <div className={styles.iconHeader}>
                        <FolderClosed size={40} />
                    </div>

                    <h1>Revisiones y Documentos</h1>
                    <p>Historial de formularios, normativas e indicadores generados.</p>
                </div>

                <div className={styles.filterBar}>
                    <div className={styles.searchContainer}>
                        <Search size={18} className={styles.searchIcon} />
                        <input
                            type="text"
                            placeholder="Buscar por título, tipo o detalle..."
                            value={filtro}
                            onChange={(e) => setFiltro(e.target.value)}
                            className={styles.searchInput}
                        />
                    </div>
                </div>

                <div className={styles.tableWrapper}>
                    <table className={styles.table}>
                        <thead>
                            <tr>
                                <th>Tipo</th>
                                <th>Título / Referencia</th>
                                <th>Detalle / Objetivo</th>
                                <th>Fecha</th>
                                <th>Estado / Valor</th>
                                <th>Ver</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filteredHistorial.length === 0 ? (
                                <tr>
                                    <td colSpan="6" className={styles.emptyState}>No hay registros encontrados.</td>
                                </tr>
                            ) : (
                                filteredHistorial.map((item, index) => (
                                    <tr key={`${item.tipo}-${item.id}-${index}`}>
                                        <td className={styles.typeCell}>
                                            <div className={styles.typeBadge}>
                                                {getIcono(item.tipo)}
                                                <span>{getEtiquetaTipo(item.tipo)}</span>
                                            </div>
                                        </td>
                                        <td className={styles.titleCell}>{item.titulo}</td>
                                        <td className={styles.subtitleCell}>{item.subtitulo}</td>
                                        <td>
                                            <div className={styles.dateCell}>
                                                <Calendar size={14} />
                                                {item.fecha}
                                            </div>
                                        </td>
                                        <td>
                                            <span className={styles.statusBadge}>{item.estado}</span>
                                        </td>
                                        <td>
                                            <Link href={getLinkDestino(item)} className={styles.viewButton}>
                                                <Eye size={18} />
                                            </Link>
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    )
}