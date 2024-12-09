import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { Header } from "./components/Header";
import { Homepage } from "./components/Homepage";
import { Footer } from "./components/Footer";
import AllCategory from "./components/category/AllCategory";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import AddOrEditCategory from "./components/category/AddCategory";
import AddProduct from "./components/Product/AddProduct";
import AllProduct from "./components/Product/AllProduct";
import Register from "./components/authentication/RegisterUser";
import LoginPage from "./components/authentication/Login";
import ProductDetailsPage from "./components/UserPages/ProductDetailsPage";
import ProductList from "./components/UserPages/ProductList";
import CartPage from "./components/Cart/CartPage";
import AboutUs from "./components/AboutUs";
import ContactUs from "./components/ContactUs";

function App() {
  return (
    <div className="App">
      <Router>
        <Header />
        <Routes>
          <Route path="/" element={<Homepage />} />
          <Route path="/categories" element={<AllCategory />} />
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/details/:productId" element={<ProductDetailsPage />} />
          <Route path="/add-category" element={<AddOrEditCategory />} />
          <Route path="/edit-category/:id" element={<AddOrEditCategory />} />
          <Route path="/add-product" element={<AddProduct />} />
          <Route path="/products" element={<AllProduct />} />
          <Route path="/productlist" element={<ProductList />} />
          <Route path="/edit-product/:id" element={<AddProduct />} />
          <Route path="/about-us" element={<AboutUs />} />
          <Route path="/contact-us" element={<ContactUs />} />
          {/* <Route path="/add-to-cart" element={<AddToCartPage />} /> */}
          <Route path="/cart" element={<CartPage />} />
        </Routes>
        {/* <Footer /> */}
      </Router>
    </div>
  );
}

export default App;
