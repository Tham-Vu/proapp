package com.example.user_management.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class RSAUtil {
    private static String getKey(String filename) throws IOException{
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
    public static RSAPrivateKey getPrivateKeyFromString(String privateKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyPEM = privateKeyStr;
        //Remove the first and last lines
        privateKeyPEM = privateKeyPEM.replace(System.lineSeparator(), "");
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");

        privateKeyPEM = privateKeyPEM.replace("\r\n","");
        privateKeyPEM = privateKeyPEM.replace("\n","");
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
        KeyFactory kf = KeyFactory.getInstance(Consts.RSA_INSTANCE);
        RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(encoded));
        return privateKey;
    }
    public static String sign(PrivateKey privateKey, String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance(Consts.RSA_INSTANCE);
        sign.initSign(privateKey);
        sign.update(DigestUtils.md5(message));
        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(sign.sign()), Consts.SYSTEM_CHARSET);
    }
}
