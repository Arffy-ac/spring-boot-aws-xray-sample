package com.arffy.aws.xray.repository;

import com.arffy.aws.xray.model.AwsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AwsRepository extends JpaRepository<AwsEntity, Integer>,
        JpaSpecificationExecutor<AwsEntity> {
}
