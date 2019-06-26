// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.wav;

import java.io.IOException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.List;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;

public final class WavExtractor implements Extractor
{
    public static final ExtractorsFactory FACTORY;
    private int bytesPerFrame;
    private ExtractorOutput extractorOutput;
    private int pendingBytes;
    private TrackOutput trackOutput;
    private WavHeader wavHeader;
    
    static {
        FACTORY = (ExtractorsFactory)_$$Lambda$WavExtractor$5r6M_S0QCNNj_Xavzq9WwuFHep0.INSTANCE;
    }
    
    @Override
    public void init(final ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.trackOutput = extractorOutput.track(0, 1);
        this.wavHeader = null;
        extractorOutput.endTracks();
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        if (this.wavHeader == null) {
            this.wavHeader = WavHeaderReader.peek(extractorInput);
            final WavHeader wavHeader = this.wavHeader;
            if (wavHeader == null) {
                throw new ParserException("Unsupported or unrecognized wav header.");
            }
            this.trackOutput.format(Format.createAudioSampleFormat(null, "audio/raw", null, wavHeader.getBitrate(), 32768, this.wavHeader.getNumChannels(), this.wavHeader.getSampleRateHz(), this.wavHeader.getEncoding(), null, null, 0, null));
            this.bytesPerFrame = this.wavHeader.getBytesPerFrame();
        }
        if (!this.wavHeader.hasDataBounds()) {
            WavHeaderReader.skipToData(extractorInput, this.wavHeader);
            this.extractorOutput.seekMap(this.wavHeader);
        }
        final long dataLimit = this.wavHeader.getDataLimit();
        int n = 0;
        Assertions.checkState(dataLimit != -1L);
        final long b = dataLimit - extractorInput.getPosition();
        if (b <= 0L) {
            return -1;
        }
        final int sampleData = this.trackOutput.sampleData(extractorInput, (int)Math.min(32768 - this.pendingBytes, b), true);
        if (sampleData != -1) {
            this.pendingBytes += sampleData;
        }
        final int n2 = this.pendingBytes / this.bytesPerFrame;
        if (n2 > 0) {
            final long timeUs = this.wavHeader.getTimeUs(extractorInput.getPosition() - this.pendingBytes);
            final int n3 = n2 * this.bytesPerFrame;
            this.pendingBytes -= n3;
            this.trackOutput.sampleMetadata(timeUs, 1, n3, this.pendingBytes, null);
        }
        if (sampleData == -1) {
            n = -1;
        }
        return n;
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void seek(final long n, final long n2) {
        this.pendingBytes = 0;
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        return WavHeaderReader.peek(extractorInput) != null;
    }
}
