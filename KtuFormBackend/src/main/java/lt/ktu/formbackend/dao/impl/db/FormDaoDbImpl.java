package lt.ktu.formbackend.dao.impl.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.DaoException.Type;
import lt.ktu.formbackend.dao.FormDao;
import lt.ktu.formbackend.model.Form;

/**
 *
 * @author Lukas
 */
public class FormDaoDbImpl implements FormDao {

    private static final String FORM_CREATE_SQL = "INSERT INTO Forms (name, author, desc, date, allowAnon, public, showResults) values (?, ?, ?, ?, ?, ?, ?)";
    private static final String FORM_GET_USER_NAME_SQL = "SELECT id, name, author, desc, date, allowAnon, public, showResults FROM Forms WHERE author = ? AND name = ?";
    private static final String FORM_GET_ID_SQL = "SELECT id, name, author, desc, date, allowAnon, public, showResults FROM Forms WHERE id = ?";
    private static final String FORM_DELETE_SQL = "DELETE FROM Forms WHERE id = ?";
    private static final String FORM_GET_USER_NAME_ID_SQL = "SELECT author FROM Forms WHERE id = ?";

    @Override
    public Boolean deleteForm(long id) {
        try {
            return SqlExecutor.executePreparedStatement(this::deleteFormFunction, FORM_DELETE_SQL, id);
        } catch (DaoException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    @Override
    public Long createForm(Form form) {
        try {
            return SqlExecutor.executePreparedStatement(this::createFormFunction, FORM_CREATE_SQL, form);
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    @Override
    public Form getFormId(long id) {
        try {
            return SqlExecutor.executePreparedStatement(this::getFormIdFunction, FORM_GET_ID_SQL, id);
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    @Override
    public String getFormAuthor(long id) {
        try {
            return SqlExecutor.executePreparedStatement(this::getFormIdUserNameFunction, FORM_GET_USER_NAME_ID_SQL, id);
        } catch (DaoException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean userHasForm(String formName, String username) {
        ArrayList<String> paramList = new ArrayList();
        paramList.add(formName);
        paramList.add(username);
        System.out.println(formName + username);
        try {
            return SqlExecutor.executePreparedStatement(this::getFormUserNameFunction, FORM_GET_USER_NAME_SQL, paramList);
        } catch (DaoException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    @Override
    public List<Form> getUsersForms(long userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String getFormIdUserNameFunction(PreparedStatement statement, long id) {
        try {
            statement.setLong(1, id);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            return rs.getString("author");
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }
    
    private Long createFormFunction(PreparedStatement statement, Form form) throws DaoException {
        try {
            statement.setString(1, form.getName());
            statement.setString(2, form.getAuthor());
            statement.setString(3, form.getDescription());
            statement.setString(4, form.getDate());
            int allowAnon = form.getAllowAnon() == true ? 1 : 0;
            statement.setInt(5, allowAnon);
            int publicForm = form.getPubliclyAvailable() == true ? 1 : 0;
            statement.setInt(6, publicForm);
            int showResults = form.getShowResults() == true ? 1 : 0;
            statement.setInt(7, showResults);
            try {
                statement.execute();
                ResultSet rs = statement.getGeneratedKeys();
                return rs.getLong(1);
            } catch (SQLException e) {
                throw new DaoException(Type.ERROR, e.getMessage());
            }
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }

    }

    private Boolean getFormUserNameFunction(PreparedStatement statement, ArrayList<String> params) throws DaoException {
        try {
            System.out.println(params.get(1) + params.get(0));
            statement.setString(1, params.get(1));
            statement.setString(2, params.get(0));
            try {
                statement.execute();
                ResultSet rs = statement.getResultSet();
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                throw new DaoException(Type.ERROR, e.getMessage());
            }
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private Form getFormIdFunction(PreparedStatement statement, long id) {
        try {
            statement.setLong(1, id);
            try {
                statement.execute();
                ResultSet rs = statement.getResultSet();
                return fillFormData(rs);
            } catch (SQLException e) {
                throw new DaoException(Type.ERROR, e.getMessage());
            }
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private Boolean deleteFormFunction(PreparedStatement statement, long id) {
        try {
            statement.setLong(1, id);
            int count = statement.executeUpdate();
            if (count == 0) {
                throw new DaoException(Type.ERROR, "Form doesn't exist");
            } else {
                return true;
            }
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private Form fillFormData(ResultSet rs) throws SQLException {
        Form form = new Form();
        boolean allowAnon = rs.getInt("allowAnon") == 1 ? true : false;
        form.setAllowAnon(allowAnon);
        form.setAuthor(rs.getString("author"));
        form.setDate(rs.getString("date"));
        form.setDescription(rs.getString("desc"));
//        boolean finished = rs.getInt("finished") == 1 ? true : false;
//        form.setFinished(finished);
        form.setId(rs.getInt("id"));
        form.setName(rs.getString("name"));
        boolean pAvailable = rs.getInt("public") == 1 ? true : false;
        form.setPubliclyAvailable(pAvailable);
        boolean showResults = rs.getInt("showResults") == 1 ? true : false;
        form.setShowResults(showResults);
        return form;
    }

}
