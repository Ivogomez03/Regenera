import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";

export default function useValidarAutenticacion() {
    const [isCheckingAuth, setIsCheckingAuth] = useState(true);
    const router = useRouter();

    useEffect(() => {
        const auth = localStorage.getItem("auth");

        if (!auth) {
            router.replace("/login"); // mejor que push
        } else {
            setIsCheckingAuth(false);
        }
    }, [router]);

    return { isCheckingAuth };
}