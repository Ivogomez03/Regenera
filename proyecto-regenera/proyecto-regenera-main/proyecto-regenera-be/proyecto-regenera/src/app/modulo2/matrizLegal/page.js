"use client"
import styles from "./matrizLegal.module.css"
import Link from "next/link"
import Image from "next/image"
import { useState, useEffect } from "react"
import { FileText, Plus, Save, Trash2, Info, Scale, CheckCircle, Printer, BookOpen } from "lucide-react"
import axiosClient from "@/app/lib/axiosClient"

export default function MatrizLegalPage() {
  // Estados para los catálogos
  const [ambitos, setAmbitos] = useState([])
  const [tipos, setTipos] = useState([])
  const [aspectos, setAspectos] = useState([])
  const [resultados, setResultados] = useState([])

  // Estado para las sugerencias de normativas (plantillas)
  const [normativasSugeridas, setNormativasSugeridas] = useState([])

  // Estado del formulario
  const [form, setForm] = useState({
    idAmbito: "",
    idTipo: "",
    nro: "",
    anio: "",
    fecha: new Date().toISOString().split('T')[0],
    resena: "",
    idAspectoAmbientalTema: "",
    obligacion: "",
    puntoControl: "",
    idResultado: "",
  })

  // Estados de la grilla y UI
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

  // 2. Cargar los catálogos (Ambitos, Tipos, Aspectos, Resultados)
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

  // --- LÓGICA NUEVA: Cambio de Aspecto y Búsqueda de Plantillas ---
  const handleAspectoChange = async (e) => {
    const nuevoAspectoId = e.target.value

    // Actualizamos el formulario
    setForm(prev => ({ ...prev, idAspectoAmbientalTema: nuevoAspectoId }))
    setNormativasSugeridas([]) // Limpiar sugerencias anteriores

    if (!nuevoAspectoId) return

    // Consultamos al backend las normativas maestras para este aspecto
    try {
      const res = await axiosClient.get(`/api/matriz-legal/plantillas/${nuevoAspectoId}`)
      if (Array.isArray(res.data)) {
        setNormativasSugeridas(res.data)
      }
    } catch (error) {
      console.error("Error buscando normativas sugeridas:", error)
    }
  }

  // --- LÓGICA NUEVA: Selección de una Normativa Sugerida ---
  const handleNormativaSelect = (e) => {
    const idNormativaSeleccionada = e.target.value
    if (!idNormativaSeleccionada) return

    // Buscar el objeto completo en el array de sugerencias
    // Nota: Asegúrate de que 'id' es el campo correcto que devuelve tu DTO RequisitoLegalResponse
    const normativa = normativasSugeridas.find(n => n.id == idNormativaSeleccionada)

    if (normativa) {
      // Autocompletar el formulario
      setForm(prev => ({
        ...prev,
        idAmbito: normativa.idAmbito || "",
        idTipo: normativa.idTipo || "",
        nro: normativa.numero || "",
        anio: normativa.anio || "",
        resena: normativa.resena || "",
        obligacion: normativa.obligacion || "",
        // Dejamos vacíos puntoControl y Resultado para que el usuario los complete
        puntoControl: "",
        idResultado: ""
      }))
    }
  }

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm((prev) => ({ ...prev, [name]: value }))
  }

  const agregarFila = () => {
    // Validaciones básicas
    if (
      !form.idAmbito ||
      !form.idTipo ||
      !form.nro ||
      !form.anio ||
      !form.idAspectoAmbientalTema
    ) {
      alert("Por favor completá los campos principales (Ámbito, Tipo, Número, Año, Aspecto).")
      return
    }

    try {
      // Buscar los objetos completos para mostrarlos en la tabla localmente antes de guardar
      const ambitoObj = ambitos.find((a) => a.idAmbito == form.idAmbito)
      const tipoObj = tipos.find((t) => t.idTipo == form.idTipo)
      const aspectoObj = aspectos.find((a) => a.idAspectoAmbientalTema == form.idAspectoAmbientalTema)
      const resultadoObj = resultados.find((r) => r.idResultado == form.idResultado)

      const nuevaFila = {
        id: Date.now(), // ID temporal para React key
        esNuevo: true,
        idAmbito: form.idAmbito,
        idTipo: form.idTipo,
        idAspectoAmbientalTema: form.idAspectoAmbientalTema,
        idResultado: form.idResultado || null, // Puede ser nulo al inicio
        fecha: form.fecha,
        // Textos para mostrar en la tabla
        ambito: ambitoObj ? ambitoObj.ambito : "",
        tipo: tipoObj ? tipoObj.tipo : "",
        aspecto: aspectoObj ? aspectoObj.aspectoAmbientalTema : "",
        resultado: resultadoObj ? resultadoObj.resultado : "",

        numero: form.nro,
        anio: form.anio,
        obligacion: form.obligacion,
        puntoInspeccion: form.puntoControl,
        resena: form.resena
      }

      const newFilas = [...filas, nuevaFila]
      setFilas(newFilas)

      // Limpiar formulario
      setForm({
        idAmbito: "",
        idTipo: "",
        nro: "",
        anio: "",
        fecha: form.fecha,
        resena: "",
        idAspectoAmbientalTema: "",
        obligacion: "",
        puntoControl: "",
        idResultado: "",
      })
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

    // Si es nuevo (no guardado en BD), solo lo sacamos del estado
    const filaAEliminar = filas.find(f => f.id === id)
    if (filaAEliminar && filaAEliminar.esNuevo) {
      setFilas(filas.filter((f) => f.id !== id))
      return
    }

    // Si ya existe en BD, llamamos a la API
    try {
      await axiosClient.delete(`/api/matriz-legal/${id}`)
      const newFilas = filas.filter((fila) => fila.id !== id)
      setFilas(newFilas)

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
      // Mapeamos al formato que espera el Backend (RequisitoLegalCreateRequest)
      const datosParaEnviar = filasNuevas.map((fila) => ({
        idAmbito: parseInt(fila.idAmbito, 10),
        idTipo: parseInt(fila.idTipo, 10),
        idAspectoAmbiental: parseInt(fila.idAspectoAmbientalTema, 10), // Nota: backend espera idAspectoAmbiental
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

      // Recargamos la matriz para obtener los IDs reales y datos frescos
      const res = await axiosClient.get("/api/matriz-legal");
      if (Array.isArray(res.data)) {
        const filasActualizadas = res.data.map(f => ({ ...f, esNuevo: false }));
        setFilas(filasActualizadas);
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

  const handlePrint = () => {
    window.print()
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
          {/* Header */}
          <div className={styles.pageHeader}>
            <div className={styles.headerIcon}>
              <Scale size={40} />
            </div>
            <h1>Matriz Legal Ambiental</h1>
            <p>Gestiona y controla el cumplimiento de normativas ambientales</p>
          </div>

          {/* Form Section */}
          <div className={styles.formSection}>
            <div className={styles.sectionHeader}>
              <FileText size={20} />
              <h2>Nueva Normativa</h2>
            </div>

            <div className={styles.formGrid}>

              {/* 1. SELECCIÓN DE ASPECTO (PRIMERO PARA FILTRAR) */}
              <div className={styles.formGroup} style={{ gridColumn: "1 / -1" }}>
                <label style={{ fontWeight: "bold", color: "#0369a1" }}>1. Selecciona el Aspecto Ambiental</label>
                <select
                  name="idAspectoAmbientalTema"
                  value={form.idAspectoAmbientalTema}
                  onChange={handleAspectoChange}
                  style={{ backgroundColor: "#f0f9ff", borderColor: "#0284c7" }}
                >
                  <option value="">-- Seleccionar Aspecto --</option>
                  {Array.isArray(aspectos) && aspectos.map((a) => (
                    <option key={a.idAspectoAmbientalTema} value={a.idAspectoAmbientalTema}>
                      {a.aspectoAmbientalTema}
                    </option>
                  ))}
                </select>
              </div>

              {/* 2. SELECTOR DE NORMATIVAS SUGERIDAS (Solo aparece si hay sugerencias) */}
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

              {/* RESTO DE CAMPOS */}
              <div className={styles.formGroup}>
                <label>Ámbito</label>
                <select name="idAmbito" value={form.idAmbito} onChange={handleChange}>
                  <option value="">Seleccione...</option>
                  {Array.isArray(ambitos) && ambitos.map((a) => (
                    <option key={a.idAmbito} value={a.idAmbito}>{a.ambito}</option>
                  ))}
                </select>
              </div>

              <div className={styles.formGroup}>
                <label>Tipo</label>
                <select name="idTipo" value={form.idTipo} onChange={handleChange}>
                  <option value="">Seleccione...</option>
                  {Array.isArray(tipos) && tipos.map((t) => (
                    <option key={t.idTipo} value={t.idTipo}>{t.tipo}</option>
                  ))}
                </select>
              </div>

              <div className={styles.formGroup}>
                <label>Nro</label>
                <input type="text" name="nro" value={form.nro} onChange={handleChange} placeholder="Ej: 25675" />
              </div>

              <div className={styles.formGroup}>
                <label>Año</label>
                <input type="text" name="anio" value={form.anio} onChange={handleChange} placeholder="Ej: 2002" />
              </div>

              <div className={styles.formGroup}>
                <label>Fecha Evaluación</label>
                <input
                  type="date"
                  name="fecha"
                  value={form.fecha}
                  onChange={handleChange}
                />
              </div>

              {/* Reseña ocupa todo el ancho */}
              <div className={styles.formGroup} style={{ gridColumn: "1 / -1" }}>
                <label>Reseña / Título</label>
                <textarea
                  name="resena"
                  value={form.resena}
                  onChange={handleChange}
                  rows={2}
                  className={styles.textarea}
                  placeholder="Breve descripción de la norma..."
                />
              </div>

              <div className={styles.formGroup} style={{ gridColumn: "1 / -1" }}>
                <label>
                  Obligación Concreta
                  <span className={styles.tooltip}>
                    <Info size={16} className={styles.tooltipIcon} />
                    <span className={styles.tooltiptext}>
                      Revisa esta norma e identifica las obligaciones que genera en tu organización
                    </span>
                  </span>
                </label>
                <textarea
                  name="obligacion"
                  value={form.obligacion}
                  onChange={handleChange}
                  placeholder="Describe la obligación..."
                  rows={2}
                  className={styles.textarea}
                />
              </div>

              <div className={styles.formGroup}>
                <label>
                  Punto de Control
                  <span className={styles.tooltip}>
                    <Info size={16} className={styles.tooltipIcon} />
                    <span className={styles.tooltiptext}>
                      Indica de qué manera verificas el cumplimiento (ej. muestreo, documento, manifiesto)
                    </span>
                  </span>
                </label>
                <input
                  type="text"
                  name="puntoControl"
                  value={form.puntoControl}
                  onChange={handleChange}
                  placeholder="Método de verificación"
                />
              </div>

              <div className={styles.formGroup}>
                <label>Resultado</label>
                <select name="idResultado" value={form.idResultado} onChange={handleChange}>
                  <option value="">Seleccione...</option>
                  {Array.isArray(resultados) && resultados.map((r) => (
                    <option key={r.idResultado} value={r.idResultado}>{r.resultado}</option>
                  ))}
                </select>
              </div>
            </div>

            <button className={styles.addButton} onClick={agregarFila}>
              <Plus size={20} />
              Añadir a la Matriz
            </button>
          </div>

          {/* Table Section */}
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
                      <td colSpan="10" className={styles.emptyState}>
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
                        {/* Limitamos el texto largo en la tabla para que no rompa el diseño */}
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
              <button className={styles.printButton} onClick={handlePrint}>
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