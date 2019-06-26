// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.video;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.NalUnitUtil;
import java.util.ArrayList;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.List;

public final class AvcConfig
{
    public final int height;
    public final List<byte[]> initializationData;
    public final int nalUnitLengthFieldLength;
    public final float pixelWidthAspectRatio;
    public final int width;
    
    private AvcConfig(final List<byte[]> initializationData, final int nalUnitLengthFieldLength, final int width, final int height, final float pixelWidthAspectRatio) {
        this.initializationData = initializationData;
        this.nalUnitLengthFieldLength = nalUnitLengthFieldLength;
        this.width = width;
        this.height = height;
        this.pixelWidthAspectRatio = pixelWidthAspectRatio;
    }
    
    private static byte[] buildNalUnitForChild(final ParsableByteArray parsableByteArray) {
        final int unsignedShort = parsableByteArray.readUnsignedShort();
        final int position = parsableByteArray.getPosition();
        parsableByteArray.skipBytes(unsignedShort);
        return CodecSpecificDataUtil.buildNalUnit(parsableByteArray.data, position, unsignedShort);
    }
    
    public static AvcConfig parse(final ParsableByteArray parsableByteArray) throws ParserException {
        try {
            parsableByteArray.skipBytes(4);
            final int n = (parsableByteArray.readUnsignedByte() & 0x3) + 1;
            if (n != 3) {
                final ArrayList<byte[]> list = new ArrayList<byte[]>();
                final int n2 = parsableByteArray.readUnsignedByte() & 0x1F;
                for (int i = 0; i < n2; ++i) {
                    list.add(buildNalUnitForChild(parsableByteArray));
                }
                for (int unsignedByte = parsableByteArray.readUnsignedByte(), j = 0; j < unsignedByte; ++j) {
                    list.add(buildNalUnitForChild(parsableByteArray));
                }
                int width;
                int height;
                float pixelWidthAspectRatio;
                if (n2 > 0) {
                    final NalUnitUtil.SpsData spsNalUnit = NalUnitUtil.parseSpsNalUnit(list.get(0), n, ((byte[])list.get(0)).length);
                    width = spsNalUnit.width;
                    height = spsNalUnit.height;
                    pixelWidthAspectRatio = spsNalUnit.pixelWidthAspectRatio;
                }
                else {
                    width = -1;
                    height = -1;
                    pixelWidthAspectRatio = 1.0f;
                }
                return new AvcConfig(list, n, width, height, pixelWidthAspectRatio);
            }
            throw new IllegalStateException();
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new ParserException("Error parsing AVC config", ex);
        }
    }
}
