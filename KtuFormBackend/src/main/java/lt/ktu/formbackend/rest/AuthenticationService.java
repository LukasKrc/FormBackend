package lt.ktu.formbackend.rest;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;
import javax.servlet.ServletRequest;
import lt.ktu.formbackend.dao.UserDao;
import lt.ktu.formbackend.dao.impl.db.DaoFactory;
import lt.ktu.formbackend.model.User;

/**
 *
 * @author Lukas
 */
public class AuthenticationService {
    
    private UserDao userDao = DaoFactory.getUserDao();
    
    public boolean authenticate(String authCredentials, ServletRequest request) {
        if (authCredentials == null)
            return false;
        final String encodedUserPassword = authCredentials.replaceFirst("Basic ", "");
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        User user = userDao.getUserUsername(username);
        return password.equals(user.getPassword());
    }
}
