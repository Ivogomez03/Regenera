'use client';

import { useState, useEffect } from "react";
//import axios from "axios"; 
import axios from "@/app/lib/axiosClient";
import styles from "./indicadoresAmbientales.module.css";
import Image from 'next/image';
import Link from 'next/link';
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import indicadoresSchema from "../../lib/validaciones/indicadoresSchema";
import { Save, BarChart2, FileText, ArrowLeft, Edit, Delete } from "lucide-react";


const TIPOS_INDICADOR = [
    { value: "CANTIDAD_RESIDUOS_GENERADOS_KG_MES", label: "Cantidad residuos generados (kg/mes)" },
    { value: "CANTIDAD_RESIDUOS_GENERADOS_KG_ANIO", label: "Cantidad residuos generados (kg/año)" },
    { value: "CANTIDAD_RESIDUOS_POR_UNIDAD_PRODUCCION", label: "Residuos por unidad de producción" },
    { value: "CONSUMO_TOTAL_AGUA_M3_MES", label: "Consumo agua total (m3/mes)" },
    { value: "CONSUMO_TOTAL_AGUA_M3_ANIO", label: "Consumo agua total (m3/año)" },
    { value: "CONSUMO_AGUA_POZO_CANTIDAD", label: "Consumo agua de pozo (cantidad)" },
    { value: "CAUDAL_VUELCO_EFLUENTES_M3_MES", label: "Caudal de vuelco de efluentes (m3/mes)" },
    { value: "CAUDAL_VUELCO_EFLUENTES_M3_ANIO", label: "Caudal de vuelco de efluentes (m3/año)" },
    { value: "PORCENTAJE_DESVIOS_VUELCO", label: "Porcentaje de desvíos de vuelco" },
    { value: "CONSUMO_AGUA_POR_UNIDAD_PRODUCCION", label: "Consumo de agua por unidad de producción" },
    { value: "CONSUMO_TOTAL_ENERGIA_ELECTRICA_KWH_MES", label: "Energía eléctrica (kWh/mes)" },
    { value: "CONSUMO_TOTAL_ENERGIA_ELECTRICA_KWH_ANIO", label: "Energía eléctrica (kWh/año)" },
    { value: "CONSUMO_ENERGETICO_POR_UNIDAD_PRODUCCION", label: "Consumo energético por unidad de producción" },
    { value: "VOLUMEN_BIOCOMBUSTIBLE_GENERADO", label: "Volumen de biocombustible generado" },
    { value: "CONSUMO_TOTAL_GAS_M3", label: "Consumo total de gas (m3)" },
    { value: "CONSUMO_ENERGIA_TERMICA_POR_PRODUCTO", label: "Consumo de energía térmica por producto" },
    { value: "CONSUMO_COMBUSTIBLES_LITROS", label: "Consumo de combustibles (litros)" },
    { value: "CONSUMO_COMBUSTIBLES_LIQUIDOS_POR_PRODUCCION", label: "Consumo de combustibles líquidos por producción" },
    { value: "NUMERO_ACCIDENTES_AMBIENTALES", label: "Número de accidentes ambientales" },
    { value: "PORCENTAJE_PRESUPUESTO_TECNOLOGIAS_BAJAS_EMISIONES", label: "Porcentaje del presupuesto en tecnologías de bajas emisiones" }
];


