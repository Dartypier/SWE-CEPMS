package it.unifi.swe.cepms.server_module.helpers;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashedPassword {

    public static String createHash(String password){
        String hashedPassword;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-512");
            byte[] messageDigest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            hashedPassword = convertToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return hashedPassword;
    }

    private static String convertToHex(final byte[] messageDigest) {
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);
        while (hexText.length() < 32) {
            hexText = "0".concat(hexText);
        }
        return hexText;
    }
}