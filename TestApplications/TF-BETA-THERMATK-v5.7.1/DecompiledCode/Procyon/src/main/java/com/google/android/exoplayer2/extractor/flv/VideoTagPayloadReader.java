// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.video.AvcConfig;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;

final class VideoTagPayloadReader extends TagPayloadReader
{
    private int frameType;
    private boolean hasOutputFormat;
    private final ParsableByteArray nalLength;
    private final ParsableByteArray nalStartCode;
    private int nalUnitLengthFieldLength;
    
    public VideoTagPayloadReader(final TrackOutput trackOutput) {
        super(trackOutput);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalLength = new ParsableByteArray(4);
    }
    
    @Override
    protected boolean parseHeader(final ParsableByteArray parsableByteArray) throws UnsupportedFormatException {
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final int frameType = unsignedByte >> 4 & 0xF;
        final int i = unsignedByte & 0xF;
        if (i == 7) {
            this.frameType = frameType;
            return frameType != 5;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Video format not supported: ");
        sb.append(i);
        throw new UnsupportedFormatException(sb.toString());
    }
    
    @Override
    protected void parsePayload(final ParsableByteArray parsableByteArray, final long n) throws ParserException {
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final long n2 = parsableByteArray.readInt24();
        if (unsignedByte == 0 && !this.hasOutputFormat) {
            final ParsableByteArray parsableByteArray2 = new ParsableByteArray(new byte[parsableByteArray.bytesLeft()]);
            parsableByteArray.readBytes(parsableByteArray2.data, 0, parsableByteArray.bytesLeft());
            final AvcConfig parse = AvcConfig.parse(parsableByteArray2);
            this.nalUnitLengthFieldLength = parse.nalUnitLengthFieldLength;
            super.output.format(Format.createVideoSampleFormat(null, "video/avc", null, -1, -1, parse.width, parse.height, -1.0f, parse.initializationData, -1, parse.pixelWidthAspectRatio, null));
            this.hasOutputFormat = true;
        }
        else if (unsignedByte == 1 && this.hasOutputFormat) {
            final byte[] data = this.nalLength.data;
            data[0] = 0;
            data[1] = 0;
            data[2] = 0;
            final int nalUnitLengthFieldLength = this.nalUnitLengthFieldLength;
            int n3 = 0;
            while (parsableByteArray.bytesLeft() > 0) {
                parsableByteArray.readBytes(this.nalLength.data, 4 - nalUnitLengthFieldLength, this.nalUnitLengthFieldLength);
                this.nalLength.setPosition(0);
                final int unsignedIntToInt = this.nalLength.readUnsignedIntToInt();
                this.nalStartCode.setPosition(0);
                super.output.sampleData(this.nalStartCode, 4);
                super.output.sampleData(parsableByteArray, unsignedIntToInt);
                n3 = n3 + 4 + unsignedIntToInt;
            }
            final TrackOutput output = super.output;
            int n4;
            if (this.frameType == 1) {
                n4 = 1;
            }
            else {
                n4 = 0;
            }
            output.sampleMetadata(n + n2 * 1000L, n4, n3, 0, null);
        }
    }
}
