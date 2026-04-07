package com.link.OnlineShop.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {
    private int userId; //=0
//UserSession s = new UserSession(); -> singleton -> o singura instanta
    //app a fost oprita -> s-a sters usersession
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
