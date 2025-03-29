import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import {Home} from "./components/Home.tsx";
import {Login} from "./components/Login.tsx";
import {Register} from "./components/Register.tsx";
import {Books} from "./components/Books.tsx";
import {Book} from "./components/Book.tsx";
import {AuthProvider} from "./context/AuthContext.tsx";
import { ToastContainer } from 'react-toastify';
import './App.css';
import { Header } from "./components/Header.tsx";
import { Footer } from "./components/Footer.tsx";
import ScrollToTop from "./ScrollToTop.tsx";
import { Profile } from "./components/Profile.tsx";

export default function LibraryApp() {
    return (
        <>
            <ToastContainer position="top-right" autoClose={3000} aria-label={undefined} />
            <div className="app-container">
                <AuthProvider>
                    <Router>
                        <Header/>
                        <ScrollToTop /> {/* Todo fix Scroll to top on route change*/}
                        <Routes>
                            <Route path="/" element={<Home />} />
                            <Route path="/login" element={<Login />} />
                            <Route path="/signup" element={<Register />} />
                            <Route path="/books" element={<Books />} />
                            <Route path="/add_book" element={<Book />}/>
                            <Route path="/profile" element={<Profile />} />
                            {/* Add more routes as needed */}
                        </Routes>
                    </Router>
                </AuthProvider>
            </div>
            <Footer/>
        </>
    );
}