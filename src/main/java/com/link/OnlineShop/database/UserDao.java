package com.link.OnlineShop.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserDao {
    @PersistenceContext
    private EntityManager em;

    public List<User> findByEmail(String email){
        //return jdbcTemplate.query("select * from users where email = '" + email + "';", new UserRowMapper());
        Query query = em.createNativeQuery("select * from users where email = '" + email +"'", User.class);
        return query.getResultList();
    }

    @Transactional
    public void save(String email, String password){
        //jdbcTemplate.update("insert into users values(null, ?, ?)", email, password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        Address dummyAddress = new Address();
        dummyAddress.setStreetName("dummy address name");
        dummyAddress.setStreetNo(0);
        dummyAddress.setBuilding("dummy building");
        dummyAddress.setCity("dummy city");
        dummyAddress.setCountry("dummy country");

        user.setAddress(dummyAddress);
        em.persist(user);
    }
}
