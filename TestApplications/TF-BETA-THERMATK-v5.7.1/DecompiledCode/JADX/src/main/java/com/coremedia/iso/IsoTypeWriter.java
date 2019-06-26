package com.coremedia.iso;

import com.google.android.exoplayer2.util.NalUnitUtil;
import java.nio.ByteBuffer;
import org.telegram.p004ui.ActionBar.Theme;

public final class IsoTypeWriter {
    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:9:0x003a in {5, 6, 8} preds:[]
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
    public static void writeIso639(java.nio.ByteBuffer r5, java.lang.String r6) {
        /*
        r0 = r6.getBytes();
        r0 = r0.length;
        r1 = 3;
        if (r0 != r1) goto L_0x0021;
        r0 = 0;
        r2 = 0;
        if (r0 < r1) goto L_0x0010;
        writeUInt16(r5, r2);
        return;
        r3 = r6.getBytes();
        r3 = r3[r0];
        r3 = r3 + -96;
        r4 = 2 - r0;
        r4 = r4 * 5;
        r3 = r3 << r4;
        r2 = r2 + r3;
        r0 = r0 + 1;
        goto L_0x000a;
        r5 = new java.lang.IllegalArgumentException;
        r0 = new java.lang.StringBuilder;
        r1 = "\"";
        r0.<init>(r1);
        r0.append(r6);
        r6 = "\" language string isn't exactly 3 characters long!";
        r0.append(r6);
        r6 = r0.toString();
        r5.<init>(r6);
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.coremedia.iso.IsoTypeWriter.writeIso639(java.nio.ByteBuffer, java.lang.String):void");
    }

    public static void writeUInt64(ByteBuffer byteBuffer, long j) {
        byteBuffer.putLong(j);
    }

    public static void writeUInt32(ByteBuffer byteBuffer, long j) {
        byteBuffer.putInt((int) j);
    }

    public static void writeUInt24(ByteBuffer byteBuffer, int i) {
        i &= 16777215;
        writeUInt16(byteBuffer, i >> 8);
        writeUInt8(byteBuffer, i);
    }

    public static void writeUInt16(ByteBuffer byteBuffer, int i) {
        i &= 65535;
        writeUInt8(byteBuffer, i >> 8);
        writeUInt8(byteBuffer, i & NalUnitUtil.EXTENDED_SAR);
    }

    public static void writeUInt8(ByteBuffer byteBuffer, int i) {
        byteBuffer.put((byte) (i & NalUnitUtil.EXTENDED_SAR));
    }

    public static void writeFixedPoint1616(ByteBuffer byteBuffer, double d) {
        int i = (int) (d * 65536.0d);
        byteBuffer.put((byte) ((Theme.ACTION_BAR_VIDEO_EDIT_COLOR & i) >> 24));
        byteBuffer.put((byte) ((16711680 & i) >> 16));
        byteBuffer.put((byte) ((65280 & i) >> 8));
        byteBuffer.put((byte) (i & NalUnitUtil.EXTENDED_SAR));
    }

    public static void writeFixedPoint0230(ByteBuffer byteBuffer, double d) {
        int i = (int) (d * 1.073741824E9d);
        byteBuffer.put((byte) ((Theme.ACTION_BAR_VIDEO_EDIT_COLOR & i) >> 24));
        byteBuffer.put((byte) ((16711680 & i) >> 16));
        byteBuffer.put((byte) ((65280 & i) >> 8));
        byteBuffer.put((byte) (i & NalUnitUtil.EXTENDED_SAR));
    }

    public static void writeFixedPoint88(ByteBuffer byteBuffer, double d) {
        short s = (short) ((int) (d * 256.0d));
        byteBuffer.put((byte) ((65280 & s) >> 8));
        byteBuffer.put((byte) (s & NalUnitUtil.EXTENDED_SAR));
    }

    public static void writeUtf8String(ByteBuffer byteBuffer, String str) {
        byteBuffer.put(Utf8.convert(str));
        writeUInt8(byteBuffer, 0);
    }
}
