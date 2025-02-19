import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "../styles/Login.css";

export const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/api/auth/login", {
                email,
                password,
            });
            localStorage.setItem("token", response.data.token);
            navigate("/books"); // Redirect to a dashboard/home page
        } catch (err) {
            setError("Invalid email or password");
        }
    };

    return (
        <section className="login-container">
            <div className="login-card">
                <h2>Login</h2>
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
                    <button className="login-button" type="submit">Login</button>
                </form>
                <p className="login-small-text">Don't have an account? <a href="/signup">Register</a></p>
            </div>
        </section>
    );
};
