import { FaGithub, FaLinkedin } from "react-icons/fa";
import { SiLeetcode } from "react-icons/si";

export const Footer = () => {
    const currentYear = new Date().getFullYear(); // Get current year dynamically
    return (
        <div style={{
            padding: '1rem',
            backgroundColor: '#bed8f3',
            minHeight: '10vh',
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            gap: "8px"
        }}>
            <p>© {currentYear} Library Management System. All rights reserved.</p>
            <p>For support, contact: <strong>Pochana Srikar Reddy</strong> -  
                <a href="mailto:srikarreddypochana12@gmail.com" style={{ textDecoration: "none", color: "#0645AD", fontWeight: "500" }}>
                    srikarreddypochana12@gmail.com
                </a>
            </p>
            <div style={{ display: "flex", gap: "15px" }}>
                <a href="https://github.com/SRIKARREDDY-dotorg" target="_blank" rel="noopener noreferrer">
                    <FaGithub size={24} color="black" />
                </a>
                <a href="https://leetcode.com/u/srikarreddypochana12/" target="_blank" rel="noopener noreferrer">
                    <SiLeetcode size={24} color="black" />
                </a>
                <a href="https://www.linkedin.com/in/pochana-srikar-reddy/" target="_blank" rel="noopener noreferrer">
                    <FaLinkedin size={24} color="#0077b5" />
                </a>
            </div>
        </div>
    );
};
