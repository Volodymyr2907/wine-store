package com.mentorship.vineservice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainerConfig {

    @Bean
    public MySQLContainer<?> mySQLContainer() {
        MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.26"))
            .withDatabaseName("wine_db")
            .withUsername("user")
            .withPassword("password");
        mySQLContainer.start();
        return mySQLContainer;
    }

    @Bean
    public DataSource dataSource(MySQLContainer<?> mySQLContainer) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(mySQLContainer.getJdbcUrl());
        hikariConfig.setUsername(mySQLContainer.getUsername());
        hikariConfig.setPassword(mySQLContainer.getPassword());
        hikariConfig.setDriverClassName(mySQLContainer.getDriverClassName());
        return new HikariDataSource(hikariConfig);
    }
}
