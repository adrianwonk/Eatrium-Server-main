package edu.cis.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EatriumIDs {

    // A for admin
    // U for user

    public static Map<String, Character> existingIds = new HashMap<String, Character>();

    public static boolean addID(String id, Character userType) {
        if (!existingIds.containsKey(id)) {
            existingIds.put(id, userType);
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean checkID (String id){
        if (existingIds.containsKey(id)) return true;
        else return false;
    }

    public static boolean checkUserID(String id){
        boolean result = false;
        if (existingIds.containsKey(id)){
            if (existingIds.get(id).equals('U')) result = true;
        }
        return result;
    }

    public static boolean checkAdminID(String id){
        boolean result = false;
        if (existingIds.containsKey(id)){
            if (existingIds.get(id).equals('A')) result = true;
        }
        return result;
    }

    public static boolean changeUserID(String id, String newId, boolean promote){
        Character t = 'U';
        if (checkUserID(id)) {
            if (addID(newId, t)) {
                existingIds.remove(id);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public static boolean changeAdminID(String id, String newId){
        Character t = 'A';
        if (checkAdminID(id)) {
            if (addID(newId, t)) {
                existingIds.remove(id);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }


    public static Character getIDType (String id) throws Exception{ //gives A or U
        if (existingIds.containsKey(id)) return existingIds.get(id);
        else {throw new Exception(CISConstants.USER_INVALID_ERR);}
    }

}
