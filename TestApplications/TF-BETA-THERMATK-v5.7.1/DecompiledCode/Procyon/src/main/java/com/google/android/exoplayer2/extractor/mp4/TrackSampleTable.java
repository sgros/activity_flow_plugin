// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.Assertions;

final class TrackSampleTable
{
    public final long durationUs;
    public final int[] flags;
    public final int maximumSize;
    public final long[] offsets;
    public final int sampleCount;
    public final int[] sizes;
    public final long[] timestampsUs;
    public final Track track;
    
    public TrackSampleTable(final Track track, final long[] offsets, final int[] sizes, int maximumSize, final long[] timestampsUs, final int[] flags, final long durationUs) {
        final int length = sizes.length;
        final int length2 = timestampsUs.length;
        final boolean b = false;
        Assertions.checkArgument(length == length2);
        Assertions.checkArgument(offsets.length == timestampsUs.length);
        boolean b2 = b;
        if (flags.length == timestampsUs.length) {
            b2 = true;
        }
        Assertions.checkArgument(b2);
        this.track = track;
        this.offsets = offsets;
        this.sizes = sizes;
        this.maximumSize = maximumSize;
        this.timestampsUs = timestampsUs;
        this.flags = flags;
        this.durationUs = durationUs;
        this.sampleCount = offsets.length;
        if (flags.length > 0) {
            maximumSize = flags.length - 1;
            flags[maximumSize] |= 0x20000000;
        }
    }
    
    public int getIndexOfEarlierOrEqualSynchronizationSample(final long n) {
        for (int i = Util.binarySearchFloor(this.timestampsUs, n, true, false); i >= 0; --i) {
            if ((this.flags[i] & 0x1) != 0x0) {
                return i;
            }
        }
        return -1;
    }
    
    public int getIndexOfLaterOrEqualSynchronizationSample(final long n) {
        for (int i = Util.binarySearchCeil(this.timestampsUs, n, true, false); i < this.timestampsUs.length; ++i) {
            if ((this.flags[i] & 0x1) != 0x0) {
                return i;
            }
        }
        return -1;
    }
}
