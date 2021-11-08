/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import static controllers.LoginPageController.globalUser;
import static controllers.LoginPageController.globalUserID;
import static dao.AppointmentDao.getAllAppointments;
import static dao.AppointmentDao.getSimilarAppointments;
import static dao.ContactsDao.getContactList;
import static dao.CountriesDao.getCountryList;
import static dao.CustomerDao.getCustomerList;
import static dao.LocationDao.getLocationList;
import definitions.Appointments;
import definitions.Contacts;
import definitions.Countries;
import definitions.Customers;
import definitions.FirstLevelDivisions;
import definitions.User;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.EnumSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DataProvider;
import utils.LoggerUtil;

/**
 * FXML Controller class
 *
 * @author SHIPLEYM
 * This class is the controller class for adding new appointments
 */
public class AppointmentAddPageController implements Initializable {

    ObservableList<FirstLevelDivisions> filteredDivisions = FXCollections.observableArrayList();

    Parent scene;
    Stage stage;
    
    @FXML
    private Label apptLabel;

    @FXML
    private TextField titleFld;

    @FXML
    private ComboBox<String> customerCB;
    
    @FXML
    private ComboBox<Contacts> contactCB;
    
    @FXML
    private ComboBox<Countries> countryCB;

    @FXML
    private ComboBox<FirstLevelDivisions> locationCB;
    
    @FXML
    private TextField descFld;
    
    @FXML
    private ComboBox<String> startCB;

    @FXML
    private ComboBox<String> endCB;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> typeCB;

    @FXML
    private Button apptSaveButton;

    @FXML
    private Button apptCancelButton;

    private final ZoneId zid = ZoneId.systemDefault();
    private final ObservableList<String> startTimes = FXCollections.observableArrayList();
    private final ObservableList<String> endTimes = FXCollections.observableArrayList();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final DateTimeFormatter dateDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final static Logger LOGGER = Logger.getLogger(LoggerUtil.class.getName());

    int selectedCustID;
    int selectedContactID;
    
    /**
     * Initializes the Add Appointment Screen controller class.
     * Tasks include filling out all the Combo Boxes for the Add Appointment Screen
     * the customer data, contact/consultant information, and country lists will be extracted from 
     * the database at this point.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<String> custData = getCustomerList();
        ObservableList<Contacts> contactData = getContactList();
        ObservableList<Countries> countryData = getCountryList();
        countryCB.setItems(countryData);
        countryCB.getSelectionModel().selectFirst();

        filterDivisions();
        createTypeList();

        customerCB.setItems(custData);
        contactCB.setItems(contactData);

        /**
         * here we set times based on the idea that Business Hours are 8am - 10pm
         * this does not allow time selection outside of Business Hours
         */
	LocalTime time = LocalTime.of(8, 0);
	do {
		startTimes.add(time.format(timeDTF));
		endTimes.add(time.format(timeDTF));
		time = time.plusMinutes(15);
	} while(!time.equals(LocalTime.of(22, 15)));
		startTimes.remove(startTimes.size() - 1);
		endTimes.remove(0);
        
        datePicker.setValue(LocalDate.now());

