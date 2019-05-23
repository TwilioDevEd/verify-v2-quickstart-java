package com.twilio.verify_quickstart.servlets;

import javax.servlet.http.HttpServletRequest;

class FlashMessageHandler {

    static void setMessage(HttpServletRequest request, String message) {
        request.getSession().setAttribute("message", message);
    }

    static void setMessage(HttpServletRequest request, String[] messages) {
        request.getSession().setAttribute("message", String.join("\n", messages));
    }


    static String getMessage(HttpServletRequest request) {
        String message = (String)request.getSession().getAttribute("message");
        request.getSession().setAttribute("message", null);
        return message;
    }
}
