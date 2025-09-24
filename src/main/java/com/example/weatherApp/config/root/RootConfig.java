package com.example.weatherApp.config.root;


import com.example.weatherApp.config.root.persistence.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DataConfig.class,
        LiquibaseConfig.class,
        RestClientConfig.class,
        JacksonConfig.class
        })
public class RootConfig {
}
