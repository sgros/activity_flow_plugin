// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import java.io.Serializable;
import android.graphics.PointF;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.content.PolystarShape;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class PolystarShapeParser
{
    static PolystarShape parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        Object nextString = null;
        Object float1;
        Serializable forValue = (Serializable)(float1 = nextString);
        Object float2;
        Object splitPath = float2 = float1;
        Object float4;
        AnimatableFloatValue float3 = (AnimatableFloatValue)(float4 = float2);
        Object float6;
        AnimatableFloatValue float5 = (AnimatableFloatValue)(float6 = float4);
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            switch (nextName) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case "is": {
                    float5 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case "ir": {
                    float3 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    continue;
                }
                case "os": {
                    float6 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case "or": {
                    float4 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    continue;
                }
                case "r": {
                    float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case "p": {
                    splitPath = AnimatablePathValueParser.parseSplitPath(jsonReader, lottieComposition);
                    continue;
                }
                case "pt": {
                    float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case "sy": {
                    forValue = PolystarShape.Type.forValue(jsonReader.nextInt());
                    continue;
                }
                case "nm": {
                    nextString = jsonReader.nextString();
                    continue;
                }
            }
        }
        return new PolystarShape((String)nextString, (PolystarShape.Type)forValue, (AnimatableFloatValue)float1, (AnimatableValue<PointF, PointF>)splitPath, (AnimatableFloatValue)float2, float3, (AnimatableFloatValue)float4, float5, (AnimatableFloatValue)float6);
    }
}
