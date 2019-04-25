/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bcfdkpconcerttracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Ben Frailey
 */
public class LoginModel {        
    private HashMap users;
    
    //tests login info and returns result
    public int testLoginInfo(String usernameInput, String passwordInput){        
        users = Users.getMap();
        int index;
        
        if(usernameInput.equals("") || passwordInput.equals("")){
            return 0;
        }
        
        if(users.containsKey(usernameInput))
            if(users.get(usernameInput).equals(passwordInput)){
                MainModel.setUsername(usernameInput);
                return 2;
            }
        
        return 1;
    }
    
    //checks if new account can be created
    public int checkNewAccount(String usernameInput, String passwordInput){
        users = Users.getMap();
        if(usernameInput.equals("") || passwordInput.equals(""))
            return 0;
        
        if(users.containsKey(usernameInput))
            return 1;
     
        return 2;
    }
    
    //creates account
    public void createAccount(String username, String password){
        Users.addToMap(username, password);
        }
    
    public String getHelp(){
        File file = new File("./about.txt");
        String helpMessage = "";
        
        if(file != null){
            BufferedReader bufferedReader = null; 
            
            String document = ""; 
            String line = ""; 
            
            try {
                bufferedReader = new BufferedReader(new FileReader(file)); 
                
                while( (line = bufferedReader.readLine()) != null ){
                    document += line + "\n"; 
                }
                
                helpMessage = document; 
  
            } catch(IOException ex){
                Event.displayExceptionAlert(ex); 
            } 
                
                    try { 
                        if(bufferedReader != null)
                            bufferedReader.close();
                    } catch (IOException ex) {
                        Event.displayExceptionAlert(ex); 
                    } 
                }  
            
    
    
        return helpMessage;
    }
    }