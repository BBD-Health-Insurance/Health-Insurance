import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import './css/Welcome.css';

export const Welcome = () => {
  const [error, setError] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const queryParameters = new URLSearchParams(location.search);
    const code = queryParameters.get('code');

    if (code) {
      fetch(`${import.meta.env.VITE_APP_API_URL}/auth`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ code }),
      })
        .then(response => {
          if (response.ok) {
            return response.json();
          } else {
            throw new Error('Failed to authenticate');
          }
        })
        .then(body => {
          const { access_token } = body;
          if (access_token) {
            localStorage.setItem('accessToken', access_token);
            window.dispatchEvent(new Event('storage'));
            navigate('/home');
          } else {
            throw new Error('Invalid response structure');
          }
        })
        .catch(error => {
          setError(true);
        });
    }
  }, [location]);

  return (
    <div className="welcome-container">

      <h1 className="welcome-heading">Welcome to the Health Insurance Portal!</h1>
      <p className="welcome-subtext">
        Please log in to access your personalized dashboard and manage your health insurance claims.
      </p>

      {error && <p className="error-message">Failed to authenticate. Please try again.</p>}

      <div className="welcome-info-section">

        <div className="welcome-info-box">
          <h2 className="info-title">Why Choose Us?</h2>
          <p>We offer the BEST health insurance plans tailored to meet your needs!</p>
        </div>

        <div className="welcome-info-box">
          <h2 className="info-title">Need Assistance?</h2>
          <p>Contact our CTO at katlego.kungoane@bbd.co.za and shout out all your needs/complaints!</p>
        </div>
      </div>
    </div>
  );
};
