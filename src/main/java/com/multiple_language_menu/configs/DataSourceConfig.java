package com.multiple_language_menu.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class DataSourceConfig {
    @Autowired
    private Environment evn;
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(evn.getProperty("spring.datasource.url"));
        dataSourceBuilder.driverClassName(evn.getProperty("mysql.driver"));
        dataSourceBuilder.username(evn.getProperty("spring.datasource.username"));
        dataSourceBuilder.password(evn.getProperty("spring.datasource.password"));
        return dataSourceBuilder.build();
    }
}
