// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.Format;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.List;
import com.google.android.exoplayer2.util.Util;

final class OpusReader extends StreamReader
{
    private static final int OPUS_CODE;
    private static final byte[] OPUS_SIGNATURE;
    private boolean headerRead;
    
    static {
        OPUS_CODE = Util.getIntegerCodeForString("Opus");
        OPUS_SIGNATURE = new byte[] { 79, 112, 117, 115, 72, 101, 97, 100 };
    }
    
    private long getPacketDurationUs(final byte[] array) {
        final int n = array[0] & 0xFF;
        final int n2 = n & 0x3;
        final int n3 = 2;
        int n4;
        if (n2 != 0) {
            n4 = n3;
            if (n2 != 1) {
                n4 = n3;
                if (n2 != 2) {
                    n4 = (array[1] & 0x3F);
                }
            }
        }
        else {
            n4 = 1;
        }
        final int n5 = n >> 3;
        final int n6 = n5 & 0x3;
        int n7;
        if (n5 >= 16) {
            n7 = 2500 << n6;
        }
        else if (n5 >= 12) {
            n7 = 10000 << (n6 & 0x1);
        }
        else if (n6 == 3) {
            n7 = 60000;
        }
        else {
            n7 = 10000 << n6;
        }
        return n4 * (long)n7;
    }
    
    private void putNativeOrderLong(final List<byte[]> list, final int n) {
        list.add(ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(n * 1000000000L / 48000L).array());
    }
    
    public static boolean verifyBitstreamType(final ParsableByteArray parsableByteArray) {
        final int bytesLeft = parsableByteArray.bytesLeft();
        final byte[] opus_SIGNATURE = OpusReader.OPUS_SIGNATURE;
        if (bytesLeft < opus_SIGNATURE.length) {
            return false;
        }
        final byte[] a = new byte[opus_SIGNATURE.length];
        parsableByteArray.readBytes(a, 0, opus_SIGNATURE.length);
        return Arrays.equals(a, OpusReader.OPUS_SIGNATURE);
    }
    
    @Override
    protected long preparePayload(final ParsableByteArray parsableByteArray) {
        return this.convertTimeToGranule(this.getPacketDurationUs(parsableByteArray.data));
    }
    
    @Override
    protected boolean readHeaders(final ParsableByteArray parsableByteArray, final long n, final SetupData setupData) {
        final boolean headerRead = this.headerRead;
        boolean b = true;
        if (!headerRead) {
            final byte[] copy = Arrays.copyOf(parsableByteArray.data, parsableByteArray.limit());
            final byte b2 = copy[9];
            final byte b3 = copy[11];
            final byte b4 = copy[10];
            final ArrayList<byte[]> list = new ArrayList<byte[]>(3);
            list.add(copy);
            this.putNativeOrderLong(list, (b3 & 0xFF) << 8 | (b4 & 0xFF));
            this.putNativeOrderLong(list, 3840);
            setupData.format = Format.createAudioSampleFormat(null, "audio/opus", null, -1, -1, b2 & 0xFF, 48000, list, null, 0, null);
            return this.headerRead = true;
        }
        if (parsableByteArray.readInt() != OpusReader.OPUS_CODE) {
            b = false;
        }
        parsableByteArray.setPosition(0);
        return b;
    }
    
    @Override
    protected void reset(final boolean b) {
        super.reset(b);
        if (b) {
            this.headerRead = false;
        }
    }
}
