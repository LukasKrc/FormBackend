package lt.ktu.formbackend.dao.impl.db;

import java.util.ArrayList;
import lt.ktu.formbackend.model.User;

/**
 *
 * @author Lukas
 */
public class SQLBuilder {

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
}
