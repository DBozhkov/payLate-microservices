class ReviewRequestModel {
    rating: number;
    productId: number;
    reviewDescription?: string;

    constructor(rating: number, productId: number, reviewDescription: string){
        this.rating = rating;
        this.productId = productId;
        this.reviewDescription = reviewDescription;
    }
}

export default ReviewRequestModel;