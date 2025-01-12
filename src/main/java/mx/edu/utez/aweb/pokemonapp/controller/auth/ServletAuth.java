package mx.edu.utez.aweb.pokemonapp.controller.auth;

import mx.edu.utez.aweb.pokemonapp.model.auth.BeanUser;
import mx.edu.utez.aweb.pokemonapp.service.auth.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(
        name = "ServletAuth",
        urlPatterns = {
                "/login",
                "/logout",
                "/signin",
                "/recover-password",
                "/send-email"
        })
public class ServletAuth extends HttpServlet {
    String action;
    String urlRedirect = "/get-pokemons";
    HttpSession session;
    AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        action = request.getServletPath();
        switch (action) {
            case "/signin":
                urlRedirect = "/index.jsp";
                break;
            case "/recover-password":
                urlRedirect = "/views/auth/recoverPassword.jsp";
                break;
            default:
                urlRedirect = "/index.jsp";
        }
        request.getRequestDispatcher(urlRedirect).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        action = req.getServletPath();
        switch (action) {
            case "/login":
                String username = req.getParameter("username");
                String password = req.getParameter("password");
                BeanUser user = authService.login(username, password);
                if (user != null) {
                    session = req.getSession();
                    session.setAttribute("user", user);
                    //user.getRole() == "ADMIN" -> /dashboard
                    //user.getRole() == "STUDENT" -> /perfil
                    urlRedirect = "/get-persons";
                } else {
                    urlRedirect = "/signin?message=" + URLEncoder.encode(
                            "Usuario y/o contraseña incorrectos",
                            StandardCharsets.UTF_8.name());
                }
                break;
            case "/send-email":
                String email = req.getParameter("username");
                if (email != null) {
                    authService.sendEmail(email);
                    urlRedirect = "/signin?message=" + URLEncoder.encode(
                            "Si existe una cuenta con este usuario," +
                                    " se ha enviado un correo electrónico.",
                            StandardCharsets.UTF_8.name());
                } else {
                    urlRedirect = "/signin?message=" + URLEncoder.encode(
                            "Error al enviar el correo de recuperación",
                            StandardCharsets.UTF_8.name());
                }
                break;
            default:
                session = req.getSession();
                session.invalidate();
                urlRedirect = "/";
                break;
        }
        resp.sendRedirect(req.getContextPath() + urlRedirect);
    }
}
