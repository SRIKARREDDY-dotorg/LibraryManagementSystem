import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "../styles/Register.css";
import {CommonConstants} from "../CommonConstants.ts";

export const Register = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        setLoading(true); // Start loading
        try {
            await axios.post(`${CommonConstants.BACKEND_END_POINT}/api/auth/register`, { email, password });
            setMessage("Registration successful! Redirecting...");
            setTimeout(() => navigate("/login"), 2000);
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
        <div className="register-container">
            <div className="register-card">
                {message && <p className="success">{message}</p>}
                {error && <p className="register-error">{error}</p>}
                <form onSubmit={handleSubmit}>
                    <div className="register-input-group">
                        <i className="fa fa-envelope"></i>
                        <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                    </div>
                    <div className="register-input-group">
                        <i className="fa fa-lock"></i>
                        <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                    </div>
                    <button type="submit" className="register-button" disabled={loading}>
                        {loading ? <div className="register-spinner"/> : "Register"}
                    </button>
                </form>
                <p className="register-small-text">Already have an account? <a href="/login">Login</a></p>
            </div>
        </div>
    );
};
