/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import definitions.Customers;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

/**
 * CustomerDao
 * @author SHIPLEYM
 * this class is used to create lists of customers available to select for appointments 
 * of the global consulting firm
 */
public class CustomerDao {

    /**
     * ObservableList getCustomerList for getting all customer records
     * @return 
     */
    public static ObservableList<String> getCustomerList() {
    
       ObservableList<String> data = FXCollections.observableArrayList();
       
       try {
       
        
        String sqlStmt = "SELECT c.Customer_ID, c.Customer_Name from customers c";
        
        PreparedStatement myStmt = DBConnection.getConn().prepareStatement(sqlStmt);
        ResultSet rs = myStmt.executeQuery();
        
        while(rs.next()) {
            
            String customerName = rs.getString("Customer_Name");
            
            String cList = new String(customerName);
            data.add(cList);
        }
  

        }
    catch(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        }
    return data;
    }
 
    /**
     * ObservableList getAllCustomers created for getting all Customer Records
     */
    public static ObservableList<Customers> getAllCustomers() {
 
        ObservableList<Customers> data = FXCollections.observableArrayList();
        
    try {
        
        String sqlStmt = "SELECT c.customer_Id, c.Customer_Name, c.address, c.postal_code, c.Phone, c.Division_ID, "
                        + "f.Division, f.Country_ID, co.country "
                        + "FROM customers c, first_level_divisions f, countries co "
                        + "WHERE c.division_ID = f.division_ID and f.country_ID = co.country_ID";
        
        PreparedStatement myStmt = DBConnection.getConn().prepareStatement(sqlStmt);;
        ResultSet rs = myStmt.executeQuery();
        
        while(rs.next()) {
            
            int customerId = rs.getInt("customer_Id");
            String customerName = rs.getString("Customer_Name");
            String phone = rs.getString("Phone");
            String address = rs.getString("address");
            String postalCode = rs.getString("postal_code");
            int divisionId = rs.getInt("division_ID");
            String division = rs.getString("division");
            int countryId = rs.getInt("country_id");
            String country = rs.getString("country");

            Customers c = new Customers(customerId, customerName, address, postalCode, phone, divisionId, division, countryId, country);
            data.add(c);
        }
  

        }
        catch(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        }
    return data;
    
    }
    
}
