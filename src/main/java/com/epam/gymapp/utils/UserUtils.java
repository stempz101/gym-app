package com.epam.gymapp.utils;

import com.epam.gymapp.model.User;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserUtils {

  private static final Logger log = LoggerFactory.getLogger(UserUtils.class);

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final int PASSWORD_LENGTH = 10;

  public static int getAppearanceFromFoundUsername(String username) {
    log.debug("Getting appearance from found username: {}", username);

    String numPart = username.replaceAll("\\D", "");
    return numPart.isEmpty() ? 1 : Integer.parseInt(numPart) + 1;
  }

  public static String buildUsername(User user, long numOfAppearance) {
    log.debug("Building username for the user: {}", user);

    return numOfAppearance == 0 ? String.format("%s.%s", user.getFirstName(), user.getLastName()) :
        String.format("%s.%s%d", user.getFirstName(), user.getLastName(), numOfAppearance);
  }

  public static char[] generatePassword() {
    log.debug("Generating password");

    Random random = new Random();
    StringBuilder builder = new StringBuilder(PASSWORD_LENGTH);
    for (int i = 0; i < PASSWORD_LENGTH; i++) {
      int randomIndex = random.nextInt(CHARACTERS.length());
      char randomChar = CHARACTERS.charAt(randomIndex);
      builder.append(randomChar);
    }

    return builder.toString().toCharArray();
  }
}
