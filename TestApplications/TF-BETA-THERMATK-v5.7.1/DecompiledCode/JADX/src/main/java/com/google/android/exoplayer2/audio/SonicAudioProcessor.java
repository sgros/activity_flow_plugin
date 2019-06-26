package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.audio.AudioProcessor.UnhandledFormatException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public final class SonicAudioProcessor implements AudioProcessor {
    private ByteBuffer buffer = AudioProcessor.EMPTY_BUFFER;
    private int channelCount = -1;
    private long inputBytes;
    private boolean inputEnded;
    private ByteBuffer outputBuffer = AudioProcessor.EMPTY_BUFFER;
    private long outputBytes;
    private int outputSampleRateHz = -1;
    private int pendingOutputSampleRateHz = -1;
    private float pitch = 1.0f;
    private int sampleRateHz = -1;
    private ShortBuffer shortBuffer = this.buffer.asShortBuffer();
    private Sonic sonic;
    private float speed = 1.0f;

    public int getOutputEncoding() {
        return 2;
    }

    public float setSpeed(float f) {
        f = Util.constrainValue(f, 0.1f, 8.0f);
        if (this.speed != f) {
            this.speed = f;
            this.sonic = null;
        }
        flush();
        return f;
    }

    public float setPitch(float f) {
        f = Util.constrainValue(f, 0.1f, 8.0f);
        if (this.pitch != f) {
            this.pitch = f;
            this.sonic = null;
        }
        flush();
        return f;
    }

    public long scaleDurationForSpeedup(long j) {
        long j2 = this.outputBytes;
        if (j2 >= 1024) {
            long scaleLargeTimestamp;
            int i = this.outputSampleRateHz;
            int i2 = this.sampleRateHz;
            if (i == i2) {
                scaleLargeTimestamp = Util.scaleLargeTimestamp(j, this.inputBytes, j2);
            } else {
                scaleLargeTimestamp = Util.scaleLargeTimestamp(j, this.inputBytes * ((long) i), j2 * ((long) i2));
            }
            return scaleLargeTimestamp;
        }
        double d = (double) this.speed;
        double d2 = (double) j;
        Double.isNaN(d);
        Double.isNaN(d2);
        return (long) (d * d2);
    }

    public boolean configure(int i, int i2, int i3) throws UnhandledFormatException {
        if (i3 == 2) {
            i3 = this.pendingOutputSampleRateHz;
            if (i3 == -1) {
                i3 = i;
            }
            if (this.sampleRateHz == i && this.channelCount == i2 && this.outputSampleRateHz == i3) {
                return false;
            }
            this.sampleRateHz = i;
            this.channelCount = i2;
            this.outputSampleRateHz = i3;
            this.sonic = null;
            return true;
        }
        throw new UnhandledFormatException(i, i2, i3);
    }

    public boolean isActive() {
        return this.sampleRateHz != -1 && (Math.abs(this.speed - 1.0f) >= 0.01f || Math.abs(this.pitch - 1.0f) >= 0.01f || this.outputSampleRateHz != this.sampleRateHz);
    }

    public int getOutputChannelCount() {
        return this.channelCount;
    }

    public int getOutputSampleRateHz() {
        return this.outputSampleRateHz;
    }

    public void queueInput(ByteBuffer byteBuffer) {
        Assertions.checkState(this.sonic != null);
        if (byteBuffer.hasRemaining()) {
            ShortBuffer asShortBuffer = byteBuffer.asShortBuffer();
            int remaining = byteBuffer.remaining();
            this.inputBytes += (long) remaining;
            this.sonic.queueInput(asShortBuffer);
            byteBuffer.position(byteBuffer.position() + remaining);
        }
        int framesAvailable = (this.sonic.getFramesAvailable() * this.channelCount) * 2;
        if (framesAvailable > 0) {
            if (this.buffer.capacity() < framesAvailable) {
                this.buffer = ByteBuffer.allocateDirect(framesAvailable).order(ByteOrder.nativeOrder());
                this.shortBuffer = this.buffer.asShortBuffer();
            } else {
                this.buffer.clear();
                this.shortBuffer.clear();
            }
            this.sonic.getOutput(this.shortBuffer);
            this.outputBytes += (long) framesAvailable;
            this.buffer.limit(framesAvailable);
            this.outputBuffer = this.buffer;
        }
    }

    public void queueEndOfStream() {
        Assertions.checkState(this.sonic != null);
        this.sonic.queueEndOfStream();
        this.inputEnded = true;
    }

    public ByteBuffer getOutput() {
        ByteBuffer byteBuffer = this.outputBuffer;
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        return byteBuffer;
    }

    public boolean isEnded() {
        if (this.inputEnded) {
            Sonic sonic = this.sonic;
            if (sonic == null || sonic.getFramesAvailable() == 0) {
                return true;
            }
        }
        return false;
    }

    public void flush() {
        if (isActive()) {
            Sonic sonic = this.sonic;
            if (sonic == null) {
                this.sonic = new Sonic(this.sampleRateHz, this.channelCount, this.speed, this.pitch, this.outputSampleRateHz);
            } else {
                sonic.flush();
            }
        }
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        this.inputBytes = 0;
        this.outputBytes = 0;
        this.inputEnded = false;
    }

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
        this.inputBytes = 0;
        this.outputBytes = 0;
        this.inputEnded = false;
    }
}
