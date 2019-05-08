package com.twilio.verify_quickstart.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.verify_quickstart.models.User;
import com.twilio.verify_quickstart.models.UserService;
import com.twilio.verify_quickstart.services.PhoneVerification;
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
    private PhoneVerification verificationService;

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

        registerServlet = new RegisterServlet(userService, verificationService);
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
        when(userService.create(any(User.class))).thenReturn(user);
        when(verificationService.startVerification(anyString(), anyString())).thenThrow(new ApiException("Error"));

        registerServlet.doPost(request, response);

        verify(verificationService).startVerification("123456789", "sms");
        verify(request).setAttribute(eq("message"), anyString());
        verify(request).getRequestDispatcher("/register.jsp");
    }

    @Test
    public void testDoPostUserCreatedAndValidationRequestSucceed() throws Exception {
        User user = new User("john", "123456789", "pass", false);
        Verification verification = Verification.fromJson(
                "{" +
                "  \"sid\": \"VEXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\"," +
                "  \"service_sid\": \"VAXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\"," +
                "  \"account_sid\": \"ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\"," +
                "  \"to\": \"+14159373912\"," +
                "  \"channel\": \"sms\"," +
                "  \"status\": \"pending\"," +
                "  \"valid\": null," +
                "  \"date_created\": \"2015-07-30T20:00:00Z\"," +
                "  \"date_updated\": \"2015-07-30T20:00:00Z\"," +
                "  \"lookup\": {" +
                "    \"carrier\": {" +
                "      \"error_code\": null," +
                "      \"name\": \"Carrier Name\"," +
                "      \"mobile_country_code\": \"310\"," +
                "      \"mobile_network_code\": \"150\"," +
                "      \"type\": \"mobile\"" +
                "    }" +
                "  }," +
                "  \"amount\": null," +
                "  \"payee\": null," +
                "  \"url\": \"https://verify.twilio.com/v2/Services/VAXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX/Verifications/VEXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\"" +
                "}", new ObjectMapper());

        when(userService.create(any(User.class))).thenReturn(user);
        when(verificationService.startVerification(anyString(), anyString())).thenReturn(verification);

        registerServlet.doPost(request, response);

        verify(verificationService).startVerification("123456789", "sms");
        verify(response).sendRedirect("/verify");
    }
}
