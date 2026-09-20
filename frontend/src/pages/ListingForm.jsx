import React, { useState, useEffect } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { apiClient } from '../api/client';
import { useAuth } from '../context/AuthContext';

export const ListingForm = () => {
  const { activeUser } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = Boolean(id);

  const [formData, setFormData] = useState({
    productId: '',
    price: 0,
    stock: 0,
    minOrderQuantity: 1,
    isActive: true
  });
  
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isEdit && activeUser?.id) {
      setLoading(true);
      apiClient.get(`/sellers/${activeUser.id}/listings`)
        .then(res => {
          const listing = res.data.find((l) => l.id === id);
          if (listing) {
            setFormData({
              productId: listing.productId,
              price: listing.price,
              stock: listing.stock,
              minOrderQuantity: listing.minOrderQuantity,
              isActive: listing.isActive
            });
          }
        })
        .catch(err => {
          console.error(err);
          setError('Failed to load listing details.');
        })
        .finally(() => setLoading(false));
    }
  }, [id, isEdit, activeUser]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : type === 'number' ? (value === '' ? '' : Number(value)) : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!activeUser?.id) return;
    
    setError('');
    setLoading(true);

    try {
      if (isEdit) {
        await apiClient.put(`/sellers/${activeUser.id}/listings/${id}`, formData);
      } else {
        await apiClient.post(`/sellers/${activeUser.id}/listings`, formData);
      }
      navigate('/seller/dashboard');
    } catch (err) {
      console.error('Failed to save listing', err);
      setError(err.response?.data?.message || 'Failed to save listing. Please try again.');
      setLoading(false);
    }
  };

  if (activeUser?.type !== 'seller') {
    return <div className="p-6 text-red-600">Access Denied.</div>;
  }

  return (
    <div className="p-6 max-w-lg mx-auto">
      <div className="mb-4">
        <Link to="/seller/dashboard" className="text-blue-600 hover:underline text-sm">&larr; Back to Dashboard</Link>
      </div>
      
      <h1 className="text-2xl font-bold mb-6 text-gray-800">{isEdit ? 'Edit Listing' : 'Create New Listing'}</h1>
      
      {error && <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded mb-4">{error}</div>}
      
      <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 space-y-5">
        {!isEdit && (
          <div>
            <label className="block text-gray-700 font-medium mb-1.5 text-sm">Product ID</label>
            <input 
              required 
              name="productId" 
              value={formData.productId} 
              onChange={handleChange} 
              className="w-full border border-gray-300 p-2.5 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition" 
              placeholder="e.g. prod_123"
            />
          </div>
        )}
        
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-gray-700 font-medium mb-1.5 text-sm">Price ($)</label>
            <input 
              required 
              type="number" 
              step="0.01" 
              min="0" 
              name="price" 
              value={formData.price} 
              onChange={handleChange} 
              className="w-full border border-gray-300 p-2.5 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition" 
            />
          </div>
          <div>
            <label className="block text-gray-700 font-medium mb-1.5 text-sm">Stock Quantity</label>
            <input 
              required 
              type="number" 
              min="0" 
              name="stock" 
              value={formData.stock} 
              onChange={handleChange} 
              className="w-full border border-gray-300 p-2.5 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition" 
            />
          </div>
        </div>

        <div>
          <label className="block text-gray-700 font-medium mb-1.5 text-sm">Min Order Quantity (MOQ)</label>
          <input 
            required 
            type="number" 
            min="1" 
            name="minOrderQuantity" 
            value={formData.minOrderQuantity} 
            onChange={handleChange} 
            className="w-full border border-gray-300 p-2.5 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition" 
          />
        </div>
        
        <div className="flex items-center pt-2">
          <input 
            type="checkbox" 
            name="isActive" 
            checked={formData.isActive} 
            onChange={handleChange} 
            className="mr-3 h-4 w-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500" 
            id="isActive" 
          />
          <label htmlFor="isActive" className="text-gray-800 font-medium cursor-pointer">Make this listing active immediately</label>
        </div>
        
        <div className="pt-4">
          <button 
            type="submit" 
            disabled={loading}
            className="w-full bg-blue-600 text-white py-2.5 rounded font-medium shadow hover:bg-blue-700 transition disabled:opacity-70"
          >
            {loading ? 'Saving...' : (isEdit ? 'Update Listing' : 'Create Listing')}
          </button>
        </div>
      </form>
    </div>
  );
};
