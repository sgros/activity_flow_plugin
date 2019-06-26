package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.audio.AudioProcessor.UnhandledFormatException;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class FloatResamplingAudioProcessor implements AudioProcessor {
    private static final int FLOAT_NAN_AS_INT = Float.floatToIntBits(Float.NaN);
    private ByteBuffer buffer;
    private int channelCount = -1;
    private boolean inputEnded;
    private ByteBuffer outputBuffer;
    private int sampleRateHz = -1;
    private int sourceEncoding = 0;

    public int getOutputEncoding() {
        return 4;
    }

    public FloatResamplingAudioProcessor() {
        ByteBuffer byteBuffer = AudioProcessor.EMPTY_BUFFER;
        this.buffer = byteBuffer;
        this.outputBuffer = byteBuffer;
    }

    public boolean configure(int i, int i2, int i3) throws UnhandledFormatException {
        if (!Util.isEncodingHighResolutionIntegerPcm(i3)) {
            throw new UnhandledFormatException(i, i2, i3);
        } else if (this.sampleRateHz == i && this.channelCount == i2 && this.sourceEncoding == i3) {
            return false;
        } else {
            this.sampleRateHz = i;
            this.channelCount = i2;
            this.sourceEncoding = i3;
            return true;
        }
    }

    public boolean isActive() {
        return Util.isEncodingHighResolutionIntegerPcm(this.sourceEncoding);
    }

    public int getOutputChannelCount() {
        return this.channelCount;
    }

    public int getOutputSampleRateHz() {
        return this.sampleRateHz;
    }

    public void queueInput(ByteBuffer byteBuffer) {
        Object obj = this.sourceEncoding == 1073741824 ? 1 : null;
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        int i = limit - position;
        if (obj == null) {
            i = (i / 3) * 4;
        }
        if (this.buffer.capacity() < i) {
            this.buffer = ByteBuffer.allocateDirect(i).order(ByteOrder.nativeOrder());
        } else {
            this.buffer.clear();
        }
        if (obj != null) {
            while (position < limit) {
                writePcm32BitFloat((((byteBuffer.get(position) & NalUnitUtil.EXTENDED_SAR) | ((byteBuffer.get(position + 1) & NalUnitUtil.EXTENDED_SAR) << 8)) | ((byteBuffer.get(position + 2) & NalUnitUtil.EXTENDED_SAR) << 16)) | ((byteBuffer.get(position + 3) & NalUnitUtil.EXTENDED_SAR) << 24), this.buffer);
                position += 4;
            }
        } else {
            while (position < limit) {
                writePcm32BitFloat((((byteBuffer.get(position) & NalUnitUtil.EXTENDED_SAR) << 8) | ((byteBuffer.get(position + 1) & NalUnitUtil.EXTENDED_SAR) << 16)) | ((byteBuffer.get(position + 2) & NalUnitUtil.EXTENDED_SAR) << 24), this.buffer);
                position += 3;
            }
        }
        byteBuffer.position(byteBuffer.limit());
        this.buffer.flip();
        this.outputBuffer = this.buffer;
    }

    public void queueEndOfStream() {
        this.inputEnded = true;
    }

    public ByteBuffer getOutput() {
        ByteBuffer byteBuffer = this.outputBuffer;
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        return byteBuffer;
    }

    public boolean isEnded() {
        return this.inputEnded && this.outputBuffer == AudioProcessor.EMPTY_BUFFER;
    }

    public void flush() {
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        this.inputEnded = false;
    }

    public void reset() {
        flush();
        this.sampleRateHz = -1;
        this.channelCount = -1;
        this.sourceEncoding = 0;
        this.buffer = AudioProcessor.EMPTY_BUFFER;
    }

    private static void writePcm32BitFloat(int i, ByteBuffer byteBuffer) {
        double d = (double) i;
        Double.isNaN(d);
        i = Float.floatToIntBits((float) (d * 4.656612875245797E-10d));
        if (i == FLOAT_NAN_AS_INT) {
            i = Float.floatToIntBits(0.0f);
        }
        byteBuffer.putInt(i);
    }
}
