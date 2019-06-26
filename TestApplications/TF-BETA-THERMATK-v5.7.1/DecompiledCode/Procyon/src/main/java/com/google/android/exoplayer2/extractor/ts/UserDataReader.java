// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.text.cea.CeaUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.Format;
import java.util.List;

final class UserDataReader
{
    private final List<Format> closedCaptionFormats;
    private final TrackOutput[] outputs;
    
    public UserDataReader(final List<Format> closedCaptionFormats) {
        this.closedCaptionFormats = closedCaptionFormats;
        this.outputs = new TrackOutput[closedCaptionFormats.size()];
    }
    
    public void consume(final long n, final ParsableByteArray parsableByteArray) {
        if (parsableByteArray.bytesLeft() < 9) {
            return;
        }
        final int int1 = parsableByteArray.readInt();
        final int int2 = parsableByteArray.readInt();
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        if (int1 == 434 && int2 == CeaUtil.USER_DATA_IDENTIFIER_GA94 && unsignedByte == 3) {
            CeaUtil.consumeCcData(n, parsableByteArray, this.outputs);
        }
    }
    
    public void createTracks(final ExtractorOutput extractorOutput, final TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        for (int i = 0; i < this.outputs.length; ++i) {
            trackIdGenerator.generateNewId();
            final TrackOutput track = extractorOutput.track(trackIdGenerator.getTrackId(), 3);
            final Format format = this.closedCaptionFormats.get(i);
            final String sampleMimeType = format.sampleMimeType;
            final boolean b = "application/cea-608".equals(sampleMimeType) || "application/cea-708".equals(sampleMimeType);
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid closed caption mime type provided: ");
            sb.append(sampleMimeType);
            Assertions.checkArgument(b, sb.toString());
            track.format(Format.createTextSampleFormat(trackIdGenerator.getFormatId(), sampleMimeType, null, -1, format.selectionFlags, format.language, format.accessibilityChannel, null, Long.MAX_VALUE, format.initializationData));
            this.outputs[i] = track;
        }
    }
}
