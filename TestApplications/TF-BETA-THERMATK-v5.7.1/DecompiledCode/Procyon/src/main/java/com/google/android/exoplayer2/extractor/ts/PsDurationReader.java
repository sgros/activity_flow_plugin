// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import java.io.IOException;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.ParsableByteArray;

final class PsDurationReader
{
    private long durationUs;
    private long firstScrValue;
    private boolean isDurationRead;
    private boolean isFirstScrValueRead;
    private boolean isLastScrValueRead;
    private long lastScrValue;
    private final ParsableByteArray packetBuffer;
    private final TimestampAdjuster scrTimestampAdjuster;
    
    PsDurationReader() {
        this.scrTimestampAdjuster = new TimestampAdjuster(0L);
        this.firstScrValue = -9223372036854775807L;
        this.lastScrValue = -9223372036854775807L;
        this.durationUs = -9223372036854775807L;
        this.packetBuffer = new ParsableByteArray();
    }
    
    private static boolean checkMarkerBits(final byte[] array) {
        boolean b = false;
        if ((array[0] & 0xC4) != 0x44) {
            return false;
        }
        if ((array[2] & 0x4) != 0x4) {
            return false;
        }
        if ((array[4] & 0x4) != 0x4) {
            return false;
        }
        if ((array[5] & 0x1) != 0x1) {
            return false;
        }
        if ((array[8] & 0x3) == 0x3) {
            b = true;
        }
        return b;
    }
    
    private int finishReadDuration(final ExtractorInput extractorInput) {
        this.packetBuffer.reset(Util.EMPTY_BYTE_ARRAY);
        this.isDurationRead = true;
        extractorInput.resetPeekPosition();
        return 0;
    }
    
    private int peekIntAtPosition(final byte[] array, final int n) {
        return (array[n + 3] & 0xFF) | ((array[n] & 0xFF) << 24 | (array[n + 1] & 0xFF) << 16 | (array[n + 2] & 0xFF) << 8);
    }
    
    private int readFirstScrValue(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        final int n = (int)Math.min(20000L, extractorInput.getLength());
        final long position = extractorInput.getPosition();
        final long position2 = 0;
        if (position != position2) {
            positionHolder.position = position2;
            return 1;
        }
        this.packetBuffer.reset(n);
        extractorInput.resetPeekPosition();
        extractorInput.peekFully(this.packetBuffer.data, 0, n);
        this.firstScrValue = this.readFirstScrValueFromBuffer(this.packetBuffer);
        this.isFirstScrValueRead = true;
        return 0;
    }
    
    private long readFirstScrValueFromBuffer(final ParsableByteArray parsableByteArray) {
        for (int i = parsableByteArray.getPosition(); i < parsableByteArray.limit() - 3; ++i) {
            if (this.peekIntAtPosition(parsableByteArray.data, i) == 442) {
                parsableByteArray.setPosition(i + 4);
                final long scrValueFromPack = readScrValueFromPack(parsableByteArray);
                if (scrValueFromPack != -9223372036854775807L) {
                    return scrValueFromPack;
                }
            }
        }
        return -9223372036854775807L;
    }
    
    private int readLastScrValue(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        final long length = extractorInput.getLength();
        final int n = (int)Math.min(20000L, length);
        final long position = length - n;
        if (extractorInput.getPosition() != position) {
            positionHolder.position = position;
            return 1;
        }
        this.packetBuffer.reset(n);
        extractorInput.resetPeekPosition();
        extractorInput.peekFully(this.packetBuffer.data, 0, n);
        this.lastScrValue = this.readLastScrValueFromBuffer(this.packetBuffer);
        this.isLastScrValueRead = true;
        return 0;
    }
    
    private long readLastScrValueFromBuffer(final ParsableByteArray parsableByteArray) {
        for (int position = parsableByteArray.getPosition(), i = parsableByteArray.limit() - 4; i >= position; --i) {
            if (this.peekIntAtPosition(parsableByteArray.data, i) == 442) {
                parsableByteArray.setPosition(i + 4);
                final long scrValueFromPack = readScrValueFromPack(parsableByteArray);
                if (scrValueFromPack != -9223372036854775807L) {
                    return scrValueFromPack;
                }
            }
        }
        return -9223372036854775807L;
    }
    
    public static long readScrValueFromPack(final ParsableByteArray parsableByteArray) {
        final int position = parsableByteArray.getPosition();
        if (parsableByteArray.bytesLeft() < 9) {
            return -9223372036854775807L;
        }
        final byte[] array = new byte[9];
        parsableByteArray.readBytes(array, 0, array.length);
        parsableByteArray.setPosition(position);
        if (!checkMarkerBits(array)) {
            return -9223372036854775807L;
        }
        return readScrValueFromPackHeader(array);
    }
    
    private static long readScrValueFromPackHeader(final byte[] array) {
        return ((long)array[0] & 0x38L) >> 3 << 30 | ((long)array[0] & 0x3L) << 28 | ((long)array[1] & 0xFFL) << 20 | ((long)array[2] & 0xF8L) >> 3 << 15 | ((long)array[2] & 0x3L) << 13 | ((long)array[3] & 0xFFL) << 5 | ((long)array[4] & 0xF8L) >> 3;
    }
    
    public long getDurationUs() {
        return this.durationUs;
    }
    
    public TimestampAdjuster getScrTimestampAdjuster() {
        return this.scrTimestampAdjuster;
    }
    
    public boolean isDurationReadFinished() {
        return this.isDurationRead;
    }
    
    public int readDuration(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        if (!this.isLastScrValueRead) {
            return this.readLastScrValue(extractorInput, positionHolder);
        }
        if (this.lastScrValue == -9223372036854775807L) {
            return this.finishReadDuration(extractorInput);
        }
        if (!this.isFirstScrValueRead) {
            return this.readFirstScrValue(extractorInput, positionHolder);
        }
        final long firstScrValue = this.firstScrValue;
        if (firstScrValue == -9223372036854775807L) {
            return this.finishReadDuration(extractorInput);
        }
        this.durationUs = this.scrTimestampAdjuster.adjustTsTimestamp(this.lastScrValue) - this.scrTimestampAdjuster.adjustTsTimestamp(firstScrValue);
        return this.finishReadDuration(extractorInput);
    }
}
