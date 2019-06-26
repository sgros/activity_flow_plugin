// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import java.nio.ByteOrder;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Arrays;
import java.nio.ByteBuffer;

final class ChannelMappingAudioProcessor implements AudioProcessor
{
    private boolean active;
    private ByteBuffer buffer;
    private int channelCount;
    private boolean inputEnded;
    private ByteBuffer outputBuffer;
    private int[] outputChannels;
    private int[] pendingOutputChannels;
    private int sampleRateHz;
    
    public ChannelMappingAudioProcessor() {
        final ByteBuffer empty_BUFFER = AudioProcessor.EMPTY_BUFFER;
        this.buffer = empty_BUFFER;
        this.outputBuffer = empty_BUFFER;
        this.channelCount = -1;
        this.sampleRateHz = -1;
    }
    
    @Override
    public boolean configure(final int sampleRateHz, final int channelCount, final int n) throws UnhandledFormatException {
        final boolean b = Arrays.equals(this.pendingOutputChannels, this.outputChannels) ^ true;
        this.outputChannels = this.pendingOutputChannels;
        if (this.outputChannels == null) {
            this.active = false;
            return b;
        }
        if (n != 2) {
            throw new UnhandledFormatException(sampleRateHz, channelCount, n);
        }
        if (!b && this.sampleRateHz == sampleRateHz && this.channelCount == channelCount) {
            return false;
        }
        this.sampleRateHz = sampleRateHz;
        this.active = ((this.channelCount = channelCount) != this.outputChannels.length);
        int n2 = 0;
        while (true) {
            final int[] outputChannels = this.outputChannels;
            if (n2 >= outputChannels.length) {
                return true;
            }
            final int n3 = outputChannels[n2];
            if (n3 >= channelCount) {
                throw new UnhandledFormatException(sampleRateHz, channelCount, n);
            }
            this.active |= (n3 != n2);
            ++n2;
        }
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
        final int[] outputChannels = this.outputChannels;
        int n;
        if (outputChannels == null) {
            n = this.channelCount;
        }
        else {
            n = outputChannels.length;
        }
        return n;
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
        return this.active;
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
        Assertions.checkState(this.outputChannels != null);
        int i = byteBuffer.position();
        final int limit = byteBuffer.limit();
        final int capacity = (limit - i) / (this.channelCount * 2) * this.outputChannels.length * 2;
        if (this.buffer.capacity() < capacity) {
            this.buffer = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
        }
        else {
            this.buffer.clear();
        }
        while (i < limit) {
            final int[] outputChannels = this.outputChannels;
            for (int length = outputChannels.length, j = 0; j < length; ++j) {
                this.buffer.putShort(byteBuffer.getShort(outputChannels[j] * 2 + i));
            }
            i += this.channelCount * 2;
        }
        byteBuffer.position(limit);
        this.buffer.flip();
        this.outputBuffer = this.buffer;
    }
    
    @Override
    public void reset() {
        this.flush();
        this.buffer = AudioProcessor.EMPTY_BUFFER;
        this.channelCount = -1;
        this.sampleRateHz = -1;
        this.outputChannels = null;
        this.pendingOutputChannels = null;
        this.active = false;
    }
    
    public void setChannelMap(final int[] pendingOutputChannels) {
        this.pendingOutputChannels = pendingOutputChannels;
    }
}
