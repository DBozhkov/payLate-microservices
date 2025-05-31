import { useOktaAuth } from "@okta/okta-react";
import { Link } from "react-router-dom";

export const LibraryServices = () => {

    const { authState } = useOktaAuth();

    return (
        <div className='container my-5'>
            <div className='row p-4 align-items-center border shadow-lg'>
                <div className='col-lg-7 p-3'>
                    <h1 className='display-4 fw-bold'>
                        You like us?
                    </h1>
                    <p className='lead'>
                        Support our cause and mission and we will
                        give half of what you donate to a random charity!
                    </p>
                    <div className='d-grid gap-2 justify-content-md-start mb-4 mb-lg-3'>
                        {authState?.isAuthenticated ?
                            <Link to='/donate' type="button" className="btn btn-warning btn-lg px-4 me-md-2 fw-bold text-dark">
                                Donate
                            </Link>
                            :
                            <Link className='btn main-color btn-lg text-white' to='/login'>
                                Sign up
                            </Link>
                        }
                    </div>
                </div>
                <div className='col-lg-4 offset-lg-1 shadow-lg lost-image'></div>
            </div>
        </div>
    );
}