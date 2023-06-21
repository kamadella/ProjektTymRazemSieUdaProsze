/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wipb.ee.jspdemo.web.controller;



import wipb.ee.jspdemo.web.dao.BookDao;
import wipb.ee.jspdemo.web.model.Book;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "BookController", urlPatterns = {"/book/list", "/book/edit/*", "/book/remove/*"})
public class BookController extends HttpServlet {
    
    private final Logger log = Logger.getLogger(BookController.class.getName());
    
    @EJB // wstrzykuje referencje do komponentu EJB (BookDao)
    private BookDao dao;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {        
        String path = request.getServletPath();
        switch (path) {
            case "/book/list":
                handleBookList(request, response);
                break;
            case "/book/edit":
                handleGetEditGet(request, response);
                break;
            case "/book/remove":
                handleBookRemove(request, response);
                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/book/edit")) {
            handleBookEditPost(request, response);
        }
    }
    
    private void handleBookList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // wczytuje listę książek z bazy
        List<Book> books = dao.findAll();
        // ustawia listę książek jako atrybut pod nazwą "bookList" dostępny na stronie jsp
        request.setAttribute("bookList", books);
        // przekazuje sterowanie do strony jsp która renderuje listę książek
        request.getRequestDispatcher("/WEB-INF/views/book/book_list.jsp").forward(request, response);
    }
    
    private void handleGetEditGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);
        Book b;
        if (id != null) {
            b = dao.findById(id).orElseThrow(() -> new IllegalStateException("No book with id "+id));
            request.setAttribute("title",b.getTitle());
            request.setAttribute("author",b.getAuthor());
            request.setAttribute("price",formatPrice(b.getPrice()));
        }
        // przekazuje sterowanie do strony jsp zwracającej formularz z książką
        request.getRequestDispatcher("/WEB-INF/views/book/book_form.jsp").forward(request, response);
    }
    
    private void handleBookEditPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);

        Map<String,String> fieldToError = new HashMap<>();
        Book b = parseBook(request.getParameterMap(),fieldToError);

        if (!fieldToError.isEmpty()) {
            // ustawia błędy jako atrybut do wyrenderowania na stronie z formularzem
            request.setAttribute("errors",fieldToError);
            // ustawia wartości przekazane z formularza metodą POST w atrybutach do wyrenderowania na stronie z formularzem
            request.setAttribute("title",request.getParameter("title"));
            request.setAttribute("author",request.getParameter("author"));
            request.setAttribute("price",request.getParameter("price"));

            // przekazuje sterowanie do widoku jsp w celu wyrenderowania formularza z informacją o błędach
            request.getRequestDispatcher("/WEB-INF/views/book/book_form.jsp").forward(request, response);
            return;
        }

        b.setId(id);
        dao.saveOrUpdate(b);

        // po udanej konwersji/walidacji i zapisie obiektu użytkownik jest przekierowywany (przez HTTP Redirect) na stronę z listą książek
        response.sendRedirect(request.getContextPath() + "/book/list");
    }

    private void handleBookRemove(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);
        dao.remove(id);
        // użytkownik jest przekierowywany (przez HTTP Redirect) na stronę z listą książek
        response.sendRedirect(request.getContextPath() + "/book/list");
    }

    private Book parseBook(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        String title = paramToValue.get("title")[0];
        String author = paramToValue.get("author")[0];
        String price = paramToValue.get("price")[0];
        BigDecimal bdPrice = null;

        if (title == null || title.trim().isEmpty()) {
            fieldToError.put("title","Pole tytuł nie może być puste");
        }

        if (author == null || author.trim().isEmpty()) {
            fieldToError.put("author","Pole autor nie może być puste");
        }

        if (price == null || price.trim().isEmpty()) {
            fieldToError.put("price","Pole cena nie może być puste");
        } else {
            try {
                bdPrice = parsePrice(price);
            } catch (Throwable e) {
                fieldToError.put("price","Cena musi być poprawną liczbą");
            }
        }

        return fieldToError.isEmpty() ?  new Book(title,author,bdPrice) : null;
    }


    private BigDecimal parsePrice(String s) throws ParseException {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        Locale locale = new Locale("pl", "PL");
        DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        format.setParseBigDecimal(true);
        return ((BigDecimal)format.parse(s)).setScale(2, RoundingMode.FLOOR);
    }

    private String formatPrice(BigDecimal price)  {
        if (price == null) return "";
        Locale locale = new Locale("pl", "PL");
        DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        return format.format(price);
    }
    
    private Long parseId(String s) {
        if (s == null || !s.startsWith("/"))
            return null;
        return Long.parseLong(s.substring(1));
    }
}
