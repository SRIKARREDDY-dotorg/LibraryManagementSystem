import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import {Home} from "./components/Home.tsx";
import {Login} from "./components/Login.tsx";
import {Register} from "./components/Register.tsx";
import {Layout} from "./components/Layout.tsx";
import {Books} from "./components/Books.tsx";

export default function LibraryApp() {
    return (
        <Router>
            <Routes>
                <Route element={<Layout />}>
                    <Route path="/" element={<Home />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/signup" element={<Register />} />
                    <Route path="/books" element={<Books />} />
                    {/* Add more routes as needed */}
                </Route>
            </Routes>
        </Router>
    );
}