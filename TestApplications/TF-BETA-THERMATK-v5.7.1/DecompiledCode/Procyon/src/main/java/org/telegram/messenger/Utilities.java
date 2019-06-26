// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import java.util.regex.Matcher;
import android.graphics.BitmapFactory$Options;
import java.math.BigInteger;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.io.FileInputStream;
import java.io.File;
import java.security.SecureRandom;
import java.util.regex.Pattern;

public class Utilities
{
    public static volatile DispatchQueue globalQueue;
    protected static final char[] hexArray;
    public static Pattern pattern;
    public static volatile DispatchQueue phoneBookQueue;
    public static SecureRandom random;
    public static volatile DispatchQueue searchQueue;
    public static volatile DispatchQueue stageQueue;
    
    static {
        Utilities.pattern = Pattern.compile("[\\-0-9]+");
        Utilities.random = new SecureRandom();
        Utilities.stageQueue = new DispatchQueue("stageQueue");
        Utilities.globalQueue = new DispatchQueue("globalQueue");
        Utilities.searchQueue = new DispatchQueue("searchQueue");
        Utilities.phoneBookQueue = new DispatchQueue("phoneBookQueue");
        hexArray = "0123456789ABCDEF".toCharArray();
        try {
            final FileInputStream fileInputStream = new FileInputStream(new File("/dev/urandom"));
            final byte[] array = new byte[1024];
            fileInputStream.read(array);
            fileInputStream.close();
            Utilities.random.setSeed(array);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static String MD5(String string) {
        if (string == null) {
            return null;
        }
        try {
            final byte[] digest = MessageDigest.getInstance("MD5").digest(AndroidUtilities.getStringBytes(string));
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; ++i) {
                sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
            }
            string = sb.toString();
            return string;
        }
        catch (NoSuchAlgorithmException ex) {
            FileLog.e(ex);
            return null;
        }
    }
    
    public static native void aesCbcEncryption(final ByteBuffer p0, final byte[] p1, final byte[] p2, final int p3, final int p4, final int p5);
    
    private static native void aesCbcEncryptionByteArray(final byte[] p0, final byte[] p1, final byte[] p2, final int p3, final int p4, final int p5, final int p6);
    
    public static void aesCbcEncryptionByteArraySafe(final byte[] array, final byte[] array2, final byte[] array3, final int n, final int n2, final int n3, final int n4) {
        aesCbcEncryptionByteArray(array, array2, array3.clone(), n, n2, n3, n4);
    }
    
    public static native void aesCtrDecryption(final ByteBuffer p0, final byte[] p1, final byte[] p2, final int p3, final int p4);
    
    public static native void aesCtrDecryptionByteArray(final byte[] p0, final byte[] p1, final byte[] p2, final int p3, final int p4, final int p5);
    
    private static native void aesIgeEncryption(final ByteBuffer p0, final byte[] p1, final byte[] p2, final boolean p3, final int p4, final int p5);
    
    public static void aesIgeEncryption(final ByteBuffer byteBuffer, final byte[] array, byte[] array2, final boolean b, final boolean b2, final int n, final int n2) {
        if (!b2) {
            array2 = array2.clone();
        }
        aesIgeEncryption(byteBuffer, array, array2, b, n, n2);
    }
    
    public static boolean arraysEquals(final byte[] array, final int n, final byte[] array2, final int n2) {
        if (array != null && array2 != null && n >= 0 && n2 >= 0 && array.length - n <= array2.length - n2 && array.length - n >= 0 && array2.length - n2 >= 0) {
            int i = n;
            boolean b = true;
            while (i < array.length) {
                if (array[i + n] != array2[i + n2]) {
                    b = false;
                }
                ++i;
            }
            return b;
        }
        return false;
    }
    
    public static native void blurBitmap(final Object p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    public static Bitmap blurWallpaper(final Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Bitmap bitmap2;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            bitmap2 = Bitmap.createBitmap(Math.round(bitmap.getWidth() * 450.0f / bitmap.getHeight()), 450, Bitmap$Config.ARGB_8888);
        }
        else {
            bitmap2 = Bitmap.createBitmap(450, Math.round(bitmap.getHeight() * 450.0f / bitmap.getWidth()), Bitmap$Config.ARGB_8888);
        }
        new Canvas(bitmap2).drawBitmap(bitmap, (Rect)null, new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight()), new Paint(2));
        stackBlurBitmap(bitmap2, 12);
        return bitmap2;
    }
    
