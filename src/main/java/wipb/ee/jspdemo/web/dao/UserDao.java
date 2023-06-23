package wipb.ee.jspdemo.web.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import wipb.ee.jspdemo.web.model.Vser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Stateless
public class UserDao {
    private static final Logger log = LogManager.getLogger(UserDao.class.getName());
    @PersistenceContext(unitName = "PU")
    private EntityManager em;

    public Vser saveOrUpdate(Vser user) {
        if (user.getId() == null) {
            em.persist(user);
            log.info("User added");
        }
        else {
            user = em.merge(user);
        }
        return user;
    }

    public void remove(Long id) {
        em.remove(em.getReference(Vser.class, id));
        log.info("User removed");
    }

    public Optional<Vser> findById(Long id) {
        Vser b = em.find(Vser.class,id);
        return Optional.ofNullable(b);
    }

    public List<Vser> findAll() {
        TypedQuery<Vser> q = em.createNamedQuery("Vser.findAll", Vser.class);
        return q.getResultList();
    }

    public void  setEm(EntityManager em){
        this.em = em;
    }
}
