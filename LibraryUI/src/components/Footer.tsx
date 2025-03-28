import { FaGithub, FaLinkedin } from "react-icons/fa";
import { SiLeetcode } from "react-icons/si";

export const Footer = () => {
    const currentYear = new Date().getFullYear(); // Get current year dynamically

    return (
        <footer style={{
            padding: "1.5rem",
            backgroundColor: "#f1f4f8",
            textAlign: "center",
            minHeight: "12vh",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            gap: "12px",
            boxShadow: "0px -2px 5px rgba(0, 0, 0, 0.1)"
        }}>
            <p style={{ fontSize: "0.9rem", fontWeight: "500", color: "#333" }}>
                © {currentYear} Library Management System. All rights reserved.
            </p>

            <p style={{ fontSize: "0.9rem", color: "#333" }}>
                For support, contact: <strong>Pochana Srikar Reddy</strong> –  
                <a 
                    href="mailto:srikarreddypochana12@gmail.com" 
                    style={{ 
                        textDecoration: "none", 
                        color: "#0645AD", 
                        fontWeight: 600, 
                        marginLeft: 6 
                    }}
                >
                    srikarreddypochana12@gmail.com
                </a>
            </p>

            <div style={{ display: "flex", gap: "20px", marginTop: "8px" }}>
                <a href="https://github.com/SRIKARREDDY-dotorg" target="_blank" rel="noopener noreferrer"
                    style={{ transition: "transform 0.2s ease-in-out" }}
                    onMouseEnter={(e) => e.currentTarget.style.transform = "scale(1.1)"}
                    onMouseLeave={(e) => e.currentTarget.style.transform = "scale(1.0)"}
                >
                    <FaGithub size={28} color="#333" />
                </a>
                <a href="https://leetcode.com/u/srikarreddypochana12/" target="_blank" rel="noopener noreferrer"
                    style={{ transition: "transform 0.2s ease-in-out" }}
                    onMouseEnter={(e) => e.currentTarget.style.transform = "scale(1.1)"}
                    onMouseLeave={(e) => e.currentTarget.style.transform = "scale(1.0)"}
                >
                    <SiLeetcode size={28} color="#f89f1b" />
                </a>
                <a href="https://www.linkedin.com/in/pochana-srikar-reddy/" target="_blank" rel="noopener noreferrer"
                    style={{ transition: "transform 0.2s ease-in-out" }}
                    onMouseEnter={(e) => e.currentTarget.style.transform = "scale(1.1)"}
                    onMouseLeave={(e) => e.currentTarget.style.transform = "scale(1.0)"}
                >
                    <FaLinkedin size={28} color="#0077b5" />
                </a>
            </div>
        </footer>
    );
};
