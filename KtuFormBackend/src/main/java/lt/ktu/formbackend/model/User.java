package lt.ktu.formbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.enterprise.inject.Model;

/**
 *
 * @author Lukas
 */

@Model
public class User {

    private Long id;
    @JsonProperty("uname")
    private String username;    
    @JsonProperty("pass")
    private String password;
    private String company;
    private String name;
    private String surname;
    private Boolean isCompany;
    
    public String hasMandatoryFields() {
        if (username == null)
            return "uname";
        if (password == null)
            return "pass";
        if (name == null)
            return "name";
        if (surname == null)
            return "surname";
        if (isCompany == null)
            return "isCompany";
        return null;
    }
    
    //<editor-fold desc="Getters, setters">
    public Boolean getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Boolean isCompany) {
        this.isCompany = isCompany;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }

    public String getCompany() {
        return company;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSurname() {
        return surname;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    //</editor-fold>
    
}
