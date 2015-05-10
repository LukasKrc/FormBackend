package lt.ktu.formbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class FormStats {
    @JsonProperty("numvotes")
    private int votes;
    @JsonProperty("answers")
    ArrayList<AnswerStats> answers;

    //<editor-fold desc="Getters and setters">
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
