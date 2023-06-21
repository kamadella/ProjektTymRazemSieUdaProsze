package wipb.ee.jspdemo.web.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String login;
    private String password;
    private String email;

    private List<Advertisement> listUserAdvertises = new LinkedList<>();


    public User() {
    }
    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
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
    public List<Advertisement> getAccountOperation() {
        return listUserAdvertises;
    }

    public void addAdvertise(Advertisement advert){
        listUserAdvertises.add(advert);
    }
}
