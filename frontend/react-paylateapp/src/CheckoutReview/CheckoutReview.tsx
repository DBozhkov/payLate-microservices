import { Link } from "react-router-dom";
import ProductModel from "../models/ProductModel";
import { LeaveReview } from "../Utils/LeaveReview";

interface CheckoutReviewProps {
    product: ProductModel | undefined;
    mobile: boolean;
    isAuthenticated: any;
    isCheckedOut: boolean;
    checkoutProduct: any;
    addToCart: any;
    addToCartAndCheckout: any;
    displayError: boolean;
    isReviewLeft: boolean;
    submitReview: any;
    partner: string;
    isCartEmpty: boolean;
}

export const CheckoutReview: React.FC<CheckoutReviewProps> = (props) => {

    const buttonRender = () => {
        if (props.isAuthenticated) {
            if (!props.isCheckedOut) {
                return (
                    <div className="d-flex">
                        <button onClick={props.addToCartAndCheckout} className="btn btn-success btn-md me-2">Checkout</button>
                        <button onClick={() => props.addToCart()} className="btn btn-success btn-md">Add to Cart</button>
                    </div>
                );
            } else if (props.isCheckedOut) {
                return (<div><b>Product checked out. Enjoy!</b></div>);
            } else if (!props.isCheckedOut) {
                return (<div className="text-danger">Too many products checked out.</div>);
            }
        }
        return (<Link to={'/login'} className="btn btn-success btn-md">Sign in</Link>);
    }

    const reviewRender = () => {
        if (props.isAuthenticated && !props.isReviewLeft) {
            return (
                <div>
                    <LeaveReview submitReview={props.submitReview} partner={props.partner}/>
                </div>
            );
        } else if (props.isAuthenticated && props.isReviewLeft) {
            return (
                <div>
                    <b>
                        Thank you for your review!
                    </b>
                </div>
            );
        }
        return (
            <div>
                <hr />
                <div>
                    Sign in to be able to leave a review.
                </div>
            </div>
        );
    }

    return (
        <div className={props.mobile ? 'card d-flex mt-5' : 'card col-3 container d-flex mb-5'}>
            <div className='card-body container'>
                {props.displayError && <div className="alert alert-danger mt-3" role='alert'>
                    An error occurred. Please try again.
                </div>}
                <div className='mt-3'>
                    {props.product && props.product.quantity && props.product.quantity > 0 ?
                        <h4 className="text-success">
                            Available
                        </h4>
                        :
                        <h4 className="text-danger">
                            Not in stock
                        </h4>
                    }
                    <div className="row">
                        <div className="col-12 lead d-inline-flex align-items-center">
                            <b>{props.product?.quantity}</b>&nbsp;products available
                        </div>
                    </div>
                </div>
                {buttonRender()}
                <hr />
                <p className="mt-3">
                    Please check again later!
                </p>
                {reviewRender()}
            </div>
        </div>
    );
};