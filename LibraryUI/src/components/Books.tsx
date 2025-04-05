import "../styles/Books.css"
import {useCallback, useEffect, useRef, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../context/AuthContext.tsx";
import { toast } from 'react-toastify';
import {CommonConstants} from "../CommonConstants.ts";
import { Unauthorised } from "./Unauthorised.tsx";

interface Book {
    id: string;
    title: string;
    author: string;
    url: string;
    stock: number;
}

type FilterType = 'all' | 'available' | 'out-of-stock';

export const Books = () => {
    const [books, setBooks] = useState<Book[]>([]);
    const [filter, setFilter] = useState<FilterType>('all');
    const [isLoading, setIsLoading] = useState(true);
    const sessionExpiredRef = useRef(false);
    const [borrowLoading, setBorrowLoading] = useState<string | null>(null);
    const [returnLoading, setReturnLoading] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);

    const navigate = useNavigate();

    const { role, isAuthenticated, logout } = useAuth();
    
    const fetchBooks = useCallback(async () => {
        try {
            const token = localStorage.getItem("token");
            if (!token) {
                throw new Error("No authentication token found");
            }

            const response = await fetch(`${CommonConstants.BACKEND_END_POINT}/api/users/books`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                const error = await response.json();
                if ((error.status === 403 || error.status === 401) && !sessionExpiredRef.current) {
                    sessionExpiredRef.current = true;
                    toast.error("Session expired. Please log in again.");
                    logout();
                    return;
                }
                throw new Error('Failed to fetch books');
            }
            const data = await response.json();
            setBooks(data);
            setError(null);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'An error occurred');
        } finally {
            setIsLoading(false);
        }
    }, [logout]);

    const borrowBooks = async (bookId: string) => {
        try {
            const token = localStorage.getItem("token");
            if (!token) {
                throw new Error("No authentication token found");
            }
            setBorrowLoading(bookId);
            const response = await fetch(`${CommonConstants.BACKEND_END_POINT}/api/users/borrow`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ 'bookId': bookId })
            });

            if (!response.ok) {
                const error = await response.json();
                toast.error(error.message);
                return;
            }
            toast.success("Book borrowed successfully! 📚");
            setBooks(prevBooks =>
                prevBooks.map(book =>
                    book.id === bookId ? { ...book, stock: book.stock - 1 } : book
                )
            );
        } catch (err) {
            setBorrowLoading(null);
            toast.error(err instanceof Error ? err.message : 'An error occurred');
        } finally {
            setBorrowLoading(null);
            setIsLoading(false);
        }
    }
    const returnBooks = async (bookId: string) => {
        try {
            const token = localStorage.getItem("token");
            if (!token) {
                throw new Error("No authentication token found");
            }
            setReturnLoading(bookId);
            const response = await fetch(`${CommonConstants.BACKEND_END_POINT}/api/users/return`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ 'bookId': bookId })
            });

            if (!response.ok) {
                const error = await response.json();
                toast.error(error.message);
                return;
            }
            toast.success("Book returned successfully! 📚");
            setBooks(prevBooks =>
                prevBooks.map(book =>
                    book.id === bookId ? { ...book, stock: book.stock + 1 } : book
                )
            );
        } catch (err) {
            setReturnLoading(null);
            setError(err instanceof Error ? err.message : 'An error occurred');
        } finally {
            setReturnLoading(null);
            setIsLoading(false);
        }
    }
    const filteredBooks = books.filter(book => {
        switch (filter) {
            case 'available':
                return book.stock > 0;
            case 'out-of-stock':
                return book.stock === 0;
            default:
                return true;
        }
    });

    useEffect(() => {
        if (isAuthenticated && !sessionExpiredRef.current) {
            fetchBooks();
        }
    }, [fetchBooks, isAuthenticated]);
    // Static book list
    
    if (!isAuthenticated || sessionExpiredRef.current) {
        return (
            <Unauthorised/>
        );
    }

    if (isLoading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Loading books...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="error-container">
                <p className="error-message">{error}</p>
                <button className="retry-button" onClick={fetchBooks}>
                    Try Again
                </button>
            </div>
        );
    }

    return (
        <div className="books-container">
            <div className="books-header">
                <div className="filter-controls">
                    <button
                        className={`filter-button ${filter === 'all' ? 'active' : ''}`}
                        onClick={() => setFilter('all')}
                    >
                        All Books
                    </button>
                    <button
                        className={`filter-button ${filter === 'available' ? 'active' : ''}`}
                        onClick={() => setFilter('available')}
                    >
                        Available
                    </button>
                    <button
                        className={`filter-button ${filter === 'out-of-stock' ? 'active' : ''}`}
                        onClick={() => setFilter('out-of-stock')}
                    >
                        Out of Stock
                    </button>
                </div>
                {role === 'ADMIN' && (
                    <div className="add_book">
                        <button className="add-book-button" onClick={() => navigate('/add_book')}> + Add Book</button>
                    </div>
                )}
            </div>
            <div className="books-grid">
                {filteredBooks.length > 0 ? (
                    filteredBooks.map((book) => (
                        <div key={book.id} className="book-card">
                            <div className="book-image">
                                <img
                                    src={book.url}
                                    alt={book.title}
                                    onError={(e) => {
                                        const target = e.target as HTMLImageElement;
                                        target.src = 'https://via.placeholder.com/150x200?text=No+Image';
                                    }}
                                />
                            </div>
                            <div className="book-info">
                                <h2 className="book-title">{book.title}</h2>
                                <p className="book-author">by {book.author}</p>
                                <div className="book-stock">
                                    <span className={`stock-status ${book.stock > 0 ? 'in-stock' : 'out-of-stock'}`}>
                                        {book.stock > 0 ? `${book.stock} available` : 'Out of stock'}
                                    </span>
                                </div>
                                <div className="book-actions">
                                    <button
                                        className="borrow-button"
                                        disabled={book.stock === 0}
                                        onClick={() => borrowBooks(book.id)}
                                    >
                                        {borrowLoading === book.id ? 'Borrowing...' : 'Borrow'}
                                    </button>
                                    <button
                                        className="return-button"
                                        onClick={() => returnBooks(book.id)}
                                    >
                                        {returnLoading === book.id ? 'Returning...' : 'Return Book'}
                                    </button>
                                </div>

                            </div>
                        </div>
                    ))
                ) : (
                    <div className="no-books-message">
                        No books found for the selected filter.
                    </div>
                )}
            </div>
        </div>
    );
};
