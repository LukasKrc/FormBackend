package lt.ktu.formbackend.rest;

import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.impl.db.DaoFactory;
import lt.ktu.formbackend.dao.impl.db.FormDaoDbImpl;
import lt.ktu.formbackend.model.Form;
import lt.ktu.formbackend.model.SearchQuery;
import lt.ktu.formbackend.utility.DateTimeHandler;
import lt.ktu.formbackend.utility.FormComparator;
import lt.ktu.formbackend.utility.JsonSerializer;
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

    private FormDaoDbImpl formDao = DaoFactory.getFormDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchForms(@QueryParam("q") String query, @QueryParam("tags") String tags,
            @QueryParam("sort") String sort, @QueryParam("limit") int limit,
            @QueryParam("skip") int skip, @QueryParam("order") String order,
            @QueryParam("author") String author, @QueryParam("allow-anon") boolean allowAnon,
            @QueryParam("finished") boolean finished) {

        SearchQuery searchQuery = new SearchQuery(allowAnon, author, finished, limit, order, query, skip, sort, tags);
        if (searchQuery.hasMandatoryFields() != null) {
            String errorJson = JsonSerializer.serializeError("Search query is missing field: " + searchQuery.hasMandatoryFields());
            return Response.serverError().entity(errorJson).build();
        }
//        try {
        ArrayList<Form> forms = formDao.searchForms(searchQuery);
        if (skip > forms.size() || limit == 0) {
            String errorJson = JsonSerializer.serializeError("Skip parameter is too high or limit is not provided");
            return Response.serverError().entity(errorJson).build();
        }
        Collections.sort(forms, new FormComparator(sort, order));
        forms = new ArrayList(forms.subList(skip, forms.size()));
        if (forms.size() > limit)
            forms = new ArrayList(forms.subList(0, limit));
            return Response.ok(forms).build();
//        } catch (DaoException e) {
//            return Response.serverError().entity(e.getMessage()).build();
//        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFormById(@PathParam("id") long id) {
        try {
            Form form = formDao.getFormId(id);
            String formJsonString = JsonSerializer.serializeForm(form);
            return Response.ok(formJsonString, MediaType.APPLICATION_JSON).build();
        } catch (DaoException e) {
            String errorJson = JsonSerializer.serializeError(e.getMessage());
            return Response.serverError().entity(errorJson).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteForm(@PathParam("id") long id) {
        try {
            if (formDao.getFormAuthor(id).equals(request.getAttribute("username"))) {
                formDao.deleteForm(id);
                return Response.ok().build();
            } else {
                return Response.serverError().entity("You can only delete your own forms").build();
            }
        } catch (DaoException e) {
            String errorJson = JsonSerializer.serializeError(e.getMessage());
            return Response.serverError().entity(errorJson).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createForm(Form form) {
        try {
            if (!formDao.userHasForm(form.getName(), (String) request.getAttribute("username"))) {
                form.setAuthor((String) request.getAttribute("username"));
                form.setDate(DateTimeHandler.getDateTime());
                long formId = formDao.createForm(form);
                JSONObject formIdJson = new JSONObject();
                try {
                    formIdJson.put("id", formId);
                } catch (JSONException e) {
                    Response.serverError().entity(e.getMessage()).build();
                }
                String formIdString = formIdJson.toString();
                return Response.ok(formIdString).build();
            } else {
                String errorJson = JsonSerializer.serializeError("User has already created a form by that name.");
                return Response.serverError().entity(errorJson).build();
            }
        } catch (DaoException e) {
            String errorJson = JsonSerializer.serializeError(e.getMessage());
            return Response.serverError().entity(errorJson).build();
        }
    }

    @Path("/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putForm(@PathParam("id") long id, Form form) {
        System.out.println("Resource");
        try {
        if (formDao.updateForm(id, form))
            return Response.ok().build();
        else {
            String errorJson = JsonSerializer.serializeError("Form update failed");
            return Response.serverError().entity(errorJson).build();
        }
        } catch (DaoException e) {
            String errorJson = JsonSerializer.serializeError(e.getMessage());
            return Response.serverError().entity(errorJson).build();
        }
    }
}
