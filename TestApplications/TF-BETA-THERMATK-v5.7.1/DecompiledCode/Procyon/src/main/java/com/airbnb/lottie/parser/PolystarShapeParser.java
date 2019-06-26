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
        boolean nextBoolean = false;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n = 0;
            Label_0302: {
                if (hashCode != 112) {
                    if (hashCode != 114) {
                        if (hashCode != 3324) {
                            if (hashCode != 3519) {
                                if (hashCode != 3588) {
                                    if (hashCode != 3686) {
                                        if (hashCode != 3369) {
                                            if (hashCode != 3370) {
                                                if (hashCode != 3555) {
                                                    if (hashCode == 3556) {
                                                        if (nextName.equals("os")) {
                                                            n = 6;
                                                            break Label_0302;
                                                        }
                                                    }
                                                }
                                                else if (nextName.equals("or")) {
                                                    n = 5;
                                                    break Label_0302;
                                                }
                                            }
                                            else if (nextName.equals("is")) {
                                                n = 8;
                                                break Label_0302;
                                            }
                                        }
                                        else if (nextName.equals("ir")) {
                                            n = 7;
                                            break Label_0302;
                                        }
                                    }
                                    else if (nextName.equals("sy")) {
                                        n = 1;
                                        break Label_0302;
                                    }
                                }
                                else if (nextName.equals("pt")) {
                                    n = 2;
                                    break Label_0302;
                                }
                            }
                            else if (nextName.equals("nm")) {
                                n = 0;
                                break Label_0302;
                            }
                        }
                        else if (nextName.equals("hd")) {
                            n = 9;
                            break Label_0302;
                        }
                    }
                    else if (nextName.equals("r")) {
                        n = 4;
                        break Label_0302;
                    }
                }
                else if (nextName.equals("p")) {
                    n = 3;
                    break Label_0302;
                }
                n = -1;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 9: {
                    nextBoolean = jsonReader.nextBoolean();
                    continue;
                }
                case 8: {
                    float5 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 7: {
                    float3 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    continue;
                }
                case 6: {
                    float6 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 5: {
                    float4 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    continue;
                }
                case 4: {
                    float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 3: {
                    splitPath = AnimatablePathValueParser.parseSplitPath(jsonReader, lottieComposition);
                    continue;
                }
                case 2: {
                    float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 1: {
                    forValue = PolystarShape.Type.forValue(jsonReader.nextInt());
                    continue;
                }
                case 0: {
                    nextString = jsonReader.nextString();
                    continue;
                }
            }
        }
        return new PolystarShape((String)nextString, (PolystarShape.Type)forValue, (AnimatableFloatValue)float1, (AnimatableValue<PointF, PointF>)splitPath, (AnimatableFloatValue)float2, float3, (AnimatableFloatValue)float4, float5, (AnimatableFloatValue)float6, nextBoolean);
    }
}
