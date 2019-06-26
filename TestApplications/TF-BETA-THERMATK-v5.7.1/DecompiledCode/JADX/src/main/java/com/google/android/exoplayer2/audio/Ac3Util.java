package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.nio.ByteBuffer;

public final class Ac3Util {
    private static final int[] BITRATE_BY_HALF_FRMSIZECOD = new int[]{32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 384, 448, 512, 576, 640};
    private static final int[] BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD = new int[]{1, 2, 3, 6};
    private static final int[] CHANNEL_COUNT_BY_ACMOD = new int[]{2, 1, 2, 3, 3, 4, 4, 5};
    private static final int[] SAMPLE_RATE_BY_FSCOD = new int[]{48000, 44100, 32000};
    private static final int[] SAMPLE_RATE_BY_FSCOD2 = new int[]{24000, 22050, 16000};
    private static final int[] SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1 = new int[]{69, 87, 104, 121, 139, 174, 208, 243, 278, 348, 417, 487, 557, 696, 835, 975, 1114, 1253, 1393};

    public static final class SyncFrameInfo {
        public final int channelCount;
        public final int frameSize;
        public final String mimeType;
        public final int sampleCount;
        public final int sampleRate;
        public final int streamType;

        private SyncFrameInfo(String str, int i, int i2, int i3, int i4, int i5) {
            this.mimeType = str;
            this.streamType = i;
            this.channelCount = i2;
            this.sampleRate = i3;
            this.frameSize = i4;
            this.sampleCount = i5;
        }
    }

    public static int getAc3SyncframeAudioSampleCount() {
        return 1536;
    }

    public static Format parseAc3AnnexFFormat(ParsableByteArray parsableByteArray, String str, String str2, DrmInitData drmInitData) {
        int i = SAMPLE_RATE_BY_FSCOD[(parsableByteArray.readUnsignedByte() & 192) >> 6];
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        int i2 = CHANNEL_COUNT_BY_ACMOD[(readUnsignedByte & 56) >> 3];
        if ((readUnsignedByte & 4) != 0) {
            i2++;
        }
        return Format.createAudioSampleFormat(str, MimeTypes.AUDIO_AC3, null, -1, -1, i2, i, null, drmInitData, 0, str2);
    }

    public static Format parseEAc3AnnexFFormat(ParsableByteArray parsableByteArray, String str, String str2, DrmInitData drmInitData) {
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        parsableByteArray.skipBytes(2);
        int i = SAMPLE_RATE_BY_FSCOD[(parsableByteArray.readUnsignedByte() & 192) >> 6];
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        int i2 = CHANNEL_COUNT_BY_ACMOD[(readUnsignedByte & 14) >> 1];
        if ((readUnsignedByte & 1) != 0) {
            i2++;
        }
        if (((parsableByteArray.readUnsignedByte() & 30) >> 1) > 0 && (2 & parsableByteArray.readUnsignedByte()) != 0) {
            i2 += 2;
        }
        int i3 = i2;
        String str3 = (parsableByteArray.bytesLeft() <= 0 || (parsableByteArray.readUnsignedByte() & 1) == 0) ? MimeTypes.AUDIO_E_AC3 : MimeTypes.AUDIO_E_AC3_JOC;
        return Format.createAudioSampleFormat(str, str3, null, -1, -1, i3, i, null, drmInitData, 0, str2);
    }

