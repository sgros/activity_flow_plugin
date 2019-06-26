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
        boolean nextBoolean = false;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n = 0;
            Label_0184: {
                if (hashCode != 101) {
                    if (hashCode != 109) {
                        if (hashCode != 111) {
                            if (hashCode != 115) {
                                if (hashCode != 3324) {
                                    if (hashCode == 3519) {
                                        if (nextName.equals("nm")) {
                                            n = 3;
                                            break Label_0184;
                                        }
                                    }
                                }
                                else if (nextName.equals("hd")) {
                                    n = 5;
                                    break Label_0184;
                                }
                            }
                            else if (nextName.equals("s")) {
                                n = 0;
                                break Label_0184;
                            }
                        }
                        else if (nextName.equals("o")) {
                            n = 2;
                            break Label_0184;
                        }
                    }
                    else if (nextName.equals("m")) {
                        n = 4;
                        break Label_0184;
                    }
                }
                else if (nextName.equals("e")) {
                    n = 1;
                    break Label_0184;
                }
                n = -1;
            }
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            if (n != 4) {
                                if (n != 5) {
                                    jsonReader.skipValue();
                                }
                                else {
                                    nextBoolean = jsonReader.nextBoolean();
                                }
                            }
                            else {
                                forId = ShapeTrimPath.Type.forId(jsonReader.nextInt());
                            }
                        }
                        else {
                            nextString = jsonReader.nextString();
                        }
                    }
                    else {
                        float3 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
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
        return new ShapeTrimPath((String)nextString, (ShapeTrimPath.Type)forId, (AnimatableFloatValue)float1, float2, (AnimatableFloatValue)float3, nextBoolean);
    }
}
