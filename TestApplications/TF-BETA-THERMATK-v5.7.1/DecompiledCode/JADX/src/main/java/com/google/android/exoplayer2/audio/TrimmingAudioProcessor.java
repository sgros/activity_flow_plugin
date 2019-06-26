package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.audio.AudioProcessor.UnhandledFormatException;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class TrimmingAudioProcessor implements AudioProcessor {
    private ByteBuffer buffer;
    private int bytesPerFrame;
    private int channelCount = -1;
    private byte[] endBuffer = Util.EMPTY_BYTE_ARRAY;
    private int endBufferSize;
    private boolean inputEnded;
    private boolean isActive;
    private ByteBuffer outputBuffer;
    private int pendingTrimStartBytes;
    private boolean receivedInputSinceConfigure;
    private int sampleRateHz = -1;
    private int trimEndFrames;
    private int trimStartFrames;
    private long trimmedFrameCount;

    public int getOutputEncoding() {
        return 2;
    }

    public TrimmingAudioProcessor() {
        ByteBuffer byteBuffer = AudioProcessor.EMPTY_BUFFER;
        this.buffer = byteBuffer;
        this.outputBuffer = byteBuffer;
    }

    public void setTrimFrameCount(int i, int i2) {
        this.trimStartFrames = i;
        this.trimEndFrames = i2;
    }

    public void resetTrimmedFrameCount() {
        this.trimmedFrameCount = 0;
    }

    public long getTrimmedFrameCount() {
        return this.trimmedFrameCount;
    }

    public boolean configure(int i, int i2, int i3) throws UnhandledFormatException {
        if (i3 == 2) {
            i3 = this.endBufferSize;
            if (i3 > 0) {
                this.trimmedFrameCount += (long) (i3 / this.bytesPerFrame);
            }
            this.channelCount = i2;
            this.sampleRateHz = i;
            this.bytesPerFrame = Util.getPcmFrameSize(2, i2);
            i = this.trimEndFrames;
            i2 = this.bytesPerFrame;
            this.endBuffer = new byte[(i * i2)];
            this.endBufferSize = 0;
            int i4 = this.trimStartFrames;
            this.pendingTrimStartBytes = i2 * i4;
            boolean z = this.isActive;
            boolean z2 = (i4 == 0 && i == 0) ? false : true;
            this.isActive = z2;
            this.receivedInputSinceConfigure = false;
            if (z != this.isActive) {
                return true;
            }
            return false;
        }
        throw new UnhandledFormatException(i, i2, i3);
    }

    public boolean isActive() {
        return this.isActive;
    }

    public int getOutputChannelCount() {
        return this.channelCount;
    }

    public int getOutputSampleRateHz() {
        return this.sampleRateHz;
    }

    public void queueInput(ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        int i = limit - position;
        if (i != 0) {
            this.receivedInputSinceConfigure = true;
            int min = Math.min(i, this.pendingTrimStartBytes);
            this.trimmedFrameCount += (long) (min / this.bytesPerFrame);
            this.pendingTrimStartBytes -= min;
            byteBuffer.position(position + min);
            if (this.pendingTrimStartBytes <= 0) {
                i -= min;
                position = (this.endBufferSize + i) - this.endBuffer.length;
                if (this.buffer.capacity() < position) {
                    this.buffer = ByteBuffer.allocateDirect(position).order(ByteOrder.nativeOrder());
                } else {
                    this.buffer.clear();
                }
                min = Util.constrainValue(position, 0, this.endBufferSize);
                this.buffer.put(this.endBuffer, 0, min);
                position = Util.constrainValue(position - min, 0, i);
                byteBuffer.limit(byteBuffer.position() + position);
                this.buffer.put(byteBuffer);
                byteBuffer.limit(limit);
                i -= position;
                this.endBufferSize -= min;
                byte[] bArr = this.endBuffer;
                System.arraycopy(bArr, min, bArr, 0, this.endBufferSize);
                byteBuffer.get(this.endBuffer, this.endBufferSize, i);
                this.endBufferSize += i;
                this.buffer.flip();
                this.outputBuffer = this.buffer;
            }
        }
    }

    public void queueEndOfStream() {
        this.inputEnded = true;
    }

    public ByteBuffer getOutput() {
        ByteBuffer byteBuffer = this.outputBuffer;
        if (this.inputEnded && this.endBufferSize > 0 && byteBuffer == AudioProcessor.EMPTY_BUFFER) {
            int capacity = this.buffer.capacity();
            int i = this.endBufferSize;
            if (capacity < i) {
                this.buffer = ByteBuffer.allocateDirect(i).order(ByteOrder.nativeOrder());
            } else {
                this.buffer.clear();
            }
            this.buffer.put(this.endBuffer, 0, this.endBufferSize);
            this.endBufferSize = 0;
            this.buffer.flip();
            byteBuffer = this.buffer;
        }
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        return byteBuffer;
    }

    public boolean isEnded() {
        return this.inputEnded && this.endBufferSize == 0 && this.outputBuffer == AudioProcessor.EMPTY_BUFFER;
    }

    public void flush() {
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        this.inputEnded = false;
        if (this.receivedInputSinceConfigure) {
            this.pendingTrimStartBytes = 0;
        }
        this.endBufferSize = 0;
    }

    public void reset() {
        flush();
        this.buffer = AudioProcessor.EMPTY_BUFFER;
        this.channelCount = -1;
        this.sampleRateHz = -1;
        this.endBuffer = Util.EMPTY_BYTE_ARRAY;
    }
}
