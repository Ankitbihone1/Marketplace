import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { apiClient } from '../api/client';
import { useAuth } from '../context/AuthContext';

export const SellerDashboard = () => {
  const { activeUser } = useAuth();
  const [listings, setListings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (activeUser?.id && activeUser.type === 'seller') {
      setLoading(true);

      apiClient
        .get('/sellers/listings', {
          headers: {
            'X-User-Id': activeUser.id,
          },
        })
        .then((res) => {
          setListings(res.data);
        })
        .catch((err) => {
          console.error('Failed to load seller listings:', err);
          setListings([]);
        })
        .finally(() => {
          setLoading(false);
        });
    } else {
      setLoading(false);
    }
  }, [activeUser]);

  if (activeUser?.type !== 'seller') {
    return (
      <div className="p-6 text-red-600">
        Access Denied. Please login as a seller to view this page.
      </div>
    );
  }

  return (
    <div className="p-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-800 mb-4 sm:mb-0">
          Seller Dashboard
        </h1>

        <Link
          to="/seller/listing/new"
          className="bg-blue-600 text-white px-4 py-2 rounded shadow hover:bg-blue-700 transition font-medium"
        >
          + Create New Listing
        </Link>
      </div>

      {loading ? (
        <p className="text-gray-500">Loading your listings...</p>
      ) : (
        <div className="overflow-x-auto bg-white rounded-lg shadow-sm border border-gray-200">
          <table className="min-w-full text-left">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                <th className="py-3 px-4 text-gray-600 font-medium">
                  Product ID
                </th>
                <th className="py-3 px-4 text-gray-600 font-medium">
                  Price
                </th>
                <th className="py-3 px-4 text-gray-600 font-medium">
                  Stock
                </th>
                <th className="py-3 px-4 text-gray-600 font-medium">
                  MOQ
                </th>
                <th className="py-3 px-4 text-gray-600 font-medium">
                  Status
                </th>
                <th className="py-3 px-4 text-gray-600 font-medium">
                  Actions
                </th>
              </tr>
            </thead>

            <tbody>
              {listings.length === 0 && (
                <tr>
                  <td
                    colSpan={6}
                    className="py-8 text-center text-gray-500"
                  >
                    You don't have any listings yet.
                  </td>
                </tr>
              )}

              {listings.map((listing) => (
                <tr
                  key={listing.id}
                  className="border-b border-gray-100 hover:bg-gray-50"
                >
                  <td className="py-3 px-4 font-mono text-sm text-gray-700">
                    {listing.productId}
                  </td>

                  <td className="py-3 px-4 text-gray-900 font-medium">
                    ₹{Number(listing.price).toFixed(2)}
                  </td>

                  <td className="py-3 px-4 text-gray-700">
                    {listing.stock}
                  </td>

                  <td className="py-3 px-4 text-gray-700">
                    {listing.minOrderQuantity}
                  </td>

                  <td className="py-3 px-4">
                    <span
                      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                        listing.isActive
                          ? 'bg-green-100 text-green-800'
                          : 'bg-red-100 text-red-800'
                      }`}
                    >
                      {listing.isActive ? 'Active' : 'Inactive'}
                    </span>
                  </td>

                  <td className="py-3 px-4">
                    <Link
                      to={`/seller/listing/${listing.id}/edit`}
                      className="text-blue-600 hover:text-blue-800 font-medium text-sm"
                    >
                      Edit
                    </Link>
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