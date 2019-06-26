// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import java.nio.ByteOrder;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;

final class TrimmingAudioProcessor implements AudioProcessor
{
    private ByteBuffer buffer;
    private int bytesPerFrame;
    private int channelCount;
    private byte[] endBuffer;
    private int endBufferSize;
    private boolean inputEnded;
    private boolean isActive;
    private ByteBuffer outputBuffer;
    private int pendingTrimStartBytes;
    private boolean receivedInputSinceConfigure;
    private int sampleRateHz;
    private int trimEndFrames;
    private int trimStartFrames;
    private long trimmedFrameCount;
    
    public TrimmingAudioProcessor() {
        final ByteBuffer empty_BUFFER = AudioProcessor.EMPTY_BUFFER;
        this.buffer = empty_BUFFER;
        this.outputBuffer = empty_BUFFER;
        this.channelCount = -1;
        this.sampleRateHz = -1;
        this.endBuffer = Util.EMPTY_BYTE_ARRAY;
    }
    
    @Override
    public boolean configure(int trimStartFrames, int trimEndFrames, int n) throws UnhandledFormatException {
        if (n == 2) {
            n = this.endBufferSize;
            if (n > 0) {
                this.trimmedFrameCount += n / this.bytesPerFrame;
            }
            this.channelCount = trimEndFrames;
            this.sampleRateHz = trimStartFrames;
            this.bytesPerFrame = Util.getPcmFrameSize(2, trimEndFrames);
            trimEndFrames = this.trimEndFrames;
            n = this.bytesPerFrame;
            this.endBuffer = new byte[trimEndFrames * n];
            final boolean b = false;
            this.endBufferSize = 0;
            trimStartFrames = this.trimStartFrames;
            this.pendingTrimStartBytes = n * trimStartFrames;
            final boolean isActive = this.isActive;
            this.isActive = (trimStartFrames != 0 || trimEndFrames != 0);
            this.receivedInputSinceConfigure = false;
            boolean b2 = b;
            if (isActive != this.isActive) {
                b2 = true;
            }
            return b2;
        }
        throw new UnhandledFormatException(trimStartFrames, trimEndFrames, n);
    }
    
    @Override
    public void flush() {
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        this.inputEnded = false;
        if (this.receivedInputSinceConfigure) {
            this.pendingTrimStartBytes = 0;
        }
        this.endBufferSize = 0;
    }
    
    @Override
    public ByteBuffer getOutput() {
        ByteBuffer byteBuffer2;
        final ByteBuffer byteBuffer = byteBuffer2 = this.outputBuffer;
        if (this.inputEnded) {
            byteBuffer2 = byteBuffer;
            if (this.endBufferSize > 0 && (byteBuffer2 = byteBuffer) == AudioProcessor.EMPTY_BUFFER) {
                final int capacity = this.buffer.capacity();
                final int endBufferSize = this.endBufferSize;
                if (capacity < endBufferSize) {
                    this.buffer = ByteBuffer.allocateDirect(endBufferSize).order(ByteOrder.nativeOrder());
                }
                else {
                    this.buffer.clear();
                }
                this.buffer.put(this.endBuffer, 0, this.endBufferSize);
                this.endBufferSize = 0;
                this.buffer.flip();
                byteBuffer2 = this.buffer;
            }
        }
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        return byteBuffer2;
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
    
    public long getTrimmedFrameCount() {
        return this.trimmedFrameCount;
    }
    
    @Override
    public boolean isActive() {
        return this.isActive;
    }
    
    @Override
    public boolean isEnded() {
        return this.inputEnded && this.endBufferSize == 0 && this.outputBuffer == AudioProcessor.EMPTY_BUFFER;
    }
    
    @Override
    public void queueEndOfStream() {
        this.inputEnded = true;
    }
    
    @Override
    public void queueInput(final ByteBuffer src) {
        final int position = src.position();
        final int limit = src.limit();
        final int a = limit - position;
        if (a == 0) {
            return;
        }
        this.receivedInputSinceConfigure = true;
        final int min = Math.min(a, this.pendingTrimStartBytes);
        this.trimmedFrameCount += min / this.bytesPerFrame;
        this.pendingTrimStartBytes -= min;
        src.position(position + min);
        if (this.pendingTrimStartBytes > 0) {
            return;
        }
        final int n = a - min;
        final int capacity = this.endBufferSize + n - this.endBuffer.length;
        if (this.buffer.capacity() < capacity) {
            this.buffer = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
        }
        else {
            this.buffer.clear();
        }
        final int constrainValue = Util.constrainValue(capacity, 0, this.endBufferSize);
        this.buffer.put(this.endBuffer, 0, constrainValue);
        final int constrainValue2 = Util.constrainValue(capacity - constrainValue, 0, n);
        src.limit(src.position() + constrainValue2);
        this.buffer.put(src);
        src.limit(limit);
        final int length = n - constrainValue2;
        this.endBufferSize -= constrainValue;
        final byte[] endBuffer = this.endBuffer;
        System.arraycopy(endBuffer, constrainValue, endBuffer, 0, this.endBufferSize);
        src.get(this.endBuffer, this.endBufferSize, length);
        this.endBufferSize += length;
        this.buffer.flip();
        this.outputBuffer = this.buffer;
    }
    
    @Override
    public void reset() {
        this.flush();
        this.buffer = AudioProcessor.EMPTY_BUFFER;
        this.channelCount = -1;
        this.sampleRateHz = -1;
        this.endBuffer = Util.EMPTY_BYTE_ARRAY;
    }
    
    public void resetTrimmedFrameCount() {
        this.trimmedFrameCount = 0L;
    }
    
    public void setTrimFrameCount(final int trimStartFrames, final int trimEndFrames) {
        this.trimStartFrames = trimStartFrames;
        this.trimEndFrames = trimEndFrames;
    }
}
