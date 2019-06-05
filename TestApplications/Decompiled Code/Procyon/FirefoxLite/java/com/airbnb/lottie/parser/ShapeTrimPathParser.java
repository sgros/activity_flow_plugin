// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import java.io.Serializable;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class ShapeTrimPathParser
{
    static ShapeTrimPath parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        Object nextString = null;
        Object float1;
        Serializable forId = (Serializable)(float1 = nextString);
        Object float3;
        AnimatableFloatValue float2 = (AnimatableFloatValue)(float3 = float1);
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n = -1;
            final int hashCode = nextName.hashCode();
            if (hashCode != 101) {
                if (hashCode != 109) {
                    if (hashCode != 111) {
                        if (hashCode != 115) {
                            if (hashCode == 3519) {
                                if (nextName.equals("nm")) {
                                    n = 3;
                                }
                            }
                        }
                        else if (nextName.equals("s")) {
                            n = 0;
                        }
                    }
                    else if (nextName.equals("o")) {
                        n = 2;
                    }
                }
                else if (nextName.equals("m")) {
                    n = 4;
                }
            }
            else if (nextName.equals("e")) {
                n = 1;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 4: {
                    forId = ShapeTrimPath.Type.forId(jsonReader.nextInt());
                    continue;
                }
                case 3: {
                    nextString = jsonReader.nextString();
                    continue;
                }
                case 2: {
                    float3 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 1: {
                    float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 0: {
                    float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
            }
        }
        return new ShapeTrimPath((String)nextString, (ShapeTrimPath.Type)forId, (AnimatableFloatValue)float1, float2, (AnimatableFloatValue)float3);
    }
}
