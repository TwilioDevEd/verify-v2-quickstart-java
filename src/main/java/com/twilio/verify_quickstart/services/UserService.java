package com.twilio.verify_quickstart.services;

import com.twilio.verify_quickstart.models.User;

import javax.persistence.*;
import java.util.List;

public class UserService {
    private EntityManager entityManager;

    public UserService() {
        this("verify-v2");
    }

    public UserService(String persistenceUnitName) {
        this.entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
    }

    public User find(long id) {
        entityManager.getEntityManagerFactory().getCache().evictAll();
        User user = entityManager.find(User.class, id);

        if(user != null) {
            entityManager.refresh(user);
        }

        return user;
    }

    public User findByPhone(String phone) {
        User user = null;

        try {
            user = (User) entityManager.createQuery("SELECT u FROM User u WHERE u.phoneNumber = :phone")
                    .setParameter("phone", phone)
                    .getSingleResult();
        } catch (NoResultException ex) {
            System.out.println(ex.getMessage());
        }

        return user;
    }

    public User findByUsername(String username) {
        User user = null;

        try {
            user = (User) entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException ex) {
            System.out.println(ex.getMessage());
        }

        return user;
    }

    public User create(User user) {

        getTransaction().begin();
        entityManager.persist(user);
        getTransaction().commit();

        return user;

    }

    public User update(User user) {
        getTransaction().begin();
        User updatedUser = entityManager.merge(user);
        getTransaction().commit();

        return updatedUser;
    }

    public void delete(User user) {
        getTransaction().begin();
        entityManager.remove(user);
        getTransaction().commit();
    }

    public List<User> all() {

        return (List<User>)entityManager.createQuery("Select u from User u").getResultList();

    }

    private EntityTransaction getTransaction() {
        return entityManager.getTransaction();
    }
}
