/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import definitions.Countries;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

/**
 * @author SHIPLEYM
 * CountriesDao
 * class to create an ObservableList for getting all countries loaded up into observable lists on the screens
 */
public class CountriesDao {
    /**
     * ObservableList getCountryList for getting all countries loaded up into observable lists on the screens
     * @return 
     */
    public static ObservableList<Countries> getCountryList() {
    
         ObservableList<Countries> data = FXCollections.observableArrayList();
     
       try {
       
        
        String sqlStmt = "SELECT * "
                + "from countries "
                + "order by country_ID";
        
        PreparedStatement myStmt = DBConnection.getConn().prepareStatement(sqlStmt);
        ResultSet rs = myStmt.executeQuery();
        
        while(rs.next()) {
                        
            int countryID = rs.getInt("country_ID");
            String countryName = rs.getString("Country");

            data.add(new Countries(countryID, countryName));
        }
  
        }
        catch(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        }
    return data;
    }    
    
}
