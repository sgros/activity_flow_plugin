// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.upstream.TransferListener;
import java.io.IOException;
import com.google.android.exoplayer2.upstream.Allocator;
import android.os.Handler;

public interface MediaSource
{
    void addEventListener(final Handler p0, final MediaSourceEventListener p1);
    
    MediaPeriod createPeriod(final MediaPeriodId p0, final Allocator p1, final long p2);
    
    void maybeThrowSourceInfoRefreshError() throws IOException;
    
    void prepareSource(final SourceInfoRefreshListener p0, final TransferListener p1);
    
    void releasePeriod(final MediaPeriod p0);
    
    void releaseSource(final SourceInfoRefreshListener p0);
    
    void removeEventListener(final MediaSourceEventListener p0);
    
    public static final class MediaPeriodId
    {
        public final int adGroupIndex;
        public final int adIndexInAdGroup;
        public final long endPositionUs;
        public final Object periodUid;
        public final long windowSequenceNumber;
        
        public MediaPeriodId(final Object o) {
            this(o, -1L);
        }
        
        public MediaPeriodId(final Object o, final int n, final int n2, final long n3) {
            this(o, n, n2, n3, -9223372036854775807L);
        }
        
        private MediaPeriodId(final Object periodUid, final int adGroupIndex, final int adIndexInAdGroup, final long windowSequenceNumber, final long endPositionUs) {
            this.periodUid = periodUid;
            this.adGroupIndex = adGroupIndex;
            this.adIndexInAdGroup = adIndexInAdGroup;
            this.windowSequenceNumber = windowSequenceNumber;
            this.endPositionUs = endPositionUs;
        }
        
        public MediaPeriodId(final Object o, final long n) {
            this(o, -1, -1, n, -9223372036854775807L);
        }
        
        public MediaPeriodId(final Object o, final long n, final long n2) {
            this(o, -1, -1, n, n2);
        }
        
        public MediaPeriodId copyWithPeriodUid(final Object obj) {
            MediaPeriodId mediaPeriodId;
            if (this.periodUid.equals(obj)) {
                mediaPeriodId = this;
            }
            else {
                mediaPeriodId = new MediaPeriodId(obj, this.adGroupIndex, this.adIndexInAdGroup, this.windowSequenceNumber, this.endPositionUs);
            }
            return mediaPeriodId;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (o != null && MediaPeriodId.class == o.getClass()) {
                final MediaPeriodId mediaPeriodId = (MediaPeriodId)o;
                if (!this.periodUid.equals(mediaPeriodId.periodUid) || this.adGroupIndex != mediaPeriodId.adGroupIndex || this.adIndexInAdGroup != mediaPeriodId.adIndexInAdGroup || this.windowSequenceNumber != mediaPeriodId.windowSequenceNumber || this.endPositionUs != mediaPeriodId.endPositionUs) {
                    b = false;
                }
                return b;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return ((((527 + this.periodUid.hashCode()) * 31 + this.adGroupIndex) * 31 + this.adIndexInAdGroup) * 31 + (int)this.windowSequenceNumber) * 31 + (int)this.endPositionUs;
        }
        
        public boolean isAd() {
            return this.adGroupIndex != -1;
        }
    }
    
    public interface SourceInfoRefreshListener
    {
        void onSourceInfoRefreshed(final MediaSource p0, final Timeline p1, final Object p2);
    }
}
