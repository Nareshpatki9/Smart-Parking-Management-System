import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

import API from "../services/api";

function Register() {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        username: "",
        password: ""
    });

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
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
    // HANDLE REGISTER
    // =====================================================

    const handleSubmit = async (event) => {

        event.preventDefault();

        setError("");
        setSuccess("");

        const username =
            formData.username.trim();

        const password =
            formData.password;

        // =================================================
        // FRONTEND VALIDATION
        // =================================================

        if (!username) {

            setError(
                "Username is required."
            );

            return;
        }

        if (username.length < 3) {

            setError(
                "Username must contain at least 3 characters."
            );

            return;
        }

        if (!password) {

            setError(
                "Password is required."
            );

            return;
        }

        if (password.length < 6) {

            setError(
                "Password must contain at least 6 characters."
            );

            return;
        }

        setLoading(true);

        try {

            const response =
                await API.post(
                    "/auth/register",
                    {
                        username,
                        password
                    }
                );

            const message =
                response.data?.message ||
                "Registration successful!";

            setSuccess(
                `${message} Redirecting to login...`
            );

            setFormData({
                username: "",
                password: ""
            });

            setTimeout(() => {
                navigate("/");
            }, 1500);

        } catch (error) {

            console.error(
                "Registration error:",
                error
            );

            // =================================================
            // DUPLICATE USERNAME
            // =================================================

            if (error.response?.status === 409) {

                setError(
                    error.response?.data?.message ||
                    "Username already exists."
                );

            } else if (
                error.response?.data?.messages
            ) {

                const messages =
                    error.response.data.messages;

                const firstMessage =
                    Object.values(messages)[0];

                setError(
                    firstMessage ||
                    "Please check your registration details."
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
                    "Registration failed. Please try again."
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
                    Create your account
                </p>

                <h2>
                    Register
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
                    SUCCESS MESSAGE
                ================================================= */}

                {success && (
                    <div className="success-message">
                        {success}
                    </div>
                )}


                {/* =================================================
                    REGISTER FORM
                ================================================= */}

                <form onSubmit={handleSubmit}>

                    <label htmlFor="username">
                        Username
                    </label>

                    <input
                        id="username"
                        type="text"
                        name="username"
                        placeholder="Choose username"
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
                        placeholder="Create password"
                        value={formData.password}
                        onChange={handleChange}
                        autoComplete="new-password"
                        required
                    />


                    <button
                        type="submit"
                        disabled={loading}
                    >
                        {loading
                            ? "Creating account..."
                            : "Create Account"
                        }
                    </button>

                </form>


                {/* =================================================
                    LOGIN LINK
                ================================================= */}

                <p className="auth-footer">

                    Already have an account?

                    {" "}

                    <Link to="/">
                        Login
                    </Link>

                </p>

            </div>

        </div>
    );
}

export default Register;
