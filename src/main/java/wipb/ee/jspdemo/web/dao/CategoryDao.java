package wipb.ee.jspdemo.web.dao;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import wipb.ee.jspdemo.web.model.Advertisement;
import wipb.ee.jspdemo.web.model.Category;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import java.util.List;
import java.util.Optional;

@Stateless
public class CategoryDao {

    private static final Logger logger = Logger.getLogger(CategoryDao.class.getName());
    private static final String Logfile = "src/main/logs/logs.log";

    @PostConstruct
    public void init(){
        try{
            FileHandler fileHandler = new FileHandler(Logfile);
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.info("CategoryDao started working.");

        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    @PersistenceContext(unitName = "PU")
    private EntityManager em;

    public Category saveOrUpdate(Category category) {
        if (category.getId() == null) {
            em.persist(category);
            logger.info("New category added:" + category.getName());
        }
        else {
            category = em.merge(category);
            logger.info("Category edited:" + category.getName());
        }
        return category;
    }

    public void remove(Long id) {
        em.remove(em.getReference(Category.class, id));
        logger.info("Category was deleted." + id.toString());
    }

    public Optional<Category> findById(Long id) {
        Category b = em.find(Category.class,id);
        logger.info("Category was found" + id.toString());
        return Optional.ofNullable(b);
    }

    public List<Category> findByName(String name) {
        List<Category> cat = em.createNamedQuery("Category.findByName", Category.class)
                .setParameter("name", name)
                .getResultList();
        //em.close();
        logger.info("Category was found" + name);
        return cat;
    }

    public List<Category> findAll() {
        TypedQuery<Category> q = em.createNamedQuery("Category.findAll", Category.class);
        logger.info("Categories were returned");
        return q.getResultList();
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
