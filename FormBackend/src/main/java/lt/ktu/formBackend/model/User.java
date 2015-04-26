package lt.ktu.formBackend.model;

/**
 *
 * @author Lukas
 */
public class User {

    private Long id;
    private String username;
    private String password;
    private String company;
    private String name;
    private String surname;
    
    
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
    
}
