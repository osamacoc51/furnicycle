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

function App() {
  return (
    <div className="App">
      <Router>
        <Header />
        <Routes>
          <Route path="/" element={<Homepage />} />
          <Route path="/categories" element={<AllCategory />} />
          <Route path="/add-category" element={<AddOrEditCategory />} />
          <Route path="/edit-category/:id" element={<AddOrEditCategory />} />
          <Route path="/add-product" element={<AddProduct />} />
          <Route path="/products" element={<AllProduct />} />
          <Route path="/edit-product/:id" element={<AddProduct />} />
        </Routes>
        {/* <Footer /> */}
      </Router>
    </div>
  );
}

export default App;
