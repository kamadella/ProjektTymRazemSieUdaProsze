package wipb.ee.jspdemo.web.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import wipb.ee.jspdemo.web.model.Book;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "RegistrationContoller", urlPatterns ={"/registration"})
public class RegistrationContoller extends HttpServlet {
    private final Logger log = Logger.getLogger(BookController.class.getName());

    @EJB // wstrzykuje referencje do komponentu EJB (BookDao)
    private UserDao dao;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/registration":
                handleGetEditGet(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/registration")) {
            handleUserEditPost(request, response);
        }

    }

    private void handleGetEditGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);
        User u;
        if (id != null) {
            u = dao.findById(id).orElseThrow(() -> new IllegalStateException("No User with id "+id));
            request.setAttribute("name",u.getName());
        }

        // przekazuje sterowanie do strony jsp zwracającej formularz z książką
        request.getRequestDispatcher("/WEB-INF/views/kategorie/category_form.jsp").forward(request, response);
    }

    private void handleUserEditPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);

        Map<String,String> fieldToError = new HashMap<>();
        Book b = parseUser(request.getParameterMap(),fieldToError);

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
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private User parseUser(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        String mail = paramToValue.get("mail")[0];
        String username = paramToValue.get("username")[0];
        String password = paramToValue.get("password")[0];

        if (username == null || username.trim().isEmpty()) {
            fieldToError.put("name","Pole username nie może być puste");
        }
        if (mail == null || mail.trim().isEmpty()) {
            fieldToError.put("mail","Pole mail nie może być puste");
        }
        if (password == null || password.trim().isEmpty()) {
            fieldToError.put("password","Pole password nie może być puste");
        }

        return fieldToError.isEmpty() ?  new User(username,password,mail) : null;
    }

    private Long parseId(String s) {
        if (s == null || !s.startsWith("/"))
            return null;
        return Long.parseLong(s.substring(1));
    }

}
