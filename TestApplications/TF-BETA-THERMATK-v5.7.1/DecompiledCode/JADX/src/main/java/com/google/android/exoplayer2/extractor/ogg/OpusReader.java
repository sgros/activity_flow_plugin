package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class OpusReader extends StreamReader {
    private static final int OPUS_CODE = Util.getIntegerCodeForString("Opus");
    private static final byte[] OPUS_SIGNATURE = new byte[]{(byte) 79, (byte) 112, (byte) 117, (byte) 115, (byte) 72, (byte) 101, (byte) 97, (byte) 100};
    private boolean headerRead;

    OpusReader() {
    }

    public static boolean verifyBitstreamType(ParsableByteArray parsableByteArray) {
        int bytesLeft = parsableByteArray.bytesLeft();
        byte[] bArr = OPUS_SIGNATURE;
        if (bytesLeft < bArr.length) {
            return false;
        }
        byte[] bArr2 = new byte[bArr.length];
        parsableByteArray.readBytes(bArr2, 0, bArr.length);
        return Arrays.equals(bArr2, OPUS_SIGNATURE);
    }

    /* Access modifiers changed, original: protected */
    public void reset(boolean z) {
        super.reset(z);
        if (z) {
            this.headerRead = false;
        }
    }

    /* Access modifiers changed, original: protected */
    public long preparePayload(ParsableByteArray parsableByteArray) {
        return convertTimeToGranule(getPacketDurationUs(parsableByteArray.data));
    }

    /* Access modifiers changed, original: protected */
    public boolean readHeaders(ParsableByteArray parsableByteArray, long j, SetupData setupData) {
        boolean z = true;
        if (this.headerRead) {
            if (parsableByteArray.readInt() != OPUS_CODE) {
                z = false;
            }
            parsableByteArray.setPosition(0);
            return z;
        }
        byte[] copyOf = Arrays.copyOf(parsableByteArray.data, parsableByteArray.limit());
        int i = copyOf[9] & NalUnitUtil.EXTENDED_SAR;
        int i2 = ((copyOf[11] & NalUnitUtil.EXTENDED_SAR) << 8) | (copyOf[10] & NalUnitUtil.EXTENDED_SAR);
        ArrayList arrayList = new ArrayList(3);
        arrayList.add(copyOf);
        putNativeOrderLong(arrayList, i2);
        putNativeOrderLong(arrayList, 3840);
        setupData.format = Format.createAudioSampleFormat(null, MimeTypes.AUDIO_OPUS, null, -1, -1, i, 48000, arrayList, null, 0, null);
        this.headerRead = true;
        return true;
    }

    private void putNativeOrderLong(List<byte[]> list, int i) {
        list.add(ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong((((long) i) * 1000000000) / 48000).array());
    }

    private long getPacketDurationUs(byte[] bArr) {
        int i = bArr[0] & NalUnitUtil.EXTENDED_SAR;
        int i2 = i & 3;
        int i3 = 2;
        if (i2 == 0) {
            i3 = 1;
        } else if (!(i2 == 1 || i2 == 2)) {
            i3 = bArr[1] & 63;
        }
        i >>= 3;
        i2 = i & 3;
        int i4 = i >= 16 ? 2500 << i2 : i >= 12 ? 10000 << (i2 & 1) : i2 == 3 ? 60000 : 10000 << i2;
        return ((long) i3) * ((long) i4);
    }
}
