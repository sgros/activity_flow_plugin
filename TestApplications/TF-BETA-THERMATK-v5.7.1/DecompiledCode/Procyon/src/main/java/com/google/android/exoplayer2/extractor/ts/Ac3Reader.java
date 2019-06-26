// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.List;
import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.Format;

public final class Ac3Reader implements ElementaryStreamReader
{
    private int bytesRead;
    private Format format;
    private final ParsableBitArray headerScratchBits;
    private final ParsableByteArray headerScratchBytes;
    private final String language;
    private boolean lastByteWas0B;
    private TrackOutput output;
    private long sampleDurationUs;
    private int sampleSize;
    private int state;
    private long timeUs;
    private String trackFormatId;
    
    public Ac3Reader() {
        this(null);
    }
    
    public Ac3Reader(final String language) {
        this.headerScratchBits = new ParsableBitArray(new byte[128]);
        this.headerScratchBytes = new ParsableByteArray(this.headerScratchBits.data);
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
        this.headerScratchBits.setPosition(0);
        final Ac3Util.SyncFrameInfo ac3SyncframeInfo = Ac3Util.parseAc3SyncframeInfo(this.headerScratchBits);
        final Format format = this.format;
        if (format == null || ac3SyncframeInfo.channelCount != format.channelCount || ac3SyncframeInfo.sampleRate != format.sampleRate || ac3SyncframeInfo.mimeType != format.sampleMimeType) {
            this.format = Format.createAudioSampleFormat(this.trackFormatId, ac3SyncframeInfo.mimeType, null, -1, -1, ac3SyncframeInfo.channelCount, ac3SyncframeInfo.sampleRate, null, null, 0, this.language);
            this.output.format(this.format);
        }
        this.sampleSize = ac3SyncframeInfo.frameSize;
        this.sampleDurationUs = ac3SyncframeInfo.sampleCount * 1000000L / this.format.sampleRate;
    }
    
    private boolean skipToNextSync(final ParsableByteArray parsableByteArray) {
        while (true) {
            final int bytesLeft = parsableByteArray.bytesLeft();
            final boolean b = false;
            boolean lastByteWas0B = false;
            if (bytesLeft <= 0) {
                return false;
            }
            if (!this.lastByteWas0B) {
                if (parsableByteArray.readUnsignedByte() == 11) {
                    lastByteWas0B = true;
                }
                this.lastByteWas0B = lastByteWas0B;
            }
            else {
                final int unsignedByte = parsableByteArray.readUnsignedByte();
                if (unsignedByte == 119) {
                    this.lastByteWas0B = false;
                    return true;
                }
                boolean lastByteWas0B2 = b;
                if (unsignedByte == 11) {
                    lastByteWas0B2 = true;
                }
                this.lastByteWas0B = lastByteWas0B2;
            }
        }
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray) {
        while (parsableByteArray.bytesLeft() > 0) {
            final int state = this.state;
            if (state != 0) {
                if (state != 1) {
                    if (state != 2) {
                        continue;
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
                    if (!this.continueRead(parsableByteArray, this.headerScratchBytes.data, 128)) {
                        continue;
                    }
                    this.parseHeader();
                    this.headerScratchBytes.setPosition(0);
                    this.output.sampleData(this.headerScratchBytes, 128);
                    this.state = 2;
                }
            }
            else {
                if (!this.skipToNextSync(parsableByteArray)) {
                    continue;
                }
                this.state = 1;
                final byte[] data = this.headerScratchBytes.data;
                data[0] = 11;
                data[1] = 119;
                this.bytesRead = 2;
            }
        }
    }
    
    @Override
    public void createTracks(final ExtractorOutput extractorOutput, final TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.trackFormatId = trackIdGenerator.getFormatId();
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
        this.lastByteWas0B = false;
    }
}
