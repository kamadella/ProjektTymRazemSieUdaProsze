package wipb.ee.jspdemo.web.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
@Entity
@NamedQuery(name = "Advertisement.findAll", query = "select a from Advertisement a")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String title;
    private String descriprion;
    private Long id_category;
    private boolean status;

    public Advertisement() {
    }

    public Advertisement(String title, String descriprion, Long id_category) {
        this.title = title;
        this.descriprion = descriprion;
        this.id_category = id_category;
        this.status = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriprion() {
        return descriprion;
    }

    public void setDescriprion(String descriprion) {
        this.descriprion = descriprion;
    }

    public Long getId_category() {
        return id_category;
    }

    public void setId_category(Long id_category) {
        this.id_category = id_category;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }



}
