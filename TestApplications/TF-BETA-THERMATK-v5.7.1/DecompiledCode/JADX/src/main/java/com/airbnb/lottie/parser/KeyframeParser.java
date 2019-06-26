package com.airbnb.lottie.parser;

import android.util.JsonReader;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.collection.SparseArrayCompat;
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

    /* JADX WARNING: Removed duplicated region for block: B:45:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x009c  */
    private static <T> com.airbnb.lottie.value.Keyframe<T> parseKeyframe(com.airbnb.lottie.LottieComposition r17, android.util.JsonReader r18, float r19, com.airbnb.lottie.parser.ValueParser<T> r20) throws java.io.IOException {
        /*
        r0 = r18;
        r1 = r19;
        r2 = r20;
        r18.beginObject();
        r5 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r12 = 0;
        r14 = 0;
        r15 = 0;
    L_0x0012:
        r10 = r18.hasNext();
        if (r10 == 0) goto L_0x00d9;
    L_0x0018:
        r10 = r18.nextName();
        r11 = -1;
        r13 = r10.hashCode();
        r3 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r4 = 1;
        if (r13 == r3) goto L_0x0089;
    L_0x0026:
        r3 = 111; // 0x6f float:1.56E-43 double:5.5E-322;
        if (r13 == r3) goto L_0x007f;
    L_0x002a:
        r3 = 3701; // 0xe75 float:5.186E-42 double:1.8285E-320;
        if (r13 == r3) goto L_0x0075;
    L_0x002e:
        r3 = 3707; // 0xe7b float:5.195E-42 double:1.8315E-320;
        if (r13 == r3) goto L_0x006b;
    L_0x0032:
        r3 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        if (r13 == r3) goto L_0x0061;
    L_0x0036:
        r3 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        if (r13 == r3) goto L_0x0057;
    L_0x003a:
        r3 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        if (r13 == r3) goto L_0x004d;
    L_0x003e:
        r3 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        if (r13 == r3) goto L_0x0043;
    L_0x0042:
        goto L_0x0093;
    L_0x0043:
        r3 = "t";
        r3 = r10.equals(r3);
        if (r3 == 0) goto L_0x0093;
    L_0x004b:
        r3 = 0;
        goto L_0x0094;
    L_0x004d:
        r3 = "s";
        r3 = r10.equals(r3);
        if (r3 == 0) goto L_0x0093;
    L_0x0055:
        r3 = 1;
        goto L_0x0094;
    L_0x0057:
        r3 = "i";
        r3 = r10.equals(r3);
        if (r3 == 0) goto L_0x0093;
    L_0x005f:
        r3 = 4;
        goto L_0x0094;
    L_0x0061:
        r3 = "h";
        r3 = r10.equals(r3);
        if (r3 == 0) goto L_0x0093;
    L_0x0069:
        r3 = 5;
        goto L_0x0094;
    L_0x006b:
        r3 = "to";
        r3 = r10.equals(r3);
        if (r3 == 0) goto L_0x0093;
    L_0x0073:
        r3 = 6;
        goto L_0x0094;
    L_0x0075:
        r3 = "ti";
        r3 = r10.equals(r3);
        if (r3 == 0) goto L_0x0093;
    L_0x007d:
        r3 = 7;
        goto L_0x0094;
    L_0x007f:
        r3 = "o";
        r3 = r10.equals(r3);
        if (r3 == 0) goto L_0x0093;
    L_0x0087:
        r3 = 3;
        goto L_0x0094;
    L_0x0089:
        r3 = "e";
        r3 = r10.equals(r3);
        if (r3 == 0) goto L_0x0093;
    L_0x0091:
        r3 = 2;
        goto L_0x0094;
    L_0x0093:
        r3 = -1;
    L_0x0094:
        switch(r3) {
            case 0: goto L_0x00d1;
            case 1: goto L_0x00ca;
            case 2: goto L_0x00c3;
            case 3: goto L_0x00bc;
            case 4: goto L_0x00b5;
            case 5: goto L_0x00aa;
            case 6: goto L_0x00a3;
            case 7: goto L_0x009c;
            default: goto L_0x0097;
        };
    L_0x0097:
        r18.skipValue();
        goto L_0x0012;
    L_0x009c:
        r3 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r18, r19);
        r15 = r3;
        goto L_0x0012;
    L_0x00a3:
        r3 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r18, r19);
        r14 = r3;
        goto L_0x0012;
    L_0x00aa:
        r3 = r18.nextInt();
        if (r3 != r4) goto L_0x00b1;
    L_0x00b0:
        goto L_0x00b2;
    L_0x00b1:
        r4 = 0;
    L_0x00b2:
        r5 = r4;
        goto L_0x0012;
    L_0x00b5:
        r3 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r18, r19);
        r7 = r3;
        goto L_0x0012;
    L_0x00bc:
        r3 = com.airbnb.lottie.parser.JsonUtils.jsonToPoint(r18, r19);
        r6 = r3;
        goto L_0x0012;
    L_0x00c3:
        r3 = r2.parse(r0, r1);
        r8 = r3;
        goto L_0x0012;
    L_0x00ca:
        r3 = r2.parse(r0, r1);
        r9 = r3;
        goto L_0x0012;
    L_0x00d1:
        r3 = r18.nextDouble();
        r3 = (float) r3;
        r12 = r3;
        goto L_0x0012;
    L_0x00d9:
        r18.endObject();
        if (r5 == 0) goto L_0x00e4;
    L_0x00de:
        r0 = LINEAR_INTERPOLATOR;
        r11 = r0;
        r10 = r9;
        goto L_0x0151;
    L_0x00e4:
        if (r6 == 0) goto L_0x014d;
    L_0x00e6:
        if (r7 == 0) goto L_0x014d;
    L_0x00e8:
        r0 = r6.x;
        r2 = -r1;
        r0 = com.airbnb.lottie.utils.MiscUtils.clamp(r0, r2, r1);
        r6.x = r0;
        r0 = r6.y;
        r3 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r4 = -1027080192; // 0xffffffffc2c80000 float:-100.0 double:NaN;
        r0 = com.airbnb.lottie.utils.MiscUtils.clamp(r0, r4, r3);
        r6.y = r0;
        r0 = r7.x;
        r0 = com.airbnb.lottie.utils.MiscUtils.clamp(r0, r2, r1);
        r7.x = r0;
        r0 = r7.y;
        r0 = com.airbnb.lottie.utils.MiscUtils.clamp(r0, r4, r3);
        r7.y = r0;
        r0 = r6.x;
        r2 = r6.y;
        r3 = r7.x;
        r4 = r7.y;
        r0 = com.airbnb.lottie.utils.Utils.hashFor(r0, r2, r3, r4);
        r2 = getInterpolator(r0);
        if (r2 == 0) goto L_0x0129;
    L_0x011f:
        r3 = r2.get();
        r4 = r3;
        r4 = (android.view.animation.Interpolator) r4;
        r16 = r4;
        goto L_0x012b;
    L_0x0129:
        r16 = 0;
    L_0x012b:
        if (r2 == 0) goto L_0x012f;
    L_0x012d:
        if (r16 != 0) goto L_0x0149;
    L_0x012f:
        r2 = r6.x;
        r2 = r2 / r1;
        r3 = r6.y;
        r3 = r3 / r1;
        r4 = r7.x;
        r4 = r4 / r1;
        r5 = r7.y;
        r5 = r5 / r1;
        r1 = androidx.core.view.animation.PathInterpolatorCompat.create(r2, r3, r4, r5);
        r2 = new java.lang.ref.WeakReference;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0147 }
        r2.<init>(r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0147 }
        putInterpolator(r0, r2);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0147 }
    L_0x0147:
        r16 = r1;
    L_0x0149:
        r10 = r8;
        r11 = r16;
        goto L_0x0151;
    L_0x014d:
        r0 = LINEAR_INTERPOLATOR;
        r11 = r0;
        r10 = r8;
    L_0x0151:
        r0 = new com.airbnb.lottie.value.Keyframe;
        r13 = 0;
        r7 = r0;
        r8 = r17;
        r7.<init>(r8, r9, r10, r11, r12, r13);
        r0.pathCp1 = r14;
        r0.pathCp2 = r15;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.KeyframeParser.parseKeyframe(com.airbnb.lottie.LottieComposition, android.util.JsonReader, float, com.airbnb.lottie.parser.ValueParser):com.airbnb.lottie.value.Keyframe");
    }

    private static <T> Keyframe<T> parseStaticValue(JsonReader jsonReader, float f, ValueParser<T> valueParser) throws IOException {
        return new Keyframe(valueParser.parse(jsonReader, f));
    }
}
