import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import '../styles/Book.css';
import { useAuth } from '../context/AuthContext.tsx';
import { CommonConstants } from '../CommonConstants.ts';
import { Unauthorised } from './Unauthorised.tsx';
import { toast } from 'react-toastify';

interface BookFormData {
    title: string;
    author: string;
    stock: number;
    url: string;
}

interface Book {
    id: string;
    title: string;
    author: string;
    url: string;
    stock: number;
}

export const UpdateBook = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const location = useLocation();
    const { role, isAuthenticated, token } = useAuth();
    
    const [formData, setFormData] = useState<BookFormData>({
        title: '',
        author: '',
        stock: 0,
        url: ''
    });
    
    const [errors, setErrors] = useState<Partial<BookFormData>>({});

    if (!isAuthenticated || role !== 'ADMIN') {
        return <Unauthorised />;
    }

    // eslint-disable-next-line react-hooks/rules-of-hooks
    useEffect(() => {
        const book = location.state?.book as Book;
        if (book) {
            setFormData({
                title: book.title,
                author: book.author,
                stock: book.stock,
                url: book.url
            });
        } else {
            toast.error('Book data not found');
            navigate('/books');
        }
    }, [location.state, navigate]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: name === 'stock' ? parseInt(value) || 0 : value
        }));
    };

    const validateForm = (): boolean => {
        const newErrors: Partial<BookFormData> = {};

        if (!formData.title.trim()) {
            newErrors.title = 'Title is required';
        }

        if (!formData.author.trim()) {
            newErrors.author = 'Author is required';
        }

        if (formData.stock < 0) {
            newErrors.stock = 0;
        }

        if (!formData.url.trim()) {
            newErrors.url = 'URL is required';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (validateForm()) {
            try {
                const response = await fetch(`${CommonConstants.BACKEND_END_POINT}/api/users/update_book`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify({
                        ...formData,
                        id: id
                    })
                });

                if (response.ok) {
                    toast.success('Book updated successfully! 📚');
                    navigate('/books');
                } else {
                    const error = await response.json();
                    toast.error(error.message || 'Failed to update book');
                }
            } catch (error) {
                toast.error('Error updating book');
            }
        }
    };

    return (
        <div className="add-book-form-container">
            <h2 className="form-title">Update Book</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="title">Title</label>
                    <input
                        type="text"
                        id="title"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        className={errors.title ? 'error' : ''}
                    />
                    {errors.title && <div className="error-message">{errors.title}</div>}
                </div>

                <div className="form-group">
                    <label htmlFor="author">Author</label>
                    <input
                        type="text"
                        id="author"
                        name="author"
                        value={formData.author}
                        onChange={handleChange}
                        className={errors.author ? 'error' : ''}
                    />
                    {errors.author && <div className="error-message">{errors.author}</div>}
                </div>

                <div className="form-group">
                    <label htmlFor="stock">Stock</label>
                    <input
                        type="number"
                        id="stock"
                        name="stock"
                        value={formData.stock}
                        onChange={handleChange}
                        min="0"
                        className={errors.stock ? 'error' : ''}
                    />
                    {errors.stock && <div className="error-message">{errors.stock}</div>}
                </div>

                <div className="form-group">
                    <label htmlFor="url">Book Cover URL</label>
                    <input
                        type="url"
                        id="url"
                        name="url"
                        value={formData.url}
                        onChange={handleChange}
                        className={errors.url ? 'error' : ''}
                    />
                    {errors.url && <div className="error-message">{errors.url}</div>}
                </div>

                <div className="button-group">
                    <button type="button" className="cancel-button" onClick={() => navigate('/books')}>
                        Cancel
                    </button>
                    <button type="submit" className="submit-button">
                        Submit
                    </button>
                </div>
            </form>
        </div>
    );
};