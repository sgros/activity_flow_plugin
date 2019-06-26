// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;

final class XingSeeker implements Seeker
{
    private final long dataEndPosition;
    private final long dataSize;
    private final long dataStartPosition;
    private final long durationUs;
    private final long[] tableOfContents;
    private final int xingFrameSize;
    
    private XingSeeker(final long n, final int n2, final long n3) {
        this(n, n2, n3, -1L, null);
    }
    
    private XingSeeker(long n, final int xingFrameSize, long durationUs, final long dataSize, final long[] tableOfContents) {
        this.dataStartPosition = n;
        this.xingFrameSize = xingFrameSize;
        this.durationUs = durationUs;
        this.tableOfContents = tableOfContents;
        this.dataSize = dataSize;
        durationUs = -1L;
        if (dataSize == -1L) {
            n = durationUs;
        }
        else {
            n += dataSize;
        }
        this.dataEndPosition = n;
    }
    
    public static XingSeeker create(final long lng, final long n, final MpegAudioHeader mpegAudioHeader, final ParsableByteArray parsableByteArray) {
        final int samplesPerFrame = mpegAudioHeader.samplesPerFrame;
        final int sampleRate = mpegAudioHeader.sampleRate;
        final int int1 = parsableByteArray.readInt();
        if ((int1 & 0x1) == 0x1) {
            final int unsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
            if (unsignedIntToInt != 0) {
                final long scaleLargeTimestamp = Util.scaleLargeTimestamp(unsignedIntToInt, samplesPerFrame * 1000000L, sampleRate);
                if ((int1 & 0x6) != 0x6) {
                    return new XingSeeker(n, mpegAudioHeader.frameSize, scaleLargeTimestamp);
                }
                final long n2 = parsableByteArray.readUnsignedIntToInt();
                final long[] array = new long[100];
                for (int i = 0; i < 100; ++i) {
                    array[i] = parsableByteArray.readUnsignedByte();
                }
                if (lng != -1L) {
                    final long lng2 = n + n2;
                    if (lng != lng2) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("XING data size mismatch: ");
                        sb.append(lng);
                        sb.append(", ");
                        sb.append(lng2);
                        Log.w("XingSeeker", sb.toString());
                    }
                }
                return new XingSeeker(n, mpegAudioHeader.frameSize, scaleLargeTimestamp, n2, array);
            }
        }
        return null;
    }
    
    private long getTimeUsForTableIndex(final int n) {
        return this.durationUs * n / 100L;
    }
    
    @Override
    public long getDataEndPosition() {
        return this.dataEndPosition;
    }
    
    @Override
    public long getDurationUs() {
        return this.durationUs;
    }
    
    @Override
    public SeekPoints getSeekPoints(long constrainValue) {
        if (!this.isSeekable()) {
            return new SeekMap.SeekPoints(new SeekPoint(0L, this.dataStartPosition + this.xingFrameSize));
        }
        final long constrainValue2 = Util.constrainValue(constrainValue, 0L, this.durationUs);
        final double v = (double)constrainValue2;
        Double.isNaN(v);
        final double v2 = (double)this.durationUs;
        Double.isNaN(v2);
        final double n = v * 100.0 / v2;
        double n2 = 0.0;
        if (n > 0.0) {
            if (n >= 100.0) {
                n2 = 256.0;
            }
            else {
                final int n3 = (int)n;
                final long[] tableOfContents = this.tableOfContents;
                Assertions.checkNotNull(tableOfContents);
                final long[] array = tableOfContents;
                final double n4 = (double)array[n3];
                double n5;
                if (n3 == 99) {
                    n5 = 256.0;
                }
                else {
                    n5 = (double)array[n3 + 1];
                }
                final double v3 = n3;
                Double.isNaN(v3);
                Double.isNaN(n4);
                Double.isNaN(n4);
                n2 = n4 + (n - v3) * (n5 - n4);
            }
        }
        final double n6 = n2 / 256.0;
        final double v4 = (double)this.dataSize;
        Double.isNaN(v4);
        constrainValue = Util.constrainValue(Math.round(n6 * v4), this.xingFrameSize, this.dataSize - 1L);
        return new SeekMap.SeekPoints(new SeekPoint(constrainValue2, this.dataStartPosition + constrainValue));
    }
    
    @Override
    public long getTimeUs(long n) {
        n -= this.dataStartPosition;
        if (this.isSeekable() && n > this.xingFrameSize) {
            final long[] tableOfContents = this.tableOfContents;
            Assertions.checkNotNull(tableOfContents);
            final long[] array = tableOfContents;
            final double v = (double)n;
            Double.isNaN(v);
            final double v2 = (double)this.dataSize;
            Double.isNaN(v2);
            final double n2 = v * 256.0 / v2;
            final int binarySearchFloor = Util.binarySearchFloor(array, (long)n2, true, true);
            final long timeUsForTableIndex = this.getTimeUsForTableIndex(binarySearchFloor);
            final long n3 = array[binarySearchFloor];
            final int n4 = binarySearchFloor + 1;
            final long timeUsForTableIndex2 = this.getTimeUsForTableIndex(n4);
            if (binarySearchFloor == 99) {
                n = 256L;
            }
            else {
                n = array[n4];
            }
            double n5;
            if (n3 == n) {
                n5 = 0.0;
            }
            else {
                final double v3 = (double)n3;
                Double.isNaN(v3);
                final double v4 = (double)(n - n3);
                Double.isNaN(v4);
                n5 = (n2 - v3) / v4;
            }
            final double v5 = (double)(timeUsForTableIndex2 - timeUsForTableIndex);
            Double.isNaN(v5);
            return timeUsForTableIndex + Math.round(n5 * v5);
        }
        return 0L;
    }
    
    @Override
    public boolean isSeekable() {
        return this.tableOfContents != null;
    }
}
