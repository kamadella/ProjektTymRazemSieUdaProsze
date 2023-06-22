package wipb.ee.jspdemo.web;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import wipb.ee.jspdemo.web.model.Advertisement;
import wipb.ee.jspdemo.web.model.Category;
import wipb.ee.jspdemo.web.model.Vser;

@Singleton
@Startup
public class StartupListener {
    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void initialize(){
        Vser user = new Vser("admin", "123", "admin@exp.com", "admin");
        Vser user2 = new Vser("jawel", "123", "admin1@exp.com", "normal");
        Vser user3 = new Vser("kama", "123", "admin2@exp.com", "normal");
        em.persist(user);
        em.persist(user2);
        em.persist(user3);

        Category cat = new Category("Elektronika");
        Category cat2 = new Category("Przedmioty kuchenne");
        em.persist(cat);
        em.persist(cat2);

        Advertisement ad = new Advertisement("Telefon", "Nowy telefon nie używany:)", cat, user2);
        Advertisement ad2 = new Advertisement("Kuchenka niegazowa", "Pali się, pali się AAAAA", cat2, user3);
        ad2.setStatus(true);
        em.persist(ad);
        em.persist(ad2);

    }
}
