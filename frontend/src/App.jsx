import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { Header } from './components/Header';
import { BuyerCatalog } from './pages/BuyerCatalog';
import { ProductDetails } from './pages/ProductDetails';
import { SellerDashboard } from './pages/SellerDashboard';
import { ListingForm } from './pages/ListingForm';

const App = () => {
  return (
    <AuthProvider>
      <Router>
        <div className="min-h-screen bg-gray-50 text-gray-900 font-sans flex flex-col">
          <Header />
          <main className="flex-grow w-full max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
            <Routes>
              <Route path="/" element={<BuyerCatalog />} />
              <Route path="/products/:id" element={<ProductDetails />} />
              <Route path="/seller/dashboard" element={<SellerDashboard />} />
              <Route path="/seller/listing/new" element={<ListingForm />} />
              <Route path="/seller/listing/:id/edit" element={<ListingForm />} />
            </Routes>
          </main>
          <footer className="bg-gray-800 text-gray-400 py-6 text-center text-sm">
            &copy; {new Date().getFullYear()} BajriX Marketplace. All rights reserved.
          </footer>
        </div>
      </Router>
    </AuthProvider>
  );
};

export default App;
