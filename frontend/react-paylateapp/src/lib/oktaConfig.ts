export const oktaConfig = {
    clientId: '0oap2yme859gAvIWZ5d7',
    issuer: 'https://dev-73369143.okta.com/oauth2/default',
    redirectUri: window.location.origin + '/login/callback',
    scopes: ['openid', 'profile', 'email'],
    pkce: true,
    disableHttpsCheck: true,
    debug: true,
    useClassicEngine: true,
}