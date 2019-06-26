package com.google.android.exoplayer2.ext.opus;

import com.google.android.exoplayer2.decoder.CryptoInfo;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.decoder.SimpleOutputBuffer;
import com.google.android.exoplayer2.drm.DecryptionException;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.util.NalUnitUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

final class OpusDecoder extends SimpleDecoder<DecoderInputBuffer, SimpleOutputBuffer, OpusDecoderException> {
    private static final int DECODE_ERROR = -1;
    private static final int DEFAULT_SEEK_PRE_ROLL_SAMPLES = 3840;
    private static final int DRM_ERROR = -2;
    private static final int NO_ERROR = 0;
    private static final int SAMPLE_RATE = 48000;
    private final int channelCount;
    private final ExoMediaCrypto exoMediaCrypto;
    private final int headerSeekPreRollSamples;
    private final int headerSkipSamples;
    private final long nativeDecoderContext;
    private int skipSamples;

    private native void opusClose(long j);

    private native int opusDecode(long j, long j2, ByteBuffer byteBuffer, int i, SimpleOutputBuffer simpleOutputBuffer);

    private native int opusGetErrorCode(long j);

    private native String opusGetErrorMessage(long j);

    private native long opusInit(int i, int i2, int i3, int i4, int i5, byte[] bArr);

    private native void opusReset(long j);

    private native int opusSecureDecode(long j, long j2, ByteBuffer byteBuffer, int i, SimpleOutputBuffer simpleOutputBuffer, int i2, ExoMediaCrypto exoMediaCrypto, int i3, byte[] bArr, byte[] bArr2, int i4, int[] iArr, int[] iArr2);

    public int getSampleRate() {
        return SAMPLE_RATE;
    }

    public OpusDecoder(int i, int i2, int i3, List<byte[]> list, ExoMediaCrypto exoMediaCrypto) throws OpusDecoderException {
        List<byte[]> list2 = list;
        ExoMediaCrypto exoMediaCrypto2 = exoMediaCrypto;
        super(new DecoderInputBuffer[i], new SimpleOutputBuffer[i2]);
        this.exoMediaCrypto = exoMediaCrypto2;
        if (exoMediaCrypto2 == null || OpusLibrary.opusIsSecureDecodeSupported()) {
            byte[] bArr = (byte[]) list2.get(0);
            String str = "Header size is too small.";
            if (bArr.length >= 19) {
                this.channelCount = bArr[9] & NalUnitUtil.EXTENDED_SAR;
                if (this.channelCount <= 8) {
                    int i4;
                    int i5;
                    int readLittleEndian16 = readLittleEndian16(bArr, 10);
                    int readLittleEndian162 = readLittleEndian16(bArr, 16);
                    byte[] bArr2 = new byte[8];
                    if (bArr[18] == (byte) 0) {
                        int i6 = this.channelCount;
                        if (i6 <= 2) {
                            i6 = i6 == 2 ? 1 : 0;
                            bArr2[0] = (byte) 0;
                            bArr2[1] = (byte) 1;
                            i4 = i6;
                            i5 = 1;
                        } else {
                            throw new OpusDecoderException("Invalid Header, missing stream map.");
                        }
                    }
                    int length = bArr.length;
                    int i7 = this.channelCount;
                    if (length >= i7 + 21) {
                        i5 = bArr[19] & NalUnitUtil.EXTENDED_SAR;
                        i4 = bArr[20] & NalUnitUtil.EXTENDED_SAR;
                        System.arraycopy(bArr, 21, bArr2, 0, i7);
                    } else {
                        throw new OpusDecoderException(str);
                    }
                    if (list.size() != 3) {
                        this.headerSkipSamples = readLittleEndian16;
                        this.headerSeekPreRollSamples = DEFAULT_SEEK_PRE_ROLL_SAMPLES;
                    } else if (((byte[]) list2.get(1)).length == 8 && ((byte[]) list2.get(2)).length == 8) {
                        long j = ByteBuffer.wrap((byte[]) list2.get(1)).order(ByteOrder.nativeOrder()).getLong();
                        long j2 = ByteBuffer.wrap((byte[]) list2.get(2)).order(ByteOrder.nativeOrder()).getLong();
                        this.headerSkipSamples = nsToSamples(j);
                        this.headerSeekPreRollSamples = nsToSamples(j2);
                    } else {
                        throw new OpusDecoderException("Invalid Codec Delay or Seek Preroll");
                    }
                    this.nativeDecoderContext = opusInit(SAMPLE_RATE, this.channelCount, i5, i4, readLittleEndian162, bArr2);
                    if (this.nativeDecoderContext != 0) {
                        setInitialInputBufferSize(i3);
                        return;
                    }
                    throw new OpusDecoderException("Failed to initialize decoder");
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid channel count: ");
                stringBuilder.append(this.channelCount);
                throw new OpusDecoderException(stringBuilder.toString());
            }
            throw new OpusDecoderException(str);
        }
        throw new OpusDecoderException("Opus decoder does not support secure decode.");
    }

    public String getName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("libopus");
        stringBuilder.append(OpusLibrary.getVersion());
        return stringBuilder.toString();
    }

