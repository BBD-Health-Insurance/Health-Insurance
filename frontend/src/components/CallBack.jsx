import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

export const Callback = () => {

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
    <div>
      <h2>Logging in...</h2>
      {error && <p>Authentication failed. Please try again.</p>}
    </div>
  );
};
