package com.example.app.configuration.listeners;

import com.example.app.repositories.UserRepository;
import com.example.app.services.UserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
public class CreateServices implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        UserRepository userRepository = new UserRepository();
        sce.getServletContext().setAttribute("userService", new UserService(userRepository));
    }
}
