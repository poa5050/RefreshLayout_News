package com.demo.flow.net;

import android.util.Base64;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class WLSignUtils {

	private static final String KEY_ALGORTHM = "RSA";
	private static final String ALGORTHM = "RSA/ECB/PKCS1Padding";

    private static final String RSA_PRIVATE =
                    "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCvAETIvqLVdHCa" +
                            "S+IA/oDP2TEAR0KIS8FTis1gA66HbP0IOdGXQKkTd/Mg9yXPbZNzYFKEmsgSnHUX" +
                            "jvHsz4pxlf7en1hpqH25zYZ2Q0HKb0LDx5r+wpG5Lx/5dQHBZMH+Lyb2YWBGVxGK" +
                            "gdrUUg0SLwAAIW5vTRTBZ57Srb3FmhC6D04pr5SxpuXzxTxj8uYh5br30h7nQ/PO" +
                            "zxg3tFpo/yPAC0+A4r9E0PW2N7JLHJCpecchye1xFfkyFL2nWQ7EsQqvT4X5RXMf" +
                            "4PZPGQMubnrbDlSnnuyn1m0dX5idrk5tToBZJ/A/vmht7VVPGjeLtw/VpNDjpDRJ" +
                            "vE1gg79HAgMBAAECggEAeYQDPy14niV7ZviYFz6hrTHQH7Rbk46e0GbKUwhbcTgR" +
                            "v1t+mLtSa+1XiqVywMUCpRPWWLOth5bSi6g7fsdiB5sy3dzhpIRVwOXgn0/2aB23" +
                            "y4BXtNzgGSmK10oTDPns5pZRRLW7BzzANmAS1mmfH+g45N5R88EfT2inVP1Fj+ZU" +
                            "EpsI59sMCm6w50oBE5YiPY0+eeL/NRScOmqF1dvHRYJj3zQdtQDWMSsZIS8XZhf+" +
                            "G35dNQhvaMYTA5xzhh42tJ8pMQtrRk6eeGvSBPw4grE6JaeH3oIi4fnXV5X2MJXy" +
                            "UYHsxsIEcXtMDJ4cDbTuGmmAu8HR27dnZlEntbK6EQKBgQDT/+TLybRQ0DM47rj+" +
                            "HpcQKBliYcujT0KmX6eoih5pAvgwn+THz773/tUiYyWeQDPtJ+mFSK/dzh242Nmj" +
                            "6AF/r0E1Hrm/qDhDwum2U9fcVsSe28O1vZz9eOOvIW7uc3h9dS0+taGWnw686D2Y" +
                            "iCXE0NxsvaBMM0N5g7RELpmXaQKBgQDTUosog4BQi+80Wkk6MdqaHdOueb5EgiPH" +
                            "jAUaM7MzU+/6hoG246jNv5DwbHURLC0BHhFv/HodOJVBPqoi1zZoprSBgGzXeGcM" +
                            "UlZs6GnPY4bAYGxe8Mq5LvpXJE6Zx8NX5m+yZ2OoDkLl16NS4jXm6s7fkntajQgD" +
                            "wT2vrVX7LwKBgA7i9zQAM/NAy3gB+2eMylrOFgo8h8ot9KYuLV2+ZShGlxsC/1Ow" +
                            "mN5dPKYvhCK6q2rJ1LIRSpgusxOZ1p6V2hUXP2L1h2GX9Gt9sl+lP5EvXpsxH2sR" +
                            "p4Z9CK/xjxKbdSiKI+U1Z+Vlwekt/bVKSiyp5fCU2D7BksljoDLxesORAoGANLrX" +
                            "EG/nt7GDKZwRw5ynAGfK5zFnoa6bgPJWX5hg8cDTE782ZM1f1J8lamiVuYg9XpcD" +
                            "IN9uFRaGX7ZkO7dZ0zktt1a1UfqwM+njuN8xb9dUy3ID+Ji67QRk4ROlLUgkXS2Z" +
                            "13lm64tLdBCfp1Hcw1b58d2aPBkO0+kHHGvzFW8CgYBvD6cOOKhRtyHDAszKwiaL" +
                            "ZLVPsQhQyKsB+zUsZJ5UQ4WYUrwvYE6Dq/UMBQxO4YTAYbODx5BMaflK2SohO1Z0" +
                            "JLVEadnJgzyaIZABgs4cDtujYLFVyS6pnlfKt4OEvHqsNLcf0aFF9e8mgDu8xxgp" +
                            "sCRHDAsFmLoKhQxKuh2KsA==";

    private static final String RSA_PUBLIC =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArwBEyL6i1XRwmkviAP6A" +
                    "z9kxAEdCiEvBU4rNYAOuh2z9CDnRl0CpE3fzIPclz22Tc2BShJrIEpx1F47x7M+K" +
                    "cZX+3p9Yaah9uc2GdkNBym9Cw8ea/sKRuS8f+XUBwWTB/i8m9mFgRlcRioHa1FIN" +
                    "Ei8AACFub00UwWee0q29xZoQug9OKa+Usabl88U8Y/LmIeW699Ie50Pzzs8YN7Ra" +
                    "aP8jwAtPgOK/RND1tjeySxyQqXnHIcntcRX5MhS9p1kOxLEKr0+F+UVzH+D2TxkD" +
                    "Lm562w5Up57sp9ZtHV+Yna5ObU6AWSfwP75obe1VTxo3i7cP1aTQ46Q0SbxNYIO/" +
                    "RwIDAQAB";

    private static Key privateKey = getPrivateKey();
    private static Key publicKey = getPublicKey();

    private static Key getPrivateKey() {
        Key key = null;
        try {
            // 解密密钥
            byte[] keyBytes = Base64.decode(RSA_PRIVATE.getBytes(), Base64.DEFAULT);
            // 取私钥
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
            key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return key;
    }

    private static Key getPublicKey() {
        Key key = null;

        try {
            byte[] keyBytes = Base64.decode(RSA_PUBLIC.getBytes(), Base64.DEFAULT);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
            key = keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return key;
    }

    private static Cipher getEncryptCipher() {
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance(ALGORTHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cipher;
    }

    private static Cipher getDecryptCipher() {
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance(ALGORTHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cipher;
    }

    /**
     * 加密过程：原始数据 → RSA → Base64 → 加密数据
     * @param content 原始数据
     * @return 加密数据
     */
    public static String sign(String content) {
        try {
            byte[] signBytes = getEncryptCipher().doFinal(content.getBytes());

            // Base64编码成字符串，并删除所有"\n"
            return Base64.encodeToString(signBytes, Base64.DEFAULT).replace("\n", "");
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 解密过程：解密数据 ← RSA ← Base64 ← 加密数据
     * @param content 加密数据
     * @return 解密数据
     */
    public static String unSign(String content) {
        try {
            // 先Base64解码一次
            byte[] deBytes = Base64.decode(content.getBytes(), Base64.DEFAULT);
            // 解密
            byte[] unsignBytes = getDecryptCipher().doFinal(deBytes);

            return new String(unsignBytes);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
