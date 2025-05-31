import React from "react";
import { Link } from "react-router-dom";

export const Partners = () => {
    const partners = [
        {
            imageUrl: "https://m.media-amazon.com/images/I/41sJviCKjVL.png",
            altText: "Olx",
            link: "/products/olx",
            description: "Olx is the biggest Bulgarian E-Commerce platform. See what they can offer you!"
        },
        {
            imageUrl: "https://cdn.vectorstock.com/i/500p/18/30/aliexpress-logo-vector-34501830.jpg",
            altText: "AliExpress",
            link: "/products/aliExpress",
            description: "AliExpress offers almost everything one can desire, from all around the world. Browse it now!"
        },
        {
            imageUrl: "https://logos-world.net/wp-content/uploads/2020/04/Amazon-Logo.png",
            altText: "Amazon",
            link: "/products/amazon",
            description: "Amazon is a global e-commerce platform that offers a wide range of products. Check it out!"
        }
    ];

    return (
        <div className="container mt-5">
            <div className="row mb-5">
                <div className="col-12 text-center">
                    <h1 className="display-5 fw-bold mb-4">Our Partners</h1>
                </div>
            </div>

            <div className="row">
                {partners.map((partner, index) => (
                    <div key={index} className="col-12 col-md-6 mb-4 d-flex align-items-stretch">
                        <div className="card w-100">
                            <Link to={partner.link}>
                                <img
                                    src={partner.imageUrl}
                                    alt={partner.altText}
                                    className="card-img-top"
                                    style={{ maxHeight: "300px", objectFit: "cover" }}
                                />
                            </Link>
                            <div className="card-body">
                                <p className="card-text text-center">{partner.description}</p>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};