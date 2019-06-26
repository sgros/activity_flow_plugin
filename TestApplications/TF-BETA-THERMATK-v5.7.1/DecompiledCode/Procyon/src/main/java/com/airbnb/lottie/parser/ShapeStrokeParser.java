// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import java.util.List;
import java.util.ArrayList;
import com.airbnb.lottie.model.content.ShapeStroke;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class ShapeStrokeParser
{
    static ShapeStroke parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        final ArrayList list = new ArrayList<Object>();
        String nextString = null;
        AnimatableFloatValue animatableFloatValue = null;
        AnimatableColorValue color = null;
        AnimatableIntegerValue integer = null;
        AnimatableFloatValue float1 = null;
        Enum<ShapeStroke.LineCapType> enum1 = null;
        Enum<ShapeStroke.LineJoinType> enum2 = null;
        float n = 0.0f;
        boolean nextBoolean = false;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n2 = 0;
            Label_0275: {
                if (hashCode != 99) {
                    if (hashCode != 100) {
                        if (hashCode != 111) {
                            if (hashCode != 119) {
                                if (hashCode != 3324) {
                                    if (hashCode != 3447) {
                                        if (hashCode != 3454) {
                                            if (hashCode != 3487) {
                                                if (hashCode == 3519) {
                                                    if (nextName.equals("nm")) {
                                                        n2 = 0;
                                                        break Label_0275;
                                                    }
                                                }
                                            }
                                            else if (nextName.equals("ml")) {
                                                n2 = 6;
                                                break Label_0275;
                                            }
                                        }
                                        else if (nextName.equals("lj")) {
                                            n2 = 5;
                                            break Label_0275;
                                        }
                                    }
                                    else if (nextName.equals("lc")) {
                                        n2 = 4;
                                        break Label_0275;
                                    }
                                }
                                else if (nextName.equals("hd")) {
                                    n2 = 7;
                                    break Label_0275;
                                }
                            }
                            else if (nextName.equals("w")) {
                                n2 = 2;
                                break Label_0275;
                            }
                        }
                        else if (nextName.equals("o")) {
                            n2 = 3;
                            break Label_0275;
                        }
                    }
                    else if (nextName.equals("d")) {
                        n2 = 8;
                        break Label_0275;
                    }
                }
                else if (nextName.equals("c")) {
                    n2 = 1;
                    break Label_0275;
                }
                n2 = -1;
            }
            switch (n2) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 8: {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        String nextString2 = null;
                        AnimatableFloatValue float2 = null;
                        while (jsonReader.hasNext()) {
                            final String nextName2 = jsonReader.nextName();
                            final int hashCode2 = nextName2.hashCode();
                            int n3 = 0;
                            Label_0428: {
                                if (hashCode2 != 110) {
                                    if (hashCode2 == 118) {
                                        if (nextName2.equals("v")) {
                                            n3 = 1;
                                            break Label_0428;
                                        }
                                    }
                                }
                                else if (nextName2.equals("n")) {
                                    n3 = 0;
                                    break Label_0428;
                                }
                                n3 = -1;
                            }
                            if (n3 != 0) {
                                if (n3 != 1) {
                                    jsonReader.skipValue();
                                }
                                else {
                                    float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                                }
                            }
                            else {
                                nextString2 = jsonReader.nextString();
                            }
                        }
                        jsonReader.endObject();
                        final int hashCode3 = nextString2.hashCode();
                        int n4 = 0;
                        Label_0551: {
                            if (hashCode3 != 100) {
                                if (hashCode3 != 103) {
                                    if (hashCode3 == 111) {
                                        if (nextString2.equals("o")) {
                                            n4 = 0;
                                            break Label_0551;
                                        }
                                    }
                                }
                                else if (nextString2.equals("g")) {
                                    n4 = 2;
                                    break Label_0551;
                                }
                            }
                            else if (nextString2.equals("d")) {
                                n4 = 1;
                                break Label_0551;
                            }
                            n4 = -1;
                        }
                        if (n4 != 0) {
                            if (n4 != 1) {
                                if (n4 != 2) {
                                    continue;
                                }
                            }
                            lottieComposition.setHasDashPattern(true);
                            list.add(float2);
                        }
                        else {
                            animatableFloatValue = float2;
                        }
                    }
                    jsonReader.endArray();
                    if (list.size() != 1) {
                        continue;
                    }
                    list.add(list.get(0));
                    continue;
                }
                case 7: {
                    nextBoolean = jsonReader.nextBoolean();
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
        return new ShapeStroke(nextString, animatableFloatValue, list, color, integer, float1, (ShapeStroke.LineCapType)enum1, (ShapeStroke.LineJoinType)enum2, n, nextBoolean);
    }
}
