// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.drm;

import android.util.Pair;
import java.util.Map;

public final class WidevineUtil
{
    private static long getDurationRemainingSec(final Map<String, String> map, final String s) {
        if (map == null) {
            return -9223372036854775807L;
        }
        try {
            final String s2 = map.get(s);
            if (s2 != null) {
                return Long.parseLong(s2);
            }
            return -9223372036854775807L;
        }
        catch (NumberFormatException ex) {
            return -9223372036854775807L;
        }
    }
    
    public static Pair<Long, Long> getLicenseDurationRemainingSec(final DrmSession<?> drmSession) {
        final Map queryKeyStatus = drmSession.queryKeyStatus();
        if (queryKeyStatus == null) {
            return null;
        }
        return (Pair<Long, Long>)new Pair((Object)getDurationRemainingSec(queryKeyStatus, "LicenseDurationRemaining"), (Object)getDurationRemainingSec(queryKeyStatus, "PlaybackDurationRemaining"));
    }
}
