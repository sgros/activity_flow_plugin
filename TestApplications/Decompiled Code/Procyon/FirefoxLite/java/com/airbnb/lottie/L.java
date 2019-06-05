// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import android.util.Log;
import android.support.v4.os.TraceCompat;
import java.util.HashSet;
import java.util.Set;

public class L
{
    public static boolean DBG = false;
    private static int depthPastMaxDepth;
    private static final Set<String> loggedMessages;
    private static String[] sections;
    private static long[] startTimeNs;
    private static int traceDepth;
    private static boolean traceEnabled;
    
    static {
        loggedMessages = new HashSet<String>();
        L.traceEnabled = false;
        L.traceDepth = 0;
        L.depthPastMaxDepth = 0;
    }
    
    public static void beginSection(final String s) {
        if (!L.traceEnabled) {
            return;
        }
        if (L.traceDepth == 20) {
            ++L.depthPastMaxDepth;
            return;
        }
        L.sections[L.traceDepth] = s;
        L.startTimeNs[L.traceDepth] = System.nanoTime();
        TraceCompat.beginSection(s);
        ++L.traceDepth;
    }
    
    public static void debug(final String s) {
        if (L.DBG) {
            Log.d("LOTTIE", s);
        }
    }
    
    public static float endSection(final String str) {
        if (L.depthPastMaxDepth > 0) {
            --L.depthPastMaxDepth;
            return 0.0f;
        }
        if (!L.traceEnabled) {
            return 0.0f;
        }
        --L.traceDepth;
        if (L.traceDepth == -1) {
            throw new IllegalStateException("Can't end trace section. There are none.");
        }
        if (str.equals(L.sections[L.traceDepth])) {
            TraceCompat.endSection();
            return (System.nanoTime() - L.startTimeNs[L.traceDepth]) / 1000000.0f;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unbalanced trace call ");
        sb.append(str);
        sb.append(". Expected ");
        sb.append(L.sections[L.traceDepth]);
        sb.append(".");
        throw new IllegalStateException(sb.toString());
    }
    
    public static void warn(final String s) {
        if (L.loggedMessages.contains(s)) {
            return;
        }
        Log.w("LOTTIE", s);
        L.loggedMessages.add(s);
    }
}
