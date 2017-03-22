package edu.norbertzardin.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import javax.servlet.ServletContext;

public class WebAppInit implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(ApplicationContext.class);

        // Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));
        container.addListener(new RequestContextListener());

        container.addFilter(
                "springSecurityFilterChain",
                new DelegatingFilterProxy("springSecurityFilterChain"))

                .addMappingForUrlPatterns(null, false, "/*");

    }
}
