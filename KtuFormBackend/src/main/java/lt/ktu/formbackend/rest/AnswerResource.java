package lt.ktu.formbackend.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Lukas
 */
@Path("/answers")
public class AnswerResource {

    @Context
    private UriInfo context;

    @POST
    @Path("/{username}/{formName}")
    public Response fillForm(@PathParam("username") String username, @PathParam("formName") String formName) {
        return Response.serverError().entity("Not implemented yet.").build();
    }
}
