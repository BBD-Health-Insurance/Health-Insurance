import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

export const Welcome = () => {
  const [error, setError] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const queryParameters = new URLSearchParams(location.search);
    const code = queryParameters.get('code');

    if (code) {
      fetch('http://localhost:8080/auth', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ code }),
      })
        .then(response => {
          if (response.ok) {
            return response.json(); // Await the response.json() promise
          } else {
            throw new Error('Failed to authenticate');
          }
        })
        .then(body => {
          console.log('Body:', body); // Debug: log the response body
          const { access_token} = body;
          if (access_token) {
            localStorage.setItem('accessToken', access_token);
            window.dispatchEvent(new Event('storage'));
            navigate('/home');
          } else {
            throw new Error('Invalid response structure');
          }
        })
        .catch(error => {
          console.error('Authentication error:', error);
          setError(true);
        });
    } else {
      console.log('No code found in URL');
    }
  }, [location]);

  return (
    <div>
      <h1>Welcome! Please log in!</h1>
      {error && <p>Failed to authenticate. Please try again.</p>}
    </div>
  );
};
