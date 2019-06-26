package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;

final class OggPageHeader {
    private static final int TYPE_OGGS = Util.getIntegerCodeForString("OggS");
    public int bodySize;
    public long granulePosition;
    public int headerSize;
    public final int[] laces = new int[NalUnitUtil.EXTENDED_SAR];
    public long pageChecksum;
    public int pageSegmentCount;
    public long pageSequenceNumber;
    public int revision;
    private final ParsableByteArray scratch = new ParsableByteArray((int) NalUnitUtil.EXTENDED_SAR);
    public long streamSerialNumber;
    public int type;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:32:0x00cd in {4, 5, 6, 10, 14, 16, 20, 22, 26, 27, 29, 31} preds:[]
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
    public boolean populate(com.google.android.exoplayer2.extractor.ExtractorInput r9, boolean r10) throws java.io.IOException, java.lang.InterruptedException {
        /*
        r8 = this;
        r0 = r8.scratch;
        r0.reset();
        r8.reset();
        r0 = r9.getLength();
        r2 = 1;
        r3 = 0;
        r4 = -1;
        r6 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r6 == 0) goto L_0x0026;
        r0 = r9.getLength();
        r4 = r9.getPeekPosition();
        r0 = r0 - r4;
        r4 = 27;
        r6 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r6 < 0) goto L_0x0024;
        goto L_0x0026;
        r0 = 0;
        goto L_0x0027;
        r0 = 1;
        if (r0 == 0) goto L_0x00c4;
        r0 = r8.scratch;
        r0 = r0.data;
        r1 = 27;
        r0 = r9.peekFully(r0, r3, r1, r2);
        if (r0 != 0) goto L_0x0037;
        goto L_0x00c4;
        r0 = r8.scratch;
        r4 = r0.readUnsignedInt();
        r0 = TYPE_OGGS;
        r6 = (long) r0;
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 == 0) goto L_0x004f;
        if (r10 == 0) goto L_0x0047;
        return r3;
        r9 = new com.google.android.exoplayer2.ParserException;
        r10 = "expected OggS capture pattern at begin of page";
        r9.<init>(r10);
        throw r9;
        r0 = r8.scratch;
        r0 = r0.readUnsignedByte();
        r8.revision = r0;
        r0 = r8.revision;
        if (r0 == 0) goto L_0x0066;
        if (r10 == 0) goto L_0x005e;
        return r3;
        r9 = new com.google.android.exoplayer2.ParserException;
        r10 = "unsupported bit stream revision";
        r9.<init>(r10);
        throw r9;
        r10 = r8.scratch;
        r10 = r10.readUnsignedByte();
        r8.type = r10;
        r10 = r8.scratch;
        r4 = r10.readLittleEndianLong();
        r8.granulePosition = r4;
        r10 = r8.scratch;
        r4 = r10.readLittleEndianUnsignedInt();
        r8.streamSerialNumber = r4;
        r10 = r8.scratch;
        r4 = r10.readLittleEndianUnsignedInt();
        r8.pageSequenceNumber = r4;
        r10 = r8.scratch;
        r4 = r10.readLittleEndianUnsignedInt();
        r8.pageChecksum = r4;
        r10 = r8.scratch;
        r10 = r10.readUnsignedByte();
        r8.pageSegmentCount = r10;
        r10 = r8.pageSegmentCount;
        r10 = r10 + r1;
        r8.headerSize = r10;
        r10 = r8.scratch;
        r10.reset();
        r10 = r8.scratch;
        r10 = r10.data;
        r0 = r8.pageSegmentCount;
        r9.peekFully(r10, r3, r0);
        r9 = r8.pageSegmentCount;
        if (r3 >= r9) goto L_0x00c3;
        r9 = r8.laces;
        r10 = r8.scratch;
        r10 = r10.readUnsignedByte();
        r9[r3] = r10;
        r9 = r8.bodySize;
        r10 = r8.laces;
        r10 = r10[r3];
        r9 = r9 + r10;
        r8.bodySize = r9;
        r3 = r3 + 1;
        goto L_0x00a9;
        return r2;
        if (r10 == 0) goto L_0x00c7;
        return r3;
        r9 = new java.io.EOFException;
        r9.<init>();
        throw r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.ogg.OggPageHeader.populate(com.google.android.exoplayer2.extractor.ExtractorInput, boolean):boolean");
    }

    OggPageHeader() {
    }

    public void reset() {
        this.revision = 0;
        this.type = 0;
        this.granulePosition = 0;
        this.streamSerialNumber = 0;
        this.pageSequenceNumber = 0;
        this.pageChecksum = 0;
        this.pageSegmentCount = 0;
        this.headerSize = 0;
        this.bodySize = 0;
    }
}
