// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import java.util.List;
import com.google.android.exoplayer2.util.Util;
import android.os.SystemClock;
import java.util.Comparator;
import java.util.Arrays;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.Format;

public abstract class BaseTrackSelection implements TrackSelection
{
    private final long[] blacklistUntilTimes;
    private final Format[] formats;
    protected final TrackGroup group;
    private int hashCode;
    protected final int length;
    protected final int[] tracks;
    
    public BaseTrackSelection(final TrackGroup trackGroup, final int... array) {
        final int length = array.length;
        final int n = 0;
        Assertions.checkState(length > 0);
        Assertions.checkNotNull(trackGroup);
        this.group = trackGroup;
        this.length = array.length;
        this.formats = new Format[this.length];
        for (int i = 0; i < array.length; ++i) {
            this.formats[i] = trackGroup.getFormat(array[i]);
        }
        Arrays.sort(this.formats, new DecreasingBandwidthComparator());
        this.tracks = new int[this.length];
        int n2 = n;
        int length2;
        while (true) {
            length2 = this.length;
            if (n2 >= length2) {
                break;
            }
            this.tracks[n2] = trackGroup.indexOf(this.formats[n2]);
            ++n2;
        }
        this.blacklistUntilTimes = new long[length2];
    }
    
    @Override
    public final boolean blacklist(final int n, final long n2) {
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        int blacklisted = this.isBlacklisted(n, elapsedRealtime) ? 1 : 0;
        for (int n3 = 0; n3 < this.length && blacklisted == 0; ++n3) {
            if (n3 != n && !this.isBlacklisted(n3, elapsedRealtime)) {
                blacklisted = 1;
            }
            else {
                blacklisted = 0;
            }
        }
        if (blacklisted == 0) {
            return false;
        }
        final long[] blacklistUntilTimes = this.blacklistUntilTimes;
        blacklistUntilTimes[n] = Math.max(blacklistUntilTimes[n], Util.addWithOverflowDefault(elapsedRealtime, n2, Long.MAX_VALUE));
        return true;
    }
    
    @Override
    public void disable() {
    }
    
    @Override
    public void enable() {
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final BaseTrackSelection baseTrackSelection = (BaseTrackSelection)o;
            if (this.group != baseTrackSelection.group || !Arrays.equals(this.tracks, baseTrackSelection.tracks)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int evaluateQueueSize(final long n, final List<? extends MediaChunk> list) {
        return list.size();
    }
    
    @Override
    public final Format getFormat(final int n) {
        return this.formats[n];
    }
    
    @Override
    public final int getIndexInTrackGroup(final int n) {
        return this.tracks[n];
    }
    
    @Override
    public final Format getSelectedFormat() {
        return this.formats[this.getSelectedIndex()];
    }
    
    @Override
    public final int getSelectedIndexInTrackGroup() {
        return this.tracks[this.getSelectedIndex()];
    }
    
    @Override
    public final TrackGroup getTrackGroup() {
        return this.group;
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = System.identityHashCode(this.group) * 31 + Arrays.hashCode(this.tracks);
        }
        return this.hashCode;
    }
    
    @Override
    public final int indexOf(final int n) {
        for (int i = 0; i < this.length; ++i) {
            if (this.tracks[i] == n) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public final int indexOf(final Format format) {
        for (int i = 0; i < this.length; ++i) {
            if (this.formats[i] == format) {
                return i;
            }
        }
        return -1;
    }
    
    protected final boolean isBlacklisted(final int n, final long n2) {
        return this.blacklistUntilTimes[n] > n2;
    }
    
    @Override
    public final int length() {
        return this.tracks.length;
    }
    
    @Override
    public void onPlaybackSpeed(final float n) {
    }
    
    private static final class DecreasingBandwidthComparator implements Comparator<Format>
    {
        @Override
        public int compare(final Format format, final Format format2) {
            return format2.bitrate - format.bitrate;
        }
    }
}
