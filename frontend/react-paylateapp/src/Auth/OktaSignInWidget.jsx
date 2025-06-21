import { useEffect, useRef } from 'react';
import OktaSignIn from '@okta/okta-signin-widget';
import { oktaConfig } from '../lib/oktaConfig';

const OktaSignInWidget = ({ onSuccess, onError }) => {
    const widgetRef = useRef();

    useEffect(() => {
        const widget = new OktaSignIn(oktaConfig);

        widget.showSignInToGetTokens({
            el: widgetRef.current,
        })
            .then(onSuccess)
            .catch(err => {
                console.error('Error displaying the widget: ', err);
                onError(err);
            });

        return () => widget.remove();
    }, []);

    return (
        <div className='container mt-5 mb-5'>
            <div ref={widgetRef}></div>
        </div>
    );
};

export default OktaSignInWidget;