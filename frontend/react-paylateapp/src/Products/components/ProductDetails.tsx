import { useOktaAuth } from "@okta/okta-react";
import ProductModel from "../../models/ProductModel";
import { Spinner } from "../../Utils/Spinner";
import ReviewModel from "../../models/ReviewModel";
import { Stars } from "../../Utils/Stars";
import ReviewRequestModel from "../../models/ReviewRequestModel";
import { CheckoutReview } from "../../CheckoutReview/CheckoutReview";
import { LatestReviews } from "../../Reviews/LatestReviews";
import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

export const ProductDetails = () => {
    const { partner, productId } = useParams<{ partner?: string, productId: string }>();
    const { authState } = useOktaAuth();
    const navigate = useNavigate();

    const [product, setProduct] = useState<ProductModel>();
    const [isLoading, setIsLoading] = useState(true);
    const [httpError, setHttpError] = useState<string | null>(null);

    const [reviews, setReviews] = useState<ReviewModel[]>([]);
    const [totalStars, setTotalStars] = useState(0);
    const [isLoadingReview, setIsLoadingReview] = useState(true);

    const [isReviewLeft, setIsReviewLeft] = useState(false);
    const [isLoadingUserReview, setIsLoadingUserReview] = useState(true);

    const [isCheckedOut, setIsCheckedOut] = useState(false);
    const [isLoadingProductCheckedOut, setIsLoadingProductCheckedOut] = useState(true);

    const [displayError, setDisplayError] = useState(false);
    const [isCartEmpty, setIsCartEmpty] = useState(true);

    useEffect(() => {
        const fetchProduct = async () => {
            try {
                const endpoint = partner
                    ? `${process.env.REACT_APP_API}/${partner}Products/${productId}`
                    : `${process.env.REACT_APP_API}/products/${productId}`;

                    console.log(`endpoint: ${endpoint}`);

                const response = await fetch(endpoint);
                if (!response.ok) throw new Error("Something went wrong while fetching product.");

                const data = await response.json();
                const id = data._links.self.href.split("/").pop();

                const loadedProduct = new ProductModel(
                    id,
                    data.price,
                    data.productName,
                    data.authorName,
                    data.description,
                    data.quantity,
                    data.category,
                    data.imgUrl,
                    data.authorUrl,
                    data.rating
                );

                setProduct(loadedProduct);
            } catch (error: any) {
                setHttpError(error.message);
            } finally {
                setIsLoading(false);
            }
        };

        fetchProduct();
    }, [partner, productId]);

    useEffect(() => {
        const fetchReviews = async () => {
            try {
                if (!productId) throw new Error("Missing product ID.");
                const reviewUrl = `${process.env.REACT_APP_API}/reviews/search/findByProductId?productId=${productId}`;
                const response = await fetch(reviewUrl);

                if (!response.ok) throw new Error("Failed to fetch reviews.");

                const data = await response.json();
                if (!data._embedded?.reviews) throw new Error("Invalid response format for reviews.");

                const loadedReviews: ReviewModel[] = data._embedded.reviews.map((review: any) => ({
                    id: review.id,
                    userEmail: review.userEmail,
                    date: review.date,
                    rating: review.rating,
                    product_id: review.product_id,
                    reviewDescription: review.reviewDescription
                }));

                const totalStars = loadedReviews.reduce((sum, review) => sum + review.rating, 0);
                setTotalStars(
                    loadedReviews.length > 0 ? parseFloat((totalStars / loadedReviews.length).toFixed(1)) : 0
                );

                setReviews(loadedReviews);
            } catch (error: any) {
                console.error("Review Fetch Error:", error.message);
                setHttpError(error.message);
            } finally {
                setIsLoadingReview(false);
            }
        };

        fetchReviews();
    }, [productId, isReviewLeft]);

    useEffect(() => {
        const fetchUserReviewProduct = async () => {
            if (authState?.isAuthenticated) {
                try {
                    const url = `${process.env.REACT_APP_API}/reviews/secure/user/product?productId=${productId}`;
                    const requestOptions = {
                        method: "GET",
                        headers: {
                            Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                            "Content-Type": "application/json"
                        }
                    };

                    const response = await fetch(url, requestOptions);
                    if (!response.ok) throw new Error("Unable to fetch user review.");

                    const data = await response.json();
                    setIsReviewLeft(data);
                } catch (error: any) {
                    console.error("User Review Fetch Error:", error.message);
                    setHttpError(error.message);
                } finally {
                    setIsLoadingUserReview(false);
                }
            }
        };

        fetchUserReviewProduct();
    }, [authState, productId]);

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
                        const errorText = await response.text();
                        throw new Error(errorText);
                    }
                    const responseData = await response.json();
                    setIsCartEmpty(responseData.length === 0);
                } catch (error: any) {
                    console.error(`Error fetching cart items: ${error.message}`);
                    setHttpError(error.message);
                }
            }
        };

        fetchCartItems();
    }, [authState]);

    if (isLoading || isLoadingReview || isLoadingUserReview) {
        return (
            <Spinner />
        );
    }

    if (httpError) {
        return (
            <div className="container m-5">
                <p>{httpError}</p>
            </div>
        );
    }

    async function checkoutProduct() {
        const url = `${process.env.REACT_APP_API}/products/secure/checkout?productId=${productId}`;
        const requestOptions = {
            method: 'PUT',
            headers: {
                Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
                'Content-Type': 'application/json'
            }
        };
        const checkoutResponse = await fetch(url, requestOptions);
        if (!checkoutResponse.ok) {
            const errorText = await checkoutResponse.text();
            console.error(`Checkout error: ${errorText}`);
            setDisplayError(true);
            throw new Error('Something went wrong!');
        }
        setDisplayError(false);
        setIsCheckedOut(true);
        window.location.href = "/buy";
    }
    async function addToCart() {
        const partnerValue = partner ?? ""; 
        const url = `${process.env.REACT_APP_API}/cart/add?productId=${productId}&partner=${partnerValue}`;
        const requestOptions = {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
                'Content-Type': 'application/json',
            },
        };
    
        try {
            const cartResponse = await fetch(url, requestOptions);
    
            if (!cartResponse.ok) {
                const errorText = await cartResponse.text();
                console.error(`Add to cart error: ${errorText}`);
                setDisplayError(true);
                throw new Error('Something went wrong!');
            }
    
            setDisplayError(false);
            alert('Product added to cart');
        } catch (error: any) {
            console.error('Error adding product to cart:', error.message);
            alert('An error occurred while adding the product to the cart. Please try again.');
        }
    }

    async function addToCartAndCheckout() {
        await addToCart();
        navigate('/checkout');
    }

    async function deleteProduct() {
        if (!authState || !authState.isAuthenticated) {
            alert("You must be logged in as an admin to delete a product.");
            return;
        }

        const url = `${process.env.REACT_APP_API}/admin/secure/delete/product?productId=${productId}&partner=${partner}`;
        const requestOptions = {
            method: 'DELETE',
            headers: {
                Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                'Content-Type': 'application/json',
            },
        };

        try {
            const response = await fetch(url, requestOptions);
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText);
            }
            alert("Product deleted successfully.");
            navigate('/');
        } catch (error: any) {
            alert("Failed to delete the product. Please try again.");
        }
    }

    async function submitReview(starinput: number, reviewDescription: string) {
        if (!product) {
            console.error("Product is not loaded yet.");
            return;
        }

        console.log(product);

        const reviewRequestModel = new ReviewRequestModel(starinput, product.id, reviewDescription);
        const url = `${process.env.REACT_APP_API}/reviews/secure?partner=${partner}`;
        const requestOptions = {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(reviewRequestModel)
        };
        try {
            const returnResponse = await fetch(url, requestOptions);
            if (!returnResponse.ok) {
                const errorText = await returnResponse.text();
                console.error(`Submit review error: ${errorText}`);
                throw new Error('Something went wrong!');
            }
            setIsReviewLeft(true);
        } catch (error: any) {
            console.error("Error submitting review:", error);
            throw error;
        }
    }

    return (
        <div>
            <div className="container d-none d-lg-block">
                {displayError && <div className="alert alert-danger mt-3" role='alert'>
                    An error occurred. Please try again.
                </div>}
                <div className="row mt-5">
                <div className="col-sm-2 col-md-2">
                        {
                            product?.imgUrl ?
                                <img src={product?.imgUrl} width='226' height='349' alt='Product' />
                                :
                                <img src={require('./../../Images/ProductImages/box.jpg')} width='226'
                                    height='349' alt='Product' />
                        }
                        {authState?.accessToken?.claims?.userType === "admin" && (
                            <button className="btn btn-danger mt-3" onClick={deleteProduct}>
                                Delete Product
                            </button>
                        )}
                    </div>
                    <div className="col-4 col-md-4 container">
                        <div className="ml-2">
                            <h2>{product?.productName}</h2>
                            <h5 className="text-primary">{product?.authorName}</h5>
                            <p className="lead">{product?.description}</p>
                            <Stars rating={totalStars} size={32} />
                        </div>
                    </div>
                    <CheckoutReview 
                        product={product} 
                        mobile={false} 
                        isAuthenticated={authState?.isAuthenticated}
                        isCheckedOut={isCheckedOut}
                        checkoutProduct={checkoutProduct}
                        addToCart={addToCart}
                        addToCartAndCheckout={addToCartAndCheckout}
                        displayError={displayError}
                        isReviewLeft={isReviewLeft}
                        submitReview={submitReview}
                        partner={partner || ''}
                        isCartEmpty={isCartEmpty}
                    />
                </div>
                <hr />
                <LatestReviews reviews={reviews} productId={product?.id} mobile={false} />
            </div>
            <div className="container d-lg-none mt-5">
                {displayError && <div className="alert alert-danger mt-3" role='alert'>
                    An error occurred. Please try again.
                </div>}
                <div className="d-flex justify-content-center align-items-center">
                    {
                        product?.imgUrl ?
                            <img src={product?.imgUrl} width='226' height='349' alt='Product' />
                            :
                            <img src={require('./../../Images/ProductImages/box.jpg')} width='226'
                                height='349' alt='Product' />
                    }
                </div>
                <div className="mt-4">
                    <div className="ml-2">
                        <h2>{product?.productName}</h2>
                        <h5 className="text-primary">{product?.authorName}</h5>
                        <p className="lead">{product?.description}</p>
                        <Stars rating={totalStars} size={32} />
                    </div>
                </div>
                <CheckoutReview 
                    product={product} 
                    mobile={true} 
                    isAuthenticated={authState?.isAuthenticated}
                    isCheckedOut={isCheckedOut}
                    checkoutProduct={checkoutProduct}
                    addToCart={addToCart}
                    addToCartAndCheckout={addToCartAndCheckout}
                    displayError={displayError}
                    isReviewLeft={isReviewLeft}
                    submitReview={submitReview}
                    partner={partner || ''}
                    isCartEmpty={isCartEmpty}
                />
                <hr />
                {<LatestReviews reviews={reviews} productId={product?.id} mobile={false} />}
            </div>
        </div>
    );
};