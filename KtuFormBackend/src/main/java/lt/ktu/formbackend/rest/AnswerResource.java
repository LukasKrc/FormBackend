package lt.ktu.formbackend.rest;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.impl.db.AnswerDaoDbImpl;
import lt.ktu.formbackend.dao.impl.db.DaoFactory;
import lt.ktu.formbackend.dao.impl.db.FormDaoDbImpl;
import lt.ktu.formbackend.dao.impl.db.UserDaoDbImpl;
import lt.ktu.formbackend.model.AnswerContainer;
import lt.ktu.formbackend.model.Form;
import lt.ktu.formbackend.model.FormAnswer;
import lt.ktu.formbackend.model.FormAnswerContainer;
import lt.ktu.formbackend.utility.JsonSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Lukas
 */
@Path("/answers")
public class AnswerResource {

    @Context
    private HttpServletRequest request;

    private AnswerDaoDbImpl answerDao = DaoFactory.getAnswerDao();
    private FormDaoDbImpl formDao = DaoFactory.getFormDao();
    private UserDaoDbImpl userDao = DaoFactory.getUserDao();

    @GET
    @Path("{username}/{formName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnswers(@PathParam("username") String username, @PathParam("formName") String formName) {
        try {
            Form form = new Form();
            form.setName(formName);
            form.setAuthor(username);
            long formId = formDao.getIdOfForm(form);
            ArrayList<Long> userIds = answerDao.getUsersFilledForm(formId);
            ArrayList<FormAnswer> formAnswers = new ArrayList();
            for (int i = 0; i < userIds.size(); i ++) {
                FormAnswer formAnswer = answerDao.getUsersAnswerToForm(userIds.get(i), formId);
                formAnswers.add(formAnswer);
            }
            FormAnswerContainer answers = new FormAnswerContainer();
            answers.setAnswers(formAnswers);
            return Response.ok(answers).build();
        } catch (DaoException e) {
            String errorJson = JsonSerializer.serializeError(e.getMessage());
            return Response.serverError().entity(errorJson).build();
        }
    }
    
    @POST
    @Path("/{username}/{formName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fillForm(@PathParam("username") String username, @PathParam("formName") String formName, FormAnswer answers) {
        if (username == null || formName == null) {
            String errorJson = JsonSerializer.serializeError("Invalid URL for POST");
            return Response.serverError().entity(errorJson).build();
        }
        if (!userDao.userExists(username)){
            String errorJson = JsonSerializer.serializeError("User with username: " + username +" doesn't exist");
            return Response.serverError().entity(errorJson).build();
        }
        if (!formDao.userHasForm(formName, username)){
            String errorJson = JsonSerializer.serializeError("User with username: " + username + " doesn't have a form with name: " + formName);
            return Response.serverError().entity(errorJson).build();
        }
        Form form = new Form();
        form.setAuthor(username);
        form.setName(formName);
        try {
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
        } catch (DaoException e) {
            String errorJson = JsonSerializer.serializeError(e.getMessage());
            return Response.serverError().entity(errorJson).build();
        }
    }
}
