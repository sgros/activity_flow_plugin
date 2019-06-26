// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.ParsableByteArray;

public final class SectionReader implements TsPayloadReader
{
    private int bytesRead;
    private final SectionPayloadReader reader;
    private final ParsableByteArray sectionData;
    private boolean sectionSyntaxIndicator;
    private int totalSectionLength;
    private boolean waitingForPayloadStart;
    
    public SectionReader(final SectionPayloadReader reader) {
        this.reader = reader;
        this.sectionData = new ParsableByteArray(32);
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray, int a) {
        if ((a & 0x1) != 0x0) {
            a = 1;
        }
        else {
            a = 0;
        }
        int position;
        if (a != 0) {
            position = parsableByteArray.readUnsignedByte() + parsableByteArray.getPosition();
        }
        else {
            position = -1;
        }
        if (this.waitingForPayloadStart) {
            if (a == 0) {
                return;
            }
            this.waitingForPayloadStart = false;
            parsableByteArray.setPosition(position);
            this.bytesRead = 0;
        }
        while (parsableByteArray.bytesLeft() > 0) {
            a = this.bytesRead;
            if (a < 3) {
                if (a == 0) {
                    a = parsableByteArray.readUnsignedByte();
                    parsableByteArray.setPosition(parsableByteArray.getPosition() - 1);
                    if (a == 255) {
                        this.waitingForPayloadStart = true;
                        return;
                    }
                }
                a = Math.min(parsableByteArray.bytesLeft(), 3 - this.bytesRead);
                parsableByteArray.readBytes(this.sectionData.data, this.bytesRead, a);
                this.bytesRead += a;
                if (this.bytesRead != 3) {
                    continue;
                }
                this.sectionData.reset(3);
                this.sectionData.skipBytes(1);
                a = this.sectionData.readUnsignedByte();
                final int unsignedByte = this.sectionData.readUnsignedByte();
                this.sectionSyntaxIndicator = ((a & 0x80) != 0x0);
                this.totalSectionLength = ((a & 0xF) << 8 | unsignedByte) + 3;
                final int capacity = this.sectionData.capacity();
                a = this.totalSectionLength;
                if (capacity >= a) {
                    continue;
                }
                final ParsableByteArray sectionData = this.sectionData;
                final byte[] data = sectionData.data;
                sectionData.reset(Math.min(4098, Math.max(a, data.length * 2)));
                System.arraycopy(data, 0, this.sectionData.data, 0, 3);
            }
            else {
                a = Math.min(parsableByteArray.bytesLeft(), this.totalSectionLength - this.bytesRead);
                parsableByteArray.readBytes(this.sectionData.data, this.bytesRead, a);
                this.bytesRead += a;
                a = this.bytesRead;
                final int totalSectionLength = this.totalSectionLength;
                if (a != totalSectionLength) {
                    continue;
                }
                if (this.sectionSyntaxIndicator) {
                    if (Util.crc(this.sectionData.data, 0, totalSectionLength, -1) != 0) {
                        this.waitingForPayloadStart = true;
                        return;
                    }
                    this.sectionData.reset(this.totalSectionLength - 4);
                }
                else {
                    this.sectionData.reset(totalSectionLength);
                }
                this.reader.consume(this.sectionData);
                this.bytesRead = 0;
            }
        }
    }
    
    @Override
    public void init(final TimestampAdjuster timestampAdjuster, final ExtractorOutput extractorOutput, final TrackIdGenerator trackIdGenerator) {
        this.reader.init(timestampAdjuster, extractorOutput, trackIdGenerator);
        this.waitingForPayloadStart = true;
    }
    
    @Override
    public void seek() {
        this.waitingForPayloadStart = true;
    }
}
