package com.twilio.verify_quickstart.filters;

import com.twilio.verify_quickstart.models.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Verify", urlPatterns = {""})
public class VerifiedFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ServletException("No session available.");
        }

        User user = (User) session.getAttribute("user");
        if(user == null) {
            throw new ServletException("No user logged in");
        }

        if (!user.isVerified()) {
            response.sendRedirect("/verify");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {}
}
