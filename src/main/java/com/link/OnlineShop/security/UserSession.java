package com.link.OnlineShop.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;

@Component
@SessionScope
public class UserSession {
    private int userId; //=0
//UserSession s = new UserSession(); -> singleton -> o singura instanta
    //app a fost oprita -> s-a sters usersession

    private HashMap<Integer, Integer> cart = new HashMap<>();

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public HashMap<Integer, Integer> getCart(){
        return cart;
    }

    public void addToCart(int idProdus){
        if (cart.containsKey(idProdus)){
            int cantitateNoua = cart.get(idProdus);
            cantitateNoua+=1;
            cart.put(idProdus, cantitateNoua);
        } else {
            cart.put(idProdus, 1);
        }
    }

    public int getCartSize(){
        int size = 0;
        for (Integer i : cart.values()){
            size+=i;
        }
        return size;
    }

}
