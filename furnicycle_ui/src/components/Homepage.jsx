import React from "react";
import sofa from "../assets/sofa.png";
import chair from "../assets/chair.jpg";
import coffeetable from "../assets/CoffeeTable.jpg";
import kitchentable from "../assets/KitchenTable.jpg";

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
                    {/* Card 1 */}
                    <div className="col-md-3 mb-4">
                        <div className="card" style={{ width: "100%" }}>
                            <img src={sofa} className="card-img-top" alt="Sofa" />
                            <div className="card-body">
                                <h5 className="card-title">Sofa</h5>
                                <p className="card-text">A comfortable and stylish sofa for your living room.</p>
                                <a href="#" className="btn btn-primary">View Details</a>
                            </div>
                        </div>
                    </div>

                    {/* Card 2 */}
                    <div className="col-md-3 mb-4">
                        <div className="card" style={{ width: "100%" }}>
                            <img src={chair} className="card-img-top" alt="Chair" />
                            <div className="card-body">
                                <h5 className="card-title">Chair</h5>
                                <p className="card-text">Elegant chair for comfort and style in your space.</p>
                                <a href="#" className="btn btn-primary">View Details</a>
                            </div>
                        </div>
                    </div>

                    {/* Card 3 */}
                    <div className="col-md-3 mb-4">
                        <div className="card" style={{ width: "100%" }}>
                            <img src={kitchentable} className="card-img-top" alt="Dining Set" />
                            <div className="card-body">
                                <h5 className="card-title">Dining Set</h5>
                                <p className="card-text">Sleek and modern dining set to enhance your dining area.</p>
                                <a href="#" className="btn btn-primary">View Details</a>
                            </div>
                        </div>
                    </div>

                    {/* Card 4 */}
                    <div className="col-md-3 mb-4">
                        <div className="card" style={{ width: "100%" }}>
                            <img src={coffeetable} className="card-img-top" alt="Coffee Table" />
                            <div className="card-body">
                                <h5 className="card-title">Coffee Table</h5>
                                <p className="card-text">A stylish coffee table to complete your living room setup.</p>
                                <a href="#" className="btn btn-primary">View Details</a>
                            </div>
                        </div>
                    </div>
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
