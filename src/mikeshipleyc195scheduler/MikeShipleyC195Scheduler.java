/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mikeshipleyc195scheduler;

import dao.LocationDao;
import dao.UserDao;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.LoggerUtil;

/**
 *
 * @author Mike Shipley
 * Student ID: #000389613
 * C195 Software II
 * Java - Scheduling Application
 * This is an application to use for scheduling appointments for a Global Consulting Firm,
 * it can be used to schedule times for consultations between 8:00 am and 10:00pm in any timezone
 * for all consultants who are employed by the Global Consulting Firm.
 */
public class MikeShipleyC195Scheduler extends Application{

    private static Connection connection;

    public static void main(String[] args) {

        timeConversions();
        DBConnection.init();
        connection = DBConnection.getConn();
        LoggerUtil.init();
        UserDao.selectUsers();
        LocationDao.getLocationList();
        launch(args);
        DBConnection.closeConn();
    }

    /**
     *  timeConversions method was built for debugging purposes
     */
    public static void timeConversions(){
        LocalDateTime startTime = LocalDateTime.of(LocalDate.of(2021,07,29),LocalTime.of(5,0));
        ZonedDateTime localZDT = ZonedDateTime.of(startTime, ZoneId.systemDefault());
        ZonedDateTime estZDT = ZonedDateTime.ofInstant(localZDT.toInstant(), ZoneId.of("America/New_York"));
        System.out.println(estZDT);
        System.out.println(estZDT.toLocalTime());
        System.out.println(Timestamp.valueOf(estZDT.toLocalDateTime()));
    }
    
    /**
     * start method to call login page to open the scheduling application
     * @param stage
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Start with Login Screen
        Parent root = FXMLLoader.load(getClass().getResource("/screens/LoginPage.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
    
}
