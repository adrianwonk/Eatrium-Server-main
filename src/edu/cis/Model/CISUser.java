package edu.cis.Model;

import java.util.ArrayList;
import java.text.MessageFormat;

public class CISUser {
    private String userId = "";
    public String name;

    public String yearLevel;
    public ArrayList<Order> orders;
    public double money;

    private static ArrayList<String> existingIds = new ArrayList<String>();

    public CISUser(String userId_, String name_, String yearLevel_) throws Exception {

        int e = setUserId(userId_);

        if (e == -1){
            throw new Exception(CISConstants.DUP_USER_ERR);
        }

        setName(name_);
        setYearLevel(yearLevel_);
        orders = new ArrayList<Order>();
        money = 50d;
    }

    @Override
    public String toString() {

        String result = "CISUser{userID='" + userId + "', ";
        result += "name='" + name + "', ";
        result += "yearLevel='" + yearLevel + "', ";
        result += "orders= ";
        // ADD ORDERS INTO RESULT
        for (Order value : orders){
            result += value + ", ";
        }

        result += "money=" + money + "}";
        return result;
    }

    public String getCart(){
        String result = "USER ID: " + userId + ", ";
        result += "NAME: " + name + ", ";
        result += "orders= ";
        // ADD ORDERS INTO RESULT
        for (Order value : orders){
            result += value + ", ";
        }
        return result.substring(0, result.length() - 2);
    }

    public int getYearLevelInt(){
        char chars[] = yearLevel.toCharArray();
        String result = "";
        for (char value:chars){
            if (Character.isDigit(value)){
                result += value;
            }
        }

        return Integer.parseInt(result);
    }

    public String getYearLevel() { return yearLevel; }
    public String getUserId() { return userId; }
    public int setUserId(String s) {

        if (existingIds.contains(s)){
            return -1;
        }

        if (userId.isEmpty()){
            userId = s;
            existingIds.add(s);
            return existingIds.indexOf(s);
        } else if (existingIds.contains(userId)){
            existingIds.remove(userId);
            existingIds.add(s);
            userId = s;
            return existingIds.indexOf(s);
        }
        else {
            existingIds.add(s);
            userId = s;
            return existingIds.indexOf(s);
        }
    }
    public String getName() { return name; }
    public ArrayList<Order> getOrders() { return orders; }
    public double getMoney() { return money; }
    public void setMoney(double money_) { money = money_; }
    public void setName(String name_) { name = name_; }
    public void setYearLevel(String yearLevel_) { yearLevel = yearLevel_; }
    public void setOrders(ArrayList<Order> orders_) { orders = orders_; }

    public void addOrder(Order o) throws Exception{
        orders.add(o);
    }

    public boolean hasOrder(String orderId){
        for (Order value : orders){
            if (value.getOrderID().equals(orderId)) {
                return true;
            }
        }
        return false;
    }

    public void removeOrder(Order o){
        orders.remove(o);
    }

    public Order getOrder(String orderId){
        for (Order value : orders){
            if (value.getOrderID().equals(orderId)) {
                return value;
            }
        }
        return null;
    }

}