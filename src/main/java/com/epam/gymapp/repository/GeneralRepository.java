package com.epam.gymapp.repository;

import java.util.List;
import java.util.Optional;

public interface GeneralRepository<K, T> {

  List<T> findAll();

  Optional<T> findById(K id);

  T save(T entity);

  T update(T entity);

  void delete(T entity);
}
