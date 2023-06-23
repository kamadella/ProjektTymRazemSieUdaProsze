package wipb.ee.jspdemo.web.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import wipb.ee.jspdemo.web.dao.UserDao;
import wipb.ee.jspdemo.web.model.Vser;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoginController", urlPatterns = {"/login", "/login/admin"})
public class LoginController extends HttpServlet {

    @EJB
    private UserDao userDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        List<Vser> users = userDao.findAll();

        for (Vser user : users) {
            if (user.getLogin().equals(username) && user.getPassword().equals(password)) {
                HttpSession session = req.getSession();
                session.setAttribute("isLoggedIn", true);
                session.setAttribute("id", user.getId());
                if (user.getType().equals("admin")){
                    session.setAttribute("isAdmin", true);
                }
                else{
                    session.setAttribute("isAdmin", false);
                }
                session.setAttribute("username", username);
                resp.sendRedirect("/ee-jspdemo-web-1.0/advertisement/list"); //this page should be only acccessed after login
                return;
            }
        }
                for (Vser user : users) {
                    if (user.getLogin().equals(username) && user.getPassword().equals(password)) {
                        HttpSession session = req.getSession();
                        session.setAttribute("isLoggedIn", true);
                        if (user.getType().equals("admin")){
                            session.setAttribute("isAdmin", true);
                        }
                        else{
                            session.setAttribute("isAdmin", false);
                        }
                        session.setAttribute("username", username);
                        resp.sendRedirect("/ee-jspdemo-web-1.0/advertisement/list"); //this page should be only acccessed after login
                        return;
                    }

                }
                else{
                    session.setAttribute("isAdmin", false);
                }
                session.setAttribute("username", username);
                resp.sendRedirect("/ee-jspdemo-web-1.0/advertisement/list"); //this page should be only acccessed after login
                return;
            }
        }

        resp.sendRedirect("login?error=true");
    }
}
