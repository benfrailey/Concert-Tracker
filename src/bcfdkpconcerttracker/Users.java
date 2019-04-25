/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bcfdkpconcerttracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author Ben Frailey
 */
public class Users {
    private static HashMap users;
    private static final String PATH = "./userdata/";
    private static JSONArray userList = new JSONArray();
    
    //returns hashmap of users
    public static HashMap getMap(){  
        //if users is null, initializes it and fills it from user json file
        if(users == null){
            users = new HashMap();
            JSONParser parser = new JSONParser();
            File makeFile = new File(PATH + "users.json");
            
            //parses json file for users
            try (FileReader reader = new FileReader(PATH + "users.json")){
                
                Object obj = parser.parse(reader);
                
                userList = (JSONArray) obj;
               
                userList.forEach(user -> parseUserObject((JSONObject) user));
            } catch (FileNotFoundException ex) {
                try {
                    makeFile.createNewFile();
                } catch (IOException ex1) {
                    Event.displayExceptionAlert(ex1);
                }
            } catch (IOException | ParseException ex) {
                Event.displayExceptionAlert(ex);
            }
        }
        return users;
    } 
    
    //adds user to hashmap and json file
    public static void addToMap(String username, String password){
        FileWriter file = null;
            
        JSONObject userInfo = new JSONObject();
        
        //adds username and password to jsonObject
        userInfo.put("password", password);
        userInfo.put("username", username);
        
        //adds user object to json array
        JSONObject userObject = new JSONObject();
        userObject.put("user", userInfo);
        
        userList.add(userObject);
        
        //rewrites users.json with new user added
        try{
            file = new FileWriter(PATH + "users.json");
            file.write(userList.toJSONString());
            file.flush();
            
            users.put(username, password);
        } catch (IOException ex) {
            Event.displayExceptionAlert(ex);
        }   
    }
    
    //parses JSONObject for user info and adds user to hashmap
    private static void parseUserObject(JSONObject user){
        JSONObject userObject = (JSONObject) user.get("user");
        
        String username = (String) userObject.get("username");
        String password = (String) userObject.get("password");
        
        users.put(username, password);
        
    }
}