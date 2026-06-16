package com.link.OnlineShop.controller;

import com.link.OnlineShop.database.*;
import com.link.OnlineShop.dto.CartProduct;
import com.link.OnlineShop.database.OrderLines;
import com.link.OnlineShop.dto.OrderDetails;
import com.link.OnlineShop.exceptions.UserException;
import com.link.OnlineShop.security.UserSession;
import com.link.OnlineShop.service.ProductService;
import com.link.OnlineShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    UserSession userSession;

    @Autowired
    OrderLinesDao orderLinesDao;

    @Autowired
    OrderDao orderDao;

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
        modelAndView.addObject("cartItemsNo", userSession.getCartSize());
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", productPage.getTotalPages());

        return modelAndView;
    }

    @GetMapping("addToCart")
    public ModelAndView addToCart(@RequestParam(defaultValue = "0") int page, @RequestParam("productId") int productId){
//        if (userSession.getUserId() == 0){
//            return new ModelAndView("redirect:/");
//        }

        ModelAndView modelAndView = new ModelAndView("dashboard");
        userSession.addToCart(productId);
        Page<Product> productDBList = productService.getProductsPage(page, 5);
        modelAndView.addObject("productList", productDBList.getContent());
        modelAndView.addObject("cartItemsNo", userSession.getCartSize());
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", productDBList.getTotalPages());
        return modelAndView;
    }

    @GetMapping("logout")
    public ModelAndView logout(){
//        userSession.setUserId(0);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/details")
    public ModelAndView getProductDetails(@RequestParam("productId") int productId){
        ModelAndView modelAndView = new ModelAndView("productDetails");
        //verificare user session
        modelAndView.addObject("cartItemsNo", userSession.getCartSize());

        Product p = productService.findProductById(productId);
        modelAndView.addObject("p", p);
        return modelAndView;
    }

    @GetMapping("/cart")
    public ModelAndView getCart(){
        //1 cart.html unde sa afisam tot ce contine cosul
        //2 logica pt a citi produsele clientului
        ModelAndView modelAndView = new ModelAndView("cart");
        //verificare securitate userSession
        List<CartProduct> produseCos = new ArrayList<>();
        List<Product> produseDB = productService.findAllProducts();
        double totalOrderAmount = 0;

        for (int idProdusCos : userSession.getCart().keySet()){
            for (Product product : produseDB){
                if (product.getId() == idProdusCos){
                    CartProduct cartProduct = new CartProduct();
                    cartProduct.setCantitate(userSession.getCart().get(idProdusCos));
                    cartProduct.setId(product.getId());
                    cartProduct.setCategory(product.getCategory());
                    cartProduct.setName(product.getName());
                    cartProduct.setBrand(product.getBrand());
                    cartProduct.setPrice(product.getPrice());
                    double priceProductUnits = userSession.getCart().get(idProdusCos)*product.getPrice();
                    cartProduct.setPretTotal(priceProductUnits);
                    totalOrderAmount+=priceProductUnits;
                    produseCos.add(cartProduct);
                }
            }
        }
        modelAndView.addObject("productList", produseCos);
        modelAndView.addObject("totalPretComanda", totalOrderAmount);
        modelAndView.addObject("cartItemsNo", userSession.getCartSize());

        return modelAndView;
    }

    @PostMapping("/sendOrder")
    public ModelAndView sendOrder(){
        ModelAndView modelAndView = new ModelAndView("orderSuccess");

        List<Product> produseDB = productService.findAllProducts();
        Order order = new Order();
        for (int idProdusCos : userSession.getCart().keySet()){
            for (Product product:produseDB){
                if (product.getId() == idProdusCos){
                    OrderLines orderLines = new OrderLines();
                    orderLines.setProductId(idProdusCos);
                    orderLines.setQuantity(userSession.getCart().get(idProdusCos));
                    orderLines.setTotalPrice(userSession.getCart().get(idProdusCos)*product.getPrice());
                    order.setUserId(userSession.getUserId());
                    order.setAddress("strada cu flori");
                    orderLines.setOrder(order);
                    orderLinesDao.save(orderLines);
                }

            }
        }
        userSession.getCart().clear();
        modelAndView.addObject("cartItemsNo", userSession.getCartSize());

        return modelAndView;

    }

    @GetMapping("/history")
    public ModelAndView showOrderHistory(){
        ModelAndView modelAndView = new ModelAndView("orderHistory");
        List<Order> orders = orderDao.findAllByUserId(userSession.getUserId());

        modelAndView.addObject("orders", orders);
        modelAndView.addObject("cartItemsNo", userSession.getCartSize());

        return modelAndView;
    }

    @GetMapping("/orderDetails")
    public ModelAndView showORderDetails(@RequestParam("orderId") int orderId){
        ModelAndView modelAndView = new ModelAndView("orderDetails");
        Iterable<OrderLines> orderLines = orderLinesDao.findAll();
        List<OrderDetails> orderDetailPerUSer = new ArrayList<>();

        for (OrderLines orderLinesDetails: orderLines){
            if (orderLinesDetails.getOrder().getId() == orderId){
                Product product = productService.findProductById(orderLinesDetails.getProductId());
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setProductName(product.getName());
                orderDetails.setCategory(product.getCategory());
                orderDetails.setPricePerUnit(product.getPrice());
                orderDetails.setQuantity(orderLinesDetails.getQuantity());
                orderDetails.setTotalPrice(orderLinesDetails.getTotalPrice());
                orderDetailPerUSer.add(orderDetails);
            }
        }
        modelAndView.addObject("orderLines", orderDetailPerUSer);
        return modelAndView;
    }
}
