import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { apiClient } from '../api/client';

export const ProductDetails = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [listings, setListings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!id) return;
    
    Promise.all([
      apiClient.get(`/products/${id}`).catch(err => console.error(err)),
      apiClient.get(`/products/${id}/listings`).catch(err => console.error(err))
    ])
    .then(([productRes, listingsRes]) => {
      if (productRes?.data) setProduct(productRes.data);
      if (listingsRes?.data) setListings(listingsRes.data);
    })
    .finally(() => setLoading(false));
  }, [id]);

  if (loading) return <div className="p-6 text-gray-500">Loading product details...</div>;
  if (!product) return <div className="p-6 text-red-600">Product not found.</div>;

  return (
    <div className="p-6 max-w-5xl mx-auto">
      <Link to="/" className="text-blue-600 hover:underline mb-4 inline-block">&larr; Back to Catalog</Link>
      
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-8">
        <h1 className="text-3xl font-bold mb-3 text-gray-900">{product.name}</h1>
        <p className="text-gray-700 text-lg">{product.description}</p>
      </div>
      
      <h2 className="text-2xl font-semibold mb-4 text-gray-800">Active Seller Listings</h2>
      {listings.length === 0 ? (
        <p className="text-gray-500">No active listings for this product currently.</p>
      ) : (
        <div className="overflow-x-auto bg-white rounded-lg shadow-sm border border-gray-200">
          <table className="min-w-full text-left">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                <th className="py-3 px-4 text-gray-600 font-medium">Seller</th>
                <th className="py-3 px-4 text-gray-600 font-medium">Price</th>
                <th className="py-3 px-4 text-gray-600 font-medium">Stock</th>
                <th className="py-3 px-4 text-gray-600 font-medium">MOQ</th>
                <th className="py-3 px-4 text-gray-600 font-medium">Action</th>
              </tr>
            </thead>
            <tbody>
              {listings.map(l => (
                <tr key={l.id} className="border-b border-gray-100 hover:bg-gray-50">
                  <td className="py-3 px-4 font-medium text-gray-900">{l.seller?.name || `Seller ${l.sellerId}`}</td>
                  <td className="py-3 px-4 text-gray-700">${Number(l.price).toFixed(2)}</td>
                  <td className="py-3 px-4 text-gray-700">{l.stock} units</td>
                  <td className="py-3 px-4 text-gray-700">{l.minOrderQuantity} units</td>
                  <td className="py-3 px-4">
                    <button className="bg-blue-600 text-white px-3 py-1.5 rounded hover:bg-blue-700 transition text-sm">
                      Buy Now
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};
