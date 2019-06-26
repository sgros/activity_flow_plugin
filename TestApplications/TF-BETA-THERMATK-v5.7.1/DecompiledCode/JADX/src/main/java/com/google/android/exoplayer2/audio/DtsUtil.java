package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableBitArray;
import java.nio.ByteBuffer;
import java.util.Arrays;

public final class DtsUtil {
    private static final int[] CHANNELS_BY_AMODE = new int[]{1, 2, 2, 2, 2, 3, 3, 4, 4, 5, 6, 6, 6, 7, 8, 8};
    private static final int[] SAMPLE_RATE_BY_SFREQ = new int[]{-1, 8000, 16000, 32000, -1, -1, 11025, 22050, 44100, -1, -1, 12000, 24000, 48000, -1, -1};
    private static final int[] TWICE_BITRATE_KBPS_BY_RATE = new int[]{64, 112, 128, 192, 224, 256, 384, 448, 512, 640, 768, 896, 1024, 1152, 1280, 1536, 1920, 2048, 2304, 2560, 2688, 2816, 2823, 2944, 3072, 3840, 4096, 6144, 7680};

    public static boolean isSyncWord(int i) {
        return i == 2147385345 || i == -25230976 || i == 536864768 || i == -14745368;
    }

    public static Format parseDtsFormat(byte[] bArr, String str, String str2, DrmInitData drmInitData) {
        ParsableBitArray normalizedFrameHeader = getNormalizedFrameHeader(bArr);
        normalizedFrameHeader.skipBits(60);
        int i = CHANNELS_BY_AMODE[normalizedFrameHeader.readBits(6)];
        int i2 = SAMPLE_RATE_BY_SFREQ[normalizedFrameHeader.readBits(4)];
        int readBits = normalizedFrameHeader.readBits(5);
        int[] iArr = TWICE_BITRATE_KBPS_BY_RATE;
        int i3 = readBits >= iArr.length ? -1 : (iArr[readBits] * 1000) / 2;
        normalizedFrameHeader.skipBits(10);
        return Format.createAudioSampleFormat(str, MimeTypes.AUDIO_DTS, null, i3, -1, i + (normalizedFrameHeader.readBits(2) > 0 ? 1 : 0), i2, null, drmInitData, 0, str2);
    }

    public static int parseDtsAudioSampleCount(byte[] bArr) {
        int i;
        int i2;
        byte b = bArr[0];
        if (b != (byte) -2) {
            if (b == (byte) -1) {
                i = (bArr[4] & 7) << 4;
                i2 = bArr[7];
            } else if (b != (byte) 31) {
                i = (bArr[4] & 1) << 6;
                i2 = bArr[5];
            } else {
                i = (bArr[5] & 7) << 4;
                i2 = bArr[6];
            }
            i2 &= 60;
            return (((i2 >> 2) | i) + 1) * 32;
        }
        i = (bArr[5] & 1) << 6;
        i2 = bArr[4];
        i2 &= 252;
        return (((i2 >> 2) | i) + 1) * 32;
    }

    public static int parseDtsAudioSampleCount(ByteBuffer byteBuffer) {
        int i;
        int i2;
        int position = byteBuffer.position();
        byte b = byteBuffer.get(position);
        if (b != (byte) -2) {
            if (b == (byte) -1) {
                i = (byteBuffer.get(position + 4) & 7) << 4;
                i2 = byteBuffer.get(position + 7);
            } else if (b != (byte) 31) {
                i = (byteBuffer.get(position + 4) & 1) << 6;
                i2 = byteBuffer.get(position + 5);
            } else {
                i = (byteBuffer.get(position + 5) & 7) << 4;
                i2 = byteBuffer.get(position + 6);
            }
            i2 &= 60;
            return (((i2 >> 2) | i) + 1) * 32;
        }
        i = (byteBuffer.get(position + 5) & 1) << 6;
        i2 = byteBuffer.get(position + 4);
        i2 &= 252;
        return (((i2 >> 2) | i) + 1) * 32;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0060  */
    public static int getDtsFrameSize(byte[] r7) {
        /*
        r0 = 0;
        r1 = r7[r0];
        r2 = -2;
        r3 = 7;
        r4 = 6;
        r5 = 1;
        r6 = 4;
        if (r1 == r2) goto L_0x004f;
    L_0x000a:
        r2 = -1;
        if (r1 == r2) goto L_0x0037;
    L_0x000d:
        r2 = 31;
        if (r1 == r2) goto L_0x0026;
    L_0x0011:
        r1 = 5;
        r1 = r7[r1];
        r1 = r1 & 3;
        r1 = r1 << 12;
        r2 = r7[r4];
        r2 = r2 & 255;
        r2 = r2 << r6;
        r1 = r1 | r2;
        r7 = r7[r3];
    L_0x0020:
        r7 = r7 & 240;
        r7 = r7 >> r6;
        r7 = r7 | r1;
        r7 = r7 + r5;
        goto L_0x005e;
    L_0x0026:
        r0 = r7[r4];
        r0 = r0 & 3;
        r0 = r0 << 12;
        r1 = r7[r3];
        r1 = r1 & 255;
        r1 = r1 << r6;
        r0 = r0 | r1;
        r1 = 8;
        r7 = r7[r1];
        goto L_0x0047;
    L_0x0037:
        r0 = r7[r3];
        r0 = r0 & 3;
        r0 = r0 << 12;
        r1 = r7[r4];
        r1 = r1 & 255;
        r1 = r1 << r6;
        r0 = r0 | r1;
        r1 = 9;
        r7 = r7[r1];
    L_0x0047:
        r7 = r7 & 60;
        r7 = r7 >> 2;
        r7 = r7 | r0;
        r7 = r7 + r5;
        r0 = 1;
        goto L_0x005e;
    L_0x004f:
        r1 = r7[r6];
        r1 = r1 & 3;
        r1 = r1 << 12;
        r2 = r7[r3];
        r2 = r2 & 255;
        r2 = r2 << r6;
        r1 = r1 | r2;
        r7 = r7[r4];
        goto L_0x0020;
    L_0x005e:
        if (r0 == 0) goto L_0x0064;
    L_0x0060:
        r7 = r7 * 16;
        r7 = r7 / 14;
    L_0x0064:
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.audio.DtsUtil.getDtsFrameSize(byte[]):int");
    }

    private static ParsableBitArray getNormalizedFrameHeader(byte[] bArr) {
        if (bArr[0] == Byte.MAX_VALUE) {
            return new ParsableBitArray(bArr);
        }
        bArr = Arrays.copyOf(bArr, bArr.length);
        if (isLittleEndianFrameHeader(bArr)) {
            for (int i = 0; i < bArr.length - 1; i += 2) {
                byte b = bArr[i];
                int i2 = i + 1;
                bArr[i] = bArr[i2];
                bArr[i2] = b;
            }
        }
        ParsableBitArray parsableBitArray = new ParsableBitArray(bArr);
        if (bArr[0] == (byte) 31) {
            ParsableBitArray parsableBitArray2 = new ParsableBitArray(bArr);
            while (parsableBitArray2.bitsLeft() >= 16) {
                parsableBitArray2.skipBits(2);
                parsableBitArray.putInt(parsableBitArray2.readBits(14), 14);
            }
        }
        parsableBitArray.reset(bArr);
        return parsableBitArray;
    }

    private static boolean isLittleEndianFrameHeader(byte[] bArr) {
        return bArr[0] == (byte) -2 || bArr[0] == (byte) -1;
    }
}
