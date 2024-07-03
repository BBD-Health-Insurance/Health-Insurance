export const cognitoConfig = {
  domain: import.meta.env.VITE_APP_DOMAIN,
  clientId: import.meta.env.VITE_APP_CLIENT_ID,
  redirectUri: import.meta.env.VITE_APP_REDIRECT_URI,
  region: import.meta.env.VITE_APP_REGION,
  scope: import.meta.env.VITE_APP_SCOPE,
};
