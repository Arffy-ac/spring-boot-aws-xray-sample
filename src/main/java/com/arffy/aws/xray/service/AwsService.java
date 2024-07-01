package com.arffy.aws.xray.service;

import com.arffy.aws.xray.model.AwsEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AwsService {

    String updateAws(String name, int age);

    ResponseEntity<Object> getAws();

    List<AwsEntity> getAwsAllService();

    List<AwsEntity> getAwsJbcAllService();
}
