package com.twilio.verify_quickstart.servlets;

import com.twilio.verify_quickstart.models.User;
import com.twilio.verify_quickstart.services.UserService;
import com.twilio.verify_quickstart.services.VerificationResult;
import com.twilio.verify_quickstart.services.VerificationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class VerifyServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private UserService userService;

    @Mock
    private VerificationService verificationService;

    private VerifyServlet verifyServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        verifyServlet = new VerifyServlet(userService, verificationService);
    }

    @Test
    public void testDoGetReturnsVerifyView() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        verifyServlet.doGet(request, response);

        verify(request).getRequestDispatcher("/verify.jsp");
    }

    @Test
    public void testDoPostWithInvalidCodeReturnVerifyView() throws Exception {
        User user = new User("john", "pass", "12345678", false);
        VerificationResult result = new VerificationResult(new String[]{"Invalid code"});

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("code")).thenReturn("123456");
        when(verificationService.checkVerification(anyString(), anyString())).thenReturn(result);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        verifyServlet.doPost(request, response);

        verify(verificationService).checkVerification(anyString(), anyString());
        verify(userService, never()).update(any(User.class));
        verify(request).setAttribute(eq("message"), anyString());
        verify(request).getRequestDispatcher("/verify.jsp");
    }

    @Test
    public void testDoPostWithValidPhoneNumberAndValidCode() throws Exception {
        User user = new User("john", "12345678", "pass", false);
        VerificationResult result = new VerificationResult("id");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("code")).thenReturn("123456");
        when(verificationService.checkVerification(anyString(), anyString())).thenReturn(result);

        verifyServlet.doPost(request, response);

        verify(verificationService).checkVerification(anyString(), anyString());
        verify(userService).update(any(User.class));
        verify(response).sendRedirect("/");
    }
}
