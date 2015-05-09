package lt.ktu.formbackend.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.impl.db.UserDaoDbImpl;
import lt.ktu.formbackend.model.User;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Lukas
 */
@Path("/user")
public class UserResource {

    private UserDaoDbImpl userDao;

    @Context
    HttpServletRequest request;

    public UserResource() {
        userDao = new UserDaoDbImpl();
    }

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("username") String username) {
        if (username == null) {
            return Response.serverError().entity("Username can't be blank").build();
        }
        try {
            User user = userDao.getUserUsername(username);
            JSONObject userObject = new JSONObject();
            try {
                if (user.getIsCompany())
                    userObject.put("company", user.getCompany());
                else {
                    userObject.put("name", user.getName());
                    userObject.put("surname", user.getSurname());
                } 
            } catch (JSONException e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
            String userJson = userObject.toString();
            return Response.ok(userJson/*userDao.getUserUsername(username)*/, MediaType.APPLICATION_JSON).build();
        } catch (DaoException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        if (user != null) {
            try {
                userDao.createUser(user);
            } catch (DaoException e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
            return Response.ok("/user/" + user.getUsername()).build();
        } else {
            return Response.serverError().entity("Please provide the user credentials.").build();
        }
    }

    @PUT
    @Path("/{username}")
    @Consumes("application/json")
    public Response updateUser(@PathParam("username") String username, User user) {
        if (((String) request.getAttribute("username")).equals(username)) {
            userDao.updateUser(user, username);
            return Response.ok().build();
        }
        else 
            return Response.serverError().entity("You can only update your own account").build();
    }
    
    @DELETE
    @Path("/{username}")
    public Response deleteUser(@PathParam("username") String username) {
        System.out.println(request.getAttribute("username"));
        if (((String) request.getAttribute("username")).equals(username)) {
            try {
                userDao.deleteUser(username);
                return Response.ok().build();
            } catch (DaoException e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
        } else {
            return Response.serverError().entity("You can only delete your own accout").build();
        }
    }
}
