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

final class PsBinarySearchSeeker extends BinarySearchSeeker
{
    public PsBinarySearchSeeker(final TimestampAdjuster timestampAdjuster, final long n, final long n2) {
        super((SeekTimestampConverter)new DefaultSeekTimestampConverter(), (TimestampSeeker)new PsScrSeeker(timestampAdjuster), n, 0L, n + 1L, 0L, n2, 188L, 1000);
    }
    
    private static int peekIntAtPosition(final byte[] array, final int n) {
        return (array[n + 3] & 0xFF) | ((array[n] & 0xFF) << 24 | (array[n + 1] & 0xFF) << 16 | (array[n + 2] & 0xFF) << 8);
    }
    
    private static final class PsScrSeeker implements TimestampSeeker
    {
        private final ParsableByteArray packetBuffer;
        private final TimestampAdjuster scrTimestampAdjuster;
        
        private PsScrSeeker(final TimestampAdjuster scrTimestampAdjuster) {
            this.scrTimestampAdjuster = scrTimestampAdjuster;
            this.packetBuffer = new ParsableByteArray();
        }
        
        private TimestampSearchResult searchForScrValueInBuffer(final ParsableByteArray parsableByteArray, final long n, final long n2) {
            int position = -1;
            long n3 = -9223372036854775807L;
            int n4 = -1;
            while (parsableByteArray.bytesLeft() >= 4) {
                if (peekIntAtPosition(parsableByteArray.data, parsableByteArray.getPosition()) != 442) {
                    parsableByteArray.skipBytes(1);
                }
                else {
                    parsableByteArray.skipBytes(4);
                    final long scrValueFromPack = PsDurationReader.readScrValueFromPack(parsableByteArray);
                    long adjustTsTimestamp = n3;
                    int position2 = n4;
                    if (scrValueFromPack != -9223372036854775807L) {
                        adjustTsTimestamp = this.scrTimestampAdjuster.adjustTsTimestamp(scrValueFromPack);
                        if (adjustTsTimestamp > n) {
                            if (n3 == -9223372036854775807L) {
                                return TimestampSearchResult.overestimatedResult(adjustTsTimestamp, n2);
                            }
                            return TimestampSearchResult.targetFoundResult(n2 + n4);
                        }
                        else {
                            if (100000L + adjustTsTimestamp > n) {
                                return TimestampSearchResult.targetFoundResult(n2 + parsableByteArray.getPosition());
                            }
                            position2 = parsableByteArray.getPosition();
                        }
                    }
                    skipToEndOfCurrentPack(parsableByteArray);
                    position = parsableByteArray.getPosition();
                    n3 = adjustTsTimestamp;
                    n4 = position2;
                }
            }
            if (n3 != -9223372036854775807L) {
                return TimestampSearchResult.underestimatedResult(n3, n2 + position);
            }
            return TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
        }
        
        private static void skipToEndOfCurrentPack(final ParsableByteArray parsableByteArray) {
            final int limit = parsableByteArray.limit();
            if (parsableByteArray.bytesLeft() < 10) {
                parsableByteArray.setPosition(limit);
                return;
            }
            parsableByteArray.skipBytes(9);
            final int n = parsableByteArray.readUnsignedByte() & 0x7;
            if (parsableByteArray.bytesLeft() < n) {
                parsableByteArray.setPosition(limit);
                return;
            }
            parsableByteArray.skipBytes(n);
            if (parsableByteArray.bytesLeft() < 4) {
                parsableByteArray.setPosition(limit);
                return;
            }
            if (peekIntAtPosition(parsableByteArray.data, parsableByteArray.getPosition()) == 443) {
                parsableByteArray.skipBytes(4);
                final int unsignedShort = parsableByteArray.readUnsignedShort();
                if (parsableByteArray.bytesLeft() < unsignedShort) {
                    parsableByteArray.setPosition(limit);
                    return;
                }
                parsableByteArray.skipBytes(unsignedShort);
            }
            while (parsableByteArray.bytesLeft() >= 4) {
                final int access$100 = peekIntAtPosition(parsableByteArray.data, parsableByteArray.getPosition());
                if (access$100 == 442) {
                    break;
                }
                if (access$100 == 441) {
                    break;
                }
                if (access$100 >>> 8 != 1) {
                    break;
                }
                parsableByteArray.skipBytes(4);
                if (parsableByteArray.bytesLeft() < 2) {
                    parsableByteArray.setPosition(limit);
                    return;
                }
                parsableByteArray.setPosition(Math.min(parsableByteArray.limit(), parsableByteArray.getPosition() + parsableByteArray.readUnsignedShort()));
            }
        }
        
        @Override
        public void onSeekFinished() {
            this.packetBuffer.reset(Util.EMPTY_BYTE_ARRAY);
        }
        
        @Override
        public TimestampSearchResult searchForTimestamp(final ExtractorInput extractorInput, final long n, final OutputFrameHolder outputFrameHolder) throws IOException, InterruptedException {
            final long position = extractorInput.getPosition();
            final int n2 = (int)Math.min(20000L, extractorInput.getLength() - position);
            this.packetBuffer.reset(n2);
            extractorInput.peekFully(this.packetBuffer.data, 0, n2);
            return this.searchForScrValueInBuffer(this.packetBuffer, n, position);
        }
    }
}
