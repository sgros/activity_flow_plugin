// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import java.nio.ByteOrder;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;

public final class SilenceSkippingAudioProcessor implements AudioProcessor
{
    private ByteBuffer buffer;
    private int bytesPerFrame;
    private int channelCount;
    private boolean enabled;
    private boolean hasOutputNoise;
    private boolean inputEnded;
    private byte[] maybeSilenceBuffer;
    private int maybeSilenceBufferSize;
    private ByteBuffer outputBuffer;
    private byte[] paddingBuffer;
    private int paddingSize;
    private int sampleRateHz;
    private long skippedFrames;
    private int state;
    
    public SilenceSkippingAudioProcessor() {
        final ByteBuffer empty_BUFFER = AudioProcessor.EMPTY_BUFFER;
        this.buffer = empty_BUFFER;
        this.outputBuffer = empty_BUFFER;
        this.channelCount = -1;
        this.sampleRateHz = -1;
        final byte[] empty_BYTE_ARRAY = Util.EMPTY_BYTE_ARRAY;
        this.maybeSilenceBuffer = empty_BYTE_ARRAY;
        this.paddingBuffer = empty_BYTE_ARRAY;
    }
    
    private int durationUsToFrames(final long n) {
        return (int)(n * this.sampleRateHz / 1000000L);
    }
    
    private int findNoiseLimit(final ByteBuffer byteBuffer) {
        for (int i = byteBuffer.limit() - 1; i >= byteBuffer.position(); i -= 2) {
            if (Math.abs(byteBuffer.get(i)) > 4) {
                final int bytesPerFrame = this.bytesPerFrame;
                return i / bytesPerFrame * bytesPerFrame + bytesPerFrame;
            }
        }
        return byteBuffer.position();
    }
    
    private int findNoisePosition(final ByteBuffer byteBuffer) {
        for (int i = byteBuffer.position() + 1; i < byteBuffer.limit(); i += 2) {
            if (Math.abs(byteBuffer.get(i)) > 4) {
                final int bytesPerFrame = this.bytesPerFrame;
                return bytesPerFrame * (i / bytesPerFrame);
            }
        }
        return byteBuffer.limit();
    }
    
    private void output(final ByteBuffer src) {
        this.prepareForOutput(src.remaining());
        this.buffer.put(src);
        this.buffer.flip();
        this.outputBuffer = this.buffer;
    }
    
    private void output(final byte[] src, final int length) {
        this.prepareForOutput(length);
        this.buffer.put(src, 0, length);
        this.buffer.flip();
        this.outputBuffer = this.buffer;
    }
    
    private void prepareForOutput(final int capacity) {
        if (this.buffer.capacity() < capacity) {
            this.buffer = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
        }
        else {
            this.buffer.clear();
        }
        if (capacity > 0) {
            this.hasOutputNoise = true;
        }
    }
    
    private void processMaybeSilence(final ByteBuffer byteBuffer) {
        final int limit = byteBuffer.limit();
        final int noisePosition = this.findNoisePosition(byteBuffer);
        final int a = noisePosition - byteBuffer.position();
        final byte[] maybeSilenceBuffer = this.maybeSilenceBuffer;
        final int length = maybeSilenceBuffer.length;
        final int maybeSilenceBufferSize = this.maybeSilenceBufferSize;
        final int b = length - maybeSilenceBufferSize;
        if (noisePosition < limit && a < b) {
            this.output(maybeSilenceBuffer, maybeSilenceBufferSize);
            this.maybeSilenceBufferSize = 0;
            this.state = 0;
        }
        else {
            final int min = Math.min(a, b);
            byteBuffer.limit(byteBuffer.position() + min);
            byteBuffer.get(this.maybeSilenceBuffer, this.maybeSilenceBufferSize, min);
            this.maybeSilenceBufferSize += min;
            final int maybeSilenceBufferSize2 = this.maybeSilenceBufferSize;
            final byte[] maybeSilenceBuffer2 = this.maybeSilenceBuffer;
            if (maybeSilenceBufferSize2 == maybeSilenceBuffer2.length) {
                if (this.hasOutputNoise) {
                    this.output(maybeSilenceBuffer2, this.paddingSize);
                    this.skippedFrames += (this.maybeSilenceBufferSize - this.paddingSize * 2) / this.bytesPerFrame;
                }
                else {
                    this.skippedFrames += (maybeSilenceBufferSize2 - this.paddingSize) / this.bytesPerFrame;
                }
                this.updatePaddingBuffer(byteBuffer, this.maybeSilenceBuffer, this.maybeSilenceBufferSize);
                this.maybeSilenceBufferSize = 0;
                this.state = 2;
            }
            byteBuffer.limit(limit);
        }
    }
    
