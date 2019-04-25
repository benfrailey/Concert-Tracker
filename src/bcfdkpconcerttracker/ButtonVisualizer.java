/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bcfdkpconcerttracker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 *
 * @author Ben Frailey
 */
public class ButtonVisualizer implements Visualizer{
    private final String name = "Button Visualizer";
    
    private AnchorPane vizPane;
    private final VBox eventVBox = new VBox();
    
    private ArrayList<Event> events;
    
    //initializes the visualizer
    @Override
    public void start(AnchorPane vizPane, ArrayList<Event> events){
        this.vizPane = vizPane;
        this.events = events;

        vizPane.getChildren().add(eventVBox);

        update(events);
    }
    
    //displays info for selected event
    public void open(Event event){
        eventVBox.getChildren().clear();
        
        ArrayList<Image> images = event.getPictures();
        
        Button backButton = new Button("Back to Concert List");
        backButton.prefWidthProperty().bind(vizPane.widthProperty());
        backButton.setOnAction((new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    update(events);
            }
    }));
        
        Button pictureButton = new Button("Add Picture");
        pictureButton.prefWidthProperty().bind(vizPane.widthProperty());
        pictureButton.setOnAction((new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    addPictures(event);
            }
    }));
        
        //concerts and festivals display differently so this divides the events into concert and festival
        if(event.getType() == EventType.CONCERT){
            
            HBox artistHBox = new HBox();
            HBox locationHBox = new HBox();
            HBox dateHBox = new HBox();
            
            artistHBox.getChildren().addAll(new Text("Artist: "), new Text(event.getName()));
            locationHBox.getChildren().addAll(new Text("Location: "), new Text(event.getLocation()));
            dateHBox.getChildren().addAll(new Text("Date: "), new Text(event.getDate()));
            
            eventVBox.getChildren().addAll(backButton, pictureButton, artistHBox, locationHBox, dateHBox);
        }
        else{
            Festival festival = (Festival) event;
            ArrayList<String> artists = festival.getartists();
            String artistsString = "";
            for(String artist : artists){
                artistsString = artistsString.concat(artist + "\n");
            }
            
            
            HBox eventHBox = new HBox();
            HBox locationHBox = new HBox();
            HBox dateHBox = new HBox();
            HBox artistsHBox = new HBox();
            
            eventHBox.getChildren().addAll(new Text("Event: "), new Text(event.getName()));
            locationHBox.getChildren().addAll(new Text("Location: "), new Text(event.getLocation()));
            dateHBox.getChildren().addAll(new Text("Date: "), new Text(event.getDate()));
            artistsHBox.getChildren().addAll(new Text("Artists: "), new Text(artistsString));
            
            eventVBox.getChildren().addAll(backButton, pictureButton, eventHBox, locationHBox, dateHBox, artistsHBox);
            
            
        }
        
        //adds images to eventVBox
        for(Image image : images){
            
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(vizPane.getWidth());
            imageView.setPreserveRatio(true);
                eventVBox.getChildren().add(imageView);
            }
    }
    
    //Loads the main event screen
    @Override
    public void update(ArrayList<Event> events){
        eventVBox.getChildren().clear();
        
        //adds a button with the event name for each event
        events.forEach((event) -> {
            Button eventButton;
            eventVBox.getChildren().add(eventButton = new Button(event.getName()));
            eventButton.prefWidthProperty().bind(vizPane.widthProperty());
            eventButton.setOnAction((new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    open(event);
                    
                }
            }));});

    }
    
    //clears the anchorpane
    @Override
    public void end(){
        vizPane.getChildren().clear();
    }
    
    //returns name of visualizer
    @Override
    public String getName(){
        return name;
    }
    
    //allows user to choose picture and adds it to event
    public void addPictures(Event event){
        FileChooser chooser = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("Image Files", "*.png", "*.jpg");
        chooser.getExtensionFilters().add(filter);
        
        List<File> fileList = chooser.showOpenMultipleDialog(null);
        
        if(fileList != null){
        for(File file : fileList){
            event.addPicture(file);
        }
        open(event);
        }
    }
}
