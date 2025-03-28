import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "../styles/Login.css";
import {useAuth} from "../context/AuthContext.tsx";
import {CommonConstants} from "../CommonConstants.ts";

export const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const { setRole } = useAuth();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        setLoading(true); // Start loading
        try {
            const response = await axios.post(`${CommonConstants.BACKEND_END_POINT}/api/auth/login`, {
                email,
                password,
            });
            setRole(response.data.role);
            localStorage.setItem("token", response.data.token);
            navigate("/books"); // Redirect to a dashboard/home page
        } catch (err : any) {
            setLoading(false); // Stop loading
            if (err.response) {
                // Server responded with an error status (4xx or 5xx)
                setError(err.response.data.message || "Invalid email or password");
            } else if (err.request) {
                // Request was made but no response received
                setError("Server is unreachable. Please try again later.");
            } else {
                // Other unexpected errors
                setError("Something went wrong. Please try again.");
            }
        } finally {
            setLoading(false); // Stop loading
        }
    };

    return (
        <section className="login-container">
            <div className="login-card">
                {error && <p className="login-error">{error}</p>}
                <form onSubmit={handleSubmit}>
                    <div className="login-input-group">
                        <i className="fa fa-envelope"></i>
                        <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                    </div>
                    <div className="login-input-group">
                        <i className="fa fa-lock"></i>
                        <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                    </div>
                    <button className="login-button" type="submit" disabled={loading}>
                        {loading ? <div className="login-spinner"/> : "Login"}
                    </button>
                </form>
                <p className="login-small-text">Don't have an account? <a href="/signup">Register</a></p>
            </div>
        </section>
    );
};
