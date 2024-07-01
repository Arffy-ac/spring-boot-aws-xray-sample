package com.arffy.aws.xray.repository;

import com.arffy.aws.xray.model.AwsEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AwsRepo {

    String updateAws(AwsEntity awsEntity);

    ResponseEntity<Object> getAws();

    List<AwsEntity> getAwsAllDataFromRepo();
}
