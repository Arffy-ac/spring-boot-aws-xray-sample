package com.arffy.aws.xray;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@XRayEnabled
public class XrayApplication {

    public static void main(String[] args) {
        SpringApplication.run(XrayApplication.class, args);
    }
}
