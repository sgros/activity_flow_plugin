// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.extractor.PositionHolder;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;

public final class FlvExtractor implements Extractor
{
    public static final ExtractorsFactory FACTORY;
    private static final int FLV_TAG;
    private AudioTagPayloadReader audioReader;
    private int bytesToNextTagHeader;
    private ExtractorOutput extractorOutput;
    private final ParsableByteArray headerBuffer;
    private long mediaTagTimestampOffsetUs;
    private final ScriptTagPayloadReader metadataReader;
    private boolean outputSeekMap;
    private final ParsableByteArray scratch;
    private int state;
    private final ParsableByteArray tagData;
    private int tagDataSize;
    private final ParsableByteArray tagHeaderBuffer;
    private long tagTimestampUs;
    private int tagType;
    private VideoTagPayloadReader videoReader;
    
    static {
        FACTORY = (ExtractorsFactory)_$$Lambda$FlvExtractor$bd1zICO7f_FQot_hbozdu7LjVyE.INSTANCE;
        FLV_TAG = Util.getIntegerCodeForString("FLV");
    }
    
    public FlvExtractor() {
        this.scratch = new ParsableByteArray(4);
        this.headerBuffer = new ParsableByteArray(9);
        this.tagHeaderBuffer = new ParsableByteArray(11);
        this.tagData = new ParsableByteArray();
        this.metadataReader = new ScriptTagPayloadReader();
        this.state = 1;
        this.mediaTagTimestampOffsetUs = -9223372036854775807L;
    }
    
    private void ensureReadyForMediaOutput() {
        if (!this.outputSeekMap) {
            this.extractorOutput.seekMap(new SeekMap.Unseekable(-9223372036854775807L));
            this.outputSeekMap = true;
        }
        if (this.mediaTagTimestampOffsetUs == -9223372036854775807L) {
            long mediaTagTimestampOffsetUs;
            if (this.metadataReader.getDurationUs() == -9223372036854775807L) {
                mediaTagTimestampOffsetUs = -this.tagTimestampUs;
            }
            else {
                mediaTagTimestampOffsetUs = 0L;
            }
            this.mediaTagTimestampOffsetUs = mediaTagTimestampOffsetUs;
        }
    }
    
    private ParsableByteArray prepareTagData(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.tagDataSize > this.tagData.capacity()) {
            final ParsableByteArray tagData = this.tagData;
            tagData.reset(new byte[Math.max(tagData.capacity() * 2, this.tagDataSize)], 0);
        }
        else {
            this.tagData.setPosition(0);
        }
        this.tagData.setLimit(this.tagDataSize);
        extractorInput.readFully(this.tagData.data, 0, this.tagDataSize);
        return this.tagData;
    }
    
    private boolean readFlvHeader(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final byte[] data = this.headerBuffer.data;
        boolean b = false;
        if (!extractorInput.readFully(data, 0, 9, true)) {
            return false;
        }
        this.headerBuffer.setPosition(0);
        this.headerBuffer.skipBytes(4);
        final int unsignedByte = this.headerBuffer.readUnsignedByte();
        final boolean b2 = (unsignedByte & 0x4) != 0x0;
        if ((unsignedByte & 0x1) != 0x0) {
            b = true;
        }
        if (b2 && this.audioReader == null) {
            this.audioReader = new AudioTagPayloadReader(this.extractorOutput.track(8, 1));
        }
        if (b && this.videoReader == null) {
            this.videoReader = new VideoTagPayloadReader(this.extractorOutput.track(9, 2));
        }
        this.extractorOutput.endTracks();
        this.bytesToNextTagHeader = this.headerBuffer.readInt() - 9 + 4;
        this.state = 2;
        return true;
    }
    
    private boolean readTagData(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final int tagType = this.tagType;
        final boolean b = true;
        boolean b2;
        if (tagType == 8 && this.audioReader != null) {
            this.ensureReadyForMediaOutput();
            this.audioReader.consume(this.prepareTagData(extractorInput), this.mediaTagTimestampOffsetUs + this.tagTimestampUs);
            b2 = b;
        }
        else if (this.tagType == 9 && this.videoReader != null) {
            this.ensureReadyForMediaOutput();
            this.videoReader.consume(this.prepareTagData(extractorInput), this.mediaTagTimestampOffsetUs + this.tagTimestampUs);
            b2 = b;
        }
        else if (this.tagType == 18 && !this.outputSeekMap) {
            this.metadataReader.consume(this.prepareTagData(extractorInput), this.tagTimestampUs);
            final long durationUs = this.metadataReader.getDurationUs();
            b2 = b;
            if (durationUs != -9223372036854775807L) {
                this.extractorOutput.seekMap(new SeekMap.Unseekable(durationUs));
                this.outputSeekMap = true;
                b2 = b;
            }
        }
        else {
            extractorInput.skipFully(this.tagDataSize);
            b2 = false;
        }
        this.bytesToNextTagHeader = 4;
        this.state = 2;
        return b2;
    }
    
    private boolean readTagHeader(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (!extractorInput.readFully(this.tagHeaderBuffer.data, 0, 11, true)) {
            return false;
        }
        this.tagHeaderBuffer.setPosition(0);
        this.tagType = this.tagHeaderBuffer.readUnsignedByte();
        this.tagDataSize = this.tagHeaderBuffer.readUnsignedInt24();
        this.tagTimestampUs = this.tagHeaderBuffer.readUnsignedInt24();
        this.tagTimestampUs = ((long)(this.tagHeaderBuffer.readUnsignedByte() << 24) | this.tagTimestampUs) * 1000L;
        this.tagHeaderBuffer.skipBytes(3);
        this.state = 4;
        return true;
    }
    
    private void skipToTagHeader(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.skipFully(this.bytesToNextTagHeader);
        this.bytesToNextTagHeader = 0;
        this.state = 3;
    }
    
    @Override
    public void init(final ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        while (true) {
            final int state = this.state;
            if (state != 1) {
                if (state != 2) {
                    if (state != 3) {
                        if (state != 4) {
                            throw new IllegalStateException();
                        }
                        if (this.readTagData(extractorInput)) {
                            return 0;
                        }
                        continue;
                    }
                    else {
                        if (!this.readTagHeader(extractorInput)) {
                            return -1;
                        }
                        continue;
                    }
                }
                else {
                    this.skipToTagHeader(extractorInput);
                }
            }
            else {
                if (!this.readFlvHeader(extractorInput)) {
                    return -1;
                }
                continue;
            }
        }
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void seek(final long n, final long n2) {
        this.state = 1;
        this.mediaTagTimestampOffsetUs = -9223372036854775807L;
        this.bytesToNextTagHeader = 0;
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final byte[] data = this.scratch.data;
        boolean b = false;
        extractorInput.peekFully(data, 0, 3);
        this.scratch.setPosition(0);
        if (this.scratch.readUnsignedInt24() != FlvExtractor.FLV_TAG) {
            return false;
        }
        extractorInput.peekFully(this.scratch.data, 0, 2);
        this.scratch.setPosition(0);
        if ((this.scratch.readUnsignedShort() & 0xFA) != 0x0) {
            return false;
        }
        extractorInput.peekFully(this.scratch.data, 0, 4);
        this.scratch.setPosition(0);
        final int int1 = this.scratch.readInt();
        extractorInput.resetPeekPosition();
        extractorInput.advancePeekPosition(int1);
        extractorInput.peekFully(this.scratch.data, 0, 4);
        this.scratch.setPosition(0);
        if (this.scratch.readInt() == 0) {
            b = true;
        }
        return b;
    }
}
