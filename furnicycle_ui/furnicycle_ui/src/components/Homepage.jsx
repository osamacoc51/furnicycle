import React, { useState } from "react";
import sofa from "../assets/sofa.png";
import ProductList from "./UserPages/ProductList";

export const Homepage = () => {
    return (
        <>
            {/* Main Container */}
            <div style={{ backgroundColor: "#0b3817", color: "#fff", height: "100vh", display: "flex", alignItems: "center", justifyContent: "space-between", padding: "20px" }}>
                {/* Left Side: Quote */}
                <div style={{ flex: "1", padding: "20px" }}>
                    <h1>Welcome to Furnicycle</h1>
                    <p style={{ fontSize: "1.5rem", lineHeight: "1.8" }}>
                        "Transform your space with timeless furniture. Comfort, style, and quality that lasts a lifetime."
                    </p>
                </div>

                {/* Right Side: Image */}
                <div style={{ flex: "1", display: "flex", justifyContent: "center", alignItems: "center" }}>
                    <img
                        src={sofa}
                        alt="Furniture Showcase"
                        style={{ maxWidth: "100%", height: "auto", borderRadius: "10px" }}
                    />
                </div>
            </div>

            {/* New Section: Additional Content */}
            <div style={{ backgroundColor: "#2d5a3f", color: "#fff", padding: "50px 20px", textAlign: "center" }}>
                <h2>Our Furniture Collection</h2>
                <p style={{ fontSize: "1.2rem", lineHeight: "1.6" }}>
                    Explore our wide range of furniture to suit your style and needs. From cozy sofas to elegant dining sets, we have it all.
                </p>
                <button
                    style={{
                        backgroundColor: "#fff",
                        color: "#0b3817",
                        border: "none",
                        padding: "10px 20px",
                        fontSize: "1rem",
                        cursor: "pointer",
                        borderRadius: "5px",
                    }}
                >
                    Browse Collection
                </button>

                {/* Cards Section: 4 cards side by side */}
                <div className="row mt-4">
                    <ProductList />
                </div>

                <div style={{ marginTop: "20px" }}>
                    <h3>Why Choose Furnicycle?</h3>
                    <p style={{ fontSize: "1rem", lineHeight: "1.4" }}>
                        We prioritize quality, style, and customer satisfaction. Our commitment to excellence ensures that every piece meets the highest standards.
                    </p>
                </div>
            </div>


        </>
    );
};
