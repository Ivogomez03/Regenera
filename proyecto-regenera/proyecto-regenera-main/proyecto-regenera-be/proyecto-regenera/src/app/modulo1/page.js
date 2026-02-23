"use client";

import { useState, useEffect } from "react";
//import axios from "axios";
import useValidarAutenticacion from "../lib/validaciones/useValidarAutenticacion";
import axios from "@/app/lib/axiosClient";
import styles from "./modulo1.module.css";
import Image from 'next/image';
import Link from 'next/link';
import { Info, Building2, MapPin, FileText, Users } from "lucide-react"
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import modulo1Schema from '../lib/validaciones/modulo1Schema';

export default function Modulo1Page() {
    const { isCheckingAuth } = useValidarAutenticacion();
    const [provincias, setProvincias] = useState([]);
    const [localidades, setLocalidades] = useState([]);
    const [codigos, setCodigos] = useState([]);
    const router = useRouter();



    const [form, setForm] = useState({
        nombreRazonSocial: "",
        cuit: "",
        domicilioLegal: "",
        domicilioOperativo: "",
        correoElectronico: "",
        telefono: "",
        idLocalidad: "",
        idProvincia: "",

        idRubroIndustrial: "",

        declaracionAlcanceSga: "",
        declaracionPoliticaAmbiental: "",
        fechaUltimaActualizacionPolitica: "",

        autoridadSocietaria: {
            nombre: "",
            apellido: "",
            correoElectronico: "",
            telefono: "",
            cargo: "",
            tipoResponsabilidad: "AUTORIDAD_SOCIETARIA",
            asignacionResponsabilidades: ""
        },

        responsableSga: {
            nombre: "",
            apellido: "",
            correoElectronico: "",
            telefono: "",
            cargo: "",
            tipoResponsabilidad: "RESPONSABLE_SGA",
            asignacionResponsabilidades: ""
        }
    });


    const {
        register,
        handleSubmit,
        formState: { errors },
        reset
    } = useForm({
        resolver: yupResolver(modulo1Schema),
        mode: "onSubmit",
        values: form
    });


    const handleChange = (e) => {
        const { name, value } = e.target;

        if (name.includes(".")) {
            const [parent, child] = name.split(".");
            setForm(prev => ({
                ...prev,
                [parent]: {
                    ...prev[parent],
                    [child]: value
                }
            }));
        } else if (name === "idProvincia") {
            setForm(prev => ({
                ...prev,
                idProvincia: value,
                idLocalidad: ""
            }));
        } else {
            setForm(prev => ({
                ...prev,
                [name]: value
            }));
        }
    };

    useEffect(() => {
        if (isCheckingAuth) return;

        Promise.all([
            axios.get("/api/provincias"),
            axios.get("/api/localidades"),
            axios.get("/api/rubro-industrial")
        ])
            .then(([resProvincias, resLocalidades, resCodigos]) => {
                console.log("Datos cargados correctamente");
                setProvincias(resProvincias.data);
                setLocalidades(resLocalidades.data);
                setCodigos(resCodigos.data);
            })
            .catch(error => {
                console.error("Error al cargar los datos maestros:", error);
            });

    }, [isCheckingAuth]);

    if (isCheckingAuth) {
        return (
            <div className={styles.loaderContainer}>
                <img src="/icons8-hilandero.gif" alt="Cargando" className={styles.loaderImage} />
                <p className={styles.loaderText}>Cargando...</p>
            </div>
        );

    }

    const onSubmitValidated = async (data) => {
        try {


            const { idProvincia, ...rest } = form;
            const dataToSend = {
                ...rest,
                idLocalidad: Number(form.idLocalidad) || null,
                idRubroIndustrial: Number(form.idRubroIndustrial) || null,
            };

            const response = await axios.post("/api/institucion", dataToSend);
            alert("Datos registrados correctamente");

            setForm({
                nombreRazonSocial: "",
                cuit: "",
                domicilioLegal: "",
                domicilioOperativo: "",
                correoElectronico: "",
                telefono: "",
                idLocalidad: "",
                idProvincia: "",
                idRubroIndustrial: "",
                declaracionAlcanceSga: "",
                declaracionPoliticaAmbiental: "",
                fechaUltimaActualizacionPolitica: "",
                autoridadSocietaria: {
                    nombre: "",
                    apellido: "",
                    correoElectronico: "",
                    telefono: "",
                    cargo: "",
                    tipoResponsabilidad: "AUTORIDAD_SOCIETARIA",
                    asignacionResponsabilidades: ""
                },
                responsableSga: {
                    nombre: "",
                    apellido: "",
                    correoElectronico: "",
                    telefono: "",
                    cargo: "",
                    tipoResponsabilidad: "RESPONSABLE_SGA",
                    asignacionResponsabilidades: ""
                }
            });
        } catch (error) {
            console.error("Error al guardar:", error);
            if (error.response && error.response.status === 401) {
                alert("Tu sesión ha expirado. Por favor inicia sesión nuevamente.");
            } else {
                alert("Error al enviar los datos");
            }
        }
    };


    return (
        <main className={styles.main}>
            <header className={styles.topHeader}>
                <Link href="/">
                    <Image src="/logo2.png" alt="Regenera Logo" width={140} height={60} className={styles.logo} priority />
                </Link>
            </header>

            <div className={styles.backgroundPattern} />

            <div className={styles.mod1Page}>
                <div className={styles.mod1Container}>
                    <div className={styles.pageHeader}>
                        <h1><strong>Registro y Perfil de la Organización</strong></h1>
                        <p>Complete la información de su organización para iniciar su gestión ambiental</p>
                    </div>

                    <form className={styles.form} onSubmit={handleSubmit(onSubmitValidated)}>
                        <div className={styles.section}>
                            <div className={styles.sectionHeader}>
                                <Building2 className={styles.sectionIcon} />
                                <h2>Datos Institucionales</h2>
                            </div>
                            <div className={styles.grid}>
                                <div className={styles.formGroup}>
                                    <label htmlFor="nombreRazonSocial">Nombre / Razón Social*</label>
                                    <input
                                        {...register("nombreRazonSocial")}
                                        id="nombreRazonSocial"
                                        name="nombreRazonSocial"
                                        value={form.nombreRazonSocial}
                                        onChange={handleChange}
                                        placeholder="Nombre de la organización"
                                        required
                                    />
                                    {errors.nombreRazonSocial && <p className={styles.error}>{errors.nombreRazonSocial.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="cuit">CUIT*</label>
                                    <input
                                        {...register("cuit")}
                                        id="cuit"
                                        name="cuit"
                                        value={form.cuit}
                                        onChange={handleChange}
                                        placeholder="XX-XXXXXXXX-X"
                                        required
                                    />
                                    {errors.cuit && <p className={styles.error}>{errors.cuit.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="domicilioLegal">Domicilio Legal*</label>
                                    <input
                                        {...register("domicilioLegal")}
                                        id="domicilioLegal"
                                        name="domicilioLegal"
                                        value={form.domicilioLegal}
                                        onChange={handleChange}
                                        placeholder="Dirección legal"
                                        required
                                    />
                                    {errors.domicilioLegal && <p className={styles.error}>{errors.domicilioLegal.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="domicilioOperativo">Domicilio Operativo*</label>
                                    <input
                                        {...register("domicilioOperativo")}
                                        id="domicilioOperativo"
                                        name="domicilioOperativo"
                                        value={form.domicilioOperativo}
                                        onChange={handleChange}
                                        placeholder="Dirección operativa"
                                        required
                                    />
                                    {errors.domicilioOperativo && <p className={styles.error}>{errors.domicilioOperativo.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="correoElectronico">Email*</label>
                                    <input
                                        {...register("correoElectronico")}
                                        type="email"
                                        id="correoElectronico"
                                        name="correoElectronico"
                                        value={form.correoElectronico}
                                        onChange={handleChange}
                                        placeholder="contacto@empresa.com"
                                        required
                                    />
                                    {errors.correoElectronico && <p className={styles.error}>{errors.correoElectronico.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="telefono">Telefono*</label>
                                    <input
                                        {...register("telefono")}
                                        id="telefono"
                                        name="telefono"
                                        value={form.telefono}
                                        onChange={handleChange}
                                        placeholder="+54 9 11 1234-5678"
                                        required
                                    />
                                    {errors.telefono && <p className={styles.error}>{errors.telefono.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="provincia">Provincia*</label>
                                    <select
                                        id="provincia"
                                        name="idProvincia"
                                        value={form.idProvincia || ""}
                                        onChange={handleChange}
                                        required
                                    >
                                        <option value="">Seleccioná una provincia</option>
                                        {provincias.map(p => (
                                            <option key={p.idProvincia} value={String(p.idProvincia)}>
                                                {p.nombreProvincia}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="localidad">Localidad*</label>
                                    <select
                                        id="localidad"
                                        name="idLocalidad"
                                        value={form.idLocalidad || ""}
                                        onChange={handleChange}
                                        required
                                    >
                                        <option value="">Seleccioná una localidad</option>
                                        {localidades
                                            .filter(l => l.idProvincia === Number(form.idProvincia))
                                            .map(l => (
                                                <option key={l.idLocalidad} value={l.idLocalidad}>
                                                    {l.nombreLocalidad}
                                                </option>
                                            ))}
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div className={styles.section}>
                            <div className={styles.sectionHeader}>
                                <MapPin className={styles.sectionIcon} />
                                <h2>Actividad Industrial</h2>
                            </div>
                            <div className={styles.singleColGrid}>
                                <div className={styles.formGroup}>
                                    <label htmlFor="cod">Código de actividad económica*</label>
                                    <select
                                        id="cod"
                                        name="idRubroIndustrial"
                                        value={form.idRubroIndustrial}
                                        onChange={handleChange}
                                        required
                                    >
                                        <option value="">Seleccioná un código</option>
                                        {codigos.map(c => (
                                            <option key={c.id} value={c.id}>
                                                {c.rubroIndustrial}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div className={styles.section}>
                            <div className={styles.sectionHeader}>
                                <FileText className={styles.sectionIcon} />
                                <h2>Política Ambiental</h2>
                            </div>
                            <div className={styles.grid}>
                                <div className={styles.formGroup}>
                                    <label htmlFor="declaracionAlcance">Declaración del alcance del SGA*
                                        <span className={styles.tooltip}> <Info size={18} className={styles.tooltipIcon} />
                                            <span className={styles.tooltiptext}>
                                                Ejemplo: Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                                                Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                                            </span>
                                        </span>
                                    </label>
                                    <textarea
                                        {...register("declaracionAlcanceSga")}
                                        id="declaracionAlcance"
                                        name="declaracionAlcanceSga"
                                        rows="8"
                                        value={form.declaracionAlcanceSga}
                                        onChange={handleChange}
                                        placeholder="Describa el alcance del Sistema de Gestión Ambiental..."
                                        required
                                    ></textarea>
                                    {errors.declaracionAlcanceSga && <p className={styles.error}>{errors.declaracionAlcanceSga.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="declaracionPoliticaAmbiental"> Declaración de política Ambiental*
                                        <span className={styles.tooltip}> <Info size={18} className={styles.tooltipIcon} />
                                            <span className={styles.tooltiptext}>
                                                Es el pilar de la GA. Consiste en una declaración formal de los principios y compromisos
                                                que la organización define para mejorar el desempeño ambiental. Debe incluir compromisos explícitos
                                                para proteger el medioambiente, cumplir los requisitos legales y propiciar la mejora continua.
                                            </span>
                                        </span>
                                    </label>
                                    <textarea
                                        {...register("declaracionPoliticaAmbiental")}
                                        id="declaracionPoliticaAmbiental"
                                        name="declaracionPoliticaAmbiental"
                                        rows="8"
                                        value={form.declaracionPoliticaAmbiental}
                                        onChange={handleChange}
                                        placeholder="Describa la política ambiental de la organización..."
                                        required
                                    ></textarea>
                                    {errors.declaracionPoliticaAmbiental && <p className={styles.error}>{errors.declaracionPoliticaAmbiental.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="fecha">Fecha de la última actualización*</label>
                                    <input
                                        {...register("fechaUltimaActualizacionPolitica")}
                                        type="date"
                                        id="fecha"
                                        name="fechaUltimaActualizacionPolitica"
                                        value={form.fechaUltimaActualizacionPolitica}
                                        onChange={handleChange}
                                        required />
                                    {errors.fechaUltimaActualizacionPolitica && <p className={styles.error}>{errors.fechaUltimaActualizacionPolitica.message}</p>}
                                </div>
                            </div>
                        </div>

                        <div className={styles.section}>
                            <div className={styles.sectionHeader}>
                                <Users className={styles.sectionIcon} />
                                <h2>Autoridades Societarias</h2>
                            </div>
                            <div className={styles.grid}>
                                <div className={styles.formGroup}>
                                    <label htmlFor="nombreAut">Nombre*</label>
                                    <input
                                        {...register("autoridadSocietaria.nombre")}
                                        id="nombreAut"
                                        name="autoridadSocietaria.nombre"
                                        value={form.autoridadSocietaria.nombre}
                                        onChange={handleChange}
                                        placeholder="Nombre"
                                        required
                                    />
                                    {errors.autoridadSocietaria?.nombre && <p className={styles.error}>{errors.autoridadSocietaria.nombre.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="apellidoAut">Apellido*</label>
                                    <input
                                        {...register("autoridadSocietaria.apellido")}
                                        id="apellidoAut"
                                        name="autoridadSocietaria.apellido"
                                        value={form.autoridadSocietaria.apellido}
                                        onChange={handleChange}
                                        placeholder="Apellido"
                                        required
                                    />
                                    {errors.autoridadSocietaria?.apellido && <p className={styles.error}>{errors.autoridadSocietaria.apellido.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="cargoAut">Cargo*</label>
                                    <input
                                        {...register("autoridadSocietaria.cargo")}
                                        id="cargoAut"
                                        name="autoridadSocietaria.cargo"
                                        value={form.autoridadSocietaria.cargo}
                                        onChange={handleChange}
                                        placeholder="Ej: Director General"
                                        required
                                    />
                                    {errors.autoridadSocietaria?.cargo && <p className={styles.error}>{errors.autoridadSocietaria.cargo.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="emailAut">Email*</label>
                                    <input
                                        {...register("autoridadSocietaria.correoElectronico")}
                                        type="email"
                                        id="emailAut"
                                        name="autoridadSocietaria.correoElectronico"
                                        value={form.autoridadSocietaria.correoElectronico}
                                        onChange={handleChange}
                                        placeholder="email@empresa.com"
                                        required
                                    />
                                    {errors.autoridadSocietaria?.correoElectronico && <p className={styles.error}>{errors.autoridadSocietaria.correoElectronico.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="telefonoAut">Teléfono*</label>
                                    <input
                                        {...register("autoridadSocietaria.telefono")}
                                        id="telefonoAut"
                                        name="autoridadSocietaria.telefono"
                                        value={form.autoridadSocietaria.telefono}
                                        onChange={handleChange}
                                        placeholder="+54 9 11 1234-5678"
                                        required
                                    />
                                    {errors.autoridadSocietaria?.telefono && <p className={styles.error}>{errors.autoridadSocietaria.telefono.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="asignacionAut">Asignación de responsabilidades específicas*</label>
                                    <textarea
                                        {...register("autoridadSocietaria.asignacionResponsabilidades")}
                                        id="asignacionAut"
                                        name="autoridadSocietaria.asignacionResponsabilidades"
                                        rows="5"
                                        value={form.autoridadSocietaria.asignacionResponsabilidades}
                                        onChange={handleChange}
                                        placeholder="Describa las responsabilidades asignadas..."
                                        required
                                    ></textarea>
                                    {errors.autoridadSocietaria?.asignacionResponsabilidades && <p className={styles.error}>{errors.autoridadSocietaria.asignacionResponsabilidades.message}</p>}
                                </div>
                            </div>
                        </div>

                        <div className={styles.section}>
                            <div className={styles.sectionHeader}>
                                <Users className={styles.sectionIcon} />
                                <h2>Responsable del SGA</h2>
                            </div>
                            <div className={styles.grid}>
                                <div className={styles.formGroup}>
                                    <label htmlFor="nombreResp">Nombre*</label>
                                    <input
                                        {...register("responsableSga.nombre")}
                                        id="nombreResp"
                                        name="responsableSga.nombre"
                                        value={form.responsableSga.nombre}
                                        onChange={handleChange}
                                        placeholder="Nombre"
                                        required
                                    />
                                    {errors.responsableSga?.nombre && <p className={styles.error}>{errors.responsableSga.nombre.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="apellidoResp">Apellido*</label>
                                    <input
                                        {...register("responsableSga.apellido")}
                                        id="apellidoResp"
                                        name="responsableSga.apellido"
                                        value={form.responsableSga.apellido}
                                        onChange={handleChange}
                                        placeholder="Apellido"
                                        required
                                    />
                                    {errors.responsableSga?.apellido && <p className={styles.error}>{errors.responsableSga.apellido.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="cargoResp">Cargo*</label>
                                    <input
                                        {...register("responsableSga.cargo")}
                                        id="cargoResp"
                                        name="responsableSga.cargo"
                                        value={form.responsableSga.cargo}
                                        onChange={handleChange}
                                        placeholder="Ej: Responsable Ambiental"
                                        required
                                    />
                                    {errors.responsableSga?.cargo && <p className={styles.error}>{errors.responsableSga.cargo.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="emailResp">Email*</label>
                                    <input
                                        {...register("responsableSga.correoElectronico")}
                                        type="email"
                                        id="emailResp"
                                        name="responsableSga.correoElectronico"
                                        value={form.responsableSga.correoElectronico}
                                        onChange={handleChange}
                                        placeholder="email@empresa.com"
                                        required
                                    />
                                    {errors.responsableSga?.correoElectronico && <p className={styles.error}>{errors.responsableSga.correoElectronico.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="telefonoResp">Teléfono*</label>
                                    <input
                                        {...register("responsableSga.telefono")}
                                        id="telefonoResp"
                                        name="responsableSga.telefono"
                                        value={form.responsableSga.telefono}
                                        onChange={handleChange}
                                        placeholder="+54 9 11 1234-5678"
                                        required
                                    />
                                    {errors.responsableSga?.telefono && <p className={styles.error}>{errors.responsableSga.telefono.message}</p>}
                                </div>
                                <div className={styles.formGroup}>
                                    <label htmlFor="asignacionResp">Asignación de responsabilidades específicas*</label>
                                    <textarea
                                        {...register("responsableSga.asignacionResponsabilidades")}
                                        id="asignacionResp"
                                        name="responsableSga.asignacionResponsabilidades"
                                        rows="5"
                                        value={form.responsableSga.asignacionResponsabilidades}
                                        onChange={handleChange}
                                        placeholder="Describa las responsabilidades asignadas..."
                                        required
                                    ></textarea>
                                    {errors.responsableSga?.asignacionResponsabilidades && <p className={styles.error}>{errors.responsableSga.asignacionResponsabilidades.message}</p>}
                                </div>
                            </div>
                        </div>

                        <button type="submit" className={styles.button}>Guardar Información</button>

                    </form>
                </div>
            </div>
        </main>
    );
}