package com.accio.api.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Cryptor {
  private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
  private static final int IV_SIZE = 16;

  private final byte[] key;

  public Cryptor(String key) {
    try {
      this.key = MessageDigest.getInstance("SHA-256")
          .digest(key.getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException();
    }
  }

  public String encrypt(String data) throws Exception {
    Cipher cipher = Cipher.getInstance(ALGORITHM);

    byte[] iv = new byte[IV_SIZE];
    new SecureRandom().nextBytes(iv);
    var ivSpec = new IvParameterSpec(iv);

    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), ivSpec);

    var cipherText = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    var ivAndCipherText = new byte[IV_SIZE + cipherText.length];

    System.arraycopy(iv, 0, ivAndCipherText, 0, IV_SIZE);
    System.arraycopy(cipherText, 0, ivAndCipherText, IV_SIZE, cipherText.length);

    return Base64.getEncoder().encodeToString(ivAndCipherText);
  }

  public String decrypt(String data) {
    try {
      var cipher = Cipher.getInstance(ALGORITHM);

      var ivAndCipherText = Base64.getDecoder().decode(data);

      var iv = new byte[IV_SIZE];
      System.arraycopy(ivAndCipherText, 0, iv, 0, IV_SIZE);
      var ivSpec = new IvParameterSpec(iv);

      var cipherText = new byte[ivAndCipherText.length - IV_SIZE];
      System.arraycopy(ivAndCipherText, IV_SIZE, cipherText, 0, cipherText.length);

      cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), ivSpec);

      return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
    } catch (Exception e) {
      return null;
    }
  }
}
