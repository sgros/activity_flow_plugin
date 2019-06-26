// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ogg;

import java.io.IOException;
import java.util.Arrays;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;

final class OggPacket
{
    private int currentSegmentIndex;
    private final ParsableByteArray packetArray;
    private final OggPageHeader pageHeader;
    private boolean populated;
    private int segmentCount;
    
    OggPacket() {
        this.pageHeader = new OggPageHeader();
        this.packetArray = new ParsableByteArray(new byte[65025], 0);
        this.currentSegmentIndex = -1;
    }
    
    private int calculatePacketSize(final int n) {
        int n2 = 0;
        this.segmentCount = 0;
        int i;
        int n3;
        do {
            final int segmentCount = this.segmentCount;
            final OggPageHeader pageHeader = this.pageHeader;
            n3 = n2;
            if (n + segmentCount >= pageHeader.pageSegmentCount) {
                break;
            }
            final int[] laces = pageHeader.laces;
            this.segmentCount = segmentCount + 1;
            i = laces[segmentCount + n];
            n3 = (n2 += i);
        } while (i == 255);
        return n3;
    }
    
    public OggPageHeader getPageHeader() {
        return this.pageHeader;
    }
    
    public ParsableByteArray getPayload() {
        return this.packetArray;
    }
    
    public boolean populate(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        Assertions.checkState(extractorInput != null);
        if (this.populated) {
            this.populated = false;
            this.packetArray.reset();
        }
        while (!this.populated) {
            if (this.currentSegmentIndex < 0) {
                if (!this.pageHeader.populate(extractorInput, true)) {
                    return false;
                }
                final OggPageHeader pageHeader = this.pageHeader;
                int headerSize = pageHeader.headerSize;
                int currentSegmentIndex;
                if ((pageHeader.type & 0x1) == 0x1 && this.packetArray.limit() == 0) {
                    headerSize += this.calculatePacketSize(0);
                    currentSegmentIndex = this.segmentCount + 0;
                }
                else {
                    currentSegmentIndex = 0;
                }
                extractorInput.skipFully(headerSize);
                this.currentSegmentIndex = currentSegmentIndex;
            }
            final int calculatePacketSize = this.calculatePacketSize(this.currentSegmentIndex);
            final int n = this.currentSegmentIndex + this.segmentCount;
            if (calculatePacketSize > 0) {
                if (this.packetArray.capacity() < this.packetArray.limit() + calculatePacketSize) {
                    final ParsableByteArray packetArray = this.packetArray;
                    packetArray.data = Arrays.copyOf(packetArray.data, packetArray.limit() + calculatePacketSize);
                }
                final ParsableByteArray packetArray2 = this.packetArray;
                extractorInput.readFully(packetArray2.data, packetArray2.limit(), calculatePacketSize);
                final ParsableByteArray packetArray3 = this.packetArray;
                packetArray3.setLimit(packetArray3.limit() + calculatePacketSize);
                this.populated = (this.pageHeader.laces[n - 1] != 255);
            }
            int currentSegmentIndex2;
            if ((currentSegmentIndex2 = n) == this.pageHeader.pageSegmentCount) {
                currentSegmentIndex2 = -1;
            }
            this.currentSegmentIndex = currentSegmentIndex2;
        }
        return true;
    }
    
    public void reset() {
        this.pageHeader.reset();
        this.packetArray.reset();
        this.currentSegmentIndex = -1;
        this.populated = false;
    }
    
    public void trimPayload() {
        final ParsableByteArray packetArray = this.packetArray;
        final byte[] data = packetArray.data;
        if (data.length == 65025) {
            return;
        }
        packetArray.data = Arrays.copyOf(data, Math.max(65025, packetArray.limit()));
    }
}
