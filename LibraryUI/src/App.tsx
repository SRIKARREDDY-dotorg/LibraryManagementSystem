import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import {Home} from "./components/Home.tsx";
import {Login} from "./components/Login.tsx";
import {Register} from "./components/Register.tsx";

export default function LibraryApp() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<Register />} />
            </Routes>
        </Router>
    );
}