/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bcfdkpconcerttracker;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javax.imageio.ImageIO;
import org.json.simple.JSONObject;

/**
 *
 * @author Ben Frailey
 */
public abstract class Event {

    private String eventName;
    private String location;
    private String date;
    private String picturePath;
        
    private ArrayList<Image> pictures = new ArrayList<>();
    
    private EventType type;    
    
    public Event(String username, String eventName, String location, String date, EventType type){
        this.eventName = eventName;
        this.location = location;
        this.date = date;
        this.type = type;
        
        picturePath = "./userdata/users/" + username + "/" + "pictures/" + eventName + "/";
    }
    
    public Event(){
    }
    
    //these methods are for converting to and from json
    public abstract Event parseObject(JSONObject event, String username);
    public abstract JSONObject convertToJSON();
    
    //returns event name
    public String getName(){
        return eventName;
    }
    
    //returns location
    public String getLocation(){
        return location;
    }
    
    //returns date
    public String getDate(){
        return date;
    }
    
    //adds a picture to the picture folder 
    public void addPicture(File pictureFile){        
        File pictureFolder = new File(picturePath);
        pictureFolder.mkdirs();

        File newPath = new File(picturePath + pictureFile.getName());

        //copies picture into picture folder
        try {
            Files.copy(pictureFile.toPath(), newPath.toPath());
        } catch (IOException ex) {
            Event.displayExceptionAlert(ex);
        }
        
        //converts picture to image
        try {
            Image image = SwingFXUtils.toFXImage(ImageIO.read(newPath), null);
            pictures.add(image);
        } catch (IOException ex) {
            Event.displayExceptionAlert(ex);
        }
        
    }
    
    //returns event type
    public EventType getType(){
        return type;
    }
    
    //displays exception alert in popup window
    public static void displayExceptionAlert(Exception ex){
        TextArea alertTextArea;
        
        Alert alert = new Alert(Alert.AlertType.ERROR); 
        alert.setTitle("Exception Dialog"); 
        alert.setHeaderText(ex.getClass().getCanonicalName());
        alert.setContentText(ex.getMessage());
        
        StringWriter sw = new StringWriter(); 
        PrintWriter pw = new PrintWriter(sw);
        
        ex.printStackTrace(pw);
        String exceptionText = sw.toString(); 
        
        
        Label label = new Label("The exception stacktrace was:"); 
        
        
        alertTextArea = new TextArea(exceptionText);
        alertTextArea.setEditable(true);
        alertTextArea.setWrapText(true);
        
        alertTextArea.setMaxWidth(Double.MAX_VALUE); 
        alertTextArea.setMaxHeight(Double.MAX_VALUE); 
        
        GridPane.setVgrow(alertTextArea, Priority.ALWAYS);
        GridPane.setHgrow(alertTextArea, Priority.ALWAYS);
        
        GridPane expContent = new GridPane(); 
        expContent.add(label, 0, 0); 
        expContent.add(alertTextArea, 0, 1); 
        
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }
    
    //returns pictures
    public ArrayList<Image> getPictures(){
        return pictures;
    }
    
    //loads pictures from folder
    public void loadPictures(){
        File pictureFolder = new File(picturePath);
        if(pictureFolder.exists()){
            File[] imageFiles = pictureFolder.listFiles();
        
            //converts each file into an image and adds it to pictures
            for (File imageFile : imageFiles) {
                try {
                    pictures.add(SwingFXUtils.toFXImage(ImageIO.read(imageFile), null));

                } catch (IOException ex) {
                    Event.displayExceptionAlert(ex);
                }
            }
        }
    }
}   