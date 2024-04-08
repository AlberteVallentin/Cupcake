package app.entities;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartLine> cartLines = new ArrayList<>();

    public List<CartLine> getCartLines() {
        return cartLines;
    }

    public void add(Top top, Bottom bottom, int quantity) {

        CartLine cartLine = new CartLine(top, bottom, quantity);
        cartLines.add(cartLine);

    }


    public int getTotal() {

        int sum = 0;

        for (CartLine cartline : cartLines) {

            sum += (cartline.getTop().getPrice() + cartline.getBottom().getPrice()) * cartline.getQuantity();

        }

        return sum;
    }
    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (CartLine cartLine : cartLines) {
            totalQuantity += cartLine.getQuantity();
        }
        return totalQuantity;
    }

    public int cartCount() {
        return cartLines.size();
    }

}
