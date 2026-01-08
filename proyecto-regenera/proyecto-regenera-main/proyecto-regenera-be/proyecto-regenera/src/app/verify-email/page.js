"use client";

import { useEffect, useState, Suspense } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import axios from "axios";
import styles from "../login/login.module.css"; // Reusamos estilos o crea uno nuevo
import Link from "next/link";

function VerifyContent() {
    const searchParams = useSearchParams();
    const token = searchParams.get("token");
    const router = useRouter();

    const [status, setStatus] = useState("verifying"); // verifying, success, error

    useEffect(() => {
        if (!token) {
            setStatus("error");
            return;
        }

        // Llamada automática al backend para validar
        axios.get(`http://localhost:8080/api/auth/verify?token=${token}`)
            .then(() => {
                setStatus("success");
                // Opcional: Redirigir al login después de 3 segundos
                setTimeout(() => router.push("/login"), 3000);
            })
            .catch((err) => {
                console.error(err);
                setStatus("error");
            });
    }, [token, router]);

    return (
        <div className={styles.loginContainer} style={{ minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center" }}>
            <div className={styles.loginCard} style={{ textAlign: "center", padding: "40px" }}>

                {status === "verifying" && (
                    <>
                        <h2>Verificando tu cuenta...</h2>
                        <p>Por favor esperá un momento.</p>
                    </>
                )}

                {status === "success" && (
                    <>
                        <h2 style={{ color: "green" }}>¡Cuenta Activada! ✅</h2>
                        <p>Tu correo ha sido verificado correctamente.</p>
                        <p>Serás redirigido al inicio de sesión en unos segundos...</p>
                        <Link href="/login" className={styles.button} style={{ display: "inline-block", marginTop: "20px", textDecoration: "none" }}>
                            Ir a Iniciar Sesión
                        </Link>
                    </>
                )}

                {status === "error" && (
                    <>
                        <h2 style={{ color: "red" }}>Error de Verificación ❌</h2>
                        <p>El enlace es inválido o ha expirado.</p>
                        <Link href="/register" className={styles.registerLink}>
                            Volver a registrarse
                        </Link>
                    </>
                )}
            </div>
        </div>
    );
}

// Necesario envolver en Suspense por el uso de useSearchParams en Next.js
export default function VerifyPage() {
    return (
        <Suspense fallback={<div>Cargando...</div>}>
            <VerifyContent />
        </Suspense>
    );
}