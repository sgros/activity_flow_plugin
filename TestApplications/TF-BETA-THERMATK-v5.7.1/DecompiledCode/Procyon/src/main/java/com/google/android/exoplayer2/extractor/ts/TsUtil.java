// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.util.ParsableByteArray;

public final class TsUtil
{
    public static int findSyncBytePosition(final byte[] array, int n, final int n2) {
        while (n < n2 && array[n] != 71) {
            ++n;
        }
        return n;
    }
    
    public static long readPcrFromPacket(final ParsableByteArray parsableByteArray, int int1, int n) {
        parsableByteArray.setPosition(int1);
        if (parsableByteArray.bytesLeft() < 5) {
            return -9223372036854775807L;
        }
        int1 = parsableByteArray.readInt();
        if ((0x800000 & int1) != 0x0) {
            return -9223372036854775807L;
        }
        if ((0x1FFF00 & int1) >> 8 != n) {
            return -9223372036854775807L;
        }
        n = 1;
        if ((int1 & 0x20) != 0x0) {
            int1 = 1;
        }
        else {
            int1 = 0;
        }
        if (int1 == 0) {
            return -9223372036854775807L;
        }
        if (parsableByteArray.readUnsignedByte() >= 7 && parsableByteArray.bytesLeft() >= 7) {
            if ((parsableByteArray.readUnsignedByte() & 0x10) == 0x10) {
                int1 = n;
            }
            else {
                int1 = 0;
            }
            if (int1 != 0) {
                final byte[] array = new byte[6];
                parsableByteArray.readBytes(array, 0, array.length);
                return readPcrValueFromPcrBytes(array);
            }
        }
        return -9223372036854775807L;
    }
    
    private static long readPcrValueFromPcrBytes(final byte[] array) {
        return ((long)array[0] & 0xFFL) << 25 | ((long)array[1] & 0xFFL) << 17 | ((long)array[2] & 0xFFL) << 9 | ((long)array[3] & 0xFFL) << 1 | (0xFFL & (long)array[4]) >> 7;
    }
}
