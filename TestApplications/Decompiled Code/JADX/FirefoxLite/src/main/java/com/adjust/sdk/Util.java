package com.adjust.sdk;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Looper;
import android.provider.Settings.Secure;
import java.io.ObjectInputStream.GetField;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public class Util {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'Z";
    public static final DecimalFormat SecondsDisplayFormat = new DecimalFormat("0.0");
    public static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);
    private static final String fieldReadErrorMessage = "Unable to read '%s' field in migration device with message (%s)";

    private static ILogger getLogger() {
        return AdjustFactory.getLogger();
    }

    protected static String createUuid() {
        return UUID.randomUUID().toString();
    }

    public static String quote(String str) {
        if (str == null) {
            return null;
        }
        if (!Pattern.compile("\\s").matcher(str).find()) {
            return str;
        }
        return String.format(Locale.US, "'%s'", new Object[]{str});
    }

    public static String getPlayAdId(Context context) {
        return Reflection.getPlayAdId(context);
    }

    public static void getGoogleAdId(Context context, final OnDeviceIdsRead onDeviceIdsRead) {
        ILogger logger = AdjustFactory.getLogger();
        if (Looper.myLooper() != Looper.getMainLooper()) {
            logger.debug("GoogleAdId being read in the background", new Object[0]);
            String playAdId = getPlayAdId(context);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("GoogleAdId read ");
            stringBuilder.append(playAdId);
            logger.debug(stringBuilder.toString(), new Object[0]);
            onDeviceIdsRead.onGoogleAdIdRead(playAdId);
            return;
        }
        logger.debug("GoogleAdId being read in the foreground", new Object[0]);
        new AsyncTask<Context, Void, String>() {
            /* Access modifiers changed, original: protected|varargs */
            public String doInBackground(Context... contextArr) {
                ILogger logger = AdjustFactory.getLogger();
                String playAdId = Util.getPlayAdId(contextArr[0]);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("GoogleAdId read ");
                stringBuilder.append(playAdId);
                logger.debug(stringBuilder.toString(), new Object[0]);
                return playAdId;
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(String str) {
                AdjustFactory.getLogger();
                onDeviceIdsRead.onGoogleAdIdRead(str);
            }
        }.execute(new Context[]{context});
    }

    public static Boolean isPlayTrackingEnabled(Context context) {
        return Reflection.isPlayTrackingEnabled(context);
    }

    public static String getMacAddress(Context context) {
        return Reflection.getMacAddress(context);
    }

    public static Map<String, String> getPluginKeys(Context context) {
        return Reflection.getPluginKeys(context);
    }

    public static String getAndroidId(Context context) {
        return Reflection.getAndroidId(context);
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a2 A:{SYNTHETIC, Splitter:B:40:0x00a2} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a2 A:{SYNTHETIC, Splitter:B:40:0x00a2} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a2 A:{SYNTHETIC, Splitter:B:40:0x00a2} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a2 A:{SYNTHETIC, Splitter:B:40:0x00a2} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a2 A:{SYNTHETIC, Splitter:B:40:0x00a2} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a2 A:{SYNTHETIC, Splitter:B:40:0x00a2} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a2 A:{SYNTHETIC, Splitter:B:40:0x00a2} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a2 A:{SYNTHETIC, Splitter:B:40:0x00a2} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a2 A:{SYNTHETIC, Splitter:B:40:0x00a2} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a2 A:{SYNTHETIC, Splitter:B:40:0x00a2} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a2 A:{SYNTHETIC, Splitter:B:40:0x00a2} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a2 A:{SYNTHETIC, Splitter:B:40:0x00a2} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a2 A:{SYNTHETIC, Splitter:B:40:0x00a2} */
    /* JADX WARNING: Removed duplicated region for block: B:38:? A:{SYNTHETIC, PHI: r0 r7 , ExcHandler: FileNotFoundException (unused java.io.FileNotFoundException), Splitter:B:3:0x0008} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:31:0x007e, code skipped:
            r8 = e;
     */
    public static <T> T readObject(android.content.Context r7, java.lang.String r8, java.lang.String r9, java.lang.Class<T> r10) {
        /*
        r0 = 0;
        r1 = 2;
        r2 = 0;
        r3 = 1;
        r7 = r7.openFileInput(r8);	 Catch:{ FileNotFoundException -> 0x0092, Exception -> 0x0080 }
        r8 = new java.io.BufferedInputStream;	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r8.<init>(r7);	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r7 = new java.io.ObjectInputStream;	 Catch:{ FileNotFoundException -> 0x007c, Exception -> 0x0077 }
        r7.<init>(r8);	 Catch:{ FileNotFoundException -> 0x007c, Exception -> 0x0077 }
        r8 = r7.readObject();	 Catch:{ ClassNotFoundException -> 0x0062, ClassCastException -> 0x004d, Exception -> 0x0038, FileNotFoundException -> 0x0093 }
        r8 = r10.cast(r8);	 Catch:{ ClassNotFoundException -> 0x0062, ClassCastException -> 0x004d, Exception -> 0x0038, FileNotFoundException -> 0x0093 }
        r10 = getLogger();	 Catch:{ ClassNotFoundException -> 0x0035, ClassCastException -> 0x0032, Exception -> 0x002f, FileNotFoundException -> 0x002c }
        r0 = "Read %s: %s";
        r4 = new java.lang.Object[r1];	 Catch:{ ClassNotFoundException -> 0x0035, ClassCastException -> 0x0032, Exception -> 0x002f, FileNotFoundException -> 0x002c }
        r4[r2] = r9;	 Catch:{ ClassNotFoundException -> 0x0035, ClassCastException -> 0x0032, Exception -> 0x002f, FileNotFoundException -> 0x002c }
        r4[r3] = r8;	 Catch:{ ClassNotFoundException -> 0x0035, ClassCastException -> 0x0032, Exception -> 0x002f, FileNotFoundException -> 0x002c }
        r10.debug(r0, r4);	 Catch:{ ClassNotFoundException -> 0x0035, ClassCastException -> 0x0032, Exception -> 0x002f, FileNotFoundException -> 0x002c }
        r0 = r8;
        goto L_0x00a0;
    L_0x002c:
        r0 = r8;
        goto L_0x0093;
    L_0x002f:
        r10 = move-exception;
        r0 = r8;
        goto L_0x0039;
    L_0x0032:
        r10 = move-exception;
        r0 = r8;
        goto L_0x004e;
    L_0x0035:
        r10 = move-exception;
        r0 = r8;
        goto L_0x0063;
    L_0x0038:
        r10 = move-exception;
    L_0x0039:
        r8 = getLogger();	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r4 = "Failed to read %s object (%s)";
        r5 = new java.lang.Object[r1];	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r5[r2] = r9;	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r10 = r10.getMessage();	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r5[r3] = r10;	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r8.error(r4, r5);	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        goto L_0x00a0;
    L_0x004d:
        r10 = move-exception;
    L_0x004e:
        r8 = getLogger();	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r4 = "Failed to cast %s object (%s)";
        r5 = new java.lang.Object[r1];	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r5[r2] = r9;	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r10 = r10.getMessage();	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r5[r3] = r10;	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r8.error(r4, r5);	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        goto L_0x00a0;
    L_0x0062:
        r10 = move-exception;
    L_0x0063:
        r8 = getLogger();	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r4 = "Failed to find %s class (%s)";
        r5 = new java.lang.Object[r1];	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r5[r2] = r9;	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r10 = r10.getMessage();	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r5[r3] = r10;	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        r8.error(r4, r5);	 Catch:{ FileNotFoundException -> 0x0093, Exception -> 0x007e }
        goto L_0x00a0;
    L_0x0077:
        r7 = move-exception;
        r6 = r8;
        r8 = r7;
        r7 = r6;
        goto L_0x0082;
    L_0x007c:
        r7 = r8;
        goto L_0x0093;
    L_0x007e:
        r8 = move-exception;
        goto L_0x0082;
    L_0x0080:
        r8 = move-exception;
        r7 = r0;
    L_0x0082:
        r10 = getLogger();
        r4 = "Failed to open %s file for reading (%s)";
        r5 = new java.lang.Object[r1];
        r5[r2] = r9;
        r5[r3] = r8;
        r10.error(r4, r5);
        goto L_0x00a0;
    L_0x0092:
        r7 = r0;
    L_0x0093:
        r8 = getLogger();
        r10 = "%s file not found";
        r4 = new java.lang.Object[r3];
        r4[r2] = r9;
        r8.debug(r10, r4);
    L_0x00a0:
        if (r7 == 0) goto L_0x00b6;
    L_0x00a2:
        r7.close();	 Catch:{ Exception -> 0x00a6 }
        goto L_0x00b6;
    L_0x00a6:
        r7 = move-exception;
        r8 = getLogger();
        r10 = "Failed to close %s file for reading (%s)";
        r1 = new java.lang.Object[r1];
        r1[r2] = r9;
        r1[r3] = r7;
        r8.error(r10, r1);
    L_0x00b6:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adjust.sdk.Util.readObject(android.content.Context, java.lang.String, java.lang.String, java.lang.Class):java.lang.Object");
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x004a A:{SYNTHETIC, Splitter:B:18:0x004a} */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x004a A:{SYNTHETIC, Splitter:B:18:0x004a} */
    /* JADX WARNING: Removed duplicated region for block: B:23:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0024 */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:5|6|7|8|9|10) */
    public static <T> void writeObject(T r5, android.content.Context r6, java.lang.String r7, java.lang.String r8) {
        /*
        r0 = 2;
        r1 = 1;
        r2 = 0;
        r6 = r6.openFileOutput(r7, r2);	 Catch:{ Exception -> 0x0037 }
        r7 = new java.io.BufferedOutputStream;	 Catch:{ Exception -> 0x0035 }
        r7.<init>(r6);	 Catch:{ Exception -> 0x0035 }
        r6 = new java.io.ObjectOutputStream;	 Catch:{ Exception -> 0x0032 }
        r6.<init>(r7);	 Catch:{ Exception -> 0x0032 }
        r6.writeObject(r5);	 Catch:{ NotSerializableException -> 0x0024 }
        r7 = getLogger();	 Catch:{ NotSerializableException -> 0x0024 }
        r3 = "Wrote %s: %s";
        r4 = new java.lang.Object[r0];	 Catch:{ NotSerializableException -> 0x0024 }
        r4[r2] = r8;	 Catch:{ NotSerializableException -> 0x0024 }
        r4[r1] = r5;	 Catch:{ NotSerializableException -> 0x0024 }
        r7.debug(r3, r4);	 Catch:{ NotSerializableException -> 0x0024 }
        goto L_0x0048;
    L_0x0024:
        r5 = getLogger();	 Catch:{ Exception -> 0x0035 }
        r7 = "Failed to serialize %s";
        r3 = new java.lang.Object[r1];	 Catch:{ Exception -> 0x0035 }
        r3[r2] = r8;	 Catch:{ Exception -> 0x0035 }
        r5.error(r7, r3);	 Catch:{ Exception -> 0x0035 }
        goto L_0x0048;
    L_0x0032:
        r5 = move-exception;
        r6 = r7;
        goto L_0x0039;
    L_0x0035:
        r5 = move-exception;
        goto L_0x0039;
    L_0x0037:
        r5 = move-exception;
        r6 = 0;
    L_0x0039:
        r7 = getLogger();
        r3 = "Failed to open %s for writing (%s)";
        r4 = new java.lang.Object[r0];
        r4[r2] = r8;
        r4[r1] = r5;
        r7.error(r3, r4);
    L_0x0048:
        if (r6 == 0) goto L_0x005e;
    L_0x004a:
        r6.close();	 Catch:{ Exception -> 0x004e }
        goto L_0x005e;
    L_0x004e:
        r5 = move-exception;
        r6 = getLogger();
        r7 = "Failed to close %s file for writing (%s)";
        r0 = new java.lang.Object[r0];
        r0[r2] = r8;
        r0[r1] = r5;
        r6.error(r7, r0);
    L_0x005e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adjust.sdk.Util.writeObject(java.lang.Object, android.content.Context, java.lang.String, java.lang.String):void");
    }

    public static boolean checkPermission(Context context, String str) {
        return context.checkCallingOrSelfPermission(str) == 0;
    }

    public static String readStringField(GetField getField, String str, String str2) {
        return (String) readObjectField(getField, str, str2);
    }

    public static <T> T readObjectField(GetField getField, String str, T t) {
        try {
            return getField.get(str, t);
        } catch (Exception e) {
            getLogger().debug(fieldReadErrorMessage, str, e.getMessage());
            return t;
        }
    }

    public static boolean readBooleanField(GetField getField, String str, boolean z) {
        try {
            return getField.get(str, z);
        } catch (Exception e) {
            getLogger().debug(fieldReadErrorMessage, str, e.getMessage());
            return z;
        }
    }

    public static int readIntField(GetField getField, String str, int i) {
        try {
            return getField.get(str, i);
        } catch (Exception e) {
            getLogger().debug(fieldReadErrorMessage, str, e.getMessage());
            return i;
        }
    }

    public static long readLongField(GetField getField, String str, long j) {
        try {
            return getField.get(str, j);
        } catch (Exception e) {
            getLogger().debug(fieldReadErrorMessage, str, e.getMessage());
            return j;
        }
    }

    public static boolean equalObject(Object obj, Object obj2) {
        if (obj != null && obj2 != null) {
            return obj.equals(obj2);
        }
        boolean z = obj == null && obj2 == null;
        return z;
    }

    public static boolean equalsDouble(Double d, Double d2) {
        boolean z = true;
        if (d == null || d2 == null) {
            if (!(d == null && d2 == null)) {
                z = false;
            }
            return z;
        }
        if (Double.doubleToLongBits(d.doubleValue()) != Double.doubleToLongBits(d2.doubleValue())) {
            z = false;
        }
        return z;
    }

    public static boolean equalString(String str, String str2) {
        return equalObject(str, str2);
    }

    public static boolean equalEnum(Enum enumR, Enum enumR2) {
        return equalObject(enumR, enumR2);
    }

    public static boolean equalLong(Long l, Long l2) {
        return equalObject(l, l2);
    }

    public static boolean equalInt(Integer num, Integer num2) {
        return equalObject(num, num2);
    }

    public static boolean equalBoolean(Boolean bool, Boolean bool2) {
        return equalObject(bool, bool2);
    }

    public static int hashBoolean(Boolean bool) {
        return bool == null ? 0 : bool.hashCode();
    }

    public static int hashLong(Long l) {
        return l == null ? 0 : l.hashCode();
    }

    public static int hashString(String str) {
        return str == null ? 0 : str.hashCode();
    }

    public static int hashEnum(Enum enumR) {
        return enumR == null ? 0 : enumR.hashCode();
    }

    public static int hashObject(Object obj) {
        return obj == null ? 0 : obj.hashCode();
    }

    public static String sha1(String str) {
        return hash(str, Constants.SHA1);
    }

    public static String md5(String str) {
        return hash(str, Constants.MD5);
    }

    public static String hash(String str, String str2) {
        try {
            byte[] bytes = str.getBytes(Constants.ENCODING);
            MessageDigest instance = MessageDigest.getInstance(str2);
            instance.update(bytes, 0, bytes.length);
            return convertToHex(instance.digest());
        } catch (Exception unused) {
            return null;
        }
    }

    public static String convertToHex(byte[] bArr) {
        BigInteger bigInteger = new BigInteger(1, bArr);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("%0");
        stringBuilder.append(bArr.length << 1);
        stringBuilder.append("x");
        String stringBuilder2 = stringBuilder.toString();
        return String.format(Locale.US, stringBuilder2, new Object[]{bigInteger});
    }

    public static String[] getSupportedAbis() {
        return Reflection.getSupportedAbis();
    }

    public static String getCpuAbi() {
        return Reflection.getCpuAbi();
    }

    public static String getReasonString(String str, Throwable th) {
        if (th != null) {
            return String.format(Locale.US, "%s: %s", new Object[]{str, th});
        }
        return String.format(Locale.US, "%s", new Object[]{str});
    }

    public static long getWaitingTime(int i, BackoffStrategy backoffStrategy) {
        if (i < backoffStrategy.minRetries) {
            return 0;
        }
        return (long) (((double) Math.min(((long) Math.pow(2.0d, (double) (i - backoffStrategy.minRetries))) * backoffStrategy.milliSecondMultiplier, backoffStrategy.maxWait)) * randomInRange(backoffStrategy.minRange, backoffStrategy.maxRange));
    }

    private static double randomInRange(double d, double d2) {
        return (new Random().nextDouble() * (d2 - d)) + d;
    }

    public static boolean isValidParameter(String str, String str2, String str3) {
        if (str == null) {
            getLogger().error("%s parameter %s is missing", str3, str2);
            return false;
        } else if (!str.equals("")) {
            return true;
        } else {
            getLogger().error("%s parameter %s is empty", str3, str2);
            return false;
        }
    }

    public static Map<String, String> mergeParameters(Map<String, String> map, Map<String, String> map2, String str) {
        if (map == null) {
            return map2;
        }
        if (map2 == null) {
            return map;
        }
        HashMap hashMap = new HashMap(map);
        ILogger logger = getLogger();
        for (Entry entry : map2.entrySet()) {
            if (((String) hashMap.put(entry.getKey(), entry.getValue())) != null) {
                logger.warn("Key %s with value %s from %s parameter was replaced by value %s", entry.getKey(), (String) hashMap.put(entry.getKey(), entry.getValue()), str, entry.getValue());
            }
        }
        return hashMap;
    }

    public static String getVmInstructionSet() {
        return Reflection.getVmInstructionSet();
    }

    public static Locale getLocale(Configuration configuration) {
        Locale localeFromLocaleList = Reflection.getLocaleFromLocaleList(configuration);
        if (localeFromLocaleList != null) {
            return localeFromLocaleList;
        }
        return Reflection.getLocaleFromField(configuration);
    }

    public static String getFireAdvertisingId(ContentResolver contentResolver) {
        if (contentResolver == null) {
            return null;
        }
        try {
            return Secure.getString(contentResolver, "advertising_id");
        } catch (Exception unused) {
            return null;
        }
    }

    public static Boolean getFireTrackingEnabled(ContentResolver contentResolver) {
        try {
            return Boolean.valueOf(Secure.getInt(contentResolver, "limit_ad_tracking") == 0);
        } catch (Exception unused) {
            return null;
        }
    }
}
