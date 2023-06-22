package wipb.ee.jspdemo.web;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import wipb.ee.jspdemo.web.model.Vser;

@Singleton
@Startup
public class StartupListener {
    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void initialize(){
        Vser user = new Vser("admin", "123", "admin@exp.com");
        System.out.printf(String.valueOf(user));
        em.persist(user);

    }
}
