// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

public class AnimatableTextPropertiesParser
{
    public static AnimatableTextProperties parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        jsonReader.beginObject();
        AnimatableTextProperties animatableTextProperties = null;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n = -1;
            if (nextName.hashCode() == 97) {
                if (nextName.equals("a")) {
                    n = 0;
                }
            }
            if (n != 0) {
                jsonReader.skipValue();
            }
            else {
                animatableTextProperties = parseAnimatableTextProperties(jsonReader, lottieComposition);
            }
        }
        jsonReader.endObject();
        if (animatableTextProperties == null) {
            return new AnimatableTextProperties(null, null, null, null);
        }
        return animatableTextProperties;
    }
    
    private static AnimatableTextProperties parseAnimatableTextProperties(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        jsonReader.beginObject();
        AnimatableColorValue color = null;
        Object color2 = null;
        Object float2;
        Object float1 = float2 = color2;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n = -1;
            final int hashCode = nextName.hashCode();
            if (hashCode != 116) {
                if (hashCode != 3261) {
                    if (hashCode != 3664) {
                        if (hashCode == 3684) {
                            if (nextName.equals("sw")) {
                                n = 2;
                            }
                        }
                    }
                    else if (nextName.equals("sc")) {
                        n = 1;
                    }
                }
                else if (nextName.equals("fc")) {
                    n = 0;
                }
            }
            else if (nextName.equals("t")) {
                n = 3;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 3: {
                    float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    continue;
                }
                case 2: {
                    float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    continue;
                }
                case 1: {
                    color2 = AnimatableValueParser.parseColor(jsonReader, lottieComposition);
                    continue;
                }
                case 0: {
                    color = AnimatableValueParser.parseColor(jsonReader, lottieComposition);
                    continue;
                }
            }
        }
        jsonReader.endObject();
        return new AnimatableTextProperties(color, (AnimatableColorValue)color2, (AnimatableFloatValue)float1, (AnimatableFloatValue)float2);
    }
}
