package edu.cis.Model;

import java.util.ArrayList;
import java.text.MessageFormat;

public class CISUser {
    private String userId = "";
    public String name;

    public String yearLevel;
    public ArrayList<Order> orders;
    public double money;


    public CISUser(String userId_, String name_, String yearLevel_) throws Exception {

        boolean e = setUserId(userId_);

        if (!e){
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
        if (result.charAt(result.length()-2)!=',')
            result += ", ";
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
    public boolean setUserId(String s) {

        if (EatriumIDs.checkID(s)){
            return false;
        }

        else if (userId.equals("")){
            if (EatriumIDs.addID(s, 'R')) {
                userId = s;
                return true;
            }
            else  {
                return false;
            }
        } else if (EatriumIDs.checkID(userId)){
            if (EatriumIDs.changeUserID(userId, s, false))
            {
                userId = s;
                return true;
            }
            else return false;
        }
        else {
            EatriumIDs.addID(s,'R');
            userId = s;
            return true;
        }
    }

    public void upgradeUser(){
        EatriumIDs.changeUserID(this.userId, this.userId, true);
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