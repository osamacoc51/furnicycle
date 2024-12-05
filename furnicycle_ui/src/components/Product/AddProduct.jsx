import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import ProductService from "../../services/ProductService";
import CategoryService from "../../services/CategoryService";

const AddProduct = () => {
    const { id } = useParams(); // Get product ID from route
    const navigate = useNavigate();
    const [categories, setCategories] = useState([]);
    const [formData, setFormData] = useState({
        productName: "",
        productDescription: "",
        productPrice: "",
        stock: "",
        productCategory: "",
    });

    // Fetch categories and product details for editing
    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await CategoryService.getAllCategories();
                setCategories(response);
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };

        const fetchProduct = async () => {
            if (id) {
                try {
                    const product = await ProductService.getProductById(id);
                    setFormData({
                        productName: product.productName,
                        productDescription: product.productDescription,
                        productPrice: product.productPrice,
                        stock: product.stock,
                        productCategory: product.productCategory,
                    });
                } catch (error) {
                    console.error("Error fetching product:", error);
                }
            }
        };

        fetchCategories();
        fetchProduct();
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (id) {
                // Update product
                await ProductService.editProduct({ productId: id, ...formData });
                alert("Product updated successfully!");
            } else {
                // Add new product
                await ProductService.addProduct(formData, formData.productCategory);
                alert("Product added successfully!");
            }
            navigate("/products"); // Navigate back to the product list
        } catch (error) {
            console.error("Error saving product:", error);
            alert("Failed to save product. Please try again.");
        }
    };

    return (
        <div className="container mt-5">
            <h2>{id ? "Update Product" : "Add Product"}</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="productName" className="form-label">
                        Product Name
                    </label>
                    <input
                        type="text"
                        id="productName"
                        name="productName"
                        value={formData.productName}
                        onChange={handleChange}
                        className="form-control"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="productDescription" className="form-label">
                        Product Description
                    </label>
                    <textarea
                        id="productDescription"
                        name="productDescription"
                        value={formData.productDescription}
                        onChange={handleChange}
                        className="form-control"
                        rows="3"
                        required
                    ></textarea>
                </div>
                <div className="mb-3">
                    <label htmlFor="productPrice" className="form-label">
                        Product Price
                    </label>
                    <input
                        type="number"
                        id="productPrice"
                        name="productPrice"
                        value={formData.productPrice}
                        onChange={handleChange}
                        className="form-control"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="stock" className="form-label">
                        Stock
                    </label>
                    <input
                        type="number"
                        id="stock"
                        name="stock"
                        value={formData.stock}
                        onChange={handleChange}
                        className="form-control"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="productCategory" className="form-label">
                        Product Category
                    </label>
                    <select
                        id="productCategory"
                        name="productCategory"
                        value={formData.productCategory}
                        onChange={handleChange}
                        className="form-select"
                        required
                    >
                        <option value="" disabled>
                            Select a category
                        </option>
                        {categories.map((category) => (
                            <option key={category.categoryId} value={category.categoryName}>
                                {category.categoryName}
                            </option>
                        ))}
                    </select>
                </div>
                <button type="submit" className="btn btn-primary">
                    {id ? "Update Product" : "Add Product"}
                </button>
            </form>
        </div>
    );
};

export default AddProduct;