    /* Access modifiers changed, original: protected */
    public DecoderInputBuffer createInputBuffer() {
        return new DecoderInputBuffer(2);
    }

    /* Access modifiers changed, original: protected */
    public SimpleOutputBuffer createOutputBuffer() {
        return new SimpleOutputBuffer(this);
    }

    /* Access modifiers changed, original: protected */
    public OpusDecoderException createUnexpectedDecodeException(Throwable th) {
        return new OpusDecoderException("Unexpected decode error", th);
    }

    /* Access modifiers changed, original: protected */
    public OpusDecoderException decode(DecoderInputBuffer decoderInputBuffer, SimpleOutputBuffer simpleOutputBuffer, boolean z) {
        int opusSecureDecode;
        OpusDecoder opusDecoder;
        DecoderInputBuffer decoderInputBuffer2 = decoderInputBuffer;
        SimpleOutputBuffer simpleOutputBuffer2 = simpleOutputBuffer;
        if (z) {
            opusReset(this.nativeDecoderContext);
            this.skipSamples = decoderInputBuffer2.timeUs == 0 ? this.headerSkipSamples : this.headerSeekPreRollSamples;
        }
        ByteBuffer byteBuffer = decoderInputBuffer2.data;
        CryptoInfo cryptoInfo = decoderInputBuffer2.cryptoInfo;
        if (decoderInputBuffer.isEncrypted()) {
            opusSecureDecode = opusSecureDecode(this.nativeDecoderContext, decoderInputBuffer2.timeUs, byteBuffer, byteBuffer.limit(), simpleOutputBuffer, SAMPLE_RATE, this.exoMediaCrypto, cryptoInfo.mode, cryptoInfo.key, cryptoInfo.f17iv, cryptoInfo.numSubSamples, cryptoInfo.numBytesOfClearData, cryptoInfo.numBytesOfEncryptedData);
            opusDecoder = this;
        } else {
            opusDecoder = this;
            opusSecureDecode = opusDecode(opusDecoder.nativeDecoderContext, decoderInputBuffer2.timeUs, byteBuffer, byteBuffer.limit(), simpleOutputBuffer);
        }
        if (opusSecureDecode >= 0) {
            SimpleOutputBuffer simpleOutputBuffer3 = simpleOutputBuffer;
            ByteBuffer byteBuffer2 = simpleOutputBuffer3.data;
            byteBuffer2.position(0);
            byteBuffer2.limit(opusSecureDecode);
            int i = opusDecoder.skipSamples;
            if (i > 0) {
                int i2 = opusDecoder.channelCount * 2;
                int i3 = i * i2;
                if (opusSecureDecode <= i3) {
                    opusDecoder.skipSamples = i - (opusSecureDecode / i2);
                    simpleOutputBuffer3.addFlag(Integer.MIN_VALUE);
                    byteBuffer2.position(opusSecureDecode);
                } else {
                    opusDecoder.skipSamples = 0;
                    byteBuffer2.position(i3);
                }
            }
            return null;
        } else if (opusSecureDecode == -2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Drm error: ");
            stringBuilder.append(opusDecoder.opusGetErrorMessage(opusDecoder.nativeDecoderContext));
            String stringBuilder2 = stringBuilder.toString();
            return new OpusDecoderException(stringBuilder2, new DecryptionException(opusDecoder.opusGetErrorCode(opusDecoder.nativeDecoderContext), stringBuilder2));
        } else {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Decode error: ");
            stringBuilder3.append(opusDecoder.opusGetErrorMessage((long) opusSecureDecode));
            return new OpusDecoderException(stringBuilder3.toString());
        }
    }

    public void release() {
        super.release();
        opusClose(this.nativeDecoderContext);
    }

    public int getChannelCount() {
        return this.channelCount;
    }

    private static int nsToSamples(long j) {
        return (int) ((j * 48000) / 1000000000);
    }

    private static int readLittleEndian16(byte[] bArr, int i) {
        return ((bArr[i + 1] & NalUnitUtil.EXTENDED_SAR) << 8) | (bArr[i] & NalUnitUtil.EXTENDED_SAR);
    }
}
