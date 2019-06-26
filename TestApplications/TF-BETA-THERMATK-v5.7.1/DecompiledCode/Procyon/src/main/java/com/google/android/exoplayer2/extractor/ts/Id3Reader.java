// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;

public final class Id3Reader implements ElementaryStreamReader
{
    private final ParsableByteArray id3Header;
    private TrackOutput output;
    private int sampleBytesRead;
    private int sampleSize;
    private long sampleTimeUs;
    private boolean writingSample;
    
    public Id3Reader() {
        this.id3Header = new ParsableByteArray(10);
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray) {
        if (!this.writingSample) {
            return;
        }
        final int bytesLeft = parsableByteArray.bytesLeft();
        final int sampleBytesRead = this.sampleBytesRead;
        if (sampleBytesRead < 10) {
            final int min = Math.min(bytesLeft, 10 - sampleBytesRead);
            System.arraycopy(parsableByteArray.data, parsableByteArray.getPosition(), this.id3Header.data, this.sampleBytesRead, min);
            if (this.sampleBytesRead + min == 10) {
                this.id3Header.setPosition(0);
                if (73 != this.id3Header.readUnsignedByte() || 68 != this.id3Header.readUnsignedByte() || 51 != this.id3Header.readUnsignedByte()) {
                    Log.w("Id3Reader", "Discarding invalid ID3 tag");
                    this.writingSample = false;
                    return;
                }
                this.id3Header.skipBytes(3);
                this.sampleSize = this.id3Header.readSynchSafeInt() + 10;
            }
        }
        final int min2 = Math.min(bytesLeft, this.sampleSize - this.sampleBytesRead);
        this.output.sampleData(parsableByteArray, min2);
        this.sampleBytesRead += min2;
    }
    
    @Override
    public void createTracks(final ExtractorOutput extractorOutput, final TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        (this.output = extractorOutput.track(trackIdGenerator.getTrackId(), 4)).format(Format.createSampleFormat(trackIdGenerator.getFormatId(), "application/id3", null, -1, null));
    }
    
    @Override
    public void packetFinished() {
        if (this.writingSample) {
            final int sampleSize = this.sampleSize;
            if (sampleSize != 0) {
                if (this.sampleBytesRead == sampleSize) {
                    this.output.sampleMetadata(this.sampleTimeUs, 1, sampleSize, 0, null);
                    this.writingSample = false;
                }
            }
        }
    }
    
    @Override
    public void packetStarted(final long sampleTimeUs, final int n) {
        if ((n & 0x4) == 0x0) {
            return;
        }
        this.writingSample = true;
        this.sampleTimeUs = sampleTimeUs;
        this.sampleSize = 0;
        this.sampleBytesRead = 0;
    }
    
    @Override
    public void seek() {
        this.writingSample = false;
    }
}
