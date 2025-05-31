import { FC } from "react";
import { Link, NavLink } from "react-router-dom";
import { useOktaAuth } from "@okta/okta-react";
import { Spinner } from "../Utils/Spinner";
import { ProfileMenu } from "../Profile/ProfileMenu";

export const Navbar: FC = () => {
  const { authState } = useOktaAuth();

  if (!authState) {
    return <Spinner />;
  }

  return (
    <nav className='navbar navbar-expand-lg navbar-dark main-color py-3'>
      <div className='container-fluid'>
        <span className='navbar-brand'>Pay Late</span>
        <button className='navbar-toggler' type='button'
          data-bs-toggle='collapse' data-bs-target='#navbarNavDropdown'
          aria-controls='navbarNavDropdown' aria-expanded='false'
          aria-label='Toggle Navigation'
        >
          <span className='navbar-toggler-icon'></span>
        </button>
        <div className='collapse navbar-collapse' id='navbarNavDropdown'>
          <ul className='navbar-nav'>
            <li className='nav-item'>
              <NavLink className='nav-link' to='/home'> Home</NavLink>
            </li>
            <li className='nav-item'>
              <NavLink className='nav-link' to='/partners'> Our Partners</NavLink>
            </li>
            {authState.isAuthenticated &&
              <li className='nav-item'>
                <NavLink className='nav-link' to='/cart'>Cart</NavLink>
              </li>
            }
            {
              authState.isAuthenticated && authState.accessToken?.claims?.userType === 'admin' &&
              <li className="nav-item">
                <NavLink className='nav-link' to='/admin'>Admin</NavLink>
              </li>
            }
          </ul>
          <ul className='navbar-nav ms-auto'>
            {!authState.isAuthenticated ?
              <li className='nav-item m-1'>
                <Link type='button' className='btn btn-outline-light' to='/login'>Sign in</Link>
              </li>
              :
              <li className='nav-item dropdown'>
                <a className="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                  Profile
                </a>
                <ProfileMenu />
              </li>
            }
          </ul>
        </div>
      </div>
    </nav>
  );
};