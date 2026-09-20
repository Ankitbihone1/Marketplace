import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export const Header = () => {
  const { activeUser, users, loginAs, logout } = useAuth();

  return (
    <header className="bg-blue-900 text-white p-4 shadow-md flex justify-between items-center">
      <div className="flex items-center space-x-6">
        <Link to="/" className="text-xl font-bold tracking-wide">BajriX Marketplace</Link>
        <nav className="hidden md:flex space-x-4">
          <Link to="/" className="hover:text-blue-300 transition">Catalog</Link>
          {activeUser?.type === 'seller' && (
            <Link to="/seller/dashboard" className="hover:text-blue-300 transition">Seller Dashboard</Link>
          )}
        </nav>
      </div>
      <div className="flex items-center space-x-4">
        {activeUser && <span className="font-medium text-sm md:text-base text-gray-200">Hello, {activeUser.name}</span>}
        <select 
          className="text-black p-1.5 rounded text-sm bg-white border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
          value={activeUser?.id || ''}
          onChange={(e) => {
            if (e.target.value) loginAs(e.target.value);
            else logout();
          }}
        >
          <option value="">-- Login As --</option>
          {users.map(u => (
            <option key={u.id} value={u.id}>{u.name} ({u.type})</option>
          ))}
        </select>
      </div>
    </header>
  );
};
