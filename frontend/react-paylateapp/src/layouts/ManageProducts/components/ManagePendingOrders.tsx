import React, { useEffect, useState } from "react";
import { useOktaAuth } from "@okta/okta-react";
import { useNavigate } from "react-router-dom";
import { Spinner } from "../../../Utils/Spinner";

export const ManagePendingOrders: React.FC = () => {
    const { authState } = useOktaAuth();
    const [orders, setOrders] = useState<any[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [httpError, setHttpError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchPendingOrders = async () => {
            if (authState && authState.isAuthenticated) {
                try {
                    const url = `${process.env.REACT_APP_API}/orders/pending`;
                    const requestOptions = {
                        method: "GET",
                        headers: {
                            Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                            "Content-Type": "application/json",
                        },
                    };
                    const response = await fetch(url, requestOptions);
                    if (!response.ok) {
                        throw new Error("Failed to fetch pending orders.");
                    }
                    const responseData = await response.json();
                    setOrders(responseData);
                    setIsLoading(false);
                } catch (error: any) {
                    setHttpError(error.message);
                    setIsLoading(false);
                }
            }
        };

        fetchPendingOrders();
    }, [authState]);

    const handleCompleteOrder = (orderId: number, userEmail: string) => {
        navigate(`/payment?orderId=${orderId}&userEmail=${userEmail}`);
    };

    if (isLoading) {
        return <Spinner />;
    }

    if (httpError) {
        return (
            <div className="container m-5">
                <p>Error: {httpError}</p>
            </div>
        );
    }

    return (
        <div className="container">
            <h2>Pending Orders</h2>
            {orders.map((order) => (
                <div key={order.id} className="mb-5 border p-3 rounded">
                    <h4>Order ID: {order.id}</h4>
                    <div className="row">
                        {order.items.map((item: any) => (
                            <div
                                key={item.productId}
                                className="cart-row item row align-items-center mb-3"
                            >
                                <div className="col-md-3">
                                    <img
                                        src={item.imgUrl || "/placeholder.jpg"}
                                        alt={item.productName || "Unnamed Product"}
                                        className="img-fluid"
                                    />
                                </div>
                                <div className="col-md-3">
                                    <h4>{item.productName || "Unnamed Product"}</h4>
                                    <p>{item.description || "No description available."}</p>
                                </div>
                                <div className="col-md-2">
                                    <p>Quantity: {item.quantity || 0}</p>
                                </div>
                                <div className="col-md-2">
                                    <p>${item.price?.toFixed(2) || "0.00"}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="text-center mt-3">
                        <button
                            className="btn btn-primary"
                            onClick={() => handleCompleteOrder(order.id, order.userEmail)}
                        >
                            Complete Order
                        </button>
                    </div>
                </div>
            ))}
        </div>
    );
};