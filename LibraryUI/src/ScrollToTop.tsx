import { useEffect } from "react";
import { useLocation } from "react-router-dom";

const ScrollToTop = () => {
    const { pathname } = useLocation();

    useEffect(() => {
        window.scrollTo({
            top: 0,
            behavior: "smooth", // Adds smooth scrolling effect
        });
    }, [pathname]); // Runs whenever the URL path changes

    return null;
};

export default ScrollToTop;
