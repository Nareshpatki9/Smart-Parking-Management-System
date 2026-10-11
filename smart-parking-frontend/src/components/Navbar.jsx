import { useNavigate } from "react-router-dom";

function Navbar() {

    const navigate = useNavigate();

    const username =
        localStorage.getItem("username");

    const role =
        localStorage.getItem("role");

    const logout = () => {

        localStorage.removeItem("token");
        localStorage.removeItem("username");
        localStorage.removeItem("role");

        navigate("/");
    };

    const goToDashboard = () => {

        if (role === "ADMIN") {
            navigate("/admin");
        } else {
            navigate("/user");
        }
    };

    return (

        <nav className="navbar">

            {/* =====================================================
                BRAND
            ===================================================== */}

            <div
                className="navbar-brand"
                onClick={goToDashboard}
            >
                Smart Parking
            </div>


            {/* =====================================================
                NAVIGATION LINKS
            ===================================================== */}

            <div className="navbar-links">

                {/* =================================================
                    USER NAVIGATION
                ================================================= */}

                {role === "USER" && (
                    <>

                        <button
                            onClick={() =>
                                navigate("/user")
                            }
                        >
                            Dashboard
                        </button>

                        <button
                            onClick={() =>
                                navigate("/vehicle-entry")
                            }
                        >
                            Vehicle Entry
                        </button>

                        <button
                            onClick={() =>
                                navigate("/vehicle-exit")
                            }
                        >
                            Vehicle Exit
                        </button>

                        <button
                            onClick={() =>
                                navigate("/vehicle-list")
                            }
                        >
                            Vehicle History
                        </button>

                        <button
                            onClick={() =>
                                navigate("/billing-history")
                            }
                        >
                            Billing
                        </button>

                    </>
                )}


                {/* =================================================
                    ADMIN NAVIGATION
                ================================================= */}

                {role === "ADMIN" && (
                    <>

                        <button
                            onClick={() =>
                                navigate("/admin")
                            }
                        >
                            Dashboard
                        </button>

                        <button
                            onClick={() =>
                                navigate("/vehicle-entry")
                            }
                        >
                            Vehicle Entry
                        </button>

                        <button
                            onClick={() =>
                                navigate("/vehicle-exit")
                            }
                        >
                            Vehicle Exit
                        </button>

                        <button
                            onClick={() =>
                                navigate("/vehicle-list")
                            }
                        >
                            Vehicles
                        </button>

                        <button
                            onClick={() =>
                                navigate("/billing-history")
                            }
                        >
                            Billing
                        </button>

                    </>
                )}


                {/* =================================================
                    LOGGED-IN USER
                ================================================= */}

                <span className="navbar-user">
                    {username}
                </span>


                {/* =================================================
                    LOGOUT
                ================================================= */}

                <button
                    className="navbar-logout"
                    onClick={logout}
                >
                    Logout
                </button>

            </div>

        </nav>
    );
}

export default Navbar;
