package lt.ktu.formBackend.restlet;

import lt.ktu.formBackend.dao.DaoException;
import lt.ktu.formBackend.dao.DaoFactory;
import lt.ktu.formBackend.dao.UserDao;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class UserResource extends ServerResource {

    private UserDao userDao;

    public UserResource() {
        userDao = DaoFactory.getUserDao("Sqlite");
    }

    @Get
    public String toString() {
        String username = (String) getRequest().getAttributes().get("username");
        try {
            return userDao.getUserUsername(username).getName();
        } catch (DaoException e) {
            return e.getMessage();
        }
//        return "hello, world asgdsag";
    }

    @Post
    public String Post() {
        return "derp";
    }

}
