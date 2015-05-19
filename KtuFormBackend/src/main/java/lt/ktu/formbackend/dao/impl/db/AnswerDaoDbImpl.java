package lt.ktu.formbackend.dao.impl.db;

//<editor-fold desc="Imports">
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import lt.ktu.formbackend.dao.AnswerDao;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.DaoException.Type;
import lt.ktu.formbackend.dao.FormDao;
import lt.ktu.formbackend.dao.QuestionDao;
import lt.ktu.formbackend.dao.UserDao;
import lt.ktu.formbackend.model.Answer;
import lt.ktu.formbackend.model.AnswerContainer;
import lt.ktu.formbackend.model.AnswerRelation;
import lt.ktu.formbackend.model.AnswerStats;
import lt.ktu.formbackend.model.Form;
import lt.ktu.formbackend.model.FormAnswer;
import lt.ktu.formbackend.model.FormStats;
import lt.ktu.formbackend.model.Question;
//</editor-fold>

/**
 *
 * @author Lukas
 */
public class AnswerDaoDbImpl implements AnswerDao {

    private FormDao formDao;
    private QuestionDao questionDao;
    private UserDao userDao;

    private final String FORM_ANSWER_CREATE_SQL = "INSERT INTO FormAnswers (author, form) values (?, ?)";
    private final String ANSWER_CREATE_SQL = "INSERT INTO Answers (type, valueInteger, valueText, oneChoice, customText, multiChoice, questionNumber, customTextArray) values (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String ANSWER_RELATION_CREATE_SQL = "INSERT INTO AnswerRelations (formAnswer, answer) values (?, ?)";
    private final String ANSWER_FORM_GET_SQL = "SELECT Answers.id, type, valueInteger, valueText, oneChoice, customInteger, customText, multiChoice, questionNumber, customIntegerArray, customTextArray FROM ((Answers LEFT JOIN AnswerRelations ON Answers.id = AnswerRelations.answer) LEFT JOIN FormAnswers ON AnswerRelations.formAnswer = FormAnswers.id) LEFT JOIN Forms ON Forms.id = FormAnswers.form WHERE Forms.id = ?";
    private final String ANSWER_FORM_COUNT_SQL = "SELECT Count(*) FROM FormAnswers WHERE form = ?";

    public void initialize() {
        formDao = DaoFactory.getFormDao();
        questionDao = DaoFactory.getQuestionDao();
        userDao = DaoFactory.getUserDao();
    }
    
    @Override
    public ArrayList<AnswerStats> getFormQuestionStats(long formId) {
        ArrayList<Answer> answers = SqlExecutor.executePreparedStatement(this::getAnswersOfFormFunction, ANSWER_FORM_GET_SQL, formId);
        ArrayList<AnswerStats> answerStats = new ArrayList();
        ArrayList<Question> questions = questionDao.getQuestionsOfForm(formId);
        for (int i = 0; i < questions.size(); i++) {
            AnswerStats answerStat = new AnswerStats(questions.get(i), answers);
            answerStats.add(answerStat);
        }
        return answerStats;
    }
    
    @Override
    public ArrayList<FormStats> getUserFormStats(String username) {
        ArrayList<FormStats> formStats = new ArrayList();
        ArrayList<Form> forms = formDao.getUsersForms(userDao.getUserUsername(username).getId());
        ArrayList<Long> formIds = new ArrayList();
        for (int i = 0; i < forms.size(); formIds.add(forms.get(i).getId()), i++);
        for (int i = 0; i < formIds.size(); i++) {
            FormStats formStat = new FormStats();
            formStat.setAnswers(getFormQuestionStats(formIds.get(i)));
            formStat.setVotes(getVotesOfForm(formIds.get(i)));
            formStat.setFormName(forms.get(i).getName());
            formStats.add(formStat);
        }
        return formStats;
    }

    @Override
    public int getVotesOfForm(long formId) {
        return SqlExecutor.executePreparedStatement(this::getFormVoteCountFunction, ANSWER_FORM_COUNT_SQL, formId);
    }

    @Override
    public ArrayList<Answer> getAnswersOfForm(long formId) throws DaoException {
        return SqlExecutor.executePreparedStatement(this::getAnswersOfFormFunction, ANSWER_FORM_GET_SQL, formId);
    }

