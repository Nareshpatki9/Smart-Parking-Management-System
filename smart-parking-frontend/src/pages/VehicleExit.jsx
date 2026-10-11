import { useState } from "react";
import { useNavigate } from "react-router-dom";

import API from "../services/api";
import Navbar from "../components/Navbar";

function VehicleExit() {

    const navigate = useNavigate();

    const [vehicleNumber, setVehicleNumber] = useState("");
    const [bill, setBill] = useState(null);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);


    // =====================================================
    // GO TO DASHBOARD
    // =====================================================

    const goToDashboard = () => {

        const role =
            localStorage.getItem("role");

        if (role === "ADMIN") {
            navigate("/admin");
        } else {
            navigate("/user");
        }
    };


    // =====================================================
    // VEHICLE EXIT
    // =====================================================

    const handleSubmit = async (event) => {

        event.preventDefault();

        setError("");
        setBill(null);

        const normalizedVehicleNumber =
            vehicleNumber
                .trim()
                .toUpperCase();


        // =================================================
        // VALIDATION
        // =================================================

        if (!normalizedVehicleNumber) {

            setError(
                "Please enter vehicle number."
            );

            return;
        }

        if (normalizedVehicleNumber.length < 4) {

            setError(
                "Please enter a valid vehicle number."
            );

            return;
        }


        setLoading(true);

        try {

            console.log(
                "Vehicle Exit Request:",
                normalizedVehicleNumber
            );

            const response =
                await API.post(
                    `/vehicle/exit/${encodeURIComponent(
                        normalizedVehicleNumber
                    )}`
                );

            console.log(
                "Vehicle Exit Response:",
                response.data
            );


            setBill(response.data);

            setVehicleNumber("");


        } catch (error) {

            console.error(
                "Vehicle Exit Error:",
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
            // 403 - NOT AUTHORIZED
            // =================================================

            if (error.response?.status === 403) {

                setError(
                    error.response?.data?.message ||
                    error.response?.data?.error ||
                    "You are not authorized to exit this vehicle."
                );

                return;
            }


            // =================================================
            // 404 - VEHICLE NOT FOUND
            // =================================================

            if (error.response?.status === 404) {

                setError(
                    error.response?.data?.message ||
                    error.response?.data?.error ||
                    "Vehicle was not found."
                );

                return;
            }


            // =================================================
            // 400 - BAD REQUEST
            // =================================================

            if (error.response?.status === 400) {

                setError(
                    error.response?.data?.message ||
                    error.response?.data?.error ||
                    "Invalid vehicle number."
                );

                return;
            }


            // =================================================
            // 409 - CONFLICT
            // =================================================

            if (error.response?.status === 409) {

                setError(
                    error.response?.data?.message ||
                    error.response?.data?.error ||
                    "Vehicle cannot be exited."
                );

                return;
            }


            // =================================================
            // 500 - SERVER ERROR
            // =================================================

            if (error.response?.status >= 500) {

                setError(
                    error.response?.data?.message ||
                    error.response?.data?.error ||
                    "Server error occurred. Check Spring Boot console."
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
            // OTHER ERRORS
            // =================================================

            setError(
                error.response?.data?.message ||
                error.response?.data?.error ||
                "Something went wrong. Please try again."
            );

        } finally {

            setLoading(false);
        }
    };


    return (

        <>

            <Navbar />

            <div className="form-page">

                <div className="form-card">

                    {/* =================================================
                        HEADER
                    ================================================= */}

                    <h1>
                        Vehicle Exit
                    </h1>

                    <p>
                        Enter vehicle number to exit the parking.
                    </p>


                    {/* =================================================
                        FORM
                    ================================================= */}

                    <form onSubmit={handleSubmit}>

                        <label htmlFor="vehicleNumber">
                            Vehicle Number
                        </label>

                        <input
                            id="vehicleNumber"
                            type="text"
                            placeholder="MH24AB1234"
                            value={vehicleNumber}
                            onChange={(event) =>
                                setVehicleNumber(
                                    event.target.value.toUpperCase()
                                )
                            }
                            autoComplete="off"
                            required
                        />


                        <button
                            type="submit"
                            disabled={loading}
                        >

                            {loading
                                ? "Processing..."
                                : "Exit Vehicle"
                            }

                        </button>

                    </form>


                    {/* =================================================
                        ERROR MESSAGE
                    ================================================= */}

                    {error && (

                        <div className="error-message">
                            {error}
                        </div>

                    )}


                    {/* =================================================
                        BILL
                    ================================================= */}

                    {bill && (

                        <div className="bill-card">

                            <h2>
                                Vehicle Exit Successful
                            </h2>


                            <p>
                                <strong>
                                    Vehicle Number:
                                </strong>{" "}
                                {bill.vehicleNumber}
                            </p>


                            <p>
                                <strong>
                                    Vehicle Type:
                                </strong>{" "}
                                {bill.vehicleType}
                            </p>


                            <p>
                                <strong>
                                    Duration:
                                </strong>{" "}
                                {bill.durationHours} hour(s)
                            </p>


                            <p>
                                <strong>
                                    Parking Amount:
                                </strong>{" "}
                                ₹
                                {Number(
                                    bill.amount ?? 0
                                ).toFixed(2)}
                            </p>


                            <p>
                                <strong>
                                    Payment Status:
                                </strong>{" "}
                                {bill.paid
                                    ? "Paid"
                                    : "Pending"
                                }
                            </p>


                            <p>
                                <strong>
                                    Payment Time:
                                </strong>{" "}

                                {bill.paymentTime
                                    ? new Date(
                                        bill.paymentTime
                                    ).toLocaleString()
                                    : "-"
                                }

                            </p>

                        </div>

                    )}


                    {/* =================================================
                        BACK TO DASHBOARD
                    ================================================= */}

                    <button
                        type="button"
                        className="secondary-button"
                        onClick={goToDashboard}
                    >
                        Back to Dashboard
                    </button>

                </div>

            </div>

        </>
    );
}

export default VehicleExit;
