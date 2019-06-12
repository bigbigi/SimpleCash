package com.big.simplecash.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author weiwei
 * @date 2014-9-3 上午10:43:52
 * @description
 */
public class Base64 {

    /**
     * 密钥
     */
    private final static String KEY = "Qxd3efg56F9abvWwBlmEL1GHnopqrSTUVYtyz0AC7DIcuJKhijk2M8sNOPRXZ4/+";

    /**
     * 後綴
     */
    private final static String SUFFIX = "_zjjtv";

    /**
     * 加密
     *
     * @param source
     * @return
     */
    public static String encode(String source) {
        char[] sourceBytes = getPaddedBytes(source);
        int numGroups = (sourceBytes.length + 2) / 3;
        char[] targetBytes = new char[4];
        char[] target = new char[4 * numGroups];

        for (int group = 0; group < numGroups; group++) {
            convert3To4(sourceBytes, group * 3, targetBytes);
            for (int i = 0; i < targetBytes.length; i++) {
                target[i + 4 * group] = KEY.charAt(targetBytes[i]);
            }
        }

        int numPadBytes = sourceBytes.length - source.length();

        for (int i = target.length - numPadBytes; i < target.length; i++)
            target[i] = '=';

        return new String(target);
    }

    private static char[] getPaddedBytes(String source) {
        char[] converted = source.toCharArray();
        int requiredLength = 3 * ((converted.length + 2) / 3);
        char[] result = new char[requiredLength];
        System.arraycopy(converted, 0, result, 0, converted.length);
        return result;
    }

    private static void convert3To4(char[] source, int sourceIndex, char[] target) {
        if (target.length >= 4) {
            target[0] = (char) (source[sourceIndex] >>> 2);
            target[1] = (char) (((source[sourceIndex] & 0x03) << 4) | (source[sourceIndex + 1] >>> 4));
            target[2] = (char) (((source[sourceIndex + 1] & 0x0f) << 2) | (source[sourceIndex + 2] >>> 6));
            target[3] = (char) (source[sourceIndex + 2] & 0x3f);
        }

    }

    /**
     * 十六进制字符串转byte数组
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.length() == 0) {
            return new byte[0];
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 二进制转十六进制
     *
     * @param bytes
     * @return
     */
    private static String bytesToHexString(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (byte temp : bytes) {
            int v = temp & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append(0);
            }
            sb.append(hv);

        }
        return sb.toString();
    }

    /**
     * 解密
     *
     * @param source
     * @return
     */
    public static String decode(String source) {
        if (source.length() % 4 != 0)
            throw new RuntimeException("valid   Base64   codes   have   a   multiple   of   4   characters ");
        int numGroups = source.length() / 4;
        int numExtraBytes = source.endsWith("== ") ? 2 : (source.endsWith("= ") ? 1 : 0);
        byte[] targetBytes = new byte[3 * numGroups];
        byte[] sourceBytes = new byte[4];
        for (int group = 0; group < numGroups; group++) {
            for (int i = 0; i < sourceBytes.length; i++) {
                sourceBytes[i] = (byte) Math.max(0, KEY.indexOf(source.charAt(4 * group + i)));
            }
            convert4To3(sourceBytes, targetBytes, group * 3);
        }
        return new String(targetBytes, 0, targetBytes.length - numExtraBytes);
    }

    private static void convert4To3(byte[] source, byte[] target, int targetIndex) {
        if (targetIndex + 2 < target.length) {
            target[targetIndex] = (byte) ((source[0] << 2) | (source[1] >>> 4));
            target[targetIndex + 1] = (byte) (((source[1] & 0x0f) << 4) | (source[2] >>> 2));
            target[targetIndex + 2] = (byte) (((source[2] & 0x03) << 6) | (source[3]));
        }

    }

    /**
     * 加密字符串（base64加密）
     *
     * @param str
     * @return
     */
    public static String encryptUrl(String str) {
        try {
            String prefix = bytesToHexString(encode(str).getBytes()).toUpperCase();

            // 生成md5加密后缀
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String encrypt = bytesToHexString(md5.digest(prefix.concat(SUFFIX).getBytes())).toUpperCase();

            return prefix.concat(encrypt.substring(encrypt.length() - 6));
        } catch (NoSuchAlgorithmException e) {

        }
        return null;
    }

    /**
     * 解密字符串
     *
     * @param str
     * @return
     */
    public static String decodeUrl(String str) {
        try {
            if (str == null || str.length() <= 10) {
                return null;
            }

            // 只匹配大小写字母和数字
            Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
            Matcher m = p.matcher(str);

            if (!m.matches()) {
                return null;
            }

            String matchSuffix = str.substring(str.length() - 6);

            // 生成md5加密后缀
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            String prefix = str.substring(0, str.length() - 6);
            // 生成正确的加密url
            String encrypt = bytesToHexString(md5.digest(prefix.concat(SUFFIX).getBytes())).toUpperCase();
            // 获取正确的后缀
            String realSuffix = encrypt.substring(encrypt.length() - 6);

            if (realSuffix.equals(matchSuffix)) {
                return decode(new String(hexStringToBytes(prefix))).trim();
            }
        } catch (NoSuchAlgorithmException e) {
        }

        return null;
    }

}
