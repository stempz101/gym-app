package com.epam.gymapp.dao;

import java.util.List;
import java.util.Optional;

public interface GeneralDAO<T> {

  T save(T entity);

  List<T> findAll();

  Optional<T> findById(Long id);
}