        startCB.setItems(startTimes);
	endCB.setItems(endTimes);
	startCB.getSelectionModel().select(LocalTime.of(8, 0).format(timeDTF));
	endCB.getSelectionModel().select(LocalTime.of(8, 15).format(timeDTF));
        
        
    }    
    /**
     * When the user selects a country, this method calls the action to
     * list the divisions/locations of that country 
     * by the country selected
     */
    @FXML
    void handleSelectDivision(ActionEvent event){
        filterDivisions();
    }
    /**
     * as the user Saves the newly created appointment, the system 
     * calls the handleSave action event to confirm that all fields have be filled in correctly
     * if not the code will alert the user to check all the fields
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleSave(ActionEvent event) throws IOException {
        if( titleFld.getText().isEmpty() || contactCB.getSelectionModel().isEmpty()|| customerCB.getSelectionModel().isEmpty()|| 
           countryCB.getSelectionModel().getSelectedItem().getCountry().isEmpty() || locationCB.getSelectionModel().isEmpty() || descFld.getText().isEmpty() || 
           startCB.getSelectionModel().getSelectedItem().isEmpty() || endCB.getSelectionModel().getSelectedItem().isEmpty() || 
           typeCB.getSelectionModel().getSelectedItem().isEmpty()){
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setContentText("All Fields are Required.  Please ensure no fields have been left blank.");
            alert.showAndWait();
        }
        else {
        
        LocalDate localDate = datePicker.getValue();
	LocalTime startTime = LocalTime.parse(startCB.getSelectionModel().getSelectedItem(), timeDTF);
	LocalTime endTime = LocalTime.parse(endCB.getSelectionModel().getSelectedItem(), timeDTF);
        
        LocalDateTime startDT = LocalDateTime.of(localDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, endTime);
        
        ZonedDateTime startUTC = startDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));            
	
        String sStartDT = startUTC.toLocalDateTime().toString().replace("T", " ");
        String sEndDT = endUTC.toLocalDateTime().toString().replace("T", " ");

        int chckEndTimeAfterStart = endTime.compareTo(startTime);
        
        if(chckEndTimeAfterStart <= 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setContentText("Appointment End Time must be After Appointment Start Time.");
            alert.showAndWait();
            return;
        }
        
        String selectedContact = contactCB.getSelectionModel().getSelectedItem().getContactName();
        
        try {
            PreparedStatement cnst = DBConnection.getConn().prepareStatement("select contact_ID from contacts where Contact_Name = ?" );
            
            cnst.setString(1, selectedContact);
            ResultSet result = cnst.executeQuery();
            while (result.next()) {
             selectedContactID = result.getInt("contact_Id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentAddPageController.class.getName()).log(Level.SEVERE, null, ex);
        }

        String selectedCustomer = customerCB.getSelectionModel().getSelectedItem();
        
        try {
            PreparedStatement cst = DBConnection.getConn().prepareStatement("select Customer_ID from customers where Customer_Name = ?" );
            
            cst.setString(1, selectedCustomer);
            ResultSet result = cst.executeQuery();
            while (result.next()) {
             selectedCustID = result.getInt("customer_Id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentAddPageController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(getTimeOverlap(selectedContactID) == true) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setContentText("Your Appointment has an overlap with another appointment, please adjust your dates and times and try again.");
            alert.showAndWait();
            return;
        }
        
        try {

                PreparedStatement pst = DBConnection.getConn().prepareStatement("INSERT INTO appointments "
                + "(title, description, location, type, start, end, create_Date, created_By, last_Update, last_Updated_By, customer_ID, user_Id, contact_Id) "
                + "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?)");
            
                pst.setString(1, titleFld.getText());
                pst.setString(2, descFld.getText());
                pst.setString(3, locationCB.getSelectionModel().getSelectedItem().getDivision());
                pst.setString(4, typeCB.getValue());
                pst.setString(5, sStartDT);
                pst.setString(6, sEndDT);
                pst.setString(7, globalUser);
                pst.setString(8, globalUser);
                pst.setInt(9, selectedCustID); //customerCB.getValue());
                pst.setInt(10, globalUserID);//1);.getUserID() //
                pst.setInt(11, selectedContactID); //contactCB.getValue());
                int result = pst.executeUpdate();
                if (result == 1) {//one row was affected; namely the one that was inserted!  Log good or bad to the log file
                    System.out.println("Good! New Appointment Saved");
                        LOGGER.log(Level.INFO, "New Appointment Saved {0}", titleFld.getText());
                } else {
                    System.out.println("Nope! New Appointment Erred");
                        LOGGER.log(Level.INFO, "New Appointment Erred {0}", titleFld.getText());
                }
            } catch (SQLException ex) {
            ex.printStackTrace();
            }
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/screens/AppointmentPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
        }
    }

    /**
     * filterDivisions method parses out the divisions to the field on the screen as
     * the user makes a selection of country so that they don't need
     * to chose the firstleveldivisions from the entire list
     */
    public void filterDivisions(){
        filteredDivisions.clear();
        int country_ID = countryCB.getSelectionModel().getSelectedItem().getCountryId();
        for(FirstLevelDivisions fld : DataProvider.getAllDivisions()){
            if(country_ID == fld.getCountryId()){
                filteredDivisions.add(fld);
            }
                
        }
        locationCB.setItems(filteredDivisions);
        locationCB.getSelectionModel().selectFirst();

        
    }
    /**
     * creatTypeList method
     * Populates a type list with predefined types
     */
    private void createTypeList() {
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.addAll("Consultation", "New Account", "Follow Up", "Planning Session", "De-Briefing", "Close Account");
        typeCB.setItems(typeList);
    }

    /**
     * getTimeOverlap Method is used to determine Overlapping Appointments
     * the start date and end dates are selected from each appointment in the appointment list 
     * database and compared with the new changes on the AppoinmentUpdate screen
     */
    private boolean getTimeOverlap(int selectedContactID) {
        
        ObservableList<Appointments> aList = getAllAppointments();

        for(Appointments a : aList) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime aListStartDT = LocalDateTime.parse(a.getStart(), formatter);
            LocalDateTime aListEndDT = LocalDateTime.parse(a.getEnd(), formatter);

            LocalDate selectedStartDate = datePicker.getValue();
            LocalDate selectedEndDate = datePicker.getValue();
            

            String selectedStartTime = startCB.getSelectionModel().getSelectedItem().substring(0, 5).trim();

            if (selectedStartTime.length() < 5)
            {
                selectedStartTime = "0" + selectedStartTime;
            }
            LocalTime ltStParsed = LocalTime.parse(selectedStartTime);
            
            String selectedEndTime = endCB.getSelectionModel().getSelectedItem().substring(0, 5).trim();

            if (selectedEndTime.length() < 5)
            {
                selectedEndTime = "0" + selectedEndTime;
            }
            LocalTime ltEndParsed = LocalTime.parse(selectedEndTime);

            LocalDate ldtStartDT = LocalDate.of(selectedStartDate.getYear(), selectedStartDate.getMonth(), selectedStartDate.getDayOfMonth());
            LocalDate ldtEndDT = LocalDate.of(selectedEndDate.getYear(), selectedEndDate.getMonth(), selectedEndDate.getDayOfMonth());
            
            LocalDateTime compStartDt = LocalDateTime.of(ldtStartDT, ltStParsed);
            LocalDateTime compEndDt = LocalDateTime.of(ldtEndDT, ltEndParsed);
            
            if(selectedContactID == a.getContactId()){
                int startCompare = compStartDt.compareTo(aListStartDT);
                int startEndCompare = compStartDt.compareTo(aListEndDT);
                int endCompare = compEndDt.compareTo(aListEndDT);
                int endStartCompare = compEndDt.compareTo(aListStartDT);

                if(startCompare >= 0 && startEndCompare <= 0 || endCompare <= 0 && endStartCompare >= 0)
                   return true; 
                }
        }
    return false;
    }

    /**
     * if the user cancels the new appointment, this action event handleCancel will confirm that the user really 
     * wants to cancel the update.  if true they will be returned to the main appointment screen
     * or the focus will remain on the update screen
     */
    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> 
                {stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            try {
                scene = FXMLLoader.load(getClass().getResource("/screens/AppointmentPage.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(WelcomeMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        stage.setScene(new Scene(scene));
        stage.show();}
        );
    }
    
}
