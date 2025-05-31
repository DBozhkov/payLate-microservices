export const oktaConfig = {
    clientId: '0oalb9wiz4IsSAhdu5d7',
    issuer: 'https://dev-73369143.okta.com/oauth2/default',
    redirectUri: window.location.origin + '/login/callback',
    scopes: ['openid', 'profile', 'email', 'manage', 'read'],
    pkce: true,
    disableHttpsCheck: true,
    debug: true,
    useClassicEngine: true,
}