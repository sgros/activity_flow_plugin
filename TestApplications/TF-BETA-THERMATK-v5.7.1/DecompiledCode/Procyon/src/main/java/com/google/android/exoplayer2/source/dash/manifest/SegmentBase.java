// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.Format;
import java.util.List;
import com.google.android.exoplayer2.util.Util;

public abstract class SegmentBase
{
    final RangedUri initialization;
    final long presentationTimeOffset;
    final long timescale;
    
    public SegmentBase(final RangedUri initialization, final long timescale, final long presentationTimeOffset) {
        this.initialization = initialization;
        this.timescale = timescale;
        this.presentationTimeOffset = presentationTimeOffset;
    }
    
    public RangedUri getInitialization(final Representation representation) {
        return this.initialization;
    }
    
    public long getPresentationTimeOffsetUs() {
        return Util.scaleLargeTimestamp(this.presentationTimeOffset, 1000000L, this.timescale);
    }
    
    public abstract static class MultiSegmentBase extends SegmentBase
    {
        final long duration;
        final List<SegmentTimelineElement> segmentTimeline;
        final long startNumber;
        
        public MultiSegmentBase(final RangedUri rangedUri, final long n, final long n2, final long startNumber, final long duration, final List<SegmentTimelineElement> segmentTimeline) {
            super(rangedUri, n, n2);
            this.startNumber = startNumber;
            this.duration = duration;
            this.segmentTimeline = segmentTimeline;
        }
        
        public long getFirstSegmentNum() {
            return this.startNumber;
        }
        
        public abstract int getSegmentCount(final long p0);
        
        public final long getSegmentDurationUs(long n, final long n2) {
            final List<SegmentTimelineElement> segmentTimeline = this.segmentTimeline;
            if (segmentTimeline != null) {
                return segmentTimeline.get((int)(n - this.startNumber)).duration * 1000000L / super.timescale;
            }
            final int segmentCount = this.getSegmentCount(n2);
            if (segmentCount != -1 && n == this.getFirstSegmentNum() + segmentCount - 1L) {
                n = n2 - this.getSegmentTimeUs(n);
            }
            else {
                n = this.duration * 1000000L / super.timescale;
            }
            return n;
        }
        
        public long getSegmentNum(long min, long n) {
            final long firstSegmentNum = this.getFirstSegmentNum();
            n = this.getSegmentCount(n);
            if (n == 0L) {
                return firstSegmentNum;
            }
            if (this.segmentTimeline == null) {
                min = min / (this.duration * 1000000L / super.timescale) + this.startNumber;
                if (min < firstSegmentNum) {
                    min = firstSegmentNum;
                }
                else if (n != -1L) {
                    min = Math.min(min, firstSegmentNum + n - 1L);
                }
                return min;
            }
            long n2 = n + firstSegmentNum - 1L;
            n = firstSegmentNum;
            while (n <= n2) {
                final long n3 = (n2 - n) / 2L + n;
                final long segmentTimeUs = this.getSegmentTimeUs(n3);
                if (segmentTimeUs < min) {
                    n = n3 + 1L;
                }
                else {
                    if (segmentTimeUs <= min) {
                        return n3;
                    }
                    n2 = n3 - 1L;
                }
            }
            if (n != firstSegmentNum) {
                n = n2;
            }
            return n;
        }
        
        public final long getSegmentTimeUs(long n) {
            final List<SegmentTimelineElement> segmentTimeline = this.segmentTimeline;
            if (segmentTimeline != null) {
                n = segmentTimeline.get((int)(n - this.startNumber)).startTime - super.presentationTimeOffset;
            }
            else {
                n = (n - this.startNumber) * this.duration;
            }
            return Util.scaleLargeTimestamp(n, 1000000L, super.timescale);
        }
        
        public abstract RangedUri getSegmentUrl(final Representation p0, final long p1);
        
