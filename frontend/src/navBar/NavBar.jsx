import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getLoginUrl } from '../authentication';

import './css/navBar.css';

export const Navbar = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('accessToken'));
  const navigate = useNavigate();

  useEffect(() => {
    const handleStorageChange = () => {
      setIsAuthenticated(!!localStorage.getItem('accessToken'));
    };

    window.addEventListener('storage', handleStorageChange);
    return () => {
      window.removeEventListener('storage', handleStorageChange);
    };
  }, []);

  const logout = () => {
    localStorage.removeItem('accessToken');
    window.dispatchEvent(new Event('storage'));
    navigate("/");
  }

  return (
    <nav>
      <ul>

        {isAuthenticated && (
          <>
            <li><Link to="/home">Home</Link></li>
            <li><Link to="/claim-history">Claim History</Link></li>
          </>
        )}

        <li className="auth">
          {isAuthenticated ? (
            <button onClick={logout}>Log Out</button>
          ) : (
            <button onClick={() => window.location.href = getLoginUrl()}>Log In</button>
          )}
        </li>
      </ul>
    </nav>
  );
};
