/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import static controllers.LoginPageController.globalUser;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import definitions.User;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import utils.DataProvider;

/**
 * Main Menu Class of the application, this will present the choices that the user
 * can select to create users, update and delete users.  the user can create global appointments
 * for the consultants throughout the consulting firm's regions. also update and delet those 
 * appointments.
 * @author SHIPLEYM
 */
public class WelcomeMenuController implements Initializable {

    ObservableList<User> singleUser = FXCollections.observableArrayList();
    
    @FXML
    private Label consultNameTxt;
    @FXML
    private Button logOff;
    
    Parent scene;
    Stage stage;

    private final DateTimeFormatter localDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId localTZ = ZoneId.systemDefault();

    /**
     * Initializes the Welcome Menu controller class.
     * and presents the user from the login screen
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultNameTxt.setText(globalUser);
    }   

    /**
     * recallUser
     * function to transport user name to welcome menu
     * @param globalUser 
     */
    public void recallUser(String globalUser){
        
        consultNameTxt.setText(globalUser);
    }
    
    /**
     * onActionCallAppointments action event
     * menu choice to open the appointment page, action called when the Appointments button pressed
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionCallAppointments(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/screens/AppointmentPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * onActionCallCust action event
     * to open the customer page, action called when the Customers button pressed
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionCallCust(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/screens/CustomerPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * onActionCallReports action event
     * to open the reports page, action called when the Reports button pressed
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionCallReports(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/screens/ReportsPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * onActionLoginPage action event
     * to open the Login Screen page, action called when the LogOff button pressed
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionLoginPage(ActionEvent event) throws IOException {
        /**
         * two Lamba expressions are used to complete the choice to leave the appointments 
         * screen
         */
        Alert warnUser = new Alert(Alert.AlertType.CONFIRMATION);
            warnUser.setTitle("Confirm Logout");
            warnUser.setHeaderText("Are you sure you want logout?");
            warnUser.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> 
                {stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            try {
                scene = FXMLLoader.load(getClass().getResource("/screens/LoginPage.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(WelcomeMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
            stage.setScene(new Scene(scene));
            stage.show();}
            );

    }
    
    /**
     * getUserName method
     * routine to find the user's name from their ID
     * @param singleID 
     */
    public void getUserName(int singleID){
       singleUser.clear();
       DataProvider.getAllUsers().stream().filter((u) -> (u.getUserID() == singleID)).forEachOrdered((u) -> {
           singleUser.add(u);
        });
   }
   
}

