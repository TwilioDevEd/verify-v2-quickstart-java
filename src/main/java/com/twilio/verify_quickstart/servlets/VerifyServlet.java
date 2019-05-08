package com.twilio.verify_quickstart.servlets;

import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import com.twilio.verify_quickstart.models.User;
import com.twilio.verify_quickstart.models.UserService;
import com.twilio.verify_quickstart.services.PhoneVerification;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/verify")
public class VerifyServlet extends HttpServlet {

    private UserService userService;

    private PhoneVerification verificationService;

    @SuppressWarnings("unused")
    public VerifyServlet() {
        this(new UserService(), new PhoneVerification());
    }

    public VerifyServlet(UserService userService, PhoneVerification verificationService) {
        this.userService = userService;
        this.verificationService = verificationService;
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String phone = (String)request.getSession().getAttribute("phone");

        User user = userService.findByPhone(phone);

        if(user == null) {
            response.sendRedirect("/login");
            return;
        }

        VerificationCheck verification = null;
        try {
            verification = verificationService.checkVerification(phone, code);
        } catch (ApiException e) {
            request.setAttribute("message", e.getMoreInfo());
        }

        if(verification != null && verification.getValid()) {
            user.setVerified(true);
            userService.update(user);
            response.sendRedirect("/");
        } else {
            request.setAttribute("message", "Verification failed");
            request.getRequestDispatcher("/verify.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("phone") != null) {
            request.getRequestDispatcher("/verify.jsp").forward(request, response);
        } else {
            response.sendRedirect("/login");
        }
    }
}
