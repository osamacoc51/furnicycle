import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom"; // Import useNavigate for navigation
import CategoryService from "../../services/CategoryService";

const AllCategory = () => {
    const [categories, setCategories] = useState([]);
    const navigate = useNavigate(); // Initialize useNavigate

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await CategoryService.getAllCategories();
                console.log("Fetched Categories:", response);
                setCategories(response);
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };

        fetchCategories();
    }, []);

    const handleDelete = async (categoryId) => {
        if (window.confirm("Are you sure you want to delete this category?")) {
            try {
                await CategoryService.deleteCategory(categoryId);
                setCategories((prevCategories) =>
                    prevCategories.filter((category) => category.categoryId !== categoryId)
                );
                alert("Category deleted successfully!");
            } catch (error) {
                console.error(`Error deleting category with ID ${categoryId}:`, error);
                alert("Failed to delete category. Please try again.");
            }
        }
    };

    const handleEdit = (categoryId) => {
        navigate(`/edit-category/${categoryId}`); // Navigate to the edit form with categoryId
    };

    return (
        <div className="container mt-5">
            <h2>Category List</h2>
            <table className="table table-bordered">
                <thead className="table-dark">
                    <tr>
                        <th>Sr. No.</th>
                        <th>Category Name</th>
                        <th>Category Description</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {categories.length > 0 ? (
                        categories.map((category, index) => (
                            <tr key={category.categoryId}>
                                <td>{index + 1}</td>
                                <td>{category.categoryName}</td>
                                <td>{category.categoryDescription}</td>
                                <td>
                                    <button
                                        className="btn btn-warning btn-sm me-2"
                                        onClick={() => handleEdit(category.categoryId)}
                                    >
                                        Edit
                                    </button>
                                    <button
                                        className="btn btn-danger btn-sm"
                                        onClick={() => handleDelete(category.categoryId)}
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="4" className="text-center">
                                No categories found.
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default AllCategory;
