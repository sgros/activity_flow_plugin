// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;

final class VbriSeeker implements Seeker
{
    private final long dataEndPosition;
    private final long durationUs;
    private final long[] positions;
    private final long[] timesUs;
    
    private VbriSeeker(final long[] timesUs, final long[] positions, final long durationUs, final long dataEndPosition) {
        this.timesUs = timesUs;
        this.positions = positions;
        this.durationUs = durationUs;
        this.dataEndPosition = dataEndPosition;
    }
    
    public static VbriSeeker create(final long lng, long b, final MpegAudioHeader mpegAudioHeader, final ParsableByteArray parsableByteArray) {
        parsableByteArray.skipBytes(10);
        final int int1 = parsableByteArray.readInt();
        if (int1 <= 0) {
            return null;
        }
        final int sampleRate = mpegAudioHeader.sampleRate;
        final long n = int1;
        int n2;
        if (sampleRate >= 32000) {
            n2 = 1152;
        }
        else {
            n2 = 576;
        }
        final long scaleLargeTimestamp = Util.scaleLargeTimestamp(n, 1000000L * n2, sampleRate);
        final int unsignedShort = parsableByteArray.readUnsignedShort();
        final int unsignedShort2 = parsableByteArray.readUnsignedShort();
        final int unsignedShort3 = parsableByteArray.readUnsignedShort();
        parsableByteArray.skipBytes(2);
        final long n3 = b + mpegAudioHeader.frameSize;
        final long[] array = new long[unsignedShort];
        final long[] array2 = new long[unsignedShort];
        int i = 0;
        long n4 = b;
        b = n3;
        while (i < unsignedShort) {
            array[i] = i * scaleLargeTimestamp / unsignedShort;
            array2[i] = Math.max(n4, b);
            int n5;
            if (unsignedShort3 != 1) {
                if (unsignedShort3 != 2) {
                    if (unsignedShort3 != 3) {
                        if (unsignedShort3 != 4) {
                            return null;
                        }
                        n5 = parsableByteArray.readUnsignedIntToInt();
                    }
                    else {
                        n5 = parsableByteArray.readUnsignedInt24();
                    }
                }
                else {
                    n5 = parsableByteArray.readUnsignedShort();
                }
            }
            else {
                n5 = parsableByteArray.readUnsignedByte();
            }
            n4 += n5 * unsignedShort2;
            ++i;
        }
        if (lng != -1L && lng != n4) {
            final StringBuilder sb = new StringBuilder();
            sb.append("VBRI data size mismatch: ");
            sb.append(lng);
            sb.append(", ");
            sb.append(n4);
            Log.w("VbriSeeker", sb.toString());
        }
        return new VbriSeeker(array, array2, scaleLargeTimestamp, n4);
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
    public SeekPoints getSeekPoints(final long n) {
        int binarySearchFloor = Util.binarySearchFloor(this.timesUs, n, true, true);
        final SeekPoint seekPoint = new SeekPoint(this.timesUs[binarySearchFloor], this.positions[binarySearchFloor]);
        if (seekPoint.timeUs < n) {
            final long[] timesUs = this.timesUs;
            if (binarySearchFloor != timesUs.length - 1) {
                ++binarySearchFloor;
                return new SeekMap.SeekPoints(seekPoint, new SeekPoint(timesUs[binarySearchFloor], this.positions[binarySearchFloor]));
            }
        }
        return new SeekMap.SeekPoints(seekPoint);
    }
    
    @Override
    public long getTimeUs(final long n) {
        return this.timesUs[Util.binarySearchFloor(this.positions, n, true, true)];
    }
    
    @Override
    public boolean isSeekable() {
        return true;
    }
}
