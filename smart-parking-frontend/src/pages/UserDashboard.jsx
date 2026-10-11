import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import API from "../services/api";
import Navbar from "../components/Navbar";

function UserDashboard() {

    const navigate = useNavigate();

    const [slots, setSlots] = useState([]);
    const [activeVehicles, setActiveVehicles] = useState([]);

    const [totalSlots, setTotalSlots] = useState(0);
    const [availableSlots, setAvailableSlots] = useState(0);
    const [occupiedSlots, setOccupiedSlots] = useState(0);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // =====================================================
    // LOAD DASHBOARD DATA
    // =====================================================

    const loadDashboard = async () => {

        setLoading(true);
        setError("");

        try {

            const [
                slotsResponse,
                summaryResponse,
                activeVehiclesResponse
            ] = await Promise.all([
                API.get("/parking/all"),
                API.get("/parking/summary"),
                API.get("/vehicle/active")
            ]);

            // =================================================
            // PARKING SLOTS
            // =================================================

            const slotsData =
                Array.isArray(slotsResponse.data)
                    ? slotsResponse.data
                    : [];

            // =================================================
            // PARKING SUMMARY
            // =================================================

            const summaryData =
                summaryResponse.data || {};

            // =================================================
            // ACTIVE VEHICLES
            // =================================================

            const activeVehiclesData =
                Array.isArray(activeVehiclesResponse.data)
                    ? activeVehiclesResponse.data
                    : [];

            setSlots(slotsData);

            setTotalSlots(
                Number(summaryData.totalSlots) || 0
            );

            setAvailableSlots(
                Number(summaryData.availableSlots) || 0
            );

            setOccupiedSlots(
                Number(summaryData.occupiedSlots) || 0
            );

            setActiveVehicles(
                activeVehiclesData
            );

        } catch (error) {

            console.error(
                "Dashboard error:",
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
            // UNAUTHORIZED
            // =================================================

            if (error.response?.status === 401) {

                localStorage.removeItem("token");
                localStorage.removeItem("username");
                localStorage.removeItem("role");

                navigate("/");

                return;
            }

            // =================================================
            // FORBIDDEN
            // =================================================

            if (error.response?.status === 403) {

                setError(
                    error.response?.data?.message ||
                    "You are not authorized to access parking data."
                );

                return;
            }

            // =================================================
            // NOT FOUND
            // =================================================

            if (error.response?.status === 404) {

                setError(
                    "Dashboard API endpoint not found. Please check the backend controllers."
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
                    "Unable to connect to the backend. Make sure Spring Boot is running on port 8080."
                );

                return;
            }

            // =================================================
            // OTHER ERROR
            // =================================================

            setError(
                error.response?.data?.message ||
                error.response?.data?.error ||
                "Unable to load parking data."
            );

        } finally {

            setLoading(false);
        }
    };

    // =====================================================
    // LOAD DASHBOARD WHEN PAGE OPENS
    // =====================================================

    useEffect(() => {

        loadDashboard();

    }, []);


    // =====================================================
    // LOADING SCREEN
    // =====================================================

    if (loading) {

        return (
            <>
                <Navbar />

                <div className="dashboard-container">

                    <h2>
                        Loading dashboard...
                    </h2>

                </div>
            </>
        );
    }


    return (
        <>
            <Navbar />

            <div className="dashboard-container">

                {/* =================================================
                    HEADER
                ================================================= */}

                <header className="dashboard-header">

                    <div>

                        <h1>
                            Smart Parking
                        </h1>

                        <p>
                            User Dashboard
                        </p>

                    </div>

                </header>


                {/* =================================================
                    ERROR
                ================================================= */}

                {error && (

                    <div className="error-message">
                        {error}
                    </div>

                )}


                {/* =================================================
                    STATISTICS
                ================================================= */}

                <section className="stats-container">

                    <div className="stat-card">

                        <h3>
                            Total Slots
                        </h3>

                        <p>
                            {totalSlots}
                        </p>

                    </div>


                    <div className="stat-card">

                        <h3>
                            Available Slots
                        </h3>

                        <p>
                            {availableSlots}
                        </p>

                    </div>


                    <div className="stat-card">

                        <h3>
                            Occupied Slots
                        </h3>

                        <p>
                            {occupiedSlots}
                        </p>

                    </div>


                    <div className="stat-card">

                        <h3>
                            Active Vehicles
                        </h3>

                        <p>
                            {activeVehicles.length}
                        </p>

                    </div>

                </section>


                {/* =================================================
                    ACTION BUTTONS
                ================================================= */}

                <div className="dashboard-actions">

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
                        Billing History
                    </button>


                    <button
                        onClick={loadDashboard}
                        disabled={loading}
                    >
                        Refresh
                    </button>

                </div>


                {/* =================================================
                    PARKING SLOTS
                ================================================= */}

                <section className="parking-section">

                    <h2>
                        Parking Slots
                    </h2>

                    {slots.length === 0 ? (

                        <p>
                            No parking slots found.
                        </p>

                    ) : (

                        <div className="slot-grid">

                            {slots.map((slot) => (

                                <div
                                    key={slot.id}
                                    className={
                                        slot.occupied
                                            ? "slot-card occupied"
                                            : "slot-card available"
                                    }
                                >

                                    <h3>
                                        {slot.slotNumber}
                                    </h3>

                                    <p>
                                        {slot.occupied
                                            ? "Occupied"
                                            : "Available"
                                        }
                                    </p>

                                </div>

                            ))}

                        </div>

                    )}

                </section>


                {/* =================================================
                    ACTIVE VEHICLES
                ================================================= */}

                <section className="vehicles-section">

                    <h2>
                        Active Vehicles
                    </h2>

                    {activeVehicles.length === 0 ? (

                        <p>
                            No vehicles currently parked.
                        </p>

                    ) : (

                        <div className="vehicle-table-container">

                            <table>

                                <thead>

                                    <tr>

                                        <th>
                                            Vehicle Number
                                        </th>

                                        <th>
                                            Vehicle Type
                                        </th>

                                        <th>
                                            Slot
                                        </th>

                                        <th>
                                            Entry Time
                                        </th>

                                    </tr>

                                </thead>

                                <tbody>

                                    {activeVehicles.map(
                                        (vehicle) => (

                                            <tr
                                                key={vehicle.id}
                                            >

                                                <td>
                                                    {vehicle.vehicleNumber}
                                                </td>

                                                <td>
                                                    {vehicle.vehicleType}
                                                </td>

                                                <td>
                                                    {vehicle.slotNumber}
                                                </td>

                                                <td>
                                                    {vehicle.entryTime
                                                        ? new Date(
                                                            vehicle.entryTime
                                                        ).toLocaleString()
                                                        : "-"
                                                    }
                                                </td>

                                            </tr>

                                        )
                                    )}

                                </tbody>

                            </table>

                        </div>

                    )}

                </section>

            </div>
        </>
    );
}

export default UserDashboard;
