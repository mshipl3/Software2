/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import definitions.FirstLevelDivisions;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utils.DBConnection;
import utils.DataProvider;

/**
 * LocationDao
 * @author SHIPLEYM
 * this class is used to pull the first level divisions to be filtered in observation lists.
 */
public class LocationDao {
    /**
     * ObservableList getLocationList for getting all locations in the dbase
     */
    public static void getLocationList() {
    
       try {
       
        
        String sqlStmt = "SELECT f.division_ID, f.division, f.country_ID, co.country "
                + "from first_level_divisions f, countries co "
                + "where f.country_ID = co.country_ID "
                + "order by co.country";
        
        PreparedStatement myStmt = DBConnection.getConn().prepareStatement(sqlStmt);
        ResultSet rs = myStmt.executeQuery();
        
        while(rs.next()) {
                        
            int divisionID = rs.getInt("division_ID");
            String divisionName = rs.getString("division");
            int countryID = rs.getInt("country_ID");

            DataProvider.addDivisions(new FirstLevelDivisions(divisionID, divisionName, countryID));
        }
       
        }
        catch(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        }
    }    
}
