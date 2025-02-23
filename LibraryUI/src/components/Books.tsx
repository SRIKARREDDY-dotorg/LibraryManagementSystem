import "../styles/Books.css"
import {useEffect, useState} from "react";

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
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetchBooks();
    }, []);
    // Static book list
    const fetchBooks = async () => {
        try {
            const token = localStorage.getItem("token");
            if (!token) {
                throw new Error("No authentication token found");
            }

            const response = await fetch('http://localhost:8080/api/users/books', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
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
                                <button
                                    className="borrow-button"
                                    disabled={book.stock === 0}
                                >
                                    Borrow Book
                                </button>
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
