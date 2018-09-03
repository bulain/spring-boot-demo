package com.bulain.oauth.ctrl;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserCtrl {
    
    @GetMapping("/api/user")
    public Principal user(Principal user) {
        return user;
    }
    
}
