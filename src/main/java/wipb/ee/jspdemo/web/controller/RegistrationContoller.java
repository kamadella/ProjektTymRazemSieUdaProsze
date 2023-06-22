package wipb.ee.jspdemo.web.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import wipb.ee.jspdemo.web.dao.UserDao;
import wipb.ee.jspdemo.web.model.Vser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "RegistrationContoller", urlPatterns ={"/registration", "/registration/save"})
public class RegistrationContoller extends HttpServlet {
    private final Logger log = Logger.getLogger(RegistrationContoller.class.getName());

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
        switch (path) {
            case "/registration":
                handleUserEditPost(request, response);
                break;
            case "/registration/save":
                response.sendRedirect(request.getContextPath()+ "/registration");
                break;
        }


    }

    private void handleGetEditGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);
        Vser u;
        if (id != null) {
            u = dao.findById(id).orElseThrow(() -> new IllegalStateException("No User with id "+id));
            request.setAttribute("username",u.getLogin());
            request.setAttribute("password",u.getPassword());
            request.setAttribute("email",u.getEmail());
        }

        // przekazuje sterowanie do strony jsp zwracającej formularz z książką
        request.getRequestDispatcher("/WEB-INF/registration.jsp").forward(request, response);
    }

    private void handleUserEditPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);

        Map<String,String> fieldToError = new HashMap<>();
        Vser b = parseUser(request.getParameterMap(),fieldToError);

        if (!fieldToError.isEmpty()) {
            // ustawia błędy jako atrybut do wyrenderowania na stronie z formularzem
            request.setAttribute("errors",fieldToError);
            // ustawia wartości przekazane z formularza metodą POST w atrybutach do wyrenderowania na stronie z formularzem
            request.setAttribute("username",request.getParameter("username"));
            request.setAttribute("password",request.getParameter("password"));
            request.setAttribute("email",request.getParameter("email"));

            // przekazuje sterowanie do widoku jsp w celu wyrenderowania formularza z informacją o błędach
            request.getRequestDispatcher("/WEB-INF/registration.jsp").forward(request, response);
            return;
        }

        b.setId(id);
        dao.saveOrUpdate(b);

        // po udanej konwersji/walidacji i zapisie obiektu użytkownik jest przekierowywany (przez HTTP Redirect) na stronę z listą książek
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private Vser parseUser(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        String login = paramToValue.get("username")[0];
        String password = paramToValue.get("password")[0];
        String email = paramToValue.get("email")[0];

        if (login == null || login.trim().isEmpty()) {
            fieldToError.put("login","Pole login nie może być puste");
        }
        if (email == null || email.trim().isEmpty()) {
            fieldToError.put("email","Pole email nie może być puste");
        }
        if (password == null || password.trim().isEmpty()) {
            fieldToError.put("password","Pole password nie może być puste");
        }

        return fieldToError.isEmpty() ?  new Vser(login,password,email, "user") : null;
    }

    private Long parseId(String s) {
        if (s == null || !s.startsWith("/"))
            return null;
        return Long.parseLong(s.substring(1));
    }

}
