import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { apiClient } from '../api/client';

export const BuyerCatalog = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    apiClient.get('/products')
      .then(res => setProducts(res.data))
      .catch(err => console.error('Error fetching products:', err))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6 text-gray-800">Product Catalog</h1>
      
      {loading ? (
        <p className="text-gray-500">Loading products...</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {products.map(p => (
            <div key={p.id} className="bg-white border border-gray-200 rounded-lg p-5 shadow-sm hover:shadow-md transition">
              <h2 className="text-lg font-semibold text-gray-900 mb-2">{p.name}</h2>
              <p className="text-gray-600 mb-4 text-sm line-clamp-3">{p.description}</p>
              <Link to={`/products/${p.id}`} className="inline-block mt-2 text-blue-600 hover:text-blue-800 font-medium">
                View Details & Listings &rarr;
              </Link>
            </div>
          ))}
          {products.length === 0 && <p className="text-gray-500 col-span-full">No products found.</p>}
        </div>
      )}
    </div>
  );
};
