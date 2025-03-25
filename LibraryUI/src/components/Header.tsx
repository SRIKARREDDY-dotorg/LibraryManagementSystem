export const Header = () => {
    return (
        <header style={{
            backgroundColor: '#bed8f3', 
            top: 0, 
            width: '100%', 
            position: 'fixed', 
            boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
            padding: '15px 0',
            borderBottom: '1px solid #ddd',
            zIndex: 1000
        }}>
            <nav style={{
                display: 'flex', 
                justifyContent: 'center', 
                alignItems: 'center',
                gap: '20px'
            }}>
                <h1 style={{
                    fontSize: '24px', 
                    fontWeight: '500', 
                    fontFamily: '"Inter", sans-serif',
                    margin: 0
                }}>
                    Library Management System
                </h1>
                {/* Add navigation links here */}
            </nav>
        </header>
    );
};
