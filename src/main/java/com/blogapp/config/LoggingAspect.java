package com.blogapp.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Pointcut that matches all controllers and services.
     */
    @Pointcut("within(com.blogapp..controller..*) || within(com.blogapp..service..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Advice that logs when a method is entered and exited.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        // Avoid logging very noisy or sensitive methods explicitly
        if (methodName.toLowerCase().contains("password") || methodName.toLowerCase().contains("otp")) {
            log.debug("Enter: {}.{}() with sensitive arguments", className, methodName);
        } else {
            log.debug("Enter: {}.{}()", className, methodName);
        }

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            
            // Only log method exits at DEBUG level to avoid spamming the console
            log.debug("Exit: {}.{}() executed in {} ms", className, methodName, elapsedTime);
            
            return result;
        } catch (IllegalArgumentException e) {
            log.warn("Illegal argument: {} in {}.{}()", e.getMessage(), className, methodName);
            throw e;
        } catch (com.blogapp.common.exception.BadRequestException |
                 com.blogapp.common.exception.ResourceNotFoundException |
                 com.blogapp.common.exception.RateLimitException |
                 com.blogapp.common.exception.OtpVerificationException e) {
            log.warn("Business exception in {}.{}() with message = '{}'", className, methodName, e.getMessage());
            throw e;
        } catch (Throwable e) {
            log.error("Exception in {}.{}() with cause = '{}' and exception = '{}'", className, methodName,
                    e.getCause() != null ? e.getCause() : "NULL", e.getMessage());
            throw e;
        }
    }
}
