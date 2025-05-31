import './App.css';
import { Navigate, Route, Routes, useNavigate } from 'react-router-dom';
import { Footer } from './NavbarFooter/Footer';
import { HomePage } from './HomePage/HomePage';
import { Navbar } from './NavbarFooter/Navbar';
import OktaAuth, { toRelativeUrl } from '@okta/okta-auth-js';
import { LoginCallback, Security } from '@okta/okta-react';
import { oktaConfig } from './lib/oktaConfig';
import LoginWidget from './Auth/LoginWidget';
import { AboutUs } from './Info/AboutUs';
import { Partners } from './Info/Partners';
import { ProductList } from './Products/components/ProductsList';
import { ProductDetails } from './Products/components/ProductDetails';
import { Cart } from './Cart/Cart';
import { PaymentPage } from './PaymentPage/PaymentPage';
import { CheckoutPage } from './CheckoutReview/CheckoutPage';
import { PreviousOrders } from './Orders/PreviousOrders';
import { ManageProductsPage } from './layouts/ManageProducts/ManageProductsPage';
import ProfilePage from './Profile/components/ProfilePage';

const oktaAuth = new OktaAuth(oktaConfig);

export const App = () => {

  const navigate = useNavigate();

  const customAuthHandler = () => {
    navigate('/login');
  };

  const restoreOriginalUri = async (_oktaAuth: any, originalUri: any) => {
    const relativeUrl = toRelativeUrl(originalUri || '/', window.location.origin);

    navigate(relativeUrl, { replace: true });
  };

  return (
    <div className='d-flex flex-column min-vh-100'>
      <Security oktaAuth={oktaAuth} restoreOriginalUri={restoreOriginalUri} onAuthRequired={customAuthHandler}>
        <Navbar />
        <div className='flex-grow-1'>
          <Routes>
            <Route path='/' element={<Navigate to='/home' />} />
            <Route path='/home' element={<HomePage />} />
            <Route path='/about' element={<AboutUs />} />
            <Route path='/partners' element={<Partners />} />
            <Route path='/products/:partner' element={<ProductList />} />
            <Route path='/products/:partner/:productId' element={<ProductDetails />} />
            <Route path='/products/:productId' element={<ProductDetails />} />
            <Route path='/products' element={<ProductList />} />
            <Route path='/cart' element={<Cart />} />
            <Route path='/checkout' element={<CheckoutPage />} />
            <Route path='/login' element={<LoginWidget config={oktaConfig} />} />
            <Route path='/login/callback' element={<LoginCallback />} />
            <Route path='/payment' element={<PaymentPage />} />
            <Route path='/orderHistory' element={<PreviousOrders />} />
            <Route path='/profile' element={<ProfilePage />} />
            <Route path='/admin' element={<ManageProductsPage />} />
          </Routes>
        </div>
        <Footer />
      </Security>
    </div>
  );
}

