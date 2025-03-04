package com.musa3team.devout.common.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private final HttpServletRequest request;

    @Around("execution(* com.musa3team.devout.domain.order.service..*(..))")
    public Object logCreateOrder(ProceedingJoinPoint joinPoint)throws Throwable{
        //요청 시각, 가게 id, 주문 id
        long startTime = System.currentTimeMillis();
        String url = request.getRequestURI();
        Object result = joinPoint.proceed();

        String storeId = MDC.get("storeId").toString();
        String orderId = MDC.get("orderId").toString();
        String status = MDC.get("status").toString();

        log.info("AOP - ORDER API Response: storeId={}, orderId={}, status={}, currentTime={}, URL={}",
                storeId, orderId, status, startTime, url);

        return result;
    }
}
