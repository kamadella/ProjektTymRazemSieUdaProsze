package wipb.ee.jspdemo.web.model;

import jakarta.persistence.*;

@Entity
@NamedQuery(name = "Advertisement.findAll", query = "select a from Advertisement a")
@NamedQuery(name = "Advertisement.findAllAccepted", query = "select a from Advertisement a where a.status=:status")
@NamedQuery(name = "Advertisement.findAllMyAdverts", query = "select a from Advertisement a where a.user.id=:id")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String title;
    private String description;
    @ManyToOne()
    private Category category;
    @ManyToOne()
    private Vser user;
    private boolean status;

    public Advertisement() {
    }

    public Advertisement(String title, String description, Category category, Vser user) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.user = user;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Vser getUser() {
        return user;
    }

    public void setUser(Vser user) {
        this.user = user;
    }
}
