package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
import java.util.Arrays;

final class OggPacket {
    private int currentSegmentIndex = -1;
    private final ParsableByteArray packetArray = new ParsableByteArray(new byte[65025], 0);
    private final OggPageHeader pageHeader = new OggPageHeader();
    private boolean populated;
    private int segmentCount;

    OggPacket() {
    }

    public void reset() {
        this.pageHeader.reset();
        this.packetArray.reset();
        this.currentSegmentIndex = -1;
        this.populated = false;
    }

    public boolean populate(ExtractorInput extractorInput) throws IOException, InterruptedException {
        Assertions.checkState(extractorInput != null);
        if (this.populated) {
            this.populated = false;
            this.packetArray.reset();
        }
        while (!this.populated) {
            int i;
            int i2;
            if (this.currentSegmentIndex < 0) {
                if (!this.pageHeader.populate(extractorInput, true)) {
                    return false;
                }
                OggPageHeader oggPageHeader = this.pageHeader;
                i = oggPageHeader.headerSize;
                if ((oggPageHeader.type & 1) == 1 && this.packetArray.limit() == 0) {
                    i += calculatePacketSize(0);
                    i2 = this.segmentCount + 0;
                } else {
                    i2 = 0;
                }
                extractorInput.skipFully(i);
                this.currentSegmentIndex = i2;
            }
            i2 = calculatePacketSize(this.currentSegmentIndex);
            i = this.currentSegmentIndex + this.segmentCount;
            if (i2 > 0) {
                ParsableByteArray parsableByteArray;
                if (this.packetArray.capacity() < this.packetArray.limit() + i2) {
                    parsableByteArray = this.packetArray;
                    parsableByteArray.data = Arrays.copyOf(parsableByteArray.data, parsableByteArray.limit() + i2);
                }
                parsableByteArray = this.packetArray;
                extractorInput.readFully(parsableByteArray.data, parsableByteArray.limit(), i2);
                parsableByteArray = this.packetArray;
                parsableByteArray.setLimit(parsableByteArray.limit() + i2);
                this.populated = this.pageHeader.laces[i + -1] != 255;
            }
            if (i == this.pageHeader.pageSegmentCount) {
                i = -1;
            }
            this.currentSegmentIndex = i;
        }
        return true;
    }

    public OggPageHeader getPageHeader() {
        return this.pageHeader;
    }

    public ParsableByteArray getPayload() {
        return this.packetArray;
    }

    public void trimPayload() {
        ParsableByteArray parsableByteArray = this.packetArray;
        byte[] bArr = parsableByteArray.data;
        if (bArr.length != 65025) {
            parsableByteArray.data = Arrays.copyOf(bArr, Math.max(65025, parsableByteArray.limit()));
        }
    }

    private int calculatePacketSize(int i) {
        int i2 = 0;
        this.segmentCount = 0;
        int i3;
        do {
            i3 = this.segmentCount;
            int i4 = i + i3;
            OggPageHeader oggPageHeader = this.pageHeader;
            if (i4 >= oggPageHeader.pageSegmentCount) {
                break;
            }
            int[] iArr = oggPageHeader.laces;
            this.segmentCount = i3 + 1;
            i3 = iArr[i3 + i];
            i2 += i3;
        } while (i3 == NalUnitUtil.EXTENDED_SAR);
        return i2;
    }
}
