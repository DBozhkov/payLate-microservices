import { Navigate } from 'react-router-dom';
import { useOktaAuth } from '@okta/okta-react';
import OktaSignInWidget from './OktaSignInWidget';
import { Spinner } from '../Utils/Spinner';

const LoginWidget = ({ config }) => {
    const { oktaAuth, authState } = useOktaAuth();
    const onSuccess = (tokens) => {
        console.log('Access Token: ', tokens.accessToken.accessToken);
        console.log('ID Token: ', tokens.idToken.idToken);
        oktaAuth.handleLoginRedirect(tokens);
    };

    const onError = (err) => {
        console.log('Sign in error: ', err);
    }

    if (!authState) {
        return (
            <Spinner />
        );
    }

    return authState.isAuthenticated ? 
    <Navigate to={{ pathname: '/'}}/>
    :
    <OktaSignInWidget config={config} onSuccess={onSuccess} onError={onError}/>;
};

export default LoginWidget;