/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.AppointmentDao;
import static dao.CustomerDao.getAllCustomers;
import definitions.Countries;
import definitions.Customers;
import definitions.FirstLevelDivisions;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.DBConnection;

/**
 * FXML Controller class
 *
 * @author mshipl3
 * 
 * this controller page was built to present and give access to the data in the 
 * customers table of the database.  The options presented to the user/administrator of  
 * of the accompanying screen allow the user to create, update and delete customers. note, 
 * if the customer is deleted and they have existing appointments/consultations those
 * items will also be deleted from the database
 */
public class CustomerPageController implements Initializable {

    // This two row create lists of office locations and countries
    ObservableList<FirstLevelDivisions> filteredDivisions = FXCollections.observableArrayList();
    ObservableList<Countries> filteredCountries = FXCollections.observableArrayList();

    Stage stage;
    Parent scene;

    @FXML
    private TableView<Customers> customerTable;

    @FXML
    private TableColumn<Customers, String> customerNameColumn;

    @FXML
    private TableColumn<Customers, String> phoneColumn;
    
    @FXML
    private TableColumn<Customers, String> customerAddressColumn;

    @FXML
    private TableColumn<Customers, String> divisonColumn;

    @FXML
    private TableColumn<Customers, String> postalCodeColumn;

    @FXML
    private TableColumn<Countries, String> countryColumn;
    
    @FXML
    private ButtonBar newEditDeleteButtonBar;
    
    /**
     * Initialization method for the Customer Page controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<Customers> custData = getAllCustomers();

        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        divisonColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

        customerTable.setItems(custData);
        
    }    

    /**
     * handleNewCustomer action event called the Add Customer Page
     * @param event
     * @throws IOException 
     * call the create new customer screen
     */
    @FXML
    private void handleNewCustomer(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/screens/AddCustomerPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * handleEditCustomer allows the edit of the selected customer, verifies a selection 
     * call screen to edit a created customer
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleEditCustomer(ActionEvent event) throws IOException {
        Customers selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer != null) {
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/screens/EditCustomerPage.fxml"));
            Object load = loader.load();
            
            EditCustomerPageController getCustomer = loader.getController();
            getCustomer.recallCustomer(selectedCustomer);
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer selected");
            alert.setContentText("Please select a Customer in the Table");
            alert.showAndWait();
        }
    }

    /**
     * handleDeleteCustomer action event
     * calls the delete function to remove a customer, this will delete all of the selected 
     * customers appointments also
     * the user will be double prompted for this request
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleDeleteCustomer(ActionEvent event) throws IOException {
        Customers selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete " + selectedCustomer.getCustomerName() + "?  This will delete the Customer and all Appointments");
            alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent((ButtonType response) -> {
                AppointmentDao.deleteCustomerAppointments(selectedCustomer.getCustomerId());
                deleteCustomer(selectedCustomer);
            });
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/screens/CustomerPage.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer selected for Deletion");
            alert.setContentText("Please select a Customer in the Table to delete");
            alert.showAndWait();
        }

    }

    /**
     * handleReturnMenu action event
     * Call the event to return to the Welcome Screen
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleReturnMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/screens/WelcomeMenu.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

   
    /**
     * deleteCustomer method 
     * Deletes selected customer from table
     * @param customer 
     */
    private void deleteCustomer(Customers customer) {
        
        try{           
            PreparedStatement pst = DBConnection.getConn().prepareStatement("DELETE from customers WHERE customer_Id = ?");
            pst.setInt(1, customer.getCustomerId()); 
            pst.executeUpdate();   
                
        } catch(SQLException e){
            e.printStackTrace();
        }
        
    }
    /**
     * showCustomerDetails passed the selected Customer 
     * fields from selectedCustomer into a variable
     * @param selectedCustomer 
     */
    @FXML
    private void showCustomerDetails(Customers selectedCustomer) {
     selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

    }
}