    public static String bytesToHex(final byte[] array) {
        if (array == null) {
            return "";
        }
        final char[] value = new char[array.length * 2];
        for (int i = 0; i < array.length; ++i) {
            final int n = array[i] & 0xFF;
            final int n2 = i * 2;
            final char[] hexArray = Utilities.hexArray;
            value[n2] = hexArray[n >>> 4];
            value[n2 + 1] = hexArray[n & 0xF];
        }
        return new String(value);
    }
    
    public static int bytesToInt(final byte[] array) {
        return ((array[3] & 0xFF) << 24) + ((array[2] & 0xFF) << 16) + ((array[1] & 0xFF) << 8) + (array[0] & 0xFF);
    }
    
    public static long bytesToLong(final byte[] array) {
        return ((long)array[7] << 56) + (((long)array[6] & 0xFFL) << 48) + (((long)array[5] & 0xFFL) << 40) + (((long)array[4] & 0xFFL) << 32) + (((long)array[3] & 0xFFL) << 24) + (((long)array[2] & 0xFFL) << 16) + (((long)array[1] & 0xFFL) << 8) + ((long)array[0] & 0xFFL);
    }
    
    public static native void calcCDT(final ByteBuffer p0, final int p1, final int p2, final ByteBuffer p3);
    
    public static native void clearDir(final String p0, final int p1, final long p2);
    
    public static byte[] computePBKDF2(final byte[] array, final byte[] array2) {
        final byte[] array3 = new byte[64];
        pbkdf2(array, array2, array3, 100000);
        return array3;
    }
    
    public static byte[] computeSHA1(final ByteBuffer byteBuffer) {
        return computeSHA1(byteBuffer, 0, byteBuffer.limit());
    }
    
    public static byte[] computeSHA1(final ByteBuffer input, final int n, final int n2) {
        final int position = input.position();
        final int limit = input.limit();
        try {
            try {
                final MessageDigest instance = MessageDigest.getInstance("SHA-1");
                input.position(n);
                input.limit(n2);
                instance.update(input);
                final byte[] digest = instance.digest();
                input.limit(limit);
                input.position(position);
                return digest;
            }
            finally {}
        }
        catch (Exception ex) {
            FileLog.e(ex);
            input.limit(limit);
            input.position(position);
            return new byte[20];
        }
        input.limit(limit);
        input.position(position);
    }
    
    public static byte[] computeSHA1(final byte[] array) {
        return computeSHA1(array, 0, array.length);
    }
    