    public static SyncFrameInfo parseAc3SyncframeInfo(ParsableBitArray parsableBitArray) {
        String str;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        ParsableBitArray parsableBitArray2 = parsableBitArray;
        int position = parsableBitArray.getPosition();
        parsableBitArray2.skipBits(40);
        Object obj = parsableBitArray2.readBits(5) == 16 ? 1 : null;
        parsableBitArray2.setPosition(position);
        position = -1;
        int readBits;
        int i6;
        int readBits2;
        int i7;
        int i8;
        if (obj != null) {
            int i9;
            int i10;
            parsableBitArray2.skipBits(16);
            readBits = parsableBitArray2.readBits(2);
            if (readBits == 0) {
                position = 0;
            } else if (readBits == 1) {
                position = 1;
            } else if (readBits == 2) {
                position = 2;
            }
            parsableBitArray2.skipBits(3);
            readBits = (parsableBitArray2.readBits(11) + 1) * 2;
            int readBits3 = parsableBitArray2.readBits(2);
            if (readBits3 == 3) {
                i6 = SAMPLE_RATE_BY_FSCOD2[parsableBitArray2.readBits(2)];
                i9 = 3;
                i10 = 6;
            } else {
                i9 = parsableBitArray2.readBits(2);
                i10 = BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[i9];
                i6 = SAMPLE_RATE_BY_FSCOD[readBits3];
            }
            int i11 = i10 * 256;
            readBits2 = parsableBitArray2.readBits(3);
            boolean readBit = parsableBitArray.readBit();
            i7 = CHANNEL_COUNT_BY_ACMOD[readBits2] + readBit;
            parsableBitArray2.skipBits(10);
            if (parsableBitArray.readBit()) {
                parsableBitArray2.skipBits(8);
            }
            if (readBits2 == 0) {
                parsableBitArray2.skipBits(5);
                if (parsableBitArray.readBit()) {
                    parsableBitArray2.skipBits(8);
                }
            }
            if (position == 1 && parsableBitArray.readBit()) {
                parsableBitArray2.skipBits(16);
            }
            if (parsableBitArray.readBit()) {
                if (readBits2 > 2) {
                    parsableBitArray2.skipBits(2);
                }
                if ((readBits2 & 1) != 0 && readBits2 > 2) {
                    parsableBitArray2.skipBits(6);
                }
                if ((readBits2 & 4) != 0) {
                    parsableBitArray2.skipBits(6);
                }
                if (readBit && parsableBitArray.readBit()) {
                    parsableBitArray2.skipBits(5);
                }
                if (position == 0) {
                    if (parsableBitArray.readBit()) {
                        parsableBitArray2.skipBits(6);
                    }
                    if (readBits2 == 0 && parsableBitArray.readBit()) {
                        parsableBitArray2.skipBits(6);
                    }
                    if (parsableBitArray.readBit()) {
                        parsableBitArray2.skipBits(6);
                    }
                    int readBits4 = parsableBitArray2.readBits(2);
                    if (readBits4 == 1) {
                        parsableBitArray2.skipBits(5);
                    } else if (readBits4 == 2) {
                        parsableBitArray2.skipBits(12);
                    } else if (readBits4 == 3) {
                        readBits4 = parsableBitArray2.readBits(5);
                        if (parsableBitArray.readBit()) {
                            parsableBitArray2.skipBits(5);
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                if (parsableBitArray.readBit()) {
                                    parsableBitArray2.skipBits(4);
                                }
                                if (parsableBitArray.readBit()) {
                                    parsableBitArray2.skipBits(4);
                                }
                            }
                        }
                        if (parsableBitArray.readBit()) {
                            parsableBitArray2.skipBits(5);
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(7);
                                if (parsableBitArray.readBit()) {
                                    parsableBitArray2.skipBits(8);
                                }
                            }
                        }
                        parsableBitArray2.skipBits((readBits4 + 2) * 8);
                        parsableBitArray.byteAlign();
                    }
                    if (readBits2 < 2) {
                        if (parsableBitArray.readBit()) {
                            parsableBitArray2.skipBits(14);
                        }
                        if (readBits2 == 0 && parsableBitArray.readBit()) {
                            parsableBitArray2.skipBits(14);
                        }
                    }
                    if (parsableBitArray.readBit()) {
                        if (i9 == 0) {
                            parsableBitArray2.skipBits(5);
                        } else {
                            for (readBits4 = 0; readBits4 < i10; readBits4++) {
                                if (parsableBitArray.readBit()) {
                                    parsableBitArray2.skipBits(5);
                                }
                            }
                        }
                    }
                }
            }
            if (parsableBitArray.readBit()) {
                parsableBitArray2.skipBits(5);
                if (readBits2 == 2) {
                    parsableBitArray2.skipBits(4);
                }
                if (readBits2 >= 6) {
                    parsableBitArray2.skipBits(2);
                }
                if (parsableBitArray.readBit()) {
                    parsableBitArray2.skipBits(8);
                }
                if (readBits2 == 0 && parsableBitArray.readBit()) {
                    parsableBitArray2.skipBits(8);
                }
                i8 = 3;
                if (readBits3 < 3) {
                    parsableBitArray.skipBit();
                }
            } else {
                i8 = 3;
            }
            if (position == 0 && i9 != i8) {
                parsableBitArray.skipBit();
            }
            if (position == 2 && (i9 == i8 || parsableBitArray.readBit())) {
                parsableBitArray2.skipBits(6);
            }
            String str2 = (parsableBitArray.readBit() && parsableBitArray2.readBits(6) == 1 && parsableBitArray2.readBits(8) == 1) ? MimeTypes.AUDIO_E_AC3_JOC : MimeTypes.AUDIO_E_AC3;
            str = str2;
            i = position;
            i2 = readBits;
            i3 = i6;
            i4 = i11;
            i5 = i7;
        } else {
            parsableBitArray2.skipBits(32);
            i8 = parsableBitArray2.readBits(2);
            readBits = getAc3SyncframeSize(i8, parsableBitArray2.readBits(6));
            parsableBitArray2.skipBits(8);
            readBits2 = parsableBitArray2.readBits(3);
            if (!((readBits2 & 1) == 0 || readBits2 == 1)) {
                parsableBitArray2.skipBits(2);
            }
            if ((readBits2 & 4) != 0) {
                parsableBitArray2.skipBits(2);
            }
            if (readBits2 == 2) {
                parsableBitArray2.skipBits(2);
            }
            i6 = SAMPLE_RATE_BY_FSCOD[i8];
            i7 = CHANNEL_COUNT_BY_ACMOD[readBits2] + parsableBitArray.readBit();
            str = MimeTypes.AUDIO_AC3;
            i2 = readBits;
            i3 = i6;
            i5 = i7;
            i = -1;
            i4 = 1536;
        }
        return new SyncFrameInfo(str, i, i5, i3, i2, i4);
    }

    public static int parseAc3SyncframeSize(byte[] bArr) {
        if (bArr.length < 6) {
            return -1;
        }
        if ((((bArr[5] & NalUnitUtil.EXTENDED_SAR) >> 3) == 16 ? 1 : null) == null) {
            return getAc3SyncframeSize((bArr[4] & 192) >> 6, bArr[4] & 63);
        }
        return (((bArr[3] & NalUnitUtil.EXTENDED_SAR) | ((bArr[2] & 7) << 8)) + 1) * 2;
    }

    public static int parseEAc3SyncframeAudioSampleCount(ByteBuffer byteBuffer) {
        int i = 6;
        if (((byteBuffer.get(byteBuffer.position() + 4) & 192) >> 6) != 3) {
            i = BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[(byteBuffer.get(byteBuffer.position() + 4) & 48) >> 4];
        }
        return i * 256;
    }

    public static int findTrueHdSyncframeOffset(ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        int limit = byteBuffer.limit() - 10;
        for (int i = position; i <= limit; i++) {
            if ((byteBuffer.getInt(i + 4) & -16777217) == -1167101192) {
                return i - position;
            }
        }
        return -1;
    }

    public static int parseTrueHdSyncframeAudioSampleCount(byte[] bArr) {
        int i = 0;
        if (bArr[4] != (byte) -8 || bArr[5] != (byte) 114 || bArr[6] != (byte) 111 || (bArr[7] & 254) != 186) {
            return 0;
        }
        if ((bArr[7] & NalUnitUtil.EXTENDED_SAR) == 187) {
            i = 1;
        }
        return 40 << ((bArr[i != 0 ? 9 : 8] >> 4) & 7);
    }

    public static int parseTrueHdSyncframeAudioSampleCount(ByteBuffer byteBuffer, int i) {
        return 40 << ((byteBuffer.get((byteBuffer.position() + i) + (((byteBuffer.get((byteBuffer.position() + i) + 7) & NalUnitUtil.EXTENDED_SAR) == 187 ? 1 : null) != null ? 9 : 8)) >> 4) & 7);
    }

    private static int getAc3SyncframeSize(int i, int i2) {
        int i3 = i2 / 2;
        if (i >= 0) {
            int[] iArr = SAMPLE_RATE_BY_FSCOD;
            if (i < iArr.length && i2 >= 0) {
                int[] iArr2 = SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1;
                if (i3 < iArr2.length) {
                    i = iArr[i];
                    if (i == 44100) {
                        return (iArr2[i3] + (i2 % 2)) * 2;
                    }
                    i2 = BITRATE_BY_HALF_FRMSIZECOD[i3];
                    return i == 32000 ? i2 * 6 : i2 * 4;
                }
            }
        }
        return -1;
    }
}
