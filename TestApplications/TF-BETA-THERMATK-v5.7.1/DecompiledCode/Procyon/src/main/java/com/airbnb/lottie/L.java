// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import androidx.core.os.TraceCompat;

public class L
{
    public static boolean DBG = false;
    private static int depthPastMaxDepth = 0;
    private static String[] sections;
    private static long[] startTimeNs;
    private static int traceDepth = 0;
    private static boolean traceEnabled = false;
    
    public static void beginSection(final String s) {
        if (!L.traceEnabled) {
            return;
        }
        final int traceDepth = L.traceDepth;
        if (traceDepth == 20) {
            ++L.depthPastMaxDepth;
            return;
        }
        L.sections[traceDepth] = s;
        L.startTimeNs[traceDepth] = System.nanoTime();
        TraceCompat.beginSection(s);
        ++L.traceDepth;
    }
    
    public static float endSection(final String str) {
        final int depthPastMaxDepth = L.depthPastMaxDepth;
        if (depthPastMaxDepth > 0) {
            L.depthPastMaxDepth = depthPastMaxDepth - 1;
            return 0.0f;
        }
        if (!L.traceEnabled) {
            return 0.0f;
        }
        --L.traceDepth;
        final int traceDepth = L.traceDepth;
        if (traceDepth == -1) {
            throw new IllegalStateException("Can't end trace section. There are none.");
        }
        if (str.equals(L.sections[traceDepth])) {
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
}
