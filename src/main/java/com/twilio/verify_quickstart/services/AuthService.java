package com.twilio.verify_quickstart.services;

import com.twilio.verify_quickstart.models.User;

import javax.servlet.http.HttpSession;

public class AuthService {

    public void login(HttpSession session, User user) {
        session.setMaxInactiveInterval(1200);
        session.setAttribute("user", user);
    }


    public void logout(HttpSession session) {
        session.invalidate();
    }
}