        public boolean isExplicit() {
            return this.segmentTimeline != null;
        }
    }
    
    public static class SegmentList extends MultiSegmentBase
    {
        final List<RangedUri> mediaSegments;
        
        public SegmentList(final RangedUri rangedUri, final long n, final long n2, final long n3, final long n4, final List<SegmentTimelineElement> list, final List<RangedUri> mediaSegments) {
            super(rangedUri, n, n2, n3, n4, list);
            this.mediaSegments = mediaSegments;
        }
        
        @Override
        public int getSegmentCount(final long n) {
            return this.mediaSegments.size();
        }
        
        @Override
        public RangedUri getSegmentUrl(final Representation representation, final long n) {
            return this.mediaSegments.get((int)(n - super.startNumber));
        }
        
        @Override
        public boolean isExplicit() {
            return true;
        }
    }
    
    public static class SegmentTemplate extends MultiSegmentBase
    {
        final UrlTemplate initializationTemplate;
        final UrlTemplate mediaTemplate;
        
        public SegmentTemplate(final RangedUri rangedUri, final long n, final long n2, final long n3, final long n4, final List<SegmentTimelineElement> list, final UrlTemplate initializationTemplate, final UrlTemplate mediaTemplate) {
            super(rangedUri, n, n2, n3, n4, list);
            this.initializationTemplate = initializationTemplate;
            this.mediaTemplate = mediaTemplate;
        }
        
        @Override
        public RangedUri getInitialization(final Representation representation) {
            final UrlTemplate initializationTemplate = this.initializationTemplate;
            if (initializationTemplate != null) {
                final Format format = representation.format;
                return new RangedUri(initializationTemplate.buildUri(format.id, 0L, format.bitrate, 0L), 0L, -1L);
            }
            return super.getInitialization(representation);
        }
        
        @Override
        public int getSegmentCount(final long n) {
            final List<SegmentTimelineElement> segmentTimeline = (List<SegmentTimelineElement>)super.segmentTimeline;
            if (segmentTimeline != null) {
                return segmentTimeline.size();
            }
            if (n != -9223372036854775807L) {
                return (int)Util.ceilDivide(n, super.duration * 1000000L / super.timescale);
            }
            return -1;
        }
        
        @Override
        public RangedUri getSegmentUrl(final Representation representation, final long n) {
            final List<SegmentTimelineElement> segmentTimeline = (List<SegmentTimelineElement>)super.segmentTimeline;
            long startTime;
            if (segmentTimeline != null) {
                startTime = segmentTimeline.get((int)(n - super.startNumber)).startTime;
            }
            else {
                startTime = (n - super.startNumber) * super.duration;
            }
            final UrlTemplate mediaTemplate = this.mediaTemplate;
            final Format format = representation.format;
            return new RangedUri(mediaTemplate.buildUri(format.id, n, format.bitrate, startTime), 0L, -1L);
        }
    }
    
    public static class SegmentTimelineElement
    {
        final long duration;
        final long startTime;
        
        public SegmentTimelineElement(final long startTime, final long duration) {
            this.startTime = startTime;
            this.duration = duration;
        }
    }
    
    public static class SingleSegmentBase extends SegmentBase
    {
        final long indexLength;
        final long indexStart;
        
        public SingleSegmentBase() {
            this(null, 1L, 0L, 0L, 0L);
        }
        
        public SingleSegmentBase(final RangedUri rangedUri, final long n, final long n2, final long indexStart, final long indexLength) {
            super(rangedUri, n, n2);
            this.indexStart = indexStart;
            this.indexLength = indexLength;
        }
        
        public RangedUri getIndex() {
            final long indexLength = this.indexLength;
            RangedUri rangedUri;
            if (indexLength <= 0L) {
                rangedUri = null;
            }
            else {
                rangedUri = new RangedUri(null, this.indexStart, indexLength);
            }
            return rangedUri;
        }
    }
}
