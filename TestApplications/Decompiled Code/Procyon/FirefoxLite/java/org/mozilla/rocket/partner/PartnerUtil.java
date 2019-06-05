// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.partner;

import java.io.Serializable;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import android.provider.Settings$Secure;
import android.content.ContentResolver;
import android.util.Log;

final class PartnerUtil
{
    private static final String LOG_TAG = "PartnerUtil";
    static boolean propNA = false;
    
    static boolean debugMock() {
        return Log.isLoggable("vendor.partner.mock", 3);
    }
    
    static String getDeviceIdentifier(final ContentResolver contentResolver) {
        return Settings$Secure.getString(contentResolver, "android_id");
    }
    
    private static String getPropertiesFromProcess(String t) throws Exception {
        final Serializable s = "";
        final Process start = new ProcessBuilder(new String[] { "/system/bin/getprop", (String)t }).redirectErrorStream(false).start();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(start.getInputStream(), StandardCharsets.UTF_8));
        t = (Throwable)s;
        try {
            while (true) {
                final Serializable line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                t = (Throwable)line;
            }
            bufferedReader.close();
            start.destroy();
            return (String)t;
        }
        catch (Throwable t) {
            try {
                throw t;
            }
            finally {}
        }
        finally {
            t = null;
        }
        if (t != null) {
            try {
                bufferedReader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
        }
        else {
            bufferedReader.close();
        }
        throw;
    }
    
    private static String getPropertiesFromSystem(final String s) throws Exception {
        return (String)Class.forName("android.os.SystemProperties").getDeclaredMethod("get", String.class).invoke(null, s);
    }
    
    static String getProperty(String propertiesFromProcess) {
        if (PartnerUtil.propNA) {
            return null;
        }
        if (debugMock() && propertiesFromProcess.equals("ro.vendor.partner")) {
            return "moz/1/DEVCFA";
        }
        try {
            return getPropertiesFromSystem(propertiesFromProcess);
        }
        catch (Exception ex) {
            try {
                propertiesFromProcess = getPropertiesFromProcess(propertiesFromProcess);
                return propertiesFromProcess;
            }
            catch (Exception ex2) {
                PartnerUtil.propNA = true;
                return null;
            }
        }
    }
    
    static int log(final String s) {
        return log(null, s);
    }
    
    static int log(final Throwable t, final String s) {
        if (!Log.isLoggable("vendor.partner", 3)) {
            return 0;
        }
        if (t != null) {
            return Log.d(PartnerUtil.LOG_TAG, s, t);
        }
        return Log.d(PartnerUtil.LOG_TAG, s);
    }
}
