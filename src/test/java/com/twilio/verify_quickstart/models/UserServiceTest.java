package com.twilio.verify_quickstart.models;

import com.twilio.verify_quickstart.services.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class UserServiceTest {

    private UserService service;

    @Before
    public void setUp() {
        service = new UserService("verify-v2-test");
    }

    @Test
    public void testUserCreationRetrievalDeletion() {
        User user = new User("john.doe", "pass", "+1234567890", false);

        // Create new
        user = service.create(user);
        long id = user.getId();

        assert id > 0;
        assert service.find(id) != null;


        // Find by username
        user = service.findByUsername("john.doe");

        assert user != null;
        assert user.getId() == id;
        assert user.getUsername().equals("john.doe");
        assert user.getPhoneNumber().equals("+1234567890");
        assert user.getPassword().equals("pass");
        assert !user.isVerified();

        // Find by phone
        user = service.findByPhone("+1234567890");

        assert user != null;
        assert user.getId() == id;
        assert user.getUsername().equals("john.doe");
        assert user.getPhoneNumber().equals("+1234567890");
        assert user.getPassword().equals("pass");
        assert !user.isVerified();

        // Find all
        List<User> users = service.all();

        assert users.size() == 1;

        // Update

        user.setVerified(true);
        user = service.update(user);

        assert user.isVerified();

        // Delete
        service.delete(user);

        assert service.find(id) == null;
    }
}
