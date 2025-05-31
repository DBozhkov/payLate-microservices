import { FC } from "react";
import { Link } from "react-router-dom";
import { useOktaAuth } from "@okta/okta-react";

export const ProfileMenu: FC = () => {
    const { authState, oktaAuth } = useOktaAuth();

    const logout = async () => {
        await oktaAuth.signOut();
    };

    return (
        <div className="dropdown-menu dropdown-menu-end bg-white" aria-labelledby="navbarDropdown">
            <Link to="/profile" className="dropdown-item text-dark" style={{ transition: 'color 0.3s' }} onMouseOver={(e) => (e.currentTarget.style.color = '#0056b3')} onMouseOut={(e) => (e.currentTarget.style.color = '#000')}>Profile</Link>
            <Link to="/orderHistory" className="dropdown-item text-dark" style={{ transition: 'color 0.3s' }} onMouseOver={(e) => (e.currentTarget.style.color = '#0056b3')} onMouseOut={(e) => (e.currentTarget.style.color = '#000')}>Previous Orders</Link>
            <button onClick={logout} className="dropdown-item text-dark" style={{ transition: 'color 0.3s' }} onMouseOver={(e) => (e.currentTarget.style.color = '#0056b3')} onMouseOut={(e) => (e.currentTarget.style.color = '#000')}>Logout</button>
        </div>
    );
};