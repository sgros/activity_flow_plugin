package com.airbnb.lottie;

import android.support.p001v4.p003os.TraceCompat;
import android.util.Log;
import java.util.HashSet;
import java.util.Set;

/* renamed from: com.airbnb.lottie.L */
public class C0352L {
    public static boolean DBG = false;
    private static int depthPastMaxDepth = 0;
    private static final Set<String> loggedMessages = new HashSet();
    private static String[] sections;
    private static long[] startTimeNs;
    private static int traceDepth = 0;
    private static boolean traceEnabled = false;

    public static void debug(String str) {
        if (DBG) {
            Log.d("LOTTIE", str);
        }
    }

    public static void warn(String str) {
        if (!loggedMessages.contains(str)) {
            Log.w("LOTTIE", str);
            loggedMessages.add(str);
        }
    }

    public static void beginSection(String str) {
        if (!traceEnabled) {
            return;
        }
        if (traceDepth == 20) {
            depthPastMaxDepth++;
            return;
        }
        sections[traceDepth] = str;
        startTimeNs[traceDepth] = System.nanoTime();
        TraceCompat.beginSection(str);
        traceDepth++;
    }

    public static float endSection(String str) {
        if (depthPastMaxDepth > 0) {
            depthPastMaxDepth--;
            return 0.0f;
        } else if (!traceEnabled) {
            return 0.0f;
        } else {
            traceDepth--;
            if (traceDepth == -1) {
                throw new IllegalStateException("Can't end trace section. There are none.");
            } else if (str.equals(sections[traceDepth])) {
                TraceCompat.endSection();
                return ((float) (System.nanoTime() - startTimeNs[traceDepth])) / 1000000.0f;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unbalanced trace call ");
                stringBuilder.append(str);
                stringBuilder.append(". Expected ");
                stringBuilder.append(sections[traceDepth]);
                stringBuilder.append(".");
                throw new IllegalStateException(stringBuilder.toString());
            }
        }
    }
}
