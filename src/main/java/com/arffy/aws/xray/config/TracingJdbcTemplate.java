package com.arffy.aws.xray.config;


import org.springframework.jdbc.core.JdbcTemplate;

public class TracingJdbcTemplate extends JdbcTemplate {

    public TracingJdbcTemplate(javax.sql.DataSource dataSource) {
        super(dataSource);
    }

}
