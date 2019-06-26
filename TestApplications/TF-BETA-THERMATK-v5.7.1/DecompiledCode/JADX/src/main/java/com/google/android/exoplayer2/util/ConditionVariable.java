package com.google.android.exoplayer2.util;

public final class ConditionVariable {
    private boolean isOpen;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:11:0x000e in {5, 7, 10} preds:[]
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
    public synchronized void block() throws java.lang.InterruptedException {
        /*
        r1 = this;
        monitor-enter(r1);
        r0 = r1.isOpen;	 Catch:{ all -> 0x000b }
        if (r0 != 0) goto L_0x0009;	 Catch:{ all -> 0x000b }
        r1.wait();	 Catch:{ all -> 0x000b }
        goto L_0x0001;
        monitor-exit(r1);
        return;
        r0 = move-exception;
        monitor-exit(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.ConditionVariable.block():void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:14:0x001f in {7, 10, 13} preds:[]
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
    public synchronized boolean block(long r4) throws java.lang.InterruptedException {
        /*
        r3 = this;
        monitor-enter(r3);
        r0 = android.os.SystemClock.elapsedRealtime();	 Catch:{ all -> 0x001c }
        r4 = r4 + r0;	 Catch:{ all -> 0x001c }
        r2 = r3.isOpen;	 Catch:{ all -> 0x001c }
        if (r2 != 0) goto L_0x0018;	 Catch:{ all -> 0x001c }
        r2 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));	 Catch:{ all -> 0x001c }
        if (r2 >= 0) goto L_0x0018;	 Catch:{ all -> 0x001c }
        r0 = r4 - r0;	 Catch:{ all -> 0x001c }
        r3.wait(r0);	 Catch:{ all -> 0x001c }
        r0 = android.os.SystemClock.elapsedRealtime();	 Catch:{ all -> 0x001c }
        goto L_0x0006;	 Catch:{ all -> 0x001c }
        r4 = r3.isOpen;	 Catch:{ all -> 0x001c }
        monitor-exit(r3);
        return r4;
        r4 = move-exception;
        monitor-exit(r3);
        throw r4;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.ConditionVariable.block(long):boolean");
    }

    public synchronized boolean open() {
        if (this.isOpen) {
            return false;
        }
        this.isOpen = true;
        notifyAll();
        return true;
    }

    public synchronized boolean close() {
        boolean z;
        z = this.isOpen;
        this.isOpen = false;
        return z;
    }
}
