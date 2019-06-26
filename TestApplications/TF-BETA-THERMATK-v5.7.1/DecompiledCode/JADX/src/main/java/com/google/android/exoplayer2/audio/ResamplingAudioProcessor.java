package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.audio.AudioProcessor.UnhandledFormatException;
import java.nio.ByteBuffer;

final class ResamplingAudioProcessor implements AudioProcessor {
    private ByteBuffer buffer;
    private int channelCount = -1;
    private int encoding = 0;
    private boolean inputEnded;
    private ByteBuffer outputBuffer;
    private int sampleRateHz = -1;

    public int getOutputEncoding() {
        return 2;
    }

    public ResamplingAudioProcessor() {
        ByteBuffer byteBuffer = AudioProcessor.EMPTY_BUFFER;
        this.buffer = byteBuffer;
        this.outputBuffer = byteBuffer;
    }

    public boolean configure(int i, int i2, int i3) throws UnhandledFormatException {
        if (i3 != 3 && i3 != 2 && i3 != Integer.MIN_VALUE && i3 != 1073741824) {
            throw new UnhandledFormatException(i, i2, i3);
        } else if (this.sampleRateHz == i && this.channelCount == i2 && this.encoding == i3) {
            return false;
        } else {
            this.sampleRateHz = i;
            this.channelCount = i2;
            this.encoding = i3;
            return true;
        }
    }

    public boolean isActive() {
        int i = this.encoding;
        return (i == 0 || i == 2) ? false : true;
    }

    public int getOutputChannelCount() {
        return this.channelCount;
    }

    public int getOutputSampleRateHz() {
        return this.sampleRateHz;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0081 A:{LOOP_START, PHI: r0 , LOOP:2: B:23:0x0081->B:24:0x0083} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0043  */
    public void queueInput(java.nio.ByteBuffer r8) {
        /*
        r7 = this;
        r0 = r8.position();
        r1 = r8.limit();
        r2 = r1 - r0;
        r3 = r7.encoding;
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r6 = 3;
        if (r3 == r5) goto L_0x0020;
    L_0x0013:
        if (r3 == r6) goto L_0x0021;
    L_0x0015:
        if (r3 != r4) goto L_0x001a;
    L_0x0017:
        r2 = r2 / 2;
        goto L_0x0023;
    L_0x001a:
        r8 = new java.lang.IllegalStateException;
        r8.<init>();
        throw r8;
    L_0x0020:
        r2 = r2 / r6;
    L_0x0021:
        r2 = r2 * 2;
    L_0x0023:
        r3 = r7.buffer;
        r3 = r3.capacity();
        if (r3 >= r2) goto L_0x003a;
    L_0x002b:
        r2 = java.nio.ByteBuffer.allocateDirect(r2);
        r3 = java.nio.ByteOrder.nativeOrder();
        r2 = r2.order(r3);
        r7.buffer = r2;
        goto L_0x003f;
    L_0x003a:
        r2 = r7.buffer;
        r2.clear();
    L_0x003f:
        r2 = r7.encoding;
        if (r2 == r5) goto L_0x0081;
    L_0x0043:
        if (r2 == r6) goto L_0x0068;
    L_0x0045:
        if (r2 != r4) goto L_0x0062;
    L_0x0047:
        if (r0 >= r1) goto L_0x009c;
    L_0x0049:
        r2 = r7.buffer;
        r3 = r0 + 2;
        r3 = r8.get(r3);
        r2.put(r3);
        r2 = r7.buffer;
        r3 = r0 + 3;
        r3 = r8.get(r3);
        r2.put(r3);
        r0 = r0 + 4;
        goto L_0x0047;
    L_0x0062:
        r8 = new java.lang.IllegalStateException;
        r8.<init>();
        throw r8;
    L_0x0068:
        if (r0 >= r1) goto L_0x009c;
    L_0x006a:
        r2 = r7.buffer;
        r3 = 0;
        r2.put(r3);
        r2 = r7.buffer;
        r3 = r8.get(r0);
        r3 = r3 & 255;
        r3 = r3 + -128;
        r3 = (byte) r3;
        r2.put(r3);
        r0 = r0 + 1;
        goto L_0x0068;
    L_0x0081:
        if (r0 >= r1) goto L_0x009c;
    L_0x0083:
        r2 = r7.buffer;
        r3 = r0 + 1;
        r3 = r8.get(r3);
        r2.put(r3);
        r2 = r7.buffer;
        r3 = r0 + 2;
        r3 = r8.get(r3);
        r2.put(r3);
        r0 = r0 + 3;
        goto L_0x0081;
    L_0x009c:
        r0 = r8.limit();
        r8.position(r0);
        r8 = r7.buffer;
        r8.flip();
        r8 = r7.buffer;
        r7.outputBuffer = r8;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.audio.ResamplingAudioProcessor.queueInput(java.nio.ByteBuffer):void");
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
        this.encoding = 0;
        this.buffer = AudioProcessor.EMPTY_BUFFER;
    }
}
