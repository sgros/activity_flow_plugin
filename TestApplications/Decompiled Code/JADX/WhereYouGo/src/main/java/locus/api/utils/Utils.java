package locus.api.utils;

import java.io.Closeable;
import java.lang.reflect.Field;

public class Utils {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String TAG = "Utils";

    public static boolean parseBoolean(Object data) {
        return parseBoolean(String.valueOf(data));
    }

    public static boolean parseBoolean(String data) {
        try {
            return data.toLowerCase().contains("true") || data.contains("1");
        } catch (Exception e) {
            return false;
        }
    }

    public static int parseInt(Object data) {
        return parseInt(String.valueOf(data));
    }

    public static int parseInt(String data) {
        return parseInt(data, 0);
    }

    public static int parseInt(String data, int defValue) {
        try {
            return Integer.parseInt(data.trim());
        } catch (Exception e) {
            return defValue;
        }
    }

    public static Integer parseInteger(Object data) {
        return parseInteger(String.valueOf(data));
    }

    public static Integer parseInteger(String data) {
        try {
            return Integer.valueOf(data.trim());
        } catch (Exception e) {
            return Integer.valueOf(0);
        }
    }

    public static long parseLong(Object data) {
        return parseLong(String.valueOf(data));
    }

    public static long parseLong(String data) {
        try {
            return Long.parseLong(data.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public static float parseFloat(Object data) {
        return parseFloat(String.valueOf(data));
    }

    public static float parseFloat(String data) {
        try {
            return Float.parseFloat(data.trim());
        } catch (Exception e) {
            return 0.0f;
        }
    }

    public static double parseDouble(Object data) {
        return parseDouble(String.valueOf(data));
    }

    public static double parseDouble(String data) {
        try {
            return Double.parseDouble(data.trim().replace(",", "."));
        } catch (Exception e) {
            return 0.0d;
        }
    }

    public static byte[] doStringToBytes(String text) {
        try {
            return text.getBytes("UTF-8");
        } catch (Exception e) {
            Logger.logE(TAG, "doStringToBytes(" + text + ")", e);
            return new byte[0];
        }
    }

    public static String doBytesToString(byte[] data) {
        try {
            return new String(data, "UTF-8");
        } catch (Exception e) {
            Logger.logE(TAG, "doBytesToString(" + data + ")", e);
            return "";
        }
    }

    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                System.err.println("closeStream(" + stream + "), e:" + e);
                e.printStackTrace();
            }
        }
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String toString(Object obj) {
        return toString(obj, "");
    }

    public static String toString(Object obj, String prefix) {
        StringBuilder result = new StringBuilder();
        result.append(prefix);
        if (obj == null) {
            result.append(" empty object!");
            return result.toString();
        }
        result.append(obj.getClass().getName()).append(" {").append(NEW_LINE);
        for (Field field : obj.getClass().getDeclaredFields()) {
            result.append(prefix).append("    ");
            try {
                result.append(field.getName());
                result.append(": ");
                field.setAccessible(true);
                result.append(field.get(obj));
            } catch (Exception ex) {
                System.out.println(ex);
            }
            result.append(NEW_LINE);
        }
        result.append(prefix).append("}");
        return result.toString();
    }

    public static byte[] copyOf(byte[] original, int newLength) {
        byte[] copy = new byte[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    public static float[] copyOfRange(float[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int originalLength = original.length;
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        float[] result = new float[resultLength];
        System.arraycopy(original, start, result, 0, Math.min(resultLength, originalLength - start));
        return result;
    }
}
