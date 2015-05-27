package lt.ktu.formbackend.rest;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;
import javax.servlet.ServletRequest;
import lt.ktu.formbackend.dao.UserDao;
import lt.ktu.formbackend.dao.impl.db.DaoFactory;
import lt.ktu.formbackend.model.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lukas
 */
public class AuthenticationService {

    private final UserDao userDao = DaoFactory.getUserDao();

    public String md5Digest(String text) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AuthenticationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    
    public boolean authenticate(String authCredentials, ServletRequest request) {
        if (authCredentials == null) {
            return false;
        }
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
        if (username.equals("Anonymous")) {
            request.setAttribute("username", "Anonymous");
            return true;
        }
        String password = tokenizer.nextToken();
        password = this.md5Digest(password);
        System.out.println(password);
	try {
            User user = userDao.getUserUsername(username);
            if (user == null) return false;
            if (password.equals(user.getPassword())) {
                request.setAttribute("username", user.getUsername());
                return true;
            } else {
                return false;
            }
        } catch(Exception e) { return false; }
    }
}
