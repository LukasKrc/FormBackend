package lt.ktu.formbackend.model;

/**
 *
 * @author Lukas
 */
public class AnswerRelation {

    private long formAnswer;
    private long answer;

    //<editor-fold desc="Getters and setters">
    public long getFormAnswer() {
        return formAnswer;
    }

    public void setFormAnswer(long formAnswer) {
        this.formAnswer = formAnswer;
    }

    public long getAnswer() {
        return answer;
    }

    public void setAnswer(long answer) {
        this.answer = answer;
    }
    //</editor-fold>

}
