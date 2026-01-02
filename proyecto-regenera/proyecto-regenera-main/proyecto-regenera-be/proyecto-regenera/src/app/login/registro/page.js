"use client";

import { useState } from "react";
import styles from "../login.module.css";
import { Eye, EyeOff } from "lucide-react"
import Image from 'next/image';
import Link from 'next/link';
import axios from "axios";


export default function RegistroPage() {
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [usuario, setUsuario] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [repPassword, setRepPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false)
  const [showRepPassword, setShowRepPassword] = useState(false)
  const [isLoading, setIsLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const [success, setSuccess] = useState("");
  const [Error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    const newErrors = {};

    //VALIDACIONES
    // ---- Nombre ----
    if (nombre.length > 20) {
        newErrors.nombre = "El nombre no puede superar los 20 caracteres";
    }

    // ---- Apellido ----
    if (apellido.length > 20) {
        newErrors.apellido = "El apellido no puede superar los 20 caracteres";
    }

    // ---- Usuario ----
    if (usuario.length < 3) {
        newErrors.usuario = "El usuario debe tener al menos 3 caracteres";
    } else if (usuario.length > 20) {
        newErrors.usuario = "El usuario no puede superar los 20 caracteres";
    }

    // ---- Email ----
    if (email.length > 50) {
        newErrors.email = "El email no puede superar los 50 caracteres";
    } else {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
        newErrors.email = "El email no tiene un formato válido";
        }
    }

    // ---- Password ----
    const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{6,20}$/;
    if (!passwordRegex.test(password)) {
        newErrors.password =
            "La contraseña debe tener entre 6 y 20 caracteres, incluir al menos una letra mayúscula, una letra minúscula y un número";
    }

    // ---- Repetición de contraseña ----
    if (password !== repPassword) {
        newErrors.repPassword = "Las contraseñas no coinciden";
    }

    // ---- Mostrar errores o enviar formulario ----
    setErrors(newErrors);

    if (Object.keys(newErrors).length === 0) {
        try {
            setIsLoading(true);

            const respuesta = await axios.post("/api/auth/register", {
                nombre: nombre,
                apellido: apellido,
                nombreUsuario: usuario,
                email: email,
                password: password,
                repeatPassword: repPassword,
            });

            alert(respuesta.data.message); // "Te enviamos un mail para verificar la cuenta"
            window.location.href = "/login";

            setNombre("");
            setApellido("");
            setUsuario("");
            setEmail("");
            setPassword("");
            setRepPassword("");

            } catch (err) {
                setError("Error en el registro. Verificá tus datos.");
                console.error(err);
            } finally {
                setIsLoading(false);
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

        <div className={styles.loginPage}>
            <div className={styles.backgroundPattern} />

            <div className={styles.loginContainer}>
                <div className={styles.loginCard}>
                    <div className={styles.cardHeader}>
                        <h1>Crear cuenta</h1>
                        <p>Comenzá tu camino hacia la sostenibilidad</p>
                    </div>
                
                    <form className={styles.form} onSubmit={handleSubmit}>
                        <div className={styles.formGroup}>
                            <label htmlFor="nombre">Nombre</label>
                            <input 
                                id="nombre" 
                                name="nombre" 
                                value={nombre}
                                onChange={(e) => setNombre(e.target.value)} 
                                className={errors.nombre ? styles.inputError : ""}
                                placeholder="Juan"
                                autoComplete="given-name"
                                required 
                            />
                            {errors.nombre && <p className={styles.error}>{errors.nombre}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label htmlFor="apellido">Apellido</label>
                            <input 
                                id="apellido"  
                                name="apellido" 
                                value={apellido}
                                onChange={(e) => setApellido(e.target.value)} 
                                className={errors.apellido ? styles.inputError : ""}
                                placeholder="Pérez"
                                autoComplete="family-name"
                                required 
                            />
                            {errors.apellido && <p className={styles.error}>{errors.apellido}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label htmlFor="usuario">Nombre de Usuario</label>
                            <input 
                                id="usuario" 
                                name="usuario" 
                                value={usuario}
                                onChange={(e) => setUsuario(e.target.value)} 
                                className={errors.usuario ? styles.inputError : ""}
                                placeholder="juanperez"
                                autoComplete="username"
                                required 
                            />
                            {errors.usuario && <p className={styles.error}>{errors.usuario}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label htmlFor="email">Email</label>
                            <input 
                                type="email" 
                                id="email" 
                                name="email" 
                                value={email}
                                onChange={(e) => setEmail(e.target.value)} 
                                className={errors.email? styles.inputError : ""}
                                placeholder="tu@email.com"
                                autoComplete="email"
                                required 
                            />
                            {errors.email && <p className={styles.error}>{errors.email}</p>}
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
                                    className={errors.password ? `${styles.inputError} ${styles.passwordInput}` : styles.passwordInput}
                                    placeholder="••••••••"
                                    autoComplete="new-password"
                                    required 
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
                            {errors.password && <p className={styles.error}>{errors.password}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label htmlFor="repPassword">Repetir contraseña</label>
                            <div className={styles.passwordWrapper}>
                                <input 
                                    type={showRepPassword ? "text" : "password"}
                                    id="repPassword" 
                                    name="repPassword" 
                                    value={repPassword}
                                    onChange={(e) => setRepPassword(e.target.value)} 
                                    className={errors.repPassword ? `${styles.inputError} ${styles.passwordInput}` : styles.passwordInput}
                                    placeholder="••••••••"
                                    autoComplete="new-password"
                                    required 
                                />
                                <button
                                    type="button"
                                    onClick={() => setShowRepPassword(!showRepPassword)}
                                    className={styles.togglePassword}
                                    aria-label={showRepPassword ? "Ocultar contraseña" : "Mostrar contraseña"}
                                >
                                    {showRepPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                                </button>
                            </div>  
                            {errors.repPassword && <p className={styles.error}>{errors.repPassword}</p>}
                        </div>

                        {Error && (
                            <div className={styles.errorMessage}>
                            <span>{Error}</span>
                            </div>
                        )}
                    

                    <button className={styles.button} disabled={isLoading}>
                        {isLoading ? "Creando cuenta..." : "Registrarme"}
                    </button>

                    <div className={styles.divider}>
                        <span>o</span>
                    </div>
                    
                    <p className={styles.registerLink}>
                        ¿Ya tenés una cuenta?{" "}
                        <Link href="/login">
                        <strong>Iniciar Sesión</strong>
                        </Link>
                    </p>
                </form>
            </div>
            </div>
        </div>
    </main>
  );
}