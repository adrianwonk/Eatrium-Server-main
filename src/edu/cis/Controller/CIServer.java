/*
 * File: CIServer.java
 * ------------------------------
 * When it is finished, this program will implement a basic
 * ecommerce network management server.  Remember to update this comment!
 */

package edu.cis.Controller;

import acm.program.*;
import edu.cis.Utils.SimpleServer;
import edu.cis.Model.*;

import java.rmi.server.UID;
import java.util.ArrayList;

public class CIServer extends ConsoleProgram
        implements SimpleServerListener
{

    /* The internet port to listen to requests on */
    private static final int PORT = 8000;

    /* The server object. All you need to do is start it */
    private SimpleServer server = new SimpleServer(this, PORT);
    private ArrayList<CISUser> users = new ArrayList<>();
    private Menu menu;

    private static int orderIdSetter = 0;
    {
        try {
            menu = new Menu("3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Starts the server running so that when a program sends
     * a request to this server, the method requestMade is
     * called.
     */
    public void run()
    {
        println("Starting server on port " + PORT);
        server.start();
    }

    public static void main(String[] args)
    {
        CIServer f = new CIServer();
        f.start(args);
    }

    /**
     * When a request is sent to this server, this method is
     * called. It must return a String.
     *
     * @param request a Request object built by SimpleServer from an
     *                incoming network request by the client
     */
    public String requestMade(Request request) {
        String cmd = request.getCommand();
        println(request.toString());

        // your code here.

        switch(cmd) {
            case CISConstants.PING:
                final String PING_MSG = "Hello, internet";

                //println is used instead of System.out.println to print to the server GUI
                println("   => " + PING_MSG);
                return PING_MSG;

            case CISConstants.CREATE_USER:
                return createUser(request);

            case CISConstants.ADD_MENU_ITEM:
                return addMenuItem(request);

            case CISConstants.PLACE_ORDER:
                return placeOrder(request);

            case CISConstants.DELETE_ORDER:
                return deleteOrder(request);

            case CISConstants.GET_USER:
                return getUser(request);
            case "GET_USER_TYPE":
                try {
                    return "" + EatriumIDs.getIDType(request.getParam(CISConstants.USER_ID_PARAM));
                }
                catch(Exception e){
                    return "N";
                }

            case "MAKE_REGISTER_REQUEST":
                return makeRegisterRequest(request);

            case "HANDLE_REGISTER_REQUEST":
                return handleRequest(request);

            case "GET_ACC_REQUESTS":
                String result = "";
                for (CISUser value: Menu.registerRequests){
                    result += value;
                    result +="'''";
                }
                return result;

            case "GET_MENU_ITEMS":
                String result2 = "";
                for(MenuItem value : menu.getEatriumItems()){
                    result2 += value;
                    result2 += "'''";
                }
                return result2;

            case "ADD_TO_CART":
                return addToCart(request);

            case "REMOVE_FROM_CART":
                return removeFromCart(request);

            case "CHECKOUT_CART":
                return checkOutCart(request);

            case "GET_CART_TOTAL":
                return getCartTotal(request);




            case CISConstants.GET_ORDER:
                return getOrder(request);

            case CISConstants.GET_ITEM:
                return getItem(request);

            case CISConstants.CHECK_USER_EXIST:
                if (EatriumIDs.checkID(request.getParam(CISConstants.USER_ID_PARAM)))
                    return "true";
                else{
                    return "false";
                }

            default:
                return "Error: Unknown command " + cmd + ".";
        }
    }

    public String makeRegisterRequest(Request req) {

        try {
            String uID = req.getParam(CISConstants.USER_ID_PARAM);
            String name = req.getParam(CISConstants.USER_NAME_PARAM);
            String yearLevel = req.getParam(CISConstants.YEAR_LEVEL_PARAM);

            if (uID.equals("") || name.equals("") || yearLevel.equals("")) {
                return "MISSING_PARAMS";
            }

            try {
                CISUser u = new CISUser(uID, name, yearLevel);
                Menu.registerRequests.add(u);
                return "SUCCESS";
            } catch (Exception e) {
                return "ERR: " + e.getMessage();
            }
        }

        catch(Exception e){
            return "ERR: " + e.getMessage();
        }

    }

    public String handleRequest(Request req) {
        String uID = req.getParam(CISConstants.USER_ID_PARAM);
        String accept = req.getParam("ACCEPT");
        boolean acceptB = accept.equals("y");
        CISUser user = Menu.getRequest(uID);
        if (user != null){
            if (acceptB){
                users.add(user);
                user.upgradeUser();
                Menu.registerRequests.remove(user);
                if (Menu.getRequest(uID) == null && getCISUser(uID) != null && EatriumIDs.getIDType(uID) == 'U')
                    return "successfully added user from requests";
                else return "unsuccessfully added user from requests";
            }

            else {
                Menu.registerRequests.remove(user);
                EatriumIDs.removeID(uID);
                if (Menu.getRequest(uID) == null && getCISUser(uID) == null && !EatriumIDs.checkID(uID))
                    return "successfully removed user from requests";
                else{
                    return "unsuccessfully removed user from requests";
                }
            }
        }

        return "user not found in requests";
    }

    public String getItem(Request req) {
        String iID = req.getParam(CISConstants.ITEM_ID_PARAM);

        if (iID.isEmpty()) return CISConstants.PARAM_MISSING_ERR;

        try{
            MenuItem item = menu.getEatriumItem(iID);
            return item.toString();
        }
        catch (Exception e) {
            return CISConstants.INVALID_MENU_ITEM_ERR;
        }

    }
    public String getOrder(Request req){
        String uID = req.getParam(CISConstants.USER_ID_PARAM);
        String oID = req.getParam(CISConstants.ORDER_ID_PARAM);

        if (uID.isEmpty() || oID.isEmpty()) return CISConstants.PARAM_MISSING_ERR;

        CISUser u = getCISUser(uID);
        Order o = u.getOrder(oID);

        return o.toString();
    }
    public String getUser(Request req){
        String userId = req.getParam(CISConstants.USER_ID_PARAM);

        if (userId.isEmpty()) return CISConstants.PARAM_MISSING_ERR;

        else if (EatriumIDs.checkID(userId) == false){
            return CISConstants.USER_INVALID_ERR;
        }

        else {
            try {
                if (EatriumIDs.getIDType(userId) == 'A') {
                    return "ADMIN";
                } else {
                    return "" + getCISUser(userId);
                }
            }
            catch(Exception e){
                return e.getMessage();
            }
        }
    }
    public String deleteOrder(Request req){
        String orderId = req.getParam(CISConstants.ORDER_ID_PARAM);
        String userId = req.getParam(CISConstants.USER_ID_PARAM);

        if (orderId.isEmpty() || userId.isEmpty()) return CISConstants.PARAM_MISSING_ERR;

        // user exist?
        if (!userExists(userId)){
            return CISConstants.USER_INVALID_ERR;
        }

        else {
            // get userIndex
            int userIndex = userIndex(userId);

            try{
                menu.handleOrder(users.get(userIndex), orderId);
            }
            catch (Exception e){
                return e.getMessage();
            }
        }
        return CISConstants.SUCCESS;
    }

    public String getCartTotal(Request req){
        String uID = req.getParam(CISConstants.USER_ID_PARAM);
        try {
            return "" + getCISUser(uID).getCartTotal(menu);
        }
        catch (Exception e){
            return e.getMessage();
        }
    }

    public String addToCart (Request req){
        String itemId = req.getParam(CISConstants.ITEM_ID_PARAM);
        String userId = req.getParam(CISConstants.USER_ID_PARAM);

        if (itemId.isEmpty() || userId.isEmpty()){
            return CISConstants.PARAM_MISSING_ERR;
        }
        else{
            CISUser u = getCISUser(userId);
            if (u == null){
                return CISConstants.USER_INVALID_ERR;
            }
            else {
                try{
                    MenuItem i = menu.getEatriumItem(itemId);
                    i.consume();
                    Order o = new Order(itemId, "", "" + orderIdSetter);
                    orderIdSetter++;
                    u.addOrder(o);
                    return CISConstants.SUCCESS;
                }
                catch (Exception e) {
                    return e.getMessage();
                }
            }
        }
    }

    public String removeFromCart(Request req) {
//        handles the orders
//        adds item back to amountAvail

        String uID = req.getParam(CISConstants.USER_ID_PARAM);
        String oID = req.getParam(CISConstants.ORDER_ID_PARAM);

        CISUser u = getCISUser(uID);
        Order o = u.getOrder(oID);

        if (u == null){
            return CISConstants.USER_INVALID_ERR;
        } else {

            try {
                MenuItem i = menu.getEatriumItem(o.getItemID());
                menu.handleOrder(u, oID);
                i.setAmountAvailable(i.getAmountAvailable() + 1);
                return CISConstants.SUCCESS;
            } catch (Exception e) {
                return e.getMessage();
            }

        }
    }

    public String checkOutCart(Request req){
//        Checks out all items from cart
//          check if user is broke
//              if broke return broke err
//              else handle all orders and minus balance

        String uID = req.getParam(CISConstants.USER_ID_PARAM);

        CISUser u = getCISUser(uID);
        if (u == null){
            return CISConstants.USER_INVALID_ERR;
        } else {

            try{
                double total = u.getCartTotal(menu);
                if (total <= u.getMoney()){
                    ArrayList<Order> tempArr = new ArrayList<>();
                    for (Order o : u.orders)
                        tempArr.add(o);

                    for (Order o : tempArr){
                        menu.handleOrder(u, o.getOrderID());
                    }
                    u.setMoney(u.getMoney()-total);
                }
                else {
                    return CISConstants.USER_BROKE_ERR;
                }
            } catch (Exception e) {
                return e.getMessage();
            }

        }
        return CISConstants.SUCCESS;
    }

    public String placeOrder(Request req) {
        String orderId = "";
        String itemId = "";
        String userId = "";
        String orderType = "";


        orderId = req.getParam(CISConstants.ORDER_ID_PARAM);
        itemId = req.getParam(CISConstants.ITEM_ID_PARAM);
        userId = req.getParam(CISConstants.USER_ID_PARAM);
        orderType = req.getParam(CISConstants.ORDER_TYPE_PARAM);

        if (orderId == null || itemId == null || userId == null || orderType == null){
            return CISConstants.PARAM_MISSING_ERR;
        }

        // user exist?
        if (!userExists(userId)){
            return CISConstants.USER_INVALID_ERR;
        }
        else {
            try{
                CISUser u = getCISUser(userId);

                if (u.hasOrder(orderId)){
                    return CISConstants.DUP_ORDER_ERR;
                }

                if (!menu.itemIDExists(itemId)){
                    return CISConstants.INVALID_MENU_ITEM_ERR;
                }

                Order o = new Order(itemId, orderType, orderId);
                MenuItem item = menu.getEatriumItem(itemId);
                double price = item.getPrice();

                if (u.getMoney() >= price) {
                    item.consume(); // Throws sold out error
                    u.setMoney(u.getMoney() - price);
                    u.addOrder(o);
                }
                else{
                    throw new Exception(CISConstants.USER_BROKE_ERR);
                }
            }
            catch (Exception e){
                return CISConstants.ORDER_INVALID_ERR;
            }


        }
        return CISConstants.SUCCESS;
    }

    public CISUser getCISUser(String uID){
        for (CISUser value : users){
            if (value.getUserId().equals(uID)){
                return value;
            }
        }

        return null;
    }

    public int userIndex(String id){
        for (int i = 0; i < users.size(); i ++){
            if (users.get(i).getUserId().equals(id)){
                return i;
            }
        }

        return -1;
    }
    public boolean userExists(String id){
        for (CISUser value: users){
            if ((value.getUserId()).equals(id)){
                return true;
            }
        }
        return false;
    }
    public String addMenuItem(Request req) {
        String itemName = req.getParam(CISConstants.ITEM_NAME_PARAM);
        String description = req.getParam(CISConstants.DESC_PARAM);
        double price = 0;
        if (!req.getParam(CISConstants.PRICE_PARAM).isEmpty()) price = Double.parseDouble(req.getParam(CISConstants.PRICE_PARAM));
        String type = req.getParam(CISConstants.ITEM_TYPE_PARAM);
        String id = req.getParam(CISConstants.ITEM_ID_PARAM);
        int amountAvail = 0;
        if (!req.getParam(CISConstants.AMOUNT_AVAIL_PARAM).isEmpty()) amountAvail = Integer.parseInt(req.getParam(CISConstants.AMOUNT_AVAIL_PARAM));

        if (itemName.isEmpty() || description.isEmpty() || type.isEmpty()) return CISConstants.PARAM_MISSING_ERR;

        try {
            if (!(id == null || id.isEmpty())) {
                if (!menu.itemIDExists(id)){
                    MenuItem m = new MenuItem(itemName, description, price, id, amountAvail, type);
                    menu.addEatriumItem(m);
                }
                else{
                    MenuItem m = menu.getEatriumItem(id);
                    m.setName(itemName);
                    m.setDescription(description);
                    m.setPrice(price);
                    m.setType(type);
                    m.setId(id);
                    m.setAmountAvailable(amountAvail);
                }
            }
            else{
                MenuItem m = new MenuItem(itemName, description, price, amountAvail, type);
                menu.addEatriumItem(m);
            }
            return CISConstants.SUCCESS;
        }
        catch (Exception e){
            return e.getMessage();
        }
    }
    public String createUser(Request req){
        String userId = req.getParam(CISConstants.USER_ID_PARAM);
        String name = req.getParam(CISConstants.USER_NAME_PARAM);
        String yearLevel = req.getParam(CISConstants.YEAR_LEVEL_PARAM);

        try {
            CISUser newUser = new CISUser(userId, name, yearLevel);
            users.add(newUser);
            return CISConstants.SUCCESS;
        }

        catch(Exception e) {
            return e.getMessage();
        }
    }
}
