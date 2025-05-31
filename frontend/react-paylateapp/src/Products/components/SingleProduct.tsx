import React from 'react';
import ProductModel from '../../models/ProductModel';
import { Link } from 'react-router-dom';

export const SingleProduct: React.FC<{ product: ProductModel, partner?: string }> = (props) => {
    const productLink = props.partner ? `/products/${props.partner}/${props.product.id}` : `/products/${props.product.id}`;

    console.log('Partner:', props.partner);
    console.log(productLink);

    return (
        <div className="col-12 col-md-6 col-lg-4 mb-4">
            <div className="card h-100 shadow-sm">
                <Link to={productLink}>
                    <img src={props.product.imgUrl} className="card-img-top" alt="Product" />
                </Link>
                <div className="card-body">
                    <p className="card-text text-center fw-bold">{props.product.productName}</p>
                    <h5 className="card-title text-center">{props.product.price}</h5>
                    <p className="text-center fw-bold">Available: {props.product.quantity}</p>
                </div>
            </div>
        </div>
    );
}; 