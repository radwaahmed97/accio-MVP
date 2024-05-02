package com.accio.api.util;

public class CaesarCipher {

  private CaesarCipher() {
  }

  public static String cipher(String message, int offset) {
    if (offset > 25) throw new IllegalArgumentException();
    while (offset < 0) {
      offset += 26;
    }
    var builder = new StringBuilder();
    for (var ch : message.toCharArray()) {
      var newCh = shift(ch, 'a', offset);
      if (newCh == ch) {
        newCh = shift(ch, 'A', offset);
      }
      builder.append(newCh);
    }
    return builder.toString();
  }

  private static char shift(char ch, char a, int offset) {
    var originalAlphabetPosition = ch - a;
    if (originalAlphabetPosition < 0 || originalAlphabetPosition > 25) {
      return ch;
    }
    var newAlphabetPosition = (originalAlphabetPosition + offset) % 26;

    return (char) (a + newAlphabetPosition);
  }
}
