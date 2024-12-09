import React, { useEffect, useState } from "react";
import ProductService from "../../services/ProductService";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from 'react-toastify';

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();


  useEffect(() => {
    // Fetch products from the backend API
    const fetchProducts = async () => {
      try {
        const response = await ProductService.allProduct(); // Replace with your backend endpoint
        // const data = await response.json();
        console.log(response);
        setProducts(response);
      } catch (error) {
        console.error("Error fetching products:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  const handleAddToCart = (product) => {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    
    // Check if the product is already in the cart
    const existingProductIndex = cart.findIndex(item => item.productId === product.productId);

    if (existingProductIndex >= 0) {
      // Update the quantity of the existing product in the cart
      cart[existingProductIndex].quantity += 1;
    } else {
      // Add a new product to the cart
      cart.push({ ...product, quantity: 1 });
    }

    // Update localStorage with the updated cart
    localStorage.setItem('cart', JSON.stringify(cart));

    // Show success toast
    toast.success(`${product.productName} added to cart!`);
  };

  const handleViewDetails = (productId) => {
    navigate(`/details/${productId}`);
  };

  if (loading) {
    return <div>Loading products...</div>;
  }

  return (
    
    <div className="row mt-4 container">
        <ToastContainer />
    {products.map((product) => (
      <div key={product.productId} className="col-md-3 mb-4 ">
        <div className="card" style={{ width: "100%" }}>
          {/* Assuming you have an image URL in the product */}
          <img
            src={product.imageUrl || 'https://img.freepik.com/free-photo/mid-century-modern-living-room-interior-design-with-monstera-tree_53876-129804.jpg'} // Provide a fallback image
            className="card-img-top"
            alt={product.productName}
          />
          <div className="card-body">
            <h5 className="card-title">{product.productName}</h5>
            <p className="card-text">{product.productDescription}</p>
            <div className="mt-2 d-flex justify-content-between">
              <p><strong>Price:</strong> ${product.productPrice.toFixed(2)}</p>
              <p><strong>Stock:</strong> {product.stock}</p>
            </div>
            <div className="d-flex justify-content-between">
              {/* Add to Cart button */}
              <button
                className="btn btn-success"
                onClick={() => handleAddToCart(product)}
              >
                Add to Cart
              </button>
              {/* View Details button */}
              <button
                className="btn btn-primary"
                onClick={() => handleViewDetails(product.productId)}
              >
                View Details
              </button>
            </div>
            {/* Display product price and stock */}

          </div>
        </div>
      </div>
    ))}
  </div>
  
  );
};

export default ProductList;
