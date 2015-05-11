package lt.ktu.formbackend.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.impl.db.AnswerDaoDbImpl;
import lt.ktu.formbackend.dao.impl.db.FormDaoDbImpl;
import lt.ktu.formbackend.model.Form;
import lt.ktu.formbackend.model.FormStats;
import lt.ktu.formbackend.utility.JsonSerializer;

/**
 *
 * @author Lukas
 */

@Path("/stats")
public class StatisticsResource {

    private AnswerDaoDbImpl answerDao = new AnswerDaoDbImpl();
    private FormDaoDbImpl formDao = new FormDaoDbImpl();

    @GET
    @Path("/{username}/{formName}")
    public Response getFormStats(@PathParam("username") String username, @PathParam("formName") String formName) {
        try {
            FormStats stats = new FormStats();
            Form form = new Form();
            form.setAuthor(username);
            form.setName(formName);
            long formId = formDao.getIdOfForm(form);
            int formVotes = answerDao.getVotesOfForm(formId);
            stats.setVotes(formVotes);
            stats.setAnswers(answerDao.getFormQuestionStats(formId));
            return Response.ok(stats, MediaType.APPLICATION_JSON).build();
        } catch (DaoException e) {
            String errorJson = JsonSerializer.serializeError(e.getMessage());
            return Response.serverError().entity(errorJson).build();
        }
    }

}
