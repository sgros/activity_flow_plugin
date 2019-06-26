package com.google.android.exoplayer2.extractor.p002ts;

import com.google.android.exoplayer2.util.ParsableByteArray;

/* renamed from: com.google.android.exoplayer2.extractor.ts.TsUtil */
public final class TsUtil {
    public static int findSyncBytePosition(byte[] bArr, int i, int i2) {
        while (i < i2 && bArr[i] != (byte) 71) {
            i++;
        }
        return i;
    }

    public static long readPcrFromPacket(ParsableByteArray parsableByteArray, int i, int i2) {
        parsableByteArray.setPosition(i);
        if (parsableByteArray.bytesLeft() < 5) {
            return -9223372036854775807L;
        }
        i = parsableByteArray.readInt();
        if ((8388608 & i) != 0 || ((2096896 & i) >> 8) != i2) {
            return -9223372036854775807L;
        }
        Object obj = 1;
        if (((i & 32) != 0 ? 1 : null) != null && parsableByteArray.readUnsignedByte() >= 7 && parsableByteArray.bytesLeft() >= 7) {
            if ((parsableByteArray.readUnsignedByte() & 16) != 16) {
                obj = null;
            }
            if (obj != null) {
                byte[] bArr = new byte[6];
                parsableByteArray.readBytes(bArr, 0, bArr.length);
                return TsUtil.readPcrValueFromPcrBytes(bArr);
            }
        }
        return -9223372036854775807L;
    }

    private static long readPcrValueFromPcrBytes(byte[] bArr) {
        return (((((((long) bArr[0]) & 255) << 25) | ((((long) bArr[1]) & 255) << 17)) | ((((long) bArr[2]) & 255) << 9)) | ((((long) bArr[3]) & 255) << 1)) | ((255 & ((long) bArr[4])) >> 7);
    }
}
