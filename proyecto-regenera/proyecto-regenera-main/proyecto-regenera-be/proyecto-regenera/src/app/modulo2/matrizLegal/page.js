"use client"
import styles from "./matrizLegal.module.css"
import Link from "next/link"
import Image from "next/image"
import { useState, useEffect } from "react"
import { FileText, Plus, Save, Trash2, Info, Scale, CheckCircle, Printer } from "lucide-react"
import axiosClient from "@/app/lib/axiosClient"

export default function MatrizLegalPage() {
  const [ambitos, setAmbitos] = useState([])
  const [tipos, setTipos] = useState([])
  const [aspectos, setAspectos] = useState([])
  const [resultados, setResultados] = useState([])

  const [form, setForm] = useState({
    idAmbito: "",
    idTipo: "",
    nro: "",
    anio: "",
    resena: "",
    idAspectoAmbientalTema: "",
    obligacion: "",
    puntoControl: "",
    idResultado: "",
  })

  const [filas, setFilas] = useState([])
  const [isLoading, setIsLoading] = useState(false)
  const [saveMessage, setSaveMessage] = useState("")

  useEffect(() => {
    const fetchMatriz = async () => {
      try {
        const response = await axiosClient.get("/api/matriz-legal")

        if (Array.isArray(response.data)) {
          const filasDeBD = response.data.map(f => ({ ...f, esNuevo: false }))
          setFilas(response.data)
        }
      } catch (error) {
        console.error("Error cargando la matriz legal:", error)
      } finally {
        setIsLoading(false)
      }
    }

    fetchMatriz()
  }, [])

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

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm((prev) => ({ ...prev, [name]: value }))
  }

  const agregarFila = () => {
    if (
      !form.idAmbito ||
      !form.idTipo ||
      !form.nro ||
      !form.anio ||
      !form.idAspectoAmbientalTema ||
      !form.obligacion ||
      !form.puntoControl ||
      !form.idResultado
    ) {
      alert("Por favor completá todos los campos.")
      return
    }
    try {

      const ambitoObj = ambitos.find((a) => a.idAmbito == form.idAmbito)
      const tipoObj = tipos.find((t) => t.idTipo == form.idTipo)
      const aspectoObj = aspectos.find((a) => a.idAspectoAmbientalTema == form.idAspectoAmbientalTema)
      const resultadoObj = resultados.find((r) => r.idResultado == form.idResultado)

      const nuevaFila = {
        id: Date.now(),
        esNuevo: true,
        idAmbito: form.idAmbito,
        idTipo: form.idTipo,
        idAspectoAmbientalTema: form.idAspectoAmbientalTema,
        idResultado: form.idResultado,

        ambito: ambitoObj ? ambitoObj.ambito : "",
        tipo: tipoObj ? tipoObj.tipo : "",
        aspecto: aspectoObj ? aspectoObj.aspectoAmbiental : "",
        resultado: resultadoObj ? resultadoObj.resultado : "",

        numero: form.nro,
        anio: form.anio,
        obligacion: form.obligacion,
        puntoInspeccion: form.puntoControl
      }

      const newFilas = [...filas, nuevaFila]
      setFilas(newFilas)

      setForm({
        idAmbito: "",
        idTipo: "",
        nro: "",
        anio: "",
        idAspectoAmbientalTema: "",
        obligacion: "",
        puntoControl: "",
        idResultado: "",
      })

      setSaveMessage("¡Fila agregada exitosamente!")
      setTimeout(() => setSaveMessage(""), 3000)
    } catch (error) {
      console.error("Error saving row:", error)
      alert("Error al guardar la fila.")
    }
  }

  const eliminarFila = async (id) => {
    if (!confirm("¿Estás seguro de eliminar esta fila?")) return

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
      alert("No hay filas NUEVAS para guardar. Las existentes ya están guardadas.")
      return
    }

    setIsLoading(true)

    try {
      const datosParaEnviar = filasNuevas.map((fila) => ({
        idAmbito: parseInt(fila.idAmbito, 10),
        idTipo: parseInt(fila.idTipo, 10),
        idAspectoAmbientalTema: parseInt(fila.idAspectoAmbientalTema, 10),
        idResultado: parseInt(fila.idResultado, 10),
        numero: String(fila.numero),
        anio: parseInt(fila.anio, 10),
        obligacion: String(fila.obligacion),
        puntoInspeccion: String(fila.puntoInspeccion),
        resena: ""
      }))

      await axiosClient.post("/api/matriz-legal", datosParaEnviar)

      setSaveMessage("¡Nuevas filas guardadas exitosamente!")
      setTimeout(() => setSaveMessage(""), 3000)

      const res = await axiosClient.get("/api/matriz-legal");
      const filasActualizadas = res.data.map(f => ({ ...f, esNuevo: false }));
      setFilas(filasActualizadas);

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
              <div className={styles.formGroup}>
                <label>Ámbito</label>
                <select name="idAmbito" value={form.idAmbito} onChange={handleChange}>
                  <option value="">Seleccione...</option>
                  {Array.isArray(ambitos) &&
                    ambitos.map((a) => (
                      <option key={a.idAmbito} value={a.idAmbito}>
                        {a.ambito}
                      </option>
                    ))}
                </select>
              </div>

              <div className={styles.formGroup}>
                <label>Tipo</label>
                <select name="idTipo" value={form.idTipo} onChange={handleChange}>
                  <option value="">Seleccione...</option>
                  {Array.isArray(tipos) &&
                    tipos.map((t) => (
                      <option key={t.idTipo} value={t.idTipo}>
                        {t.tipo}
                      </option>
                    ))}
                </select>
              </div>

              <div className={styles.formGroup}>
                <label>Nro</label>
                <input type="text" name="nro" value={form.nro} onChange={handleChange} placeholder="Número" />
              </div>

              <div className={styles.formGroup}>
                <label>Año</label>
                <input type="text" name="anio" value={form.anio} onChange={handleChange} placeholder="2024" />
              </div>

              <div className={styles.formGroup}>
                <label>Aspecto Ambiental</label>
                <select name="idAspectoAmbientalTema" value={form.idAspectoAmbientalTema} onChange={handleChange}>
                  <option value="">Seleccione...</option>
                  {Array.isArray(aspectos) &&
                    aspectos.map((a) => (
                      <option key={a.idAspectoAmbientalTema} value={a.idAspectoAmbientalTema}>
                        {a.aspectoAmbientalTema}
                      </option>
                    ))}
                </select>
              </div>

              <div className={styles.formGroup}>
                <label>Reseña</label>
                <select name="resena" value={form.resena} onChange={handleChange}>
                  <option value="">Seleccione...</option>
                  {Array.isArray(aspectos) &&
                    aspectos.map((a) => (
                      <option key={a.idAspectoAmbientalTema} value={a.idAspectoAmbientalTema}>
                        {a.aspectoAmbientalTema}
                      </option>
                    ))}
                </select>
              </div>

              <div className={styles.formGroup}>
                <label>
                  Obligación Concreta
                  <span className={styles.tooltip}>
                    <Info size={16} className={styles.tooltipIcon} />
                    <span className={styles.tooltiptext}>
                      Revisa esta norma e identifica las obligaciones que genera en tu organización
                    </span>
                  </span>
                </label>
                <input
                  type="text"
                  name="obligacion"
                  value={form.obligacion}
                  onChange={handleChange}
                  placeholder="Describe la obligación"
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
                  {Array.isArray(resultados) &&
                    resultados.map((r) => (
                      <option key={r.idResultado} value={r.idResultado}>
                        {r.resultado}
                      </option>
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
                    <th>Aspecto Ambiental</th>
                    <th>Obligación Concreta</th>
                    <th>Punto de Control</th>
                    <th>Resultado</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {filas.length === 0 ? (
                    <tr>
                      <td colSpan="9" className={styles.emptyState}>
                        <FileText size={48} />
                        <p>No hay normativas agregadas</p>
                        <span>Completa el formulario para añadir la primera</span>
                      </td>
                    </tr>
                  ) : (
                    filas.map((fila) => (
                      <tr key={fila.id}>
                        <td>
                          {fila.ambito}
                        </td>
                        <td>{fila.tipo}</td>
                        <td>{fila.numero}</td>
                        <td>{fila.anio}</td>
                        <td>
                          {fila.aspecto}
                        </td>
                        <td>{fila.obligacion}</td>
                        <td>{fila.puntoInspeccion}</td>
                        <td>
                          <span className={styles.resultadoBadge}>
                            {fila.resultado}
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
