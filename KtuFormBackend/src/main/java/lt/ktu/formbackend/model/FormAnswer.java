package lt.ktu.formbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class FormAnswer {
    private String author;
    @JsonIgnore
    private long form;
    @JsonIgnore
    private long id;
    private ArrayList<Answer> answers;
    
    @JsonIgnore
    private long authorId;
    
    //<editor-fold desc="Getters and setters">

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getForm() {
        return form;
    }

    public void setForm(long form) {
        this.form = form;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    //</editor-fold>
}
