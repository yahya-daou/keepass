
package com.example.keepass;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {
    private static final String PREFS_NAME = "MyPrefsFile";

    public static SecretKey generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256); // AES key size
            return keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveKey(Context context, String userEmail, SecretKey key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        byte[] keyBytes = key.getEncoded();
        String encodedKey = Base64.encodeToString(keyBytes, Base64.DEFAULT);
        editor.putString(userEmail, encodedKey);
        editor.apply();
    }

    public static SecretKey loadKey(Context context, String userEmail) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String encodedKey = preferences.getString(userEmail, null);
        if (encodedKey != null) {
            byte[] keyBytes = Base64.decode(encodedKey, Base64.DEFAULT);
            return new SecretKeySpec(keyBytes, "AES");
        }
        return null;
    }

    public static byte[] encrypt(SecretKey key, String value) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(value.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(SecretKey key, String encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(encryptedValue);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
