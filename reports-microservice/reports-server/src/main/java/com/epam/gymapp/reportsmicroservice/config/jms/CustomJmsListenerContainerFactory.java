package com.epam.gymapp.reportsmicroservice.config.jms;

import com.epam.gymapp.reportsmicroservice.context.TransactionContextHolder;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import java.util.Optional;
import lombok.Setter;
import org.slf4j.MDC;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

@Setter
public class CustomJmsListenerContainerFactory extends DefaultJmsListenerContainerFactory {

  private String transactionIdHeader;
  private String transactionIdKey;

  @Override
  protected DefaultMessageListenerContainer createContainerInstance() {
    return new DefaultMessageListenerContainer() {
      @Override
      protected void doExecuteListener(Session session, Message message) throws JMSException {
        Optional<String> transactionId = Optional.ofNullable(message.getStringProperty(transactionIdHeader));
        transactionId.ifPresent(txId -> {
          MDC.put(transactionIdKey, txId);
          TransactionContextHolder.setTransactionIdInContext(true);
        });

        super.doExecuteListener(session, message);

        transactionId.ifPresent(txId -> {
          MDC.remove(transactionIdKey);
          TransactionContextHolder.clearTransactionIdFromContext();
        });
      }
    };
  }
}
