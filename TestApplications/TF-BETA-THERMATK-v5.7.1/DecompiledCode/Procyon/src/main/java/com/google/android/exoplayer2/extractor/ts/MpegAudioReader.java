// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.List;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;

public final class MpegAudioReader implements ElementaryStreamReader
{
    private String formatId;
    private int frameBytesRead;
    private long frameDurationUs;
    private int frameSize;
    private boolean hasOutputFormat;
    private final MpegAudioHeader header;
    private final ParsableByteArray headerScratch;
    private final String language;
    private boolean lastByteWasFF;
    private TrackOutput output;
    private int state;
    private long timeUs;
    
    public MpegAudioReader() {
        this(null);
    }
    
    public MpegAudioReader(final String language) {
        this.state = 0;
        this.headerScratch = new ParsableByteArray(4);
        this.headerScratch.data[0] = -1;
        this.header = new MpegAudioHeader();
        this.language = language;
    }
    
    private void findHeader(final ParsableByteArray parsableByteArray) {
        final byte[] data = parsableByteArray.data;
        int i;
        int limit;
        for (i = parsableByteArray.getPosition(), limit = parsableByteArray.limit(); i < limit; ++i) {
            final boolean lastByteWasFF = (data[i] & 0xFF) == 0xFF;
            final boolean b = this.lastByteWasFF && (data[i] & 0xE0) == 0xE0;
            this.lastByteWasFF = lastByteWasFF;
            if (b) {
                parsableByteArray.setPosition(i + 1);
                this.lastByteWasFF = false;
                this.headerScratch.data[1] = data[i];
                this.frameBytesRead = 2;
                this.state = 1;
                return;
            }
        }
        parsableByteArray.setPosition(limit);
    }
    
    private void readFrameRemainder(final ParsableByteArray parsableByteArray) {
        final int min = Math.min(parsableByteArray.bytesLeft(), this.frameSize - this.frameBytesRead);
        this.output.sampleData(parsableByteArray, min);
        this.frameBytesRead += min;
        final int frameBytesRead = this.frameBytesRead;
        final int frameSize = this.frameSize;
        if (frameBytesRead < frameSize) {
            return;
        }
        this.output.sampleMetadata(this.timeUs, 1, frameSize, 0, null);
        this.timeUs += this.frameDurationUs;
        this.frameBytesRead = 0;
        this.state = 0;
    }
    
    private void readHeaderRemainder(final ParsableByteArray parsableByteArray) {
        final int min = Math.min(parsableByteArray.bytesLeft(), 4 - this.frameBytesRead);
        parsableByteArray.readBytes(this.headerScratch.data, this.frameBytesRead, min);
        this.frameBytesRead += min;
        if (this.frameBytesRead < 4) {
            return;
        }
        this.headerScratch.setPosition(0);
        if (!MpegAudioHeader.populateHeader(this.headerScratch.readInt(), this.header)) {
            this.frameBytesRead = 0;
            this.state = 1;
            return;
        }
        final MpegAudioHeader header = this.header;
        this.frameSize = header.frameSize;
        if (!this.hasOutputFormat) {
            final long n = header.samplesPerFrame;
            final int sampleRate = header.sampleRate;
            this.frameDurationUs = n * 1000000L / sampleRate;
            this.output.format(Format.createAudioSampleFormat(this.formatId, header.mimeType, null, -1, 4096, header.channels, sampleRate, null, null, 0, this.language));
            this.hasOutputFormat = true;
        }
        this.headerScratch.setPosition(0);
        this.output.sampleData(this.headerScratch, 4);
        this.state = 2;
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray) {
        while (parsableByteArray.bytesLeft() > 0) {
            final int state = this.state;
            if (state != 0) {
                if (state != 1) {
                    if (state != 2) {
                        throw new IllegalStateException();
                    }
                    this.readFrameRemainder(parsableByteArray);
                }
                else {
                    this.readHeaderRemainder(parsableByteArray);
                }
            }
            else {
                this.findHeader(parsableByteArray);
            }
        }
    }
    
    @Override
    public void createTracks(final ExtractorOutput extractorOutput, final TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.formatId = trackIdGenerator.getFormatId();
        this.output = extractorOutput.track(trackIdGenerator.getTrackId(), 1);
    }
    
    @Override
    public void packetFinished() {
    }
    
    @Override
    public void packetStarted(final long timeUs, final int n) {
        this.timeUs = timeUs;
    }
    
    @Override
    public void seek() {
        this.state = 0;
        this.frameBytesRead = 0;
        this.lastByteWasFF = false;
    }
}
