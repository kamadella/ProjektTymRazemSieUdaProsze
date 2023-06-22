package wipb.ee.jspdemo.web.model;

import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQuery(name = "Vser.findAll", query = "select u from Vser u")
@NamedQuery(name= "Vser.findByName", query="select u from Vser u where u.login=:login")
public class Vser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String login;
    private String password;
    private String email;
    private String type;
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Advertisement> advertisementList = new LinkedList<Advertisement>();


    public Vser() {
    }
    public Vser(String login, String password, String email, String type) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Vser{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
