package wipb.ee.jspdemo.web.dao;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import wipb.ee.jspdemo.web.model.Advertisement;
import wipb.ee.jspdemo.web.model.Book;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Stateless
public class AdvertisementDao {
    private static final Logger logger = Logger.getLogger(AdvertisementDao.class.getName());
    private static final String Logfile = "src/main/logs/logs.log";
    @PostConstruct
    public void init(){
        try{
            FileHandler fileHandler = new FileHandler(Logfile);
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.info("AdvertisementDao started working.");

        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    @PersistenceContext(unitName = "PU")
    private EntityManager em;

    public Advertisement saveOrUpdate(Advertisement advertisement) {
        if (advertisement.getId() == null) {
            em.persist(advertisement);
            logger.info("Advertisement added:"+advertisement.getTitle());
        }
        else {
            advertisement = em.merge(advertisement);
            logger.info("Advertisement was updated:" + advertisement.getTitle());
        }
        return advertisement;
    }

    public void remove(Long id) {
        em.remove(em.getReference(Advertisement.class, id));
        logger.info("Advertisement was removed:" + id.toString());
    }

    public Optional<Advertisement> findById(Long id) {
        Advertisement b = em.find(Advertisement.class,id);
        logger.info("Advertisement was found:" + id.toString());
        return Optional.ofNullable(b);
    }

    public List<Advertisement> findAll() {
        TypedQuery<Advertisement> q = em.createNamedQuery("Advertisement.findAll", Advertisement.class);
        logger.info("Advertisements returned");
        return q.getResultList();
    }

    public void setEm(EntityManager em){
        this.em = em;
    }
}
