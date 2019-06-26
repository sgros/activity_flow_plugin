// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls.playlist;

import java.util.Collections;
import java.util.List;
import com.google.android.exoplayer2.drm.DrmInitData;

public final class HlsMediaPlaylist extends HlsPlaylist
{
    public final int discontinuitySequence;
    public final long durationUs;
    public final boolean hasDiscontinuitySequence;
    public final boolean hasEndTag;
    public final boolean hasProgramDateTime;
    public final long mediaSequence;
    public final int playlistType;
    public final DrmInitData protectionSchemes;
    public final List<Segment> segments;
    public final long startOffsetUs;
    public final long startTimeUs;
    public final long targetDurationUs;
    public final int version;
    
    public HlsMediaPlaylist(final int playlistType, final String s, final List<String> list, long startOffsetUs, final long startTimeUs, final boolean hasDiscontinuitySequence, final int discontinuitySequence, final long mediaSequence, final int version, final long targetDurationUs, final boolean b, final boolean hasEndTag, final boolean hasProgramDateTime, final DrmInitData protectionSchemes, final List<Segment> list2) {
        super(s, list, b);
        this.playlistType = playlistType;
        this.startTimeUs = startTimeUs;
        this.hasDiscontinuitySequence = hasDiscontinuitySequence;
        this.discontinuitySequence = discontinuitySequence;
        this.mediaSequence = mediaSequence;
        this.version = version;
        this.targetDurationUs = targetDurationUs;
        this.hasEndTag = hasEndTag;
        this.hasProgramDateTime = hasProgramDateTime;
        this.protectionSchemes = protectionSchemes;
        this.segments = Collections.unmodifiableList((List<? extends Segment>)list2);
        if (!list2.isEmpty()) {
            final Segment segment = list2.get(list2.size() - 1);
            this.durationUs = segment.relativeStartTimeUs + segment.durationUs;
        }
        else {
            this.durationUs = 0L;
        }
        if (startOffsetUs == -9223372036854775807L) {
            startOffsetUs = -9223372036854775807L;
        }
        else if (startOffsetUs < 0L) {
            startOffsetUs += this.durationUs;
        }
        this.startOffsetUs = startOffsetUs;
    }
    
    public HlsMediaPlaylist copyWith(final long n, final int n2) {
        return new HlsMediaPlaylist(this.playlistType, super.baseUri, super.tags, this.startOffsetUs, n, true, n2, this.mediaSequence, this.version, this.targetDurationUs, super.hasIndependentSegments, this.hasEndTag, this.hasProgramDateTime, this.protectionSchemes, this.segments);
    }
    
    public HlsMediaPlaylist copyWithEndTag() {
        if (this.hasEndTag) {
            return this;
        }
        return new HlsMediaPlaylist(this.playlistType, super.baseUri, super.tags, this.startOffsetUs, this.startTimeUs, this.hasDiscontinuitySequence, this.discontinuitySequence, this.mediaSequence, this.version, this.targetDurationUs, super.hasIndependentSegments, true, this.hasProgramDateTime, this.protectionSchemes, this.segments);
    }
    
    public long getEndTimeUs() {
        return this.startTimeUs + this.durationUs;
    }
    
    public boolean isNewerThan(final HlsMediaPlaylist hlsMediaPlaylist) {
        boolean b2;
        final boolean b = b2 = true;
        if (hlsMediaPlaylist != null) {
            final long mediaSequence = this.mediaSequence;
            final long mediaSequence2 = hlsMediaPlaylist.mediaSequence;
            if (mediaSequence > mediaSequence2) {
                b2 = b;
            }
            else {
                if (mediaSequence < mediaSequence2) {
                    return false;
                }
                final int size = this.segments.size();
                final int size2 = hlsMediaPlaylist.segments.size();
                b2 = b;
                if (size <= size2) {
                    b2 = (size == size2 && this.hasEndTag && !hlsMediaPlaylist.hasEndTag && b);
                }
            }
        }
        return b2;
    }
    
    public static final class Segment implements Comparable<Long>
    {
        public final long byterangeLength;
        public final long byterangeOffset;
        public final DrmInitData drmInitData;
        public final long durationUs;
        public final String encryptionIV;
        public final String fullSegmentEncryptionKeyUri;
        public final boolean hasGapTag;
        public final Segment initializationSegment;
        public final int relativeDiscontinuitySequence;
        public final long relativeStartTimeUs;
        public final String title;
        public final String url;
        
        public Segment(final String s, final long n, final long n2) {
            this(s, null, "", 0L, -1, -9223372036854775807L, null, null, null, n, n2, false);
        }
        
        public Segment(final String url, final Segment initializationSegment, final String title, final long durationUs, final int relativeDiscontinuitySequence, final long relativeStartTimeUs, final DrmInitData drmInitData, final String fullSegmentEncryptionKeyUri, final String encryptionIV, final long byterangeOffset, final long byterangeLength, final boolean hasGapTag) {
            this.url = url;
            this.initializationSegment = initializationSegment;
            this.title = title;
            this.durationUs = durationUs;
            this.relativeDiscontinuitySequence = relativeDiscontinuitySequence;
            this.relativeStartTimeUs = relativeStartTimeUs;
            this.drmInitData = drmInitData;
            this.fullSegmentEncryptionKeyUri = fullSegmentEncryptionKeyUri;
            this.encryptionIV = encryptionIV;
            this.byterangeOffset = byterangeOffset;
            this.byterangeLength = byterangeLength;
            this.hasGapTag = hasGapTag;
        }
        
        @Override
        public int compareTo(final Long n) {
            int n2;
            if (this.relativeStartTimeUs > n) {
                n2 = 1;
            }
            else if (this.relativeStartTimeUs < n) {
                n2 = -1;
            }
            else {
                n2 = 0;
            }
            return n2;
        }
    }
}
