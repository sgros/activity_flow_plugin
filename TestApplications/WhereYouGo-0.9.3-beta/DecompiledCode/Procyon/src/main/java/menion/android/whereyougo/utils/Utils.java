// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import android.os.Binder;
import android.content.Intent;
import android.os.Build$VERSION;
import android.util.DisplayMetrics;
import android.content.Context;
import java.io.Closeable;
import java.security.MessageDigest;

public class Utils
{
    private static final String TAG = "Utils";
    private static float density;
    private static int densityDpi;
    private static final char[] hexDigit;
    private static MessageDigest md;
    private static int screenCategory;
    
    static {
        Utils.density = -1.0f;
        Utils.densityDpi = 0;
        Utils.screenCategory = -1;
        hexDigit = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    }
    
    public static String addZerosBefore(final String str, final int n) {
        String concat = "";
        for (int i = 0; i < n - str.length(); ++i) {
            concat = concat.concat("0");
        }
        return concat + str;
    }
    
    private static String bytesToHex(final byte[] array) {
        final int min = Math.min(array.length, 5);
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < min; ++i) {
            sb.append(Utils.hexDigit[array[i] >> 4 & 0xF]);
            sb.append(Utils.hexDigit[array[i] & 0xF]);
        }
        return sb.toString();
    }
    
    public static void closeStream(final Closeable obj) {
        if (obj == null) {
            return;
        }
        try {
            obj.close();
        }
        catch (Exception ex) {
            Logger.e("Utils", "closeStream(" + obj + ")", ex);
        }
    }
    
    public static float getDensity() {
        return getDpPixels(1.0f);
    }
    
    public static float getDpPixels(final float n) {
        return getDpPixels((Context)A.getApp(), n);
    }
    
    public static float getDpPixels(final Context context, float f) {
        try {
            if (Utils.density == -1.0f) {
                final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                Utils.density = displayMetrics.density;
                Utils.densityDpi = displayMetrics.densityDpi;
            }
            f *= Utils.density;
            return f;
        }
        catch (Exception obj) {
            Logger.e("Utils", "getDpPixels(" + f + "), e:" + obj);
            obj.printStackTrace();
            return f;
        }
    }
    
    public static int getScreenCategory() {
        if (Utils.screenCategory == -1) {
            getDpPixels(1.0f);
            if (Const.SCREEN_WIDTH * Const.SCREEN_HEIGHT >= 691200) {
                Utils.screenCategory = 3;
            }
            else if (Const.SCREEN_WIDTH * Const.SCREEN_HEIGHT >= 307200) {
                Utils.screenCategory = 2;
            }
            else if (Const.SCREEN_WIDTH * Const.SCREEN_HEIGHT >= 150400) {
                Utils.screenCategory = 1;
            }
            else {
                Utils.screenCategory = 0;
            }
        }
        return Utils.screenCategory;
    }
    
    public static int getScreenDpi() {
        getDpPixels(1.0f);
        return Utils.densityDpi;
    }
    
    public static String hashString(String bytesToHex) {
        // monitorenter(Utils.class)
        Label_0014: {
            if (bytesToHex == null) {
                break Label_0014;
            }
            try {
                if (bytesToHex.length() == 0) {
                    bytesToHex = "";
                }
                else {
                    if (Utils.md == null) {
                        Utils.md = MessageDigest.getInstance("SHA1");
                    }
                    Utils.md.update(bytesToHex.getBytes());
                    bytesToHex = bytesToHex(Utils.md.digest());
                }
                return bytesToHex;
            }
            catch (Exception ex) {
                Logger.e("Utils", "hashString(" + bytesToHex + ")", ex);
                bytesToHex = "";
                return bytesToHex;
            }
            finally {
            }
            // monitorexit(Utils.class)
        }
    }
    
    public static boolean isAndroid201OrMore() {
        return parseInt(Build$VERSION.SDK) >= 6;
    }
    
    public static boolean isAndroid21OrMore() {
        return parseInt(Build$VERSION.SDK) >= 7;
    }
    
    public static boolean isAndroid22OrMore() {
        return parseInt(Build$VERSION.SDK) >= 8;
    }
    
    public static boolean isAndroid23OrMore() {
        return parseInt(Build$VERSION.SDK) >= 9;
    }
    
    public static boolean isAndroid30OrMore() {
        return parseInt(Build$VERSION.SDK) >= 11;
    }
    
    public static boolean isAndroidTablet30OrMore() {
        return parseInt(Build$VERSION.SDK) >= 11 && getScreenCategory() == 3;
    }
    
    public static boolean isIntentAvailable(final Intent intent) {
        return A.getApp().getPackageManager().queryIntentActivities(intent, 65536).size() > 0;
    }
    
    public static boolean isIntentAvailable(final String s) {
        return isIntentAvailable(new Intent(s));
    }
    
    public static boolean isPermissionAllowed(final String str) {
        boolean b = false;
        try {
            if (A.getApp().checkPermission(str, Binder.getCallingPid(), Binder.getCallingUid()) == 0) {
                b = true;
            }
            return b;
        }
        catch (Exception ex) {
            Logger.e("Utils", "isPermissionAllowed(" + str + ")", ex);
            return b;
        }
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
            return Double.parseDouble(s.trim());
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
        try {
            return Integer.parseInt(s.trim());
        }
        catch (Exception ex) {
            return 0;
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
    
    public static String streamToString(final InputStream in) {
        final char[] str = new char[1024];
        final StringBuilder sb = new StringBuilder();
        try {
            final InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
            while (true) {
                final int read = inputStreamReader.read(str, 0, str.length);
                if (read < 0) {
                    break;
                }
                sb.append(str, 0, read);
            }
            closeStream(in);
            return sb.toString();
        }
        catch (IOException ex) {
            closeStream(in);
            return sb.toString();
        }
        finally {
            closeStream(in);
        }
    }
}
