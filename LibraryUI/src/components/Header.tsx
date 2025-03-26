export const Header = () => {
    return (
        <div style={{
            backgroundColor: '#ffffff', 
            top: 0, 
            width: '100%', 
            position: 'fixed', 
            boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
            borderBottom: '1px solid #ddd',
            padding: '1rem',
            zIndex: 1000,
            justifyItems: 'center'
        }}>
            <h1 style={{
                    fontSize: '24px', 
                    fontWeight: '500', 
                    fontFamily: '"Inter", sans-serif',
                    margin: 0
                }}>📚 Library Management System</h1>
        </div>
    );
};
