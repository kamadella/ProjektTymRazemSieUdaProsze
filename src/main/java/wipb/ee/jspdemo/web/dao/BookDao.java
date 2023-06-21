/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wipb.ee.jspdemo.web.dao;


import wipb.ee.jspdemo.web.model.Book;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Dao w postaci komponentu EJB typu @Stateless
 *
 */
@Stateless
public class BookDao {
    /**
     * Określa nazwę konfiguracji (PU z pliku persistence.xml) dla EntityManager, który ma być wstrzyknięty w momencie utworzenia instancji komponentu EJB BookDao
     */
    @PersistenceContext(unitName = "PU")
    private EntityManager em;
    
    public Book saveOrUpdate(Book book) {
        if (book.getId() == null)
            em.persist(book);
        else {
            book = em.merge(book);
        }
        return book;
    }
    
    public void remove(Long id) {
        em.remove(em.getReference(Book.class, id));
    }

    public Optional<Book> findById(Long id) {
        Book b = em.find(Book.class,id);
        return Optional.ofNullable(b);
    }

    public List<Book> findAll() {
        TypedQuery<Book> q = em.createNamedQuery("Book.findAll", Book.class);
        return q.getResultList();
    }

}
