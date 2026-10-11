import {
    BrowserRouter,
    Routes,
    Route
} from "react-router-dom";

import Login from "./pages/Login";
import Register from "./pages/Register";

import UserDashboard from "./pages/UserDashboard";
import AdminDashboard from "./pages/AdminDashboard";

import VehicleEntry from "./pages/VehicleEntry";
import VehicleExit from "./pages/VehicleExit";
import VehicleList from "./pages/VehicleList";
import BillingHistory from "./pages/BillingHistory";

import ProtectedRoute from "./components/ProtectedRoute";


function App() {

    return (

        <BrowserRouter>

            <Routes>

                {/* =====================================================
                    LOGIN
                ===================================================== */}

                <Route
                    path="/"
                    element={<Login />}
                />


                {/* =====================================================
                    REGISTER
                ===================================================== */}

                <Route
                    path="/register"
                    element={<Register />}
                />


                {/* =====================================================
                    USER DASHBOARD
                ===================================================== */}

                <Route
                    path="/user"
                    element={
                        <ProtectedRoute
                            requiredRole="USER"
                        >
                            <UserDashboard />
                        </ProtectedRoute>
                    }
                />


                {/* =====================================================
                    ADMIN DASHBOARD
                ===================================================== */}

                <Route
                    path="/admin"
                    element={
                        <ProtectedRoute
                            requiredRole="ADMIN"
                        >
                            <AdminDashboard />
                        </ProtectedRoute>
                    }
                />


                {/* =====================================================
                    VEHICLE ENTRY
                    USER + ADMIN
                ===================================================== */}

                <Route
                    path="/vehicle-entry"
                    element={
                        <ProtectedRoute
                            allowedRoles={[
                                "USER",
                                "ADMIN"
                            ]}
                        >
                            <VehicleEntry />
                        </ProtectedRoute>
                    }
                />


                {/* =====================================================
                    VEHICLE EXIT
                    USER + ADMIN
                ===================================================== */}

                <Route
                    path="/vehicle-exit"
                    element={
                        <ProtectedRoute
                            allowedRoles={[
                                "USER",
                                "ADMIN"
                            ]}
                        >
                            <VehicleExit />
                        </ProtectedRoute>
                    }
                />


                {/* =====================================================
                    VEHICLE HISTORY
                    USER + ADMIN
                ===================================================== */}

                <Route
                    path="/vehicle-list"
                    element={
                        <ProtectedRoute
                            allowedRoles={[
                                "USER",
                                "ADMIN"
                            ]}
                        >
                            <VehicleList />
                        </ProtectedRoute>
                    }
                />


                {/* =====================================================
                    BILLING HISTORY
                    USER + ADMIN
                ===================================================== */}

                <Route
                    path="/billing-history"
                    element={
                        <ProtectedRoute
                            allowedRoles={[
                                "USER",
                                "ADMIN"
                            ]}
                        >
                            <BillingHistory />
                        </ProtectedRoute>
                    }
                />

            </Routes>

        </BrowserRouter>
    );
}

export default App;
