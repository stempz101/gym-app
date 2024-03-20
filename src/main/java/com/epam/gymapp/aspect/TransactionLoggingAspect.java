package com.epam.gymapp.aspect;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.ResultSet;
import java.sql.Statement;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionLoggingAspect {

  @PersistenceContext
  private EntityManager entityManager;

  @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
  public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      entityManager.unwrap(Session.class).doWork(connection -> {
        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT txid_current()")) {
          if (resultSet.next()) {
            MDC.put("tx.id", String.format("Persistence Context Id: [%d], DB Transaction Id: [%s]",
                TSID.Factory.getTsid().toLong(), resultSet.getString(1)));
          }
        }
      });

      return joinPoint.proceed();
    } finally {
      MDC.remove("tx.id");
    }
  }
}
