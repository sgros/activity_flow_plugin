package com.airbnb.lottie.parser;

import android.support.p001v4.util.SparseArrayCompat;
import android.util.JsonReader;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.value.Keyframe;
import java.io.IOException;
import java.lang.ref.WeakReference;

class KeyframeParser {
    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    private static SparseArrayCompat<WeakReference<Interpolator>> pathInterpolatorCache;

    KeyframeParser() {
    }

    private static SparseArrayCompat<WeakReference<Interpolator>> pathInterpolatorCache() {
        if (pathInterpolatorCache == null) {
            pathInterpolatorCache = new SparseArrayCompat();
        }
        return pathInterpolatorCache;
    }

    private static WeakReference<Interpolator> getInterpolator(int i) {
        WeakReference weakReference;
        synchronized (KeyframeParser.class) {
            weakReference = (WeakReference) pathInterpolatorCache().get(i);
        }
        return weakReference;
    }

    private static void putInterpolator(int i, WeakReference<Interpolator> weakReference) {
        synchronized (KeyframeParser.class) {
            pathInterpolatorCache.put(i, weakReference);
        }
    }

    static <T> Keyframe<T> parse(JsonReader jsonReader, LottieComposition lottieComposition, float f, ValueParser<T> valueParser, boolean z) throws IOException {
        if (z) {
            return parseKeyframe(lottieComposition, jsonReader, f, valueParser);
        }
        return parseStaticValue(jsonReader, f, valueParser);
    }

