package wipb.ee.jspdemo.web.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wipb.ee.jspdemo.web.dao.AdvertisementDao;
import wipb.ee.jspdemo.web.dao.CategoryDao;
import wipb.ee.jspdemo.web.dao.UserDao;
import wipb.ee.jspdemo.web.model.Advertisement;
import wipb.ee.jspdemo.web.model.Category;
import wipb.ee.jspdemo.web.model.Vser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@WebServlet(name = "AdvertisementController", urlPatterns = {"/advertisement/list", "/advertisement/edit/*", "/advertisement/remove/*", "/advertisement/accept/*", "/advertisement/myadverts/*"})

public class AdvertisementController extends HttpServlet {
    private final Logger log = LogManager.getLogger(AdvertisementController.class.getName());

    @EJB // wstrzykuje referencje do komponentu EJB (BookDao)
    private AdvertisementDao dao;
    @EJB // wstrzykuje referencje do komponentu EJB (BookDao)
    private CategoryDao daoCategory;
    @EJB
    private UserDao daoUser;

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
            case "/advertisement/accept":
                handleAdvertisementAccept(request, response);
                break;
            case "/advertisement/myadverts":
                handleAdvertisementMyAdverts(request, response);
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
        switch (path) {
            case "/advertisement/edit":
                handleAdvertisementEditPost(request, response);
                break;

            case "/advertisement/accept":
                handleAdvertisementAcceptPOST(request, response);
                break;

        }
    }

    private void handleAdvertisementList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Object isAdmin = request.getSession().getAttribute("isAdmin");
        boolean isVdmin = (boolean) isAdmin;
        List<Advertisement> advertisements;
        if (isVdmin) {
            advertisements = dao.findAll();
        }else{
            advertisements = dao.findAllAccepted(true);
        }
        request.setAttribute("id", request.getSession().getAttribute("id"));
        request.setAttribute("isAdmin", request.getSession().getAttribute("isAdmin"));
        request.setAttribute("advertisementList", advertisements);
        request.getRequestDispatcher("/WEB-INF/views/advertisement/advertisement_list.jsp").forward(request, response);
    }

    private void handleAdvertisementMyAdverts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Advertisement> advertisements = dao.findMyAdverts(((Long)request.getSession().getAttribute("id")).longValue());
        request.setAttribute("advertisementList", advertisements);
        request.getRequestDispatcher("/WEB-INF/views/advertisement/my_advertisement.jsp").forward(request, response);
    }

    private void handleGetEditGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);
        Advertisement a;

        List<Category> categoryList = daoCategory.findAll();
        request.setAttribute("categoryList", categoryList);

        if (id != null) {
            a = dao.findById(id).orElseThrow(() -> new IllegalStateException("No Advertisement with id "+id));
            request.setAttribute("title",a.getTitle());
            request.setAttribute("description",a.getDescription());

        }

        // przekazuje sterowanie do strony jsp zwracającej formularz z książką
        request.getRequestDispatcher("/WEB-INF/views/advertisement/advertisement_form.jsp").forward(request, response);
    }

    private void handleAdvertisementEditPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);

        Map<String,String> fieldToError = new HashMap<>();
        Advertisement a = parseAdvertisement(request.getParameterMap(),fieldToError, request);

        if (!fieldToError.isEmpty()) {
            // ustawia błędy jako atrybut do wyrenderowania na stronie z formularzem
            request.setAttribute("errors",fieldToError);
            // ustawia wartości przekazane z formularza metodą POST w atrybutach do wyrenderowania na stronie z formularzem
            request.setAttribute("title",request.getParameter("title"));
            request.setAttribute("description",request.getParameter("description"));
            request.setAttribute("categoryList",request.getParameter("selectedOption"));

            // przekazuje sterowanie do widoku jsp w celu wyrenderowania formularza z informacją o błędach
            request.getRequestDispatcher("/WEB-INF/views/advertisement/advertisement_form.jsp").forward(request, response);
            return;
        }

        a.setId(id);
        dao.saveOrUpdate(a);

        // po udanej konwersji/walidacji i zapisie obiektu użytkownik jest przekierowywany (przez HTTP Redirect) na stronę z listą książek
        response.sendRedirect(request.getContextPath() + "/advertisement/myadverts");
    }

    private void handleAdvertisementAccept(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);
        Advertisement a = dao.findById(id).get();
        a.setStatus(true);
        dao.saveOrUpdate(a);
        response.sendRedirect(request.getContextPath() + "/advertisement/list");
    }

    private void handleAdvertisementAcceptPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);
        Advertisement a = dao.findById(id).get();
        a.setStatus(true);
        dao.saveOrUpdate(a);
        response.sendRedirect(request.getContextPath() + "/advertisement/list");
    }

    private void handleAdvertisementRemove(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);
        dao.remove(id);
        // użytkownik jest przekierowywany (przez HTTP Redirect) na stronę z listą książek
        response.sendRedirect(request.getContextPath() + "/advertisement/list");
    }

    private Advertisement parseAdvertisement(Map<String,String[]> paramToValue, Map<String,String> fieldToError, HttpServletRequest request) {
        String title = paramToValue.get("title")[0];
        String description = paramToValue.get("description")[0];
        String categoryName = paramToValue.get("selectedOption")[0];

        if (title == null || title.trim().isEmpty()) {
            fieldToError.put("title","Pole tytuł nie może być puste");
        }

        if (description == null || description.trim().isEmpty()) {
            fieldToError.put("description","Pole description nie może być puste");
        }

        if (categoryName == null) {
            fieldToError.put("categoryList","Pole category nie może być puste");
        }
        List<Category> cats = daoCategory.findByName(categoryName);

        Optional<Vser> u = daoUser.findById(((Long)request.getSession().getAttribute("id")).longValue());

        return fieldToError.isEmpty() ?  new Advertisement(title,description,cats.get(0), u.get()) : null;
    }

    private Long parseId(String s) {
        if (s == null || !s.startsWith("/"))
            return null;
        return Long.parseLong(s.substring(1));
    }
}
