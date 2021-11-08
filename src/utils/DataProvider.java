/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import definitions.FirstLevelDivisions;
import definitions.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author SHIPLEYM
 * This utility was created as a location to hold the list of available system user
 * and to hold the list of divisions so that they wouldn't need to be read from the 
 * database every time you needed to use the lists
 */
public class DataProvider {
   
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();
    private static ObservableList<FirstLevelDivisions> allDivisions = FXCollections.observableArrayList();
    /**
     * addUser to data list
     * @param user 
     */
    public static void addUser(User user){
        allUsers.add(user);
    }
    /**
     * getAllUsers for data list
     * @return 
     */
    public static ObservableList<User> getAllUsers(){
        return allUsers;
    }
    /**
     * add divisions to data list
     * @param fld 
     */
    public static void addDivisions(FirstLevelDivisions fld){
        allDivisions.add(fld);
    }
    /**
     * return all divisions to application in a list
     * @return 
     */
    public static ObservableList<FirstLevelDivisions> getAllDivisions(){
        return allDivisions;
    }
}
