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
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 *
 * @author Ben Frailey
 */
public class AccordionVisualizer implements Visualizer{
    private final String name = "Accordion Visualizer";
    
    private AnchorPane vizPane;
    private VBox eventVBox = new VBox();
    private ArrayList<Event> events;
    
    //initializes the visualizer
    @Override
    public void start(AnchorPane vizPane, ArrayList<Event> events){
        this.vizPane = vizPane;
        this.events = events;

        //Adds primary VBox to the anchorPane so that it can display
        vizPane.getChildren().add(eventVBox);
        
        update(events);
    }
    
    //simply clears the anchorpane
    @Override
    public void end(){
        vizPane.getChildren().clear();
    }
    
    //updates the visualizer
    @Override
    public void update(ArrayList<Event> events){
        eventVBox.getChildren().clear();
        
        
        
        //for each event, create an accordion 
        for(Event event : events){
            Accordion eventAccordion;
            VBox accordionVBox = new VBox();
            ArrayList<Image> images = event.getPictures();
            
            Button pictureButton = new Button("Add Picture");
            pictureButton.prefWidthProperty().bind(vizPane.widthProperty().subtract(25));
            pictureButton.setOnAction((new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    addPictures(event);
            }}));
            
            accordionVBox.getChildren().add(pictureButton);
            //concerts and festivals display different info, so it first checks whether it's a festival or concert
            if(event.getType() == EventType.CONCERT){
                HBox artistHBox = new HBox();
                HBox locationHBox = new HBox();
                HBox dateHBox = new HBox();
            
                //assigns info to each HBox to be displayed
                artistHBox.getChildren().addAll(new Text("Artist: "), new Text(event.getName()));
                locationHBox.getChildren().addAll(new Text("Location: "), new Text(event.getLocation()));
                dateHBox.getChildren().addAll(new Text("Date: "), new Text(event.getDate()));
            
                //adds hboxes into accordion
                accordionVBox.getChildren().addAll(artistHBox, locationHBox, dateHBox);
            }
            else{
                Festival festival = (Festival) event;
                
                //translates artists from arraylist to string
                ArrayList<String> artists = festival.getartists();
                String artistsString = "";
                for(String artist : artists)
                    artistsString = artistsString.concat(artist + "\n");
                
                HBox eventHBox = new HBox();
                HBox locationHBox = new HBox();
                HBox dateHBox = new HBox();
                HBox artistsHBox = new HBox();
            
                //assigns info to each HBox to be displayed           
                eventHBox.getChildren().addAll(new Text("Event: "), new Text(event.getName()));
                locationHBox.getChildren().addAll(new Text("Location: "), new Text(event.getLocation()));
                dateHBox.getChildren().addAll(new Text("Date: "), new Text(event.getDate()));
                artistsHBox.getChildren().addAll(new Text("Artists: "), new Text(artistsString));

                //adds hboxes into accordion
                accordionVBox.getChildren().addAll(eventHBox, locationHBox, dateHBox, artistsHBox);

            }
            
            //adds images into accordion
            for(Image image : images){
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(vizPane.getWidth() - 25);
                imageView.setPreserveRatio(true);
                accordionVBox.getChildren().add(imageView);
            }
            //adds all the info into the accordion
            eventVBox.getChildren().add(eventAccordion = new Accordion(new TitledPane(event.getName(), accordionVBox)));
            eventAccordion.prefWidthProperty().bind(vizPane.widthProperty());
        }
    }
    
    //returns the name to be displayed
    @Override
    public String getName(){
        return name;
    }
    
    //allows user to choose a picture and adds it to pictures
    public void addPictures(Event event){
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg");
        chooser.getExtensionFilters().add(filter);
        
        List<File> fileList = chooser.showOpenMultipleDialog(null);
        
        if(fileList != null){
        for(File file : fileList){
            event.addPicture(file);
        }
        update(events);
        }
    }
}
