package edu.cis.Model;

import java.util.ArrayList;

public class MenuItem {
    String name;
    String description;
    double price;
    private String id;
    int amountAvailable;
    String type;

    public MenuItem(String name_, String description_, double price_, String id_, int amountAvailable_, String type_) throws Exception {
        setName(name_);
        setDescription(description_);
        setPrice(price_);
        setId(id_);
        setAmountAvailable(amountAvailable_);
        setType(type_);
    }

    @Override
    public String toString() {
        String result = "MenuItem{";
        result += "name='" + this.name + "', ";
        result += "description='" + this.description + "', ";
        result += "price=" + price + ", ";
        result += "id='" + id + ", ";
        result += "amountAvailable=" + amountAvailable + ", ";
        result += "type='" + type + "'}";
        return result;
    }

    public String getName() {return name;}
    public void setName(String s) { name = s; }
    public String getDescription() {return description;}
    public void setDescription(String s) {description = s;}
    public double getPrice() {return price;}
    public void setPrice(double d) {price = d;}
    public String getId(){return id;}
    public void setId(String s) {
        id = s;
    }
    public int getAmountAvailable() {return amountAvailable;}
    public void setAmountAvailable(int i) {amountAvailable = i;}
    public String getType(){return type;}
    public void setType(String s){type = s;}

    public void consume() throws Exception {
        if (amountAvailable == 0) {
            throw new Exception(CISConstants.SOLD_OUT_ERR);
        }
        else {
            amountAvailable --;
        }
    }
    public void restock() {amountAvailable = CISConstants.DEFAULT_NUMBER_ITEMS;}
}
