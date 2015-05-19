package lt.ktu.formbackend.dao.impl.db;

//<editor-fold desc="Imports">
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import lt.ktu.formbackend.dao.AnswerDao;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.DaoException.Type;
import lt.ktu.formbackend.dao.FormDao;
import lt.ktu.formbackend.dao.QuestionDao;
import lt.ktu.formbackend.dao.UserDao;
import lt.ktu.formbackend.model.Form;
import lt.ktu.formbackend.model.Question;
import lt.ktu.formbackend.model.SearchQuery;
//</editor-fold>

/**
 *
 * @author Lukas
 */
public class FormDaoDbImpl implements FormDao {

    private HttpServletRequest request;

    private static final String FORM_CREATE_SQL = "INSERT INTO Forms (name, author, desc, date, allowAnon, public, showResults) values (?, ?, ?, ?, ?, ?, ?)";
    private static final String FORM_GET_USER_NAME_SQL = "SELECT id, name, author, desc, date, allowAnon, public, showResults FROM Forms WHERE author = ? AND name = ?";
    private static final String FORM_GET_ID_SQL = "SELECT id, name, author, desc, date, allowAnon, public, showResults FROM Forms WHERE id = ?";
    private static final String FORM_DELETE_SQL = "DELETE FROM Forms WHERE id = ?";
    private static final String FORM_GET_USER_NAME_ID_SQL = "SELECT author FROM Forms WHERE id = ?";
    private static final String FORM_GET_ID_BY_USER_NAME_SQL = "SELECT id FROM Forms WHERE name = ? AND author = ?";
    private static final String FORM_SEARCH_SQL = "SELECT Forms.id, name, author, desc, date, allowAnon, public, showResults FROM (Forms LEFT JOIN TagRelations ON Forms.id = TagRelations.form) LEFT JOIN Tags ON Tags.id = TagRelations.tag WHERE ((Forms.name LIKE ? AND ? = 1) OR (Tags.tag LIKE ? AND ? = 1)) AND (Forms.author = ? OR ? = 1) AND (Forms.allowAnon = ? OR ? = 1)";
    private static final String FORM_GET_TAGS_SQL = "SELECT Tags.tag FROM (Forms LEFT JOIN TagRelations ON Forms.id = TagRelations.form) LEFT JOIN Tags ON Tags.id = TagRelations.tag WHERE Forms.id = ?";
    private static final String FORM_GET_IDS_USER_SQL = "SELECT Forms.id FROM Forms LEFT JOIN Users ON Forms.author = Users.username WHERE Users.id = ?";

    private AnswerDao answerDao;
    private QuestionDao questionDao;
    private UserDao userDao;

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void initialize() {
        answerDao = DaoFactory.getAnswerDao();
        questionDao = DaoFactory.getQuestionDao();
        userDao = DaoFactory.getUserDao();
    }

    @Override
    public Boolean updateForm(long id, Form form) {
        form.setId(id);
        return SqlExecutor.executePreparedStatement(this::updateFormFunction, SQLBuilder.buildFormUpdateSQL(form), form);
    }

    @Override
    public ArrayList<Form> searchForms(SearchQuery query) {
        return SqlExecutor.executePreparedStatement(this::searchFormsFunction, FORM_SEARCH_SQL, query);
    }

    @Override
    public Long getIdOfForm(Form form) {
        return SqlExecutor.executePreparedStatement(this::getIdOfFormFunction, FORM_GET_ID_BY_USER_NAME_SQL, form);
    }

    @Override
    public Boolean deleteForm(long id) {
        Form form = getFormId(id);
        return SqlExecutor.executePreparedStatement(this::deleteFormFunction, FORM_DELETE_SQL, form);
    }

    @Override
    public Long createForm(Form form) {
        if (form.hasMandatoryFields() != null) {
            throw new DaoException(Type.ERROR, "Form is missing mandatory fields: " + form.hasMandatoryFields());
        } else {
            long formId = SqlExecutor.executePreparedStatement(this::createFormFunction, FORM_CREATE_SQL, form);
            for (int i = 0; i < form.getQuestions().size(); i++) {
                Question question = form.getQuestions().get(i);
                question.setQuestionNumber(i);
                questionDao.createQuestion(question, formId);
            }
            return formId;
        }
    }

