package com.twilio.verify_quickstart.servlets;

import com.twilio.verify_quickstart.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users")
public class UsersServlet extends HttpServlet {

    private UserService userService = new UserService();

    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("users", userService.all());
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}
