import React, { useState } from "react";

const CustomerForm = () => {
  const [formData, setFormData] = useState({
    customerName: "",
    address: "",
    user: {
      userId: "",
      username: "",
      password: "",
      userRole: "user", // Default role
    },
  });

  const [errors, setErrors] = useState({});

  // Handle form input changes
  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name.startsWith("user.")) {
      // Handle nested user fields
      const field = name.split(".")[1];
      setFormData((prevData) => ({
        ...prevData,
        user: {
          ...prevData.user,
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
    if (!formData.customerName.trim()) newErrors.customerName = "Customer name is required.";
    if (!formData.address.trim()) newErrors.address = "Address is required.";
    if (!formData.user.userId.trim()) newErrors.userId = "User ID is required.";
    if (!formData.user.username.trim()) newErrors.username = "Username is required.";
    if (!formData.user.password.trim()) newErrors.password = "Password is required.";
    return newErrors;
  };

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();
    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
    } else {
      setErrors({});
      console.log("Submitted Data:", formData);

      // Example API call
      fetch("http://localhost:8099/customer/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log("Customer saved successfully:", data);
        })
        .catch((error) => {
          console.error("Error saving customer:", error);
        });
    }
  };

  return (
    <div style={styles.container}>
      <h2>Register Customer</h2>
      <form onSubmit={handleSubmit} style={styles.form}>
        <div style={styles.formGroup}>
          <label>Customer Name:</label>
          <input
            type="text"
            name="customerName"
            value={formData.customerName}
            onChange={handleChange}
            style={styles.input}
          />
          {errors.customerName && <p style={styles.error}>{errors.customerName}</p>}
        </div>
        <div style={styles.formGroup}>
          <label>Address:</label>
          <input
            type="text"
            name="address"
            value={formData.address}
            onChange={handleChange}
            style={styles.input}
          />
          {errors.address && <p style={styles.error}>{errors.address}</p>}
        </div>
        <div style={styles.formGroup}>
          <h3>User Information</h3>
          <label>User ID:</label>
          <input
            type="text"
            name="user.userId"
            value={formData.user.userId}
            onChange={handleChange}
            style={styles.input}
          />
          {errors.userId && <p style={styles.error}>{errors.userId}</p>}

          <label>Username:</label>
          <input
            type="text"
            name="user.username"
            value={formData.user.username}
            onChange={handleChange}
            style={styles.input}
          />
          {errors.username && <p style={styles.error}>{errors.username}</p>}

          <label>Password:</label>
          <input
            type="password"
            name="user.password"
            value={formData.user.password}
            onChange={handleChange}
            style={styles.input}
          />
          {errors.password && <p style={styles.error}>{errors.password}</p>}

          <label>Role:</label>
          <input
            type="text"
            name="user.userRole"
            value={formData.user.userRole}
            onChange={handleChange}
            style={styles.input}
            disabled
          />
        </div>
        <button type="submit" style={styles.button}>
          Submit
        </button>
      </form>
    </div>
  );
};

const styles = {
  container: {
    maxWidth: "600px",
    margin: "50px auto",
    padding: "20px",
    border: "1px solid #ccc",
    borderRadius: "8px",
    textAlign: "center",
  },
  form: {
    display: "flex",
    flexDirection: "column",
  },
  formGroup: {
    marginBottom: "15px",
    textAlign: "left",
  },
  input: {
    width: "100%",
    padding: "8px",
    margin: "5px 0",
    borderRadius: "4px",
    border: "1px solid #ccc",
  },
  button: {
    padding: "10px 15px",
    borderRadius: "4px",
    backgroundColor: "#007BFF",
    color: "#fff",
    border: "none",
    cursor: "pointer",
  },
  error: {
    color: "red",
    fontSize: "12px",
  },
};

export default CustomerForm;
