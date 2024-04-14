package com.epam.reportsmicroservice.aspect;

import io.hypersistence.tsid.TSID;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionLoggingAspect {

  @Value("${application.logging.transaction-id.key}")
  private String transactionIdKey;

  @Value("${application.logging.transaction-id.value}")
  private String transactionIdValue;

  @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
  public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      if (MDC.get(transactionIdKey) == null || MDC.get(transactionIdKey).isBlank()) {
        MDC.put(transactionIdKey, String.format(transactionIdValue, TSID.Factory.getTsid().toLong()));
      }

      return joinPoint.proceed();
    } finally {
      MDC.remove(transactionIdKey);
    }
  }
}