    private void processNoisy(final ByteBuffer byteBuffer) {
        final int limit = byteBuffer.limit();
        byteBuffer.limit(Math.min(limit, byteBuffer.position() + this.maybeSilenceBuffer.length));
        final int noiseLimit = this.findNoiseLimit(byteBuffer);
        if (noiseLimit == byteBuffer.position()) {
            this.state = 1;
        }
        else {
            byteBuffer.limit(noiseLimit);
            this.output(byteBuffer);
        }
        byteBuffer.limit(limit);
    }
    
    private void processSilence(final ByteBuffer byteBuffer) {
        final int limit = byteBuffer.limit();
        final int noisePosition = this.findNoisePosition(byteBuffer);
        byteBuffer.limit(noisePosition);
        this.skippedFrames += byteBuffer.remaining() / this.bytesPerFrame;
        this.updatePaddingBuffer(byteBuffer, this.paddingBuffer, this.paddingSize);
        if (noisePosition < limit) {
            this.output(this.paddingBuffer, this.paddingSize);
            this.state = 0;
            byteBuffer.limit(limit);
        }
    }
    
    private void updatePaddingBuffer(final ByteBuffer byteBuffer, final byte[] array, final int n) {
        final int min = Math.min(byteBuffer.remaining(), this.paddingSize);
        final int offset = this.paddingSize - min;
        System.arraycopy(array, n - offset, this.paddingBuffer, 0, offset);
        byteBuffer.position(byteBuffer.limit() - min);
        byteBuffer.get(this.paddingBuffer, offset, min);
    }
    
    @Override
    public boolean configure(final int sampleRateHz, final int channelCount, final int n) throws UnhandledFormatException {
        if (n != 2) {
            throw new UnhandledFormatException(sampleRateHz, channelCount, n);
        }
        if (this.sampleRateHz == sampleRateHz && this.channelCount == channelCount) {
            return false;
        }
        this.sampleRateHz = sampleRateHz;
        this.channelCount = channelCount;
        this.bytesPerFrame = channelCount * 2;
        return true;
    }
    
    @Override
    public void flush() {
        if (this.isActive()) {
            final int n = this.durationUsToFrames(150000L) * this.bytesPerFrame;
            if (this.maybeSilenceBuffer.length != n) {
                this.maybeSilenceBuffer = new byte[n];
            }
            this.paddingSize = this.durationUsToFrames(20000L) * this.bytesPerFrame;
            final int length = this.paddingBuffer.length;
            final int paddingSize = this.paddingSize;
            if (length != paddingSize) {
                this.paddingBuffer = new byte[paddingSize];
            }
        }
        this.state = 0;
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        this.inputEnded = false;
        this.skippedFrames = 0L;
        this.maybeSilenceBufferSize = 0;
        this.hasOutputNoise = false;
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
    
    public long getSkippedFrames() {
        return this.skippedFrames;
    }
    
    @Override
    public boolean isActive() {
        return this.sampleRateHz != -1 && this.enabled;
    }
    
    @Override
    public boolean isEnded() {
        return this.inputEnded && this.outputBuffer == AudioProcessor.EMPTY_BUFFER;
    }
    
    @Override
    public void queueEndOfStream() {
        this.inputEnded = true;
        final int maybeSilenceBufferSize = this.maybeSilenceBufferSize;
        if (maybeSilenceBufferSize > 0) {
            this.output(this.maybeSilenceBuffer, maybeSilenceBufferSize);
        }
        if (!this.hasOutputNoise) {
            this.skippedFrames += this.paddingSize / this.bytesPerFrame;
        }
    }
    
    @Override
    public void queueInput(final ByteBuffer byteBuffer) {
        while (byteBuffer.hasRemaining() && !this.outputBuffer.hasRemaining()) {
            final int state = this.state;
            if (state != 0) {
                if (state != 1) {
                    if (state != 2) {
                        throw new IllegalStateException();
                    }
                    this.processSilence(byteBuffer);
                }
                else {
                    this.processMaybeSilence(byteBuffer);
                }
            }
            else {
                this.processNoisy(byteBuffer);
            }
        }
    }
    
    @Override
    public void reset() {
        this.enabled = false;
        this.flush();
        this.buffer = AudioProcessor.EMPTY_BUFFER;
        this.channelCount = -1;
        this.sampleRateHz = -1;
        this.paddingSize = 0;
        final byte[] empty_BYTE_ARRAY = Util.EMPTY_BYTE_ARRAY;
        this.maybeSilenceBuffer = empty_BYTE_ARRAY;
        this.paddingBuffer = empty_BYTE_ARRAY;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        this.flush();
    }
}
