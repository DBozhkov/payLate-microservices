import { useOktaAuth } from "@okta/okta-react";
import { useEffect, useState } from "react";
import { Spinner } from "../../Utils/Spinner";

export const PreviousOrdersPage = () => {
    const { authState } = useOktaAuth();
    const [orders, setOrders] = useState<any[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchPreviousOrders = async () => {
            if (authState && authState.isAuthenticated) {
                try {
                    const url = `${process.env.REACT_APP_API}/orders/previous`;
                    const requestOptions = {
                        method: 'GET',
                        headers: {
                            Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                            'Content-Type': 'application/json'
                        }
                    };
                    const response = await fetch(url, requestOptions);
                    if (!response.ok) {
                        throw new Error('Failed to fetch orders');
                    }
                    const responseData = await response.json();
                    setOrders(responseData);
                    setIsLoading(false);
                } catch (error) {
                    console.error(error);
                    setIsLoading(false);
                }
            }
        };

        fetchPreviousOrders();
    }, [authState]);

    if (isLoading) {
        return <Spinner />;
    }

    return (
        <div className="container">
            <h2>Previous Orders</h2>
            {orders.map((order) => (
                <div key={order.id} className="mb-5 border p-3 rounded">
                    <h4>Order ID: {order.id}</h4>
                    <p>Status: {order.status}</p>
                    <div className="row">
                        {order.items.map((item: any) => (
                            <div key={item.productId} className="cart-row item row align-items-center mb-3">
                                <div className="col-md-3">
                                    <img src={item.imgUrl} alt={item.productName} className="img-fluid" />
                                </div>
                                <div className="col-md-3">
                                    <h4>{item.productName}</h4>
                                    <p>{item.description}</p>
                                </div>
                                <div className="col-md-2">
                                    <p>Quantity: {item.quantity}</p>
                                </div>
                                <div className="col-md-2">
                                    <p>${item.price}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            ))}
        </div>
    );
};