package com.link.OnlineShop.service;


import com.link.OnlineShop.database.Category;
import com.link.OnlineShop.database.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    CategoryDao categoryDao;

    public Category getCategoryByName(String categoryName){
        return categoryDao.findByName(categoryName);
    }

    public Category saveCategory(Category category){
        return categoryDao.save(category);
    }

}
