/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import static dao.AppointmentDao.getAppointments;
import definitions.Appointments;
import definitions.User;
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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DataProvider;
import utils.LoggerUtil;
        
/**
 * FXML Controller class
 *
 * @author Mike Shipley
 * 
 * initial Class to present the Login Page for the global consultant scheduling app
 */
public class LoginPageController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    @FXML
    private Label lcltimezoneLbl;

    public static String globalUser = null;
    public static Integer globalUserID = null;
    
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="titleTxt"
    private Text titleTxt; // Value injected by FXMLLoader

    @FXML // fx:id="usernameLbl"
    private Text usernameLbl; // Value injected by FXMLLoader

    @FXML // fx:id="usernamefld"
    private TextField usernamefld; // Value injected by FXMLLoader

    @FXML // fx:id="passwordLbl"
    private Text passwordLbl; // Value injected by FXMLLoader

    @FXML // fx:id="passwordfld"
    private PasswordField passwordfld; // Value injected by FXMLLoader

    @FXML // fx:id="errorMessage"
    private Label errorMessage; // Value injected by FXMLLoader

    @FXML // fx:id="loginBtn"
    private Button loginBtn; // Value injected by FXMLLoader

    @FXML // fx:id="quitBtn"
    private Button quitBtn; // Value injected by FXMLLoader
    
    //connect to the resource bundle to get the local language
    ResourceBundle lngbndl = ResourceBundle.getBundle("properties/language", Locale.getDefault());

    private final DateTimeFormatter localDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId localTZ = ZoneId.systemDefault();
    private final ZoneId newzid = ZoneId.systemDefault();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    ObservableList<Appointments> reminderAlertList;

    private final static Logger LOGGER = Logger.getLogger(LoggerUtil.class.getName());
   
    /**
     * callLogin action event
     * when the login button is pushed this action is called to attempt to login the user
     * @param event
     * @throws SQLException
     * @throws IOException 
     */
    @FXML
    void callLogin(ActionEvent event) throws SQLException, IOException {

        String userName = usernamefld.getText();
        String passWord = passwordfld.getText();
        boolean match = false;
        for(User user : DataProvider.getAllUsers()){
            if(user.getUsername().equals(userName) && user.getPassword().equals(passWord)){
                match = true;
               globalUser = user.getUsername(); // when the user has been validated against the pre-valued user table 
               globalUserID = user.getUserID(); // then set the global user name and ID so that the user can be used to update the 
               break;                           // appointment and customer tables
            }
        }
        if(match){
                 getAppointments();  // when validated and matched, get a list of appointmens and see if the user has an appointment within the next 15 Mins.
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/screens/WelcomeMenu.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
                LOGGER.log(Level.INFO, "{0} logged in", userName);
                
        }
        else{
                LOGGER.log(Level.INFO, "{0} access denied!", userName);
                errorMessage.setText(lngbndl.getString("baduser") + ": " + userName);
        }
    }

    /**
     * callQuit
     * button to quit the system
     * logoff button pressed, verify the that the user is going to exit
     * @param event 
     */
    @FXML
    void callQuit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(lngbndl.getString("confirm"));
            alert.setHeaderText(lngbndl.getString("sure"));
            alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent((ButtonType response) -> {
                Platform.exit();
                System.exit(0);
                }
            );

    }

    /**
     * setProperties method set the login screen language, works for English, French or Spanish
     */
    public void setProperties() {
        
        if (Locale.getDefault().getLanguage().equals("en") ||
            Locale.getDefault().getLanguage().equals("fr") ||
            Locale.getDefault().getLanguage().equals("es")) {
            titleTxt.setText(lngbndl.getString("title"));
            usernameLbl.setText(lngbndl.getString("username"));
            passwordLbl.setText(lngbndl.getString("password"));
            loginBtn.setText(lngbndl.getString("signin"));
            quitBtn.setText(lngbndl.getString("cancel"));
        }

        lcltimezoneLbl.setText(newzid.getId());

    }
    /**
     * Initializes the LoginPage controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setProperties();
    }    
}
