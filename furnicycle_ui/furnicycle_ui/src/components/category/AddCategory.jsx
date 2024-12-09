import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import CategoryService from "../../services/CategoryService";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const AddOrEditCategory = () => {
    const { id } = useParams(); // Get category ID from route
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        categoryName: "",
        categoryDescription: "",
    });

    useEffect(() => {
        if (id) {
            // Fetch category details for editing
            const fetchCategory = async () => {
                try {
                    const category = await CategoryService.getCategoryById(id);
                    setFormData({
                        categoryName: category.categoryName,
                        categoryDescription: category.categoryDescription,
                    });
                } catch (error) {
                    console.error("Error fetching category:", error);
                }
            };
            fetchCategory();
        }
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
            let response;
            if (id) {
                // Update category
                response = await CategoryService.editCategory({ categoryId: id, ...formData });
                if (response.status === 200) {
                    toast.success("Category updated successfully!");
                } else {
                    toast.error("Failed to update category. Please try again.");
                }
            } else {
                // Add new category
                response = await CategoryService.addCategory(formData);
                if (response.status === 201) {
                    toast.success("Category added successfully!");
                } else {
                    toast.error("Unexpected response. Please check the server.");
                }
            }
    
            // Navigate after a short delay
            setTimeout(() => {
                navigate("/categories");
            }, 3000);
        } catch (error) {
            console.error("Error saving category:", error);
            toast.error("Failed to save category. Please try again.");
        }
    };
    

    return (
        <div className="container mt-5">
            <ToastContainer />
            <h2>{id ? "Update Category" : "Add Category"}</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="categoryName" className="form-label">
                        Category Name
                    </label>
                    <input
                        type="text"
                        id="categoryName"
                        name="categoryName"
                        value={formData.categoryName}
                        onChange={handleChange}
                        className="form-control"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="categoryDescription" className="form-label">
                        Category Description
                    </label>
                    <textarea
                        id="categoryDescription"
                        name="categoryDescription"
                        value={formData.categoryDescription}
                        onChange={handleChange}
                        className="form-control"
                        rows="3"
                        required
                    ></textarea>
                </div>
                <button type="submit" className="btn btn-primary">
                    {id ? "Update Category" : "Add Category"}
                </button>
            </form>
        </div>
    );
};

export default AddOrEditCategory;
