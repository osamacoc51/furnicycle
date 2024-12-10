import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../../services/UserService";
import signup from "../../assets/signup.jpg";

const CustomerForm = () => {
  const [formData, setFormData] = useState({
    customerName: "",
    address: "",
    userDTO: {
      username: "",
      password: "",
      userRole: "CUSTOMER", // Default role
    },
  });

  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");
  const navigate = useNavigate();

  // Handle form input changes
  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name.startsWith("userDTO.")) {
      // Handle nested userDTO fields
      const field = name.split(".")[1];
      setFormData((prevData) => ({
        ...prevData,
        userDTO: {
          ...prevData.userDTO,
          [field]: value,
        },
      }));
    } else {
      // Handle top-level fields
      setFormData((prevData) => ({
        ...prevData,
        [name]: value,
      }));
    }
  };

  // Validate the form
  const validate = () => {
    const newErrors = {};
    if (!formData.customerName.trim())
      newErrors.customerName = "Customer name is required.";
    if (!formData.address.trim())
      newErrors.address = "Address is required.";
    if (!formData.userDTO.username.trim())
      newErrors.username = "Username is required.";
    if (!formData.userDTO.password.trim())
      newErrors.password = "Password is required.";
    return newErrors;
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
    } else {
      setErrors({});
      try {
        const response = await UserService.addUser(formData);
        console.log("Customer saved successfully:", response);
        setSuccessMessage("Customer registered successfully!");

        // Redirect to login page
        setTimeout(() => {
          navigate("/login");
        }, 1000);
      } catch (error) {
        console.error("Error saving customer:", error);
        setSuccessMessage("Failed to register customer. Please try again.");
      }
    }
  };

  return (
    <>
      <div className="container mt-5">
        <h2 className="text-center mb-4">Register Customer</h2>
        {successMessage && (
          <div className="alert alert-success text-center">{successMessage}</div>
        )}
        <div className="row">
          {/* Image Section */}
          <div className="col-md-5 d-flex justify-content-center align-items-center">
            <img
              src={signup}
              alt="Customer Registration"
              className="img-fluid rounded h-100"
            />
          </div>

          {/* Form Section */}
          <div className="col-md-6">
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label htmlFor="customerName" className="form-label">
                  Customer Name:
                </label>
                <input
                  type="text"
                  className={`form-control ${errors.customerName ? "is-invalid" : ""
                    }`}
                  id="customerName"
                  name="customerName"
                  value={formData.customerName}
                  onChange={handleChange}
                />
                {errors.customerName && (
                  <div className="invalid-feedback">{errors.customerName}</div>
                )}
              </div>
              <div className="mb-3">
                <label htmlFor="address" className="form-label">
                  Address:
                </label>
                <input
                  type="text"
                  className={`form-control ${errors.address ? "is-invalid" : ""}`}
                  id="address"
                  name="address"
                  value={formData.address}
                  onChange={handleChange}
                />
                {errors.address && (
                  <div className="invalid-feedback">{errors.address}</div>
                )}
              </div>
              <div>
                <h3>User Information</h3>
                <div className="mb-3">
                  <label htmlFor="username" className="form-label">
                    Username:
                  </label>
                  <input
                    type="text"
                    className={`form-control ${errors.username ? "is-invalid" : ""
                      }`}
                    id="username"
                    name="userDTO.username"
                    value={formData.userDTO.username}
                    onChange={handleChange}
                  />
                  {errors.username && (
                    <div className="invalid-feedback">{errors.username}</div>
                  )}
                </div>
                <div className="mb-3">
                  <label htmlFor="password" className="form-label">
                    Password:
                  </label>
                  <input
                    type="password"
                    className={`form-control ${errors.password ? "is-invalid" : ""
                      }`}
                    id="password"
                    name="userDTO.password"
                    value={formData.userDTO.password}
                    onChange={handleChange}
                  />
                  {errors.password && (
                    <div className="invalid-feedback">{errors.password}</div>
                  )}
                </div>
                <div className="mb-3">
                  <label htmlFor="userRole" className="form-label">
                    Role:
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="userRole"
                    name="userDTO.userRole"
                    value={formData.userDTO.userRole}
                    onChange={handleChange}
                    disabled
                  />
                </div>
              </div>
              <button type="submit" className="btn btn-primary w-100">
                Submit
              </button>
            </form>
          </div>
        </div>
      </div>
    </>
  );
};

export default CustomerForm;
