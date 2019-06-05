// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import android.graphics.PointF;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableScaleValue;
import com.airbnb.lottie.value.ScaleXY;
import com.airbnb.lottie.model.animatable.AnimatablePathValue;
import android.util.Log;
import android.util.JsonToken;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

public class AnimatableTransformParser
{
    public static AnimatableTransform parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        final boolean b = jsonReader.peek() == JsonToken.BEGIN_OBJECT;
        if (b) {
            jsonReader.beginObject();
        }
        final AnimatablePathValue animatablePathValue = null;
        Object integer;
        final Object o = integer = null;
        final Object o2;
        final AnimatableIntegerValue animatableIntegerValue = (AnimatableIntegerValue)(o2 = integer);
        Object float2;
        AnimatableFloatValue float1 = (AnimatableFloatValue)(float2 = o2);
        AnimatableFloatValue float3 = (AnimatableFloatValue)o2;
        Object splitPath = animatableIntegerValue;
        Object scale = o;
        AnimatablePathValue parse = animatablePathValue;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n = 0;
            Label_0292: {
                switch (nextName.hashCode()) {
                    case 3676: {
                        if (nextName.equals("so")) {
                            n = 6;
                            break Label_0292;
                        }
                        break;
                    }
                    case 3656: {
                        if (nextName.equals("rz")) {
                            n = 3;
                            break Label_0292;
                        }
                        break;
                    }
                    case 3242: {
                        if (nextName.equals("eo")) {
                            n = 7;
                            break Label_0292;
                        }
                        break;
                    }
                    case 115: {
                        if (nextName.equals("s")) {
                            n = 2;
                            break Label_0292;
                        }
                        break;
                    }
                    case 114: {
                        if (nextName.equals("r")) {
                            n = 4;
                            break Label_0292;
                        }
                        break;
                    }
                    case 112: {
                        if (nextName.equals("p")) {
                            n = 1;
                            break Label_0292;
                        }
                        break;
                    }
                    case 111: {
                        if (nextName.equals("o")) {
                            n = 5;
                            break Label_0292;
                        }
                        break;
                    }
                    case 97: {
                        if (nextName.equals("a")) {
                            n = 0;
                            break Label_0292;
                        }
                        break;
                    }
                }
                n = -1;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 7: {
                    float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 6: {
                    float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 5: {
                    integer = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
                    continue;
                }
                case 3: {
                    lottieComposition.addWarning("Lottie doesn't support 3D layers.");
                }
                case 4: {
                    float3 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 2: {
                    scale = AnimatableValueParser.parseScale(jsonReader, lottieComposition);
                    continue;
                }
                case 1: {
                    splitPath = AnimatablePathValueParser.parseSplitPath(jsonReader, lottieComposition);
                    continue;
                }
                case 0: {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        if (jsonReader.nextName().equals("k")) {
                            parse = AnimatablePathValueParser.parse(jsonReader, lottieComposition);
                        }
                        else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                    continue;
                }
            }
        }
        if (b) {
            jsonReader.endObject();
        }
        AnimatablePathValue animatablePathValue2;
        if ((animatablePathValue2 = parse) == null) {
            Log.w("LOTTIE", "Layer has no transform property. You may be using an unsupported layer type such as a camera.");
            animatablePathValue2 = new AnimatablePathValue();
        }
        AnimatableScaleValue animatableScaleValue;
        if ((animatableScaleValue = (AnimatableScaleValue)scale) == null) {
            animatableScaleValue = new AnimatableScaleValue(new ScaleXY(1.0f, 1.0f));
        }
        AnimatableIntegerValue animatableIntegerValue2;
        if ((animatableIntegerValue2 = (AnimatableIntegerValue)integer) == null) {
            animatableIntegerValue2 = new AnimatableIntegerValue();
        }
        return new AnimatableTransform(animatablePathValue2, (AnimatableValue<PointF, PointF>)splitPath, animatableScaleValue, float3, animatableIntegerValue2, float1, (AnimatableFloatValue)float2);
    }
}
