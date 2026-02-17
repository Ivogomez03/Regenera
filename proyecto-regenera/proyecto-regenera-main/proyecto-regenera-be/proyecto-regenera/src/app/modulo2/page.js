'use client';

import Link from 'next/link';
import styles from "./modulo2.module.css";
import Image from 'next/image';
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { FileText, Scale, TrendingUp, Target, FileChartColumnIncreasing } from "lucide-react"

export default function Modulo2Page() {
    const [isCheckingAuth, setIsCheckingAuth] = useState(true);
    const router = useRouter();

    useEffect(() => {
        const auth = localStorage.getItem("auth");
        if (!auth) {
            router.push("/login");
        } else {
            setIsCheckingAuth(false);
        }
    }, [router]);

    if (isCheckingAuth) {
        return (
            <div className={styles.loaderContainer}>
                <img src="/icons8-hilandero.gif" alt="Cargando" className={styles.loaderImage} />
                <p className={styles.loaderText}>Cargando...</p>
            </div>
        );

    }

    const modules = [
        {
            title: "Matriz de aspectos e impactos ambientales",
            href: "/modulo2/matrizAmbiental",
            icon: FileText,
            description: "Identifica y evalúa aspectos ambientales",
            color: "teal",
        },
        {
            title: "Matriz legal ambiental",
            href: "/modulo2/matrizLegal",
            icon: Scale,
            description: "Gestiona requisitos legales aplicables",
            color: "orange",
        },
        {
            title: "Indicadores ambientales",
            href: "/modulo2/indicadoresAmbientales",
            icon: TrendingUp,
            description: "Monitorea el desempeño ambiental",
            color: "green",
        },
        {
            title: "Matriz de objetivos, metas e indicadores",
            href: "/modulo2/metasObjetivos",
            icon: FileChartColumnIncreasing,
            description: "Relaciona metas, objetivos e indicadores para seguimiento",
            color: "purple",
        },
        {
            title: "Revisiones",
            href: "/modulo2/revisiones",
            icon: Target,
            description: "Revisa y audita el sistema ambiental",
            color: "blue",
        }

    ]

    return (
        <main className={styles.main}>
            <header className={styles.topHeader}>
                <Link href="/">
                    <Image src="/logo2.png" alt="Regenera Logo" width={140} height={60} className={styles.logo} priority />
                </Link>
            </header>

            <div className={styles.backgroundPattern}></div>

            <div className={styles.mod2Page}>
                <div className={styles.headerSection}>
                    <h1 className={styles.title}>Planificación de la Gestión Ambiental</h1>
                    <p className={styles.subtitle}>Selecciona un módulo para comenzar</p>
                </div>

                <div className={styles.grid}>
                    {modules.map((module, index) => {
                        const IconComponent = module.icon
                        return (
                            <Link key={index} href={module.href} className={`${styles.card} ${styles[module.color]}`}>
                                <div className={styles.iconWrapper}>
                                    <IconComponent className={styles.icon} />
                                </div>
                                <div className={styles.cardContent}>
                                    <h3>{module.title}</h3>
                                    <p>{module.description}</p>
                                </div>
                                <div className={styles.cardArrow}>→</div>
                            </Link>
                        )
                    })}
                </div>
            </div>
        </main>
    );
}