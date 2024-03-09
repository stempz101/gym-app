package com.epam.gymapp.logging;

import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class LoggerHelper {

  public <R> R transactionalLogging(TransactionLog<R> transactionLog) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transaction.id", transactionId);

    R result = transactionLog.executeTransaction();

    MDC.clear();
    return result;
  }
}
