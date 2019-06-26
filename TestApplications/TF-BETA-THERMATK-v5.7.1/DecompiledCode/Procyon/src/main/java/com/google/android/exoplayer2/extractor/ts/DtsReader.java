// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.audio.DtsUtil;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.Format;

public final class DtsReader implements ElementaryStreamReader
{
    private int bytesRead;
    private Format format;
    private String formatId;
    private final ParsableByteArray headerScratchBytes;
    private final String language;
    private TrackOutput output;
    private long sampleDurationUs;
    private int sampleSize;
    private int state;
    private int syncBytes;
    private long timeUs;
    
    public DtsReader(final String language) {
        this.headerScratchBytes = new ParsableByteArray(new byte[18]);
        this.state = 0;
        this.language = language;
    }
    
    private boolean continueRead(final ParsableByteArray parsableByteArray, final byte[] array, final int n) {
        final int min = Math.min(parsableByteArray.bytesLeft(), n - this.bytesRead);
        parsableByteArray.readBytes(array, this.bytesRead, min);
        this.bytesRead += min;
        return this.bytesRead == n;
    }
    
    private void parseHeader() {
        final byte[] data = this.headerScratchBytes.data;
        if (this.format == null) {
            this.format = DtsUtil.parseDtsFormat(data, this.formatId, this.language, null);
            this.output.format(this.format);
        }
        this.sampleSize = DtsUtil.getDtsFrameSize(data);
        this.sampleDurationUs = (int)(DtsUtil.parseDtsAudioSampleCount(data) * 1000000L / this.format.sampleRate);
    }
    
    private boolean skipToNextSync(final ParsableByteArray parsableByteArray) {
        while (parsableByteArray.bytesLeft() > 0) {
            this.syncBytes <<= 8;
            this.syncBytes |= parsableByteArray.readUnsignedByte();
            if (DtsUtil.isSyncWord(this.syncBytes)) {
                final byte[] data = this.headerScratchBytes.data;
                final int syncBytes = this.syncBytes;
                data[0] = (byte)(syncBytes >> 24 & 0xFF);
                data[1] = (byte)(syncBytes >> 16 & 0xFF);
                data[2] = (byte)(syncBytes >> 8 & 0xFF);
                data[3] = (byte)(syncBytes & 0xFF);
                this.bytesRead = 4;
                this.syncBytes = 0;
                return true;
            }
        }
        return false;
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
                    final int min = Math.min(parsableByteArray.bytesLeft(), this.sampleSize - this.bytesRead);
                    this.output.sampleData(parsableByteArray, min);
                    this.bytesRead += min;
                    final int bytesRead = this.bytesRead;
                    final int sampleSize = this.sampleSize;
                    if (bytesRead != sampleSize) {
                        continue;
                    }
                    this.output.sampleMetadata(this.timeUs, 1, sampleSize, 0, null);
                    this.timeUs += this.sampleDurationUs;
                    this.state = 0;
                }
                else {
                    if (!this.continueRead(parsableByteArray, this.headerScratchBytes.data, 18)) {
                        continue;
                    }
                    this.parseHeader();
                    this.headerScratchBytes.setPosition(0);
                    this.output.sampleData(this.headerScratchBytes, 18);
                    this.state = 2;
                }
            }
            else {
                if (!this.skipToNextSync(parsableByteArray)) {
                    continue;
                }
                this.state = 1;
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
        this.bytesRead = 0;
        this.syncBytes = 0;
    }
}
