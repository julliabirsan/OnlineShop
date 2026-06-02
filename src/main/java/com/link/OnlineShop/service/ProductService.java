package com.link.OnlineShop.service;

import com.link.OnlineShop.database.Category;
import com.link.OnlineShop.database.CategoryDao;
import com.link.OnlineShop.database.Product;
import com.link.OnlineShop.database.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductDao productDao;

    public List<Product> findAllProducts(){
        return (List<Product>) productDao.findAll();
    }

    public Page<Product> getProductsPage(int page, int size){
        return productDao.findAll(PageRequest.of(page, size));
    }

    public Product saveProduct(Product product){
        return productDao.save(product);
    }

    public void deleteProductById(int id){
        productDao.deleteById(id);
    }
}
