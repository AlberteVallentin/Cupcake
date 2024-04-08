package app.entities;

public class CartLine {
    private Top top;
    private Bottom bottom;
    private int quantity;

    public CartLine(Top top, Bottom bottom, int quantity) {
        this.top = top;
        this.bottom = bottom;
        this.quantity = quantity;
    }

    public Top getTop() {
        return top;
    }

    public Bottom getBottom() {
        return bottom;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotal() {
        return (top.getPrice() + bottom.getPrice()) * quantity;
    }
}
