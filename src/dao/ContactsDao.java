/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import definitions.Contacts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

/**
 * ContactsDao
 * @author SHIPLEYM
 * this dao package gets the lists of contacts in the system to allow selections in the observable lists
 */
public class ContactsDao {
    
    /**
     * ObservableList getContactList for getting all Contacts(Consultants)
     * @return 
     */
    public static ObservableList<Contacts> getContactList() {
    
       ObservableList<Contacts> data = FXCollections.observableArrayList();
       
       try {
       
        
        String sqlStmt = "SELECT co.contact_Name, co.contact_ID from contacts co";
        
        PreparedStatement myStmt = DBConnection.getConn().prepareStatement(sqlStmt);
        ResultSet rs = myStmt.executeQuery();
        
        while(rs.next()) {
            
            int contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("contact_Name");

            data.add(new Contacts(contactID,contactName));
        }
  
        }
        catch(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        }
    return data;
    }
}
