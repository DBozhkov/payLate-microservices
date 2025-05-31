import { useOktaAuth } from '@okta/okta-react';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AdminMessages } from './components/AdminMessages';
import { AddNewProduct } from './components/AddNewProduct';
import { UpdateCSV } from './components/UpdateCSV';
import { ManagePendingOrders } from './components/ManagePendingOrders';

export const ManageProductsPage = () => {
    const { authState } = useOktaAuth();
    const navigate = useNavigate();

    const [activeTab, setActiveTab] = useState('addProduct');

    const handleTabClick = (tab: string) => {
        setActiveTab(tab);
    }

    useEffect(() => {
        if (authState?.isAuthenticated) {
            const claims = authState.accessToken?.claims;
            console.log("Claims:", claims);

            const userType = claims?.userType;
            console.log("User Type:", userType);

            if (userType === undefined) {
                console.log("Unauthorized access, redirecting to home...");
                navigate('/home');
            }
        }
    }, [authState, navigate]);

    return (
        <div className='container'>
            <div className='mt-5'>
                <h3>Manage Products</h3>
                <nav>
                    <div className='nav nav-tabs' id='nav-tab' role='tablist'>
                        <button onClick={() => handleTabClick('addProduct')} className={`nav-link ${activeTab === 'addProduct' ? 'active' : ''}`} id='nav-add-product-tab' data-bs-toggle='tab'
                            data-bs-target='#nav-add-product' type='button' role='tab' aria-controls='nav-add-product'
                            aria-selected={activeTab === 'addProduct'}
                        >
                            Add new product
                        </button>
                        <button onClick={() => handleTabClick('pendingOrders')} className={`nav-link ${activeTab === 'pendingOrders' ? 'active' : ''}`} id='nav-pending-orders-tab' data-bs-toggle='tab'
                            data-bs-target='#nav-pending-orders' type='button' role='tab' aria-controls='nav-pending-orders'
                            aria-selected={activeTab === 'pendingOrders'}
                        >
                            Pending Orders
                        </button>
                        <button onClick={() => handleTabClick('messages')} className={`nav-link ${activeTab === 'messages' ? 'active' : ''}`} id='nav-messages-tab' data-bs-toggle='tab'
                            data-bs-target='#nav-messages' type='button' role='tab' aria-controls='nav-messages'
                            aria-selected={activeTab === 'messages'}
                        >
                            Messages
                        </button>
                        <button onClick={() => handleTabClick('updateCsv')} className={`nav-link ${activeTab === 'updateCsv' ? 'active' : ''}`} id='nav-update-csv-tab' data-bs-toggle='tab'
                            data-bs-target='#nav-update-csv' type='button' role='tab' aria-controls='nav-update-csv'
                            aria-selected={activeTab === 'updateCsv'}
                        >
                            Update CSV
                        </button>
                    </div>
                </nav>
                <div className='tab-content' id='nav-tabContent'>
                    <div className={`tab-pane fade ${activeTab === 'addProduct' ? 'show active' : ''}`} id='nav-add-product' role='tabpanel'
                        aria-labelledby='nav-add-product-tab'>
                        <AddNewProduct />
                    </div>
                    <div className={`tab-pane fade ${activeTab === 'pendingOrders' ? 'show active' : ''}`} id='nav-pending-orders' role='tabpanel' aria-labelledby='nav-pending-orders-tab'>
                        {activeTab === 'pendingOrders' && <ManagePendingOrders />}
                    </div>
                    <div className={`tab-pane fade ${activeTab === 'messages' ? 'show active' : ''}`} id='nav-messages' role='tabpanel' aria-labelledby='nav-messages-tab'>
                        {activeTab === 'messages' && <AdminMessages />}
                    </div>
                    <div className={`tab-pane fade ${activeTab === 'updateCsv' ? 'show active' : ''}`} id='nav-update-csv' role='tabpanel' aria-labelledby='nav-update-csv-tab'>
                        {activeTab === 'updateCsv' && <UpdateCSV />}
                    </div>
                </div>
            </div>
        </div>
    );
}