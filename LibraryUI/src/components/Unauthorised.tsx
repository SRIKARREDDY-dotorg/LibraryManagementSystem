import { useNavigate } from "react-router-dom";

export const Unauthorised = () => {
    const navigate = useNavigate();
    return (
        <div style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            height: "100vh",
            backgroundColor: "#f8d7da",
            color: "#721c24",
            fontFamily: '"Inter", sans-serif',
            textAlign: "center",
        }}>
            <div style={{
                backgroundColor: "#ffffff",
                padding: "20px 40px",
                borderRadius: "8px",
                boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
                maxWidth: "400px",
                border: "2px solid #f5c6cb",
            }}>
                <h2 style={{
                    fontSize: "24px",
                    fontWeight: "600",
                    marginBottom: "10px",
                }}>
                    🚫 Access Denied
                </h2>
                <p style={{
                    fontSize: "16px",
                    marginBottom: "20px",
                }}>
                    You are not authorized to view this page.
                </p>
                <button 
                    onClick={() => navigate("/login")}
                    style={{
                        padding: "10px 20px",
                        fontSize: "14px",
                        fontWeight: "500",
                        color: "#ffffff",
                        backgroundColor: "#dc3545",
                        border: "none",
                        borderRadius: "5px",
                        cursor: "pointer",
                        transition: "background 0.3s ease-in-out, transform 0.2s ease-in-out",
                    }}
                    onMouseEnter={(e) => {
                        e.currentTarget.style.backgroundColor = "#c82333";
                        e.currentTarget.style.transform = "scale(1.05)";
                    }}
                    onMouseLeave={(e) => {
                        e.currentTarget.style.backgroundColor = "#dc3545";
                        e.currentTarget.style.transform = "scale(1)";
                    }}
                >
                    🔐 Go to Login
                </button>
            </div>
        </div>
    );
}