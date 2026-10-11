import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import API from "../services/api";
import Navbar from "../components/Navbar";

function VehicleList() {

    const navigate = useNavigate();

    const role =
        localStorage.getItem("role");


    const [vehicles, setVehicles] =
        useState([]);

    const [loading, setLoading] =
        useState(true);

    const [error, setError] =
        useState("");


    // =====================================================
    // LOAD VEHICLES
    // =====================================================

    const loadVehicles = async () => {

        setLoading(true);
        setError("");

        try {

            const response =
                await API.get(
                    "/vehicle/all"
                );

            console.log(
                "Vehicle History Response:",
                response.data
            );

            setVehicles(
                Array.isArray(response.data)
                    ? response.data
                    : []
            );

        } catch (error) {

            console.error(
                "Vehicle List Error:",
                error
            );

            console.error(
                "Status:",
                error.response?.status
            );

            console.error(
                "Response:",
                error.response?.data
            );


            // =================================================
            // 401 - AUTHENTICATION EXPIRED
            // =================================================

            if (error.response?.status === 401) {

                localStorage.removeItem("token");
                localStorage.removeItem("username");
                localStorage.removeItem("role");

                navigate("/");

                return;
            }


            // =================================================
            // 403 - FORBIDDEN
            // =================================================

            if (error.response?.status === 403) {

                setError(
                    error.response?.data?.message ||
                    error.response?.data?.error ||
                    "You are not authorized to view vehicle history."
                );

                return;
            }


            // =================================================
            // 404 - ENDPOINT NOT FOUND
            // =================================================

            if (error.response?.status === 404) {

                setError(
                    "Vehicle history API was not found. Please check VehicleController."
                );

                return;
            }


            // =================================================
            // SERVER ERROR
            // =================================================

            if (error.response?.status >= 500) {

                setError(
                    "Server error. Please check the Spring Boot backend."
                );

                return;
            }


            // =================================================
            // NETWORK ERROR
            // =================================================

            if (!error.response) {

                setError(
                    "Cannot connect to backend. Make sure Spring Boot is running on port 8080."
                );

                return;
            }


            // =================================================
            // OTHER ERROR
            // =================================================

            setError(
                error.response?.data?.message ||
                error.response?.data?.error ||
                "Unable to load vehicle history."
            );

        } finally {

            setLoading(false);
        }
    };


    // =====================================================
    // INITIAL LOAD
    // =====================================================

    useEffect(() => {

        loadVehicles();

    }, []);


    // =====================================================
    // DASHBOARD NAVIGATION
    // =====================================================

    const goToDashboard = () => {

        if (role === "ADMIN") {

            navigate("/admin");

        } else {

            navigate("/user");

        }
    };


    // =====================================================
    // FORMAT DATE
    // =====================================================

    const formatDate = (dateValue) => {

        if (!dateValue) {
            return "-";
        }

        const date =
            new Date(dateValue);

        if (Number.isNaN(date.getTime())) {
            return "-";
        }

        return date.toLocaleString();
    };


    // =====================================================
    // UI
    // =====================================================

    return (

        <>

            <Navbar />


            <div className="dashboard-container">

                {/* =================================================
                    HEADER
                ================================================= */}

                <div className="dashboard-header">

                    <div>

                        <h1>
                            Vehicle History
                        </h1>

                        <p>
                            {role === "ADMIN"
                                ? "All parking vehicle records"
                                : "Your parking vehicle records"
                            }
                        </p>

                    </div>


                    <button
                        onClick={goToDashboard}
                    >
                        Back to Dashboard
                    </button>

                </div>


                {/* =================================================
                    VEHICLE SECTION
                ================================================= */}

                <div className="vehicles-section">

                    <div className="section-header">

                        <h2>
                            {role === "ADMIN"
                                ? "All Vehicles"
                                : "My Vehicles"
                            }
                        </h2>


                        <button
                            onClick={loadVehicles}
                            disabled={loading}
                        >

                            {loading
                                ? "Refreshing..."
                                : "Refresh"
                            }

                        </button>

                    </div>


                    {/* =================================================
                        LOADING
                    ================================================= */}

                    {loading && (

                        <p>
                            Loading vehicle records...
                        </p>

                    )}


                    {/* =================================================
                        ERROR
                    ================================================= */}

                    {error && (

                        <div className="error-message">
                            {error}
                        </div>

                    )}


                    {/* =================================================
                        EMPTY
                    ================================================= */}

                    {!loading &&
                        !error &&
                        vehicles.length === 0 && (

                            <p>
                                No vehicle records found.
                            </p>

                        )}


                    {/* =================================================
                        VEHICLE TABLE
                    ================================================= */}

                    {!loading &&
                        !error &&
                        vehicles.length > 0 && (

                            <div className="vehicle-table-container">

                                <table>

                                    <thead>

                                        <tr>

                                            <th>
                                                Vehicle Number
                                            </th>

                                            <th>
                                                Type
                                            </th>

                                            <th>
                                                Slot
                                            </th>

                                            <th>
                                                Entry Time
                                            </th>

                                            <th>
                                                Exit Time
                                            </th>

                                            <th>
                                                Status
                                            </th>

                                        </tr>

                                    </thead>


                                    <tbody>

                                        {vehicles.map(
                                            (vehicle) => (

                                                <tr
                                                    key={
                                                        vehicle.id
                                                    }
                                                >

                                                    <td>
                                                        {
                                                            vehicle.vehicleNumber ||
                                                            "-"
                                                        }
                                                    </td>


                                                    <td>
                                                        {
                                                            vehicle.vehicleType ||
                                                            "-"
                                                        }
                                                    </td>


                                                    <td>
                                                        {
                                                            vehicle.slotNumber ||
                                                            "-"
                                                        }
                                                    </td>


                                                    <td>
                                                        {formatDate(
                                                            vehicle.entryTime
                                                        )}
                                                    </td>


                                                    <td>
                                                        {formatDate(
                                                            vehicle.exitTime
                                                        )}
                                                    </td>


                                                    <td>

                                                        {vehicle.exited ? (

                                                            <span className="status-exited">
                                                                Exited
                                                            </span>

                                                        ) : (

                                                            <span className="status-active">
                                                                Active
                                                            </span>

                                                        )}

                                                    </td>

                                                </tr>

                                            )
                                        )}

                                    </tbody>

                                </table>

                            </div>

                        )}

                </div>

            </div>

        </>
    );
}

export default VehicleList;