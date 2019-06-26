package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.Assertions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class ChannelMappingAudioProcessor implements AudioProcessor {
    private boolean active;
    private ByteBuffer buffer;
    private int channelCount = -1;
    private boolean inputEnded;
    private ByteBuffer outputBuffer;
    private int[] outputChannels;
    private int[] pendingOutputChannels;
    private int sampleRateHz = -1;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:31:0x0056 in {3, 11, 14, 15, 23, 24, 25, 27, 28, 30} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public boolean configure(int r6, int r7, int r8) throws com.google.android.exoplayer2.audio.AudioProcessor.UnhandledFormatException {
        /*
        r5 = this;
        r0 = r5.pendingOutputChannels;
        r1 = r5.outputChannels;
        r0 = java.util.Arrays.equals(r0, r1);
        r1 = 1;
        r0 = r0 ^ r1;
        r2 = r5.pendingOutputChannels;
        r5.outputChannels = r2;
        r2 = r5.outputChannels;
        r3 = 0;
        if (r2 != 0) goto L_0x0016;
        r5.active = r3;
        return r0;
        r2 = 2;
        if (r8 != r2) goto L_0x0050;
        if (r0 != 0) goto L_0x0024;
        r0 = r5.sampleRateHz;
        if (r0 != r6) goto L_0x0024;
        r0 = r5.channelCount;
        if (r0 != r7) goto L_0x0024;
        return r3;
        r5.sampleRateHz = r6;
        r5.channelCount = r7;
        r0 = r5.outputChannels;
        r0 = r0.length;
        if (r7 == r0) goto L_0x002f;
        r0 = 1;
        goto L_0x0030;
        r0 = 0;
        r5.active = r0;
        r0 = 0;
        r2 = r5.outputChannels;
        r4 = r2.length;
        if (r0 >= r4) goto L_0x004f;
        r2 = r2[r0];
        if (r2 >= r7) goto L_0x0049;
        r4 = r5.active;
        if (r2 == r0) goto L_0x0042;
        r2 = 1;
        goto L_0x0043;
        r2 = 0;
        r2 = r2 | r4;
        r5.active = r2;
        r0 = r0 + 1;
        goto L_0x0033;
        r0 = new com.google.android.exoplayer2.audio.AudioProcessor$UnhandledFormatException;
        r0.<init>(r6, r7, r8);
        throw r0;
        return r1;
        r0 = new com.google.android.exoplayer2.audio.AudioProcessor$UnhandledFormatException;
        r0.<init>(r6, r7, r8);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.audio.ChannelMappingAudioProcessor.configure(int, int, int):boolean");
    }

    public int getOutputEncoding() {
        return 2;
    }

    public ChannelMappingAudioProcessor() {
        ByteBuffer byteBuffer = AudioProcessor.EMPTY_BUFFER;
        this.buffer = byteBuffer;
        this.outputBuffer = byteBuffer;
    }

    public void setChannelMap(int[] iArr) {
        this.pendingOutputChannels = iArr;
    }

    public boolean isActive() {
        return this.active;
    }

    public int getOutputChannelCount() {
        int[] iArr = this.outputChannels;
        return iArr == null ? this.channelCount : iArr.length;
    }

    public int getOutputSampleRateHz() {
        return this.sampleRateHz;
    }

    public void queueInput(ByteBuffer byteBuffer) {
        Assertions.checkState(this.outputChannels != null);
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        int length = (((limit - position) / (this.channelCount * 2)) * this.outputChannels.length) * 2;
        if (this.buffer.capacity() < length) {
            this.buffer = ByteBuffer.allocateDirect(length).order(ByteOrder.nativeOrder());
        } else {
            this.buffer.clear();
        }
        while (position < limit) {
            for (int i : this.outputChannels) {
                this.buffer.putShort(byteBuffer.getShort((i * 2) + position));
            }
            position += this.channelCount * 2;
        }
        byteBuffer.position(limit);
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
        this.buffer = AudioProcessor.EMPTY_BUFFER;
        this.channelCount = -1;
        this.sampleRateHz = -1;
        this.outputChannels = null;
        this.pendingOutputChannels = null;
        this.active = false;
    }
}
