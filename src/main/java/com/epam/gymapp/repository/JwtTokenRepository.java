package com.epam.gymapp.repository;

import com.epam.gymapp.model.JwtToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRepository extends CrudRepository<JwtToken, Integer> {

  @Query("""
      select jwt from JwtToken jwt
      where jwt.isRevoked = false and jwt.user.id = :id
      """)
  List<JwtToken> findAllValidTokensByUserId(Long id);

  Optional<JwtToken> findByToken(String token);
}
