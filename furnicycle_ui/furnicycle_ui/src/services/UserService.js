import axios from "axios";

// Base URL for the API Gateway
const BASE_URL = "http://localhost:8089/customer";

const UserService = {
  // Fetch all users
  getAllUsers: async () => {
    try {
      const response = await axios.get(`${BASE_URL}/viewall`);
      return response.data;
    } catch (error) {
      console.error("Error fetching users:", error);
      throw error;
    }
  },

  // Fetch a user by ID
  getUserById: async (id) => {
    try {
      const response = await axios.get(`${BASE_URL}/id/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching user with ID ${id}:`, error);
      throw error;
    }
  },

  // Fetch a user by name
  getUserByName: async (customerName) => {
    try {
      const response = await axios.get(
        `${BASE_URL}/search/name/${customerName}`
      );
      return response.data;
    } catch (error) {
      console.error(`Error fetching user with name ${customerName}:`, error);
      throw error;
    }
  },

  // Add a new user
  addUser: async (userData) => {
    try {
      const response = await axios.post(`${BASE_URL}/add`, userData);
      return response.data;
    } catch (error) {
      console.error("Error adding user:", error);
      throw error;
    }
  },

  // Edit an existing user
  editUser: async (userData) => {
    try {
      const response = await axios.put(`${BASE_URL}/edit`, userData);
      return response.data;
    } catch (error) {
      console.error("Error editing user:", error);
      throw error;
    }
  },

  // Delete a user by ID
  deleteUser: async (id) => {
    try {
      const response = await axios.delete(`${BASE_URL}/delete/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error deleting user with ID ${id}:`, error);
      throw error;
    }
  },

  // Validate a user by ID
  validateUser: async (id) => {
    try {
      const response = await axios.get(`${BASE_URL}/validate/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error validating user with ID ${id}:`, error);
      throw error;
    }
  },

  // Login a user
  login: async (loginDto) => {
    try {
      const response = await axios.post(`${BASE_URL}/login`, loginDto);

      // Check if login is successful
      if (response.status === 200) {
        console.log("Login successful:", response.data);
        // Store user info in localStorage or context and proceed
      }
    } catch (error) {
      console.error("Error logging in:", error.response);
      if (error.response && error.response.status === 401) {
        alert("Invalid username or password");
      }
    }
  },

  // Logout a user
  logout: () => {
    localStorage.removeItem("user");
  },

  // Get the current user from localStorage
  getCurrentUser: () => {
    return JSON.parse(localStorage.getItem("user"));
  },
};

export default UserService;
