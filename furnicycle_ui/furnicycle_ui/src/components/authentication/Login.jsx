import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../../services/UserService";
import login from "../../assets/login.jpg";

const LoginPage = () => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setError(""); // Clear previous errors

    try {
      const loginDto = {
        userName: formData.username,
        password: formData.password,
      };

      // Call login service
      const user = await UserService.login(loginDto);

      // Check if user is found and password matches
      if (user && user.userName === formData.username && user.password === formData.password) {
        // Navigate based on user role (assuming role is returned from backend)
        if (user.userRole === "CUSTOMER") {
          navigate("/productlist");
        } else {
          setError("Unauthorized role for login");
        }
      } else {
        setError("Invalid username or password");
      }
    } catch (error) {
      console.error("Login error:", error);
      setError("Invalid username or password");
    }
  };

  return (
    <div className="container mt-5">
      <h2 className="text-center mb-4">Login</h2>
      {error && <div className="alert alert-danger text-center">{error}</div>}
      <div className="row align-items-center">
        <div className="col-md-6">
          <img
            src={login}
            alt="Login Illustration"
            className="img-fluid"
          />
        </div>
        <div className="col-md-6">
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="username" className="form-label">
                Username:
              </label>
              <input
                type="text"
                className="form-control"
                id="username"
                name="username"
                value={formData.username}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label htmlFor="password" className="form-label">
                Password:
              </label>
              <input
                type="password"
                className="form-control"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </div>
            <button type="submit" className="btn btn-primary w-100">
              Login
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
