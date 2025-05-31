import { FC, useEffect, useState } from "react";
import { useOktaAuth } from "@okta/okta-react";
import { Spinner } from "../Utils/Spinner";
import UserOrder from "../models/UserOrder";
import OrderItem from "../models/OrderItem";

export const PreviousOrders: FC = () => {
    const { authState } = useOktaAuth();
    const [orders, setOrders] = useState<UserOrder[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [httpError, setHttpError] = useState<string | null>(null);

    useEffect(() => {
        const fetchOrders = async () => {
            if (authState && authState.isAuthenticated) {
                try {
                    const url = `${process.env.REACT_APP_API}/orders/previous`;
                    console.log('Fetching orders from:', url);

                    const requestOptions = {
                        method: 'GET',
                        headers: {
                            Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                            'Content-Type': 'application/json'
                        }
                    };
                    console.log('Request options:', requestOptions);

                    const response = await fetch(url, requestOptions);
                    console.log('Response:', response);

                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }

                    const responseData = await response.json();
                    console.log('Response data:', responseData);

                    const loadedOrders: UserOrder[] = responseData.map((order: any) => {
                        const items: OrderItem[] = order.items.map((item: any) => new OrderItem(item.id, item.productId, item.productName, item.quantity, item.imgUrl, item.price));
                        return new UserOrder(order.id, order.orderDate, order.status, items, order.userEmail);
                    });

                    setOrders(loadedOrders);
                    setIsLoading(false);
                } catch (error: any) {
                    console.error('Fetch error:', error);
                    setIsLoading(false);
                    setHttpError(error.message);
                }
            } else {
                console.log('User is not authenticated');
            }
        };

        fetchOrders();
    }, [authState]);

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
            <h2>Previous Orders</h2>
            {orders.length === 0 ? (
                <p>You have no previous orders.</p>
            ) : (
                orders.map((order) => (
                    <div key={order.id} className="row mb-4">
                        <div className="col-md-5">
                            <div className="order-info">
                                <p style={{ fontWeight: 800 }}>Order Number</p>
                                <p className="order-info-info">{order.id}</p>
                            </div>
                            <div className="order-info">
                                <p style={{ fontWeight: 800 }}>Order Date</p>
                                <p className="order-info-info">{new Date(order.orderDate).toLocaleDateString()}</p>
                            </div>
                            <div className="order-info">
                                <p style={{ fontWeight: 800 }}>Total Amount</p>
                                <p className="order-info-info">{order.getTotalAmount().toFixed(2)} USD</p>
                            </div>
                            <div className="order-info">
                                <p style={{ fontWeight: 800 }}>Status</p>
                                <p className="order-info-info">{order.status}</p>
                            </div>
                            <div className="order-product-images">
                                {order.items.map(item => (
                                    <a key={item.id} target="_blank" rel="noopener noreferrer" href={`https://www.example.com/product/${item.productId}/`}>
                                        <img src={item.imgUrl} title={item.productName} alt={item.productName} width="60" height="75" />
                                    </a>
                                ))}
                            </div>
                            <p className="delivery-info">Delivered on <span>{new Date(order.orderDate).toLocaleDateString()}</span></p>
                        </div>
                        <div className="col-md-7">
                            <a href={`https://www.example.com/sales/order/view/order_id/${order.id}/`} className="btn btn-primary">View Order</a>
                        </div>
                    </div>
                ))
            )}
        </div>
    );
};