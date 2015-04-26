package lt.ktu.formBackend.dao.impl.db;

import java.sql.Connection;
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

    public static Connection getConnection() throws SQLException {
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        return null;
    }
}
