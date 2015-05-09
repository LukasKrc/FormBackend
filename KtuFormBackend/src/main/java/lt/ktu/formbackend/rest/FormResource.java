package lt.ktu.formbackend.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.impl.db.FormDaoDbImpl;
import lt.ktu.formbackend.model.Form;
import lt.ktu.formbackend.utility.DateTimeHandler;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Lukas
 */
@Path("/forms")
public class FormResource {

    @Context
    HttpServletRequest request;
    private UriInfo context;

    FormDaoDbImpl formDao = new FormDaoDbImpl();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFormById(@PathParam("id") long id) {
        try {
            return Response.ok(formDao.getFormId(id), MediaType.APPLICATION_JSON).build();
        } catch (DaoException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteForm(@PathParam("id") long id) {
        if (formDao.getFormAuthor(id).equals(request.getAttribute("username"))) {
            try {
                if (formDao.deleteForm(id)) {
                    return Response.ok().build();
                } else {
                    return Response.serverError().entity("Error when deleting form").build();
                }
            } catch (DaoException e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
        } else {
            return Response.serverError().entity("You can only delete your own forms").build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createForm(Form form) {
        if (!formDao.userHasForm(form.getName(), (String) request.getAttribute("username"))) {
            form.setAuthor((String) request.getAttribute("username"));
            form.setDate(DateTimeHandler.getDateTime());
            try {
                long formId = formDao.createForm(form);
                JSONObject formIdJson = new JSONObject();
                try {
                    formIdJson.put("id", formId);
                } catch (JSONException e) {
                    Response.serverError().entity(e.getMessage()).build();
                }
                String formIdString = formIdJson.toString();
                return Response.ok(formIdString).build();
            } catch (DaoException e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
        } else {
            return Response.serverError().entity("User has already created a form by that name.").build();
        }
    }

    @Path("/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putForm(@PathParam("id") long id, Form form) {
        return Response.serverError().entity("Not implemented yet.").build();
    }
}
