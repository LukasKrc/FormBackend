package lt.ktu.formbackend.dao.impl.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.DaoException.Type;
import lt.ktu.formbackend.dao.QuestionDao;
import lt.ktu.formbackend.model.Question;

/**
 *
 * @author Lukas
 */
public class QuestionDaoDbImpl implements QuestionDao {

    private final String QUESTION_CREATE_SQL = "INSERT INTO Questions (name, type, allowEmpty, choices, minVal, maxVal, allowWs, allowNl, desc, allowCustom, maxChoices, minChoices, allowedProviders, questionNumber) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String QUESTION_RELATION_CREATE_SQL = "INSERT INTO QuestionRelations (question, form) values (?, ?)";
    private final String QUESTION_OF_FORM_SQL = "SELECT Questions.id, Questions.name, type, allowEmpty, choices, minVal, maxVal, allowWs, allowNl, Questions.desc, allowCustom, maxChoices, minChoices, allowedProviders FROM (Forms LEFT JOIN QuestionRelations ON Forms.id = QuestionRelations.form)"
            + "LEFT JOIN Questions ON QuestionRelations.question = Questions.id "
            + "WHERE Forms.id = ?";

    @Override
    public long createQuestion(Question question, long formId) throws DaoException {
        long questionId = SqlExecutor.executePreparedStatement(this::createQuestionFunction, QUESTION_CREATE_SQL, question);
        ArrayList<Long> params = new ArrayList();
        params.add(formId);
        params.add(questionId);
        SqlExecutor.executePreparedStatement(this::createQuestionRelationFunction, QUESTION_RELATION_CREATE_SQL, params);
        return questionId;
    }

    @Override
    public ArrayList<Question> getQuestionsOfForm(long formId) {
        try {
            return SqlExecutor.executePreparedStatement(this::getQuestionsOfFormFunction, QUESTION_OF_FORM_SQL, formId);
        } catch (DaoException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private boolean createQuestionRelationFunction(PreparedStatement statement, ArrayList<Long> params) {
        try {
            statement.setLong(1, params.get(1));
            statement.setLong(2, params.get(0));
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private ArrayList<Question> getQuestionsOfFormFunction(PreparedStatement statement, long formId) {
        try {
            statement.setLong(1, formId);
            statement.execute();
            return fillQuestionArray(statement.getResultSet());
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private long createQuestionFunction(PreparedStatement statement, Question question) {
        try {
            statement.setString(1, question.getName());
            statement.setString(2, question.getType());
            int allowEmpty = question.getAllowEmpty() ? 1 : 0;
            statement.setInt(3, allowEmpty);
            if (question.getChoices() != null && !question.getChoices().isEmpty()) {
                statement.setString(4, question.getChoices().toString());
            }
            if (question.getMinValue() != null) {
                statement.setInt(5, question.getMinValue());
            }
            if (question.getMaxValue() != null) {
                statement.setInt(6, question.getMaxValue());
            }
            if (question.getAllowWhitespace() != null) {
                int allowWhiteSpace = question.getAllowWhitespace() ? 1 : 0;
                statement.setInt(7, allowWhiteSpace);
            }
            if (question.getAllowNewLines() != null) {
                int allowNewLines = question.getAllowNewLines() ? 1 : 0;
                statement.setInt(8, allowNewLines);
            }
            if (question.getDescription() != null) {
                statement.setString(9, question.getDescription());
            }
            if (question.getAllowCustom() != null) {
                statement.setString(10, question.getAllowCustom());
            }
            if (question.getMaxChoices() != null) {
                statement.setInt(11, question.getMaxChoices());
            }
            if (question.getMinChoices() != null) {
                statement.setInt(12, question.getMinChoices());
            }
            if (question.getAllowedProviders() != null) {
                statement.setString(13, question.getAllowedProviders().toString());
            }
            if (question.getQuestionNumber() != null) {
                statement.setInt(14, question.getQuestionNumber());
            }
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
    }

    private ArrayList<Question> fillQuestionArray(ResultSet rs) {
        ArrayList<Question> questions = new ArrayList();
        try {
            while (rs.next()) {
                Question question = new Question();
                question.setAllowCustom(rs.getString("allowCustom"));
                boolean allowEmpty = rs.getInt("allowEmpty") == 1 ? true : false;
                question.setAllowEmpty(allowEmpty);
                boolean allowNewLines = rs.getInt("allowNl") == 1 ? true : false;
                question.setAllowNewLines(allowNewLines);
                boolean allowWhiteSpace = rs.getInt("allowWs") == 1 ? true : false;
                question.setAllowWhitespace(allowWhiteSpace);
                ArrayList<String> choices = new ArrayList();// = (ArrayList) Arrays.asList(rs.getString("choices").substring(1, rs.getString("choices").length() - 1).split("\\s*,\\s*"));
                if (rs.getString("choices") != null) {
                    choices.addAll(Arrays.asList(rs.getString("choices").substring(1, rs.getString("choices").length() - 1).split("\\s*,\\s*")));
                }
                question.setChoices(choices);
                question.setDescription(rs.getString("desc"));
                question.setMaxChoices(rs.getInt("maxChoices"));
                question.setMinChoices(rs.getInt("minChoices"));
                question.setMaxValue(rs.getInt("maxVal"));
                question.setMinValue(rs.getInt("minVal"));
                question.setName(rs.getString("name"));
                question.setType(rs.getString("type"));
                ArrayList<String> allowedProviders = new ArrayList();
                if (rs.getString("allowedProviders") != null) {
                    allowedProviders.addAll(Arrays.asList(rs.getString("allowedProviders").substring(1, rs.getString("allowedProviders").length() -  1).split("\\s*,\\s*")));
                }
                question.setAllowedProviders(allowedProviders);
                questions.add(question);
            }
        } catch (SQLException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
        return questions;
    }

}
