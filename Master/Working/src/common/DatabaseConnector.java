                        
                    /*─────────────────┐
                    │      author : ismail.      │
                    └─────────────────*/

package common;

/*
 * tutorial from : https://www.tutorialspoint.com/jdbc/jdbc-db-connections.htm
 */

import java.sql.* ;  // for standard JDBC programs
import java.math.* ; // for BigDecimal and BigInteger support

public class DatabaseConnector
{

    /* this method returns a standard connection */ 
    public static Connection activateConnection() // return connection
    {

        Connection c = null ;
        
        try 
        {

           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection("jdbc:sqlite:TheFinalDatabase.db");
           // System.out.println("THE DATABASE HAS SPAWNED");

           return c ; 

        }    

        catch ( Exception e ) 
        {

           System.err.println( e.getClass().getName() + ": " + e.getMessage() );

           return null ;     

        }

    } 

    /* this method returns a connection where the SQL command ' ON DELETE CASCADE ' is implemented */
    public static Connection activateCascadedConnection()
    {

        Connection cc = null ;
        
        try 
        {

            Class.forName("org.sqlite.JDBC");
            cc = DriverManager.getConnection("jdbc:sqlite:TheFinalDatabase.db");
            // System.out.println("THE DATABASE HAS SPAWNED");
            cc.createStatement().execute( "PRAGMA foreign_keys = ON " ) ;

            return cc ; 

        }   

        catch ( Exception e ) 
        {

            System.err.println( e.getClass().getName() + ": " + e.getMessage() );

            return null ;     

        }  

   }

}