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
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Ben Frailey
 */
public class Account {
    private String username;
    
    private ArrayList<Event> events = new ArrayList<>();
    private final String relativePath; 
    private JSONArray eventList = new JSONArray();
    
    public Account(String username){
        this.username = username;
        
        relativePath = "./userdata/users/" + username + "/";
        
        //creates folder for pictures
        File makeFolder = new File(relativePath + "pictures/");
        makeFolder.mkdirs();
        
        //creates json file for account
        File userFile = new File(relativePath + username + ".json");
        try {
            userFile.createNewFile();
        } catch (IOException ex) {
            Event.displayExceptionAlert(ex);
        }
    }
    
    //reads the events from the json file
    public void initializeEvents(){
        JSONParser parser = new JSONParser();
        Concert concert = new Concert();
        Festival festival = new Festival();
        
        try (FileReader reader = new FileReader(relativePath + username + ".json")){
                
                ArrayList<Event> temp = new ArrayList<>();
                Object obj = parser.parse(reader);
                
                //parses info into eventList
                eventList = (JSONArray) obj;
               
                //adds concerts and festivals
                eventList.forEach(event -> {
                    events.add(concert.parseObject((JSONObject) event, username));
                    events.add(festival.parseObject((JSONObject) event, username));
                    
                });
                for(Event event : events){
                    if(event == null)
                        temp.add(event);
                }
                for(Event event : temp){
                    events.remove(event);
                }

            } catch (FileNotFoundException ex) {
                Event.displayExceptionAlert(ex);
            } catch (IOException ex) {
                Event.displayExceptionAlert(ex);
            } catch (ParseException ex) {
            
        }
        
    }
    
    //adds a concert
    public void addConcert(String username, String eventName, String location, String date){
        Concert concert;
        events.add(concert = new Concert(username, eventName, location, date));
        updateEvents(concert.convertToJSON());
    }
    
    //adds a festival
    public void addFestival(ArrayList<String> artists, String username, String eventName, String location, String date){
        Festival festival;
        events.add(festival = new Festival(artists, username, eventName, location, date));
        updateEvents(festival.convertToJSON());
    }
    
    //updates the events in json file
    public void updateEvents(JSONObject eventObject){
        FileWriter file;
                    
        eventList.add(eventObject);
            
        try{
            file = new FileWriter(relativePath + username + ".json");
            file.write(eventList.toJSONString());
            file.flush();
        } catch (IOException ex) {
            Event.displayExceptionAlert(ex);
        }   
    }
    
    //returns events
    public ArrayList<Event> getEvents(){
        return events;
    }
}