
package com.mit.scholarship;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionTest implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseConnectionTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM scholarships",
            Integer.class
        );

        System.out.println(
            "DATABASE CONNECTION SUCCESSFUL! Total scholarships: " + count
        );
    }
}
