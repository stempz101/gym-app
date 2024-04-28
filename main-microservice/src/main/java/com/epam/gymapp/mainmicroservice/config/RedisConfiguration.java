package com.epam.gymapp.mainmicroservice.config;

import com.epam.gymapp.mainmicroservice.model.SessionUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

  @Bean
  public RedisTemplate<String, SessionUser> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, SessionUser> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, SessionUser.class));
    return template;
  }
}
