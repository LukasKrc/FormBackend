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
import lt.ktu.formbackend.dao.impl.db.DaoFactory;
import lt.ktu.formbackend.dao.impl.db.UserDaoDbImpl;
import lt.ktu.formbackend.model.User;
import lt.ktu.formbackend.utility.JsonSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Lukas
 */

@Path("/user")
public class UserResource {

    private UserDaoDbImpl userDao = DaoFactory.getUserDao();

    @Context
    HttpServletRequest request;

    @POST
    @Path("/test")
    public Response testJson(String request) {
        System.out.println((String)this.request.getAttribute("username"));
        System.out.println(request);
        return Response.serverError().entity(request).build();
    }
    
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("username") String username) {
        if (username == null) {
            String errorJson = JsonSerializer.serializeError("Username can't be blank");
            return Response.serverError().entity(errorJson).build();
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
                String errorJson = JsonSerializer.serializeError(e.getMessage());
                return Response.serverError().entity(errorJson).build();
            }
            String userJson = userObject.toString();
            return Response.ok(userJson, MediaType.APPLICATION_JSON).build();
        } catch (DaoException e) {
            String errorJson = JsonSerializer.serializeError(e.getMessage());
            return Response.serverError().entity(errorJson).build();
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
                String errorJson = JsonSerializer.serializeError(e.getMessage());
                return Response.serverError().entity(errorJson).build();
            }
            return Response.ok("/user/" + user.getUsername()).build();
        } else {
            String errorJson = JsonSerializer.serializeError("Please provide the user credentials");
            return Response.serverError().entity(errorJson).build();
        }
    }

    @PUT
    @Path("/{username}")
    @Consumes("application/json")
    public Response updateUser(@PathParam("username") String username, User user) {
        if (!userDao.userExists(username)) {
            String errorJson = JsonSerializer.serializeError("User with username: " + username + " doesn't exist");
            return Response.serverError().entity(errorJson).build();
        }
        if (((String) request.getAttribute("username")).equals(username)) {
            userDao.updateUser(user, username);
            return Response.ok().build();
        }
        else {
            String errorJson = JsonSerializer.serializeError("You can only update your own account");
            return Response.serverError().entity(errorJson).build();
        }
    }
    
    @DELETE
    @Path("/{username}")
    public Response deleteUser(@PathParam("username") String username) {
        if (!userDao.userExists(username)) {
            String errorJson = JsonSerializer.serializeError("User with username: " + username + " doesn't exist");
            return Response.serverError().entity(errorJson).build();
        }
        if (((String) request.getAttribute("username")).equals(username)) {
            try {
                userDao.deleteUser(username);
                return Response.ok().build();
            } catch (DaoException e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
        } else {
            String errorJson = JsonSerializer.serializeError("You can only delete your own account");
            return Response.serverError().entity(errorJson).build();
        }
    }
}
