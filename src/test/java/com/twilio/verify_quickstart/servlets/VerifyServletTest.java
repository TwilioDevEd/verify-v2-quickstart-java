package com.twilio.verify_quickstart.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import com.twilio.verify_quickstart.models.User;
import com.twilio.verify_quickstart.models.UserService;
import com.twilio.verify_quickstart.services.PhoneVerification;
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
    private PhoneVerification verificationService;

    private VerifyServlet verifyServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        verifyServlet = new VerifyServlet(userService, verificationService);
    }

    @Test
    public void testDoPostWithInvalidPhoneNumber() throws Exception {

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("phone")).thenReturn("12345678");
        when(request.getParameter("code")).thenReturn("123456");
        when(userService.findByPhone(anyString())).thenReturn(null);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        verifyServlet.doPost(request, response);

        verify(verificationService, never()).checkVerification(anyString(), anyString());
        verify(userService, never()).update(any(User.class));
        verify(response).sendRedirect("/login");
    }

    @Test
    public void testDoPostWithValidPhoneNumberAndInvalidCode() throws Exception {
        User user = new User("john", "12345678", "pass", false);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("phone")).thenReturn("12345678");
        when(request.getParameter("code")).thenReturn("123456");
        when(userService.findByPhone(anyString())).thenReturn(user);
        when(verificationService.checkVerification(anyString(), anyString())).thenReturn(null);
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
        VerificationCheck verificationCheck = VerificationCheck.fromJson(
                "{" +
                "  \"sid\": \"VEXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\"," +
                "  \"service_sid\": \"VAXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\"," +
                "  \"account_sid\": \"ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\"," +
                "  \"to\": \"+12345678\"," +
                "  \"channel\": \"sms\"," +
                "  \"status\": \"approved\"," +
                "  \"valid\": true," +
                "  \"amount\": null," +
                "  \"payee\": null," +
                "  \"date_created\": \"2015-07-30T20:00:00Z\"," +
                "  \"date_updated\": \"2015-07-30T20:00:00Z\"" +
                "}", new ObjectMapper());

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("phone")).thenReturn("12345678");
        when(request.getParameter("code")).thenReturn("123456");
        when(userService.findByPhone(anyString())).thenReturn(user);
        when(verificationService.checkVerification(anyString(), anyString())).thenReturn(verificationCheck);

        verifyServlet.doPost(request, response);

        verify(verificationService).checkVerification(anyString(), anyString());
        verify(userService).update(any(User.class));
        verify(response).sendRedirect("/");
    }
}