    private static <T> com.airbnb.lottie.value.Keyframe<T> parseKeyframe(com.airbnb.lottie.LottieComposition r16, android.util.JsonReader r17, float r18, com.airbnb.lottie.parser.ValueParser<T> r19) throws java.io.IOException {
        /*
        r0 = r17;
        r1 = r18;
        r2 = r19;
        r17.beginObject();
        r4 = 0;
        r5 = 0;
        r6 = r4;
        r7 = r6;
        r8 = r7;
        r9 = r8;
        r13 = r9;
        r14 = r13;
        r5 = 0;
        r11 = 0;
    L_0x0013:
        r10 = r17.hasNext();
        if (r10 == 0) goto L_0x00ba;
    L_0x0019:
        r10 = r17.nextName();
        r12 = -1;
        r15 = r10.hashCode();
        r3 = 1;
        switch(r15) {
            case 101: goto L_0x006d;
            case 104: goto L_0x0063;
            case 105: goto L_0x0059;
            case 111: goto L_0x004f;
            case 115: goto L_0x0045;
            case 116: goto L_0x003b;
            case 3701: goto L_0x0031;
            case 3707: goto L_0x0027;
            default: goto L_0x0026;
        };
    L_0x0026:
        goto L_0x0077;
    L_0x0027:
        r15 = "to";
        r10 = r10.equals(r15);
        if (r10 == 0) goto L_0x0077;
    L_0x002f:
        r10 = 6;
        goto L_0x0078;
    L_0x0031:
        r15 = "ti";
        r10 = r10.equals(r15);
        if (r10 == 0) goto L_0x0077;
    L_0x0039:
        r10 = 7;
        goto L_0x0078;
    L_0x003b:
        r15 = "t";
        r10 = r10.equals(r15);
        if (r10 == 0) goto L_0x0077;
    L_0x0043:
        r10 = 0;
        goto L_0x0078;
    L_0x0045:
        r15 = "s";
        r10 = r10.equals(r15);
        if (r10 == 0) goto L_0x0077;
    L_0x004d:
        r10 = 1;
        goto L_0x0078;
    L_0x004f:
        r15 = "o";
        r10 = r10.equals(r15);
        if (r10 == 0) goto L_0x0077;
    L_0x0057:
        r10 = 3;
        goto L_0x0078;
    L_0x0059:
        r15 = "i";
        r10 = r10.equals(r15);
        if (r10 == 0) goto L_0x0077;
    L_0x0061:
        r10 = 4;
        goto L_0x0078;
    L_0x0063:
        r15 = "h";
        r10 = r10.equals(r15);
        if (r10 == 0) goto L_0x0077;
    L_0x006b:
        r10 = 5;
        goto L_0x0078;
    L_0x006d:
        r15 = "e";
        r10 = r10.equals(r15);
        if (r10 == 0) goto L_0x0077;
    L_0x0075:
        r10 = 2;
        goto L_0x0078;
    L_0x0077:
        r10 = -1;
    L_0x0078:
        switch(r10) {
            case 0: goto L_0x00b2;
            case 1: goto L_0x00ab;
            case 2: goto L_0x00a4;
            case 3: goto L_0x009d;
            case 4: goto L_0x0096;
            case 5: goto L_0x008b;
            case 6: goto L_0x0085;
            case 7: goto L_0x007f;
            default: goto L_0x007b;
        };
    L_0x007b:
        r17.skipValue();
        goto L_0x0013;
    L_0x007f:
        r3 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r17, r18);
        r14 = r3;
        goto L_0x0013;
    L_0x0085:
        r3 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r17, r18);
        r13 = r3;
        goto L_0x0013;
    L_0x008b:
        r5 = r17.nextInt();
        if (r5 != r3) goto L_0x0092;
    L_0x0091:
        goto L_0x0093;
    L_0x0092:
        r3 = 0;
    L_0x0093:
        r5 = r3;
        goto L_0x0013;
    L_0x0096:
        r3 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r17, r18);
        r7 = r3;
        goto L_0x0013;
    L_0x009d:
        r3 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r17, r18);
        r6 = r3;
        goto L_0x0013;
    L_0x00a4:
        r3 = r2.parse(r0, r1);
        r9 = r3;
        goto L_0x0013;
    L_0x00ab:
        r3 = r2.parse(r0, r1);
        r8 = r3;
        goto L_0x0013;
    L_0x00b2:
        r10 = r17.nextDouble();
        r3 = (float) r10;
        r11 = r3;
        goto L_0x0013;
    L_0x00ba:
        r17.endObject();
        if (r5 == 0) goto L_0x00c4;
    L_0x00bf:
        r0 = LINEAR_INTERPOLATOR;
        r10 = r0;
        r9 = r8;
        goto L_0x0127;
    L_0x00c4:
        if (r6 == 0) goto L_0x0124;
    L_0x00c6:
        if (r7 == 0) goto L_0x0124;
    L_0x00c8:
        r0 = r6.x;
        r2 = -r1;
        r0 = com.airbnb.lottie.utils.MiscUtils.clamp(r0, r2, r1);
        r6.x = r0;
        r0 = r6.y;
        r3 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r5 = -1027080192; // 0xffffffffc2c80000 float:-100.0 double:NaN;
        r0 = com.airbnb.lottie.utils.MiscUtils.clamp(r0, r5, r3);
        r6.y = r0;
        r0 = r7.x;
        r0 = com.airbnb.lottie.utils.MiscUtils.clamp(r0, r2, r1);
        r7.x = r0;
        r0 = r7.y;
        r0 = com.airbnb.lottie.utils.MiscUtils.clamp(r0, r5, r3);
        r7.y = r0;
        r0 = r6.x;
        r2 = r6.y;
        r3 = r7.x;
        r5 = r7.y;
        r0 = com.airbnb.lottie.utils.Utils.hashFor(r0, r2, r3, r5);
        r2 = getInterpolator(r0);
        if (r2 == 0) goto L_0x0106;
    L_0x00ff:
        r3 = r2.get();
        r4 = r3;
        r4 = (android.view.animation.Interpolator) r4;
    L_0x0106:
        if (r2 == 0) goto L_0x010a;
    L_0x0108:
        if (r4 != 0) goto L_0x0122;
    L_0x010a:
        r2 = r6.x;
        r2 = r2 / r1;
        r3 = r6.y;
        r3 = r3 / r1;
        r4 = r7.x;
        r4 = r4 / r1;
        r5 = r7.y;
        r5 = r5 / r1;
        r4 = android.support.p001v4.view.animation.PathInterpolatorCompat.create(r2, r3, r4, r5);
        r1 = new java.lang.ref.WeakReference;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0122 }
        r1.<init>(r4);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0122 }
        putInterpolator(r0, r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0122 }
    L_0x0122:
        r10 = r4;
        goto L_0x0127;
    L_0x0124:
        r0 = LINEAR_INTERPOLATOR;
        r10 = r0;
    L_0x0127:
        r0 = new com.airbnb.lottie.value.Keyframe;
        r12 = 0;
        r6 = r0;
        r7 = r16;
        r6.<init>(r7, r8, r9, r10, r11, r12);
        r0.pathCp1 = r13;
        r0.pathCp2 = r14;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.KeyframeParser.parseKeyframe(com.airbnb.lottie.LottieComposition, android.util.JsonReader, float, com.airbnb.lottie.parser.ValueParser):com.airbnb.lottie.value.Keyframe");
    }

    private static <T> Keyframe<T> parseStaticValue(JsonReader jsonReader, float f, ValueParser<T> valueParser) throws IOException {
        return new Keyframe(valueParser.parse(jsonReader, f));
    }
}
