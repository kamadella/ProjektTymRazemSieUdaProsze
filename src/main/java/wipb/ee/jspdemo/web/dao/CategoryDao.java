package wipb.ee.jspdemo.web.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import wipb.ee.jspdemo.web.model.Advertisement;
import wipb.ee.jspdemo.web.model.Category;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class CategoryDao {
    private static final Logger log = LogManager.getLogger(CategoryDao.class.getName());
    @PersistenceContext(unitName = "PU")
    private EntityManager em;

    public Category saveOrUpdate(Category category) {
        if (category.getId() == null) {
            em.persist(category);
            log.info("Cateory added ");
        }
        else {
            category = em.merge(category);
            log.info("Category updated");
        }
        return category;
    }

    public void remove(Long id) {
        em.remove(em.getReference(Category.class, id));
        log.info("Category removed");
    }

    public Optional<Category> findById(Long id) {
        Category b = em.find(Category.class,id);
        return Optional.ofNullable(b);
    }

    public List<Category> findByName(String name) {
        List<Category> cat = em.createNamedQuery("Category.findByName", Category.class)
                .setParameter("name", name)
                .getResultList();
        //em.close();
        return cat;
    }

    public List<Category> findAll() {
        TypedQuery<Category> q = em.createNamedQuery("Category.findAll", Category.class);
        return q.getResultList();
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
