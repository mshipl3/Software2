/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import definitions.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DBConnection;
import utils.DataProvider;

/**
 * UserDao
 * @author SHIPLEYM
 * data class to pull the users from their table into an Observable list
 */
public class UserDao {
    /**
     * selectUsers
     * method used to create a user list of all in table
     */
    public static void selectUsers(){
        
        try{           
            PreparedStatement pst = DBConnection.getConn().prepareStatement("SELECT * FROM users");
            ResultSet rs = pst.executeQuery();                        
            while(rs.next()){
                DataProvider.addUser(new User(rs.getInt("User_ID"), rs.getString("User_Name"), rs.getString("Password")));
            }            
                
        } catch(SQLException e){
            e.printStackTrace();
        }       
        
    }
    
}
