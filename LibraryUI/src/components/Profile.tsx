import { useState } from 'react';
import '../styles/Profile.css';
import { toast } from 'react-toastify';

export const Profile = () => {
    const [activeTab, setActiveTab] = useState('profile');
    const [email, setEmail] = useState('user@example.com');
    const [password, setPassword] = useState('secret123');
    const [message, setMessage] = useState('');
    
    const handleUpdate = () => {
        // Simulate an update operation
        setMessage('Profile updated successfully!');
        setTimeout(() => setMessage(''), 3000); // Clear after 3s
    };
    const handleSignout = () => {
        toast.info('Signed out! (you can add redirect logic here)');
    };
    return (
        <div className="profile-container">
            {/* Sidebar */}
            <div className="sidebar">
                <ul className="menu-list">
                    <li 
                        className={`menu-item ${activeTab === 'profile' ? 'active' : ''}`}
                        onClick={() => setActiveTab('profile')}
                    >
                        Profile
                    </li>
                    <li 
                        className={`menu-item ${activeTab === 'dashboard' ? 'active' : ''}`}
                        onClick={() => setActiveTab('dashboard')}
                    >
                        Dashboard
                    </li>
                </ul>
            </div>

            {/* Main Content */}
            <div className="main-content">
                {activeTab === 'profile' && (
                    <div className="profile-form">
                        <div className="profile-icon">👤</div>
                        <label>
                            Email:
                            <input 
                                type="email" 
                                value={email} 
                                onChange={(e) => setEmail(e.target.value)} 
                            />
                        </label>
                        <label>
                            Password:
                            <input 
                                type="password" 
                                value={password} 
                                onChange={(e) => setPassword(e.target.value)} 
                            />
                        </label>
                        <div className="button-group">
                            <button onClick={handleUpdate}>Update</button>
                            <button className="signout" onClick={handleSignout}>Signout</button>
                        </div>
                        {message && <p className="update-msg">{message}</p>}
                    </div>
                )}
                {activeTab === 'dashboard' && (
                    <div>
                        <h1>Dashboard</h1>
                        <p>This is the dashboard page.</p>
                        <p>Here you can view your dashboard information.</p>
                        <p>More features coming soon!</p>
                    </div>
                )}
            </div>
        </div>
    );
};