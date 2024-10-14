package com.example.app.configuration.listeners;

import com.example.app.controllers.UserController;
import com.example.app.services.UserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
public class CreateControllers implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        UserService userService = (UserService) sce.getServletContext().getAttribute("userService");
        sce.getServletContext().setAttribute("userController", new UserController(userService));
    }
}
