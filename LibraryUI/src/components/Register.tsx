import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "../styles/Register.css";

export const Register = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await axios.post("http://localhost:8080/api/auth/register", { email, password });
            setMessage("Registration successful! Redirecting...");
            setTimeout(() => navigate("/login"), 2000);
        } catch (err) {
            setError("User already exists");
        }
    };

    return (
        <div className="register-container">
            <div className="register-card">
                <h2>Register</h2>
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
                    <button type="submit">Register</button>
                </form>
                <p className="register-small-text">Already have an account? <a href="/login">Login</a></p>
            </div>
        </div>
    );
};
