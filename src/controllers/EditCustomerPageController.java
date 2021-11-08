/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import static controllers.LoginPageController.globalUser;
import dao.CountriesDao;
import static dao.CountriesDao.getCountryList;
import definitions.Countries;
import definitions.Customers;
import definitions.FirstLevelDivisions;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DataProvider;

/**
 * FXML EditCustomerPage Controller class
 *
 * @author SHIPLEYM
 */
public class EditCustomerPageController implements Initializable {

    ObservableList<FirstLevelDivisions> filteredDivisions = FXCollections.observableArrayList();
    ObservableList<Countries> countryData = FXCollections.observableArrayList();
    
    Stage stage;
    Parent scene;
    
    @FXML
    private TextField customerIDFld;
    @FXML
    private TextField customerNmFld;
    @FXML
    private TextField addressFld;
    @FXML
    private ComboBox<Countries> countryCB;
    @FXML
    private ComboBox<FirstLevelDivisions> divisionCB;
    @FXML
    private TextField postalCdFld;
    @FXML
    private TextField phoneFld;

    private final ZoneId zid = ZoneId.systemDefault();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final DateTimeFormatter dateDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    /**
     * Initializes the EditCustomoerPage controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // entry point, all initialzaions have been used in different functions
    }    

    /**
     * handleSaveCustomer action event
     * save the updates to the customer edit page, verify if all the fields have been filled properly
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleSaveCustomer(ActionEvent event) throws IOException {
        if(customerNmFld.getText().isEmpty() || addressFld.getText().isEmpty()|| postalCdFld.getText().isEmpty() ||
           phoneFld.getText().isEmpty() || countryCB.getSelectionModel().getSelectedItem().getCountry().isEmpty() ||
           divisionCB.getSelectionModel().isEmpty()){
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setContentText("All Fields are Required.  Please ensure no fields have been left blank.");
            alert.showAndWait();
        }
        else {
            LocalDateTime now = LocalDateTime.now();
            ZonedDateTime currentUTC = now.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));

            Timestamp currentsqlts = Timestamp.valueOf(currentUTC.toLocalDateTime()); 

            try {
                    PreparedStatement pscust = DBConnection.getConn().prepareStatement("UPDATE customers "
                    + "SET customer_Name = ?, address = ?, postal_code = ?, phone = ?, last_Update = ?, last_Updated_By = ?, division_id = ? "
                    + "WHERE customer_ID = ?");

                    pscust.setString(1, customerNmFld.getText());
                    pscust.setString(2, addressFld.getText());
                    pscust.setString(3, postalCdFld.getText());
                    pscust.setString(4, phoneFld.getText());
                    pscust.setTimestamp(5, currentsqlts);
                    pscust.setString(6, globalUser);

                    pscust.setInt(7, divisionCB.getSelectionModel().getSelectedItem().getDivisionID());
                    pscust.setString(8, customerIDFld.getText());
                    int result = pscust.executeUpdate();
                    if (result == 1) {//good insert, one row was affected;
                        System.out.println("Good! Customer Upated");
                    } else {
                        System.out.println("Sorry! Update Customer Erred");
                    }

                } catch (SQLException ex) {
                ex.printStackTrace();
                }
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/screens/CustomerPage.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();  
        }
    }

    /**
     * recallCustomer
     * function is called by the customer screen so that the selected customer information 
     * can be passed to the edit customer page 
     * @param selectedCustomer 
     */
    public void recallCustomer(Customers selectedCustomer) {
        
           
        customerIDFld.setText(Integer.toString(selectedCustomer.getCustomerId()));
        customerNmFld.setText(selectedCustomer.getCustomerName());
        addressFld.setText(selectedCustomer.getAddress());
        postalCdFld.setText(selectedCustomer.getPostalCode());
        phoneFld.setText(selectedCustomer.getPhone());
        filterDivisions(selectedCustomer.getCountryId());
        countryData = CountriesDao.getCountryList();
        Countries country = null;
        for(Countries c : countryData){
            if(c.getCountryId() == selectedCustomer.getCountryId()){
                country = c;
                break;
            }
        }
        countryCB.getSelectionModel().select(country);
        divisionCB.setItems(filteredDivisions);
        for(FirstLevelDivisions fld : filteredDivisions){
            if(fld.getDivisionID() == selectedCustomer.getDivisionID()){
                divisionCB.getSelectionModel().select(fld);
                break;
            }
        }
        
    }
    
    /**
     * handleCancelCustomer action event
     * Cancel update and confirm customer update
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleCancelCustomer(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        alert.showAndWait()
        .filter(response -> response == ButtonType.OK)
            .ifPresent((ButtonType response) -> {
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                try {
                    scene = FXMLLoader.load(EditCustomerPageController.this.getClass().getResource("/screens/CustomerPage.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();        
                }catch (IOException ex) {
                    Logger.getLogger(EditCustomerPageController.class.getName()).log(Level.SEVERE, null, ex);
                }
        });
                
    }
    
    /**
     * filterDivisions method created to
     * filter out the divisions after the selection of country
     * @param fkCountryID 
     */
    public void filterDivisions(int fkCountryID){
        filteredDivisions.clear();
        for(FirstLevelDivisions fld : DataProvider.getAllDivisions()){
            if(fld.getCountryId() == fkCountryID){
                filteredDivisions.add(fld);
            }
                
        }
        
    }
    
}
