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

final class TsDurationReader
{
    private long durationUs;
    private long firstPcrValue;
    private boolean isDurationRead;
    private boolean isFirstPcrValueRead;
    private boolean isLastPcrValueRead;
    private long lastPcrValue;
    private final ParsableByteArray packetBuffer;
    private final TimestampAdjuster pcrTimestampAdjuster;
    
    TsDurationReader() {
        this.pcrTimestampAdjuster = new TimestampAdjuster(0L);
        this.firstPcrValue = -9223372036854775807L;
        this.lastPcrValue = -9223372036854775807L;
        this.durationUs = -9223372036854775807L;
        this.packetBuffer = new ParsableByteArray();
    }
    
    private int finishReadDuration(final ExtractorInput extractorInput) {
        this.packetBuffer.reset(Util.EMPTY_BYTE_ARRAY);
        this.isDurationRead = true;
        extractorInput.resetPeekPosition();
        return 0;
    }
    
    private int readFirstPcrValue(final ExtractorInput extractorInput, final PositionHolder positionHolder, final int n) throws IOException, InterruptedException {
        final int n2 = (int)Math.min(112800L, extractorInput.getLength());
        final long position = extractorInput.getPosition();
        final long position2 = 0;
        if (position != position2) {
            positionHolder.position = position2;
            return 1;
        }
        this.packetBuffer.reset(n2);
        extractorInput.resetPeekPosition();
        extractorInput.peekFully(this.packetBuffer.data, 0, n2);
        this.firstPcrValue = this.readFirstPcrValueFromBuffer(this.packetBuffer, n);
        this.isFirstPcrValueRead = true;
        return 0;
    }
    
    private long readFirstPcrValueFromBuffer(final ParsableByteArray parsableByteArray, final int n) {
        for (int i = parsableByteArray.getPosition(); i < parsableByteArray.limit(); ++i) {
            if (parsableByteArray.data[i] == 71) {
                final long pcrFromPacket = TsUtil.readPcrFromPacket(parsableByteArray, i, n);
                if (pcrFromPacket != -9223372036854775807L) {
                    return pcrFromPacket;
                }
            }
        }
        return -9223372036854775807L;
    }
    
    private int readLastPcrValue(final ExtractorInput extractorInput, final PositionHolder positionHolder, final int n) throws IOException, InterruptedException {
        final long length = extractorInput.getLength();
        final int n2 = (int)Math.min(112800L, length);
        final long position = length - n2;
        if (extractorInput.getPosition() != position) {
            positionHolder.position = position;
            return 1;
        }
        this.packetBuffer.reset(n2);
        extractorInput.resetPeekPosition();
        extractorInput.peekFully(this.packetBuffer.data, 0, n2);
        this.lastPcrValue = this.readLastPcrValueFromBuffer(this.packetBuffer, n);
        this.isLastPcrValueRead = true;
        return 0;
    }
    
    private long readLastPcrValueFromBuffer(final ParsableByteArray parsableByteArray, final int n) {
        for (int position = parsableByteArray.getPosition(), i = parsableByteArray.limit() - 1; i >= position; --i) {
            if (parsableByteArray.data[i] == 71) {
                final long pcrFromPacket = TsUtil.readPcrFromPacket(parsableByteArray, i, n);
                if (pcrFromPacket != -9223372036854775807L) {
                    return pcrFromPacket;
                }
            }
        }
        return -9223372036854775807L;
    }
    
    public long getDurationUs() {
        return this.durationUs;
    }
    
    public TimestampAdjuster getPcrTimestampAdjuster() {
        return this.pcrTimestampAdjuster;
    }
    
    public boolean isDurationReadFinished() {
        return this.isDurationRead;
    }
    
    public int readDuration(final ExtractorInput extractorInput, final PositionHolder positionHolder, final int n) throws IOException, InterruptedException {
        if (n <= 0) {
            return this.finishReadDuration(extractorInput);
        }
        if (!this.isLastPcrValueRead) {
            return this.readLastPcrValue(extractorInput, positionHolder, n);
        }
        if (this.lastPcrValue == -9223372036854775807L) {
            return this.finishReadDuration(extractorInput);
        }
        if (!this.isFirstPcrValueRead) {
            return this.readFirstPcrValue(extractorInput, positionHolder, n);
        }
        final long firstPcrValue = this.firstPcrValue;
        if (firstPcrValue == -9223372036854775807L) {
            return this.finishReadDuration(extractorInput);
        }
        this.durationUs = this.pcrTimestampAdjuster.adjustTsTimestamp(this.lastPcrValue) - this.pcrTimestampAdjuster.adjustTsTimestamp(firstPcrValue);
        return this.finishReadDuration(extractorInput);
    }
}
