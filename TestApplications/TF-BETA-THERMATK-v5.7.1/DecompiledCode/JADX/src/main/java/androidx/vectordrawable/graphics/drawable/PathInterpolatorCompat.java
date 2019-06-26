package androidx.vectordrawable.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.animation.Interpolator;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import org.xmlpull.v1.XmlPullParser;

public class PathInterpolatorCompat implements Interpolator {
    /* renamed from: mX */
    private float[] f7mX;
    /* renamed from: mY */
    private float[] f8mY;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:29:0x0112 in {4, 17, 19, 22, 24, 26, 28} preds:[]
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
    private void initPath(android.graphics.Path r11) {
        /*
        r10 = this;
        r0 = new android.graphics.PathMeasure;
        r1 = 0;
        r0.<init>(r11, r1);
        r11 = r0.getLength();
        r2 = 990057071; // 0x3b03126f float:0.002 double:4.89153186E-315;
        r2 = r11 / r2;
        r2 = (int) r2;
        r3 = 1;
        r2 = r2 + r3;
        r4 = 3000; // 0xbb8 float:4.204E-42 double:1.482E-320;
        r2 = java.lang.Math.min(r4, r2);
        if (r2 <= 0) goto L_0x00fb;
        r4 = new float[r2];
        r10.f7mX = r4;
        r4 = new float[r2];
        r10.f8mY = r4;
        r4 = 2;
        r4 = new float[r4];
        r5 = 0;
        if (r5 >= r2) goto L_0x0042;
        r6 = (float) r5;
        r6 = r6 * r11;
        r7 = r2 + -1;
        r7 = (float) r7;
        r6 = r6 / r7;
        r7 = 0;
        r0.getPosTan(r6, r4, r7);
        r6 = r10.f7mX;
        r7 = r4[r1];
        r6[r5] = r7;
        r6 = r10.f8mY;
        r7 = r4[r3];
        r6[r5] = r7;
        r5 = r5 + 1;
        goto L_0x0026;
        r11 = r10.f7mX;
        r11 = r11[r1];
        r11 = java.lang.Math.abs(r11);
        r4 = (double) r11;
        r6 = 4532020583610935537; // 0x3ee4f8b588e368f1 float:-1.3686737E-33 double:1.0E-5;
        r11 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r11 > 0) goto L_0x00bd;
        r11 = r10.f8mY;
        r11 = r11[r1];
        r11 = java.lang.Math.abs(r11);
        r4 = (double) r11;
        r11 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r11 > 0) goto L_0x00bd;
        r11 = r10.f7mX;
        r4 = r2 + -1;
        r11 = r11[r4];
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r11 = r11 - r5;
        r11 = java.lang.Math.abs(r11);
        r8 = (double) r11;
        r11 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
        if (r11 > 0) goto L_0x00bd;
        r11 = r10.f8mY;
        r11 = r11[r4];
        r11 = r11 - r5;
        r11 = java.lang.Math.abs(r11);
        r4 = (double) r11;
        r11 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r11 > 0) goto L_0x00bd;
        r11 = 0;
        r11 = 0;
        r3 = 0;
        if (r1 >= r2) goto L_0x00ae;
        r4 = r10.f7mX;
        r5 = r11 + 1;
        r11 = r4[r11];
        r3 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1));
        if (r3 < 0) goto L_0x0097;
        r4[r1] = r11;
        r1 = r1 + 1;
        r3 = r11;
        r11 = r5;
        goto L_0x0084;
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "The Path cannot loop back on itself, x :";
        r1.append(r2);
        r1.append(r11);
        r11 = r1.toString();
        r0.<init>(r11);
        throw r0;
        r11 = r0.nextContour();
        if (r11 != 0) goto L_0x00b5;
        return;
        r11 = new java.lang.IllegalArgumentException;
        r0 = "The Path should be continuous, can't have 2+ contours";
        r11.<init>(r0);
        throw r11;
        r11 = new java.lang.IllegalArgumentException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r4 = "The Path must start at (0,0) and end at (1,1) start: ";
        r0.append(r4);
        r4 = r10.f7mX;
        r4 = r4[r1];
        r0.append(r4);
        r4 = ",";
        r0.append(r4);
        r5 = r10.f8mY;
        r1 = r5[r1];
        r0.append(r1);
        r1 = " end:";
        r0.append(r1);
        r1 = r10.f7mX;
        r2 = r2 - r3;
        r1 = r1[r2];
        r0.append(r1);
        r0.append(r4);
        r1 = r10.f8mY;
        r1 = r1[r2];
        r0.append(r1);
        r0 = r0.toString();
        r11.<init>(r0);
        throw r11;
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "The Path has a invalid length ";
        r1.append(r2);
        r1.append(r11);
        r11 = r1.toString();
        r0.<init>(r11);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat.initPath(android.graphics.Path):void");
    }

    public PathInterpolatorCompat(Context context, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        this(context.getResources(), context.getTheme(), attributeSet, xmlPullParser);
    }

    public PathInterpolatorCompat(Resources resources, Theme theme, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_PATH_INTERPOLATOR);
        parseInterpolatorFromTypeArray(obtainAttributes, xmlPullParser);
        obtainAttributes.recycle();
    }

    private void parseInterpolatorFromTypeArray(TypedArray typedArray, XmlPullParser xmlPullParser) {
        String str = "pathData";
        if (TypedArrayUtils.hasAttribute(xmlPullParser, str)) {
            String namedString = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, str, 4);
            Path createPathFromPathData = PathParser.createPathFromPathData(namedString);
            if (createPathFromPathData != null) {
                initPath(createPathFromPathData);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The path is null, which is created from ");
            stringBuilder.append(namedString);
            throw new InflateException(stringBuilder.toString());
        }
        str = "controlX1";
        if (TypedArrayUtils.hasAttribute(xmlPullParser, str)) {
            String str2 = "controlY1";
            if (TypedArrayUtils.hasAttribute(xmlPullParser, str2)) {
                float namedFloat = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, str, 0, 0.0f);
                float namedFloat2 = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, str2, 1, 0.0f);
                String str3 = "controlX2";
                boolean hasAttribute = TypedArrayUtils.hasAttribute(xmlPullParser, str3);
                String str4 = "controlY2";
                if (hasAttribute != TypedArrayUtils.hasAttribute(xmlPullParser, str4)) {
                    throw new InflateException("pathInterpolator requires both controlX2 and controlY2 for cubic Beziers.");
                } else if (hasAttribute) {
                    initCubic(namedFloat, namedFloat2, TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, str3, 2, 0.0f), TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, str4, 3, 0.0f));
                    return;
                } else {
                    initQuad(namedFloat, namedFloat2);
                    return;
                }
            }
            throw new InflateException("pathInterpolator requires the controlY1 attribute");
        }
        throw new InflateException("pathInterpolator requires the controlX1 attribute");
    }

    private void initQuad(float f, float f2) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(f, f2, 1.0f, 1.0f);
        initPath(path);
    }

    private void initCubic(float f, float f2, float f3, float f4) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(f, f2, f3, f4, 1.0f, 1.0f);
        initPath(path);
    }

    public float getInterpolation(float f) {
        if (f <= 0.0f) {
            return 0.0f;
        }
        if (f >= 1.0f) {
            return 1.0f;
        }
        int i = 0;
        int length = this.f7mX.length - 1;
        while (length - i > 1) {
            int i2 = (i + length) / 2;
            if (f < this.f7mX[i2]) {
                length = i2;
            } else {
                i = i2;
            }
        }
        float[] fArr = this.f7mX;
        float f2 = fArr[length] - fArr[i];
        if (f2 == 0.0f) {
            return this.f8mY[i];
        }
        f = (f - fArr[i]) / f2;
        float[] fArr2 = this.f8mY;
        float f3 = fArr2[i];
        return f3 + (f * (fArr2[length] - f3));
    }
}
