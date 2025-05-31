import { useOktaAuth } from "@okta/okta-react";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { Spinner } from "../Utils/Spinner";
import { CardElement, useElements, useStripe } from "@stripe/react-stripe-js";
import PaymentRequest from "../models/PaymentRequest";

export const PaymentPage = () => {
  const { authState } = useOktaAuth();
  const [httpError, setHttpError] = useState(false);
  const [submitDisabled, setSubmitDisabled] = useState(false);
  const [amount, setAmount] = useState<number | null>(null);
  const [searchParams] = useSearchParams();

  const elements = useElements();
  const stripe = useStripe();

  const orderId = searchParams.get("orderId");
  const userEmail =
    searchParams.get("userEmail") || authState?.accessToken?.claims.sub;

  useEffect(() => {
    const fetchPaymentDetails = async () => {
      if (!orderId) {
        setHttpError(true);
        return;
      }

      try {
        const url = `${process.env.REACT_APP_API}/orders/${orderId}`;
        const response = await fetch(url, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          throw new Error("Failed to fetch order details.");
        }

        const orderData = await response.json();
        setAmount(orderData?.totalAmount ?? 0);
      } catch (error) {
        console.error(error);
        setHttpError(true);
      }
    };

    fetchPaymentDetails();
  }, [orderId, authState]);

  const checkout = async () => {
    if (!stripe || !elements || !elements.getElement(CardElement)) {
      alert("Stripe is not properly initialized.");
      return;
    }

    setSubmitDisabled(true);

    try {
      const paymentInfo = new PaymentRequest(
        amount || 0,
        "USD",
        userEmail || ""
      );

      const url = `${process.env.REACT_APP_API}/api/payment/secure/payment-intent`;
      const response = await fetch(url, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(paymentInfo),
      });

      if (!response.ok) {
        throw new Error("Error creating payment intent.");
      }

      const stripeResponse = await response.json();

      const result = await stripe.confirmCardPayment(
        stripeResponse.client_secret,
        {
          payment_method: {
            card: elements.getElement(CardElement)!,
            billing_details: {
              email: userEmail,
            },
          },
        }
      );

      if (result.error) {
        alert("Payment failed.");
      } else {
        const completePaymentUrl = `${process.env.REACT_APP_API}/orders/complete-order`;
        const completeResponse = await fetch(completePaymentUrl, {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ orderId, userEmail }),
        });

        if (!completeResponse.ok) {
          throw new Error("Error completing the order.");
        }

        alert("Payment successful! The order will be delivered in 7 days.");
      }
    } catch (error) {
      console.error(error);
      setHttpError(true);
    } finally {
      setSubmitDisabled(false);
    }
  };

  if (httpError) {
    return (
      <div className="container m-5">
        <p>Something went wrong. Please try again later.</p>
      </div>
    );
  }

  if (amount === null) {
    return <Spinner />;
  }

  return (
    <div className="container">
      <div className="card mt-3">
        <h5 className="card-header">Payment</h5>
        <div className="card-body">
          <h5 className="card-title mb-3">Credit Card</h5>
          <p>Amount: ${amount.toFixed(2)}</p>
          <CardElement id="card-element" />
          <button
            disabled={submitDisabled}
            type="button"
            className="btn btn-md main-color text-white mt-3"
            onClick={checkout}
          >
            Pay
          </button>
        </div>
      </div>
      {submitDisabled && <Spinner />}
    </div>
  );
};