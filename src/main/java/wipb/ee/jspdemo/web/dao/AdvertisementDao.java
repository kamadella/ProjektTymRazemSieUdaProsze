package wipb.ee.jspdemo.web.dao;

import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import wipb.ee.jspdemo.web.model.Advertisement;

import java.util.List;
import java.util.Optional;

@Stateful
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
    public List<Advertisement> findAllAccepted(boolean status) {
        List<Advertisement> q = em.createNamedQuery("Advertisement.findAllAccepted", Advertisement.class)
                .setParameter("status", status)
                .getResultList();
        return q;
    }

    public void setEm(EntityManager em){
        this.em = em;
    }
}
