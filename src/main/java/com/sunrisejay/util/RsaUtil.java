package com.sunrisejay.util;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class RsaUtil {

    // 公钥字符串
    private static final String PUB_KEY_STR = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoZG+2JfvUXe2P19IJfjH+iLmp\n" +
            "VSBX7ErSKnN2rx40EekJ4HEmQpa+vZ76PkHa+5b8L5eTHmT4gFVSukaqwoDjVAVR\n" +
            "TufRBzy0ghfFUMfOZ8WluH42luJlEtbv9/dMqixikUrd3H7llf79QIb3gRhIIZT8\n" +
            "TcpN6LUbX8noVcBKuwIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    // 私有构造器，防止类被实例化
    private RsaUtil() {
        throw new AssertionError("Utility class cannot be instantiated.");
    }

    /**
     * 使用RSA公钥加密ID
     *
     * @param id 用户ID
     * @return 加密后的Base64字符串
     */
    public static String encryptID(String id) {
        try {
            // 解析公钥
            byte[] pubKeyBytes = Base64.getDecoder().decode(PUB_KEY_STR.replaceAll("(-{5}BEGIN PUBLIC KEY-{5})|(-{5}END PUBLIC KEY-{5})|\\s", ""));
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKeyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(keySpec);

            // 初始化Cipher对象
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // 加密ID
            byte[] encryptedBytes = cipher.doFinal(id.getBytes(StandardCharsets.UTF_8));

            // 转换为Base64编码
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
}
