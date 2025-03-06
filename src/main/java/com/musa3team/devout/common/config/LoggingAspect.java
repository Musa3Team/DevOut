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

        Object storeIdObj = MDC.get("storeId");
        String storeId = storeIdObj != null ? storeIdObj.toString() : "N/A";

        Object orderIdObj = MDC.get("orderId");
        String orderId = orderIdObj != null ? orderIdObj.toString() : "N/A";

        Object statusObj = MDC.get("status");
        String status = statusObj != null ? statusObj.toString() : "N/A";

        log.info("AOP - ORDER API Response: storeId={}, orderId={}, status={}, currentTime={}, URL={}",
                storeId, orderId, status, startTime, url);

        return result;
    }
}
