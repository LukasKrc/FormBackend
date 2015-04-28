package lt.ktu.formbackend.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.model.User;
import lt.ktu.formbackend.dao.impl.db.UserDaoDbImpl;

/**
 * REST Web Service
 *
 * @author Lukas
 */
@Path("/")
public class UserResource {

    private UserDaoDbImpl userDao;

    @Context
    private UriInfo context;

    public UserResource() {
        userDao = new UserDaoDbImpl();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@QueryParam("username") String username) {
        if (username == null) {
            return Response.serverError().entity("Username can't be blank").build();
        }
        try {
            return Response.ok(userDao.getUserUsername(username), MediaType.APPLICATION_JSON).build();
        } catch (DaoException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
