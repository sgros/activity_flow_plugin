// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import java.io.Closeable;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.HashMap;
import java.security.MessageDigest;
import java.util.Map;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Looper;
import android.provider.Settings$Secure;
import android.content.ContentResolver;
import java.util.UUID;
import java.math.BigInteger;
import android.content.Context;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

public class Util
{
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'Z";
    public static final DecimalFormat SecondsDisplayFormat;
    public static final SimpleDateFormat dateFormatter;
    private static final String fieldReadErrorMessage = "Unable to read '%s' field in migration device with message (%s)";
    
    static {
        SecondsDisplayFormat = new DecimalFormat("0.0");
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'Z", Locale.US);
    }
    
    public static boolean checkPermission(final Context context, final String s) {
        return context.checkCallingOrSelfPermission(s) == 0;
    }
    
    public static String convertToHex(final byte[] magnitude) {
        final BigInteger bigInteger = new BigInteger(1, magnitude);
        final StringBuilder sb = new StringBuilder();
        sb.append("%0");
        sb.append(magnitude.length << 1);
        sb.append("x");
        return String.format(Locale.US, sb.toString(), bigInteger);
    }
    
    protected static String createUuid() {
        return UUID.randomUUID().toString();
    }
    
    public static boolean equalBoolean(final Boolean b, final Boolean b2) {
        return equalObject(b, b2);
    }
    
    public static boolean equalEnum(final Enum enum1, final Enum enum2) {
        return equalObject(enum1, enum2);
    }
    
    public static boolean equalInt(final Integer n, final Integer n2) {
        return equalObject(n, n2);
    }
    
    public static boolean equalLong(final Long n, final Long n2) {
        return equalObject(n, n2);
    }
    
    public static boolean equalObject(final Object o, final Object obj) {
        if (o != null && obj != null) {
            return o.equals(obj);
        }
        return o == null && obj == null;
    }
    
    public static boolean equalString(final String s, final String s2) {
        return equalObject(s, s2);
    }
    
    public static boolean equalsDouble(final Double n, final Double n2) {
        final boolean b = true;
        boolean b2 = true;
        if (n != null && n2 != null) {
            if (Double.doubleToLongBits(n) != Double.doubleToLongBits(n2)) {
                b2 = false;
            }
            return b2;
        }
        return n == null && n2 == null && b;
    }
    
    public static String getAndroidId(final Context context) {
        return Reflection.getAndroidId(context);
    }
    
    public static String getCpuAbi() {
        return Reflection.getCpuAbi();
    }
    
