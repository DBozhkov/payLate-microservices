import { Navigate } from 'react-router-dom';
import { useOktaAuth } from '@okta/okta-react';
import OktaSignInWidget from './OktaSignInWidget';
import { Spinner } from '../Utils/Spinner';

const LoginWidget = () => {
    const { oktaAuth, authState } = useOktaAuth();

    const onSuccess = (tokens) => {
        console.log('Access Token: ', tokens.accessToken?.accessToken);
        console.log('ID Token: ', tokens.idToken?.idToken);
        oktaAuth.handleLoginRedirect(tokens);
    };

    const onError = (err) => {
        console.error('Login error:', err);
    };

    if (!authState) return <Spinner />;

    return authState.isAuthenticated
        ? <Navigate to='/' />
        : <OktaSignInWidget onSuccess={onSuccess} onError={onError} />;
};

export default LoginWidget;