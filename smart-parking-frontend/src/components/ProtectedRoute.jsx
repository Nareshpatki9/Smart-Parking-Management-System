import { Navigate } from "react-router-dom";

function ProtectedRoute({ children, requiredRole, allowedRoles }) {

    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    if (!token) {
        return <Navigate to="/" replace />;
    }

    // Support multiple roles
    if (
        allowedRoles &&
        !allowedRoles.includes(role)
    ) {
        return (
            <Navigate
                to={role === "ADMIN" ? "/admin" : "/user"}
                replace
            />
        );
    }

    // Support single role
    if (
        requiredRole &&
        role !== requiredRole
    ) {
        return (
            <Navigate
                to={role === "ADMIN" ? "/admin" : "/user"}
                replace
            />
        );
    }

    return children;
}

export default ProtectedRoute;