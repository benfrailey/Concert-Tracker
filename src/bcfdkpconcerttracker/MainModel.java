/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bcfdkpconcerttracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Ben Frailey
 */
public class MainModel {

    private static String username;
    
    private final Account user;
    
    public MainModel(){
        user = new Account(username);
        user.initializeEvents();
    }
    
    //returns events
    public ArrayList<Event> getEvents(){
        return user.getEvents();
    }
    
    public static void setUsername(String usernameInput){
        username = usernameInput;
    }
    
    //adds a festival
    public void addFestival(String artists, String eventName, String location, String date){
        //converts string into list and adds to an arraylist
        String[] artistArray = artists.split("\n");
        ArrayList<String> artistsList = new ArrayList<>();
        List<String> converter = Arrays.asList(artistArray);
        
        artistsList.addAll(converter);
        
        user.addFestival(artistsList, username, eventName, location, date);
    }
    
    //adds a concert
    public void addConcert(String eventName, String location, String date){
        user.addConcert(username, eventName, location, date);       
    }
}
