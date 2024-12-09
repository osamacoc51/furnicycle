import React from "react";
import aboutus from "../assets/aboutus.jpg";
import { Link } from "react-router-dom";

const AboutUs = () => {
    return (
        <div className="container my-5">
            <div className="row align-items-center">
                <div className="col-md-6">
                    <img
                        src={aboutus}
                        alt="About Us"
                        className="img-fluid rounded shadow"
                    />
                </div>

                <div className="col-md-6">
                    <h2 className="mb-4">About Us</h2>
                    <p className="text-muted">
                        Welcome to our furniture store! We specialize in providing premium, stylish, and durable furniture
                        that transforms your house into a home. Our mission is to bring you closer to the designs that match your taste,
                        making your living spaces cozy and elegant.
                    </p>
                    <p className="text-muted">
                        With years of experience in the furniture industry, we take pride in our collection that blends
                        timeless craftsmanship with modern trends. From sofas to dining tables, every piece is curated to enhance
                        comfort and aesthetics.
                    </p>
                    <p className="text-muted">
                        Our commitment to quality and customer satisfaction drives us every day. Whether you're furnishing a
                        new home or upgrading your space, we're here to help you make the best choice.
                    </p>
                    <Link to="/shop" className="btn btn-primary mt-3">
                        Shop Now
                    </Link>
                </div>
            </div>

            {/* Values Section */}
            <div className="mt-5">
                <h3 className="text-center mb-4">Why Choose Us?</h3>
                <div className="row text-center">
                    <div className="col-md-4">
                        <i className="bi bi-stars fs-1 text-primary"></i>
                        <h5 className="mt-3">High-Quality Products</h5>
                        <p className="text-muted">
                            We source only the best materials to ensure our furniture stands the test of time.
                        </p>
                    </div>
                    <div className="col-md-4">
                        <i className="bi bi-truck fs-1 text-primary"></i>
                        <h5 className="mt-3">Fast & Reliable Delivery</h5>
                        <p className="text-muted">
                            Enjoy hassle-free and timely delivery to your doorstep, every time.
                        </p>
                    </div>
                    <div className="col-md-4">
                        <i className="bi bi-emoji-smile fs-1 text-primary"></i>
                        <h5 className="mt-3">Customer-Centric Service</h5>
                        <p className="text-muted">
                            Your satisfaction is our top priority, and we're here to assist you at every step.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AboutUs;
