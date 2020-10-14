package com.on.spring.controller;

import com.on.spring.service.UserService;
import org.springframework.stereotype.Controller;

import javax.annotation.PreDestroy;

@Controller
public class UserController {

    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PreDestroy
    public void destroy() {

    }
}
