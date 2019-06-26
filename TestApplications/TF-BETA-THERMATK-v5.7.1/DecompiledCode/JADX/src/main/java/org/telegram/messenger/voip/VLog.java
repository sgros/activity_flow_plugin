package org.telegram.messenger.voip;

import android.text.TextUtils;
import java.io.PrintWriter;
import java.io.StringWriter;

class VLog {
    /* renamed from: d */
    public static native void m35d(String str);

    /* renamed from: e */
    public static native void m36e(String str);

    /* renamed from: i */
    public static native void m39i(String str);

    /* renamed from: v */
    public static native void m40v(String str);

    /* renamed from: w */
    public static native void m41w(String str);

    VLog() {
    }

    /* renamed from: e */
    public static void m38e(Throwable th) {
        m37e(null, th);
    }

    /* renamed from: e */
    public static void m37e(String str, Throwable th) {
        StringWriter stringWriter = new StringWriter();
        if (!TextUtils.isEmpty(str)) {
            stringWriter.append(str);
            stringWriter.append(": ");
        }
        th.printStackTrace(new PrintWriter(stringWriter));
        for (String e : stringWriter.toString().split("\n")) {
            m36e(e);
        }
    }
}
