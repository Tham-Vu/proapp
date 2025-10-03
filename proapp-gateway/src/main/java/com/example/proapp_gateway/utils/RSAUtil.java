package com.example.proapp_gateway.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class RSAUtil {
    private static String getKey(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
    public static RSAPublicKey getPublicKeyFromString(String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyPEM = publicKeyStr;
        //Remove the first and last lines
        publicKeyPEM = publicKeyPEM.replace(System.lineSeparator(), "");
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");

        publicKeyPEM = publicKeyPEM.replace("\r\n","");
        publicKeyPEM = publicKeyPEM.replace("\n","");
        byte[] encoded = Base64.decodeBase64(publicKeyPEM);
        KeyFactory kf = KeyFactory.getInstance(Constants.RSA_INSTANCE);
        RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(new PKCS8EncodedKeySpec(encoded));
        return publicKey;
    }
    public static boolean verify (RSAPublicKey publicKey, String message, String signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance(Constants.RSA_INSTANCE);
        sign.initVerify(publicKey);
        sign.update(DigestUtils.md5(message));
        return sign.verify(Base64.decodeBase64(signature.getBytes(Constants.SYSTEM_CHARSET)));
    }
}
