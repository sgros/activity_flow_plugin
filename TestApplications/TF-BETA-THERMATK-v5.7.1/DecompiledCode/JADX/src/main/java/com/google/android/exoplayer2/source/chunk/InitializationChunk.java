package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;

public final class InitializationChunk extends Chunk {
    private static final PositionHolder DUMMY_POSITION_HOLDER = new PositionHolder();
    private final ChunkExtractorWrapper extractorWrapper;
    private volatile boolean loadCanceled;
    private long nextLoadPosition;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:27:0x0070 in {4, 10, 13, 14, 19, 23, 26} preds:[]
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
    public void load() throws java.io.IOException, java.lang.InterruptedException {
        /*
        r14 = this;
        r0 = r14.dataSpec;
        r1 = r14.nextLoadPosition;
        r0 = r0.subrange(r1);
        r7 = new com.google.android.exoplayer2.extractor.DefaultExtractorInput;	 Catch:{ all -> 0x0069 }
        r2 = r14.dataSource;	 Catch:{ all -> 0x0069 }
        r3 = r0.absoluteStreamPosition;	 Catch:{ all -> 0x0069 }
        r1 = r14.dataSource;	 Catch:{ all -> 0x0069 }
        r5 = r1.open(r0);	 Catch:{ all -> 0x0069 }
        r1 = r7;	 Catch:{ all -> 0x0069 }
        r1.<init>(r2, r3, r5);	 Catch:{ all -> 0x0069 }
        r0 = r14.nextLoadPosition;	 Catch:{ all -> 0x0069 }
        r2 = 0;	 Catch:{ all -> 0x0069 }
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));	 Catch:{ all -> 0x0069 }
        if (r4 != 0) goto L_0x0030;	 Catch:{ all -> 0x0069 }
        r8 = r14.extractorWrapper;	 Catch:{ all -> 0x0069 }
        r9 = 0;	 Catch:{ all -> 0x0069 }
        r10 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;	 Catch:{ all -> 0x0069 }
        r12 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;	 Catch:{ all -> 0x0069 }
        r8.init(r9, r10, r12);	 Catch:{ all -> 0x0069 }
        r0 = r14.extractorWrapper;	 Catch:{ all -> 0x005c }
        r0 = r0.extractor;	 Catch:{ all -> 0x005c }
        r1 = 0;	 Catch:{ all -> 0x005c }
        r2 = 0;	 Catch:{ all -> 0x005c }
        if (r2 != 0) goto L_0x0043;	 Catch:{ all -> 0x005c }
        r3 = r14.loadCanceled;	 Catch:{ all -> 0x005c }
        if (r3 != 0) goto L_0x0043;	 Catch:{ all -> 0x005c }
        r2 = DUMMY_POSITION_HOLDER;	 Catch:{ all -> 0x005c }
        r2 = r0.read(r7, r2);	 Catch:{ all -> 0x005c }
        goto L_0x0036;	 Catch:{ all -> 0x005c }
        r0 = 1;	 Catch:{ all -> 0x005c }
        if (r2 == r0) goto L_0x0047;	 Catch:{ all -> 0x005c }
        goto L_0x0048;	 Catch:{ all -> 0x005c }
        r0 = 0;	 Catch:{ all -> 0x005c }
        com.google.android.exoplayer2.util.Assertions.checkState(r0);	 Catch:{ all -> 0x005c }
        r0 = r7.getPosition();	 Catch:{ all -> 0x0069 }
        r2 = r14.dataSpec;	 Catch:{ all -> 0x0069 }
        r2 = r2.absoluteStreamPosition;	 Catch:{ all -> 0x0069 }
        r0 = r0 - r2;	 Catch:{ all -> 0x0069 }
        r14.nextLoadPosition = r0;	 Catch:{ all -> 0x0069 }
        r0 = r14.dataSource;
        com.google.android.exoplayer2.util.Util.closeQuietly(r0);
        return;
        r0 = move-exception;
        r1 = r7.getPosition();	 Catch:{ all -> 0x0069 }
        r3 = r14.dataSpec;	 Catch:{ all -> 0x0069 }
        r3 = r3.absoluteStreamPosition;	 Catch:{ all -> 0x0069 }
        r1 = r1 - r3;	 Catch:{ all -> 0x0069 }
        r14.nextLoadPosition = r1;	 Catch:{ all -> 0x0069 }
        throw r0;	 Catch:{ all -> 0x0069 }
        r0 = move-exception;
        r1 = r14.dataSource;
        com.google.android.exoplayer2.util.Util.closeQuietly(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.chunk.InitializationChunk.load():void");
    }

    public InitializationChunk(DataSource dataSource, DataSpec dataSpec, Format format, int i, Object obj, ChunkExtractorWrapper chunkExtractorWrapper) {
        super(dataSource, dataSpec, 2, format, i, obj, -9223372036854775807L, -9223372036854775807L);
        this.extractorWrapper = chunkExtractorWrapper;
    }

    public void cancelLoad() {
        this.loadCanceled = true;
    }
}
