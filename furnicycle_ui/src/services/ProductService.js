import axios from "axios";

const BASE_URL = "http://localhost:8098/product";

const ProductService = {
  addProduct: async (productData, categoryName) => {
    try {
      const response = await axios.post(
        `${BASE_URL}/add/${categoryName}`,
        productData
      );
      return response.data;
    } catch (error) {
      console.error("Error adding product:", error);
      throw error;
    }
  },

  allProduct: async () => {
    try {
      const response = await axios.get(`${BASE_URL}/viewall`);
      return response.data;
    } catch (error) {
      console.error("Error fetching products:", error);
      throw error;
    }
  },

  getProductById: async (id) => {
    try {
      const response = await axios.get(`${BASE_URL}/id/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching product with ID ${id}:`, error);
      throw error;
    }
  },

  deleteProduct: async (id) => {
    try {
      const response = await axios.delete(`${BASE_URL}/delete/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error deleting product with ID ${id}:`, error);
      throw error;
    }
  },

  editProduct: async (productData) => {
    try {
      const response = await axios.put(`${BASE_URL}/edit`, productData);
      return response.data;
    } catch (error) {
      console.error("Error editing product:", error);
      throw error;
    }
  },
};

export default ProductService;
