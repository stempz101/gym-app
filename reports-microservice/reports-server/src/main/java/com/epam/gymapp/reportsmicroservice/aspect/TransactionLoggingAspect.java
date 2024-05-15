package com.epam.gymapp.reportsmicroservice.aspect;

import com.epam.gymapp.reportsmicroservice.context.TransactionContextHolder;
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
    boolean isTransactionIdSetInContext = TransactionContextHolder.isTransactionIdSetInContext();
    try {
      if (!isTransactionIdSetInContext) {
        MDC.put(transactionIdKey, String.format(transactionIdValue, TSID.Factory.getTsid().toLong()));
      }

      return joinPoint.proceed();
    } finally {
      if (!isTransactionIdSetInContext) {
        MDC.remove(transactionIdKey);
      }
    }
  }
}
