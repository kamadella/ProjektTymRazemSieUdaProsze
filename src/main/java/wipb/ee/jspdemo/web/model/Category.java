package wipb.ee.jspdemo.web.model;

import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQuery(name = "Category.findAll", query = "select c from Category c")
@NamedQuery(name= "Category.findByName", query="select cat from Category cat where cat.name=:name")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Advertisement> advertisementList = new LinkedList<Advertisement>();

    public Category() {}
    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Category{" +
                "ID=" + getId() +
                ", name='" + name + '\'' +
                '}';
    }
}
