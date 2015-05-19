package lt.ktu.formbackend.dao.impl.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Lukas
 */
public class SqliteConnector {
    
    private static DataSource dataSource;

    static {
        Context initCtx;
        try {
            initCtx = new InitialContext();
            dataSource = (DataSource) initCtx.lookup("java:comp/env/jdbc/Database");
            System.out.println(dataSource.toString() + "Database info __________________");
        } catch (NamingException e) {
            e.printStackTrace();

        }
    }

    public static Connection getConnection() {
        System.out.println("Connecting to database...");
        try {
            Class.forName("org.sqlite.JDBC");

            // duombaze turi buti {tomcat_dir}/database/database.db
            String dbURL = "jdbc:sqlite:../database/database.db";

            Connection sqliteConnection = DriverManager.getConnection(dbURL);
            if (sqliteConnection != null) {
                System.out.println("Connected to the database");

                DatabaseMetaData dm = (DatabaseMetaData) sqliteConnection.getMetaData();
                System.out.println("Database Info: driver=" + dm.getDriverName() + ", driverVersion=" + dm.getDriverVersion() + ", productName=" + dm.getDatabaseProductName() + ", productVersion=" + dm.getDatabaseProductVersion());

                return sqliteConnection;
            } else {
                System.out.println("Failed to connect...");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
