// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.content.Repeater;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class RepeaterParser
{
    static Repeater parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        String nextString = null;
        Object float1 = null;
        Object parse;
        AnimatableFloatValue float2 = (AnimatableFloatValue)(parse = float1);
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n = -1;
            final int hashCode = nextName.hashCode();
            if (hashCode != 99) {
                if (hashCode != 111) {
                    if (hashCode != 3519) {
                        if (hashCode == 3710) {
                            if (nextName.equals("tr")) {
                                n = 3;
                            }
                        }
                    }
                    else if (nextName.equals("nm")) {
                        n = 0;
                    }
                }
                else if (nextName.equals("o")) {
                    n = 2;
                }
            }
            else if (nextName.equals("c")) {
                n = 1;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 3: {
                    parse = AnimatableTransformParser.parse(jsonReader, lottieComposition);
                    continue;
                }
                case 2: {
                    float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 1: {
                    float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 0: {
                    nextString = jsonReader.nextString();
                    continue;
                }
            }
        }
        return new Repeater(nextString, (AnimatableFloatValue)float1, float2, (AnimatableTransform)parse);
    }
}
