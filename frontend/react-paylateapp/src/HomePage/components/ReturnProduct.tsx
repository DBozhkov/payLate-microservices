import React from 'react'
import { Link } from 'react-router-dom';
import ProductModel from '../../models/ProductModel';

export const ReturnProduct: React.FC<{ product: ProductModel }> = (props) => {
    return (
        <div className='col-xs-6 col-sm-6 col-md-4 col-lg-3 mb-3'>
            <div className='text-center'>
                {props.product.imgUrl ?
                    <img
                    src={props.product.imgUrl}
                    width='151'
                    height='233'
                    alt="product"
                />
                    :
                    <img
                        src={require('./../../Images/ProductImages/box.jpg')}
                        width='151'
                        height='233'
                        alt="product"
                    />
                }
                <h6 className='mt-2'>{props.product.productName}</h6>
                <p>{props.product.authorName}</p>
                <Link className='btn main-color text-white' to={`/checkout/${props.product.id}`}>Purchase</Link>
            </div>
        </div>
    );
}