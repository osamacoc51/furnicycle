import React, { useEffect, useState } from "react";
import ProductService from "../../services/ProductService";
import { useNavigate } from "react-router-dom";

const AllProduct = () => {
    const [products, setProducts] = useState([]);
    const navigate = useNavigate();

    // Fetch all products on component mount
    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await ProductService.allProduct();
                setProducts(response);
            } catch (error) {
                console.error("Error fetching products:", error);
            }
        };

        fetchProducts();
    }, []);

    // Handle delete action
    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete this product?")) {
            try {
                await ProductService.deleteProduct(id);
                alert("Product deleted successfully!");

                // Update UI after deletion
                setProducts((prevProducts) =>
                    prevProducts.filter((product) => product.productId !== id)
                );
            } catch (error) {
                console.error(`Error deleting product with ID ${id}:`, error);
                alert("Failed to delete product. Please try again.");
            }
        }
    };

    // Handle edit action
    const handleEdit = (id) => {
        navigate(`/edit-product/${id}`);
    };

    return (
        <div className="container mt-5">
            <h2>Product List</h2>
            <table className="table table-bordered">
                <thead className="table-dark">
                    <tr>
                        <th>Sr. No.</th>
                        <th>Product Name</th>
                        <th>Product Description</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {products.length > 0 ? (
                        products.map((product, index) => (
                            <tr key={product.productId}>
                                <td>{index + 1}</td>
                                <td>{product.productName}</td>
                                <td>{product.productDescription}</td>
                                <td>{product.productPrice}</td>
                                <td>{product.stock}</td>                                <td>
                                    <button
                                        className="btn btn-warning btn-sm me-2"
                                        onClick={() => handleEdit(product.productId)}
                                    >
                                        Edit
                                    </button>
                                    <button
                                        className="btn btn-danger btn-sm"
                                        onClick={() => handleDelete(product.productId)}
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="7" className="text-center">
                                No products found.
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default AllProduct;
