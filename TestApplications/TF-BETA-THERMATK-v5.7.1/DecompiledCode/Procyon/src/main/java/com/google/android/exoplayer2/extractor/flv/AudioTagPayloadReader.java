// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.ParserException;
import android.util.Pair;
import java.util.Collections;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.List;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.TrackOutput;

final class AudioTagPayloadReader extends TagPayloadReader
{
    private static final int[] AUDIO_SAMPLING_RATE_TABLE;
    private int audioFormat;
    private boolean hasOutputFormat;
    private boolean hasParsedAudioDataHeader;
    
    static {
        AUDIO_SAMPLING_RATE_TABLE = new int[] { 5512, 11025, 22050, 44100 };
    }
    
    public AudioTagPayloadReader(final TrackOutput trackOutput) {
        super(trackOutput);
    }
    
    @Override
    protected boolean parseHeader(final ParsableByteArray parsableByteArray) throws UnsupportedFormatException {
        if (!this.hasParsedAudioDataHeader) {
            final int unsignedByte = parsableByteArray.readUnsignedByte();
            this.audioFormat = (unsignedByte >> 4 & 0xF);
            final int audioFormat = this.audioFormat;
            if (audioFormat == 2) {
                super.output.format(Format.createAudioSampleFormat(null, "audio/mpeg", null, -1, -1, 1, AudioTagPayloadReader.AUDIO_SAMPLING_RATE_TABLE[unsignedByte >> 2 & 0x3], null, null, 0, null));
                this.hasOutputFormat = true;
            }
            else if (audioFormat != 7 && audioFormat != 8) {
                if (audioFormat != 10) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Audio format not supported: ");
                    sb.append(this.audioFormat);
                    throw new UnsupportedFormatException(sb.toString());
                }
            }
            else {
                String s;
                if (this.audioFormat == 7) {
                    s = "audio/g711-alaw";
                }
                else {
                    s = "audio/g711-mlaw";
                }
                int n;
                if ((unsignedByte & 0x1) == 0x1) {
                    n = 2;
                }
                else {
                    n = 3;
                }
                super.output.format(Format.createAudioSampleFormat(null, s, null, -1, -1, 1, 8000, n, null, null, 0, null));
                this.hasOutputFormat = true;
            }
            this.hasParsedAudioDataHeader = true;
        }
        else {
            parsableByteArray.skipBytes(1);
        }
        return true;
    }
    
    @Override
    protected void parsePayload(final ParsableByteArray parsableByteArray, final long n) throws ParserException {
        if (this.audioFormat == 2) {
            final int bytesLeft = parsableByteArray.bytesLeft();
            super.output.sampleData(parsableByteArray, bytesLeft);
            super.output.sampleMetadata(n, 1, bytesLeft, 0, null);
        }
        else {
            final int unsignedByte = parsableByteArray.readUnsignedByte();
            if (unsignedByte == 0 && !this.hasOutputFormat) {
                final byte[] o = new byte[parsableByteArray.bytesLeft()];
                parsableByteArray.readBytes(o, 0, o.length);
                final Pair<Integer, Integer> aacAudioSpecificConfig = CodecSpecificDataUtil.parseAacAudioSpecificConfig(o);
                super.output.format(Format.createAudioSampleFormat(null, "audio/mp4a-latm", null, -1, -1, (int)aacAudioSpecificConfig.second, (int)aacAudioSpecificConfig.first, Collections.singletonList(o), null, 0, null));
                this.hasOutputFormat = true;
            }
            else if (this.audioFormat != 10 || unsignedByte == 1) {
                final int bytesLeft2 = parsableByteArray.bytesLeft();
                super.output.sampleData(parsableByteArray, bytesLeft2);
                super.output.sampleMetadata(n, 1, bytesLeft2, 0, null);
            }
        }
    }
}
