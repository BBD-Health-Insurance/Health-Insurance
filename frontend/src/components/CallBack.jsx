import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

export const Callback = () => {

  const navigate = useNavigate();

  useEffect(() => {

    const fetchToken = async () => {
      const params = new URLSearchParams(window.location.search);
      const code = params.get('code');

      const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          code: code
        })
      };

      try {
        const response = await fetch(`${import.meta.env.REACT_APP_API_URL}/auth`, requestOptions);
        const data = await response.json();
        console.log('Tokens:', data);

        localStorage.setItem('accessToken', access_token);
        localStorage.setItem('idToken', id_token);
        localStorage.setItem('refreshToken', refresh_token);

        navigate('/home');
      } catch (error) {
        console.error('Token exchange error:', error);
        navigate('/welcome');
      }
    };

    fetchToken();
  }, []);

  return (
    <div>
      <h2>Logging in...</h2>
    </div>
  );
};