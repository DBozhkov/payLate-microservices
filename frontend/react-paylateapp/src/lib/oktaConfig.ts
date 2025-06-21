export const oktaConfig = {
    clientId: '0oasf4sm2j3lfZhJc697',
    issuer: 'https://trial-2413125.okta.com/oauth2/default',
    redirectUri: window.location.origin + '/login/callback',
    scopes: ['openid', 'profile', 'email'],
    pkce: true,
    disableHttpsCheck: true,
    debug: true,
    // useClassicEngine: true,
}