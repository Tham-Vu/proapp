package com.example.user_management.utils;

import org.apache.tomcat.util.bcel.classfile.Constant;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {
    public static String encrypt(String data, String secret ){
        try{
            Cipher cipher = Cipher.getInstance(Consts.AES_INSTANCE);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret.getBytes(Consts.SYSTEM_CHARSET), Consts.AES));
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(Consts.SYSTEM_CHARSET)));
        }catch (Exception e){
            System.out.println("Error while encrypting: " + e.getMessage());
        }
        return null;
    }
    public static String decrypt(String encryptData, String secret){
        try{
            Cipher cipher = Cipher.getInstance(Consts.AES_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret.getBytes(Consts.SYSTEM_CHARSET), Consts.AES));
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptData)));
        }catch (Exception e){
            System.out.println("Error while decrypting: " + e.getMessage());
        }
        return null;
    }
}
