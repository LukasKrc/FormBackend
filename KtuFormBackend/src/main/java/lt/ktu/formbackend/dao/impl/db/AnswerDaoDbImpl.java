package lt.ktu.formbackend.dao.impl.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import lt.ktu.formbackend.dao.AnswerDao;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.DaoException.Type;
import lt.ktu.formbackend.model.Answer;
import lt.ktu.formbackend.model.AnswerRelation;
import lt.ktu.formbackend.model.AnswerStats;
import lt.ktu.formbackend.model.FormAnswer;
import lt.ktu.formbackend.model.Question;

/**
 *
 * @author Lukas
 */
public class AnswerDaoDbImpl implements AnswerDao {

    private FormDaoDbImpl formDao = new FormDaoDbImpl();
    private QuestionDaoDbImpl questionDao = new QuestionDaoDbImpl();

    private final String FORM_ANSWER_CREATE_SQL = "INSERT INTO FormAnswers (author, form) values (?, ?)";
    private final String ANSWER_CREATE_SQL = "INSERT INTO Answers (type, valueInteger, valueText, oneChoice, customText, multiChoice, questionNumber, customTextArray) values (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String ANSWER_RELATION_CREATE_SQL = "INSERT INTO AnswerRelations (formAnswer, answer) values (?, ?)";
    private final String ANSWER_FORM_GET_SQL = "SELECT Answers.id, type, valueInteger, valueText, oneChoice, customInteger, customText, multiChoice, questionNumber, customIntegerArray, customTextArray FROM ((Answers LEFT JOIN AnswerRelations ON Answers.id = AnswerRelations.answer) LEFT JOIN FormAnswers ON AnswerRelations.formAnswer = FormAnswers.id) LEFT JOIN Forms ON Forms.id = FormAnswers.form WHERE Forms.id = ?";
    private final String ANSWER_FORM_COUNT_SQL = "SELECT Count(*) FROM FormAnswers WHERE form = ?";
    
    
    @Override
    public ArrayList<AnswerStats> getFormQuestionStats(long formId) {
        ArrayList<Answer> answers = SqlExecutor.executePreparedStatement(this::getAnswersOfFormFunction, ANSWER_FORM_GET_SQL, formId);
        ArrayList<AnswerStats> answerStats = new ArrayList();
        ArrayList<Question> questions = questionDao.getQuestionsOfForm(formId);
        for (int i = 0; i < questions.size(); i++) {
            AnswerStats answerStat = new AnswerStats();
            ArrayList<String> choices = new ArrayList();
            ArrayList<Integer> votes = new ArrayList();
            Question question = questions.get(i);
            switch (question.getType()) {
                    case "integer":     for (int x = 0; x < answers.size(); x++) {
                                            if (answers.get(x).getType().equals("integer")) {
                                                if (!choices.contains(Integer.toString(answers.get(x).getValueInteger()))) {
                                                    choices.add(Integer.toString(answers.get(x).getValueInteger()));
                                                    votes.add(1);
                                                } else {
                                                    int choicePosition = choices.indexOf(Integer.toString(answers.get(x).getValueInteger()));
                                                    votes.set(choicePosition, votes.get(choicePosition) + 1);
                                                }
                                            }
                                        }
                                        break;
                        
                    case "string":      for (int x = 0; x < answers.size(); x++) {
                                            if (answers.get(x).getType().equals("string")) {
                                                if (!choices.contains(answers.get(x).getValueText())) {
                                                    choices.add(answers.get(x).getValueText());
                                                    votes.add(1);
                                                } else {
                                                    int choicePosition = choices.indexOf(answers.get(x).getValueText());
                                                    votes.set(choicePosition, votes.get(choicePosition) + 1);
                                                }
                                            }
                                        }
                                        break;

                    case "one-choice":  choices.addAll(question.getChoices());
                                        for (int y = 0; y < question.getChoices().size(); votes.add(0), y++);
                                        for (int x = 0; x < answers.size(); x++) {
                                            if (answers.get(x).getType().equals("one-choice")) {
                                                if (answers.get(x).getCustomText() != null) {
                                                    if (!choices.contains(answers.get(x).getCustomText())) {
                                                        choices.add(answers.get(x).getCustomText());
                                                        votes.add(1);
                                                    } else {
                                                        int choicePosition = choices.indexOf(answers.get(x).getCustomText());
                                                        votes.set(choicePosition, votes.get(choicePosition) + 1);
                                                    }
                                                } else {
                                                    votes.set(answers.get(x).getOneChoice(), votes.get(answers.get(x).getOneChoice()) + 1);
                                                }
                                            }
                                        }
                                        break;

                    case "multi-choice":
                                        choices.addAll(question.getChoices());
                                        for (int y = 0; y < question.getChoices().size(); votes.add(0), y++);
                                        for (int x = 0; x < answers.size(); x++) {
                                            if (answers.get(x).getType().equals("multi-choice")) {
                                                if (answers.get(x).getCustomStringArray() != null) {
                                                    for (int j = 0; j < answers.get(x).getCustomStringArray().size(); j++) {
                                                        if (!choices.contains(answers.get(x).getCustomStringArray().get(j))) {
                                                            choices.add(answers.get(x).getCustomStringArray().get(j));
                                                            votes.add(1);
                                                        } else {
                                                            int choicePosition = choices.indexOf(answers.get(x).getCustomStringArray().get(j));
                                                            votes.set(choicePosition, votes.get(choicePosition) + 1);
                                                        }
                                                    }
                                                } else {
                                                    for (int b = 0; b < answers.get(x).getMultiChoice().size(); b++) {
                                                        if (answers.get(x).getMultiChoice().get(b))
                                                            votes.set(b, votes.get(b) + 1);
                                                    }
                                                }
                                            }
                                        }
                                        break;
                }
            answerStat.setChoices(choices);
            answerStat.setVotes(votes);
            answerStats.add(answerStat);
        }
        return answerStats;
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
        try {
            long formAnswerId = SqlExecutor.executePreparedStatement(this::createFormAnswerFunction, FORM_ANSWER_CREATE_SQL, formAnswer);
            for (int i = 0; i < answers.size(); i++) {
                Answer answer = answers.get(i);
                answer.setQuestionNumber(i);
                createAnswer(answers.get(i), formAnswerId);
            }
            return formAnswerId;
        } catch (DaoException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
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
        try {
            return SqlExecutor.executePreparedStatement(this::createAnswerRelationFunction, ANSWER_RELATION_CREATE_SQL, answerRelation);
        } catch (DaoException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
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
