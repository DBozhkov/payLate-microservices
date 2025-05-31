import React, { useEffect, useState } from 'react';
import ProductModel from '../../models/ProductModel';
import { SingleProduct } from './SingleProduct';
import { Pagination } from "../../Utils/Pagination";
import { Spinner } from '../../Utils/Spinner';
import { useParams } from 'react-router-dom';

export const ProductList: React.FC = () => {
    const { partner } = useParams<{ partner?: string }>();
    const [products, setProducts] = useState<ProductModel[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [httpError, setHttpError] = useState<string | null>(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [productsPerPage] = useState(6);
    const [totalProducts, setTotalProducts] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [search, setSearch] = useState('');

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const baseEndpoint = partner
                    ? `${process.env.REACT_APP_API}/${partner}Products`
                    : `${process.env.REACT_APP_API}/products`;
    
                const endpoint = search
                    ? `${baseEndpoint}/search/findByProductNameContaining?productName=${search}&page=${currentPage - 1}&size=${productsPerPage}`
                    : `${baseEndpoint}?page=${currentPage - 1}&size=${productsPerPage}`;
    
                const response = await fetch(endpoint);
                if (!response.ok) {
                    throw new Error("Failed to fetch products from the API.");
                }
    
                const responseData = await response.json();
                const loadedProducts = responseData._embedded
                    ? (partner ? responseData._embedded[`${partner}Products`] : responseData._embedded.products)
                    : [];
    
                const productList = loadedProducts.map((product: any) => {
                    const id = product._links.self.href.split("/").pop(); 
                    console.log(id);
                    return new ProductModel(
                        id,
                        product.price,
                        product.productName,
                        product.authorName,
                        product.description,
                        product.quantity,
                        product.category,
                        product.imgUrl,
                        product.authorUrl,
                        product.rating
                    );
                });
    
                setProducts(productList);
                setTotalProducts(responseData.page?.totalElements || 0);
                setTotalPages(responseData.page?.totalPages || 0);
                setIsLoading(false);
            } catch (error: any) {
                console.error("Error fetching products:", error.message);
                setIsLoading(false);
                setHttpError(error.message);
            }
        };
    
        fetchProducts();
    }, [partner, currentPage, search]);

    const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

    const handleSearch = () => {
        setCurrentPage(1);
    };

    if (isLoading) {
        return <Spinner />;
    }

    if (httpError) {
        return (
            <div className="container m-5">
                <div>{httpError}</div>
            </div>
        );
    }

    return (
        <div className="container mt-5">
            <div className="row">
                <div className="col-10">
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Search products by name..."
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                </div>
                <div className="col-2">
                    <button className="btn btn-primary w-100" onClick={handleSearch}>
                        Search
                    </button>
                </div>
            </div>
            <div className="row mt-4">
                {products.map((product) => (
                    <SingleProduct
                        key={product.id}
                        product={product}
                        partner={partner}
                    />
                ))}
            </div>
            {totalPages > 1 && (
                <Pagination currentPage={currentPage} totalPages={totalPages} paginate={paginate} />
            )}
        </div>
    );
};