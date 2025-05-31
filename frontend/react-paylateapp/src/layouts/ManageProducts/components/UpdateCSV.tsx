import { useOktaAuth } from '@okta/okta-react';
import { useState } from 'react';

export const UpdateCSV = () => {
    const { authState } = useOktaAuth();
    const [message, setMessage] = useState('');

    const handleUpdate = async (url: string) => {
        if (authState?.isAuthenticated) {
            const requestOptions = {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
                    'Content-Type': 'application/json'
                }
            };

            try {
                console.log(`Sending POST request to: ${process.env.REACT_APP_API}${url}`);
                const response = await fetch(`${process.env.REACT_APP_API}${url}`, requestOptions);
                const responseBody = await response.text(); 
                if (response.ok) {
                    setMessage('Products updated successfully');
                } else {
                    console.error('Failed to update products:', responseBody);
                    setMessage(`Failed to update products: ${responseBody}`);
                }
            } catch (error:any) {
                console.error('Error while updating products:', error);
                setMessage(`Failed to update products: ${error.message}`);
            }
        } else {
            setMessage('User is not authenticated');
        }
    };

    const handleScrape = async () => {
        if (authState?.isAuthenticated) {
            const requestOptions = {
                method: 'GET',
                headers: {
                    Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
                    'Content-Type': 'application/json'
                }
            };

            try {
                console.log(`Sending GET request to: ${process.env.REACT_APP_API}/csv/saveOlxProducts`);
                const response = await fetch(`${process.env.REACT_APP_API}/csv/saveOlxProducts`, requestOptions);
                const responseBody = await response.text(); 
                if (response.ok) {
                    setMessage('Products scraped and saved to CSV successfully');
                } else {
                    console.error('Failed to scrape products:', responseBody);
                    setMessage(`Failed to scrape products: ${responseBody}`);
                }
            } catch (error:any) {
                console.error('Error while scraping products:', error);
                setMessage(`Failed to scrape products: ${error.message}`);
            }
        } else {
            setMessage('User is not authenticated');
        }
    };

    return (
        <div className='container mt-5 mb-5'>
            {message && 
                <div className='alert alert-info' role='alert'>
                    {message}
                </div>
            }
            <div className='card'>
                <div className='card-header'>
                    Update CSV
                </div>
                <div className='card-body'>
                    <button 
                        className='btn btn-primary m-2'
                        onClick={() => handleUpdate('/csv/importAliExpressProducts')}
                    >
                        Update AliExpress Products
                    </button>
                    <button 
                        className='btn btn-primary m-2'
                        onClick={() => handleUpdate('/csv/importAmazonProducts')}
                    >
                        Update Amazon Products
                    </button>
                    <button 
                        className='btn btn-primary m-2'
                        onClick={() => handleUpdate('/csv/importOlxProducts')}
                    >
                        Update OLX Products
                    </button>
                    <button 
                        className='btn btn-warning m-2'
                        onClick={handleScrape}
                    >
                        Scrape OLX Products
                    </button>
                </div>
            </div>
        </div>
    );
}