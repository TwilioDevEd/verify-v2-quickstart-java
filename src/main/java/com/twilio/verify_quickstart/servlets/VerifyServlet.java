package com.twilio.verify_quickstart.servlets;

import com.twilio.verify_quickstart.models.User;
import com.twilio.verify_quickstart.services.UserService;
import com.twilio.verify_quickstart.services.TwilioVerification;
import com.twilio.verify_quickstart.services.VerificationResult;
import com.twilio.verify_quickstart.services.VerificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/verify")
public class VerifyServlet extends HttpServlet {

    private UserService userService;

    private VerificationService verificationService;

    @SuppressWarnings("unused")
    public VerifyServlet() {
        this(new UserService(), new TwilioVerification());
    }

    public VerifyServlet(UserService userService, VerificationService verificationService) {
        this.userService = userService;
        this.verificationService = verificationService;
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");

        User user = (User)request.getSession().getAttribute("user");

        VerificationResult result = verificationService.checkVerification(user.getPhoneNumber(), code);
        if(result.isValid()) {
            user.setVerified(true);
            userService.update(user);
            response.sendRedirect("/");
        } else {
            request.setAttribute("message",result.getErrors().toString());
            request.getRequestDispatcher("/verify.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("message", FlashMessageHandler.getMessage(request));
        request.getRequestDispatcher("/verify.jsp").forward(request, response);
    }
}
