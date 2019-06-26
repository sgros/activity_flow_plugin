// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.Util;
import java.nio.ByteOrder;
import com.google.android.exoplayer2.util.Assertions;
import java.nio.ShortBuffer;
import java.nio.ByteBuffer;

public final class SonicAudioProcessor implements AudioProcessor
{
    private ByteBuffer buffer;
    private int channelCount;
    private long inputBytes;
    private boolean inputEnded;
    private ByteBuffer outputBuffer;
    private long outputBytes;
    private int outputSampleRateHz;
    private int pendingOutputSampleRateHz;
    private float pitch;
    private int sampleRateHz;
    private ShortBuffer shortBuffer;
    private Sonic sonic;
    private float speed;
    
    public SonicAudioProcessor() {
        this.speed = 1.0f;
        this.pitch = 1.0f;
        this.channelCount = -1;
        this.sampleRateHz = -1;
        this.outputSampleRateHz = -1;
        this.buffer = AudioProcessor.EMPTY_BUFFER;
        this.shortBuffer = this.buffer.asShortBuffer();
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        this.pendingOutputSampleRateHz = -1;
    }
    
    @Override
    public boolean configure(final int sampleRateHz, final int channelCount, int pendingOutputSampleRateHz) throws UnhandledFormatException {
        if (pendingOutputSampleRateHz != 2) {
            throw new UnhandledFormatException(sampleRateHz, channelCount, pendingOutputSampleRateHz);
        }
        if ((pendingOutputSampleRateHz = this.pendingOutputSampleRateHz) == -1) {
            pendingOutputSampleRateHz = sampleRateHz;
        }
        if (this.sampleRateHz == sampleRateHz && this.channelCount == channelCount && this.outputSampleRateHz == pendingOutputSampleRateHz) {
            return false;
        }
        this.sampleRateHz = sampleRateHz;
        this.channelCount = channelCount;
        this.outputSampleRateHz = pendingOutputSampleRateHz;
        this.sonic = null;
        return true;
    }
    
    @Override
    public void flush() {
        if (this.isActive()) {
            final Sonic sonic = this.sonic;
            if (sonic == null) {
                this.sonic = new Sonic(this.sampleRateHz, this.channelCount, this.speed, this.pitch, this.outputSampleRateHz);
            }
            else {
                sonic.flush();
            }
        }
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        this.inputBytes = 0L;
        this.outputBytes = 0L;
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
        return this.outputSampleRateHz;
    }
    
    @Override
    public boolean isActive() {
        return this.sampleRateHz != -1 && (Math.abs(this.speed - 1.0f) >= 0.01f || Math.abs(this.pitch - 1.0f) >= 0.01f || this.outputSampleRateHz != this.sampleRateHz);
    }
    
    @Override
    public boolean isEnded() {
        if (this.inputEnded) {
            final Sonic sonic = this.sonic;
            if (sonic == null || sonic.getFramesAvailable() == 0) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void queueEndOfStream() {
        Assertions.checkState(this.sonic != null);
        this.sonic.queueEndOfStream();
        this.inputEnded = true;
    }
    
    @Override
    public void queueInput(final ByteBuffer byteBuffer) {
        Assertions.checkState(this.sonic != null);
        if (byteBuffer.hasRemaining()) {
            final ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
            final int remaining = byteBuffer.remaining();
            this.inputBytes += remaining;
            this.sonic.queueInput(shortBuffer);
            byteBuffer.position(byteBuffer.position() + remaining);
        }
        final int capacity = this.sonic.getFramesAvailable() * this.channelCount * 2;
        if (capacity > 0) {
            if (this.buffer.capacity() < capacity) {
                this.buffer = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
                this.shortBuffer = this.buffer.asShortBuffer();
            }
            else {
                this.buffer.clear();
                this.shortBuffer.clear();
            }
            this.sonic.getOutput(this.shortBuffer);
            this.outputBytes += capacity;
            this.buffer.limit(capacity);
            this.outputBuffer = this.buffer;
        }
    }
    
    @Override
    public void reset() {
        this.speed = 1.0f;
        this.pitch = 1.0f;
        this.channelCount = -1;
        this.sampleRateHz = -1;
        this.outputSampleRateHz = -1;
        this.buffer = AudioProcessor.EMPTY_BUFFER;
        this.shortBuffer = this.buffer.asShortBuffer();
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        this.pendingOutputSampleRateHz = -1;
        this.sonic = null;
        this.inputBytes = 0L;
        this.outputBytes = 0L;
        this.inputEnded = false;
    }
    
    public long scaleDurationForSpeedup(long n) {
        final long outputBytes = this.outputBytes;
        if (outputBytes >= 1024L) {
            final int outputSampleRateHz = this.outputSampleRateHz;
            final int sampleRateHz = this.sampleRateHz;
            if (outputSampleRateHz == sampleRateHz) {
                n = Util.scaleLargeTimestamp(n, this.inputBytes, outputBytes);
            }
            else {
                n = Util.scaleLargeTimestamp(n, this.inputBytes * outputSampleRateHz, outputBytes * sampleRateHz);
            }
            return n;
        }
        final double v = this.speed;
        final double v2 = (double)n;
        Double.isNaN(v);
        Double.isNaN(v2);
        return (long)(v * v2);
    }
    
    public float setPitch(float constrainValue) {
        constrainValue = Util.constrainValue(constrainValue, 0.1f, 8.0f);
        if (this.pitch != constrainValue) {
            this.pitch = constrainValue;
            this.sonic = null;
        }
        this.flush();
        return constrainValue;
    }
    
    public float setSpeed(float constrainValue) {
        constrainValue = Util.constrainValue(constrainValue, 0.1f, 8.0f);
        if (this.speed != constrainValue) {
            this.speed = constrainValue;
            this.sonic = null;
        }
        this.flush();
        return constrainValue;
    }
}
