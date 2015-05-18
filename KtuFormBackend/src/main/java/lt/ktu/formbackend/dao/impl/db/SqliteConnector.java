package lt.ktu.formbackend.dao.impl.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.tools.FileObject;

/**
 *
 * @author Justas
 */
public class SqliteConnector
{
    public static Connection getConnection()
    {
        System.out.println("Connecting to database...");
        try
        {
            Class.forName("org.sqlite.JDBC");
            
            // duombaze turi buti {tomcat_dir}/database/database.db
            String dbURL = "jdbc:sqlite:../database/database.db";
            
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null)
            {
                System.out.println("Connected to the database");
                
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
                
                return conn;
            } else
            {
                System.out.println("Failed to connect...");
            }
        } catch (ClassNotFoundException | SQLException ex)
        {
            ex.printStackTrace();
        }
      
        return null;
    }
}
