import "../styles/Books.css"
import {useState} from "react";

interface Book {
    id: string;
    title: string;
    author: string;
    url: string;
    stock: number;
}

type FilterType = 'all' | 'available' | 'out-of-stock';

export const Books = () => {
    const [filter, setFilter] = useState<FilterType>('all');
    // Static book list
    const books: Book[] = [
        {
            id: "1",
            title: "The Great Gatsby",
            author: "F. Scott Fitzgerald",
            url: "https://cdn.kobo.com/book-images/f37f5bc2-8171-475a-99fb-2f807813e085/1200/1200/False/the-great-gatsby-deluxe-hardbound-edition.jpg",
            stock: 5
        },
        {
            id: "2",
            title: "To Kill a Mockingbird",
            author: "Harper Lee",
            url: "https://m.media-amazon.com/images/I/811NqsxadrS._AC_UF1000,1000_QL80_.jpg",
            stock: 3
        },
        {
            id: "3",
            title: "1984",
            author: "George Orwell",
            url: "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1327144697i/3744438.jpg",
            stock: 7
        },
        {
            id: "4",
            title: "Pride and Prejudice",
            author: "Jane Austen",
            url: "https://m.media-amazon.com/images/I/81mTGi9gs-L._AC_UF1000,1000_QL80_.jpg",
            stock: 2
        },
        {
            id: "5",
            title: "The Hobbit",
            author: "J.R.R. Tolkien",
            url: "https://m.media-amazon.com/images/I/717TGeIkVML._AC_UF1000,1000_QL80_.jpg",
            stock: 4
        }
    ];

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

    return (
        <div className="books-container">
            <div className="books-header">
                <h1 className="books-title">Available Books</h1>
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