    public static String getFireAdvertisingId(final ContentResolver contentResolver) {
        if (contentResolver == null) {
            return null;
        }
        try {
            return Settings$Secure.getString(contentResolver, "advertising_id");
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static Boolean getFireTrackingEnabled(final ContentResolver contentResolver) {
        try {
            return Settings$Secure.getInt(contentResolver, "limit_ad_tracking") == 0;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static void getGoogleAdId(final Context context, final OnDeviceIdsRead onDeviceIdsRead) {
        final ILogger logger = AdjustFactory.getLogger();
        if (Looper.myLooper() != Looper.getMainLooper()) {
            logger.debug("GoogleAdId being read in the background", new Object[0]);
            final String playAdId = getPlayAdId(context);
            final StringBuilder sb = new StringBuilder();
            sb.append("GoogleAdId read ");
            sb.append(playAdId);
            logger.debug(sb.toString(), new Object[0]);
            onDeviceIdsRead.onGoogleAdIdRead(playAdId);
            return;
        }
        logger.debug("GoogleAdId being read in the foreground", new Object[0]);
        new AsyncTask<Context, Void, String>() {
            protected String doInBackground(final Context... array) {
                final ILogger logger = AdjustFactory.getLogger();
                final String playAdId = Util.getPlayAdId(array[0]);
                final StringBuilder sb = new StringBuilder();
                sb.append("GoogleAdId read ");
                sb.append(playAdId);
                logger.debug(sb.toString(), new Object[0]);
                return playAdId;
            }
            
            protected void onPostExecute(final String s) {
                AdjustFactory.getLogger();
                onDeviceIdsRead.onGoogleAdIdRead(s);
            }
        }.execute((Object[])new Context[] { context });
    }
    
    public static Locale getLocale(final Configuration configuration) {
        final Locale localeFromLocaleList = Reflection.getLocaleFromLocaleList(configuration);
        if (localeFromLocaleList != null) {
            return localeFromLocaleList;
        }
        return Reflection.getLocaleFromField(configuration);
    }
    
    private static ILogger getLogger() {
        return AdjustFactory.getLogger();
    }
    
    public static String getMacAddress(final Context context) {
        return Reflection.getMacAddress(context);
    }
    
    public static String getPlayAdId(final Context context) {
        return Reflection.getPlayAdId(context);
    }
    
    public static Map<String, String> getPluginKeys(final Context context) {
        return Reflection.getPluginKeys(context);
    }
    
    public static String getReasonString(final String s, final Throwable t) {
        if (t != null) {
            return String.format(Locale.US, "%s: %s", s, t);
        }
        return String.format(Locale.US, "%s", s);
    }
    
    public static String[] getSupportedAbis() {
        return Reflection.getSupportedAbis();
    }
    
    public static String getVmInstructionSet() {
        return Reflection.getVmInstructionSet();
    }
    
    public static long getWaitingTime(final int n, final BackoffStrategy backoffStrategy) {
        if (n < backoffStrategy.minRetries) {
            return 0L;
        }
        return (long)(Math.min((long)Math.pow(2.0, n - backoffStrategy.minRetries) * backoffStrategy.milliSecondMultiplier, backoffStrategy.maxWait) * randomInRange(backoffStrategy.minRange, backoffStrategy.maxRange));
    }
    
    public static String hash(String convertToHex, final String algorithm) {
        try {
            final byte[] bytes = convertToHex.getBytes("UTF-8");
            final MessageDigest instance = MessageDigest.getInstance(algorithm);
            instance.update(bytes, 0, bytes.length);
            convertToHex = convertToHex(instance.digest());
        }
        catch (Exception ex) {
            convertToHex = null;
        }
        return convertToHex;
    }
    
    public static int hashBoolean(final Boolean b) {
        if (b == null) {
            return 0;
        }
        return b.hashCode();
    }
    
    public static int hashEnum(final Enum enum1) {
        if (enum1 == null) {
            return 0;
        }
        return enum1.hashCode();
    }
    
    public static int hashLong(final Long n) {
        if (n == null) {
            return 0;
        }
        return n.hashCode();
    }
    
    public static int hashObject(final Object o) {
        if (o == null) {
            return 0;
        }
        return o.hashCode();
    }
    
    public static int hashString(final String s) {
        if (s == null) {
            return 0;
        }
        return s.hashCode();
    }
    
    public static Boolean isPlayTrackingEnabled(final Context context) {
        return Reflection.isPlayTrackingEnabled(context);
    }
    
    public static boolean isValidParameter(final String s, final String s2, final String s3) {
        if (s == null) {
            getLogger().error("%s parameter %s is missing", s3, s2);
            return false;
        }
        if (s.equals("")) {
            getLogger().error("%s parameter %s is empty", s3, s2);
            return false;
        }
        return true;
    }
    
    public static String md5(final String s) {
        return hash(s, "MD5");
    }
    
    public static Map<String, String> mergeParameters(final Map<String, String> m, final Map<String, String> map, final String s) {
        if (m == null) {
            return map;
        }
        if (map == null) {
            return m;
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>(m);
        final ILogger logger = getLogger();
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            final String s2 = hashMap.put(entry.getKey(), entry.getValue());
            if (s2 != null) {
                logger.warn("Key %s with value %s from %s parameter was replaced by value %s", entry.getKey(), s2, s, entry.getValue());
            }
        }
        return hashMap;
    }
    
    public static String quote(final String input) {
        if (input == null) {
            return null;
        }
        if (!Pattern.compile("\\s").matcher(input).find()) {
            return input;
        }
        return String.format(Locale.US, "'%s'", input);
    }
    
    private static double randomInRange(final double n, final double n2) {
        return new Random().nextDouble() * (n2 - n) + n;
    }
    
    public static boolean readBooleanField(final ObjectInputStream.GetField getField, final String s, final boolean b) {
        try {
            return getField.get(s, b);
        }
        catch (Exception ex) {
            getLogger().debug("Unable to read '%s' field in migration device with message (%s)", s, ex.getMessage());
            return b;
        }
    }
    
    public static int readIntField(final ObjectInputStream.GetField getField, final String s, final int n) {
        try {
            return getField.get(s, n);
        }
        catch (Exception ex) {
            getLogger().debug("Unable to read '%s' field in migration device with message (%s)", s, ex.getMessage());
            return n;
        }
    }
    
    public static long readLongField(final ObjectInputStream.GetField getField, final String s, final long n) {
        try {
            return getField.get(s, n);
        }
        catch (Exception ex) {
            getLogger().debug("Unable to read '%s' field in migration device with message (%s)", s, ex.getMessage());
            return n;
        }
    }
    
    public static <T> T readObject(Context ex, String cast, final String s, final Class<T> clazz) {
        final FileNotFoundException ex2 = null;
        final String s2 = null;
        final FileNotFoundException ex3 = null;
        final String s3 = null;
        final FileNotFoundException ex4 = null;
        Object o3 = null;
        try {
            final FileInputStream openFileInput = ((Context)ex).openFileInput(cast);
            cast = s2;
            Object o = openFileInput;
            ex = ex4;
            Object o2 = openFileInput;
            try {
                final InputStream in = new(java.io.BufferedInputStream.class);
                cast = s2;
                o = openFileInput;
                ex = ex4;
                o2 = openFileInput;
                new BufferedInputStream(openFileInput);
                try {
                    o3 = new ObjectInputStream(in);
                    ex = ex4;
                    o2 = o3;
                    Serializable s4 = null;
                    try {
                        cast = (String)clazz.cast(((ObjectInputStream)o3).readObject());
                        try {
                            getLogger().debug("Read %s: %s", s, cast);
                            ex = (FileNotFoundException)cast;
                        }
                        catch (FileNotFoundException ex) {
                            ex = (FileNotFoundException)o3;
                        }
                        catch (Exception in) {}
                        catch (ClassCastException in) {}
                        catch (ClassNotFoundException in) {
                            s4 = cast;
                        }
                    }
                    catch (Exception in) {}
                    catch (ClassCastException in) {}
                    catch (ClassNotFoundException in) {
                        s4 = ex2;
                    }
                    cast = (String)s4;
                    o = o3;
                    ex = (FileNotFoundException)s4;
                    o2 = o3;
                    getLogger().error("Failed to find %s class (%s)", s, ((Throwable)in).getMessage());
                    ex = (FileNotFoundException)s4;
                }
                catch (Exception ex5) {
                    cast = (String)in;
                    ex = ex3;
                }
                catch (FileNotFoundException ex) {
                    ex = (FileNotFoundException)in;
                    cast = s3;
                }
            }
            catch (Exception ex5) {
                ex = (FileNotFoundException)cast;
                cast = (String)o;
            }
            catch (FileNotFoundException ex6) {
                cast = (String)ex;
                ex = (FileNotFoundException)o2;
            }
        }
        catch (Exception ex7) {}
        catch (FileNotFoundException ex8) {}
        try {
            ((Closeable)o3).close();
            goto Label_0433;
        }
        catch (Exception ex9) {}
    }
    
    public static <T> T readObjectField(final ObjectInputStream.GetField getField, final String s, final T t) {
        try {
            return (T)getField.get(s, t);
        }
        catch (Exception ex) {
            getLogger().debug("Unable to read '%s' field in migration device with message (%s)", s, ex.getMessage());
            return t;
        }
    }
    
    public static String readStringField(final ObjectInputStream.GetField getField, final String s, final String s2) {
        return readObjectField(getField, s, s2);
    }
    
    public static String sha1(final String s) {
        return hash(s, "SHA-1");
    }
    
    public static <T> void writeObject(final T obj, Context openFileOutput, String s, final String s2) {
        Object o = null;
        Label_0135: {
            try {
                final Context out = openFileOutput = (Context)openFileOutput.openFileOutput(s, 0);
                try {
                    openFileOutput = out;
                    final BufferedOutputStream out2 = new BufferedOutputStream((OutputStream)out);
                    try {
                        s = (String)(openFileOutput = (Context)new ObjectOutputStream(out2));
                        try {
                            ((ObjectOutputStream)s).writeObject(obj);
                            openFileOutput = (Context)s;
                            getLogger().debug("Wrote %s: %s", s2, obj);
                            o = s;
                            break Label_0135;
                        }
                        catch (NotSerializableException ex3) {
                            openFileOutput = (Context)s;
                            getLogger().error("Failed to serialize %s", s2);
                            o = s;
                            break Label_0135;
                        }
                    }
                    catch (Exception ex) {
                        o = out2;
                    }
                }
                catch (Exception ex) {
                    o = openFileOutput;
                }
            }
            catch (Exception ex) {
                o = null;
            }
            final Exception ex;
            getLogger().error("Failed to open %s for writing (%s)", s2, ex);
        }
        if (o != null) {
            try {
                ((Closeable)o).close();
            }
            catch (Exception ex2) {
                getLogger().error("Failed to close %s file for writing (%s)", s2, ex2);
            }
        }
    }
}
