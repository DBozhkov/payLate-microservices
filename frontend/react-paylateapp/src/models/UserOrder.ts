import OrderItem from "./OrderItem";

class UserOrder {
    id: number;
    orderDate: string;
    status: string;
    items: OrderItem[];
    userEmail: string;

    constructor(id: number, orderDate: string, status: string, items: OrderItem[],
        userEmail: string) {
        this.id = id;
        this.orderDate = orderDate;
        this.status = status;
        this.items = items;
        this.userEmail = userEmail;
    }

    getTotalAmount(): number {
        return this.items.reduce((total, item) => total + item.price * item.quantity, 0);
    }
}

export default UserOrder;