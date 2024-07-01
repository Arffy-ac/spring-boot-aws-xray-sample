package com.arffy.aws.xray.config;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.sql.TracingDataSource;
import com.amazonaws.xray.strategy.sampling.CentralizedSamplingStrategy;
import com.arffy.aws.xray.exception.ApplicationNameNullException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Optional;

@Configuration
@Slf4j
public class AwsConfig {

    @Autowired
    private Environment env;

    @Value("${spring.application.name}")
    private String AWSXRAY_SEGMENT_NAME;
    private static final String SAMPLING_RULE_JSON = "classpath:xray/sampling-rules.json";
    private static final Logger logger = LoggerFactory.getLogger(AwsConfig.class);

    static {

        URL ruleFile = null;
        try {
            ruleFile = ResourceUtils.getURL(SAMPLING_RULE_JSON);
        } catch (FileNotFoundException e) {
            logger.error("sampling rule cannot load for aws xray - {}", e.getMessage());
        }
        logger.debug("sampling rule load from {} for aws xray", ruleFile.getPath());

        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard();
        builder.withSamplingStrategy(new CentralizedSamplingStrategy(ruleFile));

        AWSXRay.setGlobalRecorder(builder.build());

    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource(DataSourceProperties properties) {
        AWSXRay.beginSegment("aws_service");
        AWSXRay.endSegment();
        DataSource originalDataSource = properties.initializeDataSourceBuilder().build();
        return new TracingDataSource(originalDataSource);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TracingJdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new TracingJdbcTemplate(dataSource);
    }


    @Bean
    public Filter TracingFilter() {
        log.debug("The segment name for aws xray tracking has been set to {}.", AWSXRAY_SEGMENT_NAME);
        return new AWSXRayServletFilter(
                Optional.ofNullable(AWSXRAY_SEGMENT_NAME)
                        .orElseThrow(() -> new ApplicationNameNullException())
        );
    }
}
