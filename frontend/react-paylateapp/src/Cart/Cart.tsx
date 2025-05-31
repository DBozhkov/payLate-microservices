import { useOktaAuth } from "@okta/okta-react";
import { useEffect, useState } from "react";
import { Spinner } from "../Utils/Spinner";
import ProductModel from "../models/ProductModel";
import { Link } from "react-router-dom";

export const Cart = () => {
    const { authState } = useOktaAuth();
    const [cartItems, setCartItems] = useState<ProductModel[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchCartItems = async () => {
            if (authState && authState.isAuthenticated) {
                try {
                    const url = `${process.env.REACT_APP_API}/cart`;
                    console.log(`fetching url ${url}`);
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
                        item.quantity,
                        item.category,
                        item.imgUrl,
                        item.authorUrl,
                        item.rating
                    ));
                    setCartItems(products);
                    setIsLoading(false);
                } catch (error) {
                    setIsLoading(false);
                    console.error("Error fetching cart items:", error);
                }
            }
        };

        fetchCartItems();
    }, [authState]);

    if (isLoading) {
        return <Spinner />;
    }

    return (
        <div className="container mt-5">
            <h2>Your Cart</h2>
            <div className="row">
                {cartItems.length === 0 ? (
                    <p>Your cart is empty.</p>
                ) : (
                    cartItems.map((item, index) => {
                        if (!item) {
                            console.warn(`Cart item at index ${index} is null or invalid.`);
                            return null;
                        }
                    
                        return (
                            <div key={`${item.id}-${index}`} className="col-sm-6 col-md-4 col-lg-3 mb-3">
                                <div className="card">
                                    {item.imgUrl ? (
                                        <img
                                            src={item.imgUrl}
                                            className="card-img-top"
                                            alt={item.productName}
                                            height="233"
                                        />
                                    ) : (
                                        <div className="card-img-top placeholder-img">IMG</div>
                                    )}
                                    <div className="card-body">
                                        <h5 className="card-title">{item.productName || "No name available"}</h5>
                                        <p className="card-text">{item.authorName || "No author available"}</p>
                                        <p className="card-text">${item.price ? item.price.toFixed(2) : "N/A"}</p>
                                    </div>
                                </div>
                            </div>
                        );
                    })
                )}
            </div>
            {cartItems.length > 0 && (
                <div className="text-center mt-4">
                    <Link to="/checkout" className="btn btn-md btn-warning">
                        Checkout
                    </Link>
                </div>
            )}
        </div>
    );
};