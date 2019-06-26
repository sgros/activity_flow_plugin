// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ext.opus;

import com.google.android.exoplayer2.decoder.CryptoInfo;
import com.google.android.exoplayer2.drm.DecryptionException;
import com.google.android.exoplayer2.decoder.OutputBuffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.List;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.decoder.SimpleOutputBuffer;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.SimpleDecoder;

final class OpusDecoder extends SimpleDecoder<DecoderInputBuffer, SimpleOutputBuffer, OpusDecoderException>
{
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
    
    public OpusDecoder(int n, int n2, final int initialInputBufferSize, final List<byte[]> list, final ExoMediaCrypto exoMediaCrypto) throws OpusDecoderException {
        super(new DecoderInputBuffer[n], new SimpleOutputBuffer[n2]);
        this.exoMediaCrypto = exoMediaCrypto;
        if (exoMediaCrypto != null && !OpusLibrary.opusIsSecureDecodeSupported()) {
            throw new OpusDecoderException("Opus decoder does not support secure decode.");
        }
        final byte[] array = list.get(0);
        if (array.length < 19) {
            throw new OpusDecoderException("Header size is too small.");
        }
        this.channelCount = (array[9] & 0xFF);
        if (this.channelCount > 8) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid channel count: ");
            sb.append(this.channelCount);
            throw new OpusDecoderException(sb.toString());
        }
        final int littleEndian16 = readLittleEndian16(array, 10);
        final int littleEndian17 = readLittleEndian16(array, 16);
        final byte[] array2 = new byte[8];
        if (array[18] == 0) {
            n = this.channelCount;
            if (n > 2) {
                throw new OpusDecoderException("Invalid Header, missing stream map.");
            }
            if (n == 2) {
                n = 1;
            }
            else {
                n = 0;
            }
            array2[0] = 0;
            array2[1] = 1;
            n2 = 1;
        }
        else {
            n = array.length;
            final int channelCount = this.channelCount;
            if (n < channelCount + 21) {
                throw new OpusDecoderException("Header size is too small.");
            }
            n2 = (array[19] & 0xFF);
            n = (array[20] & 0xFF);
            System.arraycopy(array, 21, array2, 0, channelCount);
        }
        if (list.size() == 3) {
            if (list.get(1).length != 8 || list.get(2).length != 8) {
                throw new OpusDecoderException("Invalid Codec Delay or Seek Preroll");
            }
            final long long1 = ByteBuffer.wrap(list.get(1)).order(ByteOrder.nativeOrder()).getLong();
            final long long2 = ByteBuffer.wrap(list.get(2)).order(ByteOrder.nativeOrder()).getLong();
            this.headerSkipSamples = nsToSamples(long1);
            this.headerSeekPreRollSamples = nsToSamples(long2);
        }
        else {
            this.headerSkipSamples = littleEndian16;
            this.headerSeekPreRollSamples = 3840;
        }
        this.nativeDecoderContext = this.opusInit(48000, this.channelCount, n2, n, littleEndian17, array2);
        if (this.nativeDecoderContext != 0L) {
            this.setInitialInputBufferSize(initialInputBufferSize);
            return;
        }
        throw new OpusDecoderException("Failed to initialize decoder");
    }
    
    private static int nsToSamples(final long n) {
        return (int)(n * 48000L / 1000000000L);
    }
    
    private native void opusClose(final long p0);
    
    private native int opusDecode(final long p0, final long p1, final ByteBuffer p2, final int p3, final SimpleOutputBuffer p4);
    
    private native int opusGetErrorCode(final long p0);
    
    private native String opusGetErrorMessage(final long p0);
    
    private native long opusInit(final int p0, final int p1, final int p2, final int p3, final int p4, final byte[] p5);
    
    private native void opusReset(final long p0);
    
    private native int opusSecureDecode(final long p0, final long p1, final ByteBuffer p2, final int p3, final SimpleOutputBuffer p4, final int p5, final ExoMediaCrypto p6, final int p7, final byte[] p8, final byte[] p9, final int p10, final int[] p11, final int[] p12);
    
    private static int readLittleEndian16(final byte[] array, final int n) {
        return (array[n + 1] & 0xFF) << 8 | (array[n] & 0xFF);
    }
    
    @Override
    protected DecoderInputBuffer createInputBuffer() {
        return new DecoderInputBuffer(2);
    }
    
    @Override
    protected SimpleOutputBuffer createOutputBuffer() {
        return new SimpleOutputBuffer(this);
    }
    
    @Override
    protected OpusDecoderException createUnexpectedDecodeException(final Throwable t) {
        return new OpusDecoderException("Unexpected decode error", t);
    }
    
    @Override
    protected OpusDecoderException decode(final DecoderInputBuffer decoderInputBuffer, final SimpleOutputBuffer simpleOutputBuffer, final boolean b) {
        if (b) {
            this.opusReset(this.nativeDecoderContext);
            int skipSamples;
            if (decoderInputBuffer.timeUs == 0L) {
                skipSamples = this.headerSkipSamples;
            }
            else {
                skipSamples = this.headerSeekPreRollSamples;
            }
            this.skipSamples = skipSamples;
        }
        final ByteBuffer data = decoderInputBuffer.data;
        final CryptoInfo cryptoInfo = decoderInputBuffer.cryptoInfo;
        int n;
        if (decoderInputBuffer.isEncrypted()) {
            n = this.opusSecureDecode(this.nativeDecoderContext, decoderInputBuffer.timeUs, data, data.limit(), simpleOutputBuffer, 48000, this.exoMediaCrypto, cryptoInfo.mode, cryptoInfo.key, cryptoInfo.iv, cryptoInfo.numSubSamples, cryptoInfo.numBytesOfClearData, cryptoInfo.numBytesOfEncryptedData);
        }
        else {
            n = this.opusDecode(this.nativeDecoderContext, decoderInputBuffer.timeUs, data, data.limit(), simpleOutputBuffer);
        }
        if (n >= 0) {
            final ByteBuffer data2 = simpleOutputBuffer.data;
            data2.position(0);
            data2.limit(n);
            final int skipSamples2 = this.skipSamples;
            if (skipSamples2 > 0) {
                final int n2 = this.channelCount * 2;
                final int n3 = skipSamples2 * n2;
                if (n <= n3) {
                    this.skipSamples = skipSamples2 - n / n2;
                    simpleOutputBuffer.addFlag(Integer.MIN_VALUE);
                    data2.position(n);
                }
                else {
                    this.skipSamples = 0;
                    data2.position(n3);
                }
            }
            return null;
        }
        if (n == -2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Drm error: ");
            sb.append(this.opusGetErrorMessage(this.nativeDecoderContext));
            final String string = sb.toString();
            return new OpusDecoderException(string, new DecryptionException(this.opusGetErrorCode(this.nativeDecoderContext), string));
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Decode error: ");
        sb2.append(this.opusGetErrorMessage(n));
        return new OpusDecoderException(sb2.toString());
    }
    
    public int getChannelCount() {
        return this.channelCount;
    }
    
    @Override
    public String getName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("libopus");
        sb.append(OpusLibrary.getVersion());
        return sb.toString();
    }
    
    public int getSampleRate() {
        return 48000;
    }
    
    @Override
    public void release() {
        super.release();
        this.opusClose(this.nativeDecoderContext);
    }
}
