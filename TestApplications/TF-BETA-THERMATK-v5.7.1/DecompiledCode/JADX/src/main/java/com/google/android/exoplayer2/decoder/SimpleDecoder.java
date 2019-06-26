package com.google.android.exoplayer2.decoder;

import com.google.android.exoplayer2.util.Assertions;
import java.util.ArrayDeque;

public abstract class SimpleDecoder<I extends DecoderInputBuffer, O extends OutputBuffer, E extends Exception> implements Decoder<I, O, E> {
    private int availableInputBufferCount;
    private final I[] availableInputBuffers;
    private int availableOutputBufferCount;
    private final O[] availableOutputBuffers;
    private final Thread decodeThread;
    private I dequeuedInputBuffer;
    private E exception;
    private boolean flushed;
    private final Object lock = new Object();
    private final ArrayDeque<I> queuedInputBuffers = new ArrayDeque();
    private final ArrayDeque<O> queuedOutputBuffers = new ArrayDeque();
    private boolean released;
    private int skippedOutputBufferCount;

    /* renamed from: com.google.android.exoplayer2.decoder.SimpleDecoder$1 */
    class C01541 extends Thread {
        C01541() {
        }

        public void run() {
            SimpleDecoder.this.run();
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:57:0x0099 in {7, 11, 16, 19, 22, 24, 26, 33, 36, 42, 45, 46, 49, 52, 56} preds:[]
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
    private boolean decode() throws java.lang.InterruptedException {
        /*
        r6 = this;
        r0 = r6.lock;
        monitor-enter(r0);
        r1 = r6.released;	 Catch:{ all -> 0x0096 }
        if (r1 != 0) goto L_0x0013;	 Catch:{ all -> 0x0096 }
        r1 = r6.canDecodeBuffer();	 Catch:{ all -> 0x0096 }
        if (r1 != 0) goto L_0x0013;	 Catch:{ all -> 0x0096 }
        r1 = r6.lock;	 Catch:{ all -> 0x0096 }
        r1.wait();	 Catch:{ all -> 0x0096 }
        goto L_0x0003;	 Catch:{ all -> 0x0096 }
        r1 = r6.released;	 Catch:{ all -> 0x0096 }
        r2 = 0;	 Catch:{ all -> 0x0096 }
        if (r1 == 0) goto L_0x001a;	 Catch:{ all -> 0x0096 }
        monitor-exit(r0);	 Catch:{ all -> 0x0096 }
        return r2;	 Catch:{ all -> 0x0096 }
        r1 = r6.queuedInputBuffers;	 Catch:{ all -> 0x0096 }
        r1 = r1.removeFirst();	 Catch:{ all -> 0x0096 }
        r1 = (com.google.android.exoplayer2.decoder.DecoderInputBuffer) r1;	 Catch:{ all -> 0x0096 }
        r3 = r6.availableOutputBuffers;	 Catch:{ all -> 0x0096 }
        r4 = r6.availableOutputBufferCount;	 Catch:{ all -> 0x0096 }
        r5 = 1;	 Catch:{ all -> 0x0096 }
        r4 = r4 - r5;	 Catch:{ all -> 0x0096 }
        r6.availableOutputBufferCount = r4;	 Catch:{ all -> 0x0096 }
        r3 = r3[r4];	 Catch:{ all -> 0x0096 }
        r4 = r6.flushed;	 Catch:{ all -> 0x0096 }
        r6.flushed = r2;	 Catch:{ all -> 0x0096 }
        monitor-exit(r0);	 Catch:{ all -> 0x0096 }
        r0 = r1.isEndOfStream();
        if (r0 == 0) goto L_0x003c;
        r0 = 4;
        r3.addFlag(r0);
        goto L_0x0069;
        r0 = r1.isDecodeOnly();
        if (r0 == 0) goto L_0x0047;
        r0 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r3.addFlag(r0);
        r0 = r6.decode(r1, r3, r4);	 Catch:{ RuntimeException -> 0x0056, OutOfMemoryError -> 0x004e }
        r6.exception = r0;	 Catch:{ RuntimeException -> 0x0056, OutOfMemoryError -> 0x004e }
        goto L_0x005d;
        r0 = move-exception;
        r0 = r6.createUnexpectedDecodeException(r0);
        r6.exception = r0;
        goto L_0x005d;
        r0 = move-exception;
        r0 = r6.createUnexpectedDecodeException(r0);
        r6.exception = r0;
        r0 = r6.exception;
        if (r0 == 0) goto L_0x0069;
        r0 = r6.lock;
        monitor-enter(r0);
        monitor-exit(r0);	 Catch:{ all -> 0x0066 }
        return r2;	 Catch:{ all -> 0x0066 }
        r1 = move-exception;	 Catch:{ all -> 0x0066 }
        monitor-exit(r0);	 Catch:{ all -> 0x0066 }
        throw r1;
        r4 = r6.lock;
        monitor-enter(r4);
        r0 = r6.flushed;	 Catch:{ all -> 0x0093 }
        if (r0 == 0) goto L_0x0074;	 Catch:{ all -> 0x0093 }
        r3.release();	 Catch:{ all -> 0x0093 }
        goto L_0x008e;	 Catch:{ all -> 0x0093 }
        r0 = r3.isDecodeOnly();	 Catch:{ all -> 0x0093 }
        if (r0 == 0) goto L_0x0083;	 Catch:{ all -> 0x0093 }
        r0 = r6.skippedOutputBufferCount;	 Catch:{ all -> 0x0093 }
        r0 = r0 + r5;	 Catch:{ all -> 0x0093 }
        r6.skippedOutputBufferCount = r0;	 Catch:{ all -> 0x0093 }
        r3.release();	 Catch:{ all -> 0x0093 }
        goto L_0x008e;	 Catch:{ all -> 0x0093 }
        r0 = r6.skippedOutputBufferCount;	 Catch:{ all -> 0x0093 }
        r3.skippedOutputBufferCount = r0;	 Catch:{ all -> 0x0093 }
        r6.skippedOutputBufferCount = r2;	 Catch:{ all -> 0x0093 }
        r0 = r6.queuedOutputBuffers;	 Catch:{ all -> 0x0093 }
        r0.addLast(r3);	 Catch:{ all -> 0x0093 }
        r6.releaseInputBufferInternal(r1);	 Catch:{ all -> 0x0093 }
        monitor-exit(r4);	 Catch:{ all -> 0x0093 }
        return r5;	 Catch:{ all -> 0x0093 }
        r0 = move-exception;	 Catch:{ all -> 0x0093 }
        monitor-exit(r4);	 Catch:{ all -> 0x0093 }
        throw r0;
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0096 }
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.decoder.SimpleDecoder.decode():boolean");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:8:0x000f in {3, 4, 7} preds:[]
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
    private void run() {
        /*
        r2 = this;
        r0 = r2.decode();	 Catch:{ InterruptedException -> 0x0008 }
        if (r0 == 0) goto L_0x0007;
        goto L_0x0000;
        return;
        r0 = move-exception;
        r1 = new java.lang.IllegalStateException;
        r1.<init>(r0);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.decoder.SimpleDecoder.run():void");
    }

    public abstract I createInputBuffer();

    public abstract O createOutputBuffer();

    public abstract E createUnexpectedDecodeException(Throwable th);

    public abstract E decode(I i, O o, boolean z);

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:18:0x0042 in {6, 9, 12, 14, 17} preds:[]
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
    public final void flush() {
        /*
        r2 = this;
        r0 = r2.lock;
        monitor-enter(r0);
        r1 = 1;
        r2.flushed = r1;	 Catch:{ all -> 0x003f }
        r1 = 0;	 Catch:{ all -> 0x003f }
        r2.skippedOutputBufferCount = r1;	 Catch:{ all -> 0x003f }
        r1 = r2.dequeuedInputBuffer;	 Catch:{ all -> 0x003f }
        if (r1 == 0) goto L_0x0015;	 Catch:{ all -> 0x003f }
        r1 = r2.dequeuedInputBuffer;	 Catch:{ all -> 0x003f }
        r2.releaseInputBufferInternal(r1);	 Catch:{ all -> 0x003f }
        r1 = 0;	 Catch:{ all -> 0x003f }
        r2.dequeuedInputBuffer = r1;	 Catch:{ all -> 0x003f }
        r1 = r2.queuedInputBuffers;	 Catch:{ all -> 0x003f }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x003f }
        if (r1 != 0) goto L_0x0029;	 Catch:{ all -> 0x003f }
        r1 = r2.queuedInputBuffers;	 Catch:{ all -> 0x003f }
        r1 = r1.removeFirst();	 Catch:{ all -> 0x003f }
        r1 = (com.google.android.exoplayer2.decoder.DecoderInputBuffer) r1;	 Catch:{ all -> 0x003f }
        r2.releaseInputBufferInternal(r1);	 Catch:{ all -> 0x003f }
        goto L_0x0015;	 Catch:{ all -> 0x003f }
        r1 = r2.queuedOutputBuffers;	 Catch:{ all -> 0x003f }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x003f }
        if (r1 != 0) goto L_0x003d;	 Catch:{ all -> 0x003f }
        r1 = r2.queuedOutputBuffers;	 Catch:{ all -> 0x003f }
        r1 = r1.removeFirst();	 Catch:{ all -> 0x003f }
        r1 = (com.google.android.exoplayer2.decoder.OutputBuffer) r1;	 Catch:{ all -> 0x003f }
        r1.release();	 Catch:{ all -> 0x003f }
        goto L_0x0029;	 Catch:{ all -> 0x003f }
        monitor-exit(r0);	 Catch:{ all -> 0x003f }
        return;	 Catch:{ all -> 0x003f }
        r1 = move-exception;	 Catch:{ all -> 0x003f }
        monitor-exit(r0);	 Catch:{ all -> 0x003f }
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.decoder.SimpleDecoder.flush():void");
    }

    protected SimpleDecoder(I[] iArr, O[] oArr) {
        this.availableInputBuffers = iArr;
        this.availableInputBufferCount = iArr.length;
        for (int i = 0; i < this.availableInputBufferCount; i++) {
            this.availableInputBuffers[i] = createInputBuffer();
        }
        this.availableOutputBuffers = oArr;
        this.availableOutputBufferCount = oArr.length;
        for (int i2 = 0; i2 < this.availableOutputBufferCount; i2++) {
            this.availableOutputBuffers[i2] = createOutputBuffer();
        }
        this.decodeThread = new C01541();
        this.decodeThread.start();
    }

    /* Access modifiers changed, original: protected|final */
    public final void setInitialInputBufferSize(int i) {
        Assertions.checkState(this.availableInputBufferCount == this.availableInputBuffers.length);
        for (DecoderInputBuffer ensureSpaceForWrite : this.availableInputBuffers) {
            ensureSpaceForWrite.ensureSpaceForWrite(i);
        }
    }

    public final I dequeueInputBuffer() throws Exception {
        DecoderInputBuffer decoderInputBuffer;
        synchronized (this.lock) {
            maybeThrowException();
            Assertions.checkState(this.dequeuedInputBuffer == null);
            if (this.availableInputBufferCount == 0) {
                decoderInputBuffer = null;
            } else {
                DecoderInputBuffer[] decoderInputBufferArr = this.availableInputBuffers;
                int i = this.availableInputBufferCount - 1;
                this.availableInputBufferCount = i;
                decoderInputBuffer = decoderInputBufferArr[i];
            }
            this.dequeuedInputBuffer = decoderInputBuffer;
            decoderInputBuffer = this.dequeuedInputBuffer;
        }
        return decoderInputBuffer;
    }

    public final void queueInputBuffer(I i) throws Exception {
        synchronized (this.lock) {
            maybeThrowException();
            Assertions.checkArgument(i == this.dequeuedInputBuffer);
            this.queuedInputBuffers.addLast(i);
            maybeNotifyDecodeLoop();
            this.dequeuedInputBuffer = null;
        }
    }

    public final O dequeueOutputBuffer() throws Exception {
        synchronized (this.lock) {
            maybeThrowException();
            if (this.queuedOutputBuffers.isEmpty()) {
                return null;
            }
            OutputBuffer outputBuffer = (OutputBuffer) this.queuedOutputBuffers.removeFirst();
            return outputBuffer;
        }
    }

    /* Access modifiers changed, original: protected */
    public void releaseOutputBuffer(O o) {
        synchronized (this.lock) {
            releaseOutputBufferInternal(o);
            maybeNotifyDecodeLoop();
        }
    }

    public void release() {
        synchronized (this.lock) {
            this.released = true;
            this.lock.notify();
        }
        try {
            this.decodeThread.join();
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
        }
    }

    private void maybeThrowException() throws Exception {
        Exception exception = this.exception;
        if (exception != null) {
            throw exception;
        }
    }

    private void maybeNotifyDecodeLoop() {
        if (canDecodeBuffer()) {
            this.lock.notify();
        }
    }

    private boolean canDecodeBuffer() {
        return !this.queuedInputBuffers.isEmpty() && this.availableOutputBufferCount > 0;
    }

    private void releaseInputBufferInternal(I i) {
        i.clear();
        DecoderInputBuffer[] decoderInputBufferArr = this.availableInputBuffers;
        int i2 = this.availableInputBufferCount;
        this.availableInputBufferCount = i2 + 1;
        decoderInputBufferArr[i2] = i;
    }

    private void releaseOutputBufferInternal(O o) {
        o.clear();
        OutputBuffer[] outputBufferArr = this.availableOutputBuffers;
        int i = this.availableOutputBufferCount;
        this.availableOutputBufferCount = i + 1;
        outputBufferArr[i] = o;
    }
}
