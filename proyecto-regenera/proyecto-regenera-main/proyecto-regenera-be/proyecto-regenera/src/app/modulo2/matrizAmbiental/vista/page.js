// src/app/modulo2/matrizAmbiental/vista/page.js
'use client';

import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'next/navigation';
import Image from 'next/image';
import Link from 'next/link';
import { Printer, ArrowLeft, Download } from 'lucide-react';
import axiosClient from "@/app/lib/axiosClient";
import styles from './vista.module.css'; // Crearemos este CSS específico para documento

export default function VistaMatrizPage() {
    const searchParams = useSearchParams();
    const id = searchParams.get('id');

    const [loading, setLoading] = useState(true);
    const [cabecera, setCabecera] = useState(null);
    const [items, setItems] = useState([]);
    const [firmas, setFirmas] = useState(null);

    useEffect(() => {
        if (!id) return;

        const cargarDatos = async () => {
            try {
                setLoading(true);
                // 1. Cargar Cabecera
                const resForm = await axiosClient.get(`/api/formularios/${id}`);
                // 2. Cargar Items
                const resItems = await axiosClient.get(`/api/grilla/formularios/${id}/items`);
                // 3. Cargar Firmas
                const resFirmas = await axiosClient.get(`/api/firmas/formularios/${id}/historial`);

                const datosFirma = Array.isArray(resFirmas.data) ? resFirmas.data[0] : resFirmas.data;
                setCabecera(resForm.data);
                setItems(resItems.data);
                setFirmas(datosFirma);
            } catch (error) {
                console.error("Error cargando documento:", error);
                alert("Error al cargar el documento.");
            } finally {
                setLoading(false);
            }
        };

        cargarDatos();
    }, [id]);

    const handlePrint = () => {
        window.print();
    };

    if (loading) return <div className={styles.loading}>Cargando documento...</div>;
    if (!cabecera) return <div className={styles.error}>Documento no encontrado.</div>;

    return (
        <div className={styles.pageContainer}>

            {/* Barra de Herramientas (Se oculta al imprimir) */}
            <div className={styles.toolbar}>
                <Link href="/modulo2/revisiones" className={styles.backButton}>
                    <ArrowLeft size={18} /> Volver
                </Link>
                <div className={styles.actions}>
                    <button onClick={handlePrint} className={styles.printButton}>
                        <Printer size={18} /> Imprimir / Guardar PDF
                    </button>
                </div>
            </div>

            {/* HOJA A4 (Lo que realmente se imprime) */}
            <div className={styles.documento}>

                {/* Encabezado del Documento */}
                <header className={styles.docHeader}>
                    <div className={styles.logoBox}>
                        {/* Si tuvieras logo en base64 en cabecera.logoEmpresa, úsalo aquí */}
                        <Image src="/logo2.png" alt="Logo" width={120} height={50} />
                    </div>
                    <div className={styles.titleBox}>
                        <h1>MATRIZ DE ASPECTOS E IMPACTOS AMBIENTALES</h1>
                        <h2>{cabecera.nombreEmpresa}</h2>
                    </div>
                    <div className={styles.infoBox}>
                        <p><strong>Código:</strong> {cabecera.codigo}</p>
                        <p><strong>Fecha:</strong> {cabecera.fecha}</p>
                        <p><strong>Página:</strong> 1 de 1</p>
                    </div>
                </header>

                {/* Tabla Principal */}
                <div className={styles.tableContainer}>
                    <table className={styles.mainTable}>
                        <thead>
                            <tr>
                                <th rowSpan={2} style={{ width: '30px' }}>N°</th>
                                <th rowSpan={2}>Actividad</th>
                                <th rowSpan={2}>Aspecto</th>
                                <th rowSpan={2}>Impacto</th>
                                <th rowSpan={2}>Cond.</th>
                                <th colSpan={5}>Evaluación</th>
                                <th rowSpan={2}>Significancia</th>
                                <th rowSpan={2}>Control Operacional</th>
                            </tr>
                            <tr className={styles.subHeader}>
                                <th title="Severidad">S</th>
                                <th title="Magnitud">M</th>
                                <th title="Frecuencia">F</th>
                                <th title="Reversibilidad">R</th>
                                <th title="Valoración Total">V</th>
                            </tr>
                        </thead>
                        <tbody>
                            {items.map((fila, index) => (
                                <tr key={fila.idGrilla || index}>
                                    <td className={styles.center}>{index + 1}</td>
                                    <td>{fila.actividadNombre || fila.actividad}</td>
                                    <td>{fila.aspectoAmbientalNombre || fila.aspectoAmbiental}</td>
                                    <td>{fila.impactoAmbientalNombre || fila.impactoAmbiental}</td>
                                    <td className={styles.center}>{fila.condicionImpactoNombre || fila.condicionImpacto}</td>

                                    <td className={styles.center}>{fila.severidad}</td>
                                    <td className={styles.center}>{fila.magnitud}</td>
                                    <td className={styles.center}>{fila.frecuencia}</td>
                                    <td className={styles.center}>{fila.reversibilidad}</td>
                                    <td className={styles.boldCenter}>{fila.valoracion}</td>

                                    <td className={styles.center}>
                                        <span className={fila.valoracion > 15 ? styles.badgeRed : styles.badgeGreen}>
                                            {fila.valoracion > 15 ? 'Significativo' : 'No Sig.'}
                                        </span>
                                    </td>
                                    <td>{fila.control}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                {/* Sección de Firmas */}
                {firmas && (
                    <div className={styles.firmasSection}>
                        <div className={styles.firmaBox}>
                            <div className={styles.firmaLine}></div>
                            <p><strong>Elaboró:</strong> {firmas.nombreElabore} {firmas.apellidoElabore}</p>
                            <p className={styles.cargo}>{firmas.puestoElabore}</p>
                        </div>
                        <div className={styles.firmaBox}>
                            <div className={styles.firmaLine}></div>
                            <p><strong>Revisó:</strong> {firmas.nombreReviso} {firmas.apellidoReviso}</p>
                            <p className={styles.cargo}>{firmas.puestoReviso}</p>
                        </div>
                        <div className={styles.firmaBox}>
                            <div className={styles.firmaLine}></div>
                            <p><strong>Aprobó:</strong> {firmas.nombreAprobo} {firmas.apellidoAprobo}</p>
                            <p className={styles.cargo}>{firmas.puestoAprobo}</p>
                        </div>
                    </div>
                )}

            </div>
        </div>
    );
}