import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const CartPage = () => {
  const [cartItems, setCartItems] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const storedCart = JSON.parse(localStorage.getItem('cart')) || [];
    console.log(storedCart);
    setCartItems(storedCart);
  }, []);

  const handleRemoveItem = (productId) => {
    const updatedCart = cartItems.filter(item => item.productId !== productId);
    setCartItems(updatedCart);
    localStorage.setItem('cart', JSON.stringify(updatedCart)); // Update localStorage
  };

  // Calculate Total Price, GST, CGST, etc.
  const calculateTotal = () => {
    let totalPrice = 0;
    cartItems.forEach(item => {
      totalPrice += item.productPrice * item.quantity;
    });

    // Assuming 18% GST (CGST and SGST)
    const gstRate = 0.18;
    const cgstRate = gstRate / 2; // 9% CGST
    const sgstRate = gstRate / 2; // 9% SGST

    const gstAmount = totalPrice * gstRate;
    const cgstAmount = totalPrice * cgstRate;
    const sgstAmount = totalPrice * sgstRate;

    const finalTotal = totalPrice + gstAmount;

    return {
      totalPrice,
      gstAmount,
      cgstAmount,
      sgstAmount,
      finalTotal,
    };
  };

  const handleCheckout = () => {
    // Handle checkout (you can add payment functionality here)
    alert('Proceeding to checkout...');
  };

  const { totalPrice, gstAmount, cgstAmount, sgstAmount, finalTotal } = calculateTotal();

  return (
    <div className="container mt-4">
      <h3>Shopping Cart</h3>
      {cartItems.length === 0 ? (
        <p>Your cart is empty</p>
      ) : (
        <div className="row">
          {cartItems.map((item) => (
            <div key={item.productId} className="col-md-3 mb-4">
              <div className="card">
                <img
                  src={item.imageUrl || 'https://img.freepik.com/free-photo/mid-century-modern-living-room-interior-design-with-monstera-tree_53876-129804.jpg'} // Provide a fallback image
                  alt={item.productName}
                  className="card-img-top"
                />
                <div className="card-body">
                  <h5 className="card-title">{item.productName}</h5>
                  <p className="card-text">{item.productDescription}</p>
                  <p>
                    <strong>Price:</strong> ${item.productPrice}
                  </p>
                  <p>
                    <strong>Quantity:</strong> {item.quantity}
                  </p>
                  <button
                    className="btn btn-danger"
                    onClick={() => handleRemoveItem(item.productId)}
                  >
                    Remove
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {cartItems.length > 0 && (
        <div className="mt-4">
          <div className="d-flex justify-content-between">
            <div>
              <h5>Order Summary</h5>
              <p><strong>Total Price:</strong> ${totalPrice.toFixed(2)}</p>
              <p><strong>GST (18%):</strong> ${gstAmount.toFixed(2)}</p>
              <p><strong>CGST (9%):</strong> ${cgstAmount.toFixed(2)}</p>
              <p><strong>SGST (9%):</strong> ${sgstAmount.toFixed(2)}</p>
              <hr />
              <h4><strong>Total (Incl. GST):</strong> ${finalTotal.toFixed(2)}</h4>
            </div>
            <div className="align-self-end">
              <button className="btn btn-primary" onClick={handleCheckout}>
                Checkout
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default CartPage;
