// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.Format;
import java.util.Collections;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.List;
import com.google.android.exoplayer2.extractor.TrackOutput;

public final class DvbSubtitleReader implements ElementaryStreamReader
{
    private int bytesToCheck;
    private final TrackOutput[] outputs;
    private int sampleBytesWritten;
    private long sampleTimeUs;
    private final List<TsPayloadReader.DvbSubtitleInfo> subtitleInfos;
    private boolean writingSample;
    
    public DvbSubtitleReader(final List<TsPayloadReader.DvbSubtitleInfo> subtitleInfos) {
        this.subtitleInfos = subtitleInfos;
        this.outputs = new TrackOutput[subtitleInfos.size()];
    }
    
    private boolean checkNextByte(final ParsableByteArray parsableByteArray, final int n) {
        if (parsableByteArray.bytesLeft() == 0) {
            return false;
        }
        if (parsableByteArray.readUnsignedByte() != n) {
            this.writingSample = false;
        }
        --this.bytesToCheck;
        return this.writingSample;
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray) {
        if (this.writingSample) {
            if (this.bytesToCheck == 2 && !this.checkNextByte(parsableByteArray, 32)) {
                return;
            }
            final int bytesToCheck = this.bytesToCheck;
            int i = 0;
            if (bytesToCheck == 1 && !this.checkNextByte(parsableByteArray, 0)) {
                return;
            }
            final int position = parsableByteArray.getPosition();
            final int bytesLeft = parsableByteArray.bytesLeft();
            for (TrackOutput[] outputs = this.outputs; i < outputs.length; ++i) {
                final TrackOutput trackOutput = outputs[i];
                parsableByteArray.setPosition(position);
                trackOutput.sampleData(parsableByteArray, bytesLeft);
            }
            this.sampleBytesWritten += bytesLeft;
        }
    }
    
    @Override
    public void createTracks(final ExtractorOutput extractorOutput, final TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        for (int i = 0; i < this.outputs.length; ++i) {
            final TsPayloadReader.DvbSubtitleInfo dvbSubtitleInfo = this.subtitleInfos.get(i);
            trackIdGenerator.generateNewId();
            final TrackOutput track = extractorOutput.track(trackIdGenerator.getTrackId(), 3);
            track.format(Format.createImageSampleFormat(trackIdGenerator.getFormatId(), "application/dvbsubs", null, -1, 0, Collections.singletonList(dvbSubtitleInfo.initializationData), dvbSubtitleInfo.language, null));
            this.outputs[i] = track;
        }
    }
    
    @Override
    public void packetFinished() {
        if (this.writingSample) {
            final TrackOutput[] outputs = this.outputs;
            for (int length = outputs.length, i = 0; i < length; ++i) {
                outputs[i].sampleMetadata(this.sampleTimeUs, 1, this.sampleBytesWritten, 0, null);
            }
            this.writingSample = false;
        }
    }
    
    @Override
    public void packetStarted(final long sampleTimeUs, final int n) {
        if ((n & 0x4) == 0x0) {
            return;
        }
        this.writingSample = true;
        this.sampleTimeUs = sampleTimeUs;
        this.sampleBytesWritten = 0;
        this.bytesToCheck = 2;
    }
    
    @Override
    public void seek() {
        this.writingSample = false;
    }
}
