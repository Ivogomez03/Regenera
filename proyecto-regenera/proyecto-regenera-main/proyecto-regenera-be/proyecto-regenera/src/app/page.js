"use client"

import { useState, useEffect } from "react"
import Image from "next/image"
import Link from "next/link"
import styles from "./page.module.css"
import { Sparkles, Leaf, BarChart3, FileText, Target, Shield } from "lucide-react"

export default function Home() {
  const [isLogged, setIsLogged] = useState(false)
  const [scrollY, setScrollY] = useState(0)

  useEffect(() => {
    const authData = localStorage.getItem("auth")
    if (authData) setIsLogged(true)

    const handleScroll = () => {
      setScrollY(window.scrollY)
    }

    window.addEventListener("scroll", handleScroll)
    return () => window.removeEventListener("scroll", handleScroll)
  }, [])

  const handleLogout = () => {
    localStorage.removeItem("auth")
    setIsLogged(false)
    window.location.href = "/"
  }

  const scrollToSection = (id) => {
    const element = document.getElementById(id)
    if (element) {
      element.scrollIntoView({ behavior: "smooth" })
    }
  }

  return (
    <main className={styles.main}>
      <nav className={`${styles.navbar} ${scrollY > 50 ? styles.navbarScrolled : ""}`}>
        <div className={styles.navContainer}>
          <Link href="/" className={styles.logoLink}>
            <Image src="/logo2.png" alt="REGENERA Logo" width={140} height={60} className={styles.logo} />
          </Link>

          <ul className={styles.navLinks}>
            <li onClick={() => scrollToSection("about")}>¿Qué es .....?</li>
            <li onClick={() => scrollToSection("modules")}>Módulos</li>
            <li onClick={() => scrollToSection("sga")}>¿Qué es un SGA?</li>
            <li onClick={() => scrollToSection("benefits")}>Beneficios</li>
          </ul>

          <div className={styles.navRight}>
            {isLogged ? (
              <button onClick={handleLogout} className={styles.ctaBtn}>
                Cerrar Sesión
              </button>
            ) : (
              <Link href="/login" className={styles.ctaBtn}>
                Iniciar Sesión
              </Link>
            )}
          </div>
        </div>
      </nav>

      <section className={styles.hero}>
        <div className={styles.videoWrapper}>
          <video className={styles.videoBackground} autoPlay muted loop playsInline>
            <source src="/Video Plataforma.mp4" type="video/mp4" />
          </video>
          <div className={styles.videoOverlay} />
        </div>

        <div className={styles.heroContent}>
          <div className={styles.heroText}>
            <h1 className={styles.heroTitle}>
              Impulsamos la transformación digital de los sistemas de gestión ambiental
            </h1>
            <p className={styles.heroSubtitle}>
              Tecnología al servicio de la sostenibilidad, para una gestión ambiental más eficiente, transparente y
              colaborativa
            </p>
            {!isLogged && (
              <Link href="/demo" className={styles.heroBtn}>
                <Sparkles size={20} />
                <span>Empezar Ahora</span>
              </Link>
            )}
          </div>
        </div>
      </section>

      <section id="about" className={styles.aboutSection}>
        <div className={styles.container}>
          <h2 className={styles.sectionTitle}>¿Qué es .... y quienes somos?</h2>
          <p className={styles.aboutText}>
            Es una <strong>plataforma digital de gestión ambiental</strong> diseñada para asistir a PyMEs industriales y
            cooperativas en la adopción progresiva de un <strong>Sistema de Gestión Ambiental (SGA)</strong> alineado
            con los lineamientos de la norma <strong>ISO 14001</strong>.
          </p>
          <p className={styles.aboutText}>
            Esta herramienta integra funcionalidades clave que permiten:
          </p>
          <div className={styles.featuresList}>
            <div className={styles.featureItem}>
              <div className={styles.featureNumber}>1</div>
              <p>
                <strong>Estandarizar y digitalizar</strong> el registro de información organizacional y ambiental
              </p>
            </div>
            <div className={styles.featureItem}>
              <div className={styles.featureNumber}>2</div>
              <p>
                <strong>Identificar y evaluar</strong> aspectos e impactos ambientales
              </p>
            </div>
            <div className={styles.featureItem}>
              <div className={styles.featureNumber}>3</div>
              <p>
                <strong>Acceder y consultar</strong> el marco normativo aplicable según ubicación y rubro
              </p>
            </div>
            <div className={styles.featureItem}>
              <div className={styles.featureNumber}>4</div>
              <p>
                <strong>Planificar y dar seguimiento</strong> a objetivos, metas e indicadores de desempeño ambiental
              </p>
            </div>
            <div className={styles.featureItem}>
              <div className={styles.featureNumber}>5</div>
              <p>
                <strong>Generar informes y reportes automáticos</strong>, facilitando la trazabilidad y la mejora
                continua
              </p>
            </div>
          </div>
          <p className={styles.aboutText}> <br /> </p>

          <p className={styles.aboutText}>
            La plataforma actúa como una solución integral de gestión que <strong>fortalece la sostenibilidad operativa</strong>,
            mejora la toma de decisiones, y <strong>aumenta la competitividad ambiental</strong> de las organizaciones usuarias,
            promoviendo su acceso a mercados que exigen estándares verificables en materia de desempeño ambiental.
          </p>
        </div>
      </section>

      <section id="modules" className={styles.modulesSection}>
        <div className={styles.container}>
          <h2 className={styles.sectionTitle}>Sobre la plataforma (nombre de la plataforma)</h2>

          <div className={styles.bentoGrid}>
            <Link href="/modulo1" className={styles.bentoCard}>
              <img src="/imagen 2.png" alt="Registro y perfil de la Organización" />
              <h3>Registro y Perfil de la Organización</h3>
              <p>
                Acompañamos a PyMEs y cooperativas en la adopción progresiva de un
                Sistema de Gestión Ambiental conforme a ISO 14001. Nuestra plataforma
                permite establecer la política y los objetivos ambientales, roles y
                responsabilidades para iniciar o fortalecer tu estrategia ambiental.
              </p>
            </Link>

            <Link href="/modulo2" className={styles.bentoCard}>
              <img src="/imagen 3.png" alt="Planificación de la gestión ambiental" />
              <h3>Planificación de la gestión ambiental</h3>
              <p>Planificá tu gestión ambiental: identificá aspectos e impactos ambientales significativos
                y tus requisitos legales. Definí objetivos, metas e indicadores para mejorar el desempeño
                ambiental de tu organización.</p>
            </Link>
          </div>
        </div>
      </section>

      <section id="sga" className={styles.sgaSection}>
        <div className={styles.container}>
          <div className={styles.splitContent}>
            <div className={styles.splitText}>
              <h2 className={styles.sectionTitle}>¿Qué es un Sistema de Gestión Ambiental?</h2>

              <p>
                La gestión ambiental comprende todas las acciones orientadas para prevenir, controlar y reducir los
                impactos ambientales que genera una organización derivados de sus actividades, productos o servicios.
              </p>

              <p>
                Cuando estas acciones se planifican y organizan de forma integrada dentro de la empresa, constituyen el
                Sistema de Gestión Ambiental (SGA), que establece un proceso estructurado para la mejora continua.
              </p>

              <p className={styles.texto}>
                Las organizaciones pueden utilizar los requisitos de la norma ISO 14001 para diseñar su SGA.
                Se trata de una norma de aplicación voluntaria e internacionalmente reconocida. que determina y
                delimita los requisitos fundamentales para implementar un Sistema de Gestión Ambiental, permitiendo
                a las organizaciones gestionar sus responsabilidades ambientales de forma sistemática.
              </p>

              <p className={styles.texto}>
                Por lo tanto, la implantación de un SGA posibilita identificar, controlar y reducir los
                impactos ambientales significativos, cumplir la legislación aplicable y mejorar el desempeño
                ambiental de manera continua.
              </p>
            </div>

            <div className={styles.splitImage}>
              <div className={styles.imageWrapper}>
                <Image
                  src="/imagen de muestra2.jpg"
                  alt="Ilustración sobre gestión ambiental"
                  width={600}
                  height={400}
                  className={styles.floatingImage}
                />
              </div>
            </div>
          </div>
        </div>
      </section>

      <section id="benefits" className={styles.benefitsSection}>
        <div className={styles.container}>
          <h2 className={styles.sectionTitle}>¿Para qué un Software de Gestión Ambiental?</h2>

          <p className={styles.benefitsIntro}>
            Implementar un software de gestión ambiental permite a las organizaciones dar un salto de calidad en la
            gestión ambiental y en la eficiencia de sus operaciones. Ya no se trata solo de cumplir con normas, sino de
            gestionar de forma proactiva los impactos ambientales y convertir la sostenibilidad en una ventaja
            competitiva.
          </p>

          <div className={styles.benefitsGrid}>

            {/* CARD 1 */}
            <div className={styles.benefitCard}>
              <div className={styles.cardHeader}>
                <Leaf className={styles.benefitIcon} />
                <h3>Centralización</h3>
              </div>
              <p>Centraliza toda la información ambiental en un solo lugar accesible</p>
            </div>

            {/* CARD 2 */}
            <div className={styles.benefitCard}>
              <div className={styles.cardHeader}>
                <Shield className={styles.benefitIcon} />
                <h3>Cumplimiento</h3>
              </div>
              <p>Facilita el cumplimiento normativo de manera eficiente</p>
            </div>

            {/* CARD 3 */}
            <div className={styles.benefitCard}>
              <div className={styles.cardHeader}>
                <BarChart3 className={styles.benefitIcon} />
                <h3>Análisis</h3>
              </div>
              <p>Mide y analiza indicadores clave de desempeño ambiental</p>
            </div>

            {/* CARD 4 */}
            <div className={styles.benefitCard}>
              <div className={styles.cardHeader}>
                <FileText className={styles.benefitIcon} />
                <h3>Automatización</h3>
              </div>
              <p>Automatiza reportes e informes técnicos profesionales</p>
            </div>

          </div>
        </div>
      </section>

      <section className={styles.ctaSection}>
        <div className={styles.ctaContent}>
          <h2>Hacé la diferencia con REGENERA</h2>
          <p>Comienza tu transformación digital hacia la sostenibilidad</p>
          {!isLogged && (
            <Link href="/demo" className={styles.ctaLargeBtn}>
              Comenzar Ahora
            </Link>
          )}
        </div>
      </section>
    </main>
  )
}

