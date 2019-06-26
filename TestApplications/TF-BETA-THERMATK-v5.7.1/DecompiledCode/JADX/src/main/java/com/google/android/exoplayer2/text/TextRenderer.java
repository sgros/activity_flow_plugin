package com.google.android.exoplayer2.text;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.util.Collections;
import java.util.List;

public final class TextRenderer extends BaseRenderer implements Callback {
    private SubtitleDecoder decoder;
    private final SubtitleDecoderFactory decoderFactory;
    private int decoderReplacementState;
    private final FormatHolder formatHolder;
    private boolean inputStreamEnded;
    private SubtitleInputBuffer nextInputBuffer;
    private SubtitleOutputBuffer nextSubtitle;
    private int nextSubtitleEventIndex;
    private final TextOutput output;
    private final Handler outputHandler;
    private boolean outputStreamEnded;
    private Format streamFormat;
    private SubtitleOutputBuffer subtitle;

    public boolean isReady() {
        return true;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:70:0x0106 in {2, 8, 11, 14, 20, 21, 31, 32, 37, 38, 40, 43, 51, 55, 60, 61, 63, 65, 66, 69} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public void render(long r9, long r11) throws com.google.android.exoplayer2.ExoPlaybackException {
        /*
        r8 = this;
        r11 = r8.outputStreamEnded;
        if (r11 == 0) goto L_0x0005;
        return;
        r11 = r8.nextSubtitle;
        if (r11 != 0) goto L_0x0023;
        r11 = r8.decoder;
        r11.setPositionUs(r9);
        r11 = r8.decoder;	 Catch:{ SubtitleDecoderException -> 0x0019 }
        r11 = r11.dequeueOutputBuffer();	 Catch:{ SubtitleDecoderException -> 0x0019 }
        r11 = (com.google.android.exoplayer2.text.SubtitleOutputBuffer) r11;	 Catch:{ SubtitleDecoderException -> 0x0019 }
        r8.nextSubtitle = r11;	 Catch:{ SubtitleDecoderException -> 0x0019 }
        goto L_0x0023;
        r9 = move-exception;
        r10 = r8.getIndex();
        r9 = com.google.android.exoplayer2.ExoPlaybackException.createForRenderer(r9, r10);
        throw r9;
        r11 = r8.getState();
        r12 = 2;
        if (r11 == r12) goto L_0x002b;
        return;
        r11 = r8.subtitle;
        r0 = 0;
        r1 = 1;
        if (r11 == 0) goto L_0x0045;
        r2 = r8.getNextEventTime();
        r11 = 0;
        r4 = (r2 > r9 ? 1 : (r2 == r9 ? 0 : -1));
        if (r4 > 0) goto L_0x0046;
        r11 = r8.nextSubtitleEventIndex;
        r11 = r11 + r1;
        r8.nextSubtitleEventIndex = r11;
        r2 = r8.getNextEventTime();
        r11 = 1;
        goto L_0x0036;
        r11 = 0;
        r2 = r8.nextSubtitle;
        r3 = 0;
        if (r2 == 0) goto L_0x008c;
        r2 = r2.isEndOfStream();
        if (r2 == 0) goto L_0x006e;
        if (r11 != 0) goto L_0x008c;
        r4 = r8.getNextEventTime();
        r6 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r2 != 0) goto L_0x008c;
        r2 = r8.decoderReplacementState;
        if (r2 != r12) goto L_0x0068;
        r8.replaceDecoder();
        goto L_0x008c;
        r8.releaseBuffers();
        r8.outputStreamEnded = r1;
        goto L_0x008c;
        r2 = r8.nextSubtitle;
        r4 = r2.timeUs;
        r2 = (r4 > r9 ? 1 : (r4 == r9 ? 0 : -1));
        if (r2 > 0) goto L_0x008c;
        r11 = r8.subtitle;
        if (r11 == 0) goto L_0x007d;
        r11.release();
        r11 = r8.nextSubtitle;
        r8.subtitle = r11;
        r8.nextSubtitle = r3;
        r11 = r8.subtitle;
        r11 = r11.getNextEventTimeIndex(r9);
        r8.nextSubtitleEventIndex = r11;
        r11 = 1;
        if (r11 == 0) goto L_0x0097;
        r11 = r8.subtitle;
        r9 = r11.getCues(r9);
        r8.updateOutput(r9);
        r9 = r8.decoderReplacementState;
        if (r9 != r12) goto L_0x009c;
        return;
        r9 = r8.inputStreamEnded;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        if (r9 != 0) goto L_0x00fb;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r8.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        if (r9 != 0) goto L_0x00b3;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r8.decoder;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r9.dequeueInputBuffer();	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = (com.google.android.exoplayer2.text.SubtitleInputBuffer) r9;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r8.nextInputBuffer = r9;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r8.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        if (r9 != 0) goto L_0x00b3;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        return;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r8.decoderReplacementState;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        if (r9 != r1) goto L_0x00c9;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r8.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r10 = 4;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9.setFlags(r10);	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r8.decoder;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r10 = r8.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9.queueInputBuffer(r10);	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r8.nextInputBuffer = r3;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r8.decoderReplacementState = r12;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        return;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r8.formatHolder;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r10 = r8.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r8.readSource(r9, r10, r0);	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r10 = -4;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        if (r9 != r10) goto L_0x00f8;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r8.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r9.isEndOfStream();	 Catch:{ SubtitleDecoderException -> 0x00fc }
        if (r9 == 0) goto L_0x00df;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r8.inputStreamEnded = r1;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        goto L_0x00ee;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r8.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r10 = r8.formatHolder;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r10 = r10.format;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r10 = r10.subsampleOffsetUs;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9.subsampleOffsetUs = r10;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r8.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9.flip();	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9 = r8.decoder;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r10 = r8.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r9.queueInputBuffer(r10);	 Catch:{ SubtitleDecoderException -> 0x00fc }
        r8.nextInputBuffer = r3;	 Catch:{ SubtitleDecoderException -> 0x00fc }
        goto L_0x009c;
        r10 = -3;
        if (r9 != r10) goto L_0x009c;
        return;
        r9 = move-exception;
        r10 = r8.getIndex();
        r9 = com.google.android.exoplayer2.ExoPlaybackException.createForRenderer(r9, r10);
        throw r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.TextRenderer.render(long, long):void");
    }

    public TextRenderer(TextOutput textOutput, Looper looper) {
        this(textOutput, looper, SubtitleDecoderFactory.DEFAULT);
    }

    public TextRenderer(TextOutput textOutput, Looper looper, SubtitleDecoderFactory subtitleDecoderFactory) {
        Handler handler;
        super(3);
        Assertions.checkNotNull(textOutput);
        this.output = textOutput;
        if (looper == null) {
            handler = null;
        } else {
            handler = Util.createHandler(looper, this);
        }
        this.outputHandler = handler;
        this.decoderFactory = subtitleDecoderFactory;
        this.formatHolder = new FormatHolder();
    }

    public int supportsFormat(Format format) {
        if (!this.decoderFactory.supportsFormat(format)) {
            return MimeTypes.isText(format.sampleMimeType) ? 1 : 0;
        } else {
            return BaseRenderer.supportsFormatDrm(null, format.drmInitData) ? 4 : 2;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onStreamChanged(Format[] formatArr, long j) throws ExoPlaybackException {
        this.streamFormat = formatArr[0];
        if (this.decoder != null) {
            this.decoderReplacementState = 1;
        } else {
            this.decoder = this.decoderFactory.createDecoder(this.streamFormat);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onPositionReset(long j, boolean z) {
        clearOutput();
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        if (this.decoderReplacementState != 0) {
            replaceDecoder();
            return;
        }
        releaseBuffers();
        this.decoder.flush();
    }

    /* Access modifiers changed, original: protected */
    public void onDisabled() {
        this.streamFormat = null;
        clearOutput();
        releaseDecoder();
    }

    public boolean isEnded() {
        return this.outputStreamEnded;
    }

    private void releaseBuffers() {
        this.nextInputBuffer = null;
        this.nextSubtitleEventIndex = -1;
        SubtitleOutputBuffer subtitleOutputBuffer = this.subtitle;
        if (subtitleOutputBuffer != null) {
            subtitleOutputBuffer.release();
            this.subtitle = null;
        }
        subtitleOutputBuffer = this.nextSubtitle;
        if (subtitleOutputBuffer != null) {
            subtitleOutputBuffer.release();
            this.nextSubtitle = null;
        }
    }

    private void releaseDecoder() {
        releaseBuffers();
        this.decoder.release();
        this.decoder = null;
        this.decoderReplacementState = 0;
    }

    private void replaceDecoder() {
        releaseDecoder();
        this.decoder = this.decoderFactory.createDecoder(this.streamFormat);
    }

    private long getNextEventTime() {
        int i = this.nextSubtitleEventIndex;
        return (i == -1 || i >= this.subtitle.getEventTimeCount()) ? TimestampAdjuster.DO_NOT_OFFSET : this.subtitle.getEventTime(this.nextSubtitleEventIndex);
    }

    private void updateOutput(List<Cue> list) {
        Handler handler = this.outputHandler;
        if (handler != null) {
            handler.obtainMessage(0, list).sendToTarget();
        } else {
            invokeUpdateOutputInternal(list);
        }
    }

    private void clearOutput() {
        updateOutput(Collections.emptyList());
    }

    public boolean handleMessage(Message message) {
        if (message.what == 0) {
            invokeUpdateOutputInternal((List) message.obj);
            return true;
        }
        throw new IllegalStateException();
    }

    private void invokeUpdateOutputInternal(List<Cue> list) {
        this.output.onCues(list);
    }
}
