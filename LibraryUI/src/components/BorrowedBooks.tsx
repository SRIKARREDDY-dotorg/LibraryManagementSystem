import {useEffect, useState} from "react";
import {CommonConstants} from "../CommonConstants.ts";
import "../styles/BorrowedBooks.css";
import {useAuth} from "../context/AuthContext.tsx";

export const BorrowedBooks = () => {
    const [books, setBooks] = useState([]);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const {role, isAuthenticated} = useAuth();
    useEffect(() => {
        fetchBorrowedBooks();
    }, []);
    const formatDate = (isoString: string) => {
        if (!isoString) return "N/A";
        const options: Intl.DateTimeFormatOptions = {
            year: 'numeric',
            month: 'short',
            day: '2-digit'
        };
        return new Date(isoString).toLocaleDateString(undefined, options);
    };

    const fetchBorrowedBooks = async () => {
        const token = localStorage.getItem("token");
        if (!token) {
            throw new Error("No authentication token found");
        }
        try {
            setIsLoading(true);
            console.log("Fetching borrowed books...");
            const response = await fetch(`${CommonConstants.BACKEND_END_POINT}/api/users/checkBorrowed`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
            const data = await response.json();
            setBooks(data);
            console.log("Borrowed books:", data);
        } catch (error: any) {
            setError(error.message);
            console.error('Error fetching borrowed books:', error);
        } finally {
            setIsLoading(false);
        }
    }
    // Render the books data in a table here
    if (isLoading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Borrowed books...</p>
            </div>
        );
    }
    if (error) {
        return (
            <div className="error-container">
                <p className="error-message">{error}</p>
                <button className="retry-button" onClick={fetchBorrowedBooks}>
                    Try Again
                </button>
            </div>
        );
    }
    if (!isAuthenticated) {
        window.location.href = "/login";
    }
    return (
        <div className="borrowed-books-container">
            <table className="borrowed-books-table">
                <thead>
                <tr>
                    <th>Image</th>
                    <th>Title</th>
                    <th>Author</th>
                    {role === "ADMIN" && <th>Borrower</th>}
                    <th>Due Date</th>
                </tr>
                </thead>
                <tbody>
                {books.map((book: any) => (
                    <tr key={book.id}>
                        <td>
                            <img
                                src={book.url}
                                alt={book.title}
                                className="borrowed-book-image"
                                onError={(e) => {
                                    (e.target as HTMLImageElement).src = "https://via.placeholder.com/100"; // fallback image
                                }}
                            />
                        </td>
                        <td>{book.title}</td>
                        <td>{book.author}</td>
                        {role === "ADMIN" && <td>{book.borrowerId}</td>}
                        <td>{formatDate(book.dueDate)}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}