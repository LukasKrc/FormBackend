package lt.ktu.formbackend.dao.impl.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.DaoException.Type;

/**
 *
 * @author Lukas
 */
public class SqlExecutor
{
    private static Connection connection = SqliteConnector.getConnection();
    
    public static <R> R executeStatement(Function<Statement, R> function) throws DaoException{
        try (Statement statement = connection.createStatement()) {
            return function.apply(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    public static <R, P> R executePreparedStatement (
            PreparedStatementFunction<R, P> function, String sql, P param) throws DaoException{
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            return function.apply(statement, param);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    @FunctionalInterface
    public static interface PreparedStatementFunction<R, P> {
        R apply(PreparedStatement statement, P param);
    }
}
