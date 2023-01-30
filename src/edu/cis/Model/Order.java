package edu.cis.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Order {
    private String itemID = "";
    private String type = "";
    private String orderID = "";

    private static ArrayList<String> existingIds = new ArrayList<>();

    public Order(String itemID, String type, String  orderID) throws Exception {
        if (orderID.isEmpty() || orderID==null){
            throw new Exception(CISConstants.PARAM_MISSING_ERR);
        }
        setOrderID(orderID);
        setType(type);
        setItemID(itemID);
    }

    @Override
    public String toString() {
        String result = "Order{";
        result += "itemID='" + this.getItemID() + "', ";
        result += "type='" + this.getType() + "', ";
        result += "orderID='" + this.getOrderID() + "'}";
        return result;
    }

    public String getItemID() {return itemID;}
    public void setItemID(String s) {itemID = s;}

    public String getOrderID() {
        return orderID;
    }
    public void setOrderID(String s) throws Exception {
        if (existingIds.contains(s)){
            throw new Exception(CISConstants.DUP_ORDER_ERR);
        }

        if (orderID.isEmpty()) {
            orderID = s;
            existingIds.add(s);
        }
        else if (existingIds.contains(orderID)){
            existingIds.remove(orderID);
            existingIds.add(s);
            orderID = s;
        }
        else {
            existingIds.add(s);
            orderID = s;
        }
    }

    public void orderFulfilled(){
        existingIds.remove(orderID);
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
