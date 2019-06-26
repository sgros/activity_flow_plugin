// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.video;

import com.google.android.exoplayer2.ParserException;
import java.util.Collections;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.List;

public final class HevcConfig
{
    public final List<byte[]> initializationData;
    public final int nalUnitLengthFieldLength;
    
    private HevcConfig(final List<byte[]> initializationData, final int nalUnitLengthFieldLength) {
        this.initializationData = initializationData;
        this.nalUnitLengthFieldLength = nalUnitLengthFieldLength;
    }
    
    public static HevcConfig parse(final ParsableByteArray parsableByteArray) throws ParserException {
        try {
            parsableByteArray.skipBytes(21);
            final int unsignedByte = parsableByteArray.readUnsignedByte();
            final int unsignedByte2 = parsableByteArray.readUnsignedByte();
            final int position = parsableByteArray.getPosition();
            int i = 0;
            int n = 0;
            while (i < unsignedByte2) {
                parsableByteArray.skipBytes(1);
                for (int unsignedShort = parsableByteArray.readUnsignedShort(), j = 0; j < unsignedShort; ++j) {
                    final int unsignedShort2 = parsableByteArray.readUnsignedShort();
                    n += unsignedShort2 + 4;
                    parsableByteArray.skipBytes(unsignedShort2);
                }
                ++i;
            }
            parsableByteArray.setPosition(position);
            final byte[] o = new byte[n];
            int k = 0;
            int n2 = 0;
            while (k < unsignedByte2) {
                parsableByteArray.skipBytes(1);
                for (int unsignedShort3 = parsableByteArray.readUnsignedShort(), l = 0; l < unsignedShort3; ++l) {
                    final int unsignedShort4 = parsableByteArray.readUnsignedShort();
                    System.arraycopy(NalUnitUtil.NAL_START_CODE, 0, o, n2, NalUnitUtil.NAL_START_CODE.length);
                    final int n3 = n2 + NalUnitUtil.NAL_START_CODE.length;
                    System.arraycopy(parsableByteArray.data, parsableByteArray.getPosition(), o, n3, unsignedShort4);
                    n2 = n3 + unsignedShort4;
                    parsableByteArray.skipBytes(unsignedShort4);
                }
                ++k;
            }
            List<byte[]> singletonList;
            if (n == 0) {
                singletonList = null;
            }
            else {
                singletonList = Collections.singletonList(o);
            }
            return new HevcConfig(singletonList, (unsignedByte & 0x3) + 1);
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new ParserException("Error parsing HEVC config", ex);
        }
    }
}
