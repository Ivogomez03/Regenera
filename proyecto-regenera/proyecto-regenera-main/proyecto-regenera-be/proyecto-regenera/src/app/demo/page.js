"use client";

import { useState } from "react";
import styles from "./demo.module.css";
import Image from 'next/image';
import Link from 'next/link';
import { CheckCircle } from "lucide-react";

export default function DemoPage() {
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [email, setEmail] = useState("");
  const [celular, setCelular] = useState("");
  const [cargo, setCargo] = useState("");
  const [nombreEmpresa, setNombreEmpresa] = useState("");
  const [pais, setPais] = useState("");
  const [tamano, setTamano] = useState("");
  const [tipo, setTipo] = useState("");


  const [formData, setFormData] = useState({
    nombre: "",
    apellido: "",
    email: "",
    celular: "",
    cargo: "",
    nombreEmpresa: "",
    pais: "",
    tamano: "",
    tipo: "",
    terminos: false,
  })
  const [isSubmitting, setIsSubmitting] = useState(false);




  const handleChange = (e) => {
    const { name, value, type } = e.target
    const checked = (e.target).checked

    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }))
  }


  return (
    <main className={styles.main}>
      <header className={styles.topHeader}>
        <Link href="/">
          <Image src="/logo2.png" alt="Regenera Logo" width={140} height={60} className={styles.logo} priority />
        </Link>
      </header>

      <div className={styles.backgroundPattern} />

      <div className={styles.demoPage}>
        <div className={styles.contentWrapper}>
          {/* Left Side - Info Card */}
          <div className={styles.infoCard}>
            <div className={styles.infoHeader}>
              <h2>¿Por qué solicitar una demo?</h2>
              <p>Descubre cómo REGENERA puede transformar la gestión ambiental de tu empresa</p>
            </div>

            <div className={styles.benefitsList}>
              <div className={styles.benefitItem}>
                <CheckCircle className={styles.checkIcon} />
                <div>
                  <h4>Gestión Integral</h4>
                  <p>Controla todos los aspectos ambientales desde una plataforma única</p>
                </div>
              </div>

              <div className={styles.benefitItem}>
                <CheckCircle className={styles.checkIcon} />
                <div>
                  <h4>Cumplimiento Normativo</h4>
                  <p>Mantén tu empresa al día con las regulaciones ambientales</p>
                </div>
              </div>

              <div className={styles.benefitItem}>
                <CheckCircle className={styles.checkIcon} />
                <div>
                  <h4>Reportes Automatizados</h4>
                  <p>Genera informes profesionales en segundos</p>
                </div>
              </div>

              <div className={styles.benefitItem}>
                <CheckCircle className={styles.checkIcon} />
                <div>
                  <h4>Soporte Especializado</h4>
                  <p>Acompañamiento continuo de nuestro equipo de expertos</p>
                </div>
              </div>
            </div>

            <div className={styles.imageContainer}>
              <Image
                src="/foto-demo.jpg"
                alt="Environmental Dashboard"
                width={500}
                height={300}
                className={styles.dashboardImage}
              />
            </div>
          </div>

          {/* Right Side - Form Card */}
          <div className={styles.formCard}>
            <div className={styles.cardHeader}>
              <h1>Solicitá tu demo</h1>
              <p>Completa el formulario y te contactaremos en 24 horas</p>
            </div>

            <form className={styles.form}>
              <div className={styles.formRow}>
                <div className={styles.formGroup}>
                  <label htmlFor="nombre">Nombre*</label>
                  <input
                    id="nombre"
                    name="nombre"
                    value={formData.nombre}
                    onChange={handleChange}
                    placeholder="Juan"
                    required
                  />
                </div>

                <div className={styles.formGroup}>
                  <label htmlFor="apellido">Apellido*</label>
                  <input
                    id="apellido"
                    name="apellido"
                    value={formData.apellido}
                    onChange={handleChange}
                    placeholder="Pérez"
                    required
                  />
                </div>
              </div>

              <div className={styles.formRow}>
                <div className={styles.formGroup}>
                  <label htmlFor="email">Email*</label>
                  <input
                    type="email"
                    id="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="juan.perez@empresa.com"
                    required
                  />
                </div>

                <div className={styles.formGroup}>
                  <label htmlFor="celular">Número celular</label>
                  <input
                    id="celular"
                    name="celular"
                    value={formData.celular}
                    onChange={handleChange}
                    placeholder="+54 9 11 1234-5678"
                  />
                </div>
              </div>

              <div className={styles.formRow}>
                <div className={styles.formGroup}>
                  <label htmlFor="cargo">Cargo*</label>
                  <input
                    id="cargo"
                    name="cargo"
                    value={formData.cargo}
                    onChange={handleChange}
                    placeholder="Gerente de Sustentabilidad"
                    required
                  />
                </div>

                <div className={styles.formGroup}>
                  <label htmlFor="nombreEmpresa">Nombre de la empresa*</label>
                  <input
                    id="nombreEmpresa"
                    name="nombreEmpresa"
                    value={formData.nombreEmpresa}
                    onChange={handleChange}
                    placeholder="Mi Empresa S.A."
                    required
                  />
                </div>
              </div>

              <div className={styles.formRow}>
                <div className={styles.formGroup}>
                  <label htmlFor="pais">País*</label>
                  <select id="pais" name="pais" value={formData.pais} onChange={handleChange} required>
                    <option value="">Seleccione un país</option>
                  </select>
                </div>

                <div className={styles.formGroup}>
                  <label htmlFor="tamano">Tamaño de la empresa*</label>
                  <select id="tamano" name="tamano" value={formData.tamano} onChange={handleChange} required>
                    <option value="">Seleccione</option>
                  </select>
                </div>
              </div>

              <div className={styles.formGroup}>
                <label htmlFor="tipo">Tipo de Empresa*</label>
                <select id="tipo" name="tipo" value={formData.tipo} onChange={handleChange} required>
                  <option value="">Seleccione</option>
                </select>
              </div>

              <div className={styles.checkboxGroup}>
                <input
                  type="checkbox"
                  id="terminos"
                  name="terminos"
                  checked={formData.terminos}
                  onChange={handleChange}
                  required
                />
                <label htmlFor="terminos">Acepto recibir notificaciones por correo electrónico*</label>
              </div>

              <button type="submit" className={styles.button} disabled={isSubmitting}>
                {isSubmitting ? "Enviando..." : "Solicitar Demo"}
              </button>
            </form>
          </div>
        </div>
      </div>
    </main>
  );
}