package com.google.android.exoplayer2.video;

import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.List;

public final class AvcConfig {
    public final int height;
    public final List<byte[]> initializationData;
    public final int nalUnitLengthFieldLength;
    public final float pixelWidthAspectRatio;
    public final int width;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:21:0x0072 in {6, 9, 12, 13, 15, 17, 20} preds:[]
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
    public static com.google.android.exoplayer2.video.AvcConfig parse(com.google.android.exoplayer2.util.ParsableByteArray r8) throws com.google.android.exoplayer2.ParserException {
        /*
        r0 = 4;
        r8.skipBytes(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r0 = r8.readUnsignedByte();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r1 = 3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r0 = r0 & r1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r4 = r0 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        if (r4 == r1) goto L_0x0063;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r3 = new java.util.ArrayList;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r3.<init>();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r0 = r8.readUnsignedByte();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r0 = r0 & 31;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r1 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r2 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        if (r2 >= r0) goto L_0x0027;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r5 = buildNalUnitForChild(r8);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r3.add(r5);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r2 = r2 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        goto L_0x001b;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r2 = r8.readUnsignedByte();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r5 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        if (r5 >= r2) goto L_0x0038;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r6 = buildNalUnitForChild(r8);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r3.add(r6);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r5 = r5 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        goto L_0x002c;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r2 = -1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        if (r0 <= 0) goto L_0x0058;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r8 = r3.get(r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r8 = (byte[]) r8;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r0 = r3.get(r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r0 = (byte[]) r0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r8 = r8.length;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r8 = com.google.android.exoplayer2.util.NalUnitUtil.parseSpsNalUnit(r0, r4, r8);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r0 = r8.width;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r1 = r8.height;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r8 = r8.pixelWidthAspectRatio;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r7 = r8;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r5 = r0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r6 = r1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        goto L_0x005c;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r5 = -1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r6 = -1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r8 = new com.google.android.exoplayer2.video.AvcConfig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r2 = r8;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r2.<init>(r3, r4, r5, r6, r7);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        return r8;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r8 = new java.lang.IllegalStateException;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r8.<init>();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        throw r8;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0069 }
        r8 = move-exception;
        r0 = new com.google.android.exoplayer2.ParserException;
        r1 = "Error parsing AVC config";
        r0.<init>(r1, r8);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.AvcConfig.parse(com.google.android.exoplayer2.util.ParsableByteArray):com.google.android.exoplayer2.video.AvcConfig");
    }

    private AvcConfig(List<byte[]> list, int i, int i2, int i3, float f) {
        this.initializationData = list;
        this.nalUnitLengthFieldLength = i;
        this.width = i2;
        this.height = i3;
        this.pixelWidthAspectRatio = f;
    }

    private static byte[] buildNalUnitForChild(ParsableByteArray parsableByteArray) {
        int readUnsignedShort = parsableByteArray.readUnsignedShort();
        int position = parsableByteArray.getPosition();
        parsableByteArray.skipBytes(readUnsignedShort);
        return CodecSpecificDataUtil.buildNalUnit(parsableByteArray.data, position, readUnsignedShort);
    }
}
