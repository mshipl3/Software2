/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import definitions.AppointmentReport;
import definitions.Appointments;
import definitions.Customers;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.DBConnection;

/**
 * FXML ReportsPage Controller class
 *
 * @author SHIPLEYM
 */
public class ReportsPageController implements Initializable {

    Parent scene;
    Stage stage;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab schedTab;

    @FXML
    private TableView<Appointments> schedTableView;

    @FXML
    private TableColumn<Appointments, ZonedDateTime> apptSchedClmn;

    @FXML
    private TableColumn<Appointments, ZonedDateTime> startSchedClmn;

    @FXML
    private TableColumn<Appointments, LocalDateTime> endSchedClmn;
    
    @FXML
    private TableColumn<Appointments, String> titleSchedClmn;

    @FXML
    private TableColumn<Appointments, String> typeSchedClmn;

    @FXML
    private TableColumn<Appointments, Customers> customerSchedClmn;
    
    @FXML
    private TableColumn<Appointments, String> contactClmn;

    @FXML
    private Tab apptTab;

    @FXML
    private TableView<AppointmentReport> apptTableView;

    @FXML
    private TableColumn<AppointmentReport, String> monthClmn;

    @FXML
    private TableColumn<AppointmentReport, String> typeClmn;

    @FXML
    private TableColumn<AppointmentReport, String> typeAmount;

    @FXML
    private Tab custTab;
    
    @FXML
    private BarChart barChart;
    
    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private ObservableList<AppointmentReport> apptList;
    private ObservableList<Appointments> schedule;
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId newzid = ZoneId.systemDefault();

    /**
     * Initializes the Reports Page controller class.
     * The reports screen is divided into 3 tabs
     * one report is the schedule of appointments sorted in chronological order by contact
     * as of today (date the program is running and the report page is selected)
     * the second report is the number of reports by type per month
     * and the 3rd report is a bar-chart of customers by division
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        populateSchedule();      
        populateApptTypeList();
        populateCustBarChart();
        
        apptSchedClmn.setCellValueFactory(new PropertyValueFactory<>("startD"));
        startSchedClmn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endSchedClmn.setCellValueFactory(new PropertyValueFactory<>("end"));
        titleSchedClmn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeSchedClmn.setCellValueFactory(new PropertyValueFactory<>("type"));
        customerSchedClmn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        contactClmn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        
        monthClmn.setCellValueFactory(new PropertyValueFactory<>("Month"));
        typeClmn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        typeAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
    }    

    private void populateApptTypeList() {
        apptList = FXCollections.observableArrayList();
        
        try{
            
        PreparedStatement statement = DBConnection.getConn().prepareStatement(
            "SELECT MONTHNAME(`start`) AS \"Month\", type AS \"Type\", COUNT(*) as \"Total\" "
            + "FROM appointments "
            + "GROUP BY MONTHNAME(`start`), type");
            ResultSet rs = statement.executeQuery();
           
            
            while (rs.next()) {
                
                String month = rs.getString("Month");
                
                String type = rs.getString("Type");

                String amount = rs.getString("Total");
                      
                apptList.add(new AppointmentReport(month, type, amount));
                
                

            }
            
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }
        
        apptTableView.getItems().setAll(apptList);
    }

    /**
     * populateSchedule
     * This method populates the current schedules for the consultants as of right now and all forward 
     * from this point
     */
    private void populateSchedule() {
      
        schedule = FXCollections.observableArrayList();
        
        try{

            PreparedStatement pst = DBConnection.getConn().prepareStatement(
            "SELECT a.appointment_Id, a.customer_Id, a.title, a.description, a.type, "
                + "a.`start`, a.`end`, c.customer_Name, a.created_By, a.user_id, a.location, c.division_id, a.contact_id, co.contact_Name "
                + "FROM appointments a, customers c, contacts co "
                + "WHERE a.customer_Id = c.customer_Id AND a.`start` >= CURRENT_DATE and a.contact_id = co.contact_id "
                + "ORDER BY a.contact_id, a.customer_ID, `start`");
            ResultSet rs = pst.executeQuery();
           
            
            while (rs.next()) {
                
                int tAppointmentId = rs.getInt("a.appointment_Id");
                Timestamp tsStart = rs.getTimestamp("a.start");
                ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);

                Timestamp tsEnd = rs.getTimestamp("a.end");
                ZonedDateTime newzdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(newzid);

                String tTitle = rs.getString("a.title");
                
                String tDescription = rs.getString("a.description");
                String tType = rs.getString("a.type");
                String tLocation = rs.getString("a.location");
                String tStartD = newLocalStart.toLocalDate().toString();
                String tStart = newLocalStart.toLocalTime().toString();
                String tEnd = newLocalEnd.toLocalTime().toString();

                int tCustID = rs.getInt("a.customer_ID");
                String tCustomer = rs.getString("c.customer_Name");
                
                String tUser = rs.getString("a.created_By");
                      
                int tUserId = rs.getInt("a.user_Id");
                int tContactId = rs.getInt("a.contact_Id");
                String tContactName = rs.getString("co.contact_Name");
   
                schedule.add(new Appointments(tAppointmentId, tTitle, tDescription, tLocation, tType, tStartD, tStart, tEnd, tCustID, tCustomer, tUserId, tContactId, tContactName));

                
            }
            
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }
        schedTableView.getItems().setAll(schedule);
    }

    /**
     * populateCustBarChart
     * method creates the bar-chart graph of the customers by division
     */
    private void populateCustBarChart() {

        ObservableList<XYChart.Data<String, Integer>> data = FXCollections.observableArrayList();
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

            try { PreparedStatement pst = DBConnection.getConn().prepareStatement(
                  "SELECT fld.division, COUNT(division) "
                + "FROM customers c, first_level_divisions fld "
                + "WHERE c.division_id = fld.division_id "
                + "GROUP BY division"); 
                ResultSet rs = pst.executeQuery();


                while (rs.next()) {
                        String dIvsion = rs.getString("division");
                        Integer count = rs.getInt("COUNT(division)");
                        data.add(new XYChart.Data<>(dIvsion, count));
                }

            } catch (SQLException sqe) {
                System.out.println("Check your SQL");
                sqe.printStackTrace();
            } catch (Exception e) {
                System.out.println("Something besides the SQL went wrong.");
                e.printStackTrace();
            }             
        series.getData().addAll(data);
        barChart.getData().add(series);
    }
    
    /**
     * onActionCallAppointments
     * action event calls the appointments screen menu option
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionCallAppointments(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/screens/AppointmentPage.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * onActionCallCust
     * action event calls the Customers screen menu option
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionCallCust(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/screens/CustomerPage.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /**
     * onActionReturnMenu
     * action event is the call to return to the main menu option
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionReturnMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/screens/WelcomeMenu.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    
}
