package org.magnum.dataup.infrastrucutre.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;


@Configuration
public class MultipartConfig {

    private static final int MAX_REQUEST_SIZE_IN_MB = 150;

    // This configuration element adds the ability to accept multipart
    // requests to the web container.
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        // Setup the application container to be accept multipart requests
        final MultipartConfigFactory factory = new MultipartConfigFactory();
        // Place upper bounds on the size of the requests to ensure that
        // clients don't abuse the web container by sending huge requests
        factory.setMaxFileSize(DataSize.ofMegabytes(MAX_REQUEST_SIZE_IN_MB));
        factory.setMaxRequestSize(DataSize.ofMegabytes(MAX_REQUEST_SIZE_IN_MB));

        // Return the configuration to setup multipart in the container
        return factory.createMultipartConfig();
    }
}
