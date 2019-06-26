// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.PositionHolder;
import java.io.IOException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;

abstract class StreamReader
{
    private long currentGranule;
    private ExtractorOutput extractorOutput;
    private boolean formatSet;
    private long lengthOfReadPacket;
    private final OggPacket oggPacket;
    private OggSeeker oggSeeker;
    private long payloadStartPosition;
    private int sampleRate;
    private boolean seekMapSet;
    private SetupData setupData;
    private int state;
    private long targetGranule;
    private TrackOutput trackOutput;
    
    public StreamReader() {
        this.oggPacket = new OggPacket();
    }
    
    private int readHeaders(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        boolean headers = true;
        while (headers) {
            if (!this.oggPacket.populate(extractorInput)) {
                this.state = 3;
                return -1;
            }
            this.lengthOfReadPacket = extractorInput.getPosition() - this.payloadStartPosition;
            final boolean b = headers = this.readHeaders(this.oggPacket.getPayload(), this.payloadStartPosition, this.setupData);
            if (!b) {
                continue;
            }
            this.payloadStartPosition = extractorInput.getPosition();
            headers = b;
        }
        final Format format = this.setupData.format;
        this.sampleRate = format.sampleRate;
        if (!this.formatSet) {
            this.trackOutput.format(format);
            this.formatSet = true;
        }
        final OggSeeker oggSeeker = this.setupData.oggSeeker;
        if (oggSeeker != null) {
            this.oggSeeker = oggSeeker;
        }
        else if (extractorInput.getLength() == -1L) {
            this.oggSeeker = new UnseekableOggSeeker();
        }
        else {
            final OggPageHeader pageHeader = this.oggPacket.getPageHeader();
            this.oggSeeker = new DefaultOggSeeker(this.payloadStartPosition, extractorInput.getLength(), this, pageHeader.headerSize + pageHeader.bodySize, pageHeader.granulePosition, (pageHeader.type & 0x4) != 0x0);
        }
        this.setupData = null;
        this.state = 2;
        this.oggPacket.trimPayload();
        return 0;
    }
    
    private int readPayload(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        final long read = this.oggSeeker.read(extractorInput);
        if (read >= 0L) {
            positionHolder.position = read;
            return 1;
        }
        if (read < -1L) {
            this.onSeekEnd(-(read + 2L));
        }
        if (!this.seekMapSet) {
            this.extractorOutput.seekMap(this.oggSeeker.createSeekMap());
            this.seekMapSet = true;
        }
        if (this.lengthOfReadPacket <= 0L && !this.oggPacket.populate(extractorInput)) {
            this.state = 3;
            return -1;
        }
        this.lengthOfReadPacket = 0L;
        final ParsableByteArray payload = this.oggPacket.getPayload();
        final long preparePayload = this.preparePayload(payload);
        if (preparePayload >= 0L) {
            final long currentGranule = this.currentGranule;
            if (currentGranule + preparePayload >= this.targetGranule) {
                final long convertGranuleToTime = this.convertGranuleToTime(currentGranule);
                this.trackOutput.sampleData(payload, payload.limit());
                this.trackOutput.sampleMetadata(convertGranuleToTime, 1, payload.limit(), 0, null);
                this.targetGranule = -1L;
            }
        }
        this.currentGranule += preparePayload;
        return 0;
    }
    
    protected long convertGranuleToTime(final long n) {
        return n * 1000000L / this.sampleRate;
    }
    
    protected long convertTimeToGranule(final long n) {
        return this.sampleRate * n / 1000000L;
    }
    
    void init(final ExtractorOutput extractorOutput, final TrackOutput trackOutput) {
        this.extractorOutput = extractorOutput;
        this.trackOutput = trackOutput;
        this.reset(true);
    }
    
    protected void onSeekEnd(final long currentGranule) {
        this.currentGranule = currentGranule;
    }
    
    protected abstract long preparePayload(final ParsableByteArray p0);
    
    final int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        final int state = this.state;
        if (state == 0) {
            return this.readHeaders(extractorInput);
        }
        if (state == 1) {
            extractorInput.skipFully((int)this.payloadStartPosition);
            this.state = 2;
            return 0;
        }
        if (state == 2) {
            return this.readPayload(extractorInput, positionHolder);
        }
        throw new IllegalStateException();
    }
    
    protected abstract boolean readHeaders(final ParsableByteArray p0, final long p1, final SetupData p2) throws IOException, InterruptedException;
    
    protected void reset(final boolean b) {
        if (b) {
            this.setupData = new SetupData();
            this.payloadStartPosition = 0L;
            this.state = 0;
        }
        else {
            this.state = 1;
        }
        this.targetGranule = -1L;
        this.currentGranule = 0L;
    }
    
    final void seek(final long n, final long n2) {
        this.oggPacket.reset();
        if (n == 0L) {
            this.reset(this.seekMapSet ^ true);
        }
        else if (this.state != 0) {
            this.targetGranule = this.oggSeeker.startSeek(n2);
            this.state = 2;
        }
    }
    
    static class SetupData
    {
        Format format;
        OggSeeker oggSeeker;
    }
    
    private static final class UnseekableOggSeeker implements OggSeeker
    {
        @Override
        public SeekMap createSeekMap() {
            return new SeekMap.Unseekable(-9223372036854775807L);
        }
        
        @Override
        public long read(final ExtractorInput extractorInput) throws IOException, InterruptedException {
            return -1L;
        }
        
        @Override
        public long startSeek(final long n) {
            return 0L;
        }
    }
}
