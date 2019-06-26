// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.cea;

import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;

public final class CeaUtil
{
    public static final int USER_DATA_IDENTIFIER_GA94;
    
    static {
        USER_DATA_IDENTIFIER_GA94 = Util.getIntegerCodeForString("GA94");
    }
    
    public static void consume(final long n, final ParsableByteArray parsableByteArray, final TrackOutput[] array) {
        while (true) {
            final int bytesLeft = parsableByteArray.bytesLeft();
            final boolean b = true;
            if (bytesLeft <= 1) {
                break;
            }
            final int non255TerminatedValue = readNon255TerminatedValue(parsableByteArray);
            final int non255TerminatedValue2 = readNon255TerminatedValue(parsableByteArray);
            final int n2 = parsableByteArray.getPosition() + non255TerminatedValue2;
            int limit;
            if (non255TerminatedValue2 != -1 && non255TerminatedValue2 <= parsableByteArray.bytesLeft()) {
                limit = n2;
                if (non255TerminatedValue == 4) {
                    limit = n2;
                    if (non255TerminatedValue2 >= 8) {
                        final int unsignedByte = parsableByteArray.readUnsignedByte();
                        final int unsignedShort = parsableByteArray.readUnsignedShort();
                        int int1;
                        if (unsignedShort == 49) {
                            int1 = parsableByteArray.readInt();
                        }
                        else {
                            int1 = 0;
                        }
                        final int unsignedByte2 = parsableByteArray.readUnsignedByte();
                        if (unsignedShort == 47) {
                            parsableByteArray.skipBytes(1);
                        }
                        boolean b2 = unsignedByte == 181 && (unsignedShort == 49 || unsignedShort == 47) && unsignedByte2 == 3;
                        if (unsignedShort == 49) {
                            b2 &= (int1 == CeaUtil.USER_DATA_IDENTIFIER_GA94 && b);
                        }
                        limit = n2;
                        if (b2) {
                            consumeCcData(n, parsableByteArray, array);
                            limit = n2;
                        }
                    }
                }
            }
            else {
                Log.w("CeaUtil", "Skipping remainder of malformed SEI NAL unit.");
                limit = parsableByteArray.limit();
            }
            parsableByteArray.setPosition(limit);
        }
    }
    
    public static void consumeCcData(final long n, final ParsableByteArray parsableByteArray, final TrackOutput[] array) {
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final int n2 = 0;
        if ((unsignedByte & 0x40) == 0x0) {
            return;
        }
        parsableByteArray.skipBytes(1);
        final int n3 = (unsignedByte & 0x1F) * 3;
        final int position = parsableByteArray.getPosition();
        for (int length = array.length, i = n2; i < length; ++i) {
            final TrackOutput trackOutput = array[i];
            parsableByteArray.setPosition(position);
            trackOutput.sampleData(parsableByteArray, n3);
            trackOutput.sampleMetadata(n, 1, n3, 0, null);
        }
    }
    
    private static int readNon255TerminatedValue(final ParsableByteArray parsableByteArray) {
        int n = 0;
        while (parsableByteArray.bytesLeft() != 0) {
            final int unsignedByte = parsableByteArray.readUnsignedByte();
            n += unsignedByte;
            if (unsignedByte != 255) {
                return n;
            }
        }
        return -1;
    }
}
