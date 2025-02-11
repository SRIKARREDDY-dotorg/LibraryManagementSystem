import { useState } from "react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/users";

function Home() {
    return (
        <div className="p-4 max-w-md mx-auto">
            <h1 className="text-xl font-bold">Library Management</h1>
            <nav>
                <Link to="/create-user" className="block p-2 bg-blue-500 text-white m-2">Create User</Link>
                <Link to="/books" className="block p-2 bg-green-500 text-white m-2">View Books</Link>
            </nav>
        </div>
    );
}

function CreateUser() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");

    const createUser = async () => {
        try {
            if (!name || !email) {
                alert("Please fill in all fields");
                return;
            }
            const response = await axios.post(API_BASE_URL, { name, email });
            alert(`User created successfully with ID: ${response.data.id}`);
        } catch (error) {
            const errorMessage = error.response?.data?.message || "Failed to create user";
            alert(`Error: ${errorMessage}`);
        }
    };

    return (
        <div className="p-4 max-w-md mx-auto">
            <h1 className="text-xl font-bold">Create User</h1>
            <input type="text" placeholder="Name" value={name} onChange={(e) => setName(e.target.value)} className="border p-2 m-2" />
            <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} className="border p-2 m-2" />
            <button onClick={createUser} className="bg-blue-500 text-white p-2 m-2">Create User</button>
        </div>
    );
}

function Books() {
    const [userId, setUserId] = useState("");
    const [books, setBooks] = useState<any[]>([]);
    const [bookId, setBookId] = useState("");

    const fetchBooks = async () => {
        try {
            if (!userId) {
                alert("Please enter a user ID");
                return;
            }
            const response = await axios.get(`${API_BASE_URL}/${userId}/books`);
            setBooks(response.data);
            alert("Books fetched successfully");
        } catch (error) {
            const errorMessage = error.response?.data?.message || "Failed to fetch books";
            alert(`Error: ${errorMessage}`);
        }
    };

    const borrowBook = async () => {
        try {
            await axios.post(`${API_BASE_URL}/${userId}/books/${bookId}/borrow`);
            alert("Book borrowed successfully");
        } catch (error) {
            alert("Failed to borrow book");
        }
    };

    const returnBooks = async () => {
        try {
            await axios.post(`${API_BASE_URL}/${userId}/books/return`, [bookId]);
            alert("Books returned successfully");
        } catch (error) {
            alert("Failed to return books");
        }
    };

    return (
        <div className="p-4 max-w-md mx-auto">
            <h1 className="text-xl font-bold">Library Books</h1>
            <input type="text" placeholder="User ID" value={userId} onChange={(e) => setUserId(e.target.value)} className="border p-2 m-2" />
            <button onClick={fetchBooks} className="bg-green-500 text-white p-2 m-2">View Books</button>
            <input type="text" placeholder="Book ID" value={bookId} onChange={(e) => setBookId(e.target.value)} className="border p-2 m-2" />
            <button onClick={borrowBook} className="bg-yellow-500 text-white p-2 m-2">Borrow Book</button>
            <button onClick={returnBooks} className="bg-red-500 text-white p-2 m-2">Return Books</button>
            <h2 className="text-lg font-semibold">Books:</h2>
            <ul>
                {books.map((book) => (
                    <li key={book.id}>{book.title}</li>
                ))}
            </ul>
        </div>
    );
}

export default function LibraryApp() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/create-user" element={<CreateUser />} />
                <Route path="/books" element={<Books />} />
            </Routes>
        </Router>
    );
}