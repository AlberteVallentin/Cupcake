package app.entities;
public class Order {
private int orderId;
private int costumerId;
private double totalPrice;

    public Order(int orderId, int costumerId, double totalPrice) {
        this.orderId = orderId;
        this.costumerId = costumerId;
        this.totalPrice = totalPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCostumerId() {
        return costumerId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
