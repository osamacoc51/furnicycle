import React from "react";
import { Link } from "react-router-dom";

export const Header = () => {
    return (
        <>
            <nav className="navbar navbar-expand-lg">
                <div className="container-fluid">
                    <a className="navbar-brand text-white" href="#">Furnicycle</a>
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
                                <Link className="nav-link text-white" aria-current="page" to="/">
                                    Home
                                </Link>
                            </li>

                            <li className="nav-item">
                                <a className="nav-link text-white" href="#">Shop</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link text-white" href="#">About Us</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link text-white" href="#">Services</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link text-white" href="#">Contact Us</a>
                            </li>
                        </ul>
                        <div className="d-flex">
                            <button className="btn btn-light me-2" type="button">Signup</button>
                            <button className="btn btn-light" type="button">Login</button>
                        </div>
                    </div>
                </div>
            </nav>
        </>
    );
};