export default function IndicadoresPage() {
    const [indicadores, setIndicadores] = useState([]);
    const [loading, setLoading] = useState(false);
    const [idEditando, setIdEditando] = useState(null);

    // Configuración del formulario
    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm({
        resolver: yupResolver(indicadoresSchema),
        mode: "onSubmit",
    });

    // Cargar indicadores al iniciar
    useEffect(() => {
        fetchIndicadores();
    }, []);

    const fetchIndicadores = async () => {
        try {
            const res = await axios.get("/api/indicadores-ambientales/listar");
            setIndicadores(res.data);
            console.log("Respuesta" + res.data)
        } catch (error) {
            console.error("Error al cargar indicadores:", error);
        }
    };

    const onSubmit = async (data) => {
        setLoading(true);

        try {
            if (idEditando) {
                const response = await axios.put(`/api/indicadores-ambientales/modificar/${idEditando}`, data);
                alert(response.data.message || "Indicador modificado correctamente");
                reset();
                fetchIndicadores();
                setIdEditando(null);

            }
            else {


                await axios.post("/api/indicadores-ambientales/crear", data);
                alert("Indicador registrado correctamente");
                reset(); // Limpiar formulario
                fetchIndicadores(); // Actualizar tabla
            }
        } catch (error) {
            console.error("Error al guardar:", error);
            alert("Error al guardar el indicador");
        } finally {
            setLoading(false);
        }
    };

    const handleModificarFila = (id) => {
        // Lógica para modificar una fila existente
        console.log("Modificando fila con ID:", id);

        reset(indicadores.find(ind => ind.idIndicador === id)); // Cargar datos al formulario
        setIdEditando(id);
    }
    /*
    const handleEliminarFila = async (id) => {
        // Lógica para eliminar una fila existente
        console.log("Eliminando fila con ID:", id);
        // Aquí podrías agregar una confirmación antes de eliminar

        if (confirm("¿Estás seguro de que deseas eliminar este indicador?")) {
            // Lógica para eliminar el indicador (ej. llamada a API)
            console.log("Indicador eliminado con ID:", id);
            // Después de eliminar, recargar la lista de indicadores
            try {
                const response = await axios.delete(`/api/indicadores-ambientales/eliminar/${id}`);
                alert(response.data.message || "Indicador eliminado correctamente");
                fetchIndicadores();
            } catch (error) {
                console.error("Error al eliminar:", error);
                alert("Error al eliminar el indicador");
            }
        }


    }
*/
    // Función auxiliar para calcular avance visualmente (en %)
    const getAvanceColor = (valor) => {
        if (valor >= 100) return "green";
        if (valor >= 50) return "orange";
        return "red";
    };

    return (

        <main className={styles.main}>
            <header className={styles.topHeader}>
                <Link href="/modulo2" className={styles.backLink}>
                    <ArrowLeft size={24} /> Volver
                </Link>
                <Link href="/">
                    <Image src="/logo2.png" alt="Regenera Logo" width={140} height={60} className={styles.logo} priority />
                </Link>
            </header>
            <div className={styles.containerPage} >
                <div className={styles.container}>
                    <div className={styles.headerSection}>

                        <div className={styles.titleIcon}>
                            <BarChart2 size={40} color="white" />
                        </div>


                        <h1> Indicadores Ambientales</h1>


                        <p>Registro, monitoreo y seguimiento de indicadores ambientales.</p>
                    </div>

                    {/*  FORMULARIO DE CARGA  */}
                    <section className={styles.formSection}>
                        <div className={styles.titleWithIcon}>
                            <FileText size={20} />
                            <h2 className={styles.formTitle}>Nuevo Registro</h2>
                        </div>
                        <form onSubmit={handleSubmit(onSubmit)} className={styles.formGrid}>

                            {/*  Datos del Indicador */}
                            <div className={styles.column}>
                                <h3>Medición</h3>
                                <div className={styles.formGroup}>
                                    <label>Tipo de Indicador*</label>
                                    <select {...register("tipoIndicador")}>
                                        <option value="">Seleccione...</option>
                                        {TIPOS_INDICADOR.map((t) => (
                                            <option key={t.value} value={t.value}>{t.label}</option>
                                        ))}
                                    </select>
                                    <p className={styles.error}>{errors.tipoIndicador?.message}</p>
                                </div>

                                <div className={styles.row}>
                                    <div className={styles.formGroup}>
                                        <label>Valor Medido*</label>
                                        <input type="number" step="0.01" {...register("valorMedido")} placeholder="0.00" />
                                        <p className={styles.error}>{errors.valorMedido?.message}</p>
                                    </div>

                                </div>

                                <div className={styles.row}>
                                    <div className={styles.formGroup}>
                                        <label>Fecha Registro*</label>
                                        <input type="date" {...register("fechaRegistro")} />
                                        <p className={styles.error}>{errors.fechaRegistro?.message}</p>
                                    </div>
                                    <div className={styles.formGroup}>
                                        <label>Fecha Línea Base*</label>
                                        <input type="date" {...register("fechaLineaBase")} />
                                        <p className={styles.error}>{errors.fechaLineaBase?.message}</p>
                                    </div>
                                </div>
                                <div className={styles.formGroup}>
                                    <label>Fuente del Dato*</label>
                                    <input type="text" {...register("fuenteDato")} placeholder="Ej. Factura EPE, Planilla interna" />
                                </div>
                            </div>

                            {/*  Objetivos y Responsables */}



                            <div className={styles.column}>
                                <h3>Vinculación a Objetivos</h3>
                                <div className={styles.formGroup}>
                                    <label>Objetivo Asociado*</label>
                                    <textarea {...register("objetivoAsociado")} rows="2" placeholder="Describa el objetivo ambiental..." />
                                    <p className={styles.error}>{errors.objetivoAsociado?.message}</p>
                                </div>

                                <div className={styles.row}>
                                    <div className={styles.formGroup}>
                                        <label>Meta (Valor)*</label>
                                        <input type="number" step="0.01" {...register("metaValor")} placeholder="Ej. 1000" />
                                        <p className={styles.error}>{errors.metaValor?.message}</p>
                                    </div>
                                    <div className={styles.formGroup}>
                                        <label>Unidad*</label>
                                        <input type="text" {...register("metaUnidad")} placeholder="kg, m3, %" />
                                        <p className={styles.error}>{errors.metaUnidad?.message}</p>
                                    </div>


                                </div>

                                <div className={styles.row}>
                                    <div className={styles.formGroup}>
                                        <label>Sentido del Indicador*</label>
                                        <select {...register("sentidoIndicador")}>
                                            <option value="">Seleccione...</option>
                                            <option value="ASCENDENTE">Ascendente</option>
                                            <option value="DESCENDENTE">Descendente</option>
                                        </select>
                                        <p className={styles.error}>{errors.sentidoIndicador?.message}</p>
                                    </div>
                                </div>

                                <div className={styles.formGroup}>
                                    <label>Responsable Cumplimiento*</label>
                                    <input type="text" {...register("responsableCumplimiento")} placeholder="Nombre o Cargo" />
                                </div>

                                <h3>Responsable de Carga</h3>
                                <div className={styles.row}>
                                    <input {...register("respCargaNombre")} placeholder="Nombre" />
                                    <input {...register("respCargaApellido")} placeholder="Apellido" />
                                </div>
                                <div className={styles.row}>
                                    <input {...register("respCargaCargo")} placeholder="Cargo" />
                                    <input {...register("respCargaSector")} placeholder="Sector" />
                                </div>
                                {/* Mostrar error genérico de bloque si falta algo */}
                                {(errors.respCargaNombre || errors.respCargaCargo) && <p className={styles.error}>Complete los datos del responsable de carga</p>}
                            </div>

                            <div className={styles.fullWidth}>
                                <button type="submit" disabled={loading} className={styles.submitButton}>
                                    {loading ? "Guardando..." : "Registrar Indicador"} <Save size={18} style={{ marginLeft: 8 }} />
                                </button>
                            </div>
                        </form>
                    </section>

                    {/*TABLA MATRIZ DE SEGUIMIENTO */}
                    <section className={styles.tableSection}>
                        <h3>Matriz de Seguimiento de Indicadores Ambientales</h3>
                        <div className={styles.tableWrapper}>
                            <table className={styles.table}>
                                <thead>
                                    <tr>
                                        <th>Objetivo</th>
                                        <th>Meta</th>
                                        <th>Valor Actual</th>
                                        <th>Unidad</th>
                                        <th>Tipo</th>
                                        <th>Responsable</th>
                                        <th>Fecha Reg.</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {indicadores.length > 0 ? (
                                        indicadores.map((ind) => (
                                            <tr key={ind.idIndicador}>
                                                <td>{ind.objetivoAsociado}</td>
                                                <td>{ind.metaValor}</td>
                                                <td>{ind.valorMedido}</td>
                                                <td>{ind.metaUnidad}</td>
                                                <td>{TIPOS_INDICADOR.find(t => t.value === ind.tipoIndicador)?.label || ind.tipoIndicador}</td>
                                                <td>{ind.responsableCumplimiento}</td>
                                                <td>{ind.fechaRegistro}</td>
                                                <td>
                                                    <button onClick={() => handleModificarFila(ind.idIndicador)} className={styles.editButton}>
                                                        <Edit size={20} />
                                                    </button>
                                                    {/*
                                                    <button onClick={() => handleEliminarFila(ind.idIndicador)} className={styles.deleteButton}>
                                                        <Delete size={20} />
                                                    </button>*/}


                                                </td>
                                            </tr>
                                        ))
                                    ) : (
                                        <tr>
                                            <td colSpan="7" style={{ textAlign: "center" }}>No hay indicadores registrados.</td>
                                        </tr>
                                    )}
                                </tbody>
                            </table>
                        </div>
                    </section>
                </div>
            </div>
        </main>

    );
}