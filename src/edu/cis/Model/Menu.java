package edu.cis.Model;
import java.util.ArrayList;
public class Menu {

    private ArrayList<MenuItem> eatriumItems;
    private String adminID = "";

    public static ArrayList<CISUser> registerRequests = new ArrayList<>();

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

    public void handleOrder(CISUser u, String oId) throws Exception {
        Order o;
        if (u.hasOrder(oId)){
            o = u.getOrder(oId);
            o.orderFulfilled();
            u.removeOrder(o);
        } else {
            throw new Exception(CISConstants.ORDER_INVALID_ERR);
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
        if (EatriumIDs.checkID(s)){
            throw new Exception(CISConstants.ADMIN_ID_ERR);
        }

        else if (adminID.isEmpty()){
            if (EatriumIDs.addID(s, 'A'))
            {
                adminID = s;
            }

        } else if (EatriumIDs.checkID(adminID)){
            EatriumIDs.changeAdminID(adminID, s);
            adminID = s;
        }
        else {
            EatriumIDs.addID(s, 'A');
            adminID = s;
        }
    }

}
