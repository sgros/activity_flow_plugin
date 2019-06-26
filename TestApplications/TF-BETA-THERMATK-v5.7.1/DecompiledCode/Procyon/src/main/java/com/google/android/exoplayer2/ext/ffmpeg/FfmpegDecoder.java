// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ext.ffmpeg;

import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.decoder.OutputBuffer;
import java.util.List;
import java.nio.ByteBuffer;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.SimpleOutputBuffer;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.SimpleDecoder;

final class FfmpegDecoder extends SimpleDecoder<DecoderInputBuffer, SimpleOutputBuffer, FfmpegDecoderException>
{
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
    
    public FfmpegDecoder(int n, final int n2, final int initialInputBufferSize, final Format format, final boolean b) throws FfmpegDecoderException {
        super(new DecoderInputBuffer[n], new SimpleOutputBuffer[n2]);
        Assertions.checkNotNull(format.sampleMimeType);
        final String codecName = FfmpegLibrary.getCodecName(format.sampleMimeType, format.pcmEncoding);
        Assertions.checkNotNull(codecName);
        this.codecName = codecName;
        this.extraData = getExtraData(format.sampleMimeType, format.initializationData);
        if (b) {
            n = 4;
        }
        else {
            n = 2;
        }
        this.encoding = n;
        if (b) {
            n = 131072;
        }
        else {
            n = 65536;
        }
        this.outputBufferSize = n;
        this.nativeContext = this.ffmpegInitialize(this.codecName, this.extraData, b, format.sampleRate, format.channelCount);
        if (this.nativeContext != 0L) {
            this.setInitialInputBufferSize(initialInputBufferSize);
            return;
        }
        throw new FfmpegDecoderException("Initialization failed.");
    }
    
    private native int ffmpegDecode(final long p0, final ByteBuffer p1, final int p2, final ByteBuffer p3, final int p4);
    
    private native int ffmpegGetChannelCount(final long p0);
    
    private native int ffmpegGetSampleRate(final long p0);
    
    private native long ffmpegInitialize(final String p0, final byte[] p1, final boolean p2, final int p3, final int p4);
    
    private native void ffmpegRelease(final long p0);
    
    private native long ffmpegReset(final long p0, final byte[] p1);
    
    private static byte[] getExtraData(final String s, final List<byte[]> list) {
        int n = 0;
        Label_0109: {
            switch (s.hashCode()) {
                case 1504891608: {
                    if (s.equals("audio/opus")) {
                        n = 2;
                        break Label_0109;
                    }
                    break;
                }
                case 1504470054: {
                    if (s.equals("audio/alac")) {
                        n = 1;
                        break Label_0109;
                    }
                    break;
                }
                case -53558318: {
                    if (s.equals("audio/mp4a-latm")) {
                        n = 0;
                        break Label_0109;
                    }
                    break;
                }
                case -1003765268: {
                    if (s.equals("audio/vorbis")) {
                        n = 3;
                        break Label_0109;
                    }
                    break;
                }
            }
            n = -1;
        }
        if (n == 0 || n == 1 || n == 2) {
            return list.get(0);
        }
        if (n != 3) {
            return null;
        }
        final byte[] array = list.get(0);
        final byte[] array2 = list.get(1);
        final byte[] array3 = new byte[array.length + array2.length + 6];
        array3[0] = (byte)(array.length >> 8);
        array3[1] = (byte)(array.length & 0xFF);
        System.arraycopy(array, 0, array3, 2, array.length);
        array3[array.length + 2] = 0;
        array3[array.length + 3] = 0;
        array3[array.length + 4] = (byte)(array2.length >> 8);
        array3[array.length + 5] = (byte)(array2.length & 0xFF);
        System.arraycopy(array2, 0, array3, array.length + 6, array2.length);
        return array3;
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
    protected FfmpegDecoderException createUnexpectedDecodeException(final Throwable t) {
        return new FfmpegDecoderException("Unexpected decode error", t);
    }
    
    @Override
    protected FfmpegDecoderException decode(final DecoderInputBuffer decoderInputBuffer, final SimpleOutputBuffer simpleOutputBuffer, final boolean b) {
        if (b) {
            this.nativeContext = this.ffmpegReset(this.nativeContext, this.extraData);
            if (this.nativeContext == 0L) {
                return new FfmpegDecoderException("Error resetting (see logcat).");
            }
        }
        final ByteBuffer data = decoderInputBuffer.data;
        final int ffmpegDecode = this.ffmpegDecode(this.nativeContext, data, data.limit(), simpleOutputBuffer.init(decoderInputBuffer.timeUs, this.outputBufferSize), this.outputBufferSize);
        if (ffmpegDecode == -1) {
            simpleOutputBuffer.setFlags(Integer.MIN_VALUE);
            return null;
        }
        if (ffmpegDecode == -2) {
            return new FfmpegDecoderException("Error decoding (see logcat).");
        }
        if (!this.hasOutputFormat) {
            this.channelCount = this.ffmpegGetChannelCount(this.nativeContext);
            this.sampleRate = this.ffmpegGetSampleRate(this.nativeContext);
            if (this.sampleRate == 0 && "alac".equals(this.codecName)) {
                Assertions.checkNotNull(this.extraData);
                final ParsableByteArray parsableByteArray = new ParsableByteArray(this.extraData);
                parsableByteArray.setPosition(this.extraData.length - 4);
                this.sampleRate = parsableByteArray.readUnsignedIntToInt();
            }
            this.hasOutputFormat = true;
        }
        simpleOutputBuffer.data.position(0);
        simpleOutputBuffer.data.limit(ffmpegDecode);
        return null;
    }
    
    public int getChannelCount() {
        return this.channelCount;
    }
    
    public int getEncoding() {
        return this.encoding;
    }
    
    @Override
    public String getName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ffmpeg");
        sb.append(FfmpegLibrary.getVersion());
        sb.append("-");
        sb.append(this.codecName);
        return sb.toString();
    }
    
    public int getSampleRate() {
        return this.sampleRate;
    }
    
    @Override
    public void release() {
        super.release();
        this.ffmpegRelease(this.nativeContext);
        this.nativeContext = 0L;
    }
}
