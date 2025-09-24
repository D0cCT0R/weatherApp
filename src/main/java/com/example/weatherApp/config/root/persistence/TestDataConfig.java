package com.example.weatherApp.config.root.persistence;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@Profile("test")
@PropertySource("classpath:application-test.properties")
public class TestDataConfig {

    @Value("${db.url}")
    private String DATABASE_URL;
    @Value("${db.user}")
    private String DATABASE_USERNAME;
    @Value("${db.password}")
    private String DATABASE_PASSWORD;
    @Value("${db.driver-class}")
    private String DATABASE_DRIVER;

    @Bean
    public DataSource testDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(DATABASE_DRIVER);
        config.setJdbcUrl(DATABASE_URL);
        config.setUsername(DATABASE_USERNAME);
        config.setPassword(DATABASE_PASSWORD);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(3000);
        config.setIdleTimeout(60000);
        config.setMaxLifetime(1800000);
        config.addDataSourceProperty("preparedStatementCacheQueries", 256);
        config.addDataSourceProperty("preparedStatementCacheSizeMiB", 16);
        config.addDataSourceProperty("applicationName", "WeatherApp");
        return new HikariDataSource(config);
    }

    @Bean
    public Properties hibernateTestProperties() {
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", true);
        props.put("hibernate.format_sql", true);
        props.put("hibernate.use_sql_comments", true);
        props.put("hibernate.jdbc.batch_size", 30);
        props.put("hibernate.order_inserts", true);
        props.put("hibernate.order_updates", true);
        return props;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.example.weatherApp.entity");
        factory.setHibernateProperties(hibernateTestProperties());
        return factory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

}
