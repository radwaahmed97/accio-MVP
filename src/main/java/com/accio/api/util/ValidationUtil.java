package com.accio.api.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public final class ValidationUtil {
  private static final Pattern EMAIL_PATTERN = Pattern
          .compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+){0,5}@[A-Za-z0-9][A-Za-z0-9-]{0,100}" +
                  "(\\.[A-Za-z0-9-]+){0,5}(\\.[A-Za-z]{2,5})$");

  private ValidationUtil() {
  }

  public static boolean validateDomain(String url) {
    try {
      new URI(url);
      return true;
    } catch (URISyntaxException e) {
      return false;
    }
  }

  public static boolean isValidEmail(String email) {
    return EMAIL_PATTERN.matcher(email).matches();
  }

  public static boolean validateIP(String ip) {
    String ipRegex = "^(\\d{1,3}.){3}\\d{1,3}$";
    return Pattern.matches(ipRegex, ip);
  }

  public static boolean containsSpecialCharacter(String value) {
    String specialCharacters = "[!#$%^&*(),?\"{}|<>]";
    return value.matches(".*" + specialCharacters + ".*");
  }
}
