/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import static dao.AppointmentDao.deleteAppointment;
import static dao.AppointmentDao.getAllAppointments;
import definitions.Appointments;
import definitions.Customers;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SHIPLEYM
 * This class is used to present the defined appointments created by the add appointment page
 * and updated by the update appointment page
 */
public class AppointmentPageController implements Initializable {
    /**
     * 
     * created the fields and values used on the appoinment list screen
     * the appointments are listed in order by date
     */
    Parent scene;
    Stage stage;

    @FXML
    private TableView<Appointments> apptTableView;

    @FXML
    private TableColumn<Appointments, String> idApptClmn;
    
    @FXML
    private TableColumn<Appointments, String> titleApptClmn;

    @FXML
    private TableColumn<Appointments, String> descApptClmn;

    @FXML
    private TableColumn<Appointments, String> locApptClmn;

    @FXML
    private TableColumn<Appointments, String> consultantApptClmn;

    @FXML
    private TableColumn<Appointments, String> typeApptClmn;

    @FXML
    private TableColumn<Appointments, ZonedDateTime> startApptClmn;

    @FXML
    private TableColumn<Appointments, LocalDateTime> endApptClmn;
    
    @FXML
    private TableColumn<Appointments, Customers> customerApptClmn;

    @FXML
    private RadioButton weekRadioBtn;

    @FXML
    private RadioButton monthRadioBtn;
    
    @FXML
    private ToggleGroup apptToggleGrp;


    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId newzid = ZoneId.systemDefault();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * Initializes the Appointment Page controller class.
     * Sets Initial TableView Data entering into the Appointment table
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<Appointments> apptData = getAllAppointments();
        
        idApptClmn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleApptClmn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descApptClmn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locApptClmn.setCellValueFactory(new PropertyValueFactory<>("location"));
        consultantApptClmn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeApptClmn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startApptClmn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endApptClmn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerApptClmn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        
        apptTableView.setItems(apptData);
    }    

    /**
     * 
     * handleNewAppt action calls the AppointmentAddPage
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleNewAppt(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/screens/AppointmentAddPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * handleEditAppt action calls the AppoinmentEditPage
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleEditAppt(ActionEvent event) throws IOException {
        Appointments selectedAppointment = apptTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            // if the user has selected an appointment, then we want to pass the fields of the selected appointment to the 
            // next screen so the recalApppointment function is called from the EditAppointmentPage Controller class
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/screens/AppointmentEditPage.fxml"));
            Object load = loader.load();
            
            AppointmentEditPageController getAppointment = loader.getController();
            getAppointment.recallAppointment(selectedAppointment);
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Appointment selected");
            alert.setContentText("Please select an Appointment in the Table");
            alert.showAndWait();
        }
    }

    /**
    * handleDeleteAppt action event method to delete the selected appointment
    * @param event 
    */
    @FXML
    private void handleDeleteAppt(ActionEvent event) {
        Appointments selectedAppointment = apptTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            //delete the selected appointment and refreshe the screen
            int selectedAppointmentId = apptTableView.getSelectionModel().getSelectedItem().getAppointmentId();
            String selectedAppointmentType = apptTableView.getSelectionModel().getSelectedItem().getType();
            
            deleteAppointment(selectedAppointmentId, selectedAppointmentType);

            ObservableList<Appointments> apptData = getAllAppointments();

            idApptClmn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            titleApptClmn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descApptClmn.setCellValueFactory(new PropertyValueFactory<>("description"));
            locApptClmn.setCellValueFactory(new PropertyValueFactory<>("location"));
            consultantApptClmn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            typeApptClmn.setCellValueFactory(new PropertyValueFactory<>("type"));
            startApptClmn.setCellValueFactory(new PropertyValueFactory<>("start"));
            endApptClmn.setCellValueFactory(new PropertyValueFactory<>("end"));
            customerApptClmn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

            apptTableView.setItems(apptData);
        
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Appointment selected");
            alert.setContentText("Please select an Appointment in the Table");
            alert.showAndWait();
        }

    }

    /**
     * handleApptWeek action event presents a list of appointments of the upcoming week
     * from today(date the program is running)
     * @param event 
     */
    @FXML
    private void handleApptWeek(ActionEvent event) {
        ObservableList<Appointments> apptData = getAllAppointments();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime week = now.plusWeeks(1);
        
        /**
         * A Lambda Expression, used to cycle through a filtered ObservableList
         * of a Week of Appointments
         **/
        FilteredList<Appointments> filter = new FilteredList<>(apptData);
        filter.setPredicate(row -> {

            LocalDateTime startDT = LocalDateTime.parse(row.getStart(), formatter);

            return startDT.isAfter(now.minusDays(1)) && startDT.isBefore(week);
        });
        
        apptTableView.setItems(filter);
    }

    /**
     * handleApptMonth action event presents all appointments for the upcoming month
     * from today forward
     * @param event 
     */
    @FXML
    private void handleApptMonth(ActionEvent event) {
        ObservableList<Appointments> apptData = getAllAppointments();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime month = now.plusMonths(1);
        
        /** 
         * A Lambda Expression, used to cycle through the filtered ObservableList 
         * of a Month of Appointments
         **/
        FilteredList<Appointments> filter = new FilteredList<>(apptData);
        filter.setPredicate(row -> {

            LocalDateTime startDT = LocalDateTime.parse(row.getStart(), formatter);

            return startDT.isAfter(now.minusDays(1)) && startDT.isBefore(month);
        });
        
        apptTableView.setItems(filter);
    }

    /**
     * onActionCallCust action event will switch to the customer screen
     * when the button is pressed
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionCallCust(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/screens/CustomerPage.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * onActionCallReports action event used to switch to the reports screens
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionCallReports(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/screens/ReportsPage.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * onActionReturnMenu action event calls a switch back to the mainmenu screen
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onActionReturnMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/screens/WelcomeMenu.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    
    /**
     * Sets fields from selectedAppointment to be processed on the focus page
     * @param selectedAppointment 
     */
    @FXML
    private void showAppointmentDetails(Appointments selectedAppointment) {
     selectedAppointment = apptTableView.getSelectionModel().getSelectedItem();

    }
}
