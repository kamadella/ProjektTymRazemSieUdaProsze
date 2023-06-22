package wipb.ee.jspdemo.web.dao;

import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import wipb.ee.jspdemo.web.model.Advertisement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Stateful
public class AdvertisementDao {

    private static final Logger log = LogManager.getLogger(AdvertisementDao.class.getName());
    @PersistenceContext(unitName = "PU")
    private EntityManager em;

    public Advertisement saveOrUpdate(Advertisement advertisement) {
        if (advertisement.getId() == null) {
            em.persist(advertisement);
            log.info("Advertisement added");
        }
        else {
            advertisement = em.merge(advertisement);
            log.info("Advertisement updated");
        }
        return advertisement;
    }

    public void remove(Long id) {
        em.remove(em.getReference(Advertisement.class, id));
        log.info("Advertisement removed");
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
