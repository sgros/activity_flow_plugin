// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;

final class ResamplingAudioProcessor implements AudioProcessor
{
    private ByteBuffer buffer;
    private int channelCount;
    private int encoding;
    private boolean inputEnded;
    private ByteBuffer outputBuffer;
    private int sampleRateHz;
    
    public ResamplingAudioProcessor() {
        this.sampleRateHz = -1;
        this.channelCount = -1;
        this.encoding = 0;
        final ByteBuffer empty_BUFFER = AudioProcessor.EMPTY_BUFFER;
        this.buffer = empty_BUFFER;
        this.outputBuffer = empty_BUFFER;
    }
    
    @Override
    public boolean configure(final int sampleRateHz, final int channelCount, final int encoding) throws UnhandledFormatException {
        if (encoding != 3 && encoding != 2 && encoding != Integer.MIN_VALUE && encoding != 1073741824) {
            throw new UnhandledFormatException(sampleRateHz, channelCount, encoding);
        }
        if (this.sampleRateHz == sampleRateHz && this.channelCount == channelCount && this.encoding == encoding) {
            return false;
        }
        this.sampleRateHz = sampleRateHz;
        this.channelCount = channelCount;
        this.encoding = encoding;
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
        return 2;
    }
    
    @Override
    public int getOutputSampleRateHz() {
        return this.sampleRateHz;
    }
    
    @Override
    public boolean isActive() {
        final int encoding = this.encoding;
        return encoding != 0 && encoding != 2;
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
        int i = byteBuffer.position();
        final int limit = byteBuffer.limit();
        final int n = limit - i;
        final int encoding = this.encoding;
        int capacity = 0;
        Label_0074: {
            int n2;
            if (encoding != Integer.MIN_VALUE) {
                n2 = n;
                if (encoding != 3) {
                    if (encoding == 1073741824) {
                        capacity = n / 2;
                        break Label_0074;
                    }
                    throw new IllegalStateException();
                }
            }
            else {
                n2 = n / 3;
            }
            capacity = n2 * 2;
        }
        if (this.buffer.capacity() < capacity) {
            this.buffer = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
        }
        else {
            this.buffer.clear();
        }
        final int encoding2 = this.encoding;
        int j = i;
        if (encoding2 != Integer.MIN_VALUE) {
            int k = i;
            if (encoding2 != 3) {
                if (encoding2 != 1073741824) {
                    throw new IllegalStateException();
                }
                while (i < limit) {
                    this.buffer.put(byteBuffer.get(i + 2));
                    this.buffer.put(byteBuffer.get(i + 3));
                    i += 4;
                }
            }
            else {
                while (k < limit) {
                    this.buffer.put((byte)0);
                    this.buffer.put((byte)((byteBuffer.get(k) & 0xFF) - 128));
                    ++k;
                }
            }
        }
        else {
            while (j < limit) {
                this.buffer.put(byteBuffer.get(j + 1));
                this.buffer.put(byteBuffer.get(j + 2));
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
        this.encoding = 0;
        this.buffer = AudioProcessor.EMPTY_BUFFER;
    }
}
