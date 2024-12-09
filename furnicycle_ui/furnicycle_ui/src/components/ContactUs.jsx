import React from "react";

const ContactUs = () => {
    return (
        <div className="container mt-5">
            <h2 className="text-center mb-4">Contact Us</h2>
            <div className="row">

                <div className="col-md-6">
                    <h4>Send Us a Message</h4>
                    <form>
                        <div className="mb-3">
                            <label htmlFor="name" className="form-label">
                                Your Name
                            </label>
                            <input
                                type="text"
                                className="form-control"
                                id="name"
                                placeholder="Enter your name"
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="email" className="form-label">
                                Email Address
                            </label>
                            <input
                                type="email"
                                className="form-control"
                                id="email"
                                placeholder="Enter your email"
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="message" className="form-label">
                                Your Message
                            </label>
                            <textarea
                                className="form-control"
                                id="message"
                                rows="4"
                                placeholder="Type your message"
                                required
                            ></textarea>
                        </div>
                        <button type="submit" className="btn btn-primary">
                            Send Message
                        </button>
                    </form>
                </div>


                <div className="col-md-6">
                    <h4>Get in Touch</h4>
                    <p>
                        Whether you have questions about our products, need support, or just want to say hello, we’d love to hear from you!
                    </p>
                    <ul className="list-unstyled">
                        <li>
                            <strong>Address:</strong> 123 Furniture Street, Cityville, 456789
                        </li>
                        <li>
                            <strong>Phone:</strong> +1 (234) 567-890
                        </li>
                        <li>
                            <strong>Email:</strong> contact@furnicycle.com
                        </li>
                    </ul>
                    <h5 className="mt-4">Follow Us</h5>
                    <div>
                        <a href="#" className="me-3 text-decoration-none">
                            <i className="bi bi-facebook"></i> Facebook
                        </a>
                        <a href="#" className="me-3 text-decoration-none">
                            <i className="bi bi-twitter"></i> Twitter
                        </a>
                        <a href="#" className="text-decoration-none">
                            <i className="bi bi-instagram"></i> Instagram
                        </a>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ContactUs;
