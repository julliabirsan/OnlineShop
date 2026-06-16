package com.link.OnlineShop.controller;

import com.link.OnlineShop.database.Category;
import com.link.OnlineShop.database.Product;
import com.link.OnlineShop.dto.ProductRequest;
import com.link.OnlineShop.dto.ProductResponse;
import com.link.OnlineShop.service.CategoryService;
import com.link.OnlineShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public List<ProductResponse> getAllProducts(){

        List<ProductResponse> productResponseListDto = new ArrayList<>();
        List<Product> productList = productService.findAllProducts();
        for (Product product : productList){
            ProductResponse productResponse = new ProductResponse();
            productResponse.setName(product.getName());
            productResponse.setBrand(product.getBrand());
            productResponse.setPrice(product.getPrice());
            productResponse.setCategoryName(product.getCategory().getName());
            productResponse.setId(product.getId());
            productResponseListDto.add(productResponse);
        }

        return productResponseListDto;
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping
    public Product saveProduct(@RequestBody ProductRequest productRequest){
        // daca exista categoria? -> salvam produsul _ categorie
        //dar daca nu exista? o sa salvam noua categorie
        //1 -> cautam categoria
        Category category = categoryService.getCategoryByName(productRequest.getCategoryName());

        //2 daca nu exista, salvam categoria
        if (category == null){
            category = new Category();
            category.setName(productRequest.getCategoryName());
            category = categoryService.saveCategory(category);
        }
        //3 salvarea produsului
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setBrand(productRequest.getBrand());
        product.setPrice(productRequest.getPrice());
        product.setImgSrc(productRequest.getImgSrc());
        product.setCategory(category);
        return productService.saveProduct(product);
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable int id){
        productService.deleteProductById(id);
        return "ok delete";
    }


}
