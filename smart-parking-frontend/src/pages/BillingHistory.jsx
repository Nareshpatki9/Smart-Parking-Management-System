import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import API from "../services/api";
import Navbar from "../components/Navbar";

function BillingHistory() {

    const navigate = useNavigate();

    const role =
        localStorage.getItem("role");


    const [bills, setBills] =
        useState([]);

    const [loading, setLoading] =
        useState(true);

    const [error, setError] =
        useState("");


    // =====================================================
    // LOAD BILLING HISTORY
    // =====================================================

    const loadBills = async () => {

        setLoading(true);
        setError("");

        try {

            const response =
                await API.get(
                    "/billing/all"
                );

            console.log(
                "Billing History Response:",
                response.data
            );

            setBills(
                Array.isArray(response.data)
                    ? response.data
                    : []
            );

        } catch (error) {

            console.error(
                "Billing History Error:",
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
                    "You are not authorized to view billing history."
                );

                return;
            }


            // =================================================
            // 404 - ENDPOINT NOT FOUND
            // =================================================

            if (error.response?.status === 404) {

                setError(
                    "Billing history API was not found. Please check BillingController."
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
                "Unable to load billing history."
            );

        } finally {

            setLoading(false);
        }
    };


    // =====================================================
    // INITIAL LOAD
    // =====================================================

    useEffect(() => {

        loadBills();

    }, []);


    // =====================================================
    // TOTAL REVENUE
    // =====================================================

    const totalRevenue =
        bills.reduce(
            (total, bill) => {

                return total +
                    Number(
                        bill.amount ?? 0
                    );

            },
            0
        );


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
    // DATE FORMAT
    // =====================================================

    const formatDate = (dateValue) => {

        if (!dateValue) {
            return "-";
        }

        const date =
            new Date(dateValue);

        if (Number.isNaN(
            date.getTime()
        )) {
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
                            Billing History
                        </h1>

                        <p>
                            {role === "ADMIN"
                                ? "All parking payment and billing records"
                                : "Your parking payment and billing records"
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
                    STATISTICS
                ================================================= */}

                {!loading &&
                    !error && (

                        <div className="stats-container">

                            <div className="stat-card">

                                <h3>
                                    Total Bills
                                </h3>

                                <p>
                                    {bills.length}
                                </p>

                            </div>


                            <div className="stat-card">

                                <h3>
                                    Total Revenue
                                </h3>

                                <p>
                                    ₹
                                    {totalRevenue.toFixed(2)}
                                </p>

                            </div>

                        </div>

                    )}


                {/* =================================================
                    BILLING RECORDS
                ================================================= */}

                <div className="vehicles-section">

                    <div className="section-header">

                        <h2>
                            Payment Records
                        </h2>


                        <button
                            onClick={loadBills}
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
                            Loading billing records...
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
                        bills.length === 0 && (

                            <p>
                                No billing records found.
                            </p>

                        )}


                    {/* =================================================
                        TABLE
                    ================================================= */}

                    {!loading &&
                        !error &&
                        bills.length > 0 && (

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
                                                Duration
                                            </th>

                                            <th>
                                                Amount
                                            </th>

                                            <th>
                                                Payment Time
                                            </th>

                                            <th>
                                                Status
                                            </th>

                                        </tr>

                                    </thead>


                                    <tbody>

                                        {bills.map(
                                            (bill) => (

                                                <tr
                                                    key={
                                                        bill.id
                                                    }
                                                >

                                                    {/* VEHICLE NUMBER */}

                                                    <td>
                                                        {
                                                            bill.vehicleNumber ||
                                                            "-"
                                                        }
                                                    </td>


                                                    {/* TYPE */}

                                                    <td>
                                                        {
                                                            bill.vehicleType ||
                                                            "-"
                                                        }
                                                    </td>


                                                    {/* DURATION */}

                                                    <td>

                                                        {
                                                            bill.durationHours ??
                                                            0
                                                        }{" "}
                                                        hour(s)

                                                    </td>


                                                    {/* AMOUNT */}

                                                    <td>

                                                        ₹
                                                        {Number(
                                                            bill.amount ?? 0
                                                        ).toFixed(2)}

                                                    </td>


                                                    {/* PAYMENT TIME */}

                                                    <td>

                                                        {formatDate(
                                                            bill.paymentTime
                                                        )}

                                                    </td>


                                                    {/* STATUS */}

                                                    <td>

                                                        {bill.paid ? (

                                                            <span className="status-active">
                                                                Paid
                                                            </span>

                                                        ) : (

                                                            <span className="status-exited">
                                                                Pending
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

export default BillingHistory;