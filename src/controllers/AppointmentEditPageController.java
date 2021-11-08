/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import static dao.AppointmentDao.getAllAppointments;
import static dao.AppointmentDao.getSimilarAppointments;
import static dao.ContactsDao.getContactList;
import dao.CountriesDao;
import static dao.CountriesDao.getCountryList;
import static dao.CustomerDao.getAllCustomers;
import definitions.Appointments;
import definitions.Contacts;
import definitions.Countries;
import definitions.Customers;
import definitions.FirstLevelDivisions;
import definitions.User;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DataProvider;
import utils.LoggerUtil;

/**
 * FXML Controller class
 *
 * @author SHIPLEYM
 * This class is the AppointmentEditPageController class to edit/update appointments  
 * the user must select the appointment to change on the appointment screen 
 * after the appointment has bee selected and the save the button pushed
 * two calls will be made to transfer the data to this routine, 1 to transfer the data,
 * and one to begin using the code
 */
public class AppointmentEditPageController implements Initializable {

    ObservableList<FirstLevelDivisions> filteredDivisions = FXCollections.observableArrayList();
    ObservableList<Countries> countryData = FXCollections.observableArrayList();

    Parent scene;
    Stage stage;

    @FXML
    private TextField appointmentIdFld;

    @FXML
    private Label apptLabel;

    @FXML
    private TextField titleFld;

    @FXML
    private TextField customerNmFld;
    
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

    public int selectedCustID;
    public int selectedUserID;
    public String selectedUser;
    int selectedContactID;

