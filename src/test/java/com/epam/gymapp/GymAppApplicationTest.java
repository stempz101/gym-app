package com.epam.gymapp;

import com.epam.gymapp.config.GymApplicationConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GymApplicationConfiguration.class})
public class GymAppApplicationTest {

  @Test
  void contextLoads() {
  }
}