    @Override
    public long createFormAnswer(String author, long formId, ArrayList<Answer> answers) {
        FormAnswer formAnswer = new FormAnswer();
        formAnswer.setAuthor(author);
        formAnswer.setForm(formId);
        long formAnswerId = SqlExecutor.executePreparedStatement(this::createFormAnswerFunction, FORM_ANSWER_CREATE_SQL, formAnswer);
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            answer.setQuestionNumber(i);
            createAnswer(answers.get(i), formAnswerId);
        }
        return formAnswerId;
    }

    @Override
    public long createAnswer(Answer answer, long formAnswerId) {
        long answerId = SqlExecutor.executePreparedStatement(this::createAnswerFunction, ANSWER_CREATE_SQL, answer);
        createAnswerRelation(formAnswerId, answerId);
        return answerId;
    }

    private boolean createAnswerRelation(long formAnswerId, long answerId) {
        AnswerRelation answerRelation = new AnswerRelation();
        answerRelation.setFormAnswer(formAnswerId);
        answerRelation.setAnswer(answerId);
        return SqlExecutor.executePreparedStatement(this::createAnswerRelationFunction, ANSWER_RELATION_CREATE_SQL, answerRelation);
    }

    private int getFormVoteCountFunction(PreparedStatement statement, long formId) {
        try {
            statement.setLong(1, formId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private boolean createAnswerRelationFunction(PreparedStatement statement, AnswerRelation answerRelation) {
        try {
            statement.setLong(1, answerRelation.getFormAnswer());
            statement.setLong(2, answerRelation.getAnswer());
            if (statement.execute()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private ArrayList<Answer> getAnswersOfFormFunction(PreparedStatement statement, long formId) {
        try {
            statement.setLong(1, formId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            return fillAnswerArray(rs);
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private long createFormAnswerFunction(PreparedStatement statement, FormAnswer formAnswer) {
        try {
            statement.setString(1, formAnswer.getAuthor());
            statement.setLong(2, formAnswer.getForm());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private long createAnswerFunction(PreparedStatement statement, Answer answer) {
        try {
            if (answer.getType() != null) {
                statement.setString(1, answer.getType());
            }
            statement.setInt(2, answer.getValueInteger());
            if (answer.getValueText() != null) {
                statement.setString(3, answer.getValueText());
            }
            statement.setInt(4, answer.getOneChoice());
            if (answer.getCustomText() != null) {
                statement.setString(5, answer.getCustomText());
            }
            if (!(answer.getMultiChoice() == null || answer.getMultiChoice().isEmpty())) {
                statement.setString(6, answer.getMultiChoice().toString());
            }
            statement.setInt(7, answer.getQuestionNumber());
            if (!(answer.getCustomStringArray() == null || answer.getCustomStringArray().isEmpty())) {
                statement.setString(8, answer.getCustomStringArray().toString());
            }
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private ArrayList<Answer> fillAnswerArray(ResultSet rs) {
        ArrayList<Answer> answers = new ArrayList();
        try {
            while (rs.next()) {
                Answer answer = new Answer();
                answer.setCustomInteger(rs.getInt("customInteger"));
                if (rs.getString("customIntegerArray") != null) {
                    ArrayList<String> customIntegerArrayString = new ArrayList();
                    ArrayList<Integer> customIntegerArray = new ArrayList();
                    customIntegerArrayString.addAll(Arrays.asList(rs.getString("customIntegerArray").substring(1, rs.getString("customIntegerArray").length() - 1).split("\\s*,\\s*")));
                    for (int i = 0; i < customIntegerArrayString.size(); customIntegerArray.add(Integer.parseInt(customIntegerArrayString.get(i))), i++);
                    answer.setCustomIntegerArray(customIntegerArray);
                }
                if (rs.getString("customTextArray") != null) {
                    ArrayList<String> customStringArray = new ArrayList();
                    customStringArray.addAll(Arrays.asList(rs.getString("customTextArray").substring(1, rs.getString("customTextArray").length() - 1).split("\\s*,\\s*")));
                    answer.setCustomStringArray(customStringArray);
                }
                answer.setCustomText(rs.getString("customText"));
                if (rs.getString("multiChoice") != null) {
                    ArrayList<String> multiChoiceArrayString = new ArrayList();
                    ArrayList<Boolean> multiChoiceArray = new ArrayList();
                    multiChoiceArrayString.addAll(Arrays.asList(rs.getString("multiChoice").substring(1, rs.getString("multiChoice").length() - 1).split("\\s*,\\s*")));
                    for (int i = 0; i < multiChoiceArrayString.size(); multiChoiceArray.add(Boolean.parseBoolean(multiChoiceArrayString.get(i))), i++);
                    answer.setMultiChoice(multiChoiceArray);
                }
                answer.setOneChoice(rs.getInt("oneChoice"));
                answer.setQuestionNumber(rs.getInt("questionNumber"));
                answer.setType(rs.getString("type"));
                answer.setValueInteger(rs.getInt("valueInteger"));
                answer.setValueText(rs.getString("valueText"));
                answer.setId(rs.getLong("id"));
                answers.add(answer);
            };
            return answers;
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

}
