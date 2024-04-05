package app.controllers;

import app.entities.Bottom;
import app.entities.Top;

public class OrderController {

   private  Top top;
   private  Bottom bottom;
   private int quantity;


    public Top getTop() {
        return top;
    }

    public Bottom getbottom() {
        return bottom;
    }

    public int getQuantity() {
        return quantity;
    }


}
