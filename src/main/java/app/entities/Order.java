package app.entities;
public class Order {
private int orderId;
private int userId;
private double totalPrice;

    public Order(int orderId, int userId, double totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
