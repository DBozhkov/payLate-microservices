import { useEffect, useRef } from 'react';
import OktaSignIn from '@okta/okta-signin-widget';
import '@okta/okta-signin-widget/css/okta-sign-in.min.css';
import { oktaConfig } from '../lib/oktaConfig';

const OktaSignInWidget = ({ onSuccess, onError }) => {
    const widgetRef = useRef(null);

    useEffect(() => {
        const widget = new OktaSignIn({
            baseUrl: oktaConfig.issuer.replace('/oauth2/default', ''),
            clientId: oktaConfig.clientId,
            redirectUri: oktaConfig.redirectUri,
            authParams: {
                pkce: true,
                issuer: oktaConfig.issuer,
                display: 'page',
                scopes: oktaConfig.scopes,
            },
        });

        widget.showSignInToGetTokens({
            el: widgetRef.current,
        }).then(onSuccess).catch(onError);

        return () => widget.remove();
    }, []);

    return (
        <div className='container mt-5 mb-5'>
            <div ref={widgetRef}></div>
        </div>
    );
};

export default OktaSignInWidget;