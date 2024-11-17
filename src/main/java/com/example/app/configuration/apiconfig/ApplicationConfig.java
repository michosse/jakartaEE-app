package com.example.app.configuration.apiconfig;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {
}
