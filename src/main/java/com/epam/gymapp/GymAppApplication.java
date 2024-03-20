package com.epam.gymapp;

import com.epam.gymapp.config.GymApplicationConfiguration;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class GymAppApplication implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
    rootContext.register(GymApplicationConfiguration.class);
    rootContext.setConfigLocation("com.epam.gymapp.config");

    servletContext.addListener(new ContextLoaderListener(rootContext));

    ServletRegistration.Dynamic dispatcher =
        servletContext.addServlet("dispatcher",  new DispatcherServlet(rootContext));
    dispatcher.setLoadOnStartup(1);
    dispatcher.addMapping("/*");

    FilterRegistration.Dynamic encodingFilter =
        servletContext.addFilter("encoding-filter", new CharacterEncodingFilter());
    encodingFilter.setInitParameter("encoding", "UTF-8");
    encodingFilter.setInitParameter("forceEncoding", "true");
    encodingFilter.addMappingForUrlPatterns(null, true, "/*");
  }
}
