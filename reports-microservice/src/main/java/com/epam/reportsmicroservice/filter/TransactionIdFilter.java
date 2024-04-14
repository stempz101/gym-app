package com.epam.reportsmicroservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TransactionIdFilter extends OncePerRequestFilter {

  @Value("${application.logging.transaction-id.key}")
  private String transactionIdKey;

  @Value("${application.logging.transaction-id.header}")
  private String transactionIdHeader;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    Optional<String> transactionId = Optional.ofNullable(request.getHeader(transactionIdHeader));
    transactionId.ifPresent(txId -> MDC.put(transactionIdKey, txId));

    filterChain.doFilter(request, response);

    transactionId.ifPresent(txId -> MDC.remove(transactionIdKey));
  }
}
