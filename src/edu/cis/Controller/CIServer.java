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
    {
        try {
            menu = new Menu("3");
        } catch (Exception e) {
            println(e.getMessage());
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

            default:
                return "Error: Unknown command " + cmd + ".";
        }
    }

    public String placeOrder(Request req) {
        String orderId = req.getParam(CISConstants.ORDER_ID_PARAM);
        String itemId = req.getParam(CISConstants.ITEM_ID_PARAM);
        String userId = req.getParam(CISConstants.USER_ID_PARAM);
        String orderType = req.getParam(CISConstants.ORDER_TYPE_PARAM);

        // user exist?
        if (!userExists(userId)){
            return CISConstants.USER_INVALID_ERR;
        }

        else {
            // get userIndex
            int userIndex = userIndex(userId);
            if (!users.get(userIndex).hasOrder(orderId)){
                return CISConstants.ORDER_INVALID_ERR;
            }

            try{
                if (!menu.eatriumIdExists(itemId)){
                    return CISConstants.INVALID_MENU_ITEM_ERR;
                }

                Order o = new Order(itemId, orderType, orderId);
                menu.handleOrder(getUser(userId), o); // handles user broke, invalid menu item, sold out
            }
            catch (Exception e){
                return e.getMessage();
            }


        }
        return CISConstants.SUCCESS;
    }

    public CISUser getUser(String uID){
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
        double price = Double.parseDouble(req.getParam(CISConstants.PRICE_PARAM));
        String type = req.getParam(CISConstants.ITEM_TYPE_PARAM);
        String id = req.getParam(CISConstants.ITEM_ID_PARAM);

        try {
            MenuItem m = new MenuItem(itemName, description, price, id, CISConstants.DEFAULT_NUMBER_ITEMS, type);
            menu.addEatriumItem(m);
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
