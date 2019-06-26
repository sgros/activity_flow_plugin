package com.google.android.exoplayer2.ext.ffmpeg;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.decoder.SimpleOutputBuffer;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.nio.ByteBuffer;

final class FfmpegDecoder extends SimpleDecoder<DecoderInputBuffer, SimpleOutputBuffer, FfmpegDecoderException> {
    private static final int DECODER_ERROR_INVALID_DATA = -1;
    private static final int DECODER_ERROR_OTHER = -2;
    private static final int OUTPUT_BUFFER_SIZE_16BIT = 65536;
    private static final int OUTPUT_BUFFER_SIZE_32BIT = 131072;
    private volatile int channelCount;
    private final String codecName;
    private final int encoding;
    private final byte[] extraData;
    private boolean hasOutputFormat;
    private long nativeContext;
    private final int outputBufferSize;
    private volatile int sampleRate;

    private native int ffmpegDecode(long j, ByteBuffer byteBuffer, int i, ByteBuffer byteBuffer2, int i2);

    private native int ffmpegGetChannelCount(long j);

    private native int ffmpegGetSampleRate(long j);

    private native long ffmpegInitialize(String str, byte[] bArr, boolean z, int i, int i2);

    private native void ffmpegRelease(long j);

    private native long ffmpegReset(long j, byte[] bArr);

    public FfmpegDecoder(int i, int i2, int i3, Format format, boolean z) throws FfmpegDecoderException {
        super(new DecoderInputBuffer[i], new SimpleOutputBuffer[i2]);
        Assertions.checkNotNull(format.sampleMimeType);
        String codecName = FfmpegLibrary.getCodecName(format.sampleMimeType, format.pcmEncoding);
        Assertions.checkNotNull(codecName);
        this.codecName = codecName;
        this.extraData = getExtraData(format.sampleMimeType, format.initializationData);
        this.encoding = z ? 4 : 2;
        this.outputBufferSize = z ? 131072 : 65536;
        this.nativeContext = ffmpegInitialize(this.codecName, this.extraData, z, format.sampleRate, format.channelCount);
        if (this.nativeContext != 0) {
            setInitialInputBufferSize(i3);
            return;
        }
        throw new FfmpegDecoderException("Initialization failed.");
    }

    public String getName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ffmpeg");
        stringBuilder.append(FfmpegLibrary.getVersion());
        stringBuilder.append("-");
        stringBuilder.append(this.codecName);
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
    public FfmpegDecoderException createUnexpectedDecodeException(Throwable th) {
        return new FfmpegDecoderException("Unexpected decode error", th);
    }

    /* Access modifiers changed, original: protected */
    public FfmpegDecoderException decode(DecoderInputBuffer decoderInputBuffer, SimpleOutputBuffer simpleOutputBuffer, boolean z) {
        if (z) {
            this.nativeContext = ffmpegReset(this.nativeContext, this.extraData);
            if (this.nativeContext == 0) {
                return new FfmpegDecoderException("Error resetting (see logcat).");
            }
        }
        ByteBuffer byteBuffer = decoderInputBuffer.data;
        int ffmpegDecode = ffmpegDecode(this.nativeContext, byteBuffer, byteBuffer.limit(), simpleOutputBuffer.init(decoderInputBuffer.timeUs, this.outputBufferSize), this.outputBufferSize);
        if (ffmpegDecode == -1) {
            simpleOutputBuffer.setFlags(Integer.MIN_VALUE);
            return null;
        } else if (ffmpegDecode == -2) {
            return new FfmpegDecoderException("Error decoding (see logcat).");
        } else {
            if (!this.hasOutputFormat) {
                this.channelCount = ffmpegGetChannelCount(this.nativeContext);
                this.sampleRate = ffmpegGetSampleRate(this.nativeContext);
                if (this.sampleRate == 0) {
                    if ("alac".equals(this.codecName)) {
                        Assertions.checkNotNull(this.extraData);
                        ParsableByteArray parsableByteArray = new ParsableByteArray(this.extraData);
                        parsableByteArray.setPosition(this.extraData.length - 4);
                        this.sampleRate = parsableByteArray.readUnsignedIntToInt();
                    }
                }
                this.hasOutputFormat = true;
            }
            simpleOutputBuffer.data.position(0);
            simpleOutputBuffer.data.limit(ffmpegDecode);
            return null;
        }
    }

