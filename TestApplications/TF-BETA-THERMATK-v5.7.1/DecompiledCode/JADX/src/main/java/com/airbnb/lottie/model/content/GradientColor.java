package com.airbnb.lottie.model.content;

public class GradientColor {
    private final int[] colors;
    private final float[] positions;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:9:0x005c in {5, 6, 8} preds:[]
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
    public void lerp(com.airbnb.lottie.model.content.GradientColor r5, com.airbnb.lottie.model.content.GradientColor r6, float r7) {
        /*
        r4 = this;
        r0 = r5.colors;
        r0 = r0.length;
        r1 = r6.colors;
        r1 = r1.length;
        if (r0 != r1) goto L_0x0032;
        r0 = 0;
        r1 = r5.colors;
        r1 = r1.length;
        if (r0 >= r1) goto L_0x0031;
        r1 = r4.positions;
        r2 = r5.positions;
        r2 = r2[r0];
        r3 = r6.positions;
        r3 = r3[r0];
        r2 = com.airbnb.lottie.utils.MiscUtils.lerp(r2, r3, r7);
        r1[r0] = r2;
        r1 = r4.colors;
        r2 = r5.colors;
        r2 = r2[r0];
        r3 = r6.colors;
        r3 = r3[r0];
        r2 = com.airbnb.lottie.utils.GammaEvaluator.evaluate(r7, r2, r3);
        r1[r0] = r2;
        r0 = r0 + 1;
        goto L_0x0009;
        return;
        r7 = new java.lang.IllegalArgumentException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Cannot interpolate between gradients. Lengths vary (";
        r0.append(r1);
        r5 = r5.colors;
        r5 = r5.length;
        r0.append(r5);
        r5 = " vs ";
        r0.append(r5);
        r5 = r6.colors;
        r5 = r5.length;
        r0.append(r5);
        r5 = ")";
        r0.append(r5);
        r5 = r0.toString();
        r7.<init>(r5);
        throw r7;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.model.content.GradientColor.lerp(com.airbnb.lottie.model.content.GradientColor, com.airbnb.lottie.model.content.GradientColor, float):void");
    }

    public GradientColor(float[] fArr, int[] iArr) {
        this.positions = fArr;
        this.colors = iArr;
    }

    public float[] getPositions() {
        return this.positions;
    }

    public int[] getColors() {
        return this.colors;
    }

    public int getSize() {
        return this.colors.length;
    }
}
