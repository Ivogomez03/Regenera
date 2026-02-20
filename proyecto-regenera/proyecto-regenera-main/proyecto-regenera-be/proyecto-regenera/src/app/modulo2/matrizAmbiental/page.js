'use client';

import styles from "./matrizAmbiental.module.css";
import Link from 'next/link';
import Image from 'next/image';
import { useState, useEffect } from "react"
import { FileText, Plus, Save, Check, Trash2, Building2, Calendar, Info, Printer } from "lucide-react"
//import axios from "axios";
import Select from "react-select";
import "./matrizAmbiental.css";
import axiosClient from "@/app/lib/axiosClient"
import { useSearchParams } from 'next/navigation'


export default function MatrizAmbientalPage() {
  const searchParams = useSearchParams()
  const formularioId = searchParams.get('id');

  const [logoPreview, setLogoPreview] = useState(null);
  const [logoBase64, setLogoBase64] = useState("");

  // Estados de opciones para Selects
  const [sectores, setSectores] = useState([]);
  const [actividades, setActividades] = useState([]);
  const [aspectos, setAspectos] = useState([]);
  const [tipoImpactos, setTipoImpactos] = useState([]);
  const [condiciones, setCondiciones] = useState([]);
  const [requisitos, setRequisitos] = useState([]);
  const [impactosAmbientales, setImpactosAmbientales] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [resumidos, setResumidos] = useState([]);

  const [isLoading, setIsLoading] = useState(true);
  const [isClient, setIsClient] = useState(false);

  // Estados de selección
  const [sectorSeleccionado, setSectorSeleccionado] = useState(null);
  const [actividadSeleccionada, setActividadSeleccionada] = useState(null);
  const [aspectoSeleccionado, setAspectoSeleccionado] = useState(null);
  const [tipoImpactoSeleccionado, setTipoImpactoSeleccionado] = useState(null);
  const [condicionSeleccionada, setCondicionSeleccionada] = useState(null);
  const [requisitoSeleccionado, setRequisitoSeleccionado] = useState(null);
  const [impactoAmbientalSeleccionado, setImpactoAmbientalSeleccionado] = useState(null);
  const [categoriaSeleccionada, setCategoriaSeleccionada] = useState(null);

  // Valores numéricos y texto
  const [valueReversibilidad, setValueReversibilidad] = useState('');
  const [valueSeveridad, setValueSeveridad] = useState('');
  const [valueMagnitud, setValueMagnitud] = useState('');
  const [valueFrecuencia, setValueFrecuencia] = useState('');

  const [valueControl, setValueControl] = useState('');
  const [valueObservaciones, setValueObservaciones] = useState('');

  // Inputs para agregar opciones nuevas
  const [inputSector, setInputSector] = useState('');
  const [inputActividad, setInputActividad] = useState('');
  const [inputAspecto, setInputAspecto] = useState('');
  const [inputTipoImpacto, setInputTipoImpacto] = useState('');
  const [inputCondicion, setInputCondicion] = useState('');
  const [inputRequisito, setInputRequisito] = useState('');

  // Tabla y formularios
  const [filasTabla, setFilasTabla] = useState([]);
  const [tieneCambios, setTieneCambios] = useState(false);

  //const [sectorActual, setSectorActual] = useState(null);
  //const [formulariosPorSector, setFormulariosPorSector] = useState({});

  const [nombreEmpresa, setNombreEmpresa] = useState('');
  const [codigoFormulario, setCodigoFormulario] = useState('');
  const [fechaFormulario, setFechaFormulario] = useState('');
  const [logoFormulario, setLogoFormulario] = useState('');
  const [idFormularioActual, setIdFormularioActual] = useState(null);

  const [isGuardando, setIsGuardando] = useState(false) // Added for save state
  const [saveSuccess, setSaveSuccess] = useState(false)
  const [mensajeGuardado, setMensajeGuardado] = useState("") // Added for save messages

  const [firmantes, setFirmantes] = useState({
    elaboro: { nombre: '', apellido: '', puesto: '', firma: '', aclaracion: '', fecha: '' },
    reviso: { nombre: '', apellido: '', puesto: '', firma: '', aclaracion: '', fecha: '' },
    aprobo: { nombre: '', apellido: '', puesto: '', firma: '', aclaracion: '', fecha: '' }
  });

  useEffect(() => {
    setIsClient(true);
  }, []);



  const cargarDatosUsuario = async () => {
    setIsLoading(true);
    try {
      const resGrillas = await axiosClient.get("/api/grilla");

      if (Array.isArray(resGrillas.data) && resGrillas.data.length > 0) {

        const filasMapeadas = resGrillas.data.map((item) => ({
          // IMPORTANTE: Usamos 'idItem' que viene de la BD para la key de React
          id: item.idItem,
          codigoFormulario: item.codigoFormulario,
          esNuevo: false, // Al venir de la BD, ya no es nuevo
          sector: item.idSector,

          // Mapeo para los Selects (value/label)
          // Asegúrate que tu DTO en Java devuelva estos nombres (actividad, aspectoAmbiental, etc.)
          // Si el DTO devuelve null en los nombres, ponemos "..." para que no falle
          actividad: {
            value: item.idActividad,
            label: item.actividad || "..."
          },
          aspecto: {
            value: item.idAspectoAmbiental,
            label: item.aspectoAmbiental || "..."
          },
          impacto: {
            value: item.idImpactoAmbiental,
            label: item.impactoAmbiental || "..."
          },
          tipoImpacto: {
            value: item.idTipoImpacto,
            label: item.tipoImpacto || "..."
          },
          condicion: {
            value: item.idCondicionImpacto,
            label: item.condicionImpacto || "..."
          },
          requisito: {
            value: item.idRequisitoLegalAsociado,
            label: item.requisitoLegalAsociado || "..."
          },

          // Datos numéricos y texto
          severidad: item.severidad,
          magnitud: item.magnitud,
          frecuencia: item.frecuencia,
          reversibilidad: item.reversibilidad,
          valoracion: item.valoracion,
          significancia: item.valoracion > 15 ? 'Impacto significativo' : 'Impacto no significativo',
          control: item.control,
          observaciones: item.observaciones,

          // Guardamos referencia al formulario padre por si acaso
          idFormulario: item.idFormulario
        }));

        setFilasTabla(filasMapeadas);

        // Si cargamos datos, recuperamos el ID del formulario actual para seguir trabajando sobre él
        if (filasMapeadas.length > 0) {
          setIdFormularioActual(resGrillas.data[0].idFormulario);
        }
      }
    } catch (error) {
      console.error("Error cargando grillas del usuario:", error);
    } finally {
      setIsLoading(false);
    }
  };


  useEffect(() => {
    cargarDatosUsuario();
  }, []);

  // CARGA INICIAL DE DATOS
  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true);
      try {
        // 1. Cargar las opciones (Solo para los Selects de "Agregar Nueva Fila")
        const [sectoresRes, actividadesRes, aspectosRes, tiposRes, condicionesRes, requisitosRes, impactosRes, categoriasRes] = await Promise.all([
          axiosClient.get("/api/sectores"),
          axiosClient.get("/api/actividades"),
          axiosClient.get("/api/aspectos-ambientales"),
          axiosClient.get("/api/tipos-impacto"),
          axiosClient.get("/api/condiciones-impacto"),
          axiosClient.get("/api/requisitos-legales-asociados"),
          axiosClient.get("/api/impactos-ambientales"),
          axiosClient.get("/api/categorias-aspecto-ambiental"),
        ]);

        // Llenamos los selects
        setSectores(sectoresRes.data.map(i => ({ value: i.idSector, label: i.sector })));
        setActividades(actividadesRes.data.map(i => ({ value: i.idActividad, label: i.actividad })));
        setAspectos(aspectosRes.data.map(i => ({ value: i.idAspectoAmbiental, label: i.aspectoAmbiental })));
        setTipoImpactos(tiposRes.data.map(i => ({ value: i.idTipoImpacto, label: i.tipoImpacto })));
        setCondiciones(condicionesRes.data.map(i => ({ value: i.idCondicionImpacto, label: i.condicionImpacto })));
        setRequisitos(requisitosRes.data.map(i => ({ value: i.idRequisitoLegalAsociado, label: i.requisitoLegalAsociado })));
        setImpactosAmbientales(impactosRes.data.map(i => ({ value: i.idImpactoAmbiental, label: i.resumido ? i.resumido : i.impactoAmbiental })));
        setCategorias(categoriasRes.data.map(i => ({ value: i.id, label: i.categoriaAspectoAmbiental })));

        console.log("Opciones cargadas. Recuperando datos del usuario...");

        setTieneCambios(false);
      }
      catch (error) {
        console.error("Error al cargar:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, []);



  const handleLogoChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setLogoPreview(URL.createObjectURL(file));
      const reader = new FileReader();
      reader.onloadend = () => {
        const base64String = reader.result;
        const rawBase64 = base64String.includes(',') ? base64String.split(',')[1] : base64String;
        setLogoBase64(rawBase64);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleFirmanteChange = (rol, campo, valor) => {
    setFirmantes(prev => ({
      ...prev,
      [rol]: { ...prev[rol], [campo]: valor }
    }));
    setTieneCambios(true);
  };

  function normalizeText(text) {
    return text
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .trim();
  }

  /*

  const handleAgregarOpcion = async (e, {
    inputValue,
    opciones,
    setOpciones,
    setSelected,
    setInput,
    endpoint,
    idField,
    labelField,
    extraFields = {}
  }) => {
    if (e) e.preventDefault();
    const texto = inputValue.trim();
    if (!texto) return;

    const yaExiste = opciones.some(
      (o) => normalizeText(o.label) === normalizeText(texto)
    );
    if (yaExiste) {
      alert("Ese valor ya existe.");
      setInput("");
      return;
    }

    try {

      if (endpoint === '/api/actividades') {
        if (!sectorSeleccionado) {
          alert("Debe seleccionar un sector antes de agregar una actividad.");
          return;
        }
      }

      if (endpoint === '/api/aspectos-ambientales') {
        if (!impactoAmbientalSeleccionado || !categoriaSeleccionada) {
          alert("Debe seleccionar un impacto ambiental y una categoría antes de agregar un aspecto ambiental.");
          return;
        }
      }


      const body = {
        [labelField]: texto,
        ...extraFields
      };

      console.log("POST a", endpoint, "con body:", body);
      const res = await axiosClient.post(endpoint, body);

      const nuevo = res.data

      const nuevaOpcion = {
        value: nuevo[idField],
        label: nuevo[labelField]
      };

      const actualizadas = [...opciones, nuevaOpcion];
      setOpciones(actualizadas);
      setSelected(nuevaOpcion);

      setInput("");

      alert("Opción agregada correctamente.");

    } catch (err) {
      console.error("Error al guardar:", err);
      alert("Ocurrió un error al guardar.");
    }
  };



  /*LIMITAR INPUT NUMERICO ENTRE 0 Y 10*/
  const handleChange = (e, setValor) => {
    let val = e.target.value;

    // Permitir campo vacío
    if (val === '') {
      setValor('');
      return;
    }

    val = parseInt(val, 10);

    // Limitar entre 0 y 10
    if (val >= 0 && val <= 10) {
      setValor(val);
    } else if (val > 10) {
      setValor(10);
    } else if (val < 0) {
      setValor(0);
    }
  };

  const handleAñadirFila = async () => {
    // Validación 
    if (
      !sectorSeleccionado ||
      !actividadSeleccionada ||
      !aspectoSeleccionado ||
      !impactoAmbientalSeleccionado ||
      !tipoImpactoSeleccionado ||
      !categoriaSeleccionada ||
      !condicionSeleccionada ||
      valueSeveridad === '' ||
      valueMagnitud === '' ||
      valueFrecuencia === '' ||
      valueReversibilidad === '' ||
      !requisitoSeleccionado ||
      !valueControl?.trim() ||
      !valueObservaciones?.trim()
    ) {
      alert('Por favor, complete todos los campos obligatorios antes de añadir una fila.');
      return;
    }

    const severidad = parseInt(valueSeveridad, 10);
    const magnitud = parseInt(valueMagnitud, 10);
    const frecuencia = parseInt(valueFrecuencia, 10);
    const reversibilidad = parseInt(valueReversibilidad, 10);

    const valoracionCalculada = severidad + magnitud + frecuencia + reversibilidad;

    const significanciaCalculada = valoracionCalculada <= 15 ? 'Impacto no significativo' : 'Impacto significativo';

    const nuevaFila = {
      //id: filasTabla.length + 1,
      id: Date.now(),
      codigoFormulario: codigoFormulario,
      esNuevo: true,
      sector: sectorSeleccionado?.value,
      sectorNombre: sectorSeleccionado?.label,
      actividad: {
        value: actividadSeleccionada?.value,
        label: actividadSeleccionada?.label
      },
      aspecto: {
        value: aspectoSeleccionado?.value,
        label: aspectoSeleccionado?.label
      },
      impacto: {
        value: impactoAmbientalSeleccionado?.value,
        label: impactoAmbientalSeleccionado?.label ?? ''
      },
      tipoImpacto: {
        value: tipoImpactoSeleccionado?.value,
        label: tipoImpactoSeleccionado?.label ?? ''
      },
      condicion: {
        value: condicionSeleccionada?.value,
        label: condicionSeleccionada?.label ?? ''
      },
      severidad: valueSeveridad,
      magnitud: valueMagnitud,
      frecuencia: valueFrecuencia,
      reversibilidad: valueReversibilidad,
      valoracion: valoracionCalculada,
      significancia: significanciaCalculada,
      requisito: {
        value: requisitoSeleccionado?.value,
        label: requisitoSeleccionado?.label ?? ''
      },
      control: valueControl,
      observaciones: valueObservaciones,
    };

    setFilasTabla(prev => [...prev, nuevaFila]);
    setTieneCambios(true);

    setActividadSeleccionada(null);
    setAspectoSeleccionado(null);
    setImpactoAmbientalSeleccionado(null);
    setTipoImpactoSeleccionado(null);
    setCondicionSeleccionada(null);
    setRequisitoSeleccionado(null);

    setCategoriaSeleccionada(null);

    setValueSeveridad('');
    setValueMagnitud('');
    setValueFrecuencia('');
    setValueReversibilidad('');
    setValueControl('');
    setValueObservaciones('');
  };

  // Eliminar fila
  const handleEliminarFila = async (id) => {
    if (!confirm("¿Estás seguro de eliminar esta fila?")) return;

    // Buscamos la fila para saber si es nueva o si ya existe en la BDD
    const filaAEliminar = filasTabla.find(f => f.id === id);

    //Es una fila nueva que aún no se guardó en la BDD 
    if (filaAEliminar && filaAEliminar.esNuevo) {
      setFilasTabla(prev => prev.filter(fila => fila.id !== id));
      setTieneCambios(true);
      return;
    }

    // Es una fila que ya existe en la Base de Datos
    try {
      setIsLoading(true);
      await axiosClient.delete(`/api/grilla/items/${id}`);

      // Si la API responde OK, eliminamos de la vista
      setFilasTabla(prev => prev.filter(fila => fila.id !== id));
      alert("Fila eliminada correctamente.");
    } catch (error) {
      console.error("Error eliminando fila:", error);
      alert("Error al eliminar la fila de la base de datos.");
    } finally {
      setIsLoading(false);
    }
  };

  // Filas
  const filasFiltradas = filasTabla;



  const handlePrint = () => {
    window.print()
  }

  const handleGuardar = async () => {
    // Validaciones
    if (!nombreEmpresa || !codigoFormulario || !fechaFormulario) {
      alert("Faltan datos de cabecera (Empresa, Código o Fecha).");
      return;
    }
    if (filasTabla.length === 0) {
      alert("La matriz está vacía. Agregue al menos una fila.");
      return;
    }

    setIsGuardando(true);
    setMensajeGuardado("");
    setSaveSuccess(false);

    try {
      // Definimos el payload de la cabecera (se usa tanto para crear como para actualizar)
      const formPayload = {
        idSector: sectorSeleccionado ? sectorSeleccionado.value : null,
        nombreEmpresa: nombreEmpresa,
        codigo: codigoFormulario,
        fecha: fechaFormulario,
        items: [], // Lista vacía por contrato
        logoEmpresa: logoBase64 || ""
      };

      let idFinal = idFormularioActual;


      if (!idFinal) {
        // Si NO existe ID en el estado, creamos uno nuevo
        const resForm = await axiosClient.post('/api/formularios', formPayload);
        idFinal = resForm.data.idFormulario; // Recuperamos el ID nuevo

        if (!idFinal) throw new Error("El backend no devolvió un ID de formulario.");

        setIdFormularioActual(idFinal); // Actualizamos el estado para la próxima
      } else {
        // Si ya existe, asumimos que es ese 
        console.log("Usando formulario existente ID:", idFinal);
      }

      // Guardar Filas
      const promesasGrilla = filasTabla.map(fila => {
        const itemPayload = {
          idSector: fila.sector,
          idActividad: fila.actividad?.value,
          idAspectoAmbiental: fila.aspecto?.value,
          idImpactoAmbiental: fila.impacto?.value,
          idTipoImpacto: fila.tipoImpacto?.value,
          idCondicionImpacto: fila.condicion?.value,

          severidad: parseInt(fila.severidad),
          magnitud: parseInt(fila.magnitud),
          frecuencia: parseInt(fila.frecuencia),
          reversibilidad: parseInt(fila.reversibilidad),
          idRequisitoLegalAsociado: fila.requisito?.value,
          control: fila.control,
          observaciones: fila.observaciones
        };

        // ¿Es nuevo o existente?
        if (fila.esNuevo) {
          // SI ES NUEVO: Usamos POST para crear
          return axiosClient.post(`/api/grilla/formularios/${idFinal}/items`, itemPayload);
        } else {
          // SI YA EXISTE: Usamos PUT para actualizar (usando el ID de la fila)
          return axiosClient.put(`/api/grilla/items/${fila.id}`, itemPayload);
        }
      });

      // Guardar Firmas
      const fechaActual = new Date().toISOString();
      const firmasPayload = {
        nombreElabore: firmantes.elaboro.nombre,
        apellidoElabore: firmantes.elaboro.apellido,
        puestoElabore: firmantes.elaboro.puesto,
        firmaElabore: firmantes.elaboro.firma,
        aclaracionElabore: firmantes.elaboro.aclaracion,
        fechaElabore: firmantes.elaboro.nombre ? fechaActual : null,

        nombreReviso: firmantes.reviso.nombre,
        apellidoReviso: firmantes.reviso.apellido,
        puestoReviso: firmantes.reviso.puesto,
        firmaReviso: firmantes.reviso.firma,
        aclaracionReviso: firmantes.reviso.aclaracion,
        fechaReviso: firmantes.reviso.nombre ? fechaActual : null,

        nombreAprobo: firmantes.aprobo.nombre,
        apellidoAprobo: firmantes.aprobo.apellido,
        puestoAprobo: firmantes.aprobo.puesto,
        firmaAprobo: firmantes.aprobo.firma,
        aclaracionAprobo: firmantes.aprobo.aclaracion,
        fechaAprobo: firmantes.aprobo.nombre ? fechaActual : null
      };

      const promesaFirmas = axiosClient.post(`/api/firmas/formularios/${idFinal}`, firmasPayload);

      // Ejecutar todo en paralelo
      await Promise.all([...promesasGrilla, promesaFirmas]);

      setSaveSuccess(true);
      setMensajeGuardado("Matriz y firmas guardadas correctamente.");

      await cargarDatosUsuario();

      setTieneCambios(false);

    } catch (error) {
      console.error("Error al guardar:", error);
      const errorMsg = error.response?.data?.message || error.message || "Error desconocido";
      setMensajeGuardado("Error: " + errorMsg);
    } finally {
      setIsGuardando(false);
      setTimeout(() => {
        setSaveSuccess(false);
        setMensajeGuardado("");
      }, 3000);
    }
  };


  return (
    <main className={styles.main}>
      <div className={styles.backgroundPattern}></div>

      <header className={styles.topHeader}>
        <Link href="/">
          <Image src="/logo2.png" alt="Regenera Logo" width={140} height={60} className={styles.logo} priority />
        </Link>
      </header>

      <div className={styles.matrizPage}>
        <div className={styles.container}>
          {/* Page Header */}
          <div className={styles.pageHeader}>
            <div className={styles.headerIcon}>
              <FileText size={40} />
            </div>
            <h1>Matriz de Aspectos e Impactos Ambientales</h1>
            <p>Gestión y evaluación de aspectos ambientales por sector</p>
          </div>

          {/* Identification Section */}
          <div className={styles.formSection}>
            <div className={styles.sectionHeader}>
              <Building2 size={24} />
              <h2>IDENTIFICACIÓN DE ASPECTOS E IMPACTOS AMBIENTALES</h2>
            </div>

            <div className={styles.identificationHeader}>
              <div className={styles.logoUploadArea}>
                <label htmlFor="logoUpload" className={styles.logoUploadLabel}>
                  {logoPreview ? (

                    <img
                      src={logoPreview}
                      alt="Logo empresa"
                      className={styles.uploadedLogo}

                    />
                  ) : (
                    <div className={styles.logoPlaceholder}>
                      <Building2 size={48} />
                      <span>Cargar Logo</span>
                    </div>
                  )}
                </label>
                <input
                  id="logoUpload"
                  type="file"
                  accept="image/*"
                  onChange={handleLogoChange}
                  className={styles.hiddenFileInput}
                />
              </div>

              <div className={styles.identificationFields}>
                <div className={styles.formGroup}>
                  <label>Nombre Empresa</label>
                  <input
                    type="text"
                    value={nombreEmpresa}
                    onChange={(e) => setNombreEmpresa(e.target.value)}
                    placeholder="Ingrese nombre de la empresa"
                  />
                </div>
                <div className={styles.formRow}>
                  <div className={styles.formGroup}>
                    <label>Código Formulario</label>
                    <input
                      type="text"
                      value={codigoFormulario}
                      onChange={(e) => setCodigoFormulario(e.target.value)}
                      placeholder="Ej: MAI-001"
                    />
                  </div>
                  <div className={styles.formGroup}>
                    <label>
                      <Calendar size={16} />
                      Fecha
                    </label>
                    <input type="date" value={fechaFormulario} onChange={(e) => setFechaFormulario(e.target.value)} />
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Form Section for Adding Rows */}
          <div className={styles.formSection}>
            <div className={styles.sectionHeader}>
              <FileText size={24} />
              <h2>Identificación de Aspectos e Impactos Ambientales</h2>
            </div>

            <div className={styles.formGrid}>
              {/* Sector */}
              <div className={styles.formGroup}>
                <label>
                  Sector
                  <span className={styles.tooltip}>
                    <Info size={16} className={styles.tooltipIcon} />
                    <span className={styles.tooltiptext}>
                      Seleccione el sector para el cual desea registrar aspectos e impactos ambientales. Los datos se
                      cargarán y guardarán específicamente para este sector.
                    </span>
                  </span>
                </label>
                <div className={styles.inputWithButton}>
                  {isClient && sectores.length > 0 ? (
                    <Select
                      options={sectores}
                      placeholder="Seleccione..."
                      isSearchable
                      classNamePrefix="rs"
                      inputValue={inputSector}
                      onInputChange={(value, { action }) => {
                        if (action === "input-change") {
                          setInputSector(value)
                        }
                      }}
                      onChange={(selected) => {
                        setSectorSeleccionado(selected)
                      }}
                      value={sectorSeleccionado}
                    />
                  ) : (
                    <select disabled>
                      <option>Cargando...</option>
                    </select>
                  )}

                </div>
              </div>

              {/* Actividad */}
              <div className={styles.formGroup}>
                <label>Actividad</label>
                <div className={styles.inputWithButton}>
                  {isClient && actividades.length > 0 ? (
                    <Select
                      options={actividades}
                      placeholder="Seleccione..."
                      isSearchable
                      classNamePrefix="rs"
                      inputValue={inputActividad}
                      onInputChange={(value, { action }) => {
                        if (action === "input-change") {
                          setInputActividad(value)
                        }
                      }}
                      onChange={(selected) => {
                        setActividadSeleccionada(selected)
                      }}
                      value={actividadSeleccionada}
                    />
                  ) : (
                    <select disabled>
                      <option>Cargando...</option>
                    </select>
                  )}

                </div>
              </div>

              {/* Impacto Ambiental */}
              <div className={styles.formGroup}>
                <label>Impacto Ambiental</label>
                <div className={styles.inputWithButton}>
                  {isClient && impactosAmbientales.length > 0 ? (
                    <Select
                      options={impactosAmbientales}
                      placeholder="Seleccione..."
                      isSearchable
                      classNamePrefix="rs"
                      onChange={(selected) => {
                        setImpactoAmbientalSeleccionado(selected)
                      }}
                      value={impactoAmbientalSeleccionado}
                    />
                  ) : (
                    <select disabled>
                      <option>Cargando...</option>
                    </select>
                  )}
                </div>
              </div>

              {/* Categoría */}
              <div className={styles.formGroup}>
                <label>Categoría</label>
                <div className={styles.inputWithButton}>
                  {isClient && categorias.length > 0 ? (
                    <Select
                      options={categorias}
                      placeholder="Seleccione..."
                      isSearchable
                      classNamePrefix="rs"
                      onChange={(selected) => {
                        setCategoriaSeleccionada(selected)
                      }}
                      value={categoriaSeleccionada}
                    />
                  ) : (
                    <select disabled>
                      <option>Cargando...</option>
                    </select>
                  )}
                </div>
              </div>

              {/* Aspecto Ambiental */}
              <div className={styles.formGroup}>
                <label>Aspecto Ambiental</label>
                <div className={styles.inputWithButton}>
                  {isClient && aspectos.length > 0 ? (
                    <Select
                      options={aspectos}
                      placeholder="Seleccione..."
                      isSearchable
                      classNamePrefix="rs"
                      inputValue={inputAspecto}
                      onInputChange={(value, { action }) => {
                        if (action === "input-change") {
                          setInputAspecto(value)
                        }
                      }}
                      onChange={(selected) => {
                        setAspectoSeleccionado(selected)
                      }}
                      value={aspectoSeleccionado}
                    />
                  ) : (
                    <select disabled>
                      <option>Cargando...</option>
                    </select>
                  )}

                </div>
              </div>

              {/* Tipo Impacto */}
              <div className={styles.formGroup}>
                <label>Tipo Impacto</label>
                <div className={styles.inputWithButton}>
                  {isClient && tipoImpactos.length > 0 ? (
                    <Select
                      options={tipoImpactos}
                      placeholder="Seleccione..."
                      isSearchable
                      classNamePrefix="rs"
                      inputValue={inputTipoImpacto}
                      onInputChange={(value, { action }) => {
                        if (action === "input-change") {
                          setInputTipoImpacto(value)
                        }
                      }}
                      onChange={(selected) => {
                        setTipoImpactoSeleccionado(selected)
                      }}
                      value={tipoImpactoSeleccionado}
                    />
                  ) : (
                    <select disabled>
                      <option>Cargando...</option>
                    </select>
                  )}

                </div>
              </div>

              {/* Condición */}
              <div className={styles.formGroup}>
                <label>Condición</label>
                <div className={styles.inputWithButton}>
                  {isClient && condiciones.length > 0 ? (
                    <Select
                      options={condiciones}
                      placeholder="Seleccione..."
                      isSearchable
                      classNamePrefix="rs"
                      inputValue={inputCondicion}
                      onInputChange={(value, { action }) => {
                        if (action === "input-change") {
                          setInputCondicion(value)
                        }
                      }}
                      onChange={(selected) => {
                        setCondicionSeleccionada(selected)
                      }}
                      value={condicionSeleccionada}
                    />
                  ) : (
                    <select disabled>
                      <option>Cargando...</option>
                    </select>
                  )}

                </div>
              </div>

              {/* Numeric Fields */}
              <div className={styles.formGroup}>
                <label>Severidad (0-10)
                  <span className={styles.tooltip}>
                    <Info size={16} className={styles.tooltipIcon} />
                    <span className={styles.tooltiptext}>
                      <p><strong>- Baja (1-3):</strong> Bajo grado de afectación del impacto sobre la variable en consideración,
                        la condición basal del medio se mantiene.</p>
                      <p><strong>- Media (4-6):</strong> Mediano grado de afectación del impacto sobre la variable, implica cambios
                        respecto a la condición basal pero dentro de rangos aceptables.</p>
                      <p><strong>- Alta (7-10):</strong> Alto grado de afectación del impacto sobre la variable, alteración
                        significativa de la condición basal y en algunos casos inaceptables.</p>
                    </span>
                  </span>
                </label>
                <input
                  type="number"
                  min="0"
                  max="10"
                  value={valueSeveridad}
                  onChange={(e) => handleChange(e, setValueSeveridad)}
                  placeholder="0-10"
                />
              </div>

              <div className={styles.formGroup}>
                <label>Magnitud (0-10)
                  <span className={styles.tooltip}>
                    <Info size={16} className={styles.tooltipIcon} />
                    <span className={styles.tooltiptext}>
                      <p><strong>- Puntual (1-3):</strong> Supone una incidencia puntual en el área estudiada, el impacto se manifiesta
                        en el sector donde se ubica la fuente</p>
                      <p><strong>- Dispersa (4-6):</strong> Se detecta en una gran parte del territorio.</p>
                      <p><strong>- Extendida (7-10):</strong> Se manifiesta de manera generalizada en todo el entorno considerado e incluso
                        fuera del entorno de la fuente.</p>
                    </span>
                  </span>
                </label>
                <input
                  type="number"
                  min="0"
                  max="10"
                  value={valueMagnitud}
                  onChange={(e) => handleChange(e, setValueMagnitud)}
                  placeholder="0-10"
                />
              </div>

              <div className={styles.formGroup}>
                <label>Frecuencia (0-10)
                  <span className={styles.tooltip}>
                    <Info size={16} className={styles.tooltipIcon} />
                    <span className={styles.tooltiptext}>
                      <p><strong>- Corta duración (1-3):</strong> Se presenta en forma intermitente o continua, pero con un plazo limitado
                        de manifestación que puede determinarse (hasta dos años aprox)</p>
                      <p><strong>- Media Duración (4-6):</strong> Se extiende en el tiempo y luego de un tiempo finaliza la acción que lo genera
                        (dos a cinco años aprox)</p>
                      <p><strong>- Larga Duración (7-10):</strong> Aquél que supone una alteración indefinida o muy alta duración en el tiempo
                        (por más de cinco años)</p>
                    </span>
                  </span>
                </label>
                <input
                  type="number"
                  min="0"
                  max="10"
                  value={valueFrecuencia}
                  onChange={(e) => handleChange(e, setValueFrecuencia)}
                  placeholder="0-10"
                />
              </div>

              <div className={styles.formGroup}>
                <label>Reversibilidad (0-10)
                  <span className={styles.tooltip}>
                    <Info size={16} className={styles.tooltipIcon} />
                    <span className={styles.tooltiptext}>
                      <p><strong>- Reversible (1-5):</strong> La afectación puede ser asimilada por el entorno de forma medible, a corto,
                        mediano o largo plazo, debido al funcionamiento de los procesos naturales.</p>
                      <p><strong>- Irreversible (6-10):</strong> Supone la imposibilidad o dificultad extrema de retomar, por medios naturales
                        a la condición inicial por la acción que lo produce</p>
                    </span>
                  </span>
                </label>
                <input
                  type="number"
                  min="0"
                  max="10"
                  value={valueReversibilidad}
                  onChange={(e) => handleChange(e, setValueReversibilidad)}
                  placeholder="0-10"
                />
              </div>

              {/* Requisito Legal */}
              <div className={styles.formGroup}>
                <label>Requisito Legal Asociado</label>
                <div className={styles.inputWithButton}>
                  {isClient && requisitos.length > 0 ? (
                    <Select
                      options={requisitos}
                      placeholder="Seleccione..."
                      isSearchable
                      classNamePrefix="rs"
                      inputValue={inputRequisito}
                      onInputChange={(value, { action }) => {
                        if (action === "input-change") {
                          setInputRequisito(value)
                        }
                      }}
                      onChange={(selected) => {
                        setRequisitoSeleccionado(selected)
                      }}
                      value={requisitoSeleccionado}
                    />
                  ) : (
                    <select disabled>
                      <option>Cargando...</option>
                    </select>
                  )}

                </div>
              </div>

              {/* Control & Observaciones */}
              <div className={styles.formGroup}>
                <label>Control</label>
                <input
                  type="text"
                  value={valueControl}
                  onChange={(e) => setValueControl(e.target.value)}
                  placeholder="Descripción del control"
                />
              </div>

              <div className={styles.formGroup}>
                <label>Observaciones</label>
                <input
                  type="text"
                  value={valueObservaciones}
                  onChange={(e) => setValueObservaciones(e.target.value)}
                  placeholder="Observaciones adicionales"
                />
              </div>
            </div>

            <button type="button" onClick={handleAñadirFila} className={styles.addButton}>
              <Plus size={20} />
              Añadir Fila
            </button>
          </div>

          {/* Matrix Table */}
          <div className={styles.tableSection}>
            <div className={styles.tableHeader}>
              <h3>Matriz de Aspectos e Impactos</h3>
            </div>
            <div className={styles.tableWrapper}>
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th rowSpan={3}>N°</th>
                    <th rowSpan={3}>Código del Formulario</th>
                    <th rowSpan={3}>Actividad</th>
                    <th rowSpan={3}>Aspecto ambiental</th>
                    <th rowSpan={3}>Impacto Ambiental</th>
                    <th rowSpan={3}>Tipo de Impacto</th>
                    <th rowSpan={3}>Condición</th>
                    <th colSpan={7} className="text-center">
                      Evaluación
                    </th>
                    <th rowSpan={3}>Control</th>
                    <th rowSpan={3}>Observaciones</th>
                    <th rowSpan={3}>Acciones</th>
                  </tr>
                  <tr>
                    <th rowSpan={1} className={styles.verticalText}>
                      Severidad
                    </th>
                    <th rowSpan={1} className={styles.verticalText}>
                      Magnitud
                    </th>
                    <th rowSpan={1} className={styles.verticalText}>
                      Frecuencia
                    </th>
                    <th rowSpan={1} className={styles.verticalText}>
                      Reversibilidad
                    </th>
                    <th rowSpan={1} className={styles.verticalText}>
                      Valoración
                    </th>
                    <th rowSpan={2}>Significancia del impacto</th>
                    <th rowSpan={2}>Requisito legal</th>
                  </tr>
                  <tr>
                    <th>S</th>
                    <th>M</th>
                    <th>F</th>
                    <th>R</th>
                    <th>V</th>
                  </tr>
                </thead>
                <tbody>
                  {filasFiltradas.length === 0 ? (
                    <tr>
                      <td colSpan={16} className={styles.emptyState}>
                        <FileText size={48} />
                        <p>No hay filas en la matriz</p>
                        <span>Agregue filas usando el formulario superior</span>
                      </td>
                    </tr>
                  ) : (
                    filasFiltradas.map((fila, index) => (
                      <tr key={fila.id}>
                        <td>{index + 1}</td>
                        <td>{fila.codigoFormulario}</td>
                        <td>{fila.actividad.label}</td>
                        <td>{fila.aspecto.label}</td>
                        <td>{fila.impacto.label}</td>
                        <td>{fila.tipoImpacto.label}</td>
                        <td>{fila.condicion.label}</td>
                        <td>{fila.severidad}</td>
                        <td>{fila.magnitud}</td>
                        <td>{fila.frecuencia}</td>
                        <td>{fila.reversibilidad}</td>
                        <td>{fila.valoracion}</td>
                        <td>

                          <span className={styles[fila.significancia === "Impacto significativo" ? "resultadoBadge" : "noResultadoBadge"]}>{fila.significancia}</span>
                        </td>
                        <td>{fila.requisito.label}</td>
                        <td>{fila.control}</td>
                        <td>{fila.observaciones}</td>
                        <td>
                          <button
                            className={styles.deleteButton}
                            onClick={() => handleEliminarFila(fila.id)}
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

          {/* Signatories Table */}
          <div className={styles.tableSection}>
            <div className={styles.tableHeader}>
              <h3>Firmas y Responsables</h3>
            </div>
            <div className={styles.tableWrapper}>
              <table className={styles.signatoryTable}>
                <thead>
                  <tr>
                    <th></th>
                    <th>Nombre</th>
                    <th>Apellido</th>
                    <th>Puesto</th>
                    <th>Firma</th>
                    <th>Aclaración</th>
                    <th>Fecha</th>
                  </tr>
                </thead>
                <tbody>
                  {/* Row 1: Elaborated */}
                  <tr>
                    <td className={styles.roleCell}>Elaboró</td>
                    <td>
                      <input
                        type="text"
                        value={firmantes.elaboro.nombre}
                        onChange={(e) => handleFirmanteChange("elaboro", "nombre", e.target.value)}
                        className={styles.tableInput}
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        value={firmantes.elaboro.apellido}
                        onChange={(e) => handleFirmanteChange("elaboro", "apellido", e.target.value)}
                        className={styles.tableInput}
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        value={firmantes.elaboro.puesto}
                        onChange={(e) => handleFirmanteChange("elaboro", "puesto", e.target.value)}
                        className={styles.tableInput}
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        value={firmantes.elaboro.firma}
                        onChange={(e) => handleFirmanteChange("elaboro", "firma", e.target.value)}
                        className={styles.tableInput}
                        disabled
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        value={firmantes.elaboro.aclaracion}
                        onChange={(e) => handleFirmanteChange("elaboro", "aclaracion", e.target.value)}
                        className={styles.tableInput}
                        disabled
                      />
                    </td>
                    <td>
                      <input
                        type="date"
                        value={firmantes.elaboro.fecha || ''}
                        onChange={(e) => handleFirmanteChange("elaboro", "fecha", e.target.value)}
                        className={styles.tableInput}
                        placeholder="Completar"
                      />
                    </td>
                  </tr>
                  {/* Row 2: Reviewed */}
                  <tr>
                    <td className={styles.roleCell}>Revisó</td>
                    <td>
                      <input
                        type="text"
                        value={firmantes.reviso.nombre}
                        onChange={(e) => handleFirmanteChange("reviso", "nombre", e.target.value)}
                        className={styles.tableInput}
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        onChange={(e) => handleFirmanteChange("reviso", "apellido", e.target.value)}
                        className={styles.tableInput}
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        onChange={(e) => handleFirmanteChange("reviso", "puesto", e.target.value)}
                        className={styles.tableInput}
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        onChange={(e) => handleFirmanteChange("reviso", "firma", e.target.value)}
                        className={styles.tableInput}
                        disabled
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        onChange={(e) => handleFirmanteChange("reviso", "aclaracion", e.target.value)}
                        className={styles.tableInput}
                        disabled
                      />
                    </td>
                    <td>
                      <input
                        type="date"
                        value={firmantes.elaboro.fecha || ''}
                        onChange={(e) => handleFirmanteChange("elaboro", "fecha", e.target.value)}
                        className={styles.tableInput}
                        placeholder="Completar"
                      />
                    </td>
                  </tr>
                  {/* Row 3: Approved */}
                  <tr>
                    <td className={styles.roleCell}>Aprobó</td>
                    <td>
                      <input
                        type="text"
                        onChange={(e) => handleFirmanteChange("aprobo", "nombre", e.target.value)}
                        className={styles.tableInput}
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        onChange={(e) => handleFirmanteChange("aprobo", "apellido", e.target.value)}
                        className={styles.tableInput}
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        onChange={(e) => handleFirmanteChange("aprobo", "puesto", e.target.value)}
                        className={styles.tableInput}
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        onChange={(e) => handleFirmanteChange("aprobo", "firma", e.target.value)}
                        className={styles.tableInput}
                        disabled
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        onChange={(e) => handleFirmanteChange("aprobo", "aclaracion", e.target.value)}
                        className={styles.tableInput}
                        disabled
                      />
                    </td>
                    <td>
                      <input
                        type="date"
                        value={firmantes.elaboro.fecha || ''}
                        onChange={(e) => handleFirmanteChange("elaboro", "fecha", e.target.value)}
                        className={styles.tableInput}
                        placeholder="Completar"
                      />
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          {/* Action Bar */}
          <div className={styles.actionBar}>
            <button onClick={handlePrint} className={styles.printButton}>
              <Printer size={20} />
              Imprimir Matriz
            </button>
            <button className={styles.saveButton} disabled={isGuardando} onClick={handleGuardar}>
              {isGuardando ? (
                "Guardando..."
              ) : (
                <>
                  <Save size={20} />
                  Guardar Todo
                </>
              )}
            </button>
            {saveSuccess && (
              <div className={styles.saveMessage}>
                <Check size={20} />
                Guardado exitosamente
              </div>
            )}
            {mensajeGuardado && (
              <div className={styles.saveMessage}>{mensajeGuardado}</div>
            )}
          </div>
        </div>
      </div>
    </main>
  )
}
