package com.arffy.aws.xray.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.arffy.aws.xray.model.AwsEntity;
import com.arffy.aws.xray.service.AwsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@XRayEnabled
@RestController
public class AwsController {

    @Autowired
    AwsService awsService;

    private final Logger logger = LoggerFactory.getLogger(AwsController.class);


    @PostMapping("/updateAws")
    public String updateAws() {
        return this.awsService.updateAws("aws", 20);
    }

    @GetMapping("/getAws")
    public ResponseEntity<Object> getAws() {
        return this.awsService.getAws();
    }

    @GetMapping("/getAwsAll")
    public List<AwsEntity> getAwsAllController() {
        try {
            List<AwsEntity> awsEntityList = this.awsService.getAwsAllService();
            return awsEntityList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/getAwsJdbc")
    public List<AwsEntity> getAwsJbcAllController() {
        try {
            List<AwsEntity> awsEntityList = this.awsService.getAwsJbcAllService();
            return awsEntityList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}