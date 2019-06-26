// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.utils;

import java.lang.reflect.Field;
import java.io.Closeable;

public class Utils
{
    private static final String NEW_LINE;
    private static final String TAG = "Utils";
    
    static {
        NEW_LINE = System.getProperty("line.separator");
    }
    
    public static void closeStream(final Closeable obj) {
        if (obj == null) {
            return;
        }
        try {
            obj.close();
        }
        catch (Exception obj2) {
            System.err.println("closeStream(" + obj + "), e:" + obj2);
            obj2.printStackTrace();
        }
    }
    
    public static byte[] copyOf(final byte[] array, final int b) {
        final byte[] array2 = new byte[b];
        System.arraycopy(array, 0, array2, 0, Math.min(array.length, b));
        return array2;
    }
    
    public static byte[] copyOfRange(final byte[] array, final int i, final int j) {
        final int b = j - i;
        if (b < 0) {
            throw new IllegalArgumentException(i + " > " + j);
        }
        final byte[] array2 = new byte[b];
        System.arraycopy(array, i, array2, 0, Math.min(array.length - i, b));
        return array2;
    }
    
    public static float[] copyOfRange(final float[] array, final int n, int a) {
        if (n > a) {
            throw new IllegalArgumentException();
        }
        final int length = array.length;
        if (n < 0 || n > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        a -= n;
        final int min = Math.min(a, length - n);
        final float[] array2 = new float[a];
        System.arraycopy(array, n, array2, 0, min);
        return array2;
    }
    
    public static String doBytesToString(byte[] o) {
        try {
            o = new String((byte[])o, "UTF-8");
            return (String)o;
        }
        catch (Exception ex) {
            Logger.logE("Utils", "doBytesToString(" + o + ")", ex);
            o = "";
            return (String)o;
        }
    }
    
    public static byte[] doStringToBytes(String bytes) {
        try {
            bytes = ((String)bytes).getBytes("UTF-8");
            return (byte[])bytes;
        }
        catch (Exception ex) {
            Logger.logE("Utils", "doStringToBytes(" + (String)bytes + ")", ex);
            bytes = new byte[0];
            return (byte[])bytes;
        }
    }
    
    public static boolean isEmpty(final CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }
    
    public static boolean parseBoolean(final Object obj) {
        return parseBoolean(String.valueOf(obj));
    }
    
    public static boolean parseBoolean(final String s) {
        boolean b = false;
        try {
            if (s.toLowerCase().contains("true") || s.contains("1")) {
                b = true;
            }
            return b;
        }
        catch (Exception ex) {
            return b;
        }
    }
    
    public static double parseDouble(final Object obj) {
        return parseDouble(String.valueOf(obj));
    }
    
    public static double parseDouble(final String s) {
        try {
            return Double.parseDouble(s.trim().replace(",", "."));
        }
        catch (Exception ex) {
            return 0.0;
        }
    }
    
    public static float parseFloat(final Object obj) {
        return parseFloat(String.valueOf(obj));
    }
    
    public static float parseFloat(final String s) {
        try {
            return Float.parseFloat(s.trim());
        }
        catch (Exception ex) {
            return 0.0f;
        }
    }
    
    public static int parseInt(final Object obj) {
        return parseInt(String.valueOf(obj));
    }
    
    public static int parseInt(final String s) {
        return parseInt(s, 0);
    }
    
    public static int parseInt(final String s, int int1) {
        try {
            int1 = Integer.parseInt(s.trim());
            return int1;
        }
        catch (Exception ex) {
            return int1;
        }
    }
    
    public static Integer parseInteger(final Object obj) {
        return parseInteger(String.valueOf(obj));
    }
    
    public static Integer parseInteger(final String s) {
        try {
            return Integer.valueOf(s.trim());
        }
        catch (Exception ex) {
            return 0;
        }
    }
    
    public static long parseLong(final Object obj) {
        return parseLong(String.valueOf(obj));
    }
    
    public static long parseLong(final String s) {
        try {
            return Long.parseLong(s.trim());
        }
        catch (Exception ex) {
            return 0L;
        }
    }
    
    public static String toString(final Object o) {
        return toString(o, "");
    }
    
    public static String toString(final Object obj, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        String s;
        if (obj == null) {
            sb.append(" empty object!");
            s = sb.toString();
        }
        else {
            sb.append(obj.getClass().getName()).append(" {").append(Utils.NEW_LINE);
            final Field[] declaredFields = obj.getClass().getDeclaredFields();
            final int length = declaredFields.length;
            int i = 0;
        Label_0128_Outer:
            while (i < length) {
                final Field field = declaredFields[i];
                sb.append(str).append("    ");
                while (true) {
                    try {
                        sb.append(field.getName());
                        sb.append(": ");
                        field.setAccessible(true);
                        sb.append(field.get(obj));
                        sb.append(Utils.NEW_LINE);
                        ++i;
                        continue Label_0128_Outer;
                    }
                    catch (Exception x) {
                        System.out.println(x);
                        continue;
                    }
                    break;
                }
                break;
            }
            sb.append(str).append("}");
            s = sb.toString();
        }
        return s;
    }
}
