package com.link.OnlineShop.service;

import com.link.OnlineShop.database.User;
import com.link.OnlineShop.database.UserDao;
import com.link.OnlineShop.database.UserRowMapper;
import com.link.OnlineShop.exceptions.UserException;
import com.link.OnlineShop.security.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    UserSession userSession;

    public void save(String email, String password1, String password2) throws UserException{
        //sa verificam daca string-urile nu sunt empty -> ERROR!!!!!!
        if(email.isEmpty() && password1.isEmpty() && password2.isEmpty()){
                throw new UserException("empty field");
        }

        if (password1.length()<=3 && password2.length()<=3){
                throw new UserException("parola are mai putin de 3 caractere");
        }

        if (!password1.equals(password2)){
            throw new UserException("Parolele nu sunt identice");
        } else {
            //sa verificam daca exista utilizatorul!!!!, daca da -> ERROR

            //daca nu, sa-l adaugam in bd
            //jdbcTemplate.update("insert into users values(null, ?,?)", email, password1);
            userDao.save(email, password1);
        }
    }

    public void login(String email, String password) throws UserException {
        if(email.isEmpty() && password.isEmpty()){
                throw new UserException("empty field");
        }
        //sa verific daca utilizatorul exista in BD
        //daca exista in BD, sa verificam parola
        List<User> userList = userDao.findByEmail(email);
        if (userList.size() == 0){
            throw new UserException("credentialele nu sunt corecte");
        }
        if (userList.size() > 1){
            throw new UserException("credentialele nu sunt corecte");
        }
        if (userList.size() == 1){
            User userFromDB = userList.get(0);
            //verificam daca parola din bd corespunde cu ce a venit din FE(frontend)
            if (!userFromDB.getPassword().equals(password)){
                throw new UserException("credentialele nu sunt corecte");
            } else {
                userSession.setUserId(userFromDB.getId());
            }
        }
    }
}
