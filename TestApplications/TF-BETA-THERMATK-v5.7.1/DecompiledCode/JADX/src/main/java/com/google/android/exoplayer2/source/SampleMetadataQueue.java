package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.TrackOutput.CryptoData;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;

final class SampleMetadataQueue {
    private int absoluteFirstIndex;
    private int capacity = 1000;
    private CryptoData[] cryptoDatas;
    private int[] flags;
    private Format[] formats;
    private boolean isLastSampleQueued;
    private long largestDiscardedTimestampUs;
    private long largestQueuedTimestampUs;
    private int length;
    private long[] offsets;
    private int readPosition;
    private int relativeFirstIndex;
    private int[] sizes;
    private int[] sourceIds;
    private long[] timesUs;
    private Format upstreamFormat;
    private boolean upstreamFormatRequired;
    private boolean upstreamKeyframeRequired;
    private int upstreamSourceId;

    public static final class SampleExtrasHolder {
        public CryptoData cryptoData;
        public long offset;
        public int size;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:31:0x004d in {7, 9, 15, 24, 27, 30} preds:[]
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
    public synchronized boolean attemptSplice(long r8) {
        /*
        r7 = this;
        monitor-enter(r7);
        r0 = r7.length;	 Catch:{ all -> 0x004a }
        r1 = 0;	 Catch:{ all -> 0x004a }
        r2 = 1;	 Catch:{ all -> 0x004a }
        if (r0 != 0) goto L_0x0010;	 Catch:{ all -> 0x004a }
        r3 = r7.largestDiscardedTimestampUs;	 Catch:{ all -> 0x004a }
        r0 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1));
        if (r0 <= 0) goto L_0x000e;
        r1 = 1;
        monitor-exit(r7);
        return r1;
        r3 = r7.largestDiscardedTimestampUs;	 Catch:{ all -> 0x004a }
        r0 = r7.readPosition;	 Catch:{ all -> 0x004a }
        r5 = r7.getLargestTimestamp(r0);	 Catch:{ all -> 0x004a }
        r3 = java.lang.Math.max(r3, r5);	 Catch:{ all -> 0x004a }
        r0 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1));
        if (r0 < 0) goto L_0x0022;
        monitor-exit(r7);
        return r1;
        r0 = r7.length;	 Catch:{ all -> 0x004a }
        r1 = r7.length;	 Catch:{ all -> 0x004a }
        r1 = r1 - r2;	 Catch:{ all -> 0x004a }
        r1 = r7.getRelativeIndex(r1);	 Catch:{ all -> 0x004a }
        r3 = r7.readPosition;	 Catch:{ all -> 0x004a }
        if (r0 <= r3) goto L_0x0042;	 Catch:{ all -> 0x004a }
        r3 = r7.timesUs;	 Catch:{ all -> 0x004a }
        r4 = r3[r1];	 Catch:{ all -> 0x004a }
        r3 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));	 Catch:{ all -> 0x004a }
        if (r3 < 0) goto L_0x0042;	 Catch:{ all -> 0x004a }
        r0 = r0 + -1;	 Catch:{ all -> 0x004a }
        r1 = r1 + -1;	 Catch:{ all -> 0x004a }
        r3 = -1;	 Catch:{ all -> 0x004a }
        if (r1 != r3) goto L_0x002b;	 Catch:{ all -> 0x004a }
        r1 = r7.capacity;	 Catch:{ all -> 0x004a }
        r1 = r1 - r2;	 Catch:{ all -> 0x004a }
        goto L_0x002b;	 Catch:{ all -> 0x004a }
        r8 = r7.absoluteFirstIndex;	 Catch:{ all -> 0x004a }
        r8 = r8 + r0;	 Catch:{ all -> 0x004a }
        r7.discardUpstreamSamples(r8);	 Catch:{ all -> 0x004a }
        monitor-exit(r7);
        return r2;
        r8 = move-exception;
        monitor-exit(r7);
        throw r8;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.SampleMetadataQueue.attemptSplice(long):boolean");
    }

    public SampleMetadataQueue() {
        int i = this.capacity;
        this.sourceIds = new int[i];
        this.offsets = new long[i];
        this.timesUs = new long[i];
        this.flags = new int[i];
        this.sizes = new int[i];
        this.cryptoDatas = new CryptoData[i];
        this.formats = new Format[i];
        this.largestDiscardedTimestampUs = Long.MIN_VALUE;
        this.largestQueuedTimestampUs = Long.MIN_VALUE;
        this.upstreamFormatRequired = true;
        this.upstreamKeyframeRequired = true;
    }

    public void reset(boolean z) {
        this.length = 0;
        this.absoluteFirstIndex = 0;
        this.relativeFirstIndex = 0;
        this.readPosition = 0;
        this.upstreamKeyframeRequired = true;
        this.largestDiscardedTimestampUs = Long.MIN_VALUE;
        this.largestQueuedTimestampUs = Long.MIN_VALUE;
        this.isLastSampleQueued = false;
        if (z) {
            this.upstreamFormat = null;
            this.upstreamFormatRequired = true;
        }
    }

    public int getWriteIndex() {
        return this.absoluteFirstIndex + this.length;
    }

    public long discardUpstreamSamples(int i) {
        int writeIndex = getWriteIndex() - i;
        boolean z = false;
        boolean z2 = writeIndex >= 0 && writeIndex <= this.length - this.readPosition;
        Assertions.checkArgument(z2);
        this.length -= writeIndex;
        this.largestQueuedTimestampUs = Math.max(this.largestDiscardedTimestampUs, getLargestTimestamp(this.length));
        if (writeIndex == 0 && this.isLastSampleQueued) {
            z = true;
        }
        this.isLastSampleQueued = z;
        i = this.length;
        if (i == 0) {
            return 0;
        }
        i = getRelativeIndex(i - 1);
        return this.offsets[i] + ((long) this.sizes[i]);
    }

    public void sourceId(int i) {
        this.upstreamSourceId = i;
    }

    public int getFirstIndex() {
        return this.absoluteFirstIndex;
    }

    public int getReadIndex() {
        return this.absoluteFirstIndex + this.readPosition;
    }

    public int peekSourceId() {
        return hasNextSample() ? this.sourceIds[getRelativeIndex(this.readPosition)] : this.upstreamSourceId;
    }

    public synchronized boolean hasNextSample() {
        return this.readPosition != this.length;
    }

    public synchronized Format getUpstreamFormat() {
        return this.upstreamFormatRequired ? null : this.upstreamFormat;
    }

    public synchronized long getLargestQueuedTimestampUs() {
        return this.largestQueuedTimestampUs;
    }

    public synchronized boolean isLastSampleQueued() {
        return this.isLastSampleQueued;
    }

    public synchronized long getFirstTimestampUs() {
        return this.length == 0 ? Long.MIN_VALUE : this.timesUs[this.relativeFirstIndex];
    }

    public synchronized void rewind() {
        this.readPosition = 0;
    }

    /* JADX WARNING: Missing block: B:17:0x0022, code skipped:
            return -3;
     */
    public synchronized int read(com.google.android.exoplayer2.FormatHolder r5, com.google.android.exoplayer2.decoder.DecoderInputBuffer r6, boolean r7, boolean r8, com.google.android.exoplayer2.Format r9, com.google.android.exoplayer2.source.SampleMetadataQueue.SampleExtrasHolder r10) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = r4.hasNextSample();	 Catch:{ all -> 0x006f }
        r1 = -5;
        r2 = -3;
        r3 = -4;
        if (r0 != 0) goto L_0x0029;
    L_0x000a:
        if (r8 != 0) goto L_0x0023;
    L_0x000c:
        r8 = r4.isLastSampleQueued;	 Catch:{ all -> 0x006f }
        if (r8 == 0) goto L_0x0011;
    L_0x0010:
        goto L_0x0023;
    L_0x0011:
        r6 = r4.upstreamFormat;	 Catch:{ all -> 0x006f }
        if (r6 == 0) goto L_0x0021;
    L_0x0015:
        if (r7 != 0) goto L_0x001b;
    L_0x0017:
        r6 = r4.upstreamFormat;	 Catch:{ all -> 0x006f }
        if (r6 == r9) goto L_0x0021;
    L_0x001b:
        r6 = r4.upstreamFormat;	 Catch:{ all -> 0x006f }
        r5.format = r6;	 Catch:{ all -> 0x006f }
        monitor-exit(r4);
        return r1;
    L_0x0021:
        monitor-exit(r4);
        return r2;
    L_0x0023:
        r5 = 4;
        r6.setFlags(r5);	 Catch:{ all -> 0x006f }
        monitor-exit(r4);
        return r3;
    L_0x0029:
        r8 = r4.readPosition;	 Catch:{ all -> 0x006f }
        r8 = r4.getRelativeIndex(r8);	 Catch:{ all -> 0x006f }
        if (r7 != 0) goto L_0x0067;
    L_0x0031:
        r7 = r4.formats;	 Catch:{ all -> 0x006f }
        r7 = r7[r8];	 Catch:{ all -> 0x006f }
        if (r7 == r9) goto L_0x0038;
    L_0x0037:
        goto L_0x0067;
    L_0x0038:
        r5 = r6.isFlagsOnly();	 Catch:{ all -> 0x006f }
        if (r5 == 0) goto L_0x0040;
    L_0x003e:
        monitor-exit(r4);
        return r2;
    L_0x0040:
        r5 = r4.timesUs;	 Catch:{ all -> 0x006f }
        r0 = r5[r8];	 Catch:{ all -> 0x006f }
        r6.timeUs = r0;	 Catch:{ all -> 0x006f }
        r5 = r4.flags;	 Catch:{ all -> 0x006f }
        r5 = r5[r8];	 Catch:{ all -> 0x006f }
        r6.setFlags(r5);	 Catch:{ all -> 0x006f }
        r5 = r4.sizes;	 Catch:{ all -> 0x006f }
        r5 = r5[r8];	 Catch:{ all -> 0x006f }
        r10.size = r5;	 Catch:{ all -> 0x006f }
        r5 = r4.offsets;	 Catch:{ all -> 0x006f }
        r6 = r5[r8];	 Catch:{ all -> 0x006f }
        r10.offset = r6;	 Catch:{ all -> 0x006f }
        r5 = r4.cryptoDatas;	 Catch:{ all -> 0x006f }
        r5 = r5[r8];	 Catch:{ all -> 0x006f }
        r10.cryptoData = r5;	 Catch:{ all -> 0x006f }
        r5 = r4.readPosition;	 Catch:{ all -> 0x006f }
        r5 = r5 + 1;
        r4.readPosition = r5;	 Catch:{ all -> 0x006f }
        monitor-exit(r4);
        return r3;
    L_0x0067:
        r6 = r4.formats;	 Catch:{ all -> 0x006f }
        r6 = r6[r8];	 Catch:{ all -> 0x006f }
        r5.format = r6;	 Catch:{ all -> 0x006f }
        monitor-exit(r4);
        return r1;
    L_0x006f:
        r5 = move-exception;
        monitor-exit(r4);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.SampleMetadataQueue.read(com.google.android.exoplayer2.FormatHolder, com.google.android.exoplayer2.decoder.DecoderInputBuffer, boolean, boolean, com.google.android.exoplayer2.Format, com.google.android.exoplayer2.source.SampleMetadataQueue$SampleExtrasHolder):int");
    }

