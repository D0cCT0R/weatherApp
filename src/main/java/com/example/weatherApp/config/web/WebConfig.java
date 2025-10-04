package com.example.weatherApp.config.web;

import com.example.weatherApp.interceptors.AuthInterceptor;
import com.example.weatherApp.interceptors.PublicPagesInterceptor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@Import({ThymeleafConfig.class})
@EnableWebMvc
@ComponentScan("com.example.weatherApp")
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {


    private final AuthInterceptor authInterceptor;
    private final PublicPagesInterceptor publicPagesInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("/css/");
        registry.addResourceHandler("/img/**")
                .addResourceLocations("/img/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/sign-in",
                        "/sign-up",
                        "/css/**",
                        "/images/**",
                        "/error");
        registry.addInterceptor(publicPagesInterceptor).addPathPatterns("/sign-in", "/sign-up");
    }
}

