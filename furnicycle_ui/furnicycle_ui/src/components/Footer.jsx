// Footer.js

import React from "react";

export const Footer = () => {
    return (
        <footer style={{
            backgroundColor: "#0b3817",
            color: "#fff", // White text
            padding: "20px", // Padding around content
            textAlign: "center", // Center the text
            position: "fixed",
            bottom: "0",
            width: "100%", // Ensure footer spans entire width
        }}>
            <p style={{ margin: "0" }}>© 2024 Furnicycle - All rights reserved</p>
            <p style={{ margin: "5px 0 0" }}>
                <a href="#" style={{ color: "#fff", textDecoration: "none" }}>Privacy Policy</a> | <a href="#" style={{ color: "#fff", textDecoration: "none" }}>Terms of Service</a>
            </p>
        </footer>
    );
};