    public static byte[] computeSHA1(byte[] digest, final int offset, final int len) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.update(digest, offset, len);
            digest = instance.digest();
            return digest;
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return new byte[20];
        }
    }
    
    public static byte[] computeSHA256(final byte[] array) {
        return computeSHA256(array, 0, array.length);
    }
    
    public static byte[] computeSHA256(byte[] digest, final int offset, final int len) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.update(digest, offset, len);
            digest = instance.digest();
            return digest;
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return new byte[32];
        }
    }
    
    public static byte[] computeSHA256(byte[] digest, final int offset, final int len, final ByteBuffer input, final int n, final int n2) {
        final int position = input.position();
        final int limit = input.limit();
        try {
            try {
                final MessageDigest instance = MessageDigest.getInstance("SHA-256");
                instance.update(digest, offset, len);
                input.position(n);
                input.limit(n2);
                instance.update(input);
                digest = instance.digest();
                input.limit(limit);
                input.position(position);
                return digest;
            }
            finally {}
        }
        catch (Exception ex) {
            FileLog.e(ex);
            input.limit(limit);
            input.position(position);
            return new byte[32];
        }
        input.limit(limit);
        input.position(position);
    }
    
    public static byte[] computeSHA256(final byte[]... array) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("SHA-256");
            for (int i = 0; i < array.length; ++i) {
                instance.update(array[i], 0, array[i].length);
            }
            return instance.digest();
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return new byte[32];
        }
    }
    
    public static byte[] computeSHA512(byte[] digest) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("SHA-512");
            instance.update(digest, 0, digest.length);
            digest = instance.digest();
            return digest;
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return new byte[64];
        }
    }
    
    public static byte[] computeSHA512(byte[] digest, final byte[] input) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("SHA-512");
            instance.update(digest, 0, digest.length);
            instance.update(input, 0, input.length);
            digest = instance.digest();
            return digest;
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return new byte[64];
        }
    }
    
    public static byte[] computeSHA512(byte[] digest, final byte[] input, final byte[] input2) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("SHA-512");
            instance.update(digest, 0, digest.length);
            instance.update(input, 0, input.length);
            instance.update(input2, 0, input2.length);
            digest = instance.digest();
            return digest;
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return new byte[64];
        }
    }
    
    public static native int convertVideoFrame(final ByteBuffer p0, final ByteBuffer p1, final int p2, final int p3, final int p4, final int p5, final int p6);
    
    public static native long getDirSize(final String p0, final int p1);
    
    public static byte[] hexToBytes(final String s) {
        if (s == null) {
            return null;
        }
        final int length = s.length();
        final byte[] array = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            array[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return array;
    }
    
    public static boolean isGoodGaAndGb(final BigInteger bigInteger, final BigInteger bigInteger2) {
        return bigInteger.compareTo(BigInteger.valueOf(1L)) > 0 && bigInteger.compareTo(bigInteger2.subtract(BigInteger.valueOf(1L))) < 0;
    }
    
    public static boolean isGoodPrime(final byte[] magnitude, int n) {
        boolean b2;
        final boolean b = b2 = false;
        if (n >= 2) {
            if (n > 7) {
                b2 = b;
            }
            else {
                b2 = b;
                if (magnitude.length == 256) {
                    if (magnitude[0] >= 0) {
                        b2 = b;
                    }
                    else {
                        final BigInteger bigInteger = new BigInteger(1, magnitude);
                        if (n == 2) {
                            if (bigInteger.mod(BigInteger.valueOf(8L)).intValue() != 7) {
                                return false;
                            }
                        }
                        else if (n == 3) {
                            if (bigInteger.mod(BigInteger.valueOf(3L)).intValue() != 2) {
                                return false;
                            }
                        }
                        else if (n == 5) {
                            n = bigInteger.mod(BigInteger.valueOf(5L)).intValue();
                            if (n != 1 && n != 4) {
                                return false;
                            }
                        }
                        else if (n == 6) {
                            n = bigInteger.mod(BigInteger.valueOf(24L)).intValue();
                            if (n != 19 && n != 23) {
                                return false;
                            }
                        }
                        else if (n == 7) {
                            n = bigInteger.mod(BigInteger.valueOf(7L)).intValue();
                            if (n != 3 && n != 5 && n != 6) {
                                return false;
                            }
                        }
                        if (bytesToHex(magnitude).equals("C71CAEB9C6B1C9048E6C522F70F13F73980D40238E3E21C14934D037563D930F48198A0AA7C14058229493D22530F4DBFA336F6E0AC925139543AED44CCE7C3720FD51F69458705AC68CD4FE6B6B13ABDC9746512969328454F18FAF8C595F642477FE96BB2A941D5BCD1D4AC8CC49880708FA9B378E3C4F3A9060BEE67CF9A4A4A695811051907E162753B56B0F6B410DBA74D8A84B2A14B3144E0EF1284754FD17ED950D5965B4B9DD46582DB1178D169C6BC465B0D6FF9CA3928FEF5B9AE4E418FC15E83EBEA0F87FA9FF5EED70050DED2849F47BF959D956850CE929851F0D8115F635B105EE2E4E15D04B2454BF6F4FADF034B10403119CD8E3B92FCC5B")) {
                            return true;
                        }
                        final BigInteger divide = bigInteger.subtract(BigInteger.valueOf(1L)).divide(BigInteger.valueOf(2L));
                        b2 = b;
                        if (bigInteger.isProbablePrime(30)) {
                            b2 = b;
                            if (divide.isProbablePrime(30)) {
                                b2 = true;
                            }
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    public static native boolean loadWebpImage(final Bitmap p0, final ByteBuffer p1, final int p2, final BitmapFactory$Options p3, final boolean p4);
    
    public static native int needInvert(final Object p0, final int p1, final int p2, final int p3, final int p4);
    
    public static Integer parseInt(final String input) {
        final Integer value = 0;
        if (input == null) {
            return value;
        }
        Integer value2;
        try {
            final Matcher matcher = Utilities.pattern.matcher(input);
            value2 = value;
            if (matcher.find()) {
                value2 = Integer.parseInt(matcher.group(0));
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
            value2 = value;
        }
        return value2;
    }
    
    public static String parseIntToString(final String input) {
        final Matcher matcher = Utilities.pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }
    
    public static Long parseLong(final String input) {
        final Long value = 0L;
        if (input == null) {
            return value;
        }
        Long value2;
        try {
            final Matcher matcher = Utilities.pattern.matcher(input);
            value2 = value;
            if (matcher.find()) {
                value2 = Long.parseLong(matcher.group(0));
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
            value2 = value;
        }
        return value2;
    }
    
    private static native int pbkdf2(final byte[] p0, final byte[] p1, final byte[] p2, final int p3);
    
    public static native int pinBitmap(final Bitmap p0);
    
    public static native String readlink(final String p0);
    
    public static native void stackBlurBitmap(final Bitmap p0, final int p1);
    
    public static native void unpinBitmap(final Bitmap p0);
}