    /**
     * initialize method initialized the AppointmentEditPage controller class.
     * allows time selection on assumption of Business Hours being 8am - 10pm
     * does not allow time selection outside of Business Hours
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        createTypeList();
        
	LocalTime time = LocalTime.of(8, 0);
	do {
		startTimes.add(time.format(timeDTF));
		endTimes.add(time.format(timeDTF));
		time = time.plusMinutes(15);
	} while(!time.equals(LocalTime.of(22, 15)));
		startTimes.remove(startTimes.size() - 1);
		endTimes.remove(0);
        
        startCB.setItems(startTimes);
	endCB.setItems(endTimes);
    }    

    /**
     * recallAppointment method: Sets data fields for passing into the next Form
     * this is the key method to transfer data from the Appointment Page to the Update page
     * current method retrieves the appointment dates and times, then builds them as UTC times so
     * the program is able to compare the selected dates to those in the system already
     * 
     * @param selectedAppointment 
     */
    public void recallAppointment(Appointments selectedAppointment) {

        ObservableList<Contacts> contactList = getContactList();
        ObservableList<Countries> countryData = getCountryList();
        ObservableList<Customers> custData = getAllCustomers();

        appointmentIdFld.setText(Integer.toString(selectedAppointment.getAppointmentId()));
        customerNmFld.setText(selectedAppointment.getCustomerName());

        selectedCustID = selectedAppointment.getCustomerId();
        selectedUserID = selectedAppointment.getUserId();
        selectedUser = selectedAppointment.getUser();
        
        contactCB.setItems(contactList);

        Contacts conTact = null;
        for(Contacts cts : contactList){
            if(cts.getContactID() == selectedAppointment.getContactId()){
                conTact = cts;
                break;
            }
        }
        contactCB.setValue(conTact);
        titleFld.setText(selectedAppointment.getTitle());
        descFld.setText(selectedAppointment.getDescription());
        typeCB.setValue(selectedAppointment.getType());
        
        countryCB.setItems(countryData);

        countryData = CountriesDao.getCountryList();
        Countries country = null;
        for(Countries cnty : countryData){
            if(cnty.getCountryId() == selectedAppointment.getCountryId()){
                country = cnty;
                break;
            }
        }
        countryCB.setValue(country);
 
        filterDivisions();
        locationCB.setItems(filteredDivisions);
        FirstLevelDivisions loCation = null;
        for(FirstLevelDivisions fld : filteredDivisions){
            if(fld.getDivisionID() == selectedAppointment.getLocationId()){
                loCation = fld;
                break;
            }
        }
        locationCB.setValue(loCation);
        
        startCB.setValue(selectedAppointment.getStart());
        endCB.setValue(selectedAppointment.getEnd());

        String gApptDt = startCB.getValue();
        String pApptDt = gApptDt.replace(" ", "T");
        LocalDateTime startApptDt = LocalDateTime.parse(pApptDt);
        
        datePicker.setValue(startApptDt.toLocalDate());
        
        String gEndApptDt = endCB.getValue();
        String pEndApptDt = gEndApptDt.replace(" ", "T");
        LocalDateTime endApptDt = LocalDateTime.parse(pEndApptDt);
        
        ZonedDateTime startApptUTC = startApptDt.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endApptUTC = endApptDt.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));            
        
        startCB.setValue(startApptDt.toLocalTime().format(timeDTF));
        endCB.setValue(endApptDt.toLocalTime().format(timeDTF));
    }
        

    /**
     * handleSelectDivision method filters the divisions at the time of choosing
     * a country
     * @param event 
     */
    @FXML
    private void handleSelectDivision(ActionEvent event) {
        filterDivisions();
    }

    /** 
     * Saving the updates, handleSave method will verify that all fields have been filled in correctly
     * if not the focus will remain on fields not completed and alert the user to check all the fields
     * @param event
     * @throws IOException 
     */
    @FXML
    public void handleSave(ActionEvent event) throws IOException {

        if( titleFld.getText().isEmpty() || contactCB.getSelectionModel().isEmpty()|| 
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

                if(getTimeOverlap(contactCB.getSelectionModel().getSelectedItem().getContactID()) == true) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning!");
                    alert.setContentText("Your Appointment has an overlap with another appointment, please adjust your dates and times and try again.");
                    alert.showAndWait();
                    return;
                }
            
            try {
                    PreparedStatement psappt = DBConnection.getConn().prepareStatement("UPDATE appointments "
                    + "SET title = ?, description = ?, location = ?, type = ?, `start` = ?, `end` = ?, "
                    + "last_Update = CURRENT_TIMESTAMP, last_Updated_By = ?, customer_id = ?, user_id = ?, contact_id = ? "
                    + "WHERE appointment_ID = ?");

                    psappt.setString(1, titleFld.getText());
                    psappt.setString(2, descFld.getText());
                    psappt.setString(3, locationCB.getSelectionModel().getSelectedItem().getDivision());
                    psappt.setString(4, typeCB.getSelectionModel().getSelectedItem());
                    psappt.setString(5, sStartDT);
                    psappt.setString(6, sEndDT);
                    psappt.setString(7, selectedUser);
                    psappt.setInt(8, selectedCustID);
                    psappt.setInt(9, selectedUserID);
                    psappt.setInt(10, contactCB.getSelectionModel().getSelectedItem().getContactID());
                    psappt.setString(11, appointmentIdFld.getText());

                    int result = psappt.executeUpdate();
                    if (result == 1) {
                        System.out.println("Good! Appointment Upated");
                        LOGGER.log(Level.INFO, "Appointment Updated {0}", titleFld.getText());
                    } else {
                        System.out.println("Sorry! Update to Appointment Erred");
                        LOGGER.log(Level.INFO, "Appointment Updated Erred {0}", titleFld.getText());
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
     * if the user cancels the appointment update, this action event, handleCancle, will confirm that the 
     * user definitely wants to cancel the update.  if correct, the user be returned to the main appointment screen
     * else the focus remain on the update screen
     * @param event
     * @throws IOException 
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
    
    /**
     * Method called to determine Overlapping Appointments
     * @param selectedContactID
     * @return 
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
     * createTypeList method used to build the type of appointments list with a set of predefined types
     */
    private void createTypeList() {
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.addAll("Consultation", "New Account", "Follow Up", "Planning Session", "De-Briefing", "Close Account");
        typeCB.setItems(typeList);
    }

    /**
     * filterDivisions method is used when the selection of a country is made 
     * so that the choice of divisions is restricted to the country chosen.
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
    }
}
