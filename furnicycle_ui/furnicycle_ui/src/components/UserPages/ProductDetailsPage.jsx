import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import ProductService from "../../services/ProductService";

const ProductDetailsPage = () => {
  const { productId } = useParams(); // Get productId from URL params
  const [product, setProduct] = useState(null);
  const navigate = useNavigate();

  // Fetch product data when the component mounts
  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await ProductService.getProductById(productId); // Adjust API URL as needed
        setProduct(response); // Set the product data in the state
      } catch (error) {
        console.error("Error fetching product:", error);
        // Handle error (you can display an error message if necessary)
      }
    };

    fetchProduct();
  }, [productId]); // Re-run the effect if productId changes

  // If product is not available, show an error message
  if (!product) {
    return <h3>Product not found</h3>;
  }

  return (
    <div className="container mt-4">
      <button className="btn btn-secondary mb-3" onClick={() => navigate(-1)}>
        Go Back
      </button>
      <div className="card">
        <div className="card-header">
          <h4>Product Details</h4>
        </div>
        <div className="card-body">
          <h5>{product.productName}</h5>
          <p>{product.productDescription}</p>
          <p>
            <strong>Price:</strong> ${product.productPrice.toFixed(2)}
          </p>
          <p>
            <strong>Stock:</strong> {product.stock}
          </p>
          <hr />
          <h6>Category Details:</h6>
          <p>
            <strong>Name:</strong> {product.categoryDTO.categoryName}
          </p>
          <p>
            <strong>Description:</strong> {product.categoryDTO.categoryDescription}
          </p>
        </div>
      </div>
    </div>
  );
};

export default ProductDetailsPage;
