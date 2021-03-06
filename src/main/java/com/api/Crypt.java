package com.api;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Crypt {

    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'b', 'c', 'D', 'e', 'F'};
    private static Cipher cipher;
    protected static final String password = "password";

    public static void init() {

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
        }

    }

    public static String encrypt(String password, String message) {

        try {

            //String salt = random(16);
            String salt = "506c709e6c2ee32b02b56f6419bb505a";
            String iv = random(16);
            SecretKey key = generateKey(salt, password);
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, message.getBytes("UTF-8"));
            String code = Base64.getEncoder().encodeToString(encrypted);
            return salt + code.substring(0, code.length() - 2) + iv;

        } catch (UnsupportedEncodingException e) {
            return null;
        }

    }

    public static String decrypt(String password, String message) {

        System.out.println(message);

        if(message == "" || message.isEmpty() || message.equals(null)){
            System.out.println("message is empty");
            return "";
        }
        try {

            //String salt = message.substring(0, 32);
            String salt = "506c709e6c2ee32b02b56f6419bb505a";
            String iv = message.substring(message.length() - 32, message.length());
            String base = message = message.substring(32, message.length() - 32) + "==";
            SecretKey key = generateKey(salt, password);
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, Base64.getDecoder().decode(base));
            return new String(decrypted, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            return null;
        }

    }

    private static byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {

        try {

            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            return cipher.doFinal(bytes);

        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            return null;
        }

    }

    private static SecretKey generateKey(String salt, String passphrase) {

        try {

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), 1000, 128);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            //System.out.println("klucz to: " + key);
            return key;

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }

    }

    private static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return hex(salt);
    }

    private static String hex(byte[] data) {

        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;

        for (int var5 = 0; i < l; ++i) {
            out[var5++] = HEX[(240 & data[i]) >>> 4];
            out[var5++] = HEX[15 & data[i]];
        }

        return new String(out);

    }

    private static byte[] hex(String hex) {

        char[] data = hex.toCharArray();
        int len = data.length;

        if ((len & 1) != 0) {
            return null;
        } else {

            byte[] out = new byte[len >> 1];
            int i = 0;

            for (int j = 0; j < len; ++i) {

                int f = Character.digit(data[j], 16) << 4;
                ++j;
                f |= Character.digit(data[j], 16);
                ++j;
                out[i] = (byte) (f & 255);

            }

            return out;

        }

    }



}
