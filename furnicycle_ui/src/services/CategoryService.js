import axios from "axios";

// Base URL for the API Gateway
const BASE_URL = "http://localhost:8098/category";

const CategoryService = {
  // Fetch all categories
  getAllCategories: async () => {
    try {
      const response = await axios.get(`${BASE_URL}/viewall`);
      return response.data;
    } catch (error) {
      console.error("Error fetching categories:", error);
      throw error;
    }
  },

  // Fetch a category by ID
  getCategoryById: async (id) => {
    try {
      const response = await axios.get(`${BASE_URL}/id/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching category with ID ${id}:`, error);
      throw error;
    }
  },

  // Fetch a category by name
  getCategoryByName: async (categoryName) => {
    try {
      const response = await axios.get(`${BASE_URL}/name/${categoryName}`);
      return response.data;
    } catch (error) {
      console.error(
        `Error fetching category with name ${categoryName}:`,
        error
      );
      throw error;
    }
  },

  // Add a new category
  addCategory: async (categoryData) => {
    try {
      const response = await axios.post(`${BASE_URL}/add`, categoryData);
      return response.data;
    } catch (error) {
      console.error("Error adding category:", error);
      throw error;
    }
  },

  // Edit an existing category
  editCategory: async (categoryData) => {
    try {
      const response = await axios.put(`${BASE_URL}/edit`, categoryData);
      return response.data;
    } catch (error) {
      console.error("Error editing category:", error);
      throw error;
    }
  },

  // Delete a category by ID
  deleteCategory: async (id) => {
    try {
      const response = await axios.delete(`${BASE_URL}/delete/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error deleting category with ID ${id}:`, error);
      throw error;
    }
  },
};

export default CategoryService;
