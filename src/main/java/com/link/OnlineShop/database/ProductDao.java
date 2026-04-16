package com.link.OnlineShop.database;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDao {

    @PersistenceContext
    private EntityManager em;

    public List<Product> findAll(){
        return em.createNativeQuery("select * from product", Product.class).getResultList();
    }
}
