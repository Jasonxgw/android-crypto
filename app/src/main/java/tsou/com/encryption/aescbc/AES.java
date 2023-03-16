package tsou.com.encryption.aescbc;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import tsou.com.encryption.jnimode.AESecb;

/**
 * http://blog.csdn.net/qq_33237207/article/details/53114122
 */
public class AES {
    private final String CIPHERMODEPADDING = "AES/CBC/PKCS5Padding";// AES/CBC/PKCS7Padding

    private SecretKeySpec skforAES = null;
    private static String ivParameter = "1234huangxiaoguo";// 密钥默认偏移，可更改

    private byte[] iv = ivParameter.getBytes();
    private IvParameterSpec IV;


    private static AES instance = null;
    private static String sKey;
    //    String sKey = "huangxiaoguo1234";// key必须为16位，可更改为自己的key

    public static AES getInstance() {
        if (instance == null) {
            synchronized (AES.class) {
                if (instance == null) {
                    sKey = new AESecb().getKey();
                    instance = new AES();
                }
            }
        }
        return instance;
    }

    public AES() {
        byte[] skAsByteArray;
        try {
            skAsByteArray = sKey.getBytes("ASCII");
            skforAES = new SecretKeySpec(skAsByteArray, "AES");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        IV = new IvParameterSpec(iv);
    }

    public String encrypt(byte[] plaintext) {
        byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV, plaintext);
        String base64_ciphertext = Base64Encoder.encode(ciphertext);
        return base64_ciphertext;
    }

    public String decrypt(String ciphertext_base64) {
        byte[] s = Base64Decoder.decodeToBytes(ciphertext_base64);
        String decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES, IV,
                s));
        return decrypted;
    }

    private byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                           byte[] msg) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.ENCRYPT_MODE, sk, IV);
            return c.doFinal(msg);
        } catch (Exception nsae) {
        }
        return null;
    }

    private byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                           byte[] ciphertext) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.DECRYPT_MODE, sk, IV);
            return c.doFinal(ciphertext);
        } catch (Exception nsae) {
        }
        return null;
    }
}