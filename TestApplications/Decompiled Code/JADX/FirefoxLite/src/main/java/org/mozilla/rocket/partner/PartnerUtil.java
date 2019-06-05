package org.mozilla.rocket.partner;

import android.content.ContentResolver;
import android.provider.Settings.Secure;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

final class PartnerUtil {
    private static final String LOG_TAG = "PartnerUtil";
    static boolean propNA = false;

    PartnerUtil() {
    }

    static boolean debugMock() {
        return Log.isLoggable("vendor.partner.mock", 3);
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x001c */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:12|13|14) */
    /* JADX WARNING: Missing block: B:14:0x0020, code skipped:
            return getPropertiesFromProcess(r2);
     */
    /* JADX WARNING: Missing block: B:15:0x0021, code skipped:
            propNA = true;
     */
    /* JADX WARNING: Missing block: B:17:0x0024, code skipped:
            return null;
     */
    static java.lang.String getProperty(java.lang.String r2) {
        /*
        r0 = propNA;
        r1 = 0;
        if (r0 == 0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        r0 = debugMock();
        if (r0 == 0) goto L_0x0017;
    L_0x000c:
        r0 = "ro.vendor.partner";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0017;
    L_0x0014:
        r2 = "moz/1/DEVCFA";
        return r2;
    L_0x0017:
        r0 = getPropertiesFromSystem(r2);	 Catch:{ Exception -> 0x001c }
        return r0;
    L_0x001c:
        r2 = getPropertiesFromProcess(r2);	 Catch:{ Exception -> 0x0021 }
        return r2;
    L_0x0021:
        r2 = 1;
        propNA = r2;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.rocket.partner.PartnerUtil.getProperty(java.lang.String):java.lang.String");
    }

    private static String getPropertiesFromSystem(String str) throws Exception {
        return (String) Class.forName("android.os.SystemProperties").getDeclaredMethod("get", new Class[]{String.class}).invoke(null, new Object[]{str});
    }

    private static String getPropertiesFromProcess(String str) throws Exception {
        Throwable th;
        Throwable th2;
        String str2 = "";
        Process start = new ProcessBuilder(new String[]{"/system/bin/getprop", str}).redirectErrorStream(false).start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(start.getInputStream(), StandardCharsets.UTF_8));
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    str2 = readLine;
                } else {
                    bufferedReader.close();
                    start.destroy();
                    return str2;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
        throw th;
        if (th2 != null) {
            try {
                bufferedReader.close();
            } catch (Throwable th4) {
                th2.addSuppressed(th4);
            }
        } else {
            bufferedReader.close();
        }
        throw th;
    }

    static String getDeviceIdentifier(ContentResolver contentResolver) {
        return Secure.getString(contentResolver, "android_id");
    }

    static int log(String str) {
        return log(null, str);
    }

    static int log(Throwable th, String str) {
        if (!Log.isLoggable("vendor.partner", 3)) {
            return 0;
        }
        if (th != null) {
            return Log.d(LOG_TAG, str, th);
        }
        return Log.d(LOG_TAG, str);
    }
}
