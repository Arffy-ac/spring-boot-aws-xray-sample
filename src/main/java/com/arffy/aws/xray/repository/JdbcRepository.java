package com.arffy.aws.xray.repository;

import com.arffy.aws.xray.config.TracingJdbcTemplate;
import com.arffy.aws.xray.model.AwsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcRepository {

    private final TracingJdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcRepository(TracingJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AwsEntity> findJbcAll() {
        try {
            String sql = "SELECT * FROM aws WHERE id=1";
            Map<String, Object> result = jdbcTemplate.queryForMap(sql);

            List<AwsEntity> awsEntityList = new ArrayList<>();
            if (!result.isEmpty()) {
                AwsEntity awsEntity = new AwsEntity();
                awsEntity.setId((Integer) result.get("id"));
                awsEntity.setName((String) result.get("name"));
                awsEntity.setAge((Integer) result.get("age"));
                awsEntityList.add(awsEntity);
            }
            return awsEntityList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
