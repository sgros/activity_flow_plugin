package com.google.android.exoplayer2.extractor.p002ts;

import com.google.android.exoplayer2.extractor.BinarySearchSeeker;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker.DefaultSeekTimestampConverter;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker.OutputFrameHolder;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker.TimestampSearchResult;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

/* renamed from: com.google.android.exoplayer2.extractor.ts.PsBinarySearchSeeker */
final class PsBinarySearchSeeker extends BinarySearchSeeker {

    /* renamed from: com.google.android.exoplayer2.extractor.ts.PsBinarySearchSeeker$PsScrSeeker */
    private static final class PsScrSeeker implements TimestampSeeker {
        private final ParsableByteArray packetBuffer;
        private final TimestampAdjuster scrTimestampAdjuster;

        private PsScrSeeker(TimestampAdjuster timestampAdjuster) {
            this.scrTimestampAdjuster = timestampAdjuster;
            this.packetBuffer = new ParsableByteArray();
        }

        public TimestampSearchResult searchForTimestamp(ExtractorInput extractorInput, long j, OutputFrameHolder outputFrameHolder) throws IOException, InterruptedException {
            long position = extractorInput.getPosition();
            int min = (int) Math.min(20000, extractorInput.getLength() - position);
            this.packetBuffer.reset(min);
            extractorInput.peekFully(this.packetBuffer.data, 0, min);
            return searchForScrValueInBuffer(this.packetBuffer, j, position);
        }

        public void onSeekFinished() {
            this.packetBuffer.reset(Util.EMPTY_BYTE_ARRAY);
        }

        private TimestampSearchResult searchForScrValueInBuffer(ParsableByteArray parsableByteArray, long j, long j2) {
            int i = -1;
            long j3 = -9223372036854775807L;
            int i2 = -1;
            while (parsableByteArray.bytesLeft() >= 4) {
                if (PsBinarySearchSeeker.peekIntAtPosition(parsableByteArray.data, parsableByteArray.getPosition()) != 442) {
                    parsableByteArray.skipBytes(1);
                } else {
                    parsableByteArray.skipBytes(4);
                    long readScrValueFromPack = PsDurationReader.readScrValueFromPack(parsableByteArray);
                    if (readScrValueFromPack != -9223372036854775807L) {
                        readScrValueFromPack = this.scrTimestampAdjuster.adjustTsTimestamp(readScrValueFromPack);
                        if (readScrValueFromPack > j) {
                            if (j3 == -9223372036854775807L) {
                                return TimestampSearchResult.overestimatedResult(readScrValueFromPack, j2);
                            }
                            return TimestampSearchResult.targetFoundResult(j2 + ((long) i2));
                        } else if (100000 + readScrValueFromPack > j) {
                            return TimestampSearchResult.targetFoundResult(j2 + ((long) parsableByteArray.getPosition()));
                        } else {
                            i2 = parsableByteArray.getPosition();
                            j3 = readScrValueFromPack;
                        }
                    }
                    PsScrSeeker.skipToEndOfCurrentPack(parsableByteArray);
                    i = parsableByteArray.getPosition();
                }
            }
            if (j3 != -9223372036854775807L) {
                return TimestampSearchResult.underestimatedResult(j3, j2 + ((long) i));
            }
            return TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
        }

        private static void skipToEndOfCurrentPack(ParsableByteArray parsableByteArray) {
            int limit = parsableByteArray.limit();
            if (parsableByteArray.bytesLeft() < 10) {
                parsableByteArray.setPosition(limit);
                return;
            }
            parsableByteArray.skipBytes(9);
            int readUnsignedByte = parsableByteArray.readUnsignedByte() & 7;
            if (parsableByteArray.bytesLeft() < readUnsignedByte) {
                parsableByteArray.setPosition(limit);
                return;
            }
            parsableByteArray.skipBytes(readUnsignedByte);
            if (parsableByteArray.bytesLeft() < 4) {
                parsableByteArray.setPosition(limit);
                return;
            }
            if (PsBinarySearchSeeker.peekIntAtPosition(parsableByteArray.data, parsableByteArray.getPosition()) == 443) {
                parsableByteArray.skipBytes(4);
                readUnsignedByte = parsableByteArray.readUnsignedShort();
                if (parsableByteArray.bytesLeft() < readUnsignedByte) {
                    parsableByteArray.setPosition(limit);
                    return;
                }
                parsableByteArray.skipBytes(readUnsignedByte);
            }
            while (parsableByteArray.bytesLeft() >= 4) {
                readUnsignedByte = PsBinarySearchSeeker.peekIntAtPosition(parsableByteArray.data, parsableByteArray.getPosition());
                if (readUnsignedByte == 442 || readUnsignedByte == 441 || (readUnsignedByte >>> 8) != 1) {
                    break;
                }
                parsableByteArray.skipBytes(4);
                if (parsableByteArray.bytesLeft() < 2) {
                    parsableByteArray.setPosition(limit);
                    return;
                } else {
                    parsableByteArray.setPosition(Math.min(parsableByteArray.limit(), parsableByteArray.getPosition() + parsableByteArray.readUnsignedShort()));
                }
            }
        }
    }

    public PsBinarySearchSeeker(TimestampAdjuster timestampAdjuster, long j, long j2) {
        super(new DefaultSeekTimestampConverter(), new PsScrSeeker(timestampAdjuster), j, 0, j + 1, 0, j2, 188, 1000);
    }

    private static int peekIntAtPosition(byte[] bArr, int i) {
        return (bArr[i + 3] & NalUnitUtil.EXTENDED_SAR) | ((((bArr[i] & NalUnitUtil.EXTENDED_SAR) << 24) | ((bArr[i + 1] & NalUnitUtil.EXTENDED_SAR) << 16)) | ((bArr[i + 2] & NalUnitUtil.EXTENDED_SAR) << 8));
    }
}
