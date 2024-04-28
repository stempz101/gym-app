package com.epam.gymapp.reportsmicroservice.context;

public class TransactionContextHolder {

  private static final ThreadLocal<Boolean> IS_TX_ID_SET_IN_CONTEXT = new ThreadLocal<>();

  public static boolean isTransactionIdSetInContext() {
    return IS_TX_ID_SET_IN_CONTEXT.get() != null && IS_TX_ID_SET_IN_CONTEXT.get();
  }

  public static void setTransactionIdInContext(Boolean isSet) {
    IS_TX_ID_SET_IN_CONTEXT.set(isSet);
  }

  public static void clearTransactionIdFromContext() {
    IS_TX_ID_SET_IN_CONTEXT.remove();
  }
}
