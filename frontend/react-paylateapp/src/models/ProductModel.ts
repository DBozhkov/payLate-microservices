class ProductModel {
    id: number;
    price: number;
    productName: string;
    authorName?: string;
    authorUrl?: string;
    description?: string;
    quantity?: number;
    category?: string;
    imgUrl?: string;
    rating?: number;

    constructor (id: number, price: number, productName: string, authorName: string, description: string,
         quantity: number, category: string, 
         imgUrl: string, authorUrl: string, rating: number){
            this.id = id;
            this.price = price;
            this.productName = productName;
            this.authorName = authorName;
            this.authorUrl = authorUrl;
            this.description = description;
            this.quantity = quantity;
            this.category = category;
            this.imgUrl = imgUrl;
            this.rating = rating;
        }
}

export default ProductModel;