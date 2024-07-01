package com.arffy.aws.xray.service;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.arffy.aws.xray.model.AwsEntity;
import com.arffy.aws.xray.repository.AwsRepo;
import com.arffy.aws.xray.repository.JdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@XRayEnabled
public class AwsServiceImpl implements AwsService {

    private static final Logger logger = LoggerFactory.getLogger(AwsServiceImpl.class);

    @Autowired
    AwsRepo awsRepo;

    @Autowired
    JdbcRepository jdbcRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String updateAws(String name, int age) {
        logger.info("Updating AWS");
        AwsEntity awsEntity = new AwsEntity();
        awsEntity.setName(name);
        awsEntity.setAge(age);
        return this.awsRepo.updateAws(awsEntity);
    }

    @Override
    public ResponseEntity<Object> getAws() {
        logger.info("Getting Data for AWS");
        return this.awsRepo.getAws();
    }

    @Override
    public List<AwsEntity> getAwsAllService() {
        logger.info("Getting Data for AWS");
        Subsegment subsegment = null;
        try {
            List<AwsEntity> awsEntityList = this.awsRepo.getAwsAllDataFromRepo();
            subsegment = AWSXRay.beginSubsegment("getDataFromRestTemplate");
            String url = "https://sample.api.com/";
            String response = restTemplate.getForObject(url, String.class);
            logger.info("response from rest template {}", response);
            return awsEntityList;
        } catch (Exception e) {
            e.printStackTrace();
            subsegment.addException(e);
            logger.error(e.getMessage());
        } finally {
            AWSXRay.endSubsegment();
        }
        return null;
    }

    @Override
    public List<AwsEntity> getAwsJbcAllService() {
        logger.info("Getting jdbc data AWS");
        try {
            return jdbcRepository.findJbcAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
