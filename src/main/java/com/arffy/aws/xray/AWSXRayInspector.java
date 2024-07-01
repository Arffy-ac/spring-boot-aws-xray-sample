package com.arffy.aws.xray;

import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.spring.aop.AbstractXRayInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class AWSXRayInspector extends AbstractXRayInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AWSXRayInspector.class);

    @Override
    protected Map<String, Map<String, Object>> generateMetadata(ProceedingJoinPoint pjp, Subsegment subsegment) {
        logger.trace("aws xray tracing method - {}.{}", pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName());

        Map<String, Map<String, Object>> metadata = super.generateMetadata(pjp, subsegment);
        metadata.get("ClassInfo").put("Class", pjp.getSignature().getDeclaringTypeName());

        Map<String, Object> argumentsInfo = new HashMap<>();

        Arrays.stream(pjp.getArgs())
                .forEach(arg -> argumentsInfo.put(arg.getClass().getSimpleName(), arg));

        metadata.put("Arguments", argumentsInfo);
        metadata.get("ClassInfo").put("Package", pjp.getSignature().getDeclaringType().getPackage().getName());
        metadata.get("ClassInfo").put("Method", pjp.getSignature().getName());

        return metadata;
    }

    @Override
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) && execution(* *(..))")
    public void xrayEnabledClasses() {
    }

}