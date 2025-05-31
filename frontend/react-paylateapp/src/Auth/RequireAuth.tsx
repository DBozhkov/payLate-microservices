import React from 'react';
import { useOktaAuth } from '@okta/okta-react';
import { Navigate } from 'react-router-dom';

const RequireAuth = ({ children }: { children: JSX.Element }) => {
  const { authState } = useOktaAuth();

  if (!authState?.isAuthenticated) {
    return <Navigate to='/login' />;
  }

  return children;
};

export default RequireAuth;
