package lt.ktu.formbackend.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import lt.ktu.formbackend.model.Form;

/**
 *
 * @author Lukas
 */

@Path("/forms")
public class FormResource {

    @Context
    private UriInfo context;
    
     
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForm(@PathParam("id") long id) {
        return Response.serverError().entity("Not implemented yet.").build();
    }
     
    @Path("/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)    
    public Response putForm(@PathParam("id") long id, Form form) {
        return Response.serverError().entity("Not implemented yet.").build();
    }
}
