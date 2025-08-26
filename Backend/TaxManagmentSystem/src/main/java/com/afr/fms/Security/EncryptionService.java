package com.afr.fms.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class EncryptionService {

  // AES Encryption with IV
  public String encrypt(String plainText, String secretKey, String initVector) throws Exception {
    IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
    SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);

    byte[] encrypted = cipher.doFinal(plainText.getBytes());
    return Base64.getEncoder().encodeToString(encrypted);
  }

  // AES Decryption with IV
  public String decrypt(String encryptedText, String secretKey, String initVector) throws Exception {
    IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
    SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    cipher.init(Cipher.DECRYPT_MODE, key, iv);

    byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
    return new String(original);
  }
}
