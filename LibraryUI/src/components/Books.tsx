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

interface PageResponse {
    content: Book[];
    currentPage: number;
    totalPages: number;
    totalElements: number;
    hasNext: boolean;
    hasPrevious: boolean;
}

type FilterType = 'all' | 'available' | 'out-of-stock';

export const Books = () => {
    const [books, setBooks] = useState<Book[]>([]);
    const [masterBooks, setMasterBooks] = useState<Book[]>([]);
    const [allBooks, setAllBooks] = useState<Book[]>([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [isSearchFocused, setIsSearchFocused] = useState(false);
    const [filter, setFilter] = useState<FilterType>('all');
    const [isLoading, setIsLoading] = useState(true);
    const [isPaginationLoading, setIsPaginationLoading] = useState(false);
    const [isSearching, setIsSearching] = useState(false);
    const sessionExpiredRef = useRef(false);
    const [borrowLoading, setBorrowLoading] = useState<string | null>(null);
    const [returnLoading, setReturnLoading] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [hasNext, setHasNext] = useState(false);
    const [hasPrevious, setHasPrevious] = useState(false);
    const [openDropdown, setOpenDropdown] = useState<string | null>(null);

    const navigate = useNavigate();
    const { role, isAuthenticated, logout, token } = useAuth();
    
    const fetchBooks = useCallback(async () => {
        try {
            if (!token) {
                throw new Error("No authentication token found");
            }

            const response = await fetch(`${CommonConstants.BACKEND_END_POINT}/api/users/books?page=${currentPage}&size=12`, {
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
            const data: PageResponse = await response.json();
            setBooks(data.content);
            setMasterBooks(data.content);
            setTotalPages(data.totalPages);
            setHasNext(data.hasNext);
            setHasPrevious(data.hasPrevious);
            setError(null);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'An error occurred');
        } finally {
            setIsLoading(false);
            setIsPaginationLoading(false);
        }
    }, [currentPage, logout, token]);

    const fetchAllPagesForSearch = useCallback(async () => {
        if (!token || allBooks.length > 0) return;
        
        setIsSearching(true);
        const allBooksData: Book[] = [];
        
        try {
            // Fetch all pages in parallel
            const pagePromises = [];
            for (let page = 0; page < totalPages; page++) {
                if (page === currentPage) continue; // Skip current page as we already have it
                
                pagePromises.push(
                    fetch(`${CommonConstants.BACKEND_END_POINT}/api/users/books?page=${page}&size=12`, {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${token}`
                        }
                    }).then(res => res.json())
                );
            }
            
            const results = await Promise.all(pagePromises);
            results.forEach(data => {
                allBooksData.push(...data.content);
            });
            
            // Add current page books
            allBooksData.push(...masterBooks);
            setAllBooks(allBooksData);
        } catch (err) {
            console.error('Failed to fetch all pages:', err);
        } finally {
            setIsSearching(false);
        }
    }, [token, totalPages, currentPage, masterBooks, allBooks.length]);

    const borrowBooks = async (bookId: string) => {
        try {
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
        const timeoutId = setTimeout(() => {
            const searchTerm = searchQuery.trim().toLowerCase();
            
            if (!searchTerm) {
                setBooks(masterBooks);
                return;
            }

            // Immediately search current page
            const currentPageResults = masterBooks.filter(book =>
                book.title.toLowerCase().includes(searchTerm) ||
                book.author.toLowerCase().includes(searchTerm)
            );

            // Start fetching other pages in background
            fetchAllPagesForSearch();

            // Search in all available books (current + fetched)
            const searchInAllBooks = () => {
                const allAvailableBooks = allBooks.length > 0 ? allBooks : masterBooks;
                const allResults = allAvailableBooks.filter(book =>
                    book.title.toLowerCase().includes(searchTerm) ||
                    book.author.toLowerCase().includes(searchTerm)
                );
                setBooks(allResults);
            };

            if (currentPageResults.length > 0) {
                setBooks(currentPageResults);
                // Still search all books in background to get complete results
                setTimeout(searchInAllBooks, 100);
            } else {
                searchInAllBooks();
            }
        }, 300);

        return () => clearTimeout(timeoutId);
    }, [searchQuery, masterBooks, allBooks, fetchAllPagesForSearch]);
    
    useEffect(() => {
        if (isAuthenticated && !sessionExpiredRef.current) {
            fetchBooks();
        }
    }, [fetchBooks, isAuthenticated]);

    // Close dropdown when clicking outside
    useEffect(() => {
        const handleClickOutside = () => setOpenDropdown(null);
        document.addEventListener('click', handleClickOutside);
        return () => document.removeEventListener('click', handleClickOutside);
    }, []);
    
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
                <div className="search-container">
                    <input
                        type="text"
                        className="search-input"
                        placeholder={isSearchFocused ? '' : '🔍  Search by title or author'}
                        value={searchQuery}
                        onFocus={() => setIsSearchFocused(true)}
                        onBlur={() => setIsSearchFocused(false)}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                    {isSearching && (
                        <div className="search-loading">
                            <div className="loading-spinner-small"></div>
                        </div>
                    )}
                </div>
            </div>
            <div className="books-grid">
                {filteredBooks.length > 0 ? (
                    filteredBooks.map((book) => (
                        <div key={book.id} className="book-card">
                            {role === 'ADMIN' && (
                                <div className="book-menu">
                                    <button 
                                        className="menu-button"
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            setOpenDropdown(openDropdown === book.id ? null : book.id);
                                        }}
                                    >
                                        ⋮
                                    </button>
                                    {openDropdown === book.id && (
                                        <div className="dropdown-menu">
                                            <button 
                                                className="dropdown-item"
                                                onClick={() => {
                                                    navigate(`/update_book/${book.id}`, { state: { book } });
                                                    setOpenDropdown(null);
                                                }}
                                            >
                                                Update Book
                                            </button>
                                        </div>
                                    )}
                                </div>
                            )}
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
            <div className="pagination-controls">
                <button
                    className="pagination-button"
                    onClick={() => {
                        setIsPaginationLoading(true);
                        setCurrentPage(prev => prev - 1);
                    }}
                    disabled={!hasPrevious || isPaginationLoading}
                >
                    {isPaginationLoading && currentPage > 0 ? (
                        <span className="button-loading">
                            <div className="loading-spinner-small"></div>
                        </span>
                    ) : (
                        'Previous'
                    )}
                </button>
                <span className="page-info">
                    Page {currentPage + 1} of {totalPages}
                </span>
                <button
                    className="pagination-button"
                    onClick={() => {
                        setIsPaginationLoading(true);
                        setCurrentPage(prev => prev + 1);
                    }}
                    disabled={!hasNext || isPaginationLoading}
                >
                    {isPaginationLoading && currentPage < totalPages - 1 ? (
                        <span className="button-loading">
                            <div className="loading-spinner-small"></div>
                        </span>
                    ) : (
                        'Next'
                    )}
                </button>
            </div>
        </div>
    );
};