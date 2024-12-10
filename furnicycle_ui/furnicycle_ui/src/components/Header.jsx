import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import UserService from "../services/UserService";

export const Header = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false); // Track login state
    const navigate = useNavigate();

    // Check login status on component mount and when session changes
    useEffect(() => {
        const checkLoginStatus = () => {
            const user = UserService.getCurrentUser(); // Check if user exists in sessionStorage
            setIsLoggedIn(!!user); // Update login state based on user data
        };

        checkLoginStatus(); // Initial check for login status

        // Optional: Listen to session changes
        const storageListener = () => checkLoginStatus();
        window.addEventListener("storage", storageListener);

        // Cleanup listener
        return () => {
            window.removeEventListener("storage", storageListener);
        };
    }, []); // Empty dependency array so this runs only once

    // Handle Logout functionality
    const handleLogout = () => {
        UserService.logout(); // Clear session data
        setIsLoggedIn(false); // Immediately update login state
        navigate("/"); // Redirect to homepage
    };

    // Handle Login Button (redirect to login page)
    const handleLogin = () => {
        navigate("/login");
    };

    return (
        <nav className="navbar navbar-expand-lg">
            <div className="container-fluid">
                <a className="navbar-brand text-white" href="#">
                    Furnicycle
                </a>
                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                >
                    <span className="navbar-toggler-icon" style={{ color: "#fff" }}></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                            <Link className="nav-link text-white" to="/">
                                Home
                            </Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link text-white" to="/productlist">
                                Shop
                            </Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link text-white" to="/cart">
                                Cart
                            </Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link text-white" to="/about-us">
                                About Us
                            </Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link text-white" to="/contact-us">
                                Contact Us
                            </Link>
                        </li>
                    </ul>
                    <div className="d-flex">
                        {!isLoggedIn ? (
                            <>
                                <Link to="/register" className="btn btn-light me-2">
                                    Signup
                                </Link>
                                <button className="btn btn-light me-2" onClick={handleLogin}>
                                    Login
                                </button>
                            </>
                        ) : (
                            <button
                                className="btn btn-danger me-2"
                                onClick={handleLogout}
                            >
                                Logout
                            </button>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
};
