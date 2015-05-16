package lt.ktu.formbackend.dao.impl.db;

import java.util.ArrayList;
import lt.ktu.formbackend.model.Form;
import lt.ktu.formbackend.model.Question;
import lt.ktu.formbackend.model.User;

/**
 *
 * @author Lukas
 */
public class SQLBuilder {

    public static String buildQuestionUpdateSql(Question question) {
        if (question.getAllowEmpty() == null && question.getAllowNewLines() == null
                && question.getAllowWhitespace() == null && question.getAllowCustom() == null
                && (question.getAllowedProviders() == null || question.getAllowedProviders().size() == 0)
                && (question.getChoices() == null || question.getChoices().size() == 0)
                && question.getDescription() == null && question.getMaxChoices() == null
                && question.getMinChoices() == null && question.getMinValue() == null
                && question.getName() == null && question.getType() == null)
            return null;
        else {
            String sql = "UPDATE Questions SET ";
            if (question.getAllowEmpty() != null) {
                sql = sql + "allowEmpty = ? ,";
            }
            if (question.getAllowNewLines() != null) {
                sql = sql + "allowNl = ? ,";
            }
            if (question.getAllowWhitespace() != null) {
                sql = sql + "allowWs = ? ,";
            }
            if (question.getAllowCustom() != null) {
                sql = sql + "allowCustom = ? ,";
            }
            if (question.getAllowedProviders() != null && question.getAllowedProviders().size() != 0) {
                sql = sql + "allowedProviders = ? ,";
            }
            if (question.getChoices() != null && question.getChoices().size() != 0) {
                sql = sql + "choices = ? ,";
            }
            if (question.getDescription() != null) {
                sql = sql + "desc = ? ,";
            }
            if (question.getMaxChoices() != null) {
                sql = sql + "maxChoices = ? ,";
            }
            if (question.getMinChoices() != null) {
                sql = sql + "minChoices = ? ,";
            }
            if (question.getMinValue() != null) {
                sql = sql + "minVal = ? ,";
            }
            if (question.getName() != null) {
                sql = sql + "name = ? ,";
            }
            if (question.getType() != null) {
                sql = sql + "type = ? ,";
            } 
            if (sql.substring(sql.length() - 1).equals(",")) {
                sql = sql.substring(0, sql.length() - 1);
            }
            sql = sql + " WHERE Questions.id = ?";
            return sql;
        }
    }
    
    public static String buildUserUpdateSQL(User user, String username) {
        if (user.getIsCompany() == null && user.getCompany() == null 
                && user.getName() == null && user.getPassword() == null
                && user.getSurname() == null)
            return null;
        else {
            String sql = "UPDATE Users SET ";
            if (user.getIsCompany() != null) {
                sql = sql + "isCompany = ? ,";
            }
            if (user.getCompany() != null) {
                sql = sql + "company = ? ,";
            }
            if (user.getName() != null) {
                sql = sql + "name = ? ,";
            }
            if (user.getPassword() != null) {
                sql = sql + "password = ? ,";
            }
            if (user.getSurname() != null) {
                sql = sql + "surname = ? ,";
            }
            if (sql.substring(sql.length() - 1).equals(","))
                sql = sql.substring(0, sql.length() - 1);
            sql = sql + "WHERE username=\"" + username +"\"";
            return sql;
        }
    }
    
    public static String buildFormUpdateSQL(Form form) {
        if (form.getAllowAnon() == null && form.getPubliclyAvailable() == null 
                && form.getShowResults() == null && form.getDescription() == null 
                && form.getName() == null)
            return null;
        else {
            String sql = "UPDATE Forms SET ";
            if (form.getAllowAnon() != null) {
                sql = sql + "allowAnon = ? ,";
            }
            if (form.getPubliclyAvailable() != null) {
                sql = sql + "public = ? ,";
            }
            if (form.getShowResults() != null) {
                sql = sql + "showResults = ? ,";
            }
            if (form.getDescription() != null) {
                sql = sql + "desc = ? ,";
            }
            if (form.getName() != null) {
                sql = sql + "name = ? ,";
            }
            if (sql.substring(sql.length() - 1).equals(","))
                sql = sql.substring(0, sql.length() - 1);
            sql = sql + "WHERE id = ?";
            return sql;
        }
    }
}
