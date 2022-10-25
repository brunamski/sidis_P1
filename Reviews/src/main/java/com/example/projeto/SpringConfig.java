package com.example.projeto;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.awt.image.BufferedImage;

@Configuration
@EnableConfigurationProperties({ FileStorageProperties.class })
public class SpringConfig {

    // Etags

    /*
     * @Bean public FilterRegistrationBean<ShallowEtagHeaderFilter>
     * shallowEtagHeaderFilter() { FilterRegistrationBean<ShallowEtagHeaderFilter>
     * filterRegistrationBean = new FilterRegistrationBean<>( new
     * ShallowEtagHeaderFilter()); filterRegistrationBean.addUrlPatterns("/foos/*");
     * filterRegistrationBean.setName("etagFilter"); return filterRegistrationBean;
     * }
     */

    // We can also just declare the filter directly
    @Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    /*
     * OpenAPI
     */
    @Bean
    public OpenAPI openApi() {
        final String securitySchemeName = "bearer authorization";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT"))
                )
                .info(new Info()
                        .title("ACME API")
                        .description("Sample demo API")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("")
                                .email("@isep.ipp.pt"))
                        .termsOfService("TOC")
                        .license(new License()
                                .name("MIT")
                                .url("#")));
    }

    /*
     * Barcode
     */

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }
}
