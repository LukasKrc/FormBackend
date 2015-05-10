package lt.ktu.formbackend.model;

/**
 *
 * @author Lukas
 */
public class FormAnswer {
    private String author;
    private long form;
    private long id;
    
    //<editor-fold desc="Getters and setters">
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
