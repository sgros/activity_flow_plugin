// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import java.util.List;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import java.util.ArrayList;
import com.airbnb.lottie.model.content.ShapeStroke;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class ShapeStrokeParser
{
    static ShapeStroke parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        final ArrayList<AnimatableFloatValue> list = new ArrayList<AnimatableFloatValue>();
        String nextString = null;
        AnimatableFloatValue animatableFloatValue = null;
        AnimatableColorValue color = null;
        AnimatableIntegerValue integer = null;
        AnimatableFloatValue float1 = null;
        Enum<ShapeStroke.LineCapType> enum1 = null;
        Enum<ShapeStroke.LineJoinType> enum2 = null;
        float n = 0.0f;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n2 = 0;
            Label_0256: {
                if (hashCode != 111) {
                    if (hashCode != 119) {
                        if (hashCode != 3447) {
                            if (hashCode != 3454) {
                                if (hashCode != 3487) {
                                    if (hashCode != 3519) {
                                        switch (hashCode) {
                                            case 100: {
                                                if (nextName.equals("d")) {
                                                    n2 = 7;
                                                    break Label_0256;
                                                }
                                                break;
                                            }
                                            case 99: {
                                                if (nextName.equals("c")) {
                                                    n2 = 1;
                                                    break Label_0256;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                    else if (nextName.equals("nm")) {
                                        n2 = 0;
                                        break Label_0256;
                                    }
                                }
                                else if (nextName.equals("ml")) {
                                    n2 = 6;
                                    break Label_0256;
                                }
                            }
                            else if (nextName.equals("lj")) {
                                n2 = 5;
                                break Label_0256;
                            }
                        }
                        else if (nextName.equals("lc")) {
                            n2 = 4;
                            break Label_0256;
                        }
                    }
                    else if (nextName.equals("w")) {
                        n2 = 2;
                        break Label_0256;
                    }
                }
                else if (nextName.equals("o")) {
                    n2 = 3;
                    break Label_0256;
                }
                n2 = -1;
            }
            switch (n2) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 7: {
                    jsonReader.beginArray();
                    AnimatableFloatValue animatableFloatValue2 = animatableFloatValue;
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        String nextString2 = null;
                        AnimatableFloatValue float2 = null;
                        while (jsonReader.hasNext()) {
                            final String nextName2 = jsonReader.nextName();
                            final int hashCode2 = nextName2.hashCode();
                            int n3 = 0;
                            Label_0408: {
                                if (hashCode2 != 110) {
                                    if (hashCode2 == 118) {
                                        if (nextName2.equals("v")) {
                                            n3 = 1;
                                            break Label_0408;
                                        }
                                    }
                                }
                                else if (nextName2.equals("n")) {
                                    n3 = 0;
                                    break Label_0408;
                                }
                                n3 = -1;
                            }
                            switch (n3) {
                                default: {
                                    jsonReader.skipValue();
                                    continue;
                                }
                                case 1: {
                                    float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                                    continue;
                                }
                                case 0: {
                                    nextString2 = jsonReader.nextString();
                                    continue;
                                }
                            }
                        }
                        jsonReader.endObject();
                        final int hashCode3 = nextString2.hashCode();
                        int n4 = 0;
                        Label_0544: {
                            if (hashCode3 != 100) {
                                if (hashCode3 != 103) {
                                    if (hashCode3 == 111) {
                                        if (nextString2.equals("o")) {
                                            n4 = 0;
                                            break Label_0544;
                                        }
                                    }
                                }
                                else if (nextString2.equals("g")) {
                                    n4 = 2;
                                    break Label_0544;
                                }
                            }
                            else if (nextString2.equals("d")) {
                                n4 = 1;
                                break Label_0544;
                            }
                            n4 = -1;
                        }
                        switch (n4) {
                            default: {
                                continue;
                            }
                            case 1:
                            case 2: {
                                list.add(float2);
                                continue;
                            }
                            case 0: {
                                animatableFloatValue2 = float2;
                                continue;
                            }
                        }
                    }
                    jsonReader.endArray();
                    animatableFloatValue = animatableFloatValue2;
                    if (list.size() == 1) {
                        list.add((AnimatableFloatValue)list.get(0));
                        animatableFloatValue = animatableFloatValue2;
                        continue;
                    }
                    continue;
                }
                case 6: {
                    n = (float)jsonReader.nextDouble();
                    continue;
                }
                case 5: {
                    enum2 = ShapeStroke.LineJoinType.values()[jsonReader.nextInt() - 1];
                    continue;
                }
                case 4: {
                    enum1 = ShapeStroke.LineCapType.values()[jsonReader.nextInt() - 1];
                    continue;
                }
                case 3: {
                    integer = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
                    continue;
                }
                case 2: {
                    float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    continue;
                }
                case 1: {
                    color = AnimatableValueParser.parseColor(jsonReader, lottieComposition);
                    continue;
                }
                case 0: {
                    nextString = jsonReader.nextString();
                    continue;
                }
            }
        }
        return new ShapeStroke(nextString, animatableFloatValue, list, color, integer, float1, (ShapeStroke.LineCapType)enum1, (ShapeStroke.LineJoinType)enum2, n);
    }
}