    /* JADX WARNING: Missing block: B:19:0x0038, code skipped:
            return -1;
     */
    public synchronized int advanceTo(long r9, boolean r11, boolean r12) {
        /*
        r8 = this;
        monitor-enter(r8);
        r0 = r8.readPosition;	 Catch:{ all -> 0x0039 }
        r2 = r8.getRelativeIndex(r0);	 Catch:{ all -> 0x0039 }
        r0 = r8.hasNextSample();	 Catch:{ all -> 0x0039 }
        r7 = -1;
        if (r0 == 0) goto L_0x0037;
    L_0x000e:
        r0 = r8.timesUs;	 Catch:{ all -> 0x0039 }
        r3 = r0[r2];	 Catch:{ all -> 0x0039 }
        r0 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
        if (r0 < 0) goto L_0x0037;
    L_0x0016:
        r0 = r8.largestQueuedTimestampUs;	 Catch:{ all -> 0x0039 }
        r3 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1));
        if (r3 <= 0) goto L_0x001f;
    L_0x001c:
        if (r12 != 0) goto L_0x001f;
    L_0x001e:
        goto L_0x0037;
    L_0x001f:
        r12 = r8.length;	 Catch:{ all -> 0x0039 }
        r0 = r8.readPosition;	 Catch:{ all -> 0x0039 }
        r3 = r12 - r0;
        r1 = r8;
        r4 = r9;
        r6 = r11;
        r9 = r1.findSampleBefore(r2, r3, r4, r6);	 Catch:{ all -> 0x0039 }
        if (r9 != r7) goto L_0x0030;
    L_0x002e:
        monitor-exit(r8);
        return r7;
    L_0x0030:
        r10 = r8.readPosition;	 Catch:{ all -> 0x0039 }
        r10 = r10 + r9;
        r8.readPosition = r10;	 Catch:{ all -> 0x0039 }
        monitor-exit(r8);
        return r9;
    L_0x0037:
        monitor-exit(r8);
        return r7;
    L_0x0039:
        r9 = move-exception;
        monitor-exit(r8);
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.SampleMetadataQueue.advanceTo(long, boolean, boolean):int");
    }

    public synchronized int advanceToEnd() {
        int i;
        i = this.length - this.readPosition;
        this.readPosition = this.length;
        return i;
    }

    public synchronized boolean setReadPosition(int i) {
        if (this.absoluteFirstIndex > i || i > this.absoluteFirstIndex + this.length) {
            return false;
        }
        this.readPosition = i - this.absoluteFirstIndex;
        return true;
    }

    /* JADX WARNING: Missing block: B:22:0x0037, code skipped:
            return -1;
     */
    public synchronized long discardTo(long r10, boolean r12, boolean r13) {
        /*
        r9 = this;
        monitor-enter(r9);
        r0 = r9.length;	 Catch:{ all -> 0x0038 }
        r1 = -1;
        if (r0 == 0) goto L_0x0036;
    L_0x0007:
        r0 = r9.timesUs;	 Catch:{ all -> 0x0038 }
        r3 = r9.relativeFirstIndex;	 Catch:{ all -> 0x0038 }
        r3 = r0[r3];	 Catch:{ all -> 0x0038 }
        r0 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1));
        if (r0 >= 0) goto L_0x0012;
    L_0x0011:
        goto L_0x0036;
    L_0x0012:
        if (r13 == 0) goto L_0x001f;
    L_0x0014:
        r13 = r9.readPosition;	 Catch:{ all -> 0x0038 }
        r0 = r9.length;	 Catch:{ all -> 0x0038 }
        if (r13 == r0) goto L_0x001f;
    L_0x001a:
        r13 = r9.readPosition;	 Catch:{ all -> 0x0038 }
        r13 = r13 + 1;
        goto L_0x0021;
    L_0x001f:
        r13 = r9.length;	 Catch:{ all -> 0x0038 }
    L_0x0021:
        r5 = r13;
        r4 = r9.relativeFirstIndex;	 Catch:{ all -> 0x0038 }
        r3 = r9;
        r6 = r10;
        r8 = r12;
        r10 = r3.findSampleBefore(r4, r5, r6, r8);	 Catch:{ all -> 0x0038 }
        r11 = -1;
        if (r10 != r11) goto L_0x0030;
    L_0x002e:
        monitor-exit(r9);
        return r1;
    L_0x0030:
        r10 = r9.discardSamples(r10);	 Catch:{ all -> 0x0038 }
        monitor-exit(r9);
        return r10;
    L_0x0036:
        monitor-exit(r9);
        return r1;
    L_0x0038:
        r10 = move-exception;
        monitor-exit(r9);
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.SampleMetadataQueue.discardTo(long, boolean, boolean):long");
    }

    public synchronized long discardToRead() {
        if (this.readPosition == 0) {
            return -1;
        }
        return discardSamples(this.readPosition);
    }

    public synchronized long discardToEnd() {
        if (this.length == 0) {
            return -1;
        }
        return discardSamples(this.length);
    }

    public synchronized boolean format(Format format) {
        if (format == null) {
            this.upstreamFormatRequired = true;
            return false;
        }
        this.upstreamFormatRequired = false;
        if (Util.areEqual(format, this.upstreamFormat)) {
            return false;
        }
        this.upstreamFormat = format;
        return true;
    }

    /* JADX WARNING: Missing block: B:23:0x00e0, code skipped:
            return;
     */
    public synchronized void commitSample(long r6, int r8, long r9, int r11, com.google.android.exoplayer2.extractor.TrackOutput.CryptoData r12) {
        /*
        r5 = this;
        monitor-enter(r5);
        r0 = r5.upstreamKeyframeRequired;	 Catch:{ all -> 0x00e1 }
        r1 = 0;
        if (r0 == 0) goto L_0x000e;
    L_0x0006:
        r0 = r8 & 1;
        if (r0 != 0) goto L_0x000c;
    L_0x000a:
        monitor-exit(r5);
        return;
    L_0x000c:
        r5.upstreamKeyframeRequired = r1;	 Catch:{ all -> 0x00e1 }
    L_0x000e:
        r0 = r5.upstreamFormatRequired;	 Catch:{ all -> 0x00e1 }
        r2 = 1;
        if (r0 != 0) goto L_0x0015;
    L_0x0013:
        r0 = 1;
        goto L_0x0016;
    L_0x0015:
        r0 = 0;
    L_0x0016:
        com.google.android.exoplayer2.util.Assertions.checkState(r0);	 Catch:{ all -> 0x00e1 }
        r0 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
        r0 = r0 & r8;
        if (r0 == 0) goto L_0x0020;
    L_0x001e:
        r0 = 1;
        goto L_0x0021;
    L_0x0020:
        r0 = 0;
    L_0x0021:
        r5.isLastSampleQueued = r0;	 Catch:{ all -> 0x00e1 }
        r3 = r5.largestQueuedTimestampUs;	 Catch:{ all -> 0x00e1 }
        r3 = java.lang.Math.max(r3, r6);	 Catch:{ all -> 0x00e1 }
        r5.largestQueuedTimestampUs = r3;	 Catch:{ all -> 0x00e1 }
        r0 = r5.length;	 Catch:{ all -> 0x00e1 }
        r0 = r5.getRelativeIndex(r0);	 Catch:{ all -> 0x00e1 }
        r3 = r5.timesUs;	 Catch:{ all -> 0x00e1 }
        r3[r0] = r6;	 Catch:{ all -> 0x00e1 }
        r6 = r5.offsets;	 Catch:{ all -> 0x00e1 }
        r6[r0] = r9;	 Catch:{ all -> 0x00e1 }
        r6 = r5.sizes;	 Catch:{ all -> 0x00e1 }
        r6[r0] = r11;	 Catch:{ all -> 0x00e1 }
        r6 = r5.flags;	 Catch:{ all -> 0x00e1 }
        r6[r0] = r8;	 Catch:{ all -> 0x00e1 }
        r6 = r5.cryptoDatas;	 Catch:{ all -> 0x00e1 }
        r6[r0] = r12;	 Catch:{ all -> 0x00e1 }
        r6 = r5.formats;	 Catch:{ all -> 0x00e1 }
        r7 = r5.upstreamFormat;	 Catch:{ all -> 0x00e1 }
        r6[r0] = r7;	 Catch:{ all -> 0x00e1 }
        r6 = r5.sourceIds;	 Catch:{ all -> 0x00e1 }
        r7 = r5.upstreamSourceId;	 Catch:{ all -> 0x00e1 }
        r6[r0] = r7;	 Catch:{ all -> 0x00e1 }
        r6 = r5.length;	 Catch:{ all -> 0x00e1 }
        r6 = r6 + r2;
        r5.length = r6;	 Catch:{ all -> 0x00e1 }
        r6 = r5.length;	 Catch:{ all -> 0x00e1 }
        r7 = r5.capacity;	 Catch:{ all -> 0x00e1 }
        if (r6 != r7) goto L_0x00df;
    L_0x005c:
        r6 = r5.capacity;	 Catch:{ all -> 0x00e1 }
        r6 = r6 + 1000;
        r7 = new int[r6];	 Catch:{ all -> 0x00e1 }
        r8 = new long[r6];	 Catch:{ all -> 0x00e1 }
        r9 = new long[r6];	 Catch:{ all -> 0x00e1 }
        r10 = new int[r6];	 Catch:{ all -> 0x00e1 }
        r11 = new int[r6];	 Catch:{ all -> 0x00e1 }
        r12 = new com.google.android.exoplayer2.extractor.TrackOutput.CryptoData[r6];	 Catch:{ all -> 0x00e1 }
        r0 = new com.google.android.exoplayer2.Format[r6];	 Catch:{ all -> 0x00e1 }
        r2 = r5.capacity;	 Catch:{ all -> 0x00e1 }
        r3 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00e1 }
        r2 = r2 - r3;
        r3 = r5.offsets;	 Catch:{ all -> 0x00e1 }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r3, r4, r8, r1, r2);	 Catch:{ all -> 0x00e1 }
        r3 = r5.timesUs;	 Catch:{ all -> 0x00e1 }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r3, r4, r9, r1, r2);	 Catch:{ all -> 0x00e1 }
        r3 = r5.flags;	 Catch:{ all -> 0x00e1 }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r3, r4, r10, r1, r2);	 Catch:{ all -> 0x00e1 }
        r3 = r5.sizes;	 Catch:{ all -> 0x00e1 }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r3, r4, r11, r1, r2);	 Catch:{ all -> 0x00e1 }
        r3 = r5.cryptoDatas;	 Catch:{ all -> 0x00e1 }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r3, r4, r12, r1, r2);	 Catch:{ all -> 0x00e1 }
        r3 = r5.formats;	 Catch:{ all -> 0x00e1 }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r3, r4, r0, r1, r2);	 Catch:{ all -> 0x00e1 }
        r3 = r5.sourceIds;	 Catch:{ all -> 0x00e1 }
        r4 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r3, r4, r7, r1, r2);	 Catch:{ all -> 0x00e1 }
        r3 = r5.relativeFirstIndex;	 Catch:{ all -> 0x00e1 }
        r4 = r5.offsets;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r4, r1, r8, r2, r3);	 Catch:{ all -> 0x00e1 }
        r4 = r5.timesUs;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r4, r1, r9, r2, r3);	 Catch:{ all -> 0x00e1 }
        r4 = r5.flags;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r4, r1, r10, r2, r3);	 Catch:{ all -> 0x00e1 }
        r4 = r5.sizes;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r4, r1, r11, r2, r3);	 Catch:{ all -> 0x00e1 }
        r4 = r5.cryptoDatas;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r4, r1, r12, r2, r3);	 Catch:{ all -> 0x00e1 }
        r4 = r5.formats;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r4, r1, r0, r2, r3);	 Catch:{ all -> 0x00e1 }
        r4 = r5.sourceIds;	 Catch:{ all -> 0x00e1 }
        java.lang.System.arraycopy(r4, r1, r7, r2, r3);	 Catch:{ all -> 0x00e1 }
        r5.offsets = r8;	 Catch:{ all -> 0x00e1 }
        r5.timesUs = r9;	 Catch:{ all -> 0x00e1 }
        r5.flags = r10;	 Catch:{ all -> 0x00e1 }
        r5.sizes = r11;	 Catch:{ all -> 0x00e1 }
        r5.cryptoDatas = r12;	 Catch:{ all -> 0x00e1 }
        r5.formats = r0;	 Catch:{ all -> 0x00e1 }
        r5.sourceIds = r7;	 Catch:{ all -> 0x00e1 }
        r5.relativeFirstIndex = r1;	 Catch:{ all -> 0x00e1 }
        r7 = r5.capacity;	 Catch:{ all -> 0x00e1 }
        r5.length = r7;	 Catch:{ all -> 0x00e1 }
        r5.capacity = r6;	 Catch:{ all -> 0x00e1 }
    L_0x00df:
        monitor-exit(r5);
        return;
    L_0x00e1:
        r6 = move-exception;
        monitor-exit(r5);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.SampleMetadataQueue.commitSample(long, int, long, int, com.google.android.exoplayer2.extractor.TrackOutput$CryptoData):void");
    }

    private int findSampleBefore(int i, int i2, long j, boolean z) {
        int i3 = i;
        int i4 = -1;
        for (i = 0; i < i2 && this.timesUs[i3] <= j; i++) {
            if (!(z && (this.flags[i3] & 1) == 0)) {
                i4 = i;
            }
            i3++;
            if (i3 == this.capacity) {
                i3 = 0;
            }
        }
        return i4;
    }

    private long discardSamples(int i) {
        this.largestDiscardedTimestampUs = Math.max(this.largestDiscardedTimestampUs, getLargestTimestamp(i));
        this.length -= i;
        this.absoluteFirstIndex += i;
        this.relativeFirstIndex += i;
        int i2 = this.relativeFirstIndex;
        int i3 = this.capacity;
        if (i2 >= i3) {
            this.relativeFirstIndex = i2 - i3;
        }
        this.readPosition -= i;
        if (this.readPosition < 0) {
            this.readPosition = 0;
        }
        if (this.length != 0) {
            return this.offsets[this.relativeFirstIndex];
        }
        i = this.relativeFirstIndex;
        if (i == 0) {
            i = this.capacity;
        }
        i--;
        return this.offsets[i] + ((long) this.sizes[i]);
    }

    private long getLargestTimestamp(int i) {
        long j = Long.MIN_VALUE;
        if (i == 0) {
            return Long.MIN_VALUE;
        }
        int relativeIndex = getRelativeIndex(i - 1);
        for (int i2 = 0; i2 < i; i2++) {
            j = Math.max(j, this.timesUs[relativeIndex]);
            if ((this.flags[relativeIndex] & 1) != 0) {
                break;
            }
            relativeIndex--;
            if (relativeIndex == -1) {
                relativeIndex = this.capacity - 1;
            }
        }
        return j;
    }

    private int getRelativeIndex(int i) {
        int i2 = this.relativeFirstIndex + i;
        i = this.capacity;
        return i2 < i ? i2 : i2 - i;
    }
}
