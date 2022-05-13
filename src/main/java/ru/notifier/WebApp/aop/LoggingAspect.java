package ru.notifier.WebApp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
@Aspect
public class LoggingAspect {

    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Pointcut("execution(public * ru.notifier.WebApp.controllers.ClientRestController.*(..))")
    public void callServiceClient() { }

    @Before("callServiceClient()")
    public void beforeCallMethodClient(JoinPoint jp) {
        String args = Arrays.stream(jp.getArgs())
                .map(a -> a.toString())
                .collect(Collectors.joining(","));
        logger.info("Перед вызовом метода " + jp.toString() + ", args=[" + args + "]");
    }

    @After("callServiceClient()")
    public void afterCallMethodClient(JoinPoint jp) {
        logger.info("После вызова метода " + jp.toString());
    }

    @Pointcut("execution(public * ru.notifier.WebApp.controllers.NotificationRestController.*(..))")
    public void callServiceNotification() { }

    @Before("callServiceNotification()")
    public void beforeCallMethodNotification(JoinPoint jp1) {
        String args = Arrays.stream(jp1.getArgs())
                .map(a -> a.toString())
                .collect(Collectors.joining(","));
        logger.info("Перед вызовом метода " + jp1.toString() + ", args=[" + args + "]");
    }

    @After("callServiceNotification()")
    public void afterCallMethodNotification(JoinPoint jp1) {
        logger.info("После вызова метода " + jp1.toString());
    }
}
