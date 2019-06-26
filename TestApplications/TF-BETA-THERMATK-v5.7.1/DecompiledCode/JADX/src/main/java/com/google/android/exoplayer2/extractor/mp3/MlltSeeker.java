package com.google.android.exoplayer2.extractor.mp3;

import android.util.Pair;
import com.google.android.exoplayer2.C0131C;
import com.google.android.exoplayer2.extractor.SeekMap.SeekPoints;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.metadata.id3.MlltFrame;
import com.google.android.exoplayer2.util.Util;

final class MlltSeeker implements Seeker {
    private final long durationUs;
    private final long[] referencePositions;
    private final long[] referenceTimesMs;

    public long getDataEndPosition() {
        return -1;
    }

    public boolean isSeekable() {
        return true;
    }

    public static MlltSeeker create(long j, MlltFrame mlltFrame) {
        int length = mlltFrame.bytesDeviations.length;
        int i = length + 1;
        long[] jArr = new long[i];
        long[] jArr2 = new long[i];
        jArr[0] = j;
        long j2 = 0;
        jArr2[0] = 0;
        for (int i2 = 1; i2 <= length; i2++) {
            int i3 = i2 - 1;
            j += (long) (mlltFrame.bytesBetweenReference + mlltFrame.bytesDeviations[i3]);
            j2 += (long) (mlltFrame.millisecondsBetweenReference + mlltFrame.millisecondsDeviations[i3]);
            jArr[i2] = j;
            jArr2[i2] = j2;
        }
        return new MlltSeeker(jArr, jArr2);
    }

    private MlltSeeker(long[] jArr, long[] jArr2) {
        this.referencePositions = jArr;
        this.referenceTimesMs = jArr2;
        this.durationUs = C0131C.msToUs(jArr2[jArr2.length - 1]);
    }

    public SeekPoints getSeekPoints(long j) {
        Pair linearlyInterpolate = linearlyInterpolate(C0131C.usToMs(Util.constrainValue(j, 0, this.durationUs)), this.referenceTimesMs, this.referencePositions);
        return new SeekPoints(new SeekPoint(C0131C.msToUs(((Long) linearlyInterpolate.first).longValue()), ((Long) linearlyInterpolate.second).longValue()));
    }

    public long getTimeUs(long j) {
        return C0131C.msToUs(((Long) linearlyInterpolate(j, this.referencePositions, this.referenceTimesMs).second).longValue());
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    private static Pair<Long, Long> linearlyInterpolate(long j, long[] jArr, long[] jArr2) {
        int binarySearchFloor = Util.binarySearchFloor(jArr, j, true, true);
        long j2 = jArr[binarySearchFloor];
        long j3 = jArr2[binarySearchFloor];
        binarySearchFloor++;
        if (binarySearchFloor == jArr.length) {
            return Pair.create(Long.valueOf(j2), Long.valueOf(j3));
        }
        double d;
        long j4 = jArr[binarySearchFloor];
        long j5 = jArr2[binarySearchFloor];
        if (j4 == j2) {
            d = 0.0d;
        } else {
            d = (double) j;
            double d2 = (double) j2;
            Double.isNaN(d);
            Double.isNaN(d2);
            d -= d2;
            double d3 = (double) (j4 - j2);
            Double.isNaN(d3);
            d /= d3;
        }
        double d4 = (double) (j5 - j3);
        Double.isNaN(d4);
        return Pair.create(Long.valueOf(j), Long.valueOf(((long) (d * d4)) + j3));
    }
}
