package wipb.ee.jspdemo.web.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import wipb.ee.jspdemo.web.dao.CategoryDao;
import wipb.ee.jspdemo.web.model.Category;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "CategoryController", urlPatterns = {"/category/list", "/category/edit/*", "/category/remove/*"})
public class CategoryController extends HttpServlet {
    private final Logger log = Logger.getLogger(CategoryController.class.getName());

    @EJB // wstrzykuje referencje do komponentu EJB (BookDao)
    private CategoryDao dao;

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
        if ((boolean) request.getSession().getAttribute("isAdmin")) {
            switch (path) {
                case "/category/list":
                    handleCategoryList(request, response);
                    break;
                case "/category/edit":
                    handleGetEditGet(request, response);
                    break;
                case "/category/remove":
                    handleCategoryRemove(request, response);
                    break;
            }
        }else{
            response.sendRedirect("/ee-jspdemo-web-1.0/login");
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
        if (path.equals("/category/edit")) {
            handleCategoryEditPost(request, response);
        }
    }

    private void handleCategoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // wczytuje listę książek z bazy
        List<Category> categories = dao.findAll();
        // ustawia listę książek jako atrybut pod nazwą "bookList" dostępny na stronie jsp
        request.setAttribute("categoryList", categories);
        // przekazuje sterowanie do strony jsp która renderuje listę książek
        request.getRequestDispatcher("/WEB-INF/views/kategorie/category_list.jsp").forward(request, response);
    }

    private void handleGetEditGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);
        Category c;
        if (id != null) {
            c = dao.findById(id).orElseThrow(() -> new IllegalStateException("No Category with id "+id));
            request.setAttribute("name",c.getName());
        }

        // przekazuje sterowanie do strony jsp zwracającej formularz z książką
        request.getRequestDispatcher("/WEB-INF/views/kategorie/category_form.jsp").forward(request, response);
    }

    private void handleCategoryEditPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);

        Map<String,String> fieldToError = new HashMap<>();
        Category c = parseCategory(request.getParameterMap(),fieldToError);

        if (!fieldToError.isEmpty()) {
            // ustawia błędy jako atrybut do wyrenderowania na stronie z formularzem
            request.setAttribute("errors",fieldToError);
            // ustawia wartości przekazane z formularza metodą POST w atrybutach do wyrenderowania na stronie z formularzem
            request.setAttribute("name",request.getParameter("name"));

            // przekazuje sterowanie do widoku jsp w celu wyrenderowania formularza z informacją o błędach
            request.getRequestDispatcher("/WEB-INF/views/kategorie/category_form.jsp").forward(request, response);
            return;
        }

        c.setId(id);
        dao.saveOrUpdate(c);

        // po udanej konwersji/walidacji i zapisie obiektu użytkownik jest przekierowywany (przez HTTP Redirect) na stronę z listą książek
        response.sendRedirect(request.getContextPath() + "/category/list");
    }


    private void handleCategoryRemove(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String s = request.getPathInfo();
        Long id = parseId(s);
        dao.remove(id);
        // użytkownik jest przekierowywany (przez HTTP Redirect) na stronę z listą książek
        response.sendRedirect(request.getContextPath() + "/category/list");
    }

    private Category parseCategory(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        String name = paramToValue.get("name")[0];

        if (name == null || name.trim().isEmpty()) {
            fieldToError.put("name","Pole nazwa nie może być puste");
        }

        return fieldToError.isEmpty() ?  new Category(name) : null;
    }


    private Long parseId(String s) {
        if (s == null || !s.startsWith("/"))
            return null;
        return Long.parseLong(s.substring(1));
    }
}
