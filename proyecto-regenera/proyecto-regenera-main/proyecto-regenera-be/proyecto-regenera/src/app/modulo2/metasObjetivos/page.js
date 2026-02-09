"use client"
import React, { useState, useEffect } from "react"
import Link from "next/link"
import Image from "next/image"
import Select from "react-select"
import { Target, Plus, Save, Trash2, CheckCircle, Printer, ArrowLeft } from "lucide-react"
import axiosClient from "@/app/lib/axiosClient"


import styles from "../matrizAmbiental/matrizAmbiental.module.css"

export default function MetasObjetivosPage() {

    const [filas, setFilas] = useState([])
    const [indicadoresOptions, setIndicadoresOptions] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    const [isGuardando, setIsGuardando] = useState(false)
    const [saveMessage, setSaveMessage] = useState("")

    // carga inicial de datos

    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true)
            try {
                const resInd = await axiosClient.get('/api/indicadores-ambientales')

                let options = []
                if (Array.isArray(resInd.data)) {
                    options = resInd.data.map(i => ({
                        value: i.idIndicador,
                        label: i.nombre || i.tipoIndicador || `Indicador ${i.idIndicador}`
                    }))
                }
                setIndicadoresOptions(options)

                const resObj = await axiosClient.get('/api/objetivos')

                if (Array.isArray(resObj.data)) {
                    const filasMapeadas = resObj.data.map(item => ({
                        id: item.id,
                        esNuevo: false,
                        objetivo: item.objetivo,
                        meta: item.meta,
                        responsable: item.responsable,
                        indicadorValue: item.idIndicador
                    }))
                    setFilas(filasMapeadas)
                }

            } catch (error) {
                console.error("Error cargando datos:", error)
            } finally {
                setIsLoading(false)
            }
        }
        fetchData()
    }, [])

    // Agregar una fila vacía
    const agregarFila = () => {
        const nuevaFila = {
            id: Date.now(),
            esNuevo: true,
            objetivo: "",
            meta: "",
            indicadorValue: null,
            responsable: ""
        }
        setFilas([...filas, nuevaFila])
    }

    const handleChange = (id, campo, valor) => {
        setFilas(prev => prev.map(f => f.id === id ? { ...f, [campo]: valor } : f))
    }

    const handleSelectChange = (id, option) => {
        setFilas(prev => prev.map(f => f.id === id ? {
            ...f,
            indicadorValue: option ? option.value : null
        } : f))
    }

    // Eliminar fila (Si es nueva borra local, si es vieja llama a API)
    const eliminarFila = async (id) => {
        if (!confirm("¿Eliminar este objetivo?")) return

        const fila = filas.find(f => f.id === id)

        // Si ya existía en BD, borrarlo real
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

    const guardarTodo = async () => {
        setIsGuardando(true)
        setSaveMessage("")

        try {
            const payload = filas.map(f => ({
                id: f.esNuevo ? null : f.id,
                objetivo: f.objetivo,
                meta: f.meta,
                responsable: f.responsable,
                idIndicador: f.indicadorValue
            }))

            await axiosClient.post('/api/objetivos', payload)

            setSaveMessage("Guardado exitosamente")

            const resObj = await axiosClient.get('/api/objetivos')
            if (resObj.data) {
                setFilas(resObj.data.map(item => ({
                    id: item.id,
                    esNuevo: false,
                    objetivo: item.objetivo,
                    meta: item.meta,
                    responsable: item.responsable,
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

                    {/* TABLA DE CONTENIDO */}
                    <div className={styles.tableSection}>
                        <div className={styles.tableWrapper} style={{ overflow: 'visible' }}> {/* Overflow visible para que el Select se vea bien */}
                            <table className={styles.table}>
                                <thead>
                                    <tr>
                                        <th style={{ width: '30%' }}>1. Objetivo Global</th>
                                        <th style={{ width: '30%' }}>2. Meta Específica</th>
                                        <th style={{ width: '20%' }}>3. Indicador Asociado</th>
                                        <th style={{ width: '15%' }}>Responsable</th>
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
                                                <p>No hay objetivos definidos.</p>
                                                <span>Agregue una fila para comenzar.</span>
                                            </td>
                                        </tr>
                                    ) : (
                                        filas.map((fila) => (
                                            <tr key={fila.id}>
                                                {/* Columna: Objetivo */}
                                                <td>
                                                    <textarea
                                                        rows={3}
                                                        className={styles.tableInput} // Reutiliza estilo input transparente
                                                        style={{ border: '1px solid #ddd', borderRadius: '4px', padding: '8px' }} // Un poco de estilo extra para que parezca editable
                                                        value={fila.objetivo || ''}
                                                        onChange={e => handleChange(fila.id, 'objetivo', e.target.value)}
                                                        placeholder="Describir objetivo global..."
                                                    />
                                                </td>

                                                {/* Columna: Meta */}
                                                <td>
                                                    <textarea
                                                        rows={3}
                                                        className={styles.tableInput}
                                                        style={{ border: '1px solid #ddd', borderRadius: '4px', padding: '8px' }}
                                                        value={fila.meta || ''}
                                                        onChange={e => handleChange(fila.id, 'meta', e.target.value)}
                                                        placeholder="Definir meta (cuantitativa/fecha)..."
                                                    />
                                                </td>

                                                {/* Columna: Indicador (Select) */}
                                                <td>
                                                    <Select
                                                        instanceId={`select-indicador-${fila.id}`}
                                                        options={indicadoresOptions}
                                                        // Buscamos el objeto opción que coincida con el ID guardado
                                                        value={indicadoresOptions.find(op => op.value === fila.indicadorValue)}
                                                        onChange={(op) => handleSelectChange(fila.id, op)}
                                                        placeholder="Seleccionar..."
                                                        classNamePrefix="rs"
                                                        menuPortalTarget={typeof document !== 'undefined' ? document.body : null}
                                                        styles={{
                                                            menuPortal: base => ({ ...base, zIndex: 9999 }),
                                                            control: base => ({ ...base, fontSize: '13px', minHeight: '38px' })
                                                        }}
                                                    />
                                                </td>

                                                {/* Columna: Responsable */}
                                                <td>
                                                    <input
                                                        type="text"
                                                        className={styles.tableInput}
                                                        style={{ border: '1px solid #ddd', borderRadius: '4px', padding: '8px', width: '100%' }}
                                                        value={fila.responsable || ''}
                                                        onChange={e => handleChange(fila.id, 'responsable', e.target.value)}
                                                        placeholder="Nombre..."
                                                    />
                                                </td>

                                                {/* Acciones */}
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

                        <button className={styles.addButton} onClick={agregarFila} style={{ marginTop: '1.5rem' }}>
                            <Plus size={20} /> Agregar Objetivo
                        </button>
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
                                <><Save size={20} /> Guardar Cambios</>
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