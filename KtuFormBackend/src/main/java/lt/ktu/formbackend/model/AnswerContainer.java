package lt.ktu.formbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class AnswerContainer {
    
    @JsonProperty("answers")
    private ArrayList<Answer> answers;
    
    //<editor-fold desc="Getters and setters">

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }
    
    //</editor-fold>
    
}
