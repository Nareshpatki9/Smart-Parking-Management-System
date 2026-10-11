import { useState } from "react";
import { useNavigate } from "react-router-dom";

import API from "../services/api";
import Navbar from "../components/Navbar";

function VehicleEntry() {

    const navigate = useNavigate();

    const [vehicleNumber, setVehicleNumber] = useState("");
    const [vehicleType, setVehicleType] = useState("CAR");

    const [message, setMessage] = useState("");
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
    // VEHICLE ENTRY
    // =====================================================

    const handleSubmit = async (event) => {

        event.preventDefault();

        setMessage("");
        setError("");

        const normalizedVehicleNumber =
            vehicleNumber
                .trim()
                .toUpperCase();

        // =================================================
        // VALIDATE VEHICLE NUMBER
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

            const response =
                await API.post(
                    "/vehicle/entry",
                    {
                        vehicleNumber:
                            normalizedVehicleNumber,

                        vehicleType:
                            vehicleType.toUpperCase()
                    }
                );

            console.log(
                "Vehicle Entry Response:",
                response.data
            );

            const vehicle =
                response.data;

            setMessage(
                `Vehicle parked successfully in slot ${vehicle.slotNumber}.`
            );

            setVehicleNumber("");

        } catch (error) {

            console.error(
                "Vehicle Entry Error:",
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
                    "You are not authorized to park a vehicle."
                );

                return;
            }

            // =================================================
            // BAD REQUEST
            // =================================================

            if (error.response?.status === 400) {

                setError(
                    error.response?.data?.message ||
                    "Invalid vehicle details."
                );

                return;
            }

            // =================================================
            // CONFLICT
            // =================================================

            if (error.response?.status === 409) {

                setError(
                    error.response?.data?.message ||
                    "Vehicle is already parked."
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
                "Unable to park vehicle. Please try again."
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
                        Vehicle Entry
                    </h1>

                    <p>
                        Enter vehicle details to park.
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


                        <label htmlFor="vehicleType">
                            Vehicle Type
                        </label>

                        <select
                            id="vehicleType"
                            value={vehicleType}
                            onChange={(event) =>
                                setVehicleType(
                                    event.target.value
                                )
                            }
                        >

                            <option value="BIKE">
                                Bike
                            </option>

                            <option value="CAR">
                                Car
                            </option>

                            <option value="TRUCK">
                                Truck
                            </option>

                        </select>


                        <button
                            type="submit"
                            disabled={loading}
                        >

                            {loading
                                ? "Parking..."
                                : "Park Vehicle"
                            }

                        </button>

                    </form>


                    {/* =================================================
                        SUCCESS MESSAGE
                    ================================================= */}

                    {message && (
                        <div className="success-message">
                            {message}
                        </div>
                    )}


                    {/* =================================================
                        ERROR MESSAGE
                    ================================================= */}

                    {error && (
                        <div className="error-message">
                            {error}
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

export default VehicleEntry;
