package com.raihan.nexaorder.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/admin/**", "/customer/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Path.of("src/main/resources/static/uploads/products")
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        if (!uploadPath.endsWith("/")) {
            uploadPath = uploadPath + "/";
        }

        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations(uploadPath);
    }
}
