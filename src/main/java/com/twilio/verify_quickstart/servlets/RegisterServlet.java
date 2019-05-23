package com.twilio.verify_quickstart.servlets;

import com.twilio.verify_quickstart.models.User;
import com.twilio.verify_quickstart.services.*;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService;

    private AuthService authService;

    private VerificationService verificationService;

    @SuppressWarnings("unused")
    public RegisterServlet() {
        this(new UserService(), new AuthService(), new TwilioVerification());
    }

    public RegisterServlet(UserService userService, AuthService authService,
                           VerificationService verificationService) {
        this.userService = userService;
        this.authService = authService;
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

        authService.login(request.getSession(), user);

        VerificationResult result = verificationService.startVerification(phone, channel);
        if(result.isValid()) {
            response.sendRedirect("/verify");
        } else {
            userService.delete(user);
            request.setAttribute("message", String.join("\n", result.getErrors()));
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("message", FlashMessageHandler.getMessage(request));
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
