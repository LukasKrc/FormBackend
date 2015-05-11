package lt.ktu.formbackend.rest;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;
import javax.servlet.ServletRequest;

/**
 *
 * @author Lukas
 */
public class AuthenticationService {
    public boolean authenticate(String authCredentials, ServletRequest request) {
        request.setAttribute("username", "Lukas");
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
        return username.equals("Lukas") && password.equals("password");
    }
}
