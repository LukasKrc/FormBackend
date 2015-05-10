package lt.ktu.formbackend.model;

import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class AnswerStats {

    private ArrayList<String> choices;
    private ArrayList<Integer> votes;

    //<editor-fold desc="Getters and setters">
    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public ArrayList<Integer> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<Integer> votes) {
        this.votes = votes;
    }
    //</editor-fold>
    
}
