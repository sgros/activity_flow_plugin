// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp4;

import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;

final class TrackFragment
{
    public long atomPosition;
    public long auxiliaryDataPosition;
    public long dataPosition;
    public boolean definesEncryptionData;
    public DefaultSampleValues header;
    public long nextFragmentDecodeTime;
    public int[] sampleCompositionTimeOffsetTable;
    public int sampleCount;
    public long[] sampleDecodingTimeTable;
    public ParsableByteArray sampleEncryptionData;
    public int sampleEncryptionDataLength;
    public boolean sampleEncryptionDataNeedsFill;
    public boolean[] sampleHasSubsampleEncryptionTable;
    public boolean[] sampleIsSyncFrameTable;
    public int[] sampleSizeTable;
    public TrackEncryptionBox trackEncryptionBox;
    public int trunCount;
    public long[] trunDataPosition;
    public int[] trunLength;
    
    public void fillEncryptionData(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.readFully(this.sampleEncryptionData.data, 0, this.sampleEncryptionDataLength);
        this.sampleEncryptionData.setPosition(0);
        this.sampleEncryptionDataNeedsFill = false;
    }
    
    public void fillEncryptionData(final ParsableByteArray parsableByteArray) {
        parsableByteArray.readBytes(this.sampleEncryptionData.data, 0, this.sampleEncryptionDataLength);
        this.sampleEncryptionData.setPosition(0);
        this.sampleEncryptionDataNeedsFill = false;
    }
    
    public long getSamplePresentationTime(final int n) {
        return this.sampleDecodingTimeTable[n] + this.sampleCompositionTimeOffsetTable[n];
    }
    
    public void initEncryptionData(final int sampleEncryptionDataLength) {
        final ParsableByteArray sampleEncryptionData = this.sampleEncryptionData;
        if (sampleEncryptionData == null || sampleEncryptionData.limit() < sampleEncryptionDataLength) {
            this.sampleEncryptionData = new ParsableByteArray(sampleEncryptionDataLength);
        }
        this.sampleEncryptionDataLength = sampleEncryptionDataLength;
        this.definesEncryptionData = true;
        this.sampleEncryptionDataNeedsFill = true;
    }
    
    public void initTables(int trunCount, final int sampleCount) {
        this.trunCount = trunCount;
        this.sampleCount = sampleCount;
        final int[] trunLength = this.trunLength;
        if (trunLength == null || trunLength.length < trunCount) {
            this.trunDataPosition = new long[trunCount];
            this.trunLength = new int[trunCount];
        }
        final int[] sampleSizeTable = this.sampleSizeTable;
        if (sampleSizeTable == null || sampleSizeTable.length < sampleCount) {
            trunCount = sampleCount * 125 / 100;
            this.sampleSizeTable = new int[trunCount];
            this.sampleCompositionTimeOffsetTable = new int[trunCount];
            this.sampleDecodingTimeTable = new long[trunCount];
            this.sampleIsSyncFrameTable = new boolean[trunCount];
            this.sampleHasSubsampleEncryptionTable = new boolean[trunCount];
        }
    }
    
    public void reset() {
        this.trunCount = 0;
        this.nextFragmentDecodeTime = 0L;
        this.definesEncryptionData = false;
        this.sampleEncryptionDataNeedsFill = false;
        this.trackEncryptionBox = null;
    }
    
    public boolean sampleHasSubsampleEncryptionTable(final int n) {
        return this.definesEncryptionData && this.sampleHasSubsampleEncryptionTable[n];
    }
}
