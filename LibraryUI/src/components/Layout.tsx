import { Outlet } from 'react-router-dom';
import { Header } from './Header';
import { Footer } from './Footer';
import '../styles/Layout.css';

export const Layout = () => {
    return (
        <div className="layout-container">
            <Header />
            <main className="main-content">
                <Outlet />
            </main>
            <Footer />
        </div>
    );
};
