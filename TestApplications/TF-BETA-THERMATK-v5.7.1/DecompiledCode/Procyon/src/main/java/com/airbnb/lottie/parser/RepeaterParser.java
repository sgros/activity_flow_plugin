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
        Object float1;
        Object nextString = float1 = null;
        Object parse;
        AnimatableFloatValue float2 = (AnimatableFloatValue)(parse = float1);
        boolean nextBoolean = false;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n = -1;
            final int hashCode = nextName.hashCode();
            if (hashCode != 99) {
                if (hashCode != 111) {
                    if (hashCode != 3324) {
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
                    else if (nextName.equals("hd")) {
                        n = 4;
                    }
                }
                else if (nextName.equals("o")) {
                    n = 2;
                }
            }
            else if (nextName.equals("c")) {
                n = 1;
            }
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            if (n != 4) {
                                jsonReader.skipValue();
                            }
                            else {
                                nextBoolean = jsonReader.nextBoolean();
                            }
                        }
                        else {
                            parse = AnimatableTransformParser.parse(jsonReader, lottieComposition);
                        }
                    }
                    else {
                        float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    }
                }
                else {
                    float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                }
            }
            else {
                nextString = jsonReader.nextString();
            }
        }
        return new Repeater((String)nextString, (AnimatableFloatValue)float1, float2, (AnimatableTransform)parse, nextBoolean);
    }
}
