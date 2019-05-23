package com.twilio.verify_quickstart.servlets;

import com.twilio.verify_quickstart.models.User;
import com.twilio.verify_quickstart.services.AuthService;
import com.twilio.verify_quickstart.services.UserService;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;
    private AuthService authService;

    public LoginServlet() {
        this(new UserService(), new AuthService());
    }

    public LoginServlet(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // get request parameters for username and password
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.findByUsername(username);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            authService.login(request.getSession(), user);
            response.sendRedirect("/");
        } else {
            request.setAttribute("message", "Login failed: invalid credentials");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
