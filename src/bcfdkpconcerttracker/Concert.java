/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bcfdkpconcerttracker;

import static bcfdkpconcerttracker.EventType.CONCERT;
import java.io.File;
import java.io.IOException;
import org.json.simple.JSONObject;

/**
 *
 * @author Ben Frailey
 */
public class Concert extends Event{   
    private String username;
    private String eventName;
    private String location;
    private String date;
    
    private String path;
    private File info;
        
    public Concert(String username, String eventName, String location, String date){
        super(username, eventName, location, date, CONCERT);
        
        this.username = username;
        this.eventName = eventName;
        this.location = location;
        this.date = date;
        
        path = "./userdata/users/" + username + "/";
        
        //creates a file to store the info if one doesnt already exists
        info = new File(path + username + ".json");
        try {
            info.createNewFile();
        } catch (IOException ex) {
            Event.displayExceptionAlert(ex);
        }
    }
    
    public Concert(){
        super();
    }
    
    //returns date
    public String getEventDate(){
        return date;
    }
    
    //returns location
    public String getEventLocation(){
        return location;
    }
    
    //converts concert to jsonobject
    @Override
    public JSONObject convertToJSON(){            
        JSONObject concertInfo = new JSONObject();
        
        concertInfo.put("username", username);
        concertInfo.put("eventName", eventName);
        concertInfo.put("location", location);
        concertInfo.put("date", date);

        
        JSONObject concertObject = new JSONObject();
        concertObject.put("concert", concertInfo);
        
        return concertObject;
    }
    
    //reads json file and creates and returns concert
    @Override
    public Event parseObject(JSONObject event, String username){        
        JSONObject userObject = (JSONObject) event.get("concert");
        
        if(userObject != null){
        String eventName = (String) userObject.get("eventName");
        String location = (String) userObject.get("location");
        String date = (String) userObject.get("date");
        
        Concert concert = new Concert(username, eventName, location, date);
        concert.loadPictures();
        return concert;
        }
        
        //returns null if userObject is null
        else return null;
    }
}