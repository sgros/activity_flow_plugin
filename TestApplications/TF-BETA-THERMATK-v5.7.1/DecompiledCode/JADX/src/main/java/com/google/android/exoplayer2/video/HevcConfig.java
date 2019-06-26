package com.google.android.exoplayer2.video;

import java.util.List;

public final class HevcConfig {
    public final List<byte[]> initializationData;
    public final int nalUnitLengthFieldLength;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:23:0x0085 in {7, 8, 13, 14, 16, 17, 19, 22} preds:[]
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
    public static com.google.android.exoplayer2.video.HevcConfig parse(com.google.android.exoplayer2.util.ParsableByteArray r13) throws com.google.android.exoplayer2.ParserException {
        /*
        r0 = 21;
        r13.skipBytes(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r0 = r13.readUnsignedByte();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r0 = r0 & 3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r1 = r13.readUnsignedByte();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r2 = r13.getPosition();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r3 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r4 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r5 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r6 = 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        if (r4 >= r1) goto L_0x0035;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r13.skipBytes(r6);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r6 = r13.readUnsignedShort();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r7 = r5;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r5 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        if (r5 >= r6) goto L_0x0031;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r8 = r13.readUnsignedShort();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r9 = r8 + 4;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r7 = r7 + r9;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r13.skipBytes(r8);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r5 = r5 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        goto L_0x0022;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r4 = r4 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r5 = r7;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        goto L_0x0016;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r13.setPosition(r2);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r2 = new byte[r5];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r4 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r7 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        if (r4 >= r1) goto L_0x006d;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r13.skipBytes(r6);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r8 = r13.readUnsignedShort();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r9 = r7;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r7 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        if (r7 >= r8) goto L_0x0069;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r10 = r13.readUnsignedShort();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r11 = com.google.android.exoplayer2.util.NalUnitUtil.NAL_START_CODE;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r12 = com.google.android.exoplayer2.util.NalUnitUtil.NAL_START_CODE;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r12 = r12.length;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        java.lang.System.arraycopy(r11, r3, r2, r9, r12);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r11 = com.google.android.exoplayer2.util.NalUnitUtil.NAL_START_CODE;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r11 = r11.length;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r9 = r9 + r11;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r11 = r13.data;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r12 = r13.getPosition();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        java.lang.System.arraycopy(r11, r12, r2, r9, r10);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r9 = r9 + r10;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r13.skipBytes(r10);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r7 = r7 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        goto L_0x0047;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r4 = r4 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r7 = r9;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        goto L_0x003c;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        if (r5 != 0) goto L_0x0071;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r13 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        goto L_0x0075;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r13 = java.util.Collections.singletonList(r2);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r1 = new com.google.android.exoplayer2.video.HevcConfig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r0 = r0 + r6;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        r1.<init>(r13, r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x007c }
        return r1;
        r13 = move-exception;
        r0 = new com.google.android.exoplayer2.ParserException;
        r1 = "Error parsing HEVC config";
        r0.<init>(r1, r13);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.HevcConfig.parse(com.google.android.exoplayer2.util.ParsableByteArray):com.google.android.exoplayer2.video.HevcConfig");
    }

    private HevcConfig(List<byte[]> list, int i) {
        this.initializationData = list;
        this.nalUnitLengthFieldLength = i;
    }
}
