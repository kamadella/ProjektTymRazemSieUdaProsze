package wipb.ee.jspdemo.web.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import wipb.ee.jspdemo.web.model.Vser;

import java.util.List;
import java.util.Optional;

@Stateless
public class UserDao {
    @PersistenceContext(unitName = "PU")
    private EntityManager em;

    public Vser saveOrUpdate(Vser user) {
        if (user.getId() == null)
            em.persist(user);
        else {
            user = em.merge(user);
        }
        return user;
    }

    public void remove(Long id) {
        em.remove(em.getReference(Vser.class, id));
    }

    public Optional<Vser> findById(Long id) {
        Vser b = em.find(Vser.class,id);
        return Optional.ofNullable(b);
    }

    public List<Vser> findAll() {
        TypedQuery<Vser> q = em.createNamedQuery("Vser.findAll", Vser.class);
        return q.getResultList();
    }
}
