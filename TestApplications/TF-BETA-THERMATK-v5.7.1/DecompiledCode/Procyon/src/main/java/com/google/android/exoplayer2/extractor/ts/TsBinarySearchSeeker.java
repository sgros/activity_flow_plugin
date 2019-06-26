// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker;

final class TsBinarySearchSeeker extends BinarySearchSeeker
{
    public TsBinarySearchSeeker(final TimestampAdjuster timestampAdjuster, final long n, final long n2, final int n3) {
        super((SeekTimestampConverter)new DefaultSeekTimestampConverter(), (TimestampSeeker)new TsPcrSeeker(n3, timestampAdjuster), n, 0L, n + 1L, 0L, n2, 188L, 940);
    }
    
    private static final class TsPcrSeeker implements TimestampSeeker
    {
        private final ParsableByteArray packetBuffer;
        private final int pcrPid;
        private final TimestampAdjuster pcrTimestampAdjuster;
        
        public TsPcrSeeker(final int pcrPid, final TimestampAdjuster pcrTimestampAdjuster) {
            this.pcrPid = pcrPid;
            this.pcrTimestampAdjuster = pcrTimestampAdjuster;
            this.packetBuffer = new ParsableByteArray();
        }
        
        private TimestampSearchResult searchForPcrValueInBuffer(final ParsableByteArray parsableByteArray, final long n, final long n2) {
            final int limit = parsableByteArray.limit();
            long n3 = -1L;
            long n4 = -1L;
            long n5 = -9223372036854775807L;
            while (parsableByteArray.bytesLeft() >= 188) {
                final int syncBytePosition = TsUtil.findSyncBytePosition(parsableByteArray.data, parsableByteArray.getPosition(), limit);
                final int position = syncBytePosition + 188;
                if (position > limit) {
                    break;
                }
                final long pcrFromPacket = TsUtil.readPcrFromPacket(parsableByteArray, syncBytePosition, this.pcrPid);
                long adjustTsTimestamp = n5;
                long n6 = n4;
                if (pcrFromPacket != -9223372036854775807L) {
                    adjustTsTimestamp = this.pcrTimestampAdjuster.adjustTsTimestamp(pcrFromPacket);
                    if (adjustTsTimestamp > n) {
                        if (n5 == -9223372036854775807L) {
                            return TimestampSearchResult.overestimatedResult(adjustTsTimestamp, n2);
                        }
                        return TimestampSearchResult.targetFoundResult(n2 + n4);
                    }
                    else {
                        if (100000L + adjustTsTimestamp > n) {
                            return TimestampSearchResult.targetFoundResult(n2 + syncBytePosition);
                        }
                        n6 = syncBytePosition;
                    }
                }
                parsableByteArray.setPosition(position);
                n3 = position;
                n5 = adjustTsTimestamp;
                n4 = n6;
            }
            if (n5 != -9223372036854775807L) {
                return TimestampSearchResult.underestimatedResult(n5, n2 + n3);
            }
            return TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
        }
        
        @Override
        public void onSeekFinished() {
            this.packetBuffer.reset(Util.EMPTY_BYTE_ARRAY);
        }
        
        @Override
        public TimestampSearchResult searchForTimestamp(final ExtractorInput extractorInput, final long n, final OutputFrameHolder outputFrameHolder) throws IOException, InterruptedException {
            final long position = extractorInput.getPosition();
            final int n2 = (int)Math.min(112800L, extractorInput.getLength() - position);
            this.packetBuffer.reset(n2);
            extractorInput.peekFully(this.packetBuffer.data, 0, n2);
            return this.searchForPcrValueInBuffer(this.packetBuffer, n, position);
        }
    }
}
