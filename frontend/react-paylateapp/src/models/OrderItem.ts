class OrderItem {
    id: number;
    productId: number;
    productName: string;
    quantity: number;
    imgUrl: string;
    price: number;

    constructor(id: number, productId: number, productName: string, quantity: number,
         imgUrl: string, price: number) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}

export default OrderItem;