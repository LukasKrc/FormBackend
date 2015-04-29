package lt.ktu.formbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 *
 * @author Lukas
 */
public class Form {
    private long id;
    private String name;
    private String author;
    @JsonProperty("desc")
    private String description;
    private ArrayList<String> tags;
    private String date;
    @JsonProperty("allow-anon")
    private Boolean allowAnon;
    @JsonProperty("public")
    private Boolean publiclyAvailable;
    @JsonProperty("show-results")
    private Boolean showResults;
    private Boolean finished;
    private ArrayList<Question> questions;

    //<editor-fold desc="Getters, setters">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Boolean getAllowAnon() {
        return allowAnon;
    }

    public void setAllowAnon(Boolean allowAnon) {
        this.allowAnon = allowAnon;
    }

    public Boolean getPubliclyAvailable() {
        return publiclyAvailable;
    }

    public void setPubliclyAvailable(Boolean publiclyAvailable) {
        this.publiclyAvailable = publiclyAvailable;
    }

    public Boolean getShowResults() {
        return showResults;
    }

    public void setShowResults(Boolean showResults) {
        this.showResults = showResults;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
    //</editor-fold>
    
}
