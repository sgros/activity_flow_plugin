package com.google.android.exoplayer2;

import android.os.Handler;
import com.google.android.exoplayer2.util.Assertions;

public final class PlayerMessage {
    private boolean deleteAfterDelivery = true;
    private Handler handler;
    private boolean isCanceled;
    private boolean isDelivered;
    private boolean isProcessed;
    private boolean isSent;
    private Object payload;
    private long positionMs = -9223372036854775807L;
    private final Sender sender;
    private final Target target;
    private final Timeline timeline;
    private int type;
    private int windowIndex;

    public interface Sender {
        void sendMessage(PlayerMessage playerMessage);
    }

    public interface Target {
        void handleMessage(int i, Object obj) throws ExoPlaybackException;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:16:0x002b in {4, 5, 9, 12, 15} preds:[]
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
    public synchronized boolean blockUntilDelivered() throws java.lang.InterruptedException {
        /*
        r2 = this;
        monitor-enter(r2);
        r0 = r2.isSent;	 Catch:{ all -> 0x0028 }
        com.google.android.exoplayer2.util.Assertions.checkState(r0);	 Catch:{ all -> 0x0028 }
        r0 = r2.handler;	 Catch:{ all -> 0x0028 }
        r0 = r0.getLooper();	 Catch:{ all -> 0x0028 }
        r0 = r0.getThread();	 Catch:{ all -> 0x0028 }
        r1 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0028 }
        if (r0 == r1) goto L_0x0018;	 Catch:{ all -> 0x0028 }
        r0 = 1;	 Catch:{ all -> 0x0028 }
        goto L_0x0019;	 Catch:{ all -> 0x0028 }
        r0 = 0;	 Catch:{ all -> 0x0028 }
        com.google.android.exoplayer2.util.Assertions.checkState(r0);	 Catch:{ all -> 0x0028 }
        r0 = r2.isProcessed;	 Catch:{ all -> 0x0028 }
        if (r0 != 0) goto L_0x0024;	 Catch:{ all -> 0x0028 }
        r2.wait();	 Catch:{ all -> 0x0028 }
        goto L_0x001c;	 Catch:{ all -> 0x0028 }
        r0 = r2.isDelivered;	 Catch:{ all -> 0x0028 }
        monitor-exit(r2);
        return r0;
        r0 = move-exception;
        monitor-exit(r2);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.PlayerMessage.blockUntilDelivered():boolean");
    }

    public PlayerMessage(Sender sender, Target target, Timeline timeline, int i, Handler handler) {
        this.sender = sender;
        this.target = target;
        this.timeline = timeline;
        this.handler = handler;
        this.windowIndex = i;
    }

    public Timeline getTimeline() {
        return this.timeline;
    }

    public Target getTarget() {
        return this.target;
    }

    public PlayerMessage setType(int i) {
        Assertions.checkState(this.isSent ^ 1);
        this.type = i;
        return this;
    }

    public int getType() {
        return this.type;
    }

    public PlayerMessage setPayload(Object obj) {
        Assertions.checkState(this.isSent ^ 1);
        this.payload = obj;
        return this;
    }

    public Object getPayload() {
        return this.payload;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public long getPositionMs() {
        return this.positionMs;
    }

    public int getWindowIndex() {
        return this.windowIndex;
    }

    public boolean getDeleteAfterDelivery() {
        return this.deleteAfterDelivery;
    }

    public PlayerMessage send() {
        Assertions.checkState(this.isSent ^ 1);
        if (this.positionMs == -9223372036854775807L) {
            Assertions.checkArgument(this.deleteAfterDelivery);
        }
        this.isSent = true;
        this.sender.sendMessage(this);
        return this;
    }

    public synchronized boolean isCanceled() {
        return this.isCanceled;
    }

    public synchronized void markAsProcessed(boolean z) {
        this.isDelivered = z | this.isDelivered;
        this.isProcessed = true;
        notifyAll();
    }
}
