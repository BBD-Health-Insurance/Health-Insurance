import { cognitoConfig } from './CognitoConfig';

export const getLoginUrl = () => {
  return `https://${cognitoConfig.domain}/login?response_type=code&client_id=${cognitoConfig.clientId}&scope=${cognitoConfig.scope}&redirect_uri=${encodeURIComponent(cognitoConfig.redirectUri)}`;
};

export const getLogoutUrl = () => {
  localStorage.clear();
  return `https://${cognitoConfig.domain}/logout?client_id=${cognitoConfig.clientId}&logout_uri=${encodeURIComponent(cognitoConfig.redirectUri)}/`;
};