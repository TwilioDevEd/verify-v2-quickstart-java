package com.twilio.verify_quickstart.servlets;

import com.twilio.exception.ApiException;
import com.twilio.verify_quickstart.models.User;
import com.twilio.verify_quickstart.models.UserService;
import com.twilio.verify_quickstart.services.PhoneVerification;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService;

    private PhoneVerification verificationService;

    @SuppressWarnings("unused")
    public RegisterServlet() {
        this(new UserService(), new PhoneVerification());
    }

    public RegisterServlet(UserService userService, PhoneVerification verificationService) {
        this.userService = userService;
        this.verificationService = verificationService;
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {


        String phone = request.getParameter("full_phone");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String channel = request.getParameter("channel");

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

        User user;
        try {
            user = userService.create(new User(username, hashed, phone, false));
        } catch (RollbackException e) {

            request.setAttribute("message", "User creation failed: " + e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);

            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("phone", phone);

        try {
            verificationService.startVerification(phone, channel);
            response.sendRedirect("/verify");
        } catch (ApiException e) {
            userService.delete(user);
            request.setAttribute("message", e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }

    }

    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
