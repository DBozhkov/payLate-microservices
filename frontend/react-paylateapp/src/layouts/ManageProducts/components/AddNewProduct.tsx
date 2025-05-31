import { useOktaAuth } from '@okta/okta-react';
import { useState } from 'react';
import AddProductRequest from '../../../models/AddProductRequest';

export const AddNewProduct = () => {

    const { authState } = useOktaAuth();

    const [productName, setProductName] = useState('');
    const [price, setPrice] = useState(0);
    const [description, setDescription] = useState('');
    const [quantity, setQuantity] = useState(0);
    const [category, setCategory] = useState('Category');
    const [selectedImage, setSelectedImage] = useState<any>(null);
    const [rating, setRating] = useState(0);
    const [authorName, setAuthorName] = useState('');
    const [authorUrl, setAuthorUrl] = useState('');
    const [productType, setProductType] = useState('local');

    const [displayWarning, setDisplayWarning] = useState(false);
    const [displaySuccess, setDisplaySuccess] = useState(false);

    function categoryField(value: string) {
        setCategory(value);
    }

    async function base64ConversionForImages(e: any) {
        if (e.target.files[0]) {
            getBase64(e.target.files[0]);
        }
    }

    function getBase64(file: any) {
        let reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function () {
            setSelectedImage(reader.result);
        };
        reader.onerror = function (error) {
            console.log('Error', error);
        }
    }

    async function submitNewProduct() {
        const url = `${process.env.REACT_APP_API}/admin/secure/add/product`;
        if (authState?.isAuthenticated && productName !== '' && price > 0 && category !== 'Category'
            && description !== '' && quantity > 0 && productType !== '') {
            const product: AddProductRequest = new AddProductRequest(
                productName, price, description, category, quantity, selectedImage, rating, authorName, authorUrl, productType
            );
            const requestOptions = {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(product)
            };

            const submitNewProductResponse = await fetch(url, requestOptions);
            if (!submitNewProductResponse.ok) {
                throw new Error('Something went wrong!');
            }
            setProductName('');
            setPrice(0);
            setDescription('');
            setQuantity(0);
            setCategory('Category');
            setSelectedImage(null);
            setRating(0);
            setAuthorName('');
            setAuthorUrl('');
            setProductType('local');
            setDisplayWarning(false);
            setDisplaySuccess(true);
        } else {
            setDisplayWarning(true);
            setDisplaySuccess(false);
        }
    }

    return (
        <div className='container mt-5 mb-5'>
            {displaySuccess &&
                <div className='alert alert-success' role='alert'>
                    Product added successfully
                </div>
            }
            {displayWarning &&
                <div className='alert alert-danger' role='alert'>
                    All fields must be filled out
                </div>
            }
            <div className='card'>
                <div className='card-header'>
                    Add a new product
                </div>
                <div className='card-body'>
                    <form method='POST'>
                        <div className='row'>
                            <div className='col-md-6 mb-3'>
                                <label className='form-label'>Product Name</label>
                                <input type="text" className='form-control' name='productName' required
                                    onChange={e => setProductName(e.target.value)} value={productName} />
                            </div>
                            <div className='col-md-3 mb-3'>
                                <label className='form-label'>Price</label>
                                <input type="number" className='form-control' name='price' required
                                    onChange={e => setPrice(Number(e.target.value))} value={price} />
                            </div>
                            <div className='col-md-3 mb-3'>
                                <label className='form-label'>Category</label>
                                <input type="text" className='form-control' name='category' required
                                    onChange={e => setCategory(e.target.value)} value={category} />
                            </div>
                        </div>
                        <div className='col-md-12 mb-3'>
                            <label className='form-label'>Description</label>
                            <textarea className='form-control' id='exampleFormControlTextarea1' rows={3}
                                onChange={e => setDescription(e.target.value)} value={description}></textarea>
                        </div>
                        <div className='col-md-3 mb-3'>
                            <label className='form-label'>Quantity</label>
                            <input type='number' className='form-control' name='quantity' required
                                onChange={e => setQuantity(Number(e.target.value))} value={quantity} />
                        </div>
                        <div className='col-md-3 mb-3'>
                            <label className='form-label'>Rating</label>
                            <input type='number' className='form-control' name='rating' required
                                onChange={e => setRating(Number(e.target.value))} value={rating} />
                        </div>
                        <div className='col-md-6 mb-3'>
                            <label className='form-label'>Author Name</label>
                            <input type="text" className='form-control' name='authorName' required
                                onChange={e => setAuthorName(e.target.value)} value={authorName} />
                        </div>
                        <div className='col-md-6 mb-3'>
                            <label className='form-label'>Author URL</label>
                            <input type="text" className='form-control' name='authorUrl' required
                                onChange={e => setAuthorUrl(e.target.value)} value={authorUrl} />
                        </div>
                        <div className='col-md-6 mb-3'>
                            <label className='form-label'>Product Type</label>
                            <input type="text" className='form-control' name='productType' required
                                onChange={e => setProductType(e.target.value)} value={productType} />
                        </div>
                        <input type='file' onChange={e => base64ConversionForImages(e)} />
                        <div>
                            <button type='button' className='btn btn-primary mt-3' onClick={submitNewProduct}>
                                Add Product
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}