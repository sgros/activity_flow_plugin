// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.source.MediaSource;

final class MediaPeriodInfo
{
    public final long contentPositionUs;
    public final long durationUs;
    public final MediaSource.MediaPeriodId id;
    public final boolean isFinal;
    public final boolean isLastInTimelinePeriod;
    public final long startPositionUs;
    
    MediaPeriodInfo(final MediaSource.MediaPeriodId id, final long startPositionUs, final long contentPositionUs, final long durationUs, final boolean isLastInTimelinePeriod, final boolean isFinal) {
        this.id = id;
        this.startPositionUs = startPositionUs;
        this.contentPositionUs = contentPositionUs;
        this.durationUs = durationUs;
        this.isLastInTimelinePeriod = isLastInTimelinePeriod;
        this.isFinal = isFinal;
    }
    
    public MediaPeriodInfo copyWithStartPositionUs(final long n) {
        return new MediaPeriodInfo(this.id, n, this.contentPositionUs, this.durationUs, this.isLastInTimelinePeriod, this.isFinal);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && MediaPeriodInfo.class == o.getClass()) {
            final MediaPeriodInfo mediaPeriodInfo = (MediaPeriodInfo)o;
            if (this.startPositionUs != mediaPeriodInfo.startPositionUs || this.contentPositionUs != mediaPeriodInfo.contentPositionUs || this.durationUs != mediaPeriodInfo.durationUs || this.isLastInTimelinePeriod != mediaPeriodInfo.isLastInTimelinePeriod || this.isFinal != mediaPeriodInfo.isFinal || !Util.areEqual(this.id, mediaPeriodInfo.id)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (((((527 + this.id.hashCode()) * 31 + (int)this.startPositionUs) * 31 + (int)this.contentPositionUs) * 31 + (int)this.durationUs) * 31 + (this.isLastInTimelinePeriod ? 1 : 0)) * 31 + (this.isFinal ? 1 : 0);
    }
}
