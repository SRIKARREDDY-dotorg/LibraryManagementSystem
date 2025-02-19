import { Link } from "react-router-dom";
import "../styles/Home.css"; // Import the styles

export const Home = () => {
    return (
        <section className="library-container">
            <div className="library-card">
                <h1 className="library-title">📚 Library Management</h1>
                <nav className="nav-container">
                    <div className="auth-buttons">
                        <Link to="/login" className="auth-button login-button-home">
                            🔐 Login
                        </Link>
                        <Link to="/sign-up" className="auth-button signup-button-home">
                            ✨ Sign Up
                        </Link>
                    </div>
                </nav>
            </div>
        </section>
    );
};
