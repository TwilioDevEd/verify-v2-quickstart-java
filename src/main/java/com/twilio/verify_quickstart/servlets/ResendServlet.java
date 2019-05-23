package com.twilio.verify_quickstart.servlets;

import com.twilio.verify_quickstart.models.User;
import com.twilio.verify_quickstart.services.TwilioVerification;
import com.twilio.verify_quickstart.services.VerificationResult;
import com.twilio.verify_quickstart.services.VerificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/resend")
public class ResendServlet extends HttpServlet {

    private final VerificationService service;

    @SuppressWarnings("unused")
    public ResendServlet() {
        this(new TwilioVerification());
    }

    public ResendServlet(VerificationService service) {
        this.service = service;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String channel = request.getParameter("channel");

        User user = (User)request.getSession().getAttribute("user");

        VerificationResult result = service.startVerification(user.getPhoneNumber(), channel);
        if(result.isValid()) {
            FlashMessageHandler.setMessage(request,
                    "A new verification code has been sent to " + user.getPhoneNumber());
        } else {
            FlashMessageHandler.setMessage(request, String.join("\n", result.getErrors()));
        }
        response.sendRedirect("/verify");
    }
}
