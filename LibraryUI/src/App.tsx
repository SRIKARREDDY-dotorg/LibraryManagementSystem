import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import {Home} from "./components/Home.tsx";
import {Login} from "./components/Login.tsx";
import {Register} from "./components/Register.tsx";
import {Layout} from "./components/Layout.tsx";
import {Books} from "./components/Books.tsx";
import {Book} from "./components/Book.tsx";
import {AuthProvider} from "./context/AuthContext.tsx";

export default function LibraryApp() {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route element={<Layout />}>
                        <Route path="/" element={<Home />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/signup" element={<Register />} />
                        <Route path="/books" element={<Books />} />
                        <Route path="/add_book" element={<Book />}/>
                        {/* Add more routes as needed */}
                    </Route>
                </Routes>
            </Router>
        </AuthProvider>
    );
}