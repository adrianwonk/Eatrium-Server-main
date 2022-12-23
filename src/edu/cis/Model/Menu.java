package edu.cis.Model;
import java.util.ArrayList;
public class Menu {

    private ArrayList<MenuItem> eatriumItems;
    private String adminID;
    private static ArrayList<String> existingIds = new ArrayList<>();

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

    public Menu(String adminID_){
        eatriumItems = new ArrayList<MenuItem>();
        setAdminID(adminID_);
    }

    public ArrayList<MenuItem> getEatriumItems() {
        return eatriumItems;
    }

    public MenuItem getItem(String id){
        for (MenuItem value : eatriumItems){
            if (value.getId().equals(id)){
                return value;
            }
        }
        return null;
    }

    public void addEatriumItem(MenuItem i) throws Exception {
        if (!eatriumIdExists(i.getId())) {
            eatriumItems.add(i);
        }
        else{
            throw new Exception(CISConstants.DUP_ITEM_ERR);
        }
    }

    public boolean eatriumIdExists(String id){
        for (MenuItem value : eatriumItems){
            if (value.getId().equals(id)) return true;
        }

        return false;
    }

    public void handleOrder(Order o) throws Exception {
        o.orderFulfilled();
        String itemId = o.getItemID();
        if (eatriumIdExists(itemId)){
            MenuItem item = getItem(itemId);
            item.consume();
        }
        else{
            throw new Exception(CISConstants.INVALID_MENU_ITEM_ERR);
        }
    }


    public void setEatriumItems(ArrayList<MenuItem> eatriumItems) {
        this.eatriumItems = eatriumItems;
    }

    public String getAdminID() {return adminID;}
    public int setAdminID(String s){
        if (existingIds.contains(s)){
            return -1;
        }

        if (adminID.isEmpty()){
            adminID = s;
            existingIds.add(s);
            return existingIds.indexOf(s);
        } else if (existingIds.contains(adminID)){
            existingIds.remove(adminID);
            existingIds.add(s);
            adminID = s;
            return existingIds.indexOf(s);
        }
        else {
            existingIds.add(s);
            adminID = s;
            return existingIds.indexOf(s);
        }
    }

}
