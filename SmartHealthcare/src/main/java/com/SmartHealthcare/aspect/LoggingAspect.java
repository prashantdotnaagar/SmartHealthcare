package com.SmartHealthcare.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Logging Aspect for capturing method execution logs
 * Place this file in: src/main/java/com/yourcompany/yourproject/aspect/
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Pointcut for all controller methods
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)" +
            " || within(@org.springframework.stereotype.Controller *)")
    public void controllerMethods() {}

    /**
     * Pointcut for all service methods
     */
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceMethods() {}

    /**
     * Pointcut for all repository methods
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void repositoryMethods() {}

    /**
     * Log before any controller method execution
     */
    @Before("controllerMethods()")
    public void logBeforeController(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            logger.info("=== Incoming Request ===");
            logger.info("URL: {}", request.getRequestURL().toString());
            logger.info("HTTP Method: {}", request.getMethod());
            logger.info("IP Address: {}", request.getRemoteAddr());
            logger.info("Class Method: {}.{}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
            logger.info("Arguments: {}", Arrays.toString(joinPoint.getArgs()));
        }
    }

    /**
     * Log after successful controller method execution
     */
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterController(JoinPoint joinPoint, Object result) {
        logger.info("=== Response ===");
        logger.info("Method: {}.{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        logger.info("Response: {}", result);
    }

    /**
     * Log around service methods with execution time
     */
    @Around("serviceMethods()")
    public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        logger.info("Executing Service: {}.{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        logger.debug("Service Arguments: {}", Arrays.toString(joinPoint.getArgs()));

        Object result;
        try {
            result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("Service Execution Completed: {}.{} in {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    executionTime);
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("Service Execution Failed: {}.{} after {} ms with error: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    executionTime,
                    e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Log all exceptions thrown from any method
     */
    @AfterThrowing(pointcut = "controllerMethods() || serviceMethods() || repositoryMethods()",
            throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("=== Exception in {}.{} ===",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        logger.error("Exception Type: {}", exception.getClass().getName());
        logger.error("Exception Message: {}", exception.getMessage());
        logger.error("Stack Trace: ", exception);
    }

    /**
     * Log repository operations
     */
    @Around("repositoryMethods()")
    public Object logAroundRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        logger.debug("Executing Repository: {}.{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());

        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        logger.debug("Repository Execution Completed: {}.{} in {} ms",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                executionTime);

        return result;
    }
}