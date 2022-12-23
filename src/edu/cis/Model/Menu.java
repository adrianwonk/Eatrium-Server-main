package edu.cis.Model;
import java.util.ArrayList;
public class Menu {

    private ArrayList<MenuItem> eatriumItems;
    private String adminID;
    private static ArrayList<String> existingIds = new ArrayList<>(); // <-- for adminIds

    @Override
    public String toString() {
        String result = "";
        int i = 1;
        for (MenuItem value : eatriumItems){
            result += i + ". \n";
            result += value;
            result += "\n\n";
            i++;
        }
        return result;
    }

    public Menu(String adminID_) throws Exception {
        eatriumItems = new ArrayList<MenuItem>();
        setAdminID(adminID_);
    }

    public ArrayList<MenuItem> getEatriumItems() {
        return eatriumItems;
    }

    public boolean eatriumIdExists(String itemId) {
        for (MenuItem value : eatriumItems){
            if (value.getId().equals(itemId)){
                return true;
            }
        }
        return false;
    }

    public void addEatriumItem(MenuItem i) throws Exception {
        if (!eatriumIdExists(i.getId())) {
            eatriumItems.add(i);
        }
        else{
            throw new Exception(CISConstants.DUP_ITEM_ERR);
        }
    }

    public void handleOrder(CISUser u, Order o) throws Exception {
        o.orderFulfilled();
        String itemId = o.getItemID();
        MenuItem item = getEatriumItem(itemId);
        double price = item.getPrice();

        if (u.getMoney() >= price) {
            u.setMoney(u.getMoney() - price);
            item.consume(); // Throws sold out error
        }
        else{
            throw new Exception(CISConstants.USER_BROKE_ERR);
        }
    }

    public MenuItem getEatriumItem(String itemId) throws Exception {
        for (MenuItem value : eatriumItems){
            if (value.getId().equals(itemId)){
                return value;
            }
        }
        throw new Exception(CISConstants.INVALID_MENU_ITEM_ERR);
    }


    public void setEatriumItems(ArrayList<MenuItem> eatriumItems) {
        this.eatriumItems = eatriumItems;
    }

    public String getAdminID() {return adminID;}

    public void setAdminID(String s) throws Exception{
        if (existingIds.contains(s)){
            throw new Exception(CISConstants.ADMIN_ID_ERR);
        }

        if (adminID.isEmpty()){
            adminID = s;
            existingIds.add(s);
        } else if (existingIds.contains(adminID)){
            existingIds.remove(adminID);
            existingIds.add(s);
            adminID = s;
        }
        else {
            existingIds.add(s);
            adminID = s;
        }
    }

}
