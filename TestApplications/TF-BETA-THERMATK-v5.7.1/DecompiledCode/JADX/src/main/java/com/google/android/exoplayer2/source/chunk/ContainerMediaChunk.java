package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;

public class ContainerMediaChunk extends BaseMediaChunk {
    private static final PositionHolder DUMMY_POSITION_HOLDER = new PositionHolder();
    private final int chunkCount;
    private final ChunkExtractorWrapper extractorWrapper;
    private volatile boolean loadCanceled;
    private boolean loadCompleted;
    private long nextLoadPosition;
    private final long sampleOffsetUs;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:34:0x0090 in {6, 7, 10, 11, 12, 18, 21, 26, 30, 33} preds:[]
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
    public final void load() throws java.io.IOException, java.lang.InterruptedException {
        /*
        r14 = this;
        r0 = r14.dataSpec;
        r1 = r14.nextLoadPosition;
        r0 = r0.subrange(r1);
        r7 = new com.google.android.exoplayer2.extractor.DefaultExtractorInput;	 Catch:{ all -> 0x0089 }
        r2 = r14.dataSource;	 Catch:{ all -> 0x0089 }
        r3 = r0.absoluteStreamPosition;	 Catch:{ all -> 0x0089 }
        r1 = r14.dataSource;	 Catch:{ all -> 0x0089 }
        r5 = r1.open(r0);	 Catch:{ all -> 0x0089 }
        r1 = r7;	 Catch:{ all -> 0x0089 }
        r1.<init>(r2, r3, r5);	 Catch:{ all -> 0x0089 }
        r0 = r14.nextLoadPosition;	 Catch:{ all -> 0x0089 }
        r2 = 0;	 Catch:{ all -> 0x0089 }
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));	 Catch:{ all -> 0x0089 }
        if (r4 != 0) goto L_0x004f;	 Catch:{ all -> 0x0089 }
        r9 = r14.getOutput();	 Catch:{ all -> 0x0089 }
        r0 = r14.sampleOffsetUs;	 Catch:{ all -> 0x0089 }
        r9.setSampleOffsetUs(r0);	 Catch:{ all -> 0x0089 }
        r8 = r14.extractorWrapper;	 Catch:{ all -> 0x0089 }
        r0 = r14.clippedStartTimeUs;	 Catch:{ all -> 0x0089 }
        r2 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;	 Catch:{ all -> 0x0089 }
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));	 Catch:{ all -> 0x0089 }
        if (r4 != 0) goto L_0x0038;	 Catch:{ all -> 0x0089 }
        r10 = r2;	 Catch:{ all -> 0x0089 }
        goto L_0x003e;	 Catch:{ all -> 0x0089 }
        r0 = r14.clippedStartTimeUs;	 Catch:{ all -> 0x0089 }
        r4 = r14.sampleOffsetUs;	 Catch:{ all -> 0x0089 }
        r0 = r0 - r4;	 Catch:{ all -> 0x0089 }
        r10 = r0;	 Catch:{ all -> 0x0089 }
        r0 = r14.clippedEndTimeUs;	 Catch:{ all -> 0x0089 }
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));	 Catch:{ all -> 0x0089 }
        if (r4 != 0) goto L_0x0046;	 Catch:{ all -> 0x0089 }
        r12 = r2;	 Catch:{ all -> 0x0089 }
        goto L_0x004c;	 Catch:{ all -> 0x0089 }
        r0 = r14.clippedEndTimeUs;	 Catch:{ all -> 0x0089 }
        r2 = r14.sampleOffsetUs;	 Catch:{ all -> 0x0089 }
        r0 = r0 - r2;	 Catch:{ all -> 0x0089 }
        r12 = r0;	 Catch:{ all -> 0x0089 }
        r8.init(r9, r10, r12);	 Catch:{ all -> 0x0089 }
        r0 = r14.extractorWrapper;	 Catch:{ all -> 0x007c }
        r0 = r0.extractor;	 Catch:{ all -> 0x007c }
        r1 = 0;	 Catch:{ all -> 0x007c }
        r2 = 0;	 Catch:{ all -> 0x007c }
        if (r2 != 0) goto L_0x0062;	 Catch:{ all -> 0x007c }
        r3 = r14.loadCanceled;	 Catch:{ all -> 0x007c }
        if (r3 != 0) goto L_0x0062;	 Catch:{ all -> 0x007c }
        r2 = DUMMY_POSITION_HOLDER;	 Catch:{ all -> 0x007c }
        r2 = r0.read(r7, r2);	 Catch:{ all -> 0x007c }
        goto L_0x0055;	 Catch:{ all -> 0x007c }
        r0 = 1;	 Catch:{ all -> 0x007c }
        if (r2 == r0) goto L_0x0066;	 Catch:{ all -> 0x007c }
        r1 = 1;	 Catch:{ all -> 0x007c }
        com.google.android.exoplayer2.util.Assertions.checkState(r1);	 Catch:{ all -> 0x007c }
        r1 = r7.getPosition();	 Catch:{ all -> 0x0089 }
        r3 = r14.dataSpec;	 Catch:{ all -> 0x0089 }
        r3 = r3.absoluteStreamPosition;	 Catch:{ all -> 0x0089 }
        r1 = r1 - r3;	 Catch:{ all -> 0x0089 }
        r14.nextLoadPosition = r1;	 Catch:{ all -> 0x0089 }
        r1 = r14.dataSource;
        com.google.android.exoplayer2.util.Util.closeQuietly(r1);
        r14.loadCompleted = r0;
        return;
        r0 = move-exception;
        r1 = r7.getPosition();	 Catch:{ all -> 0x0089 }
        r3 = r14.dataSpec;	 Catch:{ all -> 0x0089 }
        r3 = r3.absoluteStreamPosition;	 Catch:{ all -> 0x0089 }
        r1 = r1 - r3;	 Catch:{ all -> 0x0089 }
        r14.nextLoadPosition = r1;	 Catch:{ all -> 0x0089 }
        throw r0;	 Catch:{ all -> 0x0089 }
        r0 = move-exception;
        r1 = r14.dataSource;
        com.google.android.exoplayer2.util.Util.closeQuietly(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.chunk.ContainerMediaChunk.load():void");
    }

    public ContainerMediaChunk(DataSource dataSource, DataSpec dataSpec, Format format, int i, Object obj, long j, long j2, long j3, long j4, long j5, int i2, long j6, ChunkExtractorWrapper chunkExtractorWrapper) {
        super(dataSource, dataSpec, format, i, obj, j, j2, j3, j4, j5);
        this.chunkCount = i2;
        this.sampleOffsetUs = j6;
        this.extractorWrapper = chunkExtractorWrapper;
    }

    public long getNextChunkIndex() {
        return this.chunkIndex + ((long) this.chunkCount);
    }

    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }

    public final void cancelLoad() {
        this.loadCanceled = true;
    }
}
