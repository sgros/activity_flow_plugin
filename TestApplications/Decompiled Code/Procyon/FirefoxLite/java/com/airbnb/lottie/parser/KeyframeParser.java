// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import android.graphics.PointF;
import android.support.v4.view.animation.PathInterpolatorCompat;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.utils.MiscUtils;
import java.io.IOException;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;
import android.view.animation.LinearInterpolator;
import java.lang.ref.WeakReference;
import android.support.v4.util.SparseArrayCompat;
import android.view.animation.Interpolator;

class KeyframeParser
{
    private static final Interpolator LINEAR_INTERPOLATOR;
    private static SparseArrayCompat<WeakReference<Interpolator>> pathInterpolatorCache;
    
    static {
        LINEAR_INTERPOLATOR = (Interpolator)new LinearInterpolator();
    }
    
    private static WeakReference<Interpolator> getInterpolator(final int n) {
        synchronized (KeyframeParser.class) {
            return pathInterpolatorCache().get(n);
        }
    }
    
    static <T> Keyframe<T> parse(final JsonReader jsonReader, final LottieComposition lottieComposition, final float n, final ValueParser<T> valueParser, final boolean b) throws IOException {
        if (b) {
            return parseKeyframe(lottieComposition, jsonReader, n, valueParser);
        }
        return parseStaticValue(jsonReader, n, valueParser);
    }
    
    private static <T> Keyframe<T> parseKeyframe(LottieComposition lottieComposition, final JsonReader jsonReader, final float n, ValueParser<T> create) throws IOException {
        jsonReader.beginObject();
        final Interpolator interpolator = null;
        PointF jsonToPoint2;
        PointF jsonToPoint = jsonToPoint2 = null;
        PointF parse2;
        PointF parse = parse2 = jsonToPoint2;
        PointF jsonToPoint4;
        PointF jsonToPoint3 = jsonToPoint4 = parse2;
        int n2 = 0;
        float n3 = 0.0f;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            final int n4 = 1;
            int n5 = 0;
            Label_0272: {
                switch (hashCode) {
                    case 3707: {
                        if (nextName.equals("to")) {
                            n5 = 6;
                            break Label_0272;
                        }
                        break;
                    }
                    case 3701: {
                        if (nextName.equals("ti")) {
                            n5 = 7;
                            break Label_0272;
                        }
                        break;
                    }
                    case 116: {
                        if (nextName.equals("t")) {
                            n5 = 0;
                            break Label_0272;
                        }
                        break;
                    }
                    case 115: {
                        if (nextName.equals("s")) {
                            n5 = 1;
                            break Label_0272;
                        }
                        break;
                    }
                    case 111: {
                        if (nextName.equals("o")) {
                            n5 = 3;
                            break Label_0272;
                        }
                        break;
                    }
                    case 105: {
                        if (nextName.equals("i")) {
                            n5 = 4;
                            break Label_0272;
                        }
                        break;
                    }
                    case 104: {
                        if (nextName.equals("h")) {
                            n5 = 5;
                            break Label_0272;
                        }
                        break;
                    }
                    case 101: {
                        if (nextName.equals("e")) {
                            n5 = 2;
                            break Label_0272;
                        }
                        break;
                    }
                }
                n5 = -1;
            }
            switch (n5) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 7: {
                    jsonToPoint4 = JsonUtils.jsonToPoint(jsonReader, n);
                    continue;
                }
                case 6: {
                    jsonToPoint3 = JsonUtils.jsonToPoint(jsonReader, n);
                    continue;
                }
                case 5: {
                    int n6;
                    if (jsonReader.nextInt() == 1) {
                        n6 = n4;
                    }
                    else {
                        n6 = 0;
                    }
                    n2 = n6;
                    continue;
                }
                case 4: {
                    jsonToPoint2 = JsonUtils.jsonToPoint(jsonReader, n);
                    continue;
                }
                case 3: {
                    jsonToPoint = JsonUtils.jsonToPoint(jsonReader, n);
                    continue;
                }
                case 2: {
                    parse2 = ((ValueParser<PointF>)create).parse(jsonReader, n);
                    continue;
                }
                case 1: {
                    parse = ((ValueParser<PointF>)create).parse(jsonReader, n);
                    continue;
                }
                case 0: {
                    n3 = (float)jsonReader.nextDouble();
                    continue;
                }
            }
        }
        jsonReader.endObject();
    Label_0635_Outer:
        while (true) {
            if (n2 != 0) {
                final Interpolator interpolator2 = KeyframeParser.LINEAR_INTERPOLATOR;
                parse2 = parse;
                break Label_0644;
            }
            Label_0640: {
                if (jsonToPoint == null || jsonToPoint2 == null) {
                    break Label_0640;
                }
                final float x = jsonToPoint.x;
                final float n7 = -n;
                jsonToPoint.x = MiscUtils.clamp(x, n7, n);
                jsonToPoint.y = MiscUtils.clamp(jsonToPoint.y, -100.0f, 100.0f);
                jsonToPoint2.x = MiscUtils.clamp(jsonToPoint2.x, n7, n);
                jsonToPoint2.y = MiscUtils.clamp(jsonToPoint2.y, -100.0f, 100.0f);
                final int hash = Utils.hashFor(jsonToPoint.x, jsonToPoint.y, jsonToPoint2.x, jsonToPoint2.y);
                final WeakReference<Interpolator> interpolator3 = getInterpolator(hash);
                Interpolator interpolator4 = interpolator;
                if (interpolator3 != null) {
                    interpolator4 = interpolator3.get();
                }
                while (true) {
                    if (interpolator3 != null && (create = interpolator4) != null) {
                        break Label_0635;
                    }
                    create = PathInterpolatorCompat.create(jsonToPoint.x / n, jsonToPoint.y / n, jsonToPoint2.x / n, jsonToPoint2.y / n);
                    try {
                        putInterpolator(hash, new WeakReference<Interpolator>(create));
                        Interpolator interpolator2 = create;
                        lottieComposition = (LottieComposition)new Keyframe(lottieComposition, parse, parse2, interpolator2, n3, null);
                        ((Keyframe)lottieComposition).pathCp1 = jsonToPoint3;
                        ((Keyframe)lottieComposition).pathCp2 = jsonToPoint4;
                        return (Keyframe<T>)lottieComposition;
                        interpolator2 = KeyframeParser.LINEAR_INTERPOLATOR;
                        continue Label_0635_Outer;
                    }
                    catch (ArrayIndexOutOfBoundsException ex) {
                        continue;
                    }
                    break;
                }
            }
            break;
        }
    }
    
    private static <T> Keyframe<T> parseStaticValue(final JsonReader jsonReader, final float n, final ValueParser<T> valueParser) throws IOException {
        return new Keyframe<T>(valueParser.parse(jsonReader, n));
    }
    
    private static SparseArrayCompat<WeakReference<Interpolator>> pathInterpolatorCache() {
        if (KeyframeParser.pathInterpolatorCache == null) {
            KeyframeParser.pathInterpolatorCache = new SparseArrayCompat<WeakReference<Interpolator>>();
        }
        return KeyframeParser.pathInterpolatorCache;
    }
    
    private static void putInterpolator(final int n, final WeakReference<Interpolator> weakReference) {
        synchronized (KeyframeParser.class) {
            KeyframeParser.pathInterpolatorCache.put(n, weakReference);
        }
    }
}
