package wipb.ee.jspdemo.web.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import wipb.ee.jspdemo.web.model.Advertisement;
import wipb.ee.jspdemo.web.model.Category;

import java.util.List;
import java.util.Optional;

@Stateless
public class CategoryDao {
    @PersistenceContext(unitName = "PU")
    private EntityManager em;

    public Category saveOrUpdate(Category category) {
        if (category.getId() == null)
            em.persist(category);
        else {
            category = em.merge(category);
        }
        return category;
    }

    public void remove(Long id) {
        em.remove(em.getReference(Category.class, id));
    }

    public Optional<Category> findById(Long id) {
        Category b = em.find(Category.class,id);
        return Optional.ofNullable(b);
    }

    public List<Category> findAll() {
        TypedQuery<Category> q = em.createNamedQuery("Category.findAll", Category.class);
        return q.getResultList();
    }
}
