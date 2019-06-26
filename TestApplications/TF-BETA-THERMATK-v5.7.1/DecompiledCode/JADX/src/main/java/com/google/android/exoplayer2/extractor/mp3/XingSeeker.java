package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.extractor.SeekMap.SeekPoints;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;

final class XingSeeker implements Seeker {
    private final long dataEndPosition;
    private final long dataSize;
    private final long dataStartPosition;
    private final long durationUs;
    private final long[] tableOfContents;
    private final int xingFrameSize;

    public static XingSeeker create(long j, long j2, MpegAudioHeader mpegAudioHeader, ParsableByteArray parsableByteArray) {
        long j3 = j;
        MpegAudioHeader mpegAudioHeader2 = mpegAudioHeader;
        int i = mpegAudioHeader2.samplesPerFrame;
        int i2 = mpegAudioHeader2.sampleRate;
        int readInt = parsableByteArray.readInt();
        if ((readInt & 1) == 1) {
            int readUnsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
            if (readUnsignedIntToInt != 0) {
                long scaleLargeTimestamp = Util.scaleLargeTimestamp((long) readUnsignedIntToInt, ((long) i) * 1000000, (long) i2);
                if ((readInt & 6) != 6) {
                    return new XingSeeker(j2, mpegAudioHeader2.frameSize, scaleLargeTimestamp);
                }
                long readUnsignedIntToInt2 = (long) parsableByteArray.readUnsignedIntToInt();
                long[] jArr = new long[100];
                for (int i3 = 0; i3 < 100; i3++) {
                    jArr[i3] = (long) parsableByteArray.readUnsignedByte();
                }
                if (j3 != -1) {
                    long j4 = j2 + readUnsignedIntToInt2;
                    if (j3 != j4) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("XING data size mismatch: ");
                        stringBuilder.append(j3);
                        stringBuilder.append(", ");
                        stringBuilder.append(j4);
                        Log.m18w("XingSeeker", stringBuilder.toString());
                    }
                }
                return new XingSeeker(j2, mpegAudioHeader2.frameSize, scaleLargeTimestamp, readUnsignedIntToInt2, jArr);
            }
        }
        return null;
    }

    private XingSeeker(long j, int i, long j2) {
        this(j, i, j2, -1, null);
    }

    private XingSeeker(long j, int i, long j2, long j3, long[] jArr) {
        this.dataStartPosition = j;
        this.xingFrameSize = i;
        this.durationUs = j2;
        this.tableOfContents = jArr;
        this.dataSize = j3;
        long j4 = -1;
        if (j3 != -1) {
            j4 = j + j3;
        }
        this.dataEndPosition = j4;
    }

    public boolean isSeekable() {
        return this.tableOfContents != null;
    }

    public SeekPoints getSeekPoints(long j) {
        if (!isSeekable()) {
            return new SeekPoints(new SeekPoint(0, this.dataStartPosition + ((long) this.xingFrameSize)));
        }
        j = Util.constrainValue(j, 0, this.durationUs);
        double d = (double) j;
        Double.isNaN(d);
        d *= 100.0d;
        double d2 = (double) this.durationUs;
        Double.isNaN(d2);
        d /= d2;
        d2 = 0.0d;
        if (d > 0.0d) {
            if (d >= 100.0d) {
                d2 = 256.0d;
            } else {
                double d3;
                int i = (int) d;
                long[] jArr = this.tableOfContents;
                Assertions.checkNotNull(jArr);
                jArr = jArr;
                d2 = (double) jArr[i];
                if (i == 99) {
                    d3 = 256.0d;
                } else {
                    d3 = (double) jArr[i + 1];
                }
                double d4 = (double) i;
                Double.isNaN(d4);
                d -= d4;
                Double.isNaN(d2);
                d *= d3 - d2;
                Double.isNaN(d2);
                d2 += d;
            }
        }
        d2 /= 256.0d;
        d = (double) this.dataSize;
        Double.isNaN(d);
        return new SeekPoints(new SeekPoint(j, this.dataStartPosition + Util.constrainValue(Math.round(d2 * d), (long) this.xingFrameSize, this.dataSize - 1)));
    }

    public long getTimeUs(long j) {
        j -= this.dataStartPosition;
        if (!isSeekable() || j <= ((long) this.xingFrameSize)) {
            return 0;
        }
        long j2;
        double d;
        long[] jArr = this.tableOfContents;
        Assertions.checkNotNull(jArr);
        jArr = jArr;
        double d2 = (double) j;
        Double.isNaN(d2);
        d2 *= 256.0d;
        double d3 = (double) this.dataSize;
        Double.isNaN(d3);
        d2 /= d3;
        int binarySearchFloor = Util.binarySearchFloor(jArr, (long) d2, true, true);
        long timeUsForTableIndex = getTimeUsForTableIndex(binarySearchFloor);
        long j3 = jArr[binarySearchFloor];
        int i = binarySearchFloor + 1;
        long timeUsForTableIndex2 = getTimeUsForTableIndex(i);
        if (binarySearchFloor == 99) {
            j2 = 256;
        } else {
            j2 = jArr[i];
        }
        if (j3 == j2) {
            d2 = 0.0d;
        } else {
            double d4 = (double) j3;
            Double.isNaN(d4);
            d2 -= d4;
            d = (double) (j2 - j3);
            Double.isNaN(d);
            d2 /= d;
        }
        d = (double) (timeUsForTableIndex2 - timeUsForTableIndex);
        Double.isNaN(d);
        return timeUsForTableIndex + Math.round(d2 * d);
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    public long getDataEndPosition() {
        return this.dataEndPosition;
    }

    private long getTimeUsForTableIndex(int i) {
        return (this.durationUs * ((long) i)) / 100;
    }
}
