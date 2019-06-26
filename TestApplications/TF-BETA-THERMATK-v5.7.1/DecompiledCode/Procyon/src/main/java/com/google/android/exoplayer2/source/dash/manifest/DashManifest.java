// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.C;
import java.util.Collections;
import java.util.List;
import android.net.Uri;
import com.google.android.exoplayer2.offline.FilterableManifest;

public class DashManifest implements FilterableManifest<DashManifest>
{
    public final long availabilityStartTimeMs;
    public final long durationMs;
    public final boolean dynamic;
    public final Uri location;
    public final long minBufferTimeMs;
    public final long minUpdatePeriodMs;
    private final List<Period> periods;
    public final ProgramInformation programInformation;
    public final long publishTimeMs;
    public final long suggestedPresentationDelayMs;
    public final long timeShiftBufferDepthMs;
    public final UtcTimingElement utcTiming;
    
    public DashManifest(final long availabilityStartTimeMs, final long durationMs, final long minBufferTimeMs, final boolean dynamic, final long minUpdatePeriodMs, final long timeShiftBufferDepthMs, final long suggestedPresentationDelayMs, final long publishTimeMs, final ProgramInformation programInformation, final UtcTimingElement utcTiming, final Uri location, List<Period> emptyList) {
        this.availabilityStartTimeMs = availabilityStartTimeMs;
        this.durationMs = durationMs;
        this.minBufferTimeMs = minBufferTimeMs;
        this.dynamic = dynamic;
        this.minUpdatePeriodMs = minUpdatePeriodMs;
        this.timeShiftBufferDepthMs = timeShiftBufferDepthMs;
        this.suggestedPresentationDelayMs = suggestedPresentationDelayMs;
        this.publishTimeMs = publishTimeMs;
        this.programInformation = programInformation;
        this.utcTiming = utcTiming;
        this.location = location;
        if (emptyList == null) {
            emptyList = Collections.emptyList();
        }
        this.periods = emptyList;
    }
    
    public final Period getPeriod(final int n) {
        return this.periods.get(n);
    }
    
    public final int getPeriodCount() {
        return this.periods.size();
    }
    
    public final long getPeriodDurationMs(final int n) {
        long n2;
        if (n == this.periods.size() - 1) {
            final long durationMs = this.durationMs;
            if (durationMs == -9223372036854775807L) {
                n2 = -9223372036854775807L;
            }
            else {
                n2 = durationMs - this.periods.get(n).startMs;
            }
        }
        else {
            n2 = this.periods.get(n + 1).startMs - this.periods.get(n).startMs;
        }
        return n2;
    }
    
    public final long getPeriodDurationUs(final int n) {
        return C.msToUs(this.getPeriodDurationMs(n));
    }
}
