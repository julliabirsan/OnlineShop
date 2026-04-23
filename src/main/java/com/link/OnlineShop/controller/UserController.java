package com.link.OnlineShop.controller;

import com.link.OnlineShop.database.Product;
import com.link.OnlineShop.exceptions.UserException;
import com.link.OnlineShop.security.UserSession;
import com.link.OnlineShop.service.ProductService;
import com.link.OnlineShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    UserSession userSession;

    private int cartItems = 0;

    @GetMapping("register-form")
    public ModelAndView registerAction(@RequestParam("email") String email,
                                       @RequestParam("password1") String password1,
                                       @RequestParam("password2") String password2){
        ModelAndView modelAndView = new ModelAndView("register");

        try {
            userService.save(email, password1, password2);
        } catch (UserException e) {
            modelAndView.addObject("error", e.getMessage());
            return modelAndView;
        }

        //redirectionam clientul catre pagina de index.html
        return new ModelAndView("redirect:/index.html");
    }

    @GetMapping("register")
    public ModelAndView register(){
        return new ModelAndView("register");
    }

    @GetMapping("login")
    public ModelAndView login(@RequestParam("email") String email,
                              @RequestParam("password") String password){
        ModelAndView modelAndView = new ModelAndView("index");
        try {
            userService.login(email,password);
            modelAndView = new ModelAndView("redirect:/dashboard");
        } catch (UserException e) {
            modelAndView.addObject("error", e.getMessage());
            return modelAndView;
        }
        return modelAndView;
    }

    @GetMapping("dashboard")
    public ModelAndView dashboard(@RequestParam(defaultValue = "0") int page){
//        if (userSession.getUserId() == 0){
//           return new ModelAndView("redirect:/");
//        }

        ModelAndView modelAndView = new ModelAndView("dashboard");
        //List<Product> productDBList = productService.findAllProducts();
        Page<Product> productPage = productService.getProductsPage(page, 5);
        modelAndView.addObject("productList", productPage);
        modelAndView.addObject("cartItemsNo", cartItems);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", productPage.getTotalPages());

        return modelAndView;
    }

    @GetMapping("addToCart")
    public ModelAndView addToCart(){
//        if (userSession.getUserId() == 0){
//            return new ModelAndView("redirect:/");
//        }

        ModelAndView modelAndView = new ModelAndView("dashboard");
        cartItems++;
        List<Product> productDBList = productService.findAllProducts();
        modelAndView.addObject("productList", productDBList);
        modelAndView.addObject("cartItemsNo", cartItems);
        return modelAndView;
    }

    @GetMapping("logout")
    public ModelAndView logout(){
//        userSession.setUserId(0);
        return new ModelAndView("redirect:/");
    }
}
