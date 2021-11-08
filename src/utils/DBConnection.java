/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author SHIPLEYM
 */
public class DBConnection {
    
    /**
     * Create the JDBC connection
     */
    private static final String protocol = "jdbc";
    private static final String dbName = ":mysql:";
    private static final String dbLocation = "//localhost/client_schedule?autoReconnect=true";
    
    private static final String jdbcURL = protocol + dbName + dbLocation; // + "?connectionTimeZone=SERVER";

    private static Connection connDB;
    
    public DBConnection(){}
    
    /**
     * Opens connection to database based on following info:
     * Server name: localhost
     * Database name: client_schedule
     * Username: sqlUser
     * Password: Passw0rd!
     */
    public static void init(){
        System.out.println("Connecting to the database");
        try{
            // jdbc database driver
            Class.forName("com.mysql.jdbc.Driver");
            // connection to database
//            connDB = DriverManager.getConnection("jdbc:mysql://wgudb.ucertify.com/WJ03p8u", "U03p8u", "53688046131");
            connDB = DriverManager.getConnection(jdbcURL, "sqlUser", "Passw0rd!");
        }catch (ClassNotFoundException ce){
            System.out.println("Cannot find the right class.  Did you remember to add the mysql library to your Run Configuration?");
            ce.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    
    /**
     * Returns Connection to the DB
     * @return 
     */
    public static Connection getConn(){
    
        return connDB;
    }
    
    /**
     * Closes connections
     */
    public static void closeConn(){
        try{
            connDB.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            System.out.println("Connection closed.");
        }
    }
    
}
