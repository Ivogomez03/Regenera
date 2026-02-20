"use client"
import styles from "./matrizLegal.module.css"
import Link from "next/link"
import Image from "next/image"
import { useState, useEffect } from "react"
import { useForm } from "react-hook-form"
import { yupResolver } from "@hookform/resolvers/yup" // IMPORTANTE AGREGAR ESTO
import { FileText, Plus, Save, Trash2, Info, Scale, CheckCircle, Printer, BookOpen } from "lucide-react"
import matrizLegalSchema from "../../lib/validaciones/matrizLegalSchema"
import axiosClient from "@/app/lib/axiosClient"

export default function MatrizLegalPage() {
  // Configuración de React Hook Form
  const {
    register,
    handleSubmit,
    reset,
    watch,
    setValue,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(matrizLegalSchema),
    mode: "onSubmit",
    defaultValues: {
      fecha: new Date().toISOString().split('T')[0], // Fecha actual por defecto
    }
  });

  // Escuchamos los cambios en el selector de Aspecto Ambiental para buscar plantillas
  const aspectoSeleccionado = watch("idAspectoAmbientalTema");

  // Estados para los catálogos y UI
  const [ambitos, setAmbitos] = useState([])
  const [tipos, setTipos] = useState([])
  const [aspectos, setAspectos] = useState([])
  const [resultados, setResultados] = useState([])
  const [normativasSugeridas, setNormativasSugeridas] = useState([])

  const [filas, setFilas] = useState([])
  const [isLoading, setIsLoading] = useState(false)
  const [saveMessage, setSaveMessage] = useState("")

  // 1. Cargar las filas existentes de la Matriz del usuario
  useEffect(() => {
    const fetchMatriz = async () => {
      try {
        const response = await axiosClient.get("/api/matriz-legal")
        if (Array.isArray(response.data)) {
          const filasDeBD = response.data.map(f => ({ ...f, esNuevo: false }))
          setFilas(filasDeBD)
        }
      } catch (error) {
        console.error("Error cargando la matriz legal:", error)
      }
    }
    fetchMatriz()
  }, [])

  // 2. Cargar los catálogos
  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true)
      try {
        const [ambitosRes, tiposRes, aspectosRes, resultadosRes] = await Promise.all([
          axiosClient.get("/api/ambitos"),
          axiosClient.get("/api/tipos"),
          axiosClient.get("/api/aspectos-ambientales-temas"),
          axiosClient.get("/api/resultados"),
        ])

        setAmbitos(Array.isArray(ambitosRes.data) ? ambitosRes.data : [])
        setTipos(Array.isArray(tiposRes.data) ? tiposRes.data : [])
        setAspectos(Array.isArray(aspectosRes.data) ? aspectosRes.data : [])
        setResultados(Array.isArray(resultadosRes.data) ? resultadosRes.data : [])
      } catch (error) {
        console.error("Error fetching data:", error)
      } finally {
        setIsLoading(false)
      }
    }
    fetchData()
  }, [])

  // 3. Buscar sugerencias (Plantillas) cuando cambia el Aspecto
  useEffect(() => {
    setNormativasSugeridas([]) // Limpiar anteriores
    if (!aspectoSeleccionado) return

    const fetchPlantillas = async () => {
      try {
        const res = await axiosClient.get(`/api/matriz-legal/plantillas/${aspectoSeleccionado}`)
        if (Array.isArray(res.data)) {
          setNormativasSugeridas(res.data)
        }
      } catch (error) {
        console.error("Error buscando normativas sugeridas:", error)
      }
    }
    fetchPlantillas()
  }, [aspectoSeleccionado])

  // --- Selección de una Normativa Sugerida ---
  const handleNormativaSelect = (e) => {
    const idNormativaSeleccionada = e.target.value
    if (!idNormativaSeleccionada) return

    const normativa = normativasSugeridas.find(n => n.id == idNormativaSeleccionada)

    if (normativa) {
      // Usamos setValue para rellenar los inputs automáticamente
      setValue("idAmbito", normativa.idAmbito || "");
      setValue("idTipo", normativa.idTipo || "");
      setValue("nro", normativa.numero || "");
      setValue("anio", normativa.anio || "");
      setValue("resena", normativa.resena || "");
      setValue("obligacion", normativa.obligacion || "");
      setValue("puntoControl", "");
      setValue("idResultado", "");
    }
  }

  // --- GUARDAR FILA AL APRETAR AÑADIR (Solo se ejecuta si pasa las validaciones de Yup) ---
  const onSubmitRow = (data) => {
    try {
      // Buscar nombres para mostrarlos visualmente en la tabla
      const ambitoObj = ambitos.find((a) => a.idAmbito == data.idAmbito)
      const tipoObj = tipos.find((t) => t.idTipo == data.idTipo)
      const aspectoObj = aspectos.find((a) => a.idAspectoAmbientalTema == data.idAspectoAmbientalTema)
      const resultadoObj = resultados.find((r) => r.idResultado == data.idResultado)

      const nuevaFila = {
        id: Date.now(),
        esNuevo: true,
        ...data, // Pasamos todos los datos (idAmbito, idTipo, fecha, etc.)
        fecha: data.fecha instanceof Date ? data.fecha.toISOString().split('T')[0] : data.fecha,
        numero: data.nro, // Renombramos nro a numero para la tabla y backend
        puntoInspeccion: data.puntoControl, // Renombramos para el backend

        // Textos para mostrar en la tabla
        ambito: ambitoObj ? ambitoObj.ambito : "",
        tipo: tipoObj ? tipoObj.tipo : "",
        aspecto: aspectoObj ? aspectoObj.aspectoAmbientalTema : "",
        resultado: resultadoObj ? resultadoObj.resultado : "",
      }

      setFilas([...filas, nuevaFila])

      // Limpiar formulario y restablecer fecha por defecto
      reset({ fecha: new Date().toISOString().split('T')[0] })
      setNormativasSugeridas([])

      setSaveMessage("¡Fila agregada a la lista!")
      setTimeout(() => setSaveMessage(""), 3000)
    } catch (error) {
      console.error("Error saving row:", error)
      alert("Error al agregar la fila.")
    }
  }

  const eliminarFila = async (id) => {
    if (!confirm("¿Estás seguro de eliminar esta fila?")) return

    const filaAEliminar = filas.find(f => f.id === id)
    if (filaAEliminar && filaAEliminar.esNuevo) {
      setFilas(filas.filter((f) => f.id !== id))
      return
    }

    try {
      await axiosClient.delete(`/api/matriz-legal/${id}`)
      setFilas(filas.filter((fila) => fila.id !== id))

      setSaveMessage("¡Fila eliminada exitosamente!")
      setTimeout(() => setSaveMessage(""), 3000)
    } catch (error) {
      console.error("Error deleting row:", error)
      alert("Error al eliminar la fila.")
    }
  }

  const guardarFilas = async () => {
    const filasNuevas = filas.filter((fila) => fila.esNuevo === true)

    if (filasNuevas.length === 0) {
      alert("No hay filas NUEVAS para guardar. Las existentes ya están en la base de datos.")
      return
    }

    setIsLoading(true)

    try {
      const datosParaEnviar = filasNuevas.map((fila) => ({
        idAmbito: parseInt(fila.idAmbito, 10),
        idTipo: parseInt(fila.idTipo, 10),
        idAspectoAmbiental: parseInt(fila.idAspectoAmbientalTema, 10),
        idResultado: fila.idResultado ? parseInt(fila.idResultado, 10) : null,
        fecha: fila.fecha,
        numero: String(fila.numero),
        anio: parseInt(fila.anio, 10),
        obligacion: String(fila.obligacion),
        puntoInspeccion: String(fila.puntoInspeccion),
        resena: String(fila.resena || "")
      }))

      await axiosClient.post("/api/matriz-legal", datosParaEnviar)

      setSaveMessage("¡Matriz guardada exitosamente!")
      setTimeout(() => setSaveMessage(""), 3000)

      const res = await axiosClient.get("/api/matriz-legal");
      if (Array.isArray(res.data)) {
        setFilas(res.data.map(f => ({ ...f, esNuevo: false })));
      }

    } catch (error) {
      console.error("Error saving data:", error)
      if (error.response?.data?.message) {
        alert(`Error del servidor: ${error.response.data.message}`)
      } else {
        alert("Error al guardar.")
      }
    } finally {
      setIsLoading(false)
    }
  }

  if (isLoading) {
    return (
      <div className={styles.loaderContainer}>
        <Image src="/logo4.png" alt="Logo" width={120} height={120} className={styles.loaderImage} />
        <p className={styles.loaderText}>Cargando...</p>
      </div>
    )
  }

  return (
    <main className={styles.main}>
      <header className={styles.topHeader}>
        <Link href="/">
          <Image src="/logo2.png" alt="Regenera Logo" width={140} height={60} className={styles.logo} priority />
        </Link>
      </header>

      <div className={styles.backgroundPattern} />

      <div className={styles.matrizPage}>
        <div className={styles.container}>
          <div className={styles.pageHeader}>
            <div className={styles.headerIcon}>
              <Scale size={40} />
            </div>
            <h1>Matriz Legal Ambiental</h1>
            <p>Gestiona y controla el cumplimiento de normativas ambientales</p>
          </div>

          {/* REEMPLAZAMOS EL DIV DEL FORMULARIO POR UN <form onSubmit={handleSubmit}> */}
          <form className={styles.formSection} onSubmit={handleSubmit(onSubmitRow)}>
            <div className={styles.sectionHeader}>
              <FileText size={20} />
              <h2>Nueva Normativa</h2>
            </div>

            <div className={styles.formGrid}>
              {/* 1. SELECCIÓN DE ASPECTO */}
              <div className={styles.formGroup} style={{ gridColumn: "1 / -1" }}>
                <label style={{ fontWeight: "bold", color: "#0369a1" }}>1. Selecciona el Aspecto Ambiental*</label>
                <select
                  {...register("idAspectoAmbientalTema")}
                  style={{ backgroundColor: "#f0f9ff", borderColor: "#0284c7" }}
                >
                  <option value="">-- Seleccionar Aspecto --</option>
                  {aspectos.map((a) => (
                    <option key={a.idAspectoAmbientalTema} value={a.idAspectoAmbientalTema}>
                      {a.aspectoAmbientalTema}
                    </option>
                  ))}
                </select>
                <p style={{ color: "red", fontSize: "0.8rem", margin: 0 }}>{errors.idAspectoAmbientalTema?.message}</p>
              </div>

              {/* 2. SELECTOR DE NORMATIVAS SUGERIDAS */}
              {normativasSugeridas.length > 0 && (
                <div className={styles.formGroup} style={{ gridColumn: "1 / -1" }}>
                  <label style={{ display: 'flex', alignItems: 'center', gap: '8px', color: '#166534', fontWeight: "bold" }}>
                    <BookOpen size={18} />
                    2. Normativas Sugeridas (Selecciona para autocompletar)
                  </label>
                  <select onChange={handleNormativaSelect} defaultValue="" style={{ backgroundColor: "#f0fdf4", borderColor: "#16a34a" }}>
                    <option value="">-- Seleccionar de la Biblioteca --</option>
                    {normativasSugeridas.map((norma) => (
                      <option key={norma.id} value={norma.id}>
                        {norma.tipo} {norma.numero} ({norma.anio}) - {norma.resena ? norma.resena.substring(0, 80) : "Sin descripción"}...
                      </option>
                    ))}
                  </select>
                </div>
              )}

              {/* RESTO DE CAMPOS CON REGISTER */}
              <div className={styles.formGroup}>
                <label>Ámbito*</label>
                <select {...register("idAmbito")}>
                  <option value="">Seleccione...</option>
                  {ambitos.map((a) => (
                    <option key={a.idAmbito} value={a.idAmbito}>{a.ambito}</option>
                  ))}
                </select>
                <p style={{ color: "red", fontSize: "0.8rem", margin: 0 }}>{errors.idAmbito?.message}</p>
              </div>

              <div className={styles.formGroup}>
                <label>Tipo*</label>
                <select {...register("idTipo")}>
                  <option value="">Seleccione...</option>
                  {tipos.map((t) => (
                    <option key={t.idTipo} value={t.idTipo}>{t.tipo}</option>
                  ))}
                </select>
                <p style={{ color: "red", fontSize: "0.8rem", margin: 0 }}>{errors.idTipo?.message}</p>
              </div>

              <div className={styles.formGroup}>
                <label>Nro*</label>
                <input type="text" {...register("nro")} placeholder="Ej: 25675" />
                <p style={{ color: "red", fontSize: "0.8rem", margin: 0 }}>{errors.nro?.message}</p>
              </div>

              <div className={styles.formGroup}>
                <label>Año*</label>
                <input type="number" {...register("anio")} placeholder="Ej: 2002" />
                <p style={{ color: "red", fontSize: "0.8rem", margin: 0 }}>{errors.anio?.message}</p>
              </div>

              <div className={styles.formGroup}>
                <label>Fecha Evaluación*</label>
                <input type="date" {...register("fecha")} />
                <p style={{ color: "red", fontSize: "0.8rem", margin: 0 }}>{errors.fecha?.message}</p>
              </div>

              <div className={styles.formGroup} style={{ gridColumn: "1 / -1" }}>
                <label>Reseña / Título</label>
                <textarea {...register("resena")} rows={2} className={styles.textarea} placeholder="Breve descripción..." />
              </div>

              <div className={styles.formGroup} style={{ gridColumn: "1 / -1" }}>
                <label>Obligación Concreta</label>
                <textarea {...register("obligacion")} rows={2} className={styles.textarea} placeholder="Describe la obligación..." />
              </div>

              <div className={styles.formGroup}>
                <label>Punto de Control</label>
                <input type="text" {...register("puntoControl")} placeholder="Método de verificación" />
              </div>

              <div className={styles.formGroup}>
                <label>Resultado</label>
                <select {...register("idResultado")}>
                  <option value="">Seleccione...</option>
                  {resultados.map((r) => (
                    <option key={r.idResultado} value={r.idResultado}>{r.resultado}</option>
                  ))}
                </select>
              </div>
            </div>

            {/* BOTÓN DE SUBMIT */}
            <button type="submit" className={styles.addButton}>
              <Plus size={20} />
              Añadir a la Matriz
            </button>
          </form>

          {/* Table Section (Sin cambios sustanciales, igual al original) */}
          <div className={styles.tableSection}>
            <div className={styles.tableHeader}>
              <h3>Matriz de Normativas ({filas.length})</h3>
            </div>
            <div className={styles.tableWrapper}>
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Ámbito</th>
                    <th>Tipo</th>
                    <th>N°</th>
                    <th>Año</th>
                    <th>Fecha Evaluación</th>
                    <th>Aspecto Ambiental</th>
                    <th>Reseña</th>
                    <th>Obligación</th>
                    <th>Punto Control</th>
                    <th>Resultado</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {filas.length === 0 ? (
                    <tr>
                      <td colSpan="11" className={styles.emptyState}>
                        <FileText size={48} />
                        <p>No hay normativas agregadas</p>
                        <span>Selecciona un aspecto y agrega una normativa</span>
                      </td>
                    </tr>
                  ) : (
                    filas.map((fila) => (
                      <tr key={fila.id}>
                        <td>{fila.ambito}</td>
                        <td>{fila.tipo}</td>
                        <td>{fila.numero}</td>
                        <td>{fila.anio}</td>
                        <td>{fila.fecha}</td>
                        <td>{fila.aspecto}</td>
                        <td title={fila.resena}>
                          {fila.resena && fila.resena.length > 30 ? fila.resena.substring(0, 30) + "..." : fila.resena}
                        </td>
                        <td title={fila.obligacion}>
                          {fila.obligacion && fila.obligacion.length > 30 ? fila.obligacion.substring(0, 30) + "..." : fila.obligacion}
                        </td>
                        <td>{fila.puntoInspeccion}</td>
                        <td>
                          <span className={styles.resultadoBadge}>
                            {fila.resultado || "Pendiente"}
                          </span>
                        </td>
                        <td>
                          <button
                            className={styles.deleteButton}
                            onClick={() => eliminarFila(fila.id)}
                            title="Eliminar"
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

          {/* Save Button */}
          {filas.length > 0 && (
            <div className={styles.actionBar}>
              <button className={styles.printButton} onClick={() => window.print()}>
                <Printer size={20} />
                Imprimir Matriz
              </button>
              <button className={styles.saveButton} onClick={guardarFilas}>
                <Save size={20} />
                Guardar Matriz
              </button>
              {saveMessage && (
                <div className={styles.saveMessage}>
                  <CheckCircle size={18} />
                  {saveMessage}
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </main>
  )
}