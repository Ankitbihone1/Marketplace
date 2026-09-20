import React, { createContext, useContext, useState, useEffect } from 'react';
import { setAuthUser } from '../api/client';

const MOCK_USERS = [
  { id: 1, name: 'Seller A', type: 'seller' },
  { id: 2, name: 'Seller B', type: 'seller' },
];

const AuthContext = createContext(undefined);

export const AuthProvider = ({ children }) => {
  const [activeUser, setActiveUser] = useState(() => {
    const saved = localStorage.getItem('bajrix_user');
    return saved ? JSON.parse(saved) : null;
  });

  useEffect(() => {
    if (activeUser) {
      localStorage.setItem('bajrix_user', JSON.stringify(activeUser));
      setAuthUser(activeUser.id);
    } else {
      localStorage.removeItem('bajrix_user');
      setAuthUser(null);
    }
  }, [activeUser]);

  const loginAs = (id) => {
    const user = MOCK_USERS.find(u => u.id === id);
    if (user) setActiveUser(user);
  };

  const logout = () => setActiveUser(null);

  return (
    <AuthContext.Provider value={{ activeUser, users: MOCK_USERS, loginAs, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
};
