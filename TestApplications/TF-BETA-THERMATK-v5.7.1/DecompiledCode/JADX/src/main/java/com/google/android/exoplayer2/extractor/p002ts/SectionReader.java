package com.google.android.exoplayer2.extractor.p002ts;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.p002ts.TsPayloadReader.TrackIdGenerator;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;

/* renamed from: com.google.android.exoplayer2.extractor.ts.SectionReader */
public final class SectionReader implements TsPayloadReader {
    private int bytesRead;
    private final SectionPayloadReader reader;
    private final ParsableByteArray sectionData = new ParsableByteArray(32);
    private boolean sectionSyntaxIndicator;
    private int totalSectionLength;
    private boolean waitingForPayloadStart;

    public SectionReader(SectionPayloadReader sectionPayloadReader) {
        this.reader = sectionPayloadReader;
    }

    public void init(TimestampAdjuster timestampAdjuster, ExtractorOutput extractorOutput, TrackIdGenerator trackIdGenerator) {
        this.reader.init(timestampAdjuster, extractorOutput, trackIdGenerator);
        this.waitingForPayloadStart = true;
    }

    public void seek() {
        this.waitingForPayloadStart = true;
    }

    public void consume(ParsableByteArray parsableByteArray, int i) {
        Object obj = (i & 1) != 0 ? 1 : null;
        int readUnsignedByte = obj != null ? parsableByteArray.readUnsignedByte() + parsableByteArray.getPosition() : -1;
        if (this.waitingForPayloadStart) {
            if (obj != null) {
                this.waitingForPayloadStart = false;
                parsableByteArray.setPosition(readUnsignedByte);
                this.bytesRead = 0;
            } else {
                return;
            }
        }
        while (parsableByteArray.bytesLeft() > 0) {
            i = this.bytesRead;
            if (i < 3) {
                if (i == 0) {
                    i = parsableByteArray.readUnsignedByte();
                    parsableByteArray.setPosition(parsableByteArray.getPosition() - 1);
                    if (i == NalUnitUtil.EXTENDED_SAR) {
                        this.waitingForPayloadStart = true;
                        return;
                    }
                }
                i = Math.min(parsableByteArray.bytesLeft(), 3 - this.bytesRead);
                parsableByteArray.readBytes(this.sectionData.data, this.bytesRead, i);
                this.bytesRead += i;
                if (this.bytesRead == 3) {
                    this.sectionData.reset(3);
                    this.sectionData.skipBytes(1);
                    i = this.sectionData.readUnsignedByte();
                    int readUnsignedByte2 = this.sectionData.readUnsignedByte();
                    this.sectionSyntaxIndicator = (i & 128) != 0;
                    this.totalSectionLength = (((i & 15) << 8) | readUnsignedByte2) + 3;
                    i = this.sectionData.capacity();
                    readUnsignedByte2 = this.totalSectionLength;
                    if (i < readUnsignedByte2) {
                        ParsableByteArray parsableByteArray2 = this.sectionData;
                        byte[] bArr = parsableByteArray2.data;
                        parsableByteArray2.reset(Math.min(4098, Math.max(readUnsignedByte2, bArr.length * 2)));
                        System.arraycopy(bArr, 0, this.sectionData.data, 0, 3);
                    }
                }
            } else {
                i = Math.min(parsableByteArray.bytesLeft(), this.totalSectionLength - this.bytesRead);
                parsableByteArray.readBytes(this.sectionData.data, this.bytesRead, i);
                this.bytesRead += i;
                i = this.bytesRead;
                readUnsignedByte = this.totalSectionLength;
                if (i != readUnsignedByte) {
                    continue;
                } else {
                    if (!this.sectionSyntaxIndicator) {
                        this.sectionData.reset(readUnsignedByte);
                    } else if (Util.crc(this.sectionData.data, 0, readUnsignedByte, -1) != 0) {
                        this.waitingForPayloadStart = true;
                        return;
                    } else {
                        this.sectionData.reset(this.totalSectionLength - 4);
                    }
                    this.reader.consume(this.sectionData);
                    this.bytesRead = 0;
                }
            }
        }
    }
}
