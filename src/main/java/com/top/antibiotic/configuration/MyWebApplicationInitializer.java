package com.top.antibiotic.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Configuration
public class MyWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //servletContext.setInitParameter("spring.profiles.active", "dev");
        servletContext.setInitParameter("spring.profiles.active", "prod");
    }
}