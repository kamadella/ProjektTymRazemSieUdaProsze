package wipb.ee.jspdemo.web.model;

import jakarta.persistence.*;

@Entity
@NamedQuery(name = "Advertisement.findAll", query = "select a from Advertisement a")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String title;
    private String description;
    private Long idCategory;
    private boolean status;

    public Advertisement() {
    }

    public Advertisement(String title, String description, Long idCategory) {
        this.title = title;
        this.description = description;
        this.idCategory = idCategory;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long id_category) {
        this.idCategory = id_category;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }



}
