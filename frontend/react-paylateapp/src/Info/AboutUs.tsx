import React from "react";
import { PostMessage } from "../Messages/PostMessage";

export const AboutUs = () => {
    return (
        <div className="container mt-5">
            <div className="row mb-5">
                <div className="col-12 text-center">
                    <h1 className="display-5 fw-bold mb-4">The Team</h1>
                    <img
                        src="https://img.freepik.com/premium-photo/will-you-join-our-team-group-cheerful-young-people-standing-close-each-other-pointing-you_425904-32938.jpg?w=360"
                        alt="Our Team"
                        className="img-fluid shadow rounded"
                        style={{ width: "80%", maxWidth: "800px", height: "auto" }}
                    />
                </div>
            </div>

            <div className="row mb-4">
                <div className="col-12">
                    <h2 className="fw-bold text-center">Our Goals</h2>
                    <div className="row mb-4">
                        <div className="col-12"></div>
                    </div>
                    <p className="lead text-center">
                        At PayLate, our main focus is on assisting those who can't use online payment methods
                        to buy the things they desire. Our mission is to reach out to those who want something,
                        but they can't get it, because of online payment limitations. That's why we created
                        this platform, so that we can connect you to your favourite e-commerce website and
                        you can choose everything you want, after which we purchase the product for you and ship
                        it to you desired location.
                        <div className="row mb-4">
                            <div className="col-12"></div>
                        </div>
                        We've also implemented an online payment option on our platform for those
                        who can make transactions and simply enjoy having all their favorite products in one place.
                        We've even established our own shop, filled with all natural products from our affiliates.
                    </p>
                </div>
            </div>


            <div className="container mt-5">
                <div className="row mb-4">
                    <div className="col-12">
                        <h2 className="fw-bold text-center">Our Partners</h2>
                        <div className="row justify-content-center g-3 align-items-center">
                            <div className="col-6 col-md-4 col-lg-2">
                                <img
                                    loading="lazy"
                                    src="https://m.media-amazon.com/images/I/41sJviCKjVL.png"
                                    alt="Partner 1"
                                    className="img-fluid"
                                />
                            </div>
                            <div className="col-6 col-md-4 col-lg-2">
                                <img
                                    loading="lazy"
                                    src="https://cdn.vectorstock.com/i/500p/18/30/aliexpress-logo-vector-34501830.jpg"
                                    alt="Partner 2"
                                    className="img-fluid"
                                />
                            </div>
                            <div className="col-6 col-md-4 col-lg-2">
                                <img
                                    loading="lazy"
                                    src="https://src.n-ix.com/uploads/2023/07/03/ee1d3a4a-38bd-4f82-86d8-b4fe07cc758b.png"
                                    alt="Partner 3"
                                    className="img-fluid"
                                />
                            </div>
                            <div className="col-6 col-md-4 col-lg-2">
                                <img
                                    loading="lazy"
                                    src="https://img.freepik.com/premium-vector/amazon-logotype-white-background-logo-internet-service-sale-goods-online-store-platform-arrow-free-shipping-worldwide-shopping-editorial_661108-8065.jpg"
                                    alt="Partner 4"
                                    className="img-fluid"
                                />
                            </div>
                            <div className="col-6 col-md-4 col-lg-2">
                                <img
                                    loading="lazy"
                                    src="https://thumbs.dreamstime.com/b/buenos-aires-argentina-april-rd-temu-logo-three-dimensions-isolated-white-background-d-illustration-314121134.jpg"
                                    alt="Partner 5"
                                    className="img-fluid"
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <div className="row mb-4">
                <div className="col-12">
                    <h2 className="fw-bold text-center">Contacts</h2>
                    <div className="row mb-4">
                        <div className="col-12"></div>
                    </div>
                    <ul className="list-unstyled">
                        <div className="row g-4">
                            <div className="col-12 col-md-4">
                                <li className="item">
                                    <div className="item-header">
                                        <h4 className="fw-bold text-center">PayLate Corporation</h4>
                                        <div className="row mb-4">
                                            <div className="col-12"></div>
                                        </div>
                                    </div>
                                    <div className="fw-bold item-body text-center">
                                        <p>Our Location</p>
                                        <p>Sofia, ul. "Okolovrusten put" 15</p>
                                        <br />
                                    </div>
                                </li>
                            </div>

                            <div className="col-12 col-md-4">
                                <li className="item">
                                    <div className="item-header">
                                        <h4 className="fw-bold text-center">Call us:</h4>
                                        <div className="row mb-4">
                                            <div className="col-12"></div>
                                        </div>
                                    </div>
                                    <div className="fw-bold item-body text-center">
                                        <div className="h5">
                                            0888887777
                                        </div>
                                    </div>
                                    <br />
                                    <div className="fw-bold item-body text-center">
                                        <div className="h5">
                                            02/ 21-210
                                        </div>
                                    </div>
                                </li>
                            </div>

                            <div className="col-12 col-md-4">
                                <li className="item">
                                    <div className="item-header">
                                        <h4 className="fw-bold text-center">E-mail</h4>
                                        <div className="row mb-4">
                                            <div className="col-12"></div>
                                        </div>
                                    </div>
                                    <div className="fw-bold tem-body text-center">
                                        <p>
                                            paylate@gmail.com
                                        </p>
                                    </div>
                                </li>
                            </div>

                            <PostMessage/>

                        </div>
                    </ul>
                </div>
            </div>


            {/* Mobile Vision Section */}
            <div className="row d-lg-none mt-4">
                <div className="col-12">
                    <h2 className="fw-bold text-center">Our Vision</h2>
                    <p className="text-center">
                        Our journey is defined by our commitment to excellence and the belief
                        that knowledge transforms lives. Let us guide you toward achieving
                        your goals.
                    </p>
                </div>
            </div>
        </div>
    );
};
