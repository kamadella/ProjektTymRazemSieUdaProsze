package wipb.ee.jspdemo.web.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import wipb.ee.jspdemo.web.model.Advertisement;
import wipb.ee.jspdemo.web.model.Book;

import java.util.List;
import java.util.Optional;

@Stateless
public class AdvertisementDao {
    @PersistenceContext(unitName = "PU")
    private EntityManager em;

    public Advertisement saveOrUpdate(Advertisement advertisement) {
        if (advertisement.getId() == null)
            em.persist(advertisement);
        else {
            advertisement = em.merge(advertisement);
        }
        return advertisement;
    }

    public void remove(Long id) {
        em.remove(em.getReference(Advertisement.class, id));
    }

    public Optional<Advertisement> findById(Long id) {
        Advertisement b = em.find(Advertisement.class,id);
        return Optional.ofNullable(b);
    }

    public List<Advertisement> findAll() {
        TypedQuery<Advertisement> q = em.createNamedQuery("Advertisement.findAll", Advertisement.class);
        return q.getResultList();
    }
}