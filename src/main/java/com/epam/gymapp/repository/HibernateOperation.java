package com.epam.gymapp.repository;

import org.hibernate.Session;

@FunctionalInterface
public interface HibernateOperation<T> {

  T execute(Session session);
}
