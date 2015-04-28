package lt.ktu.formbackend.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

/**
 * REST Web Service
 *
 * @author Lukas
 */
@Path("/")
public class UserResource {

    @Context
    private UriInfo context;

    public UserResource() {
    }

    @GET
    @Produces("application/json")
    public String getJson() {
        return "derp";
    }
    
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
