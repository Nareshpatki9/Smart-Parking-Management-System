import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

import API from "../services/api";

function Login() {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        username: "",
        password: ""
    });

    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    // =====================================================
    // HANDLE INPUT CHANGE
    // =====================================================

    const handleChange = (event) => {

        const { name, value } = event.target;

        setFormData({
            ...formData,
            [name]: value
        });
    };

    // =====================================================
    // HANDLE LOGIN
    // =====================================================

    const handleSubmit = async (event) => {

        event.preventDefault();

        setError("");

        const username =
            formData.username.trim();

        const password =
            formData.password;

        if (!username) {

            setError(
                "Username is required."
            );

            return;
        }

        if (!password) {

            setError(
                "Password is required."
            );

            return;
        }

        setLoading(true);

        try {

            const response =
                await API.post(
                    "/auth/login",
                    {
                        username,
                        password
                    }
                );

            const data =
                response.data;

            // =================================================
            // CLEAR OLD LOGIN DATA
            // =================================================

            localStorage.removeItem("token");
            localStorage.removeItem("username");
            localStorage.removeItem("role");

            // =================================================
            // SAVE NEW LOGIN DATA
            // =================================================

            localStorage.setItem(
                "token",
                data.token
            );

            localStorage.setItem(
                "username",
                data.username
            );

            localStorage.setItem(
                "role",
                data.role
            );

            // =================================================
            // ROLE-BASED REDIRECT
            // =================================================

            if (data.role === "ADMIN") {

                navigate("/admin");

            } else {

                navigate("/user");
            }

        } catch (error) {

            console.error(
                "Login error:",
                error
            );

            if (error.response?.status === 401) {

                setError(
                    "Invalid username or password."
                );

            } else if (
                error.response?.data?.message
            ) {

                setError(
                    error.response.data.message
                );

            } else if (
                error.response?.status === 500
            ) {

                setError(
                    "Server error. Please try again."
                );

            } else {

                setError(
                    "Unable to login. Please check the backend server."
                );
            }

        } finally {

            setLoading(false);
        }
    };

    return (

        <div className="auth-page">

            <div className="auth-card">

                {/* =================================================
                    LOGO
                ================================================= */}

                <div className="auth-logo">
                    🚗
                </div>

                <h1>
                    Smart Parking
                </h1>

                <p className="auth-subtitle">
                    Parking Management System
                </p>

                <h2>
                    Login
                </h2>


                {/* =================================================
                    ERROR MESSAGE
                ================================================= */}

                {error && (
                    <div className="error-message">
                        {error}
                    </div>
                )}


                {/* =================================================
                    LOGIN FORM
                ================================================= */}

                <form onSubmit={handleSubmit}>

                    <label htmlFor="username">
                        Username
                    </label>

                    <input
                        id="username"
                        type="text"
                        name="username"
                        placeholder="Enter username"
                        value={formData.username}
                        onChange={handleChange}
                        autoComplete="username"
                        required
                    />


                    <label htmlFor="password">
                        Password
                    </label>

                    <input
                        id="password"
                        type="password"
                        name="password"
                        placeholder="Enter password"
                        value={formData.password}
                        onChange={handleChange}
                        autoComplete="current-password"
                        required
                    />


                    <button
                        type="submit"
                        disabled={loading}
                    >
                        {loading
                            ? "Logging in..."
                            : "Login"
                        }
                    </button>

                </form>


                {/* =================================================
                    REGISTER LINK
                ================================================= */}

                <p className="auth-footer">

                    Don't have an account?

                    {" "}

                    <Link to="/register">
                        Register
                    </Link>

                </p>

            </div>

        </div>
    );
}

export default Login;