    @Override
    public Form getFormId(long id) {
        return SqlExecutor.executePreparedStatement(this::getFormIdFunction, FORM_GET_ID_SQL, id);
    }

    @Override
    public String getFormAuthor(long id) {
        return SqlExecutor.executePreparedStatement(this::getFormIdUserNameFunction, FORM_GET_USER_NAME_ID_SQL, id);
    }

    @Override
    public Boolean userHasForm(String formName, String username) {
        ArrayList<String> paramList = new ArrayList();
        paramList.add(formName);
        paramList.add(username);
        System.out.println(formName + username);
        return SqlExecutor.executePreparedStatement(this::getFormUserNameFunction, FORM_GET_USER_NAME_SQL, paramList);
    }

    @Override
    public ArrayList<Form> getUsersForms(long userId) {
        ArrayList<Form> forms = new ArrayList();
        ArrayList<Long> ids = getUsersFormIds(userId);
        for (int i = 0; i < ids.size(); i++) {
            forms.add(getFormId(ids.get(i)));
        }
        return forms;
    }

    private ArrayList<Long> getUsersFormIds(long userId) {
        return SqlExecutor.executePreparedStatement(this::getUsersFormIdsFunction, FORM_GET_IDS_USER_SQL, userId);
    }

    private ArrayList<String> getTagsOfForm(long formId) {
        return SqlExecutor.executePreparedStatement(this::getTagsOfFormFunction, FORM_GET_TAGS_SQL, formId);
    }

