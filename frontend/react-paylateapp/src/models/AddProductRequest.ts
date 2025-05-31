class AddProductRequest {
    productName: string;
    price: number;
    description: string;
    category: string;
    quantity: number;
    imgUrl?: string;
    rating: number;
    authorName: string;
    authorUrl: string;
    productType: string;

    constructor(productName: string, price: number, description: string,
        category: string, quantity: number, imgUrl: string, rating: number, 
        authorName: string, authorUrl: string, productType: string){
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.imgUrl = imgUrl;
        this.rating = rating;
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.productType = productType;
    }
}

export default AddProductRequest;