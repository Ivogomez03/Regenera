"use client"

import { useState } from "react";
import styles from "./login.module.css";
import { Eye, EyeOff } from "lucide-react";
import Image from 'next/image';
import Link from 'next/link';
import axios from "axios";

export default function LoginPage() {
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setIsLoading(true)

    try {
        const respuesta = await axios.post("/api/auth/login", {
        email: usuario,
        password: password,
    });

    localStorage.setItem("auth", JSON.stringify(respuesta.data));
    console.log("Token guardado:", respuesta.data);
    window.location.href = "/";
    } catch (err) {
        if (err.response && (err.response.status === 401 || err.response.status === 403 || err.response.status === 400)) {
            const mensajeBackend = err.response?.data?.message || "El correo o contraseña no existe";
            setError(mensajeBackend);
        } else {
            setError("Error al conectarse al servidor. Intentá nuevamente.");
        }
    } finally{
      setIsLoading(false)
    }

  };



  return (
    <main className={styles.main}>
        {/* Glassmorphic Header */}
        <header className={styles.topHeader}>
        <Link href="/">
          <Image src="/logo2.png" alt="Regenera Logo" width={140} height={60} className={styles.logo} priority />
        </Link>
      </header>

      {/* Login Section with Gradient Background */}
        <div className={styles.loginPage}>
            <div className={styles.backgroundPattern} />

            <div className={styles.loginContainer}>
                <div className={styles.loginCard}>
                    <div className={styles.cardHeader}>
                        <h1>Bienvenido de nuevo</h1>
                        <p>Ingresá a tu cuenta para continuar</p>
                    </div>

                    <form className={styles.form} onSubmit={handleSubmit}>
                        <div className={styles.formGroup}>
                            <label htmlFor="usuario">Correo electrónico:</label>
                            <input 
                                type="usuario" 
                                id="usuario" 
                                name="usuario"  
                                value={usuario}
                                onChange={(e) => setUsuario(e.target.value)}
                                placeholder="tu@email.com"
                                required
                                autoComplete="email"
                            />
                        </div>

                        <div className={styles.formGroup}>
                            <label htmlFor="password">Contraseña</label>
                            <div className={styles.passwordWrapper}>
                                <input 
                                    type={showPassword ? "text" : "password"} 
                                    id="password" 
                                    name="password"  
                                    value={password} 
                                    onChange={(e) => setPassword(e.target.value)}
                                    placeholder="••••••••"
                                    required
                                    autoComplete="current-password"
                                    className={styles.passwordInput} 
                                />

                                <button 
                                    type="button" 
                                    onClick={() => setShowPassword(!showPassword)} 
                                    className={styles.togglePassword}
                                    aria-label={showPassword ? "Ocultar contraseña" : "Mostrar contraseña"}
                                >
                                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                                </button>
                            </div>
                        </div>
                        
                        {error && (
                            <div className={styles.errorMessage}>
                            <span>{error}</span>
                            </div>
                        )}

                        <button className={styles.button}
                        disabled={isLoading}>
                            {isLoading ? "Iniciando sesión..." : "Iniciar Sesión"}
                        </button>

                        <div className={styles.divider}>
                            <span>o</span>
                        </div>

                        <p className={styles.registerLink}>
                            ¿No tenés una cuenta?{" "}
                            <Link href="/login/registro">
                            <strong>Registrate</strong>
                            </Link>
                        </p>
                    </form>
                </div>
            </div>
        </div>
    </main>
  );
}