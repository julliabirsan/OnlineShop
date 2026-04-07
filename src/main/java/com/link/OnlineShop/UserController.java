package com.link.OnlineShop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("register-form")
    public ModelAndView registerAction(@RequestParam("email") String email,
                                       @RequestParam("password1") String password1,
                                       @RequestParam("password2") String password2){
        ModelAndView modelAndView = new ModelAndView("register");

        //sa verificam daca string-urile nu sunt empty -> ERROR!!!!!!

        if (!password1.equals(password2)){
            modelAndView.addObject("error", "Parolele nu sunt identice");
            return modelAndView;
        } else {
            //sa verificam daca exista utilizatorul!!!!, daca da -> ERROR

            //daca nu, sa-l adaugam in bd
            jdbcTemplate.update("insert into users values(null, ?,?)", email, password1);
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
        //daca avem empty String!!! -> ERROR
        //sa verific daca utilizatorul exista in BD
        //daca exista in BD, sa verificam parola
        List<User> userList = jdbcTemplate.query("select * from users where email ='"+email+"';", new UserRowMapper());
        if (userList.size() == 0){
            modelAndView.addObject("error", "credentialele nu sunt corecte");
        }
        if (userList.size() > 1){
            modelAndView.addObject("error", "credentialele nu sunt corecte");
        }
        if (userList.size() == 1){
            User userFromDB = userList.get(0);
            //verificam daca parola din bd corespunde cu ce a venit din FE(frontend)
            if (!userFromDB.getPassword().equals(password)){
                modelAndView.addObject("error", "credentialele nu sunt corecte");
            } else {
                modelAndView = new ModelAndView("redirect:/dashboard");
            }
        }
        return modelAndView;
    }

    @GetMapping("dashboard")
    public ModelAndView dashboard(){
        return new ModelAndView("dashboard");
    }
}
