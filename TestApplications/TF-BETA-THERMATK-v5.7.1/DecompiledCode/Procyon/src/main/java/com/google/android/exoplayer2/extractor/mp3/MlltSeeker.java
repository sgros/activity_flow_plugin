// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.Util;
import android.util.Pair;
import com.google.android.exoplayer2.metadata.id3.MlltFrame;
import com.google.android.exoplayer2.C;

final class MlltSeeker implements Seeker
{
    private final long durationUs;
    private final long[] referencePositions;
    private final long[] referenceTimesMs;
    
    private MlltSeeker(final long[] referencePositions, final long[] referenceTimesMs) {
        this.referencePositions = referencePositions;
        this.referenceTimesMs = referenceTimesMs;
        this.durationUs = C.msToUs(referenceTimesMs[referenceTimesMs.length - 1]);
    }
    
    public static MlltSeeker create(long n, final MlltFrame mlltFrame) {
        final int length = mlltFrame.bytesDeviations.length;
        final int n2 = length + 1;
        final long[] array = new long[n2];
        final long[] array2 = new long[n2];
        array[0] = n;
        long n3 = 0L;
        array2[0] = 0L;
        for (int i = 1; i <= length; ++i) {
            final int bytesBetweenReference = mlltFrame.bytesBetweenReference;
            final int[] bytesDeviations = mlltFrame.bytesDeviations;
            final int n4 = i - 1;
            n += bytesBetweenReference + bytesDeviations[n4];
            n3 += mlltFrame.millisecondsBetweenReference + mlltFrame.millisecondsDeviations[n4];
            array[i] = n;
            array2[i] = n3;
        }
        return new MlltSeeker(array, array2);
    }
    
    private static Pair<Long, Long> linearlyInterpolate(final long l, final long[] array, final long[] array2) {
        int binarySearchFloor = Util.binarySearchFloor(array, l, true, true);
        final long i = array[binarySearchFloor];
        final long j = array2[binarySearchFloor];
        if (++binarySearchFloor == array.length) {
            return (Pair<Long, Long>)Pair.create((Object)i, (Object)j);
        }
        final long n = array[binarySearchFloor];
        final long n2 = array2[binarySearchFloor];
        double n3;
        if (n == i) {
            n3 = 0.0;
        }
        else {
            final double v = (double)l;
            final double v2 = (double)i;
            Double.isNaN(v);
            Double.isNaN(v2);
            final double v3 = (double)(n - i);
            Double.isNaN(v3);
            n3 = (v - v2) / v3;
        }
        final double v4 = (double)(n2 - j);
        Double.isNaN(v4);
        return (Pair<Long, Long>)Pair.create((Object)l, (Object)((long)(n3 * v4) + j));
    }
    
    @Override
    public long getDataEndPosition() {
        return -1L;
    }
    
    @Override
    public long getDurationUs() {
        return this.durationUs;
    }
    
    @Override
    public SeekPoints getSeekPoints(final long n) {
        final Pair<Long, Long> linearlyInterpolate = linearlyInterpolate(C.usToMs(Util.constrainValue(n, 0L, this.durationUs)), this.referenceTimesMs, this.referencePositions);
        return new SeekMap.SeekPoints(new SeekPoint(C.msToUs((long)linearlyInterpolate.first), (long)linearlyInterpolate.second));
    }
    
    @Override
    public long getTimeUs(final long n) {
        return C.msToUs((long)linearlyInterpolate(n, this.referencePositions, this.referenceTimesMs).second);
    }
    
    @Override
    public boolean isSeekable() {
        return true;
    }
}
