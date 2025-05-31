import { Link } from "react-router-dom";

export const LearnMore = () => {
    return (
        <div className='p-5 mb-4 bg-dark header'>
            <div className='container-fluid py-5 text-white 
                d-flex justify-content-center align-items-center'>
                <div>
                    <h1 className='display-5 fw-bold'>Can't make online transactions?</h1>
                    <p className='col-md-8 fs-4'>We can help you! You order, we pay it!</p>
                    <Link type='button' className='btn main-color btn-lg text-white' to='/about'>
                        Learn more</Link>
                </div>
            </div>
        </div>
    );
}