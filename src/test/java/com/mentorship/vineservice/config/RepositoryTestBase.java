package com.mentorship.vineservice.config;


import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;


@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class RepositoryTestBase {
    /**
     * This method is called to add properties with dynamic values
     * to the Environment's set of PropertySources.
     * */
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("wine_db")
            .withUsername("user")
            .withPassword("password");
        mysql.start();

        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }
}
