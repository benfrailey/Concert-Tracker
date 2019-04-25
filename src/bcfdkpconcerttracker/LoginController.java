/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcfdkpconcerttracker;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author benfrailey
 */
public class LoginController implements Initializable {
    
    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private TextField username;
    
    @FXML
    private TextField password;
    
    @FXML
    private Text errorText;
    
    private LoginModel login;
    
    //initializes login screen
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        login = new LoginModel();
        errorText.setText("");
    } 
    
    //triggers when sign in button is clicked, tests login info and signs in to account
    @FXML
    private void handleSignInButtonAction(ActionEvent event) throws IOException {
        //reads the username and password textboxes
        String usernameInput = username.getText();
        String passwordInput = password.getText();
        
        //tests login info
        int check = login.testLoginInfo(usernameInput, passwordInput);
        
        //uses result of testlogin info to determine what to do
        switch(check) {
            
            //successfully signs in and goes to main screen
            case 2:
                Stage stage = (Stage)rootPane.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
                
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            break;
        
            //doesnt recognize login
            case 1:
                errorText.setText("Invalid Username/Password combination. \nHaven't created an account yet? Create one now!");
                break;
        
            //text field is empty
            case 0:
                errorText.setText("Username or Password field is empty.");
                break;
        }
    }
    
    //checks login info and creates account
    @FXML
    private void handleCreateAccountButtonAction(ActionEvent event){
        //reads username and password info from textboxes
        String usernameInput = username.getText();
        String passwordInput = password.getText();
        
        //checks new account info
        int check = login.checkNewAccount(usernameInput, passwordInput);
        
        //creates account or fails depending on check
        switch (check) {
            
            //successfully creates account
            case 2:
                login.createAccount(usernameInput, passwordInput);
                    errorText.setText("Account Created! Sign in to continue.");
                break;
                
            //username is used
            case 1:
                errorText.setText("Username \"" + usernameInput + "\" already exists. Please choose new Username");
                break;
                
            //text field is empty
            case 0:
                errorText.setText("Username or Password field is empty.");
                break;
            default:
                break;
        }
    }
    
    @FXML
    private void handleHelpButtonAction(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help Page");
        alert.setHeaderText("Here's some useful information about the application");
        alert.setContentText(login.getHelp());
        
        alert.showAndWait();
    }
}