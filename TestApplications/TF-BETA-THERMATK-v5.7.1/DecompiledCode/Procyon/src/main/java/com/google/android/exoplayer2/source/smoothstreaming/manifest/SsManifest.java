// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Assertions;
import android.net.Uri;
import com.google.android.exoplayer2.Format;
import java.util.List;
import java.util.UUID;
import com.google.android.exoplayer2.extractor.mp4.TrackEncryptionBox;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.offline.FilterableManifest;

public class SsManifest implements FilterableManifest<SsManifest>
{
    public final long durationUs;
    public final long dvrWindowLengthUs;
    public final boolean isLive;
    public final int lookAheadCount;
    public final int majorVersion;
    public final int minorVersion;
    public final ProtectionElement protectionElement;
    public final StreamElement[] streamElements;
    
    private SsManifest(final int majorVersion, final int minorVersion, final long durationUs, final long dvrWindowLengthUs, final int lookAheadCount, final boolean isLive, final ProtectionElement protectionElement, final StreamElement[] streamElements) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.durationUs = durationUs;
        this.dvrWindowLengthUs = dvrWindowLengthUs;
        this.lookAheadCount = lookAheadCount;
        this.isLive = isLive;
        this.protectionElement = protectionElement;
        this.streamElements = streamElements;
    }
    
    public SsManifest(final int n, final int n2, long scaleLargeTimestamp, long scaleLargeTimestamp2, final long n3, final int n4, final boolean b, final ProtectionElement protectionElement, final StreamElement[] array) {
        final long n5 = -9223372036854775807L;
        if (scaleLargeTimestamp2 == 0L) {
            scaleLargeTimestamp2 = -9223372036854775807L;
        }
        else {
            scaleLargeTimestamp2 = Util.scaleLargeTimestamp(scaleLargeTimestamp2, 1000000L, scaleLargeTimestamp);
        }
        if (n3 == 0L) {
            scaleLargeTimestamp = n5;
        }
        else {
            scaleLargeTimestamp = Util.scaleLargeTimestamp(n3, 1000000L, scaleLargeTimestamp);
        }
        this(n, n2, scaleLargeTimestamp2, scaleLargeTimestamp, n4, b, protectionElement, array);
    }
    
    public static class ProtectionElement
    {
        public final byte[] data;
        public final TrackEncryptionBox[] trackEncryptionBoxes;
        public final UUID uuid;
        
        public ProtectionElement(final UUID uuid, final byte[] data, final TrackEncryptionBox[] trackEncryptionBoxes) {
            this.uuid = uuid;
            this.data = data;
            this.trackEncryptionBoxes = trackEncryptionBoxes;
        }
    }
    
    public static class StreamElement
    {
        private final String baseUri;
        public final int chunkCount;
        private final List<Long> chunkStartTimes;
        private final long[] chunkStartTimesUs;
        private final String chunkTemplate;
        public final int displayHeight;
        public final int displayWidth;
        public final Format[] formats;
        public final String language;
        private final long lastChunkDurationUs;
        public final int maxHeight;
        public final int maxWidth;
        public final String name;
        public final String subType;
        public final long timescale;
        public final int type;
        
        public StreamElement(final String s, final String s2, final int n, final String s3, final long n2, final String s4, final int n3, final int n4, final int n5, final int n6, final String s5, final Format[] array, final List<Long> list, final long n7) {
            this(s, s2, n, s3, n2, s4, n3, n4, n5, n6, s5, array, list, Util.scaleLargeTimestamps(list, 1000000L, n2), Util.scaleLargeTimestamp(n7, 1000000L, n2));
        }
        
        private StreamElement(final String baseUri, final String chunkTemplate, final int type, final String subType, final long timescale, final String name, final int maxWidth, final int maxHeight, final int displayWidth, final int displayHeight, final String language, final Format[] formats, final List<Long> chunkStartTimes, final long[] chunkStartTimesUs, final long lastChunkDurationUs) {
            this.baseUri = baseUri;
            this.chunkTemplate = chunkTemplate;
            this.type = type;
            this.subType = subType;
            this.timescale = timescale;
            this.name = name;
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
            this.displayWidth = displayWidth;
            this.displayHeight = displayHeight;
            this.language = language;
            this.formats = formats;
            this.chunkStartTimes = chunkStartTimes;
            this.chunkStartTimesUs = chunkStartTimesUs;
            this.lastChunkDurationUs = lastChunkDurationUs;
            this.chunkCount = chunkStartTimes.size();
        }
        
        public Uri buildRequestUri(final int n, final int n2) {
            final Format[] formats = this.formats;
            final boolean b = true;
            Assertions.checkState(formats != null);
            Assertions.checkState(this.chunkStartTimes != null);
            Assertions.checkState(n2 < this.chunkStartTimes.size() && b);
            final String string = Integer.toString(this.formats[n].bitrate);
            final String string2 = this.chunkStartTimes.get(n2).toString();
            return UriUtil.resolveToUri(this.baseUri, this.chunkTemplate.replace("{bitrate}", string).replace("{Bitrate}", string).replace("{start time}", string2).replace("{start_time}", string2));
        }
        
        public long getChunkDurationUs(final int n) {
            long lastChunkDurationUs;
            if (n == this.chunkCount - 1) {
                lastChunkDurationUs = this.lastChunkDurationUs;
            }
            else {
                final long[] chunkStartTimesUs = this.chunkStartTimesUs;
                lastChunkDurationUs = chunkStartTimesUs[n + 1] - chunkStartTimesUs[n];
            }
            return lastChunkDurationUs;
        }
        
        public int getChunkIndex(final long n) {
            return Util.binarySearchFloor(this.chunkStartTimesUs, n, true, true);
        }
        
        public long getStartTimeUs(final int n) {
            return this.chunkStartTimesUs[n];
        }
    }
}
