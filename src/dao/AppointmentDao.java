/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static controllers.LoginPageController.globalUser;
import definitions.Appointments;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert; 
import javafx.scene.control.ButtonType;
import utils.DBConnection;

/**
 * AppointmentDao
 * @author SHIPLEYM
 * this dao class is used to create lists of appointments, it is meant to allow a refresh of items to 
 * reduce over head on the methods in the program
 */
public class AppointmentDao {
    /**
     * this first method getAppointments will check for any appointments 
     * for the user logging into the system.
     */
    public static void getAppointments(){
    try {
               
        
        String sqlStmt = "SELECT a.appointment_ID, a.customer_ID, a.type, "
                       + "a.start, a.end, c.customer_Name, u.user_Name "
                       + "FROM appointments a, customers c, users u "
                       + "WHERE a.customer_Id = c.customer_Id and a.user_Id = u.user_Id";
        
        PreparedStatement myStmt = DBConnection.getConn().prepareStatement(sqlStmt);
        ResultSet rs = myStmt.executeQuery();
        
        while(rs.next()) {
            ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
            DateTimeFormatter formatterLocalDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(localZoneId);
            DateTimeFormatter formatterLocalTime = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(localZoneId);
            
            LocalDate startDate = LocalDate.parse(rs.getString("start").substring(0, 10));
            LocalTime startTime = LocalTime.parse(rs.getString("start").substring(11, 19));
            
            ZonedDateTime UTCStartDT = ZonedDateTime.of(startDate, startTime, ZoneId.of("UTC"));
            
            String localStartDate= formatterLocalDate.format(UTCStartDT);
            LocalDate ltStartDate = LocalDate.parse(localStartDate);
            String localStartTime= formatterLocalTime.format(UTCStartDT);
            LocalTime ltStartTime = LocalTime.parse(localStartTime);
            
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            
            Long timeDifference = ChronoUnit.MINUTES.between(currentTime, ltStartTime);
            long elapsedTime = timeDifference + 1;

            if(globalUser == null ? rs.getString("u.user_Name") == null : globalUser.equals(rs.getString("u.user_Name"))) {
                if(currentDate.equals(ltStartDate)) {
                    if(elapsedTime >= 0 && elapsedTime <= 15) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Appointment Reminder");
                        alert.setContentText("Hello " + rs.getString("u.user_Name") + "! You have an appointment in approximately " + elapsedTime + " minute(s) with " + rs.getString("c.customer_Name"));
                        alert.showAndWait();
                        return;
                    }
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointment Reminder");
                    alert.setContentText("Hello " + rs.getString("u.user_Name") + "! You have no appoinments in the next 15 minutes!");
                    alert.show();
                    return;
                }
            }
        }
  
        }
    catch(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        }  
    }
    
    /**
     * ObservableList getAllAppointments created for getting all appointments in the 
     * database and all of the text stored in the joining tables
     */
    public static ObservableList<Appointments> getAllAppointments() {
 
        ObservableList<Appointments> data = FXCollections.observableArrayList();
        
        try {

            String sqlStmt = "SELECT a.appointment_ID, a.title, a.description, a.Location, a.type, "
                    + "a.start, a.end, a.customer_Id, c.customer_Name, a.contact_id, co.contact_name, a.user_id, u.user_name, "
                    + "f.division_id, f.country_id, cn.country "
                    + "FROM appointments a, customers c, contacts co, users u, countries cn, first_level_divisions f "
                    + "WHERE a.customer_Id = c.customer_Id and a.contact_id = co.contact_id and a.user_id = u.user_id and "
                    + "a.location = f.division and f.country_id = cn.country_id "
                    + "ORDER BY `start`";

            Connection myConn = DBConnection.getConn();
            PreparedStatement myStmt = myConn.prepareStatement(sqlStmt);
            ResultSet rs = myStmt.executeQuery();

            while(rs.next()) {
                ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
                DateTimeFormatter formatterLocal = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(localZoneId);

                LocalDate startDate = LocalDate.parse(rs.getString("start").substring(0, 10));
                LocalTime startTime = LocalTime.parse(rs.getString("start").substring(11, 19));
                LocalDate endDate = LocalDate.parse(rs.getString("end").substring(0, 10));
                LocalTime endTime = LocalTime.parse(rs.getString("end").substring(11, 19));

                ZonedDateTime UTCStartDT = ZonedDateTime.of(startDate, startTime, ZoneId.of("UTC"));
                ZonedDateTime UTCEndDT = ZonedDateTime.of(endDate, endTime, ZoneId.of("UTC"));

                String localStartDT = formatterLocal.format(UTCStartDT);
                String localEndDT = formatterLocal.format(UTCEndDT);


                int appointmentId = rs.getInt("appointment_Id");
                int customerId = rs.getInt("customer_Id");
                int contactId = rs.getInt("contact_Id");
                String type = rs.getString("type");
                String start = localStartDT;
                String end = localEndDT;
                String description = rs.getString("description");
                String title = rs.getString("title");
                String location = rs.getString("location");
                int locationId = rs.getInt("division_id");
                String contactName = rs.getString("contact_name");
                String customerName = rs.getString("customer_Name");
                int userId = rs.getInt("user_Id");
                String userName = rs.getString("user_Name");
                int countryId = rs.getInt("country_id");
                String country = rs.getString("country");


                Appointments a = new Appointments(appointmentId, title, description, location, locationId, type, start, end, customerId, contactId, contactName, customerName, userId, userName, countryId, country);
                data.add(a);
            }
  
        }
    catch(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        }
    return data;
    
    }    

    /**
     * getSimilarAppointments
     * Observable list for appointments that are the same as the new appointment being created 
     */
    public static ObservableList<Appointments> getSimilarAppointments(ZonedDateTime apptStart, ZonedDateTime apptEnd, int cnstltntid) {
 
    ObservableList<Appointments> data = FXCollections.observableArrayList();
        
    try {
        
        String stringStartDT = apptStart.toLocalDateTime().toString().replace("T", " ");
        String stringEndDT = apptEnd.toLocalDateTime().toString().replace("T", " ");

        String sqlStmt = "SELECT appointment_ID, title, description, Location, type, "
                + "start, end, customer_Id, contact_id "
                + "FROM appointments "
                + "WHERE (? BETWEEN start AND end OR ? BETWEEN start AND end OR ? < start AND ? > end) "
                + "AND (contact_id = ?) "
                + "ORDER BY `start`";

        Connection myConn = DBConnection.getConn();
        PreparedStatement myStmt = myConn.prepareStatement(sqlStmt);
        myStmt.setString(1, stringStartDT);
        myStmt.setString(2, stringEndDT);
        myStmt.setString(3, stringStartDT);
        myStmt.setString(4, stringEndDT);
        myStmt.setString(5, Integer.toString(cnstltntid));
        ResultSet rs = myStmt.executeQuery();
        
        while(rs.next()) {
            ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
            DateTimeFormatter formatterLocal = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(localZoneId);
            
            LocalDate startDate = LocalDate.parse(rs.getString("start").substring(0, 10));
            LocalTime startTime = LocalTime.parse(rs.getString("start").substring(11, 19));
            LocalDate endDate = LocalDate.parse(rs.getString("end").substring(0, 10));
            LocalTime endTime = LocalTime.parse(rs.getString("end").substring(11, 19));

            ZonedDateTime UTCStartDT = ZonedDateTime.of(startDate, startTime, ZoneId.of("UTC"));
            ZonedDateTime UTCEndDT = ZonedDateTime.of(endDate, endTime, ZoneId.of("UTC"));
            
            String localStartDT = formatterLocal.format(UTCStartDT);
            String localEndDT = formatterLocal.format(UTCEndDT);
            

            int appointmentId = rs.getInt("appointment_Id");
            int customerId = rs.getInt("customer_Id");
            int contactId = rs.getInt("contact_Id");
            String type = rs.getString("type");
            String start = localStartDT;
            String end = localEndDT;
            String description = rs.getString("description");
            String title = rs.getString("title");
            String location = rs.getString("location");
     
            Appointments a = new Appointments(appointmentId, title, description, location, type, start, end, customerId, contactId);
            data.add(a);
        }
  
        }
    catch(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        }
    return data;
    
    }    

    /**
     * deleteAppointment
     * Method used for deleting appointments called from the Appointment Page Controller
     * @param appointmentId
     * @param appointmentType
     * @return 
     */
    public static String deleteAppointment(int appointmentId, String appointmentType) {

    try {

        
        String sqlStmt = "DELETE from appointments WHERE appointment_Id = ?";
        
        Connection myConn = DBConnection.getConn();
        
        PreparedStatement myStmt = myConn.prepareStatement(sqlStmt);
        
        myStmt.setString(1, Integer.toString(appointmentId));

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning!");
        alert.setHeaderText("This will delete the selected appointment #" + appointmentId + " \nWhich is a " + appointmentType + " appointment.");
        alert.setContentText("Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            myStmt.executeUpdate();
        }
        
    }
    catch(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        }
    return null;
    }
    
    /**
     * deleteCustomerAppointments
     * Method to delete all appointments for a single customer
     * this method is called as a first step to deleting a customer
     */
    public static String deleteCustomerAppointments(int customerID) {
        
    try {

        
        String sqlStmt = "DELETE from appointments WHERE Customer_ID = ?";
        
        Connection myConn = DBConnection.getConn();
        
        PreparedStatement myStmt = myConn.prepareStatement(sqlStmt);
        
        myStmt.setString(1, Integer.toString(customerID));
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning!");
        alert.setHeaderText("This will delete all appointments for Customer #" + customerID);
        alert.setContentText("Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            myStmt.executeUpdate();
        }
        
    }
    catch(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        }
    return null;
    }
}
