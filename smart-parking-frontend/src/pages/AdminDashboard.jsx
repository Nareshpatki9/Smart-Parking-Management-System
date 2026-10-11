import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import API from "../services/api";
import Navbar from "../components/Navbar";

function AdminDashboard() {

    const navigate = useNavigate();

    // =====================================================
    // DASHBOARD DATA
    // =====================================================

    const [dashboard, setDashboard] = useState({
        totalSlots: 0,
        availableSlots: 0,
        occupiedSlots: 0,
        activeVehicles: 0,
        totalBills: 0,
        totalRevenue: 0
    });

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // =====================================================
    // DATE-WISE REPORT
    // =====================================================

    const [selectedDate, setSelectedDate] =
        useState("");

    const [dateReport, setDateReport] =
        useState(null);

    const [reportLoading, setReportLoading] =
        useState(false);

    const [reportError, setReportError] =
        useState("");

    // =====================================================
    // LOAD ADMIN DASHBOARD
    // =====================================================

    const loadDashboard = async () => {

        setLoading(true);
        setError("");

        try {

            const response =
                await API.get("/admin/dashboard");

            console.log(
                "Admin Dashboard Response:",
                response.data
            );

            const data =
                response.data || {};

            setDashboard({
                totalSlots:
                    Number(data.totalSlots) || 0,

                availableSlots:
                    Number(data.availableSlots) || 0,

                occupiedSlots:
                    Number(data.occupiedSlots) || 0,

                activeVehicles:
                    Number(data.activeVehicles) || 0,

                totalBills:
                    Number(data.totalBills) || 0,

                totalRevenue:
                    Number(data.totalRevenue) || 0
            });

        } catch (error) {

            console.error(
                "Admin dashboard error:",
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

            if (error.response?.status === 401) {

                localStorage.removeItem("token");
                localStorage.removeItem("username");
                localStorage.removeItem("role");

                navigate("/");

                return;
            }

            if (error.response?.status === 403) {

                setError(
                    "You are not authorized to access the admin dashboard."
                );

                return;
            }

            if (error.response?.status === 404) {

                setError(
                    "Admin dashboard API not found. Please check AdminController."
                );

                return;
            }

            if (error.response?.status >= 500) {

                setError(
                    "Server error. Please check the Spring Boot backend."
                );

                return;
            }

            if (!error.response) {

                setError(
                    "Unable to connect to the backend. Make sure Spring Boot is running on port 8080."
                );

                return;
            }

            setError(
                error.response?.data?.message ||
                error.response?.data?.error ||
                "Unable to load admin dashboard."
            );

        } finally {

            setLoading(false);
        }
    };

    // =====================================================
    // LOAD DATE-WISE REPORT
    // =====================================================

    const loadDateReport = async () => {

        setReportError("");
        setDateReport(null);

        if (!selectedDate) {

            setReportError(
                "Please select a date."
            );

            return;
        }

        setReportLoading(true);

        try {

            console.log(
                "Date-wise report date:",
                selectedDate
            );

            const response =
                await API.get(
                    `/admin/date-report?date=${selectedDate}`
                );

            console.log(
                "Date-wise Report Response:",
                response.data
            );

            const data =
                response.data || {};

            setDateReport({
                date:
                    data.date || selectedDate,

                totalVehicles:
                    Number(data.totalVehicles) || 0,

                totalBills:
                    Number(data.totalBills) || 0
            });

        } catch (error) {

            console.error(
                "Date-wise report error:",
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

            if (error.response?.status === 401) {

                localStorage.removeItem("token");
                localStorage.removeItem("username");
                localStorage.removeItem("role");

                navigate("/");

                return;
            }

            if (error.response?.status === 403) {

                setReportError(
                    "You are not authorized to view this report."
                );

                return;
            }

            if (error.response?.status === 400) {

                setReportError(
                    error.response?.data?.message ||
                    "Invalid date. Please select a valid date."
                );

                return;
            }

            if (error.response?.status >= 500) {

                setReportError(
                    "Server error while loading the date-wise report."
                );

                return;
            }

            if (!error.response) {

                setReportError(
                    "Unable to connect to the backend. Make sure Spring Boot is running on port 8080."
                );

                return;
            }

            setReportError(
                error.response?.data?.message ||
                error.response?.data?.error ||
                "Unable to load date-wise report."
            );

        } finally {

            setReportLoading(false);
        }
    };

    // =====================================================
    // LOAD WHEN PAGE OPENS
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
                        Loading admin dashboard...
                    </h2>

                </div>
            </>
        );
    }

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

                <header className="dashboard-header">

                    <div>

                        <h1>
                            Smart Parking
                        </h1>

                        <p>
                            Admin Dashboard
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
                            {dashboard.totalSlots}
                        </p>

                    </div>


                    <div className="stat-card">

                        <h3>
                            Available Slots
                        </h3>

                        <p>
                            {dashboard.availableSlots}
                        </p>

                    </div>


                    <div className="stat-card">

                        <h3>
                            Occupied Slots
                        </h3>

                        <p>
                            {dashboard.occupiedSlots}
                        </p>

                    </div>


                    <div className="stat-card">

                        <h3>
                            Active Vehicles
                        </h3>

                        <p>
                            {dashboard.activeVehicles}
                        </p>

                    </div>


                    <div className="stat-card">

                        <h3>
                            Total Bills
                        </h3>

                        <p>
                            {dashboard.totalBills}
                        </p>

                    </div>


                    <div className="stat-card">

                        <h3>
                            Total Revenue
                        </h3>

                        <p>
                            ₹{dashboard.totalRevenue.toFixed(2)}
                        </p>

                    </div>

                </section>


                {/* =================================================
                    ADMIN ACTIONS
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
                        onClick={() =>
                            navigate("/user")
                        }
                    >
                        User Dashboard
                    </button>


                    <button
                        onClick={loadDashboard}
                        disabled={loading}
                    >
                        Refresh
                    </button>

                </div>


                {/* =================================================
                    DATE-WISE REPORT
                ================================================= */}

                <section className="parking-section">

                    <div className="section-header">

                        <div>

                            <h2>
                                Date-wise Parking Report
                            </h2>

                            <p>
                                Check how many vehicles were parked
                                and how many bills were generated
                                on a selected date.
                            </p>

                        </div>

                    </div>


                    <div className="date-report-form">

                        <label htmlFor="reportDate">
                            Select Date
                        </label>

                        <input
                            id="reportDate"
                            type="date"
                            value={selectedDate}
                            onChange={(event) =>
                                setSelectedDate(
                                    event.target.value
                                )
                            }
                        />

                        <button
                            type="button"
                            onClick={loadDateReport}
                            disabled={reportLoading}
                        >

                            {reportLoading
                                ? "Searching..."
                                : "Search Report"
                            }

                        </button>

                    </div>


                    {reportError && (

                        <div className="error-message">
                            {reportError}
                        </div>

                    )}


                    {dateReport && (

                        <div className="date-report-result">

                            <div className="date-report-header">

                                <h3>
                                    Report for{" "}
                                    {new Date(
                                        `${dateReport.date}T00:00:00`
                                    ).toLocaleDateString(
                                        "en-IN",
                                        {
                                            day: "2-digit",
                                            month: "short",
                                            year: "numeric"
                                        }
                                    )}
                                </h3>

                            </div>


                            <div className="date-report-cards">

                                <div className="date-report-card">

                                    <span>
                                        🚗
                                    </span>

                                    <div>

                                        <h4>
                                            Total Vehicles Parked
                                        </h4>

                                        <strong>
                                            {dateReport.totalVehicles}
                                        </strong>

                                    </div>

                                </div>


                                <div className="date-report-card">

                                    <span>
                                        🧾
                                    </span>

                                    <div>

                                        <h4>
                                            Total Bills
                                        </h4>

                                        <strong>
                                            {dateReport.totalBills}
                                        </strong>

                                    </div>

                                </div>

                            </div>

                        </div>

                    )}

                </section>


                {/* =================================================
                    PARKING SUMMARY
                ================================================= */}

                <section className="parking-section">

                    <h2>
                        Parking Summary
                    </h2>

                    <div className="admin-summary">

                        <div>

                            <strong>
                                Parking Capacity
                            </strong>

                            <p>
                                {dashboard.totalSlots} Slots
                            </p>

                        </div>


                        <div>

                            <strong>
                                Currently Available
                            </strong>

                            <p>
                                {dashboard.availableSlots} Slots
                            </p>

                        </div>


                        <div>

                            <strong>
                                Currently Occupied
                            </strong>

                            <p>
                                {dashboard.occupiedSlots} Slots
                            </p>

                        </div>


                        <div>

                            <strong>
                                Current Vehicles
                            </strong>

                            <p>
                                {dashboard.activeVehicles}
                            </p>

                        </div>

                    </div>

                </section>


                {/* =================================================
                    REVENUE SUMMARY
                ================================================= */}

                <section className="vehicles-section">

                    <h2>
                        Revenue Summary
                    </h2>

                    <div className="revenue-box">

                        <h3>
                            Total Revenue
                        </h3>

                        <p>
                            ₹{dashboard.totalRevenue.toFixed(2)}
                        </p>

                        <span>
                            From {dashboard.totalBills} completed bill(s)
                        </span>

                    </div>

                </section>

            </div>
        </>
    );
}

export default AdminDashboard;
