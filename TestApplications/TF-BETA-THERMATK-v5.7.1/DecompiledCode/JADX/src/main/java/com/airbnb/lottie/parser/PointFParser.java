package com.airbnb.lottie.parser;

import android.graphics.PointF;

public class PointFParser implements ValueParser<PointF> {
    public static final PointFParser INSTANCE = new PointFParser();

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:17:0x004f in {3, 7, 13, 14, 16} preds:[]
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
    public android.graphics.PointF parse(android.util.JsonReader r5, float r6) throws java.io.IOException {
        /*
        r4 = this;
        r0 = r5.peek();
        r1 = android.util.JsonToken.BEGIN_ARRAY;
        if (r0 != r1) goto L_0x000d;
        r5 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r5, r6);
        return r5;
        r1 = android.util.JsonToken.BEGIN_OBJECT;
        if (r0 != r1) goto L_0x0016;
        r5 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r5, r6);
        return r5;
        r1 = android.util.JsonToken.NUMBER;
        if (r0 != r1) goto L_0x0038;
        r0 = new android.graphics.PointF;
        r1 = r5.nextDouble();
        r1 = (float) r1;
        r1 = r1 * r6;
        r2 = r5.nextDouble();
        r2 = (float) r2;
        r2 = r2 * r6;
        r0.<init>(r1, r2);
        r6 = r5.hasNext();
        if (r6 == 0) goto L_0x0037;
        r5.skipValue();
        goto L_0x002d;
        return r0;
        r5 = new java.lang.IllegalArgumentException;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r1 = "Cannot convert json to point. Next token is ";
        r6.append(r1);
        r6.append(r0);
        r6 = r6.toString();
        r5.<init>(r6);
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.PointFParser.parse(android.util.JsonReader, float):android.graphics.PointF");
    }

    private PointFParser() {
    }
}
