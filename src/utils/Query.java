/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;


/**
 *
 * @author SHIPLEYM
 * the query class is used to define the objects used to read the data tables and retrieve the needed information
 */
public class Query {
    
    /**
     * private static statement declaration
     */
    private static Statement statement;
    
    /**
     * Create statement object for setting preparedstatment
     * @param conn
     * @param sqlStatement
     * @throws SQLException 
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException
    {
        System.out.println(conn);
        System.out.println(sqlStatement);
        statement = conn.prepareStatement(sqlStatement);
    }
    
    /**
     *  return statement object
     * @return 
     */
    public static Statement getPreparedStatement()
    {
        return statement;
    }

}
