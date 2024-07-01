package com.arffy.aws.xray.repository;


import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Entity;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.arffy.aws.xray.model.AwsEntity;
import com.arffy.aws.xray.repository.AwsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@XRayEnabled
public class AwsRepoImpl implements AwsRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    AwsRepository awsRepository;

    @Autowired
    JdbcRepository jdbcRepository;

    @Override
    @Transactional
    public String updateAws(AwsEntity awsEntity) {
        Subsegment subsegment = AWSXRay.beginSubsegment("UpdateAws");
        try {
            if (awsEntity != null) {
                entityManager.persist(awsEntity);
            }
            return "Successfully Updated";
        } catch (Exception e) {
            subsegment.addException(e);
            throw e;
        } finally {
            AWSXRay.endSubsegment();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> getAws() {
        Entity parentSegment = AWSXRay.beginSegment("getAwsMessage");
        AWSXRay.getGlobalRecorder().setTraceEntity(parentSegment);
        Subsegment subsegment = AWSXRay.beginSubsegment("GetAws");
        try {
            List<AwsEntity> awsEntity = null;
            awsEntity = awsRepository.findAll();
            return ResponseEntity.ok(awsEntity);
        } catch (Exception e) {
            parentSegment.addException(e);
            subsegment.addException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        } finally {
            AWSXRay.endSubsegment();
            AWSXRay.endSegment();
        }
    }

    @Override
    public List<AwsEntity> getAwsAllDataFromRepo() {
        Subsegment subsegment = AWSXRay.beginSubsegment("getAwsAllDataFromRepo");
        try {
            List<AwsEntity> awsRepoList = jdbcRepository.findJbcAll();
            return awsRepoList;
        } catch (Exception e) {
            subsegment.addException(e);
        } finally {
            AWSXRay.endSubsegment();
        }
        return null;
    }

}
