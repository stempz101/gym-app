package com.epam.gymapp.logging;

@FunctionalInterface
public interface TransactionLog<T> {

  T executeTransaction();
}