    public void release() {
        super.release();
        ffmpegRelease(this.nativeContext);
        this.nativeContext = 0;
    }

    public int getChannelCount() {
        return this.channelCount;
    }

    public int getSampleRate() {
        return this.sampleRate;
    }

    public int getEncoding() {
        return this.encoding;
    }

    private static byte[] getExtraData(java.lang.String r6, java.util.List<byte[]> r7) {
        /*
        r0 = r6.hashCode();
        r1 = 3;
        r2 = 2;
        r3 = 1;
        r4 = 0;
        switch(r0) {
            case -1003765268: goto L_0x002a;
            case -53558318: goto L_0x0020;
            case 1504470054: goto L_0x0016;
            case 1504891608: goto L_0x000c;
            default: goto L_0x000b;
        };
    L_0x000b:
        goto L_0x0034;
    L_0x000c:
        r0 = "audio/opus";
        r6 = r6.equals(r0);
        if (r6 == 0) goto L_0x0034;
    L_0x0014:
        r6 = 2;
        goto L_0x0035;
    L_0x0016:
        r0 = "audio/alac";
        r6 = r6.equals(r0);
        if (r6 == 0) goto L_0x0034;
    L_0x001e:
        r6 = 1;
        goto L_0x0035;
    L_0x0020:
        r0 = "audio/mp4a-latm";
        r6 = r6.equals(r0);
        if (r6 == 0) goto L_0x0034;
    L_0x0028:
        r6 = 0;
        goto L_0x0035;
    L_0x002a:
        r0 = "audio/vorbis";
        r6 = r6.equals(r0);
        if (r6 == 0) goto L_0x0034;
    L_0x0032:
        r6 = 3;
        goto L_0x0035;
    L_0x0034:
        r6 = -1;
    L_0x0035:
        if (r6 == 0) goto L_0x0084;
    L_0x0037:
        if (r6 == r3) goto L_0x0084;
    L_0x0039:
        if (r6 == r2) goto L_0x0084;
    L_0x003b:
        if (r6 == r1) goto L_0x003f;
    L_0x003d:
        r6 = 0;
        return r6;
    L_0x003f:
        r6 = r7.get(r4);
        r6 = (byte[]) r6;
        r7 = r7.get(r3);
        r7 = (byte[]) r7;
        r0 = r6.length;
        r5 = r7.length;
        r0 = r0 + r5;
        r0 = r0 + 6;
        r0 = new byte[r0];
        r5 = r6.length;
        r5 = r5 >> 8;
        r5 = (byte) r5;
        r0[r4] = r5;
        r5 = r6.length;
        r5 = r5 & 255;
        r5 = (byte) r5;
        r0[r3] = r5;
        r3 = r6.length;
        java.lang.System.arraycopy(r6, r4, r0, r2, r3);
        r3 = r6.length;
        r3 = r3 + r2;
        r0[r3] = r4;
        r2 = r6.length;
        r2 = r2 + r1;
        r0[r2] = r4;
        r1 = r6.length;
        r1 = r1 + 4;
        r2 = r7.length;
        r2 = r2 >> 8;
        r2 = (byte) r2;
        r0[r1] = r2;
        r1 = r6.length;
        r1 = r1 + 5;
        r2 = r7.length;
        r2 = r2 & 255;
        r2 = (byte) r2;
        r0[r1] = r2;
        r6 = r6.length;
        r6 = r6 + 6;
        r1 = r7.length;
        java.lang.System.arraycopy(r7, r4, r0, r6, r1);
        return r0;
    L_0x0084:
        r6 = r7.get(r4);
        r6 = (byte[]) r6;
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.ext.ffmpeg.FfmpegDecoder.getExtraData(java.lang.String, java.util.List):byte[]");
    }
}
