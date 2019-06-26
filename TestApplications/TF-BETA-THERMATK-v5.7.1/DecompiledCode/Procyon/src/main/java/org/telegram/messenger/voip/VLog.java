// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.voip;

import java.io.Writer;
import java.io.PrintWriter;
import android.text.TextUtils;
import java.io.StringWriter;

class VLog
{
    public static native void d(final String p0);
    
    public static native void e(final String p0);
    
    public static void e(final String csq, final Throwable t) {
        final StringWriter out = new StringWriter();
        if (!TextUtils.isEmpty((CharSequence)csq)) {
            out.append(csq);
            out.append(": ");
        }
        t.printStackTrace(new PrintWriter(out));
        final String[] split = out.toString().split("\n");
        for (int length = split.length, i = 0; i < length; ++i) {
            e(split[i]);
        }
    }
    
    public static void e(final Throwable t) {
        e(null, t);
    }
    
    public static native void i(final String p0);
    
    public static native void v(final String p0);
    
    public static native void w(final String p0);
}
