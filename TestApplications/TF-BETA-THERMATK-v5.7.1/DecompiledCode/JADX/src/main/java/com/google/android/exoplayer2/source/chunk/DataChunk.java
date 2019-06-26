package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import java.io.IOException;
import java.util.Arrays;

public abstract class DataChunk extends Chunk {
    private byte[] data;
    private volatile boolean loadCanceled;

    public abstract void consume(byte[] bArr, int i) throws IOException;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:17:0x0037 in {8, 11, 13, 16} preds:[]
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
        r5 = this;
        r0 = r5.dataSource;	 Catch:{ all -> 0x0030 }
        r1 = r5.dataSpec;	 Catch:{ all -> 0x0030 }
        r0.open(r1);	 Catch:{ all -> 0x0030 }
        r0 = 0;	 Catch:{ all -> 0x0030 }
        r1 = 0;	 Catch:{ all -> 0x0030 }
        r2 = -1;	 Catch:{ all -> 0x0030 }
        if (r0 == r2) goto L_0x0021;	 Catch:{ all -> 0x0030 }
        r0 = r5.loadCanceled;	 Catch:{ all -> 0x0030 }
        if (r0 != 0) goto L_0x0021;	 Catch:{ all -> 0x0030 }
        r5.maybeExpandData(r1);	 Catch:{ all -> 0x0030 }
        r0 = r5.dataSource;	 Catch:{ all -> 0x0030 }
        r3 = r5.data;	 Catch:{ all -> 0x0030 }
        r4 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;	 Catch:{ all -> 0x0030 }
        r0 = r0.read(r3, r1, r4);	 Catch:{ all -> 0x0030 }
        if (r0 == r2) goto L_0x0009;	 Catch:{ all -> 0x0030 }
        r1 = r1 + r0;	 Catch:{ all -> 0x0030 }
        goto L_0x0009;	 Catch:{ all -> 0x0030 }
        r0 = r5.loadCanceled;	 Catch:{ all -> 0x0030 }
        if (r0 != 0) goto L_0x002a;	 Catch:{ all -> 0x0030 }
        r0 = r5.data;	 Catch:{ all -> 0x0030 }
        r5.consume(r0, r1);	 Catch:{ all -> 0x0030 }
        r0 = r5.dataSource;
        com.google.android.exoplayer2.util.Util.closeQuietly(r0);
        return;
        r0 = move-exception;
        r1 = r5.dataSource;
        com.google.android.exoplayer2.util.Util.closeQuietly(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.chunk.DataChunk.load():void");
    }

    public DataChunk(DataSource dataSource, DataSpec dataSpec, int i, Format format, int i2, Object obj, byte[] bArr) {
        super(dataSource, dataSpec, i, format, i2, obj, -9223372036854775807L, -9223372036854775807L);
        this.data = bArr;
    }

    public byte[] getDataHolder() {
        return this.data;
    }

    public final void cancelLoad() {
        this.loadCanceled = true;
    }

    private void maybeExpandData(int i) {
        byte[] bArr = this.data;
        if (bArr == null) {
            this.data = new byte[16384];
        } else if (bArr.length < i + 16384) {
            this.data = Arrays.copyOf(bArr, bArr.length + 16384);
        }
    }
}
