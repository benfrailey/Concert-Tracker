/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcfdkpconcerttracker;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author benfrailey
 */
public class MainSceneController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML 
    private ScrollPane scroll;
    
    @FXML
    private AnchorPane vizPane;
    
    @FXML
    private Menu visualizerMenu;
        
    private MainModel main;
    
    private ArrayList<Visualizer> visualizers;
    private Visualizer currentVisualizer;
    
    //initializes scene
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        main = new MainModel();

        //creates an arraylist of visualizers and adds both visualizers
        visualizers = new ArrayList<>();
        visualizers.add(new ButtonVisualizer());
        visualizers.add(new AccordionVisualizer());
        
        //creates a menu option for each visualizer
        for(Visualizer visualizer : visualizers) {
            MenuItem menuItem = new MenuItem(visualizer.getName());
            menuItem.setUserData(visualizer);
            menuItem.setOnAction((ActionEvent event) -> {
                selectVisualizer(event);
            });
            visualizerMenu.getItems().add(menuItem);
        }
        currentVisualizer = visualizers.get(0);
        currentVisualizer.start(vizPane, main.getEvents());
        
        //formats scrollpane
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }    
    
    //selects visualizer
    private void selectVisualizer(ActionEvent event){
        MenuItem menuItem = (MenuItem)event.getSource();
        Visualizer visualizer = (Visualizer)menuItem.getUserData();
        changeVisualizer(visualizer);
    }
    
    //changes visualizer
    private void changeVisualizer(Visualizer visualizer){
        if (currentVisualizer != null) {
            currentVisualizer.end();
        }
        currentVisualizer = visualizer;
        currentVisualizer.start(vizPane, main.getEvents());
    }
    
    //adds event
    @FXML
    private void handleAddEventAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Add Event...");
        alert.setHeaderText("Select whether you're adding a concert or a festival.");
        alert.setContentText("Choose your option.");
                
        //user chooses to add either a festival or a concert
        ButtonType buttonTypeOne = new ButtonType("Concert");
        ButtonType buttonTypeTwo = new ButtonType("Festival");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
        
        //adds either a concert or festival based on user input
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            addConcert();
        } else if (result.get() == buttonTypeTwo) {
           addFestival();
        } else {
    // ... user chose CANCEL or closed the dialog
    }

    }
    
    //adds a concert
    private void addConcert(){
        Dialog<ButtonType> dialog = new Dialog<>();

        //formats input window
        dialog.setTitle("Add Concert");
        dialog.setHeaderText("Fill in the information below to add a new concert");
        
        ButtonType addButtonType = new ButtonType("Add Concert", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField artistText = new TextField();
        artistText.setPromptText("Artist");

        TextField locationText = new TextField();
        locationText.setPromptText("Location");

        TextField dateText = new TextField();
        dateText.setPromptText("Date");

        grid.add(new Label("Artist:"), 0, 0);
        grid.add(artistText, 1, 0);
        grid.add(new Label("Location:"), 0, 1);
        grid.add(locationText, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(dateText, 1, 2);

        
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        //disables addbutton until final field is filled in
        dateText.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        
        //displays dialog
        Optional<ButtonType> result = dialog.showAndWait();
        
        //tests result if user didn't press cancel
        if(result.get() != ButtonType.CANCEL){
            //if a field is blank gives error
            if(artistText.getText().equals("") || locationText.getText().equals("") || dateText.getText().equals("")){
                eventError();
            }
            
            //creates a concert and updates the visualizer
            else{
            main.addConcert(artistText.getText(), locationText.getText(), dateText.getText());
            currentVisualizer.update(main.getEvents());
            }
        }
    }
    
    //adds a festival
    private void addFestival(){
        Dialog<ButtonType> dialog = new Dialog<>();
        
        //formats input window
        dialog.setTitle("Add Festival");
        dialog.setHeaderText("Fill in the information below to add a new festival. Put one artist per line in the \"Artists\" box");

        ButtonType addButtonType = new ButtonType("Add Festival", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextArea artistsText = new TextArea();
        artistsText.setPromptText("Artists");

        TextField eventNameText = new TextField();
        eventNameText.setPromptText("Event Name");

        TextField locationText = new TextField();
        locationText.setPromptText("Location");

        TextField dateText = new TextField();
        dateText.setPromptText("Date");

        grid.add(new Label("Artists:"), 0, 0);
        grid.add(artistsText, 1, 0);
        grid.add(new Label("Event Name:"), 0, 1);
        grid.add(eventNameText, 1, 1);
        grid.add(new Label("Location:"), 0, 2);
        grid.add(locationText, 1, 2);
        grid.add(new Label("Date:"), 0, 3);
        grid.add(dateText, 1, 3);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        //disables button until final field is filled
        dateText.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        
        //displays dialog
        Optional<ButtonType> result = dialog.showAndWait();

        //creates a festival if user didn't press cancewl
        if(result.get() != ButtonType.CANCEL){
            //if a field is blank gives error
            if(artistsText.getText().equals("") || eventNameText.getText().equals("") || locationText.getText().equals("") || dateText.getText().equals("")){
                eventError();
            }
            
            //creates a festival and updates visualizer
            else{
                main.addFestival(artistsText.getText(), eventNameText.getText(), locationText.getText(), dateText.getText());
                currentVisualizer.update(main.getEvents());
            }
        }
    }
    
    //displays error when event cannot be created
    public void eventError(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Event Creation Error!");
        alert.setHeaderText("Please try again!");
        alert.setContentText("Unable to create event because information may have not been entered correctly.");
        
        alert.showAndWait();
    }
}
