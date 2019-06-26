// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import android.graphics.Path$FillType;
import com.airbnb.lottie.model.content.ShapeFill;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class ShapeFillParser
{
    static ShapeFill parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        Object nextString = null;
        Object integer;
        Object color = integer = nextString;
        int nextInt = 1;
        boolean nextBoolean = false;
        boolean nextBoolean2 = false;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n = -1;
            final int hashCode = nextName.hashCode();
            if (hashCode != -396065730) {
                if (hashCode != 99) {
                    if (hashCode != 111) {
                        if (hashCode != 114) {
                            if (hashCode != 3324) {
                                if (hashCode == 3519) {
                                    if (nextName.equals("nm")) {
                                        n = 0;
                                    }
                                }
                            }
                            else if (nextName.equals("hd")) {
                                n = 5;
                            }
                        }
                        else if (nextName.equals("r")) {
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
            }
            else if (nextName.equals("fillEnabled")) {
                n = 3;
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
                                    nextBoolean2 = jsonReader.nextBoolean();
                                }
                            }
                            else {
                                nextInt = jsonReader.nextInt();
                            }
                        }
                        else {
                            nextBoolean = jsonReader.nextBoolean();
                        }
                    }
                    else {
                        integer = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
                    }
                }
                else {
                    color = AnimatableValueParser.parseColor(jsonReader, lottieComposition);
                }
            }
            else {
                nextString = jsonReader.nextString();
            }
        }
        Path$FillType path$FillType;
        if (nextInt == 1) {
            path$FillType = Path$FillType.WINDING;
        }
        else {
            path$FillType = Path$FillType.EVEN_ODD;
        }
        return new ShapeFill((String)nextString, nextBoolean, path$FillType, (AnimatableColorValue)color, (AnimatableIntegerValue)integer, nextBoolean2);
    }
}
