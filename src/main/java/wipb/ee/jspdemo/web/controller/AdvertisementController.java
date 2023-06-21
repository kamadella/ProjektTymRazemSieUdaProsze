package wipb.ee.jspdemo.web.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import wipb.ee.jspdemo.web.dao.AdvertisementDao;
import wipb.ee.jspdemo.web.model.Advertisement;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "AdvertisementController", urlPatterns = {"/advertisement/list", "/advertisement/edit/*", "/advertisement/remove/*"})
public class AdvertisementController extends HttpServlet {
    private final Logger log = Logger.getLogger(AdvertisementController.class.getName());

    @EJB // wstrzykuje referencje do komponentu EJB (BookDao)
    private AdvertisementDao dao;

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
            case "/advertisement/list":
                handleAdvertisementList(request, response);
                break;
            case "/advertisement/edit":
                handleGetEditGet(request, response);
                break;
            case "/advertisement/remove":
                handleAdvertisementRemove(request, response);
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
        if (path.equals("/advertisement/edit")) {
            handleAdvertisementEditPost(request, response);
        }
    }

    private void handleAdvertisementList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // wczytuje listę książek z bazy
        List<Advertisement> advertisements = dao.findAll();
        // ustawia listę książek jako atrybut pod nazwą "bookList" dostępny na stronie jsp
        request.setAttribute("advertisementList", advertisements);
        // przekazuje sterowanie do strony jsp która renderuje listę książek
        request.getRequestDispatcher("/WEB-INF/views/advertisement/advertisement_list.jsp").forward(request, response);
    }

    private void handleGetEditGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);
        Advertisement a;
        if (id != null) {
            a = dao.findById(id).orElseThrow(() -> new IllegalStateException("No Advertisement with id "+id));
            request.setAttribute("title",a.getTitle());
            request.setAttribute("description",a.getDescription());
            request.setAttribute("idCategory",a.getIdCategory());
        }

        // przekazuje sterowanie do strony jsp zwracającej formularz z książką
        request.getRequestDispatcher("/WEB-INF/views/advertisement/advertisement_form.jsp").forward(request, response);
    }

    private void handleAdvertisementEditPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);

        Map<String,String> fieldToError = new HashMap<>();
        Advertisement a = parseAdvertisement(request.getParameterMap(),fieldToError);

        if (!fieldToError.isEmpty()) {
            // ustawia błędy jako atrybut do wyrenderowania na stronie z formularzem
            request.setAttribute("errors",fieldToError);
            // ustawia wartości przekazane z formularza metodą POST w atrybutach do wyrenderowania na stronie z formularzem
            request.setAttribute("title",request.getParameter("title"));
            request.setAttribute("description",request.getParameter("description"));
            request.setAttribute("idCategory",request.getParameter("idCategory"));

            // przekazuje sterowanie do widoku jsp w celu wyrenderowania formularza z informacją o błędach
            request.getRequestDispatcher("/WEB-INF/views/advertisement/advertisement_form.jsp").forward(request, response);
            return;
        }

        a.setId(id);
        dao.saveOrUpdate(a);

        // po udanej konwersji/walidacji i zapisie obiektu użytkownik jest przekierowywany (przez HTTP Redirect) na stronę z listą książek
        response.sendRedirect(request.getContextPath() + "/advertisement/list");
    }

    private void handleAdvertisementRemove(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);
        dao.remove(id);
        // użytkownik jest przekierowywany (przez HTTP Redirect) na stronę z listą książek
        response.sendRedirect(request.getContextPath() + "/advertisement/list");
    }

    private Advertisement parseAdvertisement(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        String title = paramToValue.get("title")[0];
        String description = paramToValue.get("description")[0];
        Long idCategory = Long.valueOf(paramToValue.get("idCategory")[0]);

        if (title == null || title.trim().isEmpty()) {
            fieldToError.put("title","Pole tytuł nie może być puste");
        }

        if (description == null || description.trim().isEmpty()) {
            fieldToError.put("description","Pole description nie może być puste");
        }

        if (idCategory == null) {
            fieldToError.put("idCategory","Pole idCategory nie może być puste");
        }

        return fieldToError.isEmpty() ?  new Advertisement(title,description,idCategory) : null;
    }

    private Long parseId(String s) {
        if (s == null || !s.startsWith("/"))
            return null;
        return Long.parseLong(s.substring(1));
    }
}
