package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;

public final class SingleSampleMediaChunk extends BaseMediaChunk {
    private boolean loadCompleted;
    private long nextLoadPosition;
    private final Format sampleFormat;
    private final int trackType;

    public void cancelLoad() {
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:15:0x0063 in {4, 8, 11, 14} preds:[]
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
    public void load() throws java.io.IOException, java.lang.InterruptedException {
        /*
        r11 = this;
        r0 = r11.dataSpec;
        r1 = r11.nextLoadPosition;
        r0 = r0.subrange(r1);
        r1 = r11.dataSource;	 Catch:{ all -> 0x005c }
        r0 = r1.open(r0);	 Catch:{ all -> 0x005c }
        r2 = -1;	 Catch:{ all -> 0x005c }
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));	 Catch:{ all -> 0x005c }
        if (r4 == 0) goto L_0x0017;	 Catch:{ all -> 0x005c }
        r2 = r11.nextLoadPosition;	 Catch:{ all -> 0x005c }
        r0 = r0 + r2;	 Catch:{ all -> 0x005c }
        r5 = r0;	 Catch:{ all -> 0x005c }
        r0 = new com.google.android.exoplayer2.extractor.DefaultExtractorInput;	 Catch:{ all -> 0x005c }
        r2 = r11.dataSource;	 Catch:{ all -> 0x005c }
        r3 = r11.nextLoadPosition;	 Catch:{ all -> 0x005c }
        r1 = r0;	 Catch:{ all -> 0x005c }
        r1.<init>(r2, r3, r5);	 Catch:{ all -> 0x005c }
        r1 = r11.getOutput();	 Catch:{ all -> 0x005c }
        r2 = 0;	 Catch:{ all -> 0x005c }
        r1.setSampleOffsetUs(r2);	 Catch:{ all -> 0x005c }
        r2 = r11.trackType;	 Catch:{ all -> 0x005c }
        r3 = 0;	 Catch:{ all -> 0x005c }
        r4 = r1.track(r3, r2);	 Catch:{ all -> 0x005c }
        r1 = r11.sampleFormat;	 Catch:{ all -> 0x005c }
        r4.format(r1);	 Catch:{ all -> 0x005c }
        r1 = -1;	 Catch:{ all -> 0x005c }
        r2 = 1;	 Catch:{ all -> 0x005c }
        if (r3 == r1) goto L_0x0049;	 Catch:{ all -> 0x005c }
        r5 = r11.nextLoadPosition;	 Catch:{ all -> 0x005c }
        r7 = (long) r3;	 Catch:{ all -> 0x005c }
        r5 = r5 + r7;	 Catch:{ all -> 0x005c }
        r11.nextLoadPosition = r5;	 Catch:{ all -> 0x005c }
        r1 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;	 Catch:{ all -> 0x005c }
        r3 = r4.sampleData(r0, r1, r2);	 Catch:{ all -> 0x005c }
        goto L_0x0037;	 Catch:{ all -> 0x005c }
        r0 = r11.nextLoadPosition;	 Catch:{ all -> 0x005c }
        r8 = (int) r0;	 Catch:{ all -> 0x005c }
        r5 = r11.startTimeUs;	 Catch:{ all -> 0x005c }
        r7 = 1;	 Catch:{ all -> 0x005c }
        r9 = 0;	 Catch:{ all -> 0x005c }
        r10 = 0;	 Catch:{ all -> 0x005c }
        r4.sampleMetadata(r5, r7, r8, r9, r10);	 Catch:{ all -> 0x005c }
        r0 = r11.dataSource;
        com.google.android.exoplayer2.util.Util.closeQuietly(r0);
        r11.loadCompleted = r2;
        return;
        r0 = move-exception;
        r1 = r11.dataSource;
        com.google.android.exoplayer2.util.Util.closeQuietly(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.chunk.SingleSampleMediaChunk.load():void");
    }

    public SingleSampleMediaChunk(DataSource dataSource, DataSpec dataSpec, Format format, int i, Object obj, long j, long j2, long j3, int i2, Format format2) {
        super(dataSource, dataSpec, format, i, obj, j, j2, -9223372036854775807L, -9223372036854775807L, j3);
        this.trackType = i2;
        this.sampleFormat = format2;
    }

    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }
}
