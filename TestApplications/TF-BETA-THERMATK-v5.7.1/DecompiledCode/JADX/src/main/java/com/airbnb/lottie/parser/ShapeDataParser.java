package com.airbnb.lottie.parser;

import com.airbnb.lottie.model.content.ShapeData;

public class ShapeDataParser implements ValueParser<ShapeData> {
    public static final ShapeDataParser INSTANCE = new ShapeDataParser();

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:55:0x011b in {2, 14, 17, 20, 23, 26, 31, 32, 33, 34, 35, 38, 45, 48, 50, 52, 54} preds:[]
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
    public com.airbnb.lottie.model.content.ShapeData parse(android.util.JsonReader r13, float r14) throws java.io.IOException {
        /*
        r12 = this;
        r0 = r13.peek();
        r1 = android.util.JsonToken.BEGIN_ARRAY;
        if (r0 != r1) goto L_0x000b;
        r13.beginArray();
        r13.beginObject();
        r0 = 0;
        r1 = 0;
        r2 = r0;
        r3 = r2;
        r4 = 0;
        r5 = r13.hasNext();
        r6 = 1;
        if (r5 == 0) goto L_0x007a;
        r5 = r13.nextName();
        r7 = -1;
        r8 = r5.hashCode();
        r9 = 99;
        r10 = 3;
        r11 = 2;
        if (r8 == r9) goto L_0x0054;
        r9 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        if (r8 == r9) goto L_0x004a;
        r9 = 111; // 0x6f float:1.56E-43 double:5.5E-322;
        if (r8 == r9) goto L_0x0040;
        r9 = 118; // 0x76 float:1.65E-43 double:5.83E-322;
        if (r8 == r9) goto L_0x0036;
        goto L_0x005d;
        r8 = "v";
        r5 = r5.equals(r8);
        if (r5 == 0) goto L_0x005d;
        r7 = 1;
        goto L_0x005d;
        r8 = "o";
        r5 = r5.equals(r8);
        if (r5 == 0) goto L_0x005d;
        r7 = 3;
        goto L_0x005d;
        r8 = "i";
        r5 = r5.equals(r8);
        if (r5 == 0) goto L_0x005d;
        r7 = 2;
        goto L_0x005d;
        r8 = "c";
        r5 = r5.equals(r8);
        if (r5 == 0) goto L_0x005d;
        r7 = 0;
        if (r7 == 0) goto L_0x0075;
        if (r7 == r6) goto L_0x0070;
        if (r7 == r11) goto L_0x006b;
        if (r7 == r10) goto L_0x0066;
        goto L_0x0013;
        r3 = com.airbnb.lottie.parser.JsonUtils.jsonToPoints(r13, r14);
        goto L_0x0013;
        r2 = com.airbnb.lottie.parser.JsonUtils.jsonToPoints(r13, r14);
        goto L_0x0013;
        r0 = com.airbnb.lottie.parser.JsonUtils.jsonToPoints(r13, r14);
        goto L_0x0013;
        r4 = r13.nextBoolean();
        goto L_0x0013;
        r13.endObject();
        r14 = r13.peek();
        r5 = android.util.JsonToken.END_ARRAY;
        if (r14 != r5) goto L_0x0088;
        r13.endArray();
        if (r0 == 0) goto L_0x0113;
        if (r2 == 0) goto L_0x0113;
        if (r3 == 0) goto L_0x0113;
        r13 = r0.isEmpty();
        if (r13 == 0) goto L_0x00a3;
        r13 = new com.airbnb.lottie.model.content.ShapeData;
        r14 = new android.graphics.PointF;
        r14.<init>();
        r0 = java.util.Collections.emptyList();
        r13.<init>(r14, r1, r0);
        return r13;
        r13 = r0.size();
        r14 = r0.get(r1);
        r14 = (android.graphics.PointF) r14;
        r5 = new java.util.ArrayList;
        r5.<init>(r13);
        r7 = 1;
        if (r7 >= r13) goto L_0x00e2;
        r8 = r0.get(r7);
        r8 = (android.graphics.PointF) r8;
        r9 = r7 + -1;
        r10 = r0.get(r9);
        r10 = (android.graphics.PointF) r10;
        r9 = r3.get(r9);
        r9 = (android.graphics.PointF) r9;
        r11 = r2.get(r7);
        r11 = (android.graphics.PointF) r11;
        r9 = com.airbnb.lottie.utils.MiscUtils.addPoints(r10, r9);
        r10 = com.airbnb.lottie.utils.MiscUtils.addPoints(r8, r11);
        r11 = new com.airbnb.lottie.model.CubicCurveData;
        r11.<init>(r9, r10, r8);
        r5.add(r11);
        r7 = r7 + 1;
        goto L_0x00b3;
        if (r4 == 0) goto L_0x010d;
        r7 = r0.get(r1);
        r7 = (android.graphics.PointF) r7;
        r13 = r13 - r6;
        r0 = r0.get(r13);
        r0 = (android.graphics.PointF) r0;
        r13 = r3.get(r13);
        r13 = (android.graphics.PointF) r13;
        r1 = r2.get(r1);
        r1 = (android.graphics.PointF) r1;
        r13 = com.airbnb.lottie.utils.MiscUtils.addPoints(r0, r13);
        r0 = com.airbnb.lottie.utils.MiscUtils.addPoints(r7, r1);
        r1 = new com.airbnb.lottie.model.CubicCurveData;
        r1.<init>(r13, r0, r7);
        r5.add(r1);
        r13 = new com.airbnb.lottie.model.content.ShapeData;
        r13.<init>(r14, r4, r5);
        return r13;
        r13 = new java.lang.IllegalArgumentException;
        r14 = "Shape data was missing information.";
        r13.<init>(r14);
        throw r13;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.ShapeDataParser.parse(android.util.JsonReader, float):com.airbnb.lottie.model.content.ShapeData");
    }

    private ShapeDataParser() {
    }
}
