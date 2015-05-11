package lt.ktu.formbackend.utility;

import java.util.ArrayList;
import lt.ktu.formbackend.dao.DaoException;
import lt.ktu.formbackend.dao.DaoException.Type;
import lt.ktu.formbackend.model.Form;
import lt.ktu.formbackend.model.Question;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Lukas
 */
public class JsonSerializer {
    
    public static String serializeError(String error) {
        return "{ \"error\" : \"" + error + "\" }";
    }

    public static String serializeForm(Form form) {
        JSONObject formJson = new JSONObject();
        try {
            formJson.put("id", form.getId());
            formJson.put("name", form.getName());
            formJson.put("author", form.getAuthor());
            formJson.put("tags", form.getTags());
            formJson.put("date", form.getDate());
            formJson.put("finished", form.getFinished());
            formJson.put("desc", form.getDescription());
            formJson.put("allow-anon", form.getAllowAnon());
            formJson.put("show-results", form.getShowResults());
            JSONArray questionJsonArray = new JSONArray();
            ArrayList<Question> questions = form.getQuestions();
            for (int i = 0; i < questions.size(); i++) {
                JSONObject questionJson = new JSONObject();
                Question question = questions.get(i);
                switch (question.getType()) {
                    case "integer":
                        questionJson.put("name", question.getName());
                        questionJson.put("type", "integer");
                        questionJson.put("min-val", question.getMinValue());
                        questionJson.put("max-val", question.getMaxValue());
                        questionJson.put("allow-empty", question.getAllowEmpty());
                        break;

                    case "string":
                        questionJson.put("name", question.getName());
                        questionJson.put("type", "string");
                        questionJson.put("min-val", question.getMinValue());
                        questionJson.put("max-val", question.getMaxValue());
                        questionJson.put("allow-ws", question.getAllowWhitespace());
                        questionJson.put("allow-nl", question.getAllowNewLines());
                        questionJson.put("allow-empty", question.getAllowEmpty());
                        break;

                    case "email":
                        questionJson.put("name", question.getName());
                        questionJson.put("type", "email");
                        JSONArray providerJsonArray = new JSONArray();
                        providerJsonArray.put(question.getAllowedProviders());
                        questionJson.put("allow-empty", question.getAllowEmpty());
                        questionJson.put("allowed-providers", providerJsonArray);
                        break;

                    case "one-choice":
                        questionJson.put("name", question.getName());
                        questionJson.put("type", "one-choice");
                        questionJson.put("allow-custom", question.getAllowCustom());
                        questionJson.put("allow-empty", question.getAllowEmpty());
                        questionJson.put("choices", question.getChoices());
                        break;

                    case "multi-choice":
                        questionJson.put("name", question.getName());
                        questionJson.put("type", "multi-choice");
                        questionJson.put("allow-custom", question.getAllowCustom());
                        questionJson.put("allow-empty", question.getAllowEmpty());
                        questionJson.put("choices", question.getChoices());
                        break;
                }
                questionJsonArray.put(questionJson);
            }
            formJson.put("questions", questionJsonArray);
        } catch (JSONException e) {
            throw new DaoException(Type.ERROR, e.getMessage());
        }
        return formJson.toString();
    }

}
