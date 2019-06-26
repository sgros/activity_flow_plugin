package com.google.android.exoplayer2.util;

import java.io.IOException;
import java.util.Collections;
import java.util.PriorityQueue;

public final class PriorityTaskManager {
    private int highestPriority = Integer.MIN_VALUE;
    private final Object lock = new Object();
    private final PriorityQueue<Integer> queue = new PriorityQueue(10, Collections.reverseOrder());

    public static class PriorityTooLowException extends IOException {
        public PriorityTooLowException(int i, int i2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Priority too low [priority=");
            stringBuilder.append(i);
            stringBuilder.append(", highest=");
            stringBuilder.append(i2);
            stringBuilder.append("]");
            super(stringBuilder.toString());
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:11:0x0012 in {5, 7, 10} preds:[]
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
    public void proceed(int r3) throws java.lang.InterruptedException {
        /*
        r2 = this;
        r0 = r2.lock;
        monitor-enter(r0);
        r1 = r2.highestPriority;	 Catch:{ all -> 0x000f }
        if (r1 == r3) goto L_0x000d;	 Catch:{ all -> 0x000f }
        r1 = r2.lock;	 Catch:{ all -> 0x000f }
        r1.wait();	 Catch:{ all -> 0x000f }
        goto L_0x0003;	 Catch:{ all -> 0x000f }
        monitor-exit(r0);	 Catch:{ all -> 0x000f }
        return;	 Catch:{ all -> 0x000f }
        r3 = move-exception;	 Catch:{ all -> 0x000f }
        monitor-exit(r0);	 Catch:{ all -> 0x000f }
        throw r3;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.PriorityTaskManager.proceed(int):void");
    }

    public void add(int i) {
        synchronized (this.lock) {
            this.queue.add(Integer.valueOf(i));
            this.highestPriority = Math.max(this.highestPriority, i);
        }
    }

    public boolean proceedNonBlocking(int i) {
        boolean z;
        synchronized (this.lock) {
            z = this.highestPriority == i;
        }
        return z;
    }

    public void proceedOrThrow(int i) throws PriorityTooLowException {
        synchronized (this.lock) {
            if (this.highestPriority == i) {
            } else {
                throw new PriorityTooLowException(i, this.highestPriority);
            }
        }
    }

    public void remove(int i) {
        synchronized (this.lock) {
            this.queue.remove(Integer.valueOf(i));
            if (this.queue.isEmpty()) {
                i = Integer.MIN_VALUE;
            } else {
                Object peek = this.queue.peek();
                Util.castNonNull(peek);
                i = ((Integer) peek).intValue();
            }
            this.highestPriority = i;
            this.lock.notifyAll();
        }
    }
}
