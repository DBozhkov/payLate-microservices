import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import { App } from './App';
import { BrowserRouter } from 'react-router-dom';
import { loadStripe } from '@stripe/stripe-js';
import { Elements } from '@stripe/react-stripe-js'

const stripePromise = loadStripe('pk_test_51QEwx2Krl8g12jNVqbxfmuLl9hOYyI93esV5IvqjHvPox3rTm2j6sKpH0hzZYj1bCnuoJXFrcz3TVwo4FYFsjC04001U8KtJmf');

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <BrowserRouter>
    <Elements stripe={stripePromise}>
      <App />
    </Elements>
  </BrowserRouter>
);