    private Boolean updateFormFunction(PreparedStatement statement, Form form) {
        questionDao.updateQuestionsOfForm(form);
        try {
            int i = 1;
            if (form.getAllowAnon() != null) {
                Integer allowAnon = form.getAllowAnon() ? 1 : 0;
                statement.setInt(i++, allowAnon);
            }
            if (form.getPubliclyAvailable() != null) {
                Integer publiclyAvailable = form.getPubliclyAvailable() ? 1 : 0;
                statement.setInt(i++, publiclyAvailable);
            }
            if (form.getShowResults() != null) {
                Integer showResults = form.getShowResults() ? 1 : 0;
                statement.setInt(i++, showResults);
            }
            if (form.getDescription() != null) {
                statement.setString(i++, form.getDescription());
            }
            if (form.getName() != null) {
                statement.setString(i++, form.getName());
            }
            statement.setLong(i, form.getId());
            System.out.println(i);
            if (statement.executeUpdate() > 0) {
                return true;
            } else {
                throw new DaoException(Type.ERROR, "Form update failed");
            }
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private ArrayList<Long> getUsersFormIdsFunction(PreparedStatement statement, long userId) {
        ArrayList<Long> formIds = new ArrayList();
        try {
            statement.setLong(1, userId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                formIds.add(rs.getLong("id"));
            }
            return formIds;
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private ArrayList<String> getTagsOfFormFunction(PreparedStatement statement, long formId) {
        ArrayList<String> tags = new ArrayList();
        try {
            statement.setLong(1, formId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                tags.add(rs.getString("tag"));
            }
            if (tags.size() == 0) {
                throw new DaoException(Type.ERROR, "Form has no tags");
            }
            return tags;
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private ArrayList<Form> searchFormsFunction(PreparedStatement statement, SearchQuery query) {
        ArrayList<Form> forms = new ArrayList();
//        if (query.getQuery() == null) {
//            throw new DaoException(Type.ERROR, "Search query is empty.");
//        }
        try {
            for (int i = 0; i < query.getTags().size() || (query.getTags().size() == 0 && i < 1); i++) {
                if (query.getQuery() == null || query.getQuery().equals("")) {
                    statement.setInt(2, 0);
                    statement.setString(1, "%%");
                } else {
                    String namePattern = "%" + query.getQuery() + "%";
                    statement.setString(1, namePattern);
                    statement.setInt(2, 1);
                }
                if (query.getTags() == null || query.getTags().size() == 0) {
                    statement.setString(3, "%%");
                    statement.setInt(4, 0);
                } else {
                    String tagPattern = "%" + query.getTags().get(i) + "%";
                    statement.setString(3, tagPattern);
                    statement.setInt(4, 1);
                }
                if (query.getAuthor() != null && !query.getAuthor().equals("")) {
                    statement.setString(5, query.getAuthor());
                } else {
                    statement.setString(5, "");
                    statement.setInt(6, 1);
                }
                if (query.getAllowAnon() != null && !query.getAllowAnon().equals("")) {
                    int allowAnon = query.getAllowAnon().equals("true") ? 1 : 0;
                    statement.setInt(7, allowAnon);
                    statement.setInt(8, 0);
                } else {
                    statement.setInt(7, 1);
                    statement.setInt(8, 1);
                }
                if ((query.getQuery() == null || query.getQuery().equals("")) && (query.getTags() == null || query.getTags().size() == 0)) {
                    statement.setInt(2, 1);
                    statement.setInt(4, 1);
                }
                statement.execute();
                ResultSet rs = statement.getResultSet();
                ArrayList<Form> formsResult = fillFormArray(rs);
                for (int y = 0; y < formsResult.size(); y++) {
                    boolean contains = false;
                    for (int x = 0; x < forms.size(); x++) {
                        if (forms.get(x).getId() == formsResult.get(y).getId()) {
                            contains = true;
                        }
                    }
                    if (!contains) {
                        Form form = formsResult.get(y);
                        form.setTags(getTagsOfForm(form.getId()));
                        form.setVotes(answerDao.getVotesOfForm(form.getId()));
                        forms.add(formsResult.get(y));
                    }
                }
            }
            return forms;
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private String getFormIdUserNameFunction(PreparedStatement statement, long id) {
        try {
            statement.setLong(1, id);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            if (!rs.next()) {
                throw new DaoException(Type.ERROR, "Form doesn't exist");
            }
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
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }

    }

    private Boolean getFormUserNameFunction(PreparedStatement statement, ArrayList<String> params) throws DaoException {
        try {
            statement.setString(1, params.get(1));
            statement.setString(2, params.get(0));
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
    }

    private Form getFormIdFunction(PreparedStatement statement, long id) {
        try {
            statement.setLong(1, id);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            if (!rs.next()) {
                throw new DaoException(Type.ERROR, "Form doesn't exist");
            }
            Form form = new Form();
            form = fillFormData(rs);
            form.setQuestions(questionDao.getQuestionsOfForm(id));
            return form;
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private Boolean deleteFormFunction(PreparedStatement statement, Form form) {
        try {
            statement.setLong(1, form.getId());
            int count = statement.executeUpdate();
            if (count == 0) {
                throw new DaoException(Type.ERROR, "Form doesn't exist");
            } else {
                for (int i = 0; i < form.getQuestions().size(); i++) {
                    questionDao.deleteQuestion(form.getQuestions().get(i).getId());
                }
                questionDao.deleteQuestionRelations(form.getId());
                return true;
            }
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private long getIdOfFormFunction(PreparedStatement statement, Form form) {
        try {
            statement.setString(1, form.getName());
            statement.setString(2, form.getAuthor());
            statement.execute();
            ResultSet rs = statement.getResultSet();
            return rs.getLong("id");
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private ArrayList<Form> fillFormArray(ResultSet rs) throws SQLException {
        ArrayList<Form> forms = new ArrayList();
        while (rs.next()) {
            forms.add(fillFormData(rs));
        }
        return forms;
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
        form.setFinished(userDao.userFinishedForm((String) request.getAttribute("username"), form.getId()));
        boolean pAvailable = rs.getInt("public") == 1 ? true : false;
        form.setPubliclyAvailable(pAvailable);
        boolean showResults = rs.getInt("showResults") == 1 ? true : false;
        form.setShowResults(showResults);
        return form;
    }

}
