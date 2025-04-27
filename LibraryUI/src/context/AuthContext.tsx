// src/context/AuthContext.tsx
import React, { createContext, useContext, useState, useEffect } from 'react';

interface AuthContextType {
    email: string;
    password: string;
    role: string | null;
    setRole: (role: string | null) => void;
    setEmail: (email: string) => void;
    setPassword: (password: string) => void;
    isAuthenticated: boolean;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [role, setRole] = useState<string | null>(() => {
        // Initialize from localStorage
        return localStorage.getItem('role');
    });
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    let isAuthenticated = !!role;

    const logout = () => {
        localStorage.removeItem('role');
        localStorage.removeItem('token');
        setRole(null);
        isAuthenticated = false;
    };

    // Update localStorage when role changes
    useEffect(() => {
        if (role) {
            localStorage.setItem('role', role);
        }
    }, [role]);

    const value = {
        email,
        password,
        role,
        setRole,
        setEmail,
        setPassword,
        isAuthenticated,
        logout
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};

// Custom hook to use the auth context
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
