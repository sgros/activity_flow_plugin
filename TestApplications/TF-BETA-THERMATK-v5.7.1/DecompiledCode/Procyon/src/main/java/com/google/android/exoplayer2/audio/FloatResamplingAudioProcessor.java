// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import java.nio.ByteOrder;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;

final class FloatResamplingAudioProcessor implements AudioProcessor
{
    private static final int FLOAT_NAN_AS_INT;
    private ByteBuffer buffer;
    private int channelCount;
    private boolean inputEnded;
    private ByteBuffer outputBuffer;
    private int sampleRateHz;
    private int sourceEncoding;
    
    static {
        FLOAT_NAN_AS_INT = Float.floatToIntBits(Float.NaN);
    }
    
    public FloatResamplingAudioProcessor() {
        this.sampleRateHz = -1;
        this.channelCount = -1;
        this.sourceEncoding = 0;
        final ByteBuffer empty_BUFFER = AudioProcessor.EMPTY_BUFFER;
        this.buffer = empty_BUFFER;
        this.outputBuffer = empty_BUFFER;
    }
    
    private static void writePcm32BitFloat(int n, final ByteBuffer byteBuffer) {
        final double v = n;
        Double.isNaN(v);
        if ((n = Float.floatToIntBits((float)(v * 4.656612875245797E-10))) == FloatResamplingAudioProcessor.FLOAT_NAN_AS_INT) {
            n = Float.floatToIntBits(0.0f);
        }
        byteBuffer.putInt(n);
    }
    
    @Override
    public boolean configure(final int sampleRateHz, final int channelCount, final int sourceEncoding) throws UnhandledFormatException {
        if (!Util.isEncodingHighResolutionIntegerPcm(sourceEncoding)) {
            throw new UnhandledFormatException(sampleRateHz, channelCount, sourceEncoding);
        }
        if (this.sampleRateHz == sampleRateHz && this.channelCount == channelCount && this.sourceEncoding == sourceEncoding) {
            return false;
        }
        this.sampleRateHz = sampleRateHz;
        this.channelCount = channelCount;
        this.sourceEncoding = sourceEncoding;
        return true;
    }
    
    @Override
    public void flush() {
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        this.inputEnded = false;
    }
    
    @Override
    public ByteBuffer getOutput() {
        final ByteBuffer outputBuffer = this.outputBuffer;
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        return outputBuffer;
    }
    
    @Override
    public int getOutputChannelCount() {
        return this.channelCount;
    }
    
    @Override
    public int getOutputEncoding() {
        return 4;
    }
    
    @Override
    public int getOutputSampleRateHz() {
        return this.sampleRateHz;
    }
    
    @Override
    public boolean isActive() {
        return Util.isEncodingHighResolutionIntegerPcm(this.sourceEncoding);
    }
    
    @Override
    public boolean isEnded() {
        return this.inputEnded && this.outputBuffer == AudioProcessor.EMPTY_BUFFER;
    }
    
    @Override
    public void queueEndOfStream() {
        this.inputEnded = true;
    }
    
    @Override
    public void queueInput(final ByteBuffer byteBuffer) {
        final boolean b = this.sourceEncoding == 1073741824;
        int i = byteBuffer.position();
        final int limit = byteBuffer.limit();
        int capacity = limit - i;
        if (!b) {
            capacity = capacity / 3 * 4;
        }
        if (this.buffer.capacity() < capacity) {
            this.buffer = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
        }
        else {
            this.buffer.clear();
        }
        int j = i;
        if (b) {
            while (i < limit) {
                writePcm32BitFloat((byteBuffer.get(i) & 0xFF) | (byteBuffer.get(i + 1) & 0xFF) << 8 | (byteBuffer.get(i + 2) & 0xFF) << 16 | (byteBuffer.get(i + 3) & 0xFF) << 24, this.buffer);
                i += 4;
            }
        }
        else {
            while (j < limit) {
                writePcm32BitFloat((byteBuffer.get(j) & 0xFF) << 8 | (byteBuffer.get(j + 1) & 0xFF) << 16 | (byteBuffer.get(j + 2) & 0xFF) << 24, this.buffer);
                j += 3;
            }
        }
        byteBuffer.position(byteBuffer.limit());
        this.buffer.flip();
        this.outputBuffer = this.buffer;
    }
    
    @Override
    public void reset() {
        this.flush();
        this.sampleRateHz = -1;
        this.channelCount = -1;
        this.sourceEncoding = 0;
        this.buffer = AudioProcessor.EMPTY_BUFFER;
    }
}
