import axios from "axios"

const axiosClient = axios.create()

axiosClient.interceptors.request.use(
  (config) => {
    const auth = localStorage.getItem("auth")
    if (auth) {
      try {
        const authData = JSON.parse(auth)
        const token = authData.token || authData.accessToken || authData
        if (token) {
          config.headers.Authorization = `Bearer ${token}`
        }
      } catch (error) {
        console.error("Error parsing auth data:", error)
      }
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("auth")
      window.location.href = "/login"
    }
    return Promise.reject(error)
  },
)

export default axiosClient
