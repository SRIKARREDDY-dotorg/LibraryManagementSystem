// src/context/AuthContext.tsx
import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { CommonConstants } from '../CommonConstants';

// Authentication state stored in localStorage
interface AuthState {
    token: string | null;
    refreshToken: string | null;
    role: string | null;
    tokenExpiry: number | null;
    refreshTokenExpiry: number | null;
}

// Context interface for authentication functionality
interface AuthContextType {
    email: string;
    password: string;
    role: string | null;
    token: string | null;
    setAuthState: (state: AuthState) => void;
    setEmail: (email: string) => void;
    setPassword: (password: string) => void;
    isAuthenticated: boolean;
    logout: () => void;
    refreshAuthToken: () => Promise<boolean>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Retrieve authentication state from localStorage
const getStoredAuthState = (): AuthState => {
    return {
        token: localStorage.getItem('token'),
        refreshToken: localStorage.getItem('refreshToken'),
        role: localStorage.getItem('role'),
        tokenExpiry: Number(localStorage.getItem('tokenExpiry')) || null,
        refreshTokenExpiry: Number(localStorage.getItem('refreshTokenExpiry')) || null,
    };
};

// Check if token is still valid based on expiry time
const isTokenValid = (expiryTime: number | null): boolean => {
    if (!expiryTime) return false;
    return Date.now() < expiryTime;
};

// Check if token should be refreshed (within threshold of expiry)
const shouldRefreshToken = (expiryTime: number | null): boolean => {
    if (!expiryTime) return false;
    return Date.now() > expiryTime - CommonConstants.TOKEN_REFRESH_THRESHOLD;
};

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    // Initialize auth state from localStorage, clearing if refresh token expired
    const [authState, setAuthStateInternal] = useState<AuthState>(() => {
        const storedState = getStoredAuthState();
        
        // Clear stored auth if refresh token is expired
        if (!isTokenValid(storedState.refreshTokenExpiry)) {
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('role');
            localStorage.removeItem('tokenExpiry');
            localStorage.removeItem('refreshTokenExpiry');
            return {
                token: null,
                refreshToken: null,
                role: null,
                tokenExpiry: null,
                refreshTokenExpiry: null,
            };
        }
        
        return storedState;
    });
    
    // Form state for login credentials
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isAuthenticated, setIsAuthenticated] = useState(() => 
        isTokenValid(authState.tokenExpiry) && !!authState.token
    );

    // Update auth state and sync with localStorage
    const setAuthState = useCallback((newState: AuthState) => {
        setAuthStateInternal(newState);
        
        // Update localStorage
        if (newState.token) {
            localStorage.setItem('token', newState.token);
            localStorage.setItem('tokenExpiry', String(newState.tokenExpiry));
        }
        if (newState.refreshToken) {
            localStorage.setItem('refreshToken', newState.refreshToken);
            localStorage.setItem('refreshTokenExpiry', String(newState.refreshTokenExpiry));
        }
        if (newState.role) {
            localStorage.setItem('role', newState.role);
        }
        
        setIsAuthenticated(isTokenValid(newState.tokenExpiry) && !!newState.token);
    }, []);

    // Clear all authentication data
    const logout = useCallback(() => {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('role');
        localStorage.removeItem('tokenExpiry');
        localStorage.removeItem('refreshTokenExpiry');
        
        setAuthStateInternal({
            token: null,
            refreshToken: null,
            role: null,
            tokenExpiry: null,
            refreshTokenExpiry: null,
        });
        
        setIsAuthenticated(false);
    }, []);

    // Refresh access token using refresh token
    const refreshAuthToken = useCallback(async (): Promise<boolean> => {
        if (!authState.refreshToken || !isTokenValid(authState.refreshTokenExpiry)) {
            logout();
            return false;
        }

        try {
            const response = await fetch(`${CommonConstants.BACKEND_END_POINT}/auth/refresh`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: authState.refreshToken,
                }),
            });

            if (!response.ok) {
                throw new Error('Token refresh failed');
            }

            const data = await response.json();
            const newState: AuthState = {
                ...authState,
                token: data.token,
                tokenExpiry: Date.now() + CommonConstants.TOKEN_EXPIRY_TIME,
            };
            
            setAuthState(newState);
            return true;
        } catch (error) {
            console.error('Failed to refresh token:', error);
            logout();
            return false;
        }
    }, [authState, logout, setAuthState]);

    // Auto-refresh token when approaching expiry
    useEffect(() => {
        if (!isAuthenticated || !authState.tokenExpiry) return;

        const checkTokenExpiry = async () => {
            if (shouldRefreshToken(authState.tokenExpiry)) {
                await refreshAuthToken();
            }
        };

        const interval = setInterval(checkTokenExpiry, 60000); // Check every minute
        checkTokenExpiry(); // Check immediately

        return () => clearInterval(interval);
    }, [isAuthenticated, authState.tokenExpiry, refreshAuthToken]);

    const value = {
        email,
        password,
        role: authState.role,
        token: authState.token,
        setAuthState,
        setEmail,
        setPassword,
        isAuthenticated,
        logout,
        refreshAuthToken
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
