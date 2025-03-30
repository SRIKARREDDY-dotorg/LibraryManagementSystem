import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useState } from "react";

export const Header = () => {
    const navigate = useNavigate();
    const { isAuthenticated, logout } = useAuth();
    const [dropdownOpen, setDropdownOpen] = useState(false);

    const toggleDropdown = () => {
        setDropdownOpen(!dropdownOpen);
    };
    const closeDropdown = () => {
        setDropdownOpen(false);
    };

    return (
        <div style={{
            backgroundColor: '#ffffff', 
            top: 0, 
            width: '100%', 
            position: 'fixed', 
            boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
            borderBottom: '1px solid #ddd',
            zIndex: 1000,
            justifyItems: 'center',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
        }}>
            {/* Left Section - Logo */}
            <Link to="/" style={{
                flex: 1,
                fontSize: '24px',
                fontWeight: '500',
                fontFamily: '"Inter", sans-serif',
                textDecoration: 'none',
                color: '#000',
                display: 'flex',
                alignItems: 'center',
                padding: '1rem',
                transition: 'background 0.3s ease-in-out',
                maxWidth: 'fit-content',
            }}
            onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#d2d2d2'}
            onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'transparent'}>
                📚 LMS
            </Link>
            {/* Profile Icon */}
            {isAuthenticated ? (
                <div style={{
                    flex: 1,
                    display: 'flex',
                    justifyContent: 'flex-end',
                    alignItems: 'center',
                }}>
                    <Link to="/books" style={{
                        flex: 1,
                        fontSize: '18px',
                        fontWeight: '500',
                        textDecoration: 'none',
                        color: '#000',
                        textAlign: 'center',
                        padding: '1rem',
                        marginRight: '10px',
                        borderRadius: '8px',
                        transition: 'background 0.3s ease-in-out',
                        maxWidth: '100px',
                    }}
                    onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#f5f5f5'}
                    onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'transparent'}>
                        📖 Books
                    </Link>
                    <div style={{ position: 'relative' }}>
                        <div 
                            style={{
                                width: '50px',
                                height: '50px',
                                borderRadius: '20%',
                                backgroundColor: '#ddd',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                cursor: 'pointer',
                                marginRight: '10px',
                                transition: 'transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out',
                            }}
                            onMouseEnter={(e) => {
                                e.currentTarget.style.transform = 'scale(1.1)';
                                e.currentTarget.style.boxShadow = '0 4px 8px rgba(0, 0, 0, 0.2)';
                            }}
                            onMouseLeave={(e) => {
                                e.currentTarget.style.transform = 'scale(1)';
                                e.currentTarget.style.boxShadow = 'none';
                            }}
                            onClick={toggleDropdown}
                        >
                            <img 
                                src={`https://api.dicebear.com/7.x/adventurer/svg?seed=guest`} 
                                alt="Profile" 
                                style={{ width: '100%', height: '100%' }}
                            />
                        </div>
                        {/* Dropdown Menu */}
                        {dropdownOpen && (
                            <div style={{
                                position: 'absolute',
                                top: '60px',
                                right: 0,
                                backgroundColor: '#fff',
                                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                                borderRadius: '8px',
                                overflow: 'hidden',
                                width: '150px',
                                display: 'flex',
                                flexDirection: 'column',
                            }}>
                                <Link 
                                    to="/profile" 
                                    style={{
                                        padding: '10px',
                                        textAlign: 'center',
                                        textDecoration: 'none',
                                        color: '#000',
                                        transition: 'background 0.3s ease-in-out',
                                    }}
                                    onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#f5f5f5'}
                                    onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
                                    onClick={closeDropdown}
                                >
                                    Profile
                                </Link>
                                <button 
                                    style={{
                                        padding: '10px',
                                        textAlign: 'center',
                                        background: 'none',
                                        border: 'none',
                                        cursor: 'pointer',
                                        color: '#000',
                                        transition: 'background 0.3s ease-in-out',
                                    }}
                                    onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#f5f5f5'}
                                    onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
                                    onClick={() => {
                                        closeDropdown();
                                        logout();
                                        navigate('/login');
                                        console.log("Logout clicked");
                                    }}
                                >
                                    Logout
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            ) : (
                <Link to="/login" style={{
                    fontSize: '20px',
                    fontWeight: '100',
                    fontFamily: '"Inter", sans-serif',
                    textDecoration: 'wavy',
                    color: '#000',
                    padding: '1rem',
                    marginRight: '3px',
                    borderRadius: '8px',
                    transition: 'background 0.3s ease-in-out',
                    maxWidth: '150px',
                }}
                onMouseEnter={(e) => {
                    e.currentTarget.style.backgroundColor = '#5f5f5f'
                    e.currentTarget.style.color = '#fff'
                }}
                onMouseLeave={(e) => {
                    e.currentTarget.style.backgroundColor = 'transparent'
                    e.currentTarget.style.color = '#000'
                    }}>
                    SignIn
                </Link>
            )}
        </div>
    );
};
