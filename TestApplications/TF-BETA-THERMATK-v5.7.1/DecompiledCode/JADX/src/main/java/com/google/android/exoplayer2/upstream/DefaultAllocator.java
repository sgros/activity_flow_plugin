package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.Assertions;

public final class DefaultAllocator implements Allocator {
    private int allocatedCount;
    private Allocation[] availableAllocations;
    private int availableCount;
    private final int individualAllocationSize;
    private final byte[] initialAllocationBlock;
    private final Allocation[] singleAllocationReleaseHolder;
    private int targetBufferSize;
    private final boolean trimOnReset;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:14:0x0042 in {4, 7, 10, 13} preds:[]
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
    public synchronized void release(com.google.android.exoplayer2.upstream.Allocation[] r7) {
        /*
        r6 = this;
        monitor-enter(r6);
        r0 = r6.availableCount;	 Catch:{ all -> 0x003f }
        r1 = r7.length;	 Catch:{ all -> 0x003f }
        r0 = r0 + r1;	 Catch:{ all -> 0x003f }
        r1 = r6.availableAllocations;	 Catch:{ all -> 0x003f }
        r1 = r1.length;	 Catch:{ all -> 0x003f }
        if (r0 < r1) goto L_0x0021;	 Catch:{ all -> 0x003f }
        r0 = r6.availableAllocations;	 Catch:{ all -> 0x003f }
        r1 = r6.availableAllocations;	 Catch:{ all -> 0x003f }
        r1 = r1.length;	 Catch:{ all -> 0x003f }
        r1 = r1 * 2;	 Catch:{ all -> 0x003f }
        r2 = r6.availableCount;	 Catch:{ all -> 0x003f }
        r3 = r7.length;	 Catch:{ all -> 0x003f }
        r2 = r2 + r3;	 Catch:{ all -> 0x003f }
        r1 = java.lang.Math.max(r1, r2);	 Catch:{ all -> 0x003f }
        r0 = java.util.Arrays.copyOf(r0, r1);	 Catch:{ all -> 0x003f }
        r0 = (com.google.android.exoplayer2.upstream.Allocation[]) r0;	 Catch:{ all -> 0x003f }
        r6.availableAllocations = r0;	 Catch:{ all -> 0x003f }
        r0 = r7.length;	 Catch:{ all -> 0x003f }
        r1 = 0;	 Catch:{ all -> 0x003f }
        if (r1 >= r0) goto L_0x0034;	 Catch:{ all -> 0x003f }
        r2 = r7[r1];	 Catch:{ all -> 0x003f }
        r3 = r6.availableAllocations;	 Catch:{ all -> 0x003f }
        r4 = r6.availableCount;	 Catch:{ all -> 0x003f }
        r5 = r4 + 1;	 Catch:{ all -> 0x003f }
        r6.availableCount = r5;	 Catch:{ all -> 0x003f }
        r3[r4] = r2;	 Catch:{ all -> 0x003f }
        r1 = r1 + 1;	 Catch:{ all -> 0x003f }
        goto L_0x0023;	 Catch:{ all -> 0x003f }
        r0 = r6.allocatedCount;	 Catch:{ all -> 0x003f }
        r7 = r7.length;	 Catch:{ all -> 0x003f }
        r0 = r0 - r7;	 Catch:{ all -> 0x003f }
        r6.allocatedCount = r0;	 Catch:{ all -> 0x003f }
        r6.notifyAll();	 Catch:{ all -> 0x003f }
        monitor-exit(r6);
        return;
        r7 = move-exception;
        monitor-exit(r6);
        throw r7;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.upstream.DefaultAllocator.release(com.google.android.exoplayer2.upstream.Allocation[]):void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:29:0x0063 in {5, 13, 16, 17, 21, 25, 28} preds:[]
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
    public synchronized void trim() {
        /*
        r7 = this;
        monitor-enter(r7);
        r0 = r7.targetBufferSize;	 Catch:{ all -> 0x0060 }
        r1 = r7.individualAllocationSize;	 Catch:{ all -> 0x0060 }
        r0 = com.google.android.exoplayer2.util.Util.ceilDivide(r0, r1);	 Catch:{ all -> 0x0060 }
        r1 = r7.allocatedCount;	 Catch:{ all -> 0x0060 }
        r0 = r0 - r1;	 Catch:{ all -> 0x0060 }
        r1 = 0;	 Catch:{ all -> 0x0060 }
        r0 = java.lang.Math.max(r1, r0);	 Catch:{ all -> 0x0060 }
        r2 = r7.availableCount;	 Catch:{ all -> 0x0060 }
        if (r0 < r2) goto L_0x0017;
        monitor-exit(r7);
        return;
        r2 = r7.initialAllocationBlock;	 Catch:{ all -> 0x0060 }
        if (r2 == 0) goto L_0x0054;	 Catch:{ all -> 0x0060 }
        r2 = r7.availableCount;	 Catch:{ all -> 0x0060 }
        r2 = r2 + -1;	 Catch:{ all -> 0x0060 }
        if (r1 > r2) goto L_0x004a;	 Catch:{ all -> 0x0060 }
        r3 = r7.availableAllocations;	 Catch:{ all -> 0x0060 }
        r3 = r3[r1];	 Catch:{ all -> 0x0060 }
        r4 = r3.data;	 Catch:{ all -> 0x0060 }
        r5 = r7.initialAllocationBlock;	 Catch:{ all -> 0x0060 }
        if (r4 != r5) goto L_0x002e;	 Catch:{ all -> 0x0060 }
        r1 = r1 + 1;	 Catch:{ all -> 0x0060 }
        goto L_0x001f;	 Catch:{ all -> 0x0060 }
        r4 = r7.availableAllocations;	 Catch:{ all -> 0x0060 }
        r4 = r4[r2];	 Catch:{ all -> 0x0060 }
        r5 = r4.data;	 Catch:{ all -> 0x0060 }
        r6 = r7.initialAllocationBlock;	 Catch:{ all -> 0x0060 }
        if (r5 == r6) goto L_0x003b;	 Catch:{ all -> 0x0060 }
        r2 = r2 + -1;	 Catch:{ all -> 0x0060 }
        goto L_0x001f;	 Catch:{ all -> 0x0060 }
        r5 = r7.availableAllocations;	 Catch:{ all -> 0x0060 }
        r6 = r1 + 1;	 Catch:{ all -> 0x0060 }
        r5[r1] = r4;	 Catch:{ all -> 0x0060 }
        r1 = r7.availableAllocations;	 Catch:{ all -> 0x0060 }
        r4 = r2 + -1;	 Catch:{ all -> 0x0060 }
        r1[r2] = r3;	 Catch:{ all -> 0x0060 }
        r2 = r4;	 Catch:{ all -> 0x0060 }
        r1 = r6;	 Catch:{ all -> 0x0060 }
        goto L_0x001f;	 Catch:{ all -> 0x0060 }
        r0 = java.lang.Math.max(r0, r1);	 Catch:{ all -> 0x0060 }
        r1 = r7.availableCount;	 Catch:{ all -> 0x0060 }
        if (r0 < r1) goto L_0x0054;
        monitor-exit(r7);
        return;
        r1 = r7.availableAllocations;	 Catch:{ all -> 0x0060 }
        r2 = r7.availableCount;	 Catch:{ all -> 0x0060 }
        r3 = 0;	 Catch:{ all -> 0x0060 }
        java.util.Arrays.fill(r1, r0, r2, r3);	 Catch:{ all -> 0x0060 }
        r7.availableCount = r0;	 Catch:{ all -> 0x0060 }
        monitor-exit(r7);
        return;
        r0 = move-exception;
        monitor-exit(r7);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.upstream.DefaultAllocator.trim():void");
    }

    public DefaultAllocator(boolean z, int i) {
        this(z, i, 0);
    }

    public DefaultAllocator(boolean z, int i, int i2) {
        Assertions.checkArgument(i > 0);
        Assertions.checkArgument(i2 >= 0);
        this.trimOnReset = z;
        this.individualAllocationSize = i;
        this.availableCount = i2;
        this.availableAllocations = new Allocation[(i2 + 100)];
        if (i2 > 0) {
            this.initialAllocationBlock = new byte[(i2 * i)];
            for (int i3 = 0; i3 < i2; i3++) {
                this.availableAllocations[i3] = new Allocation(this.initialAllocationBlock, i3 * i);
            }
        } else {
            this.initialAllocationBlock = null;
        }
        this.singleAllocationReleaseHolder = new Allocation[1];
    }

    public synchronized void reset() {
        if (this.trimOnReset) {
            setTargetBufferSize(0);
        }
    }

    public synchronized void setTargetBufferSize(int i) {
        Object obj = i < this.targetBufferSize ? 1 : null;
        this.targetBufferSize = i;
        if (obj != null) {
            trim();
        }
    }

    public synchronized Allocation allocate() {
        Allocation allocation;
        this.allocatedCount++;
        if (this.availableCount > 0) {
            Allocation[] allocationArr = this.availableAllocations;
            int i = this.availableCount - 1;
            this.availableCount = i;
            allocation = allocationArr[i];
            this.availableAllocations[this.availableCount] = null;
        } else {
            allocation = new Allocation(new byte[this.individualAllocationSize], 0);
        }
        return allocation;
    }

    public synchronized void release(Allocation allocation) {
        this.singleAllocationReleaseHolder[0] = allocation;
        release(this.singleAllocationReleaseHolder);
    }

    public synchronized int getTotalBytesAllocated() {
        return this.allocatedCount * this.individualAllocationSize;
    }

    public int getIndividualAllocationLength() {
        return this.individualAllocationSize;
    }
}
