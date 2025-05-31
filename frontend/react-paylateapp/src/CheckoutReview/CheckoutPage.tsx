import { useOktaAuth } from "@okta/okta-react";
import { useEffect, useState } from "react";
import { Spinner } from "../Utils/Spinner";
import { Link, useNavigate } from "react-router-dom";
import ProductModel from "../models/ProductModel";

export const CheckoutPage = () => {
    const { authState } = useOktaAuth();
    const navigate = useNavigate();
    const [cartItems, setCartItems] = useState<ProductModel[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [httpError, setHttpError] = useState<string | null>(null);
    const [voucher, setVoucher] = useState('');

    useEffect(() => {
        const fetchCartItems = async () => {
            if (authState && authState.isAuthenticated) {
                try {
                    const url = `${process.env.REACT_APP_API}/cart`;
                    const requestOptions = {
                        method: 'GET',
                        headers: {
                            Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                            'Content-Type': 'application/json'
                        }
                    };
                    const response = await fetch(url, requestOptions);
                    if (!response.ok) {
                        throw new Error('Something went wrong!');
                    }
                    const responseData = await response.json();
                    const products = responseData.map((item: any) => new ProductModel(
                        item.id,
                        item.price,
                        item.productName,
                        item.authorName,
                        item.description,
                        item.quantity ?? 1,
                        item.category,
                        item.imgUrl,
                        item.authorUrl,
                        item.rating
                    ));
                    setCartItems(products);
                    setIsLoading(false);
                } catch (error: any) {
                    setIsLoading(false);
                    setHttpError(error.message);
                }
            }
        };

        fetchCartItems();
    }, [authState]);

    const handleQuantityChange = (productId: number, newQuantity: number) => {
        setCartItems(cartItems.map(item =>
            item.id === productId ? { ...item, quantity: Math.max(newQuantity, 1) } : item
        ));
    };

    const handleRemoveItem = async (productId: number) => {
        if (authState && authState.isAuthenticated) {
            try {
                const url = `${process.env.REACT_APP_API}/cart/remove?productId=${productId}`;
                const requestOptions = {
                    method: 'DELETE',
                    headers: {
                        Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                        'Content-Type': 'application/json'
                    }
                };
                const response = await fetch(url, requestOptions);
                if (!response.ok) {
                    throw new Error('Something went wrong!');
                }
                setCartItems(cartItems.filter(item => item.id !== productId));
            } catch (error: any) {
                setHttpError(error.message);
            }
        }
    };

    const handlePayLate = async () => {
        if (authState && authState.isAuthenticated) {
            try {
                const url = `${process.env.REACT_APP_API}/admin/pay-late?userEmail=${authState.accessToken?.claims.sub}`;
                const requestOptions = {
                    method: 'POST',
                    headers: {
                        Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                        'Content-Type': 'application/json'
                    }
                };
                const response = await fetch(url, requestOptions);
                if (!response.ok) {
                    throw new Error('Failed to save the order');
                }
                alert('Order has been saved for later payment');
                navigate('/managependingorders');
            } catch (error: any) {
                setHttpError(error.message);
            }
        }
    };

    if (isLoading) {
        return <Spinner />;
    }

    if (httpError) {
        return (
            <div className="container m-5">
                <p>Something went wrong: {httpError}</p>
            </div>
        );
    }

    return (
        <div className="container">
            <h2>Checkout</h2>
            <div className="row">
                {cartItems.map((item) => (
                    <div key={item.id} className="cart-row item row align-items-center mb-3">
                        <div className="col-md-3">
                            <img src={item.imgUrl} alt={item.productName} className="img-fluid" />
                        </div>
                        <div className="col-md-3">
                            <h4>{item.productName}</h4>
                            <p>{item.description}</p>
                            <input type="text" placeholder="Voucher Code" value={voucher} onChange={(e) => setVoucher(e.target.value)} className="form-control" />
                        </div>
                        <div className="col-md-2">
                            <input
                                type="number"
                                value={item.quantity}
                                className="form-control"
                                min="1"
                                max={item.quantity}
                                onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value))}
                                style={{ borderColor: 'initial' }}
                            />
                        </div>
                        <div className="col-md-2">
                            <p>${item.price}</p>
                        </div>
                        <div className="col-md-2">
                            <button className="btn btn-danger" onClick={() => handleRemoveItem(item.id)}>Remove</button>
                        </div>
                    </div>
                ))}
            </div>
            <div className="text-center mt-4">
                <Link to="/payment" className="btn btn-lg btn-warning" style={{ width: '150px', marginRight: '10px' }}>
                    Pay Now
                </Link>
                <button className="btn btn-lg btn-warning" style={{ width: '150px', marginLeft: '10px' }} onClick={handlePayLate}>
                    Pay Late
                </button>
            </div>
        </div>
    );
};