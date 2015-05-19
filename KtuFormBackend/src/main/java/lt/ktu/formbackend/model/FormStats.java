package lt.ktu.formbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class FormStats {
    @JsonIgnore
    private String formName;
    @JsonProperty("numvotes")
    private int votes;
    @JsonProperty("answers")
    ArrayList<AnswerStats> answers;

    //<editor-fold desc="Getters and setters">
    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
    
    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public ArrayList<AnswerStats> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<AnswerStats> answers) {
        this.answers = answers;
    }
    //</editor-fold>
    
}
