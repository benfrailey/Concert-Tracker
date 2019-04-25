/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bcfdkpconcerttracker;

import static bcfdkpconcerttracker.EventType.FESTIVAL;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONObject;

/**
 *
 * @author Ben Frailey
 */
public class Festival extends Event{
    ArrayList<String> artists = new ArrayList<>();

    private String username;
    private String eventName;
    private String location;
    private String date;
    private String path;
    private File info;
    
    public Festival(ArrayList<String> artists, String username, String eventName, String location, String date){      
        super(username, eventName, location, date, FESTIVAL);
        
        path = "./userdata/users/" + username + "/";
        
        this.username = username;
        this.artists = artists;
        this.eventName = eventName;
        this.location = location;
        this.date = date;
        
        //creates new file for user if one doesnt already exist
        info = new File(path + username + ".json");
        try {
            info.createNewFile();
        } catch (IOException ex) {
            Event.displayExceptionAlert(ex);
        }
    }
    
    public Festival(){
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
    
    //returns artists
    public ArrayList<String> getartists(){
        return artists;
    }
    
    //adds an artist
    public void addArtist(String artist){
        artists.add(artist);
    }
    
    //reads json and creates and returns a festival
    @Override
    public Event parseObject(JSONObject event, String username){
        JSONObject eventObject = (JSONObject) event.get("festival");
        Gson gson = new Gson();
        
        if(eventObject != null){
        String json = (String) eventObject.get("artists");
        ArrayList<String> artists = gson.fromJson(json, ArrayList.class);
        String eventName = (String) eventObject.get("eventName");
        String location = (String) eventObject.get("location");
        String date = (String) eventObject.get("date");
        
        Festival festival = new Festival(artists, username, eventName, location, date);
        festival.loadPictures();
        
        return festival;
        }
        
        //returns null if eventObject is null
        else return null;
    }
    
    //converts festival into a jsonobject
    @Override
    public JSONObject convertToJSON(){
        JSONObject festivalInfo = new JSONObject();
        Gson gson = new Gson();
        
        festivalInfo.put("artists", gson.toJson(artists));
        festivalInfo.put("username", username);
        festivalInfo.put("eventName", eventName);
        festivalInfo.put("location", location);
        festivalInfo.put("date", date);

        
        JSONObject festivalObject = new JSONObject();
        festivalObject.put("festival", festivalInfo);
        
        return festivalObject;
    }
}