package lt.ktu.formbackend.dao.impl.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.DaoException.Type;
import lt.ktu.formbackend.dao.UserDao;

import lt.ktu.formbackend.model.User;

/**
 *
 * @author Lukas
 */
public class UserDaoDbImpl implements UserDao {

    private static final String USER_GET_USERNAME_SQL = "SELECT id, username, password, name ,surname ,company, isCompany FROM Users WHERE username = ?";
    private static final String USER_GET_SQL = "SELECT id, username, password, name, surname, company, isCompany FROM Users WHERE id = ?";
    private static final String USER_GET_ALL_SQL = "SELECT id, username, password, name, surname, company, isCompany FROM Users";
    private static final String USER_CREATE_SQL = "INSERT INTO Users (username, password, name, surname, company, isCompany) values (?, ?, ?, ?, ?, ?)";
    private static final String USER_DELETE_SQL = "DELETE FROM Users WHERE username = ?";
    
    @Override
    public Boolean updateUser(User user, String username) throws DaoException {
        String sql = SQLBuilder.buildUserUpdateSQL(user, username);
        try {
            return SqlExecutor.executePreparedStatement(this::updateUserFunction, sql, user);
        } catch (DaoException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }
    
    @Override
    public List<User> getAllUsers() throws DaoException {
        try {
            return SqlExecutor.executeStatement(this::getUserListFunction);
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    @Override
    public User getUserId(Long userId) throws DaoException {
        try {
            return SqlExecutor.executePreparedStatement(this::getUserByIdFunction, USER_GET_SQL, userId);
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    @Override
    public User getUserUsername(String username) throws DaoException {
        try {
            return SqlExecutor.executePreparedStatement(this::getUserByUsernameFunction, USER_GET_USERNAME_SQL, username);
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }
    
    @Override
    public Boolean createUser(User user) throws DaoException {
        try {
            return SqlExecutor.executePreparedStatement(this::createUserFunction, USER_CREATE_SQL, user);
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }
    
    @Override
    public Boolean deleteUser(String username) throws DaoException {
        try {
            return SqlExecutor.executePreparedStatement(this::deleteUserFunction, USER_DELETE_SQL, username);
        } catch (DaoException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private List<User> getUserListFunction(Statement statement) throws DaoException {
        try {
            if (statement.execute(USER_GET_ALL_SQL)) {
                ResultSet rs = statement.getResultSet();
                List<User> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(fillUserData(rs));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException(Type.ERROR, e.getMessage());
        }
        throw new DaoException(Type.NO_DATA, "No users");
    }

    private User getUserByIdFunction(PreparedStatement statement, Long userId) throws DaoException {
        try {
            statement.setLong(1, userId);
            if (statement.execute()) {
                ResultSet rs = statement.getResultSet();
                if (rs.next()) {
                    return fillUserData(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException(Type.ERROR, e.getMessage());
        }
        throw new DaoException(Type.NO_DATA, "No user by id=" + userId);
    }

    private User getUserByUsernameFunction(PreparedStatement statement, String username) throws DaoException {
        try {
            statement.setString(1, username);
            if (statement.execute()) {
                ResultSet rs = statement.getResultSet();
                if (rs.next()) {
                    return fillUserData(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException(Type.ERROR, e.getMessage());
        }
        throw new DaoException(Type.NO_DATA, "No user by username=" + username);
    }
    
    private Boolean createUserFunction(PreparedStatement statement, User user) {
        try {
            Integer isCompany = 0;
            if (user.getIsCompany()) isCompany = 1;
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getCompany());
            statement.setInt(6, isCompany);
            try {
                statement.execute();
                return true;
            } catch (SQLException e) {
                throw new DaoException(Type.ERROR, e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }
    
    private Boolean updateUserFunction(PreparedStatement statement, User user) {
        int i = 1;
        try {
            if (user.getIsCompany() != null) {
                Integer isCompany = user.getIsCompany() ? 1 : 0;
                statement.setInt(i++, isCompany);
            }
            if (user.getCompany() != null) {
                statement.setString(i++, user.getCompany());
            }
            if (user.getName() != null) {
                statement.setString(i++, user.getName());
            }
            if (user.getPassword() != null) {
                statement.setString(i++, user.getPassword());
            }
            if (user.getSurname() != null) {
                statement.setString(i++, user.getSurname());
            }
            if (statement.execute())
                return true;
            else 
                return false;
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }
    
    private Boolean deleteUserFunction(PreparedStatement statement, String username) {
        try {
            statement.setString(1, username);
            if (statement.execute())
                return true;
            else
                return false;
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private User fillUserData(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setId(rs.getLong("id"));
        user.setCompany(rs.getString("company"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        Boolean isCompany = false;
        if (rs.getInt("isCompany") == 1) isCompany = true;
        user.setIsCompany(isCompany);
        return user;
    }

}
