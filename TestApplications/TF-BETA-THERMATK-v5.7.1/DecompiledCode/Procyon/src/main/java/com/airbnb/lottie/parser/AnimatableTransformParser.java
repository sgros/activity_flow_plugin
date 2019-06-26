// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import android.view.animation.Interpolator;
import android.util.JsonToken;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;
import com.airbnb.lottie.value.ScaleXY;
import com.airbnb.lottie.model.animatable.AnimatableScaleValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableSplitDimensionPathValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.value.Keyframe;
import android.graphics.PointF;
import com.airbnb.lottie.model.animatable.AnimatablePathValue;

public class AnimatableTransformParser
{
    private static boolean isAnchorPointIdentity(final AnimatablePathValue animatablePathValue) {
        final boolean b = false;
        if (animatablePathValue != null) {
            boolean b2 = b;
            if (!animatablePathValue.isStatic()) {
                return b2;
            }
            b2 = b;
            if (!animatablePathValue.getKeyframes().get(0).startValue.equals(0.0f, 0.0f)) {
                return b2;
            }
        }
        return true;
    }
    
    private static boolean isPositionIdentity(final AnimatableValue<PointF, PointF> animatableValue) {
        final boolean b = false;
        if (animatableValue != null) {
            boolean b2 = b;
            if (animatableValue instanceof AnimatableSplitDimensionPathValue) {
                return b2;
            }
            b2 = b;
            if (!animatableValue.isStatic()) {
                return b2;
            }
            b2 = b;
            if (!animatableValue.getKeyframes().get(0).startValue.equals(0.0f, 0.0f)) {
                return b2;
            }
        }
        return true;
    }
    
    private static boolean isRotationIdentity(final AnimatableFloatValue animatableFloatValue) {
        final boolean b = false;
        if (animatableFloatValue != null) {
            boolean b2 = b;
            if (!animatableFloatValue.isStatic()) {
                return b2;
            }
            b2 = b;
            if ((float)animatableFloatValue.getKeyframes().get(0).startValue != 0.0f) {
                return b2;
            }
        }
        return true;
    }
    
    private static boolean isScaleIdentity(final AnimatableScaleValue animatableScaleValue) {
        final boolean b = false;
        if (animatableScaleValue != null) {
            boolean b2 = b;
            if (!animatableScaleValue.isStatic()) {
                return b2;
            }
            b2 = b;
            if (!((ScaleXY)animatableScaleValue.getKeyframes().get(0).startValue).equals(1.0f, 1.0f)) {
                return b2;
            }
        }
        return true;
    }
    
    private static boolean isSkewAngleIdentity(final AnimatableFloatValue animatableFloatValue) {
        final boolean b = false;
        if (animatableFloatValue != null) {
            boolean b2 = b;
            if (!animatableFloatValue.isStatic()) {
                return b2;
            }
            b2 = b;
            if ((float)animatableFloatValue.getKeyframes().get(0).startValue != 0.0f) {
                return b2;
            }
        }
        return true;
    }
    
    private static boolean isSkewIdentity(final AnimatableFloatValue animatableFloatValue) {
        final boolean b = false;
        if (animatableFloatValue != null) {
            boolean b2 = b;
            if (!animatableFloatValue.isStatic()) {
                return b2;
            }
            b2 = b;
            if ((float)animatableFloatValue.getKeyframes().get(0).startValue != 0.0f) {
                return b2;
            }
        }
        return true;
    }
    
    public static AnimatableTransform parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        final boolean b = jsonReader.peek() == JsonToken.BEGIN_OBJECT;
        if (b) {
            jsonReader.beginObject();
        }
        AnimatableFloatValue float1 = null;
        AnimatablePathValue parse = null;
        AnimatableValue<PointF, PointF> splitPath = null;
        AnimatableScaleValue scale = null;
        AnimatableFloatValue float2 = null;
        AnimatableFloatValue float3 = null;
        AnimatableIntegerValue integer = null;
        AnimatableFloatValue float4 = null;
        AnimatableFloatValue float5 = null;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n = 0;
            Label_0316: {
                if (hashCode != 97) {
                    if (hashCode != 3242) {
                        if (hashCode != 3656) {
                            if (hashCode != 3662) {
                                if (hashCode != 3672) {
                                    if (hashCode != 3676) {
                                        if (hashCode != 111) {
                                            if (hashCode != 112) {
                                                if (hashCode != 114) {
                                                    if (hashCode == 115) {
                                                        if (nextName.equals("s")) {
                                                            n = 2;
                                                            break Label_0316;
                                                        }
                                                    }
                                                }
                                                else if (nextName.equals("r")) {
                                                    n = 4;
                                                    break Label_0316;
                                                }
                                            }
                                            else if (nextName.equals("p")) {
                                                n = 1;
                                                break Label_0316;
                                            }
                                        }
                                        else if (nextName.equals("o")) {
                                            n = 5;
                                            break Label_0316;
                                        }
                                    }
                                    else if (nextName.equals("so")) {
                                        n = 6;
                                        break Label_0316;
                                    }
                                }
                                else if (nextName.equals("sk")) {
                                    n = 8;
                                    break Label_0316;
                                }
                            }
                            else if (nextName.equals("sa")) {
                                n = 9;
                                break Label_0316;
                            }
                        }
                        else if (nextName.equals("rz")) {
                            n = 3;
                            break Label_0316;
                        }
                    }
                    else if (nextName.equals("eo")) {
                        n = 7;
                        break Label_0316;
                    }
                }
                else if (nextName.equals("a")) {
                    n = 0;
                    break Label_0316;
                }
                n = -1;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 9: {
                    float3 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 8: {
                    float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 7: {
                    float5 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 6: {
                    float4 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
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
                    float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    if (float1.getKeyframes().isEmpty()) {
                        float1.getKeyframes().add(new Keyframe<Float>(lottieComposition, 0.0f, 0.0f, null, 0.0f, lottieComposition.getEndFrame()));
                    }
                    else {
                        if (((Keyframe)float1.getKeyframes().get(0)).startValue != null) {
                            continue;
                        }
                        float1.getKeyframes().set(0, new Keyframe<Float>(lottieComposition, 0.0f, 0.0f, null, 0.0f, lottieComposition.getEndFrame()));
                    }
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
        AnimatablePathValue animatablePathValue = parse;
        if (isAnchorPointIdentity(parse)) {
            animatablePathValue = null;
        }
        AnimatableValue<PointF, PointF> animatableValue;
        if (isPositionIdentity(splitPath)) {
            animatableValue = null;
        }
        else {
            animatableValue = splitPath;
        }
        AnimatableFloatValue animatableFloatValue;
        if (isRotationIdentity(float1)) {
            animatableFloatValue = null;
        }
        else {
            animatableFloatValue = float1;
        }
        if (isScaleIdentity(scale)) {
            scale = null;
        }
        if (isSkewIdentity(float2)) {
            float2 = null;
        }
        if (isSkewAngleIdentity(float3)) {
            float3 = null;
        }
        return new AnimatableTransform(animatablePathValue, animatableValue, scale, animatableFloatValue, integer, float4, float5, float2, float3);
    }
}
