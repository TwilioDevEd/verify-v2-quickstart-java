package com.twilio.verify_quickstart.servlets;

import com.twilio.verify_quickstart.models.User;
import com.twilio.verify_quickstart.services.AuthService;
import com.twilio.verify_quickstart.services.UserService;
import com.twilio.verify_quickstart.services.TwilioVerification;
import com.twilio.verify_quickstart.services.VerificationResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.RollbackException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class RegisterServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private TwilioVerification verificationService;

    private RegisterServlet registerServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(session.getAttribute(anyString())).thenReturn("something");
        when(request.getParameter(anyString())).thenReturn("something");
        when(request.getParameter("full_phone")).thenReturn("123456789");
        when(request.getParameter("channel")).thenReturn("sms");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        registerServlet = new RegisterServlet(userService, authService, verificationService);
    }

    @Test
    public void testDoPostUserCreationFails() throws Exception {
        when(userService.create(any(User.class))).thenThrow(new RollbackException("Failed"));

        registerServlet.doPost(request, response);

        verify(verificationService, never()).startVerification(anyString(), anyString());
        verify(request).setAttribute(eq("message"), anyString());
        verify(request).getRequestDispatcher("/register.jsp");
    }

    @Test
    public void testDoPostUserCreatedAndValidationRequestFails() throws Exception {
        User user = new User("john", "123456789", "pass", false);
        VerificationResult result = new VerificationResult(new String[]{"Failed"});

        when(userService.create(any(User.class))).thenReturn(user);
        when(verificationService.startVerification(anyString(), anyString())).thenReturn(result);

        registerServlet.doPost(request, response);

        verify(verificationService).startVerification("123456789", "sms");
        verify(request).setAttribute(eq("message"), anyString());
        verify(request).getRequestDispatcher("/register.jsp");
    }

    @Test
    public void testDoPostUserCreatedAndValidationRequestSucceed() throws Exception {
        User user = new User("john", "123456789", "pass", false);
        VerificationResult result = new VerificationResult("id");

        when(userService.create(any(User.class))).thenReturn(user);
        when(verificationService.startVerification(anyString(), anyString())).thenReturn(result);

        registerServlet.doPost(request, response);

        verify(verificationService).startVerification("123456789", "sms");
        verify(response).sendRedirect("/verify");
    }
}
