/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import static controllers.LoginPageController.globalUser;
import static dao.CountriesDao.getCountryList;
import static dao.LocationDao.getLocationList;
import definitions.Countries;
import definitions.FirstLevelDivisions;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DataProvider;
import utils.LoggerUtil;

/**
 * FXML Controller class
 *
 * @author SHIPLEYM
 * This class is the Add Customer Controller Class,
 * this is a branch from the Customer screen so that
 * users/DataEntrypersone can add new customers and user the variables
 * needed to render this screen
 * Start by creating a list for the department,
 * and then the fields that display the data On the add customer page
 */
public class AddCustomerPageController implements Initializable {

    ObservableList<FirstLevelDivisions> filteredDivisions = FXCollections.observableArrayList();
    
    Parent scene;
    Stage stage;
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private final static Logger LOGGER = Logger.getLogger(LoggerUtil.class.getName());

    /**
     * when the country has been chosen from the screen field,
     * take action to filter the divisions for selection
     */
    @FXML
    void onActionSelectCountry(ActionEvent event) {
        filterDivisions();
    }

    /**
     * method used to cancel the creation of a new user.  if the cancel button is pressed 
     * an alert is presented to make sure the user is ready to cancel and return 
     * to the customer screen
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleCancelCustomer(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        alert.showAndWait()
        .filter(response -> response == ButtonType.OK)
            .ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType response) {
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                try {
                    scene = FXMLLoader.load(AddCustomerPageController.this.getClass().getResource("/screens/CustomerPage.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();        
                }catch (IOException ex) {
                    Logger.getLogger(AddCustomerPageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
                
    }

    /**
     * Method for saving all changes entered by the user on the "Add Customer Screen Page"
     */
    @FXML
    void handleSaveCustomer(ActionEvent event) throws IOException {

        if(customerNmFld.getText().isEmpty() || addressFld.getText().isEmpty()|| postalCdFld.getText().isEmpty() ||
           phoneFld.getText().isEmpty()) {
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setContentText("All Fields are Required.  Please ensure no fields have been left blank.");
            alert.showAndWait();
        }
        else {
            LocalDateTime now = LocalDateTime.now();
            ZonedDateTime currentUTC = now.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));

            String sCurrentDT = currentUTC.toLocalDateTime().toString().replace("T", " ");

            try {
                    PreparedStatement pscust = DBConnection.getConn().prepareStatement("INSERT INTO customers "
                    + "(customer_Name, address, postal_code, phone, create_Date, created_By, last_Update, last_Updated_By, division_id )"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

                    pscust.setString(1, customerNmFld.getText());
                    pscust.setString(2, addressFld.getText());
                    pscust.setString(3, postalCdFld.getText());
                    pscust.setString(4, phoneFld.getText());
                    pscust.setString(5, sCurrentDT);
                    pscust.setString(6, globalUser);

                    pscust.setString(7, sCurrentDT);
                    pscust.setString(8, globalUser);
                    pscust.setInt(9, divisionCB.getSelectionModel().getSelectedItem().getDivisionID());
                    int result = pscust.executeUpdate();
                    if (result == 1) {
                        System.out.println("Good! New Customer Saved");
                        LOGGER.log(Level.INFO, "New Cusomter Saved {0}", customerNmFld.getText());
                    } else {
                        System.out.println("Sorry! New Customer Erred");
                        LOGGER.log(Level.INFO, "New Cusomter Erred {0}", customerNmFld.getText());
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
     * Initialize the Add Customer controller class. 
     * Fill in the country/region combo box on the Add Customer Page Screen
     * Then after selecting the country, the process of filtering departments by country is Processed
     * and then the method can fill in the location/department combo box
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        

        ObservableList<Countries> countryData = getCountryList();
        countryCB.setItems(countryData);
        countryCB.getSelectionModel().selectFirst();
        filterDivisions();

    }    
    
    /**
     * The method created to filter the divisions after the user selects the country 
     */
    public void filterDivisions(){
        filteredDivisions.clear();
        int country_ID = countryCB.getSelectionModel().getSelectedItem().getCountryId();
        for(FirstLevelDivisions fld : DataProvider.getAllDivisions()){
            if(country_ID == fld.getCountryId()){
                filteredDivisions.add(fld);
            }
                
        }
        divisionCB.setItems(filteredDivisions);
        divisionCB.getSelectionModel().selectFirst();

        
    }
    
}
