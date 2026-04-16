package com.link.OnlineShop.service;

import com.link.OnlineShop.database.Product;
import com.link.OnlineShop.database.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductDao productDao;

    public List<Product> findAllProducts(){
        return productDao.findAll();
    }
}
