package menion.android.whereyougo.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.MessageDigest;

public class Utils {
    private static final String TAG = "Utils";
    private static float density = -1.0f;
    private static int densityDpi = 0;
    private static final char[] hexDigit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /* renamed from: md */
    private static MessageDigest f60md;
    private static int screenCategory = -1;

    public static String addZerosBefore(String s, int length) {
        String prefix = "";
        for (int i = 0; i < length - s.length(); i++) {
            prefix = prefix.concat("0");
        }
        return prefix + s;
    }

    private static String bytesToHex(byte[] b) {
        int length = Math.min(b.length, 5);
        StringBuilder buf = new StringBuilder();
        for (int j = 0; j < length; j++) {
            buf.append(hexDigit[(b[j] >> 4) & 15]);
            buf.append(hexDigit[b[j] & 15]);
        }
        return buf.toString();
    }

    public static String streamToString(InputStream is) {
        char[] buffer = new char[1024];
        StringBuilder out = new StringBuilder();
        try {
            Reader in = new InputStreamReader(is, "UTF-8");
            while (true) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0) {
                    break;
                }
                out.append(buffer, 0, rsz);
            }
        } catch (IOException e) {
        } finally {
            closeStream(is);
        }
        return out.toString();
    }

    public static void closeStream(Closeable is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                Logger.m22e(TAG, "closeStream(" + is + ")", e);
            }
        }
    }

    public static float getDensity() {
        return getDpPixels(1.0f);
    }

    public static float getDpPixels(Context context, float pixels) {
        try {
            if (density == -1.0f) {
                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                density = metrics.density;
                densityDpi = metrics.densityDpi;
            }
            return pixels * density;
        } catch (Exception e) {
            Logger.m21e(TAG, "getDpPixels(" + pixels + "), e:" + e);
            e.printStackTrace();
            return pixels;
        }
    }

    public static float getDpPixels(float pixels) {
        return getDpPixels(C0322A.getApp(), pixels);
    }

    public static int getScreenCategory() {
        if (screenCategory == -1) {
            getDpPixels(1.0f);
            if (Const.SCREEN_WIDTH * Const.SCREEN_HEIGHT >= 691200) {
                screenCategory = 3;
            } else if (Const.SCREEN_WIDTH * Const.SCREEN_HEIGHT >= 307200) {
                screenCategory = 2;
            } else if (Const.SCREEN_WIDTH * Const.SCREEN_HEIGHT >= 150400) {
                screenCategory = 1;
            } else {
                screenCategory = 0;
            }
        }
        return screenCategory;
    }

    public static int getScreenDpi() {
        getDpPixels(1.0f);
        return densityDpi;
    }

    public static synchronized String hashString(String data) {
        String bytesToHex;
        synchronized (Utils.class) {
            if (data != null) {
                try {
                    if (data.length() != 0) {
                        if (f60md == null) {
                            f60md = MessageDigest.getInstance("SHA1");
                        }
                        f60md.update(data.getBytes());
                        bytesToHex = bytesToHex(f60md.digest());
                    }
                } catch (Exception e) {
                    Logger.m22e(TAG, "hashString(" + data + ")", e);
                    bytesToHex = "";
                }
            }
            bytesToHex = "";
        }
        return bytesToHex;
    }

    public static boolean isAndroid201OrMore() {
        return parseInt(VERSION.SDK) >= 6;
    }

    public static boolean isAndroid21OrMore() {
        return parseInt(VERSION.SDK) >= 7;
    }

    public static boolean isAndroid22OrMore() {
        return parseInt(VERSION.SDK) >= 8;
    }

    public static boolean isAndroid23OrMore() {
        return parseInt(VERSION.SDK) >= 9;
    }

    public static boolean isAndroid30OrMore() {
        return parseInt(VERSION.SDK) >= 11;
    }

    public static boolean isAndroidTablet30OrMore() {
        return parseInt(VERSION.SDK) >= 11 && getScreenCategory() == 3;
    }

    public static boolean isIntentAvailable(Intent intent) {
        return C0322A.getApp().getPackageManager().queryIntentActivities(intent, 65536).size() > 0;
    }

    public static boolean isIntentAvailable(String action) {
        return isIntentAvailable(new Intent(action));
    }

    public static boolean isPermissionAllowed(String permission) {
        try {
            return C0322A.getApp().checkPermission(permission, Binder.getCallingPid(), Binder.getCallingUid()) == 0;
        } catch (Exception e) {
            Logger.m22e(TAG, "isPermissionAllowed(" + permission + ")", e);
            return false;
        }
    }

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

    public static double parseDouble(Object data) {
        return parseDouble(String.valueOf(data));
    }

    public static double parseDouble(String data) {
        try {
            return Double.parseDouble(data.trim());
        } catch (Exception e) {
            return 0.0d;
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

    public static int parseInt(Object data) {
        return parseInt(String.valueOf(data));
    }

    public static int parseInt(String data) {
        try {
            return Integer.parseInt(data.trim());
        } catch (Exception e) {
            return 0;
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
}
