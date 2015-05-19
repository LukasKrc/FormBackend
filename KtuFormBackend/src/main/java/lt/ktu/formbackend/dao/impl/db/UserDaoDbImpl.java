package lt.ktu.formbackend.dao.impl.db;

//<editor-fold desc="Imports">
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.DaoException.Type;
import lt.ktu.formbackend.dao.UserDao;
import lt.ktu.formbackend.model.FormAnswer;
import lt.ktu.formbackend.model.User;
import lt.ktu.formbackend.rest.AuthenticationService;
//</editor-fold>

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
    private static final String USER_FINISHED_FORM = "SELECT Forms.id FROM (Users LEFT JOIN FormAnswers on Users.username = FormAnswers.author) LEFT JOIN Forms ON Forms.id = FormAnswers.form WHERE Forms.id = ? AND Users.username = ?";
            
    @Override
    public Boolean userFinishedForm(String username, long formId) throws DaoException {
        FormAnswer formAnswer = new FormAnswer();
        formAnswer.setAuthor(username);
        formAnswer.setForm(formId);
        return SqlExecutor.executePreparedStatement(this::userFinishedFormFunction, USER_FINISHED_FORM, formAnswer);
    }
    
    @Override
    public Boolean updateUser(User user, String username) throws DaoException {
        String sql = SQLBuilder.buildUserUpdateSQL(user, username);
        return SqlExecutor.executePreparedStatement(this::updateUserFunction, sql, user);
    }

    @Override
    public List<User> getAllUsers() throws DaoException {
        return SqlExecutor.executeStatement(this::getUserListFunction);
    }

    @Override
    public User getUserId(Long userId) throws DaoException {
        return SqlExecutor.executePreparedStatement(this::getUserByIdFunction, USER_GET_SQL, userId);
    }

    @Override
    public User getUserUsername(String username) throws DaoException {
        return SqlExecutor.executePreparedStatement(this::getUserByUsernameFunction, USER_GET_USERNAME_SQL, username);
    }

    @Override
    public Boolean userExists(String username) throws DaoException {
        return SqlExecutor.executePreparedStatement(this::userExists, USER_GET_USERNAME_SQL, username);
    }

    @Override
    public Boolean createUser(User user) throws DaoException {
        if (!userExists(user.getUsername())) {
            if (user.hasMandatoryFields() == null) {
                return SqlExecutor.executePreparedStatement(this::createUserFunction, USER_CREATE_SQL, user);
            } else {
                throw new DaoException(Type.ERROR, "Missing mandatory field: " + user.hasMandatoryFields());
            }
        } else {
            throw new DaoException(Type.ERROR, "User with that username already exists");
        }
    }

    @Override
    public Boolean deleteUser(String username) throws DaoException {
        return SqlExecutor.executePreparedStatement(this::deleteUserFunction, USER_DELETE_SQL, username);
    }
    
    private Boolean userFinishedFormFunction(PreparedStatement statement, FormAnswer answer) {
        try {
            statement.setLong(1, answer.getForm());
            statement.setString(2, answer.getAuthor());
            statement.execute();
            ResultSet rs = statement.getResultSet();
            if (rs.next())
                return true;
            else 
                return false;
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }
    
    private Boolean userExists(PreparedStatement statement, String username) {
        try {
            statement.setString(1, username);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            if (rs.next())
                return true;
            else
                return false;
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private List<User> getUserListFunction(Statement statement) throws DaoException {
        try {
            statement.execute(USER_GET_ALL_SQL);
            ResultSet rs = statement.getResultSet();
            List<User> result = new ArrayList<>();
            if (rs.next()) {
                while (rs.next()) {
                    result.add(fillUserData(rs));
                }
                return result;
            } else {
                throw new DaoException(Type.ERROR, "No users in database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException(Type.ERROR, e.getMessage());
        }
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
        throw new DaoException(Type.NO_DATA, "No user by id: " + userId);
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
        throw new DaoException(Type.NO_DATA, "No user by username: " + username);
    }

    private Boolean createUserFunction(PreparedStatement statement, User user) {
        try {
            if (user.getUsername() != null) {
                statement.setString(1, user.getUsername());
            }
            if (user.getPassword() != null) {
                statement.setString(2, user.getPassword());
            }
            if (user.getName() != null) {
                statement.setString(3, user.getName());
            }
            if (user.getSurname() != null) {
                statement.setString(4, user.getSurname());
            }
            if (user.getCompany() != null) {
                statement.setString(5, user.getCompany());
            }
            if (user.getIsCompany() != null) {
                Integer isCompany = user.getIsCompany() == true ? 1 : 0;
                statement.setInt(6, isCompany);
            }
            if (statement.executeUpdate() > 0) {
                return true;
            } else {
                throw new DaoException(Type.ERROR, "User creation failed");
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
            if (statement.executeUpdate() > 0) {
                return true;
            } else {
                throw new DaoException(Type.ERROR, "User update failed");
            }
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private Boolean deleteUserFunction(PreparedStatement statement, String username) {
        try {
            statement.setString(1, username);
            if (statement.executeUpdate() > 0) {
                return true;
            } else {
                throw new DaoException(Type.ERROR, "User deletion failed");
            }
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
        if (rs.getInt("isCompany") == 1) {
            isCompany = true;
        }
        user.setIsCompany(isCompany);
        return user;
    }

}
