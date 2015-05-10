package lt.ktu.formbackend.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.impl.db.AnswerDaoDbImpl;
import lt.ktu.formbackend.dao.impl.db.FormDaoDbImpl;
import lt.ktu.formbackend.model.Answer;
import lt.ktu.formbackend.model.AnswerContainer;
import lt.ktu.formbackend.model.Form;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Lukas
 */
@Path("/answers")
public class AnswerResource {
    
    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest request;

    private AnswerDaoDbImpl answerDao = new AnswerDaoDbImpl();
    private FormDaoDbImpl formDao = new FormDaoDbImpl();

    @POST
    @Path("/{username}/{formName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fillForm(@PathParam("username") String username, @PathParam("formName") String formName, AnswerContainer answers) {
        if (username == null || formName == null)
            return Response.serverError().entity("Invalid path for POST.").build();
        Form form = new Form();
        form.setAuthor(username);
        form.setName(formName);
//        try {
            long formId = formDao.getIdOfForm(form);
            long answerId = answerDao.createFormAnswer((String) request.getAttribute("username"), formId, answers.getAnswers());
            JSONObject answerIdJson = new JSONObject();
            try {
                answerIdJson.put("id", answerId);
            } catch (JSONException e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
            String answerIdString = answerIdJson.toString();
            return Response.ok(answerIdString).build();
//        } catch (DaoException e) {
//            return Response.serverError().entity(e.getMessage()).build();
//        }
    }
}
