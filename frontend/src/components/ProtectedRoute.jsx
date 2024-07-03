import React from 'react';
import { Route } from 'react-router-dom';
import { getLoginUrl } from '../authentication';

export const ProtectedRoute = ({ component: Component, ...rest }) => {
  const isAuthenticated = !!localStorage.getItem('accessToken');

  return (
    <Route
      {...rest}
      element={isAuthenticated ? <Component /> : (() => { window.location.href = getLoginUrl(); return null; })()}
    />
  );
};
