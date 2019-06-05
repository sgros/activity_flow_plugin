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
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n = 0;
            Label_0155: {
                if (hashCode != -396065730) {
                    if (hashCode != 99) {
                        if (hashCode != 111) {
                            if (hashCode != 114) {
                                if (hashCode == 3519) {
                                    if (nextName.equals("nm")) {
                                        n = 0;
                                        break Label_0155;
                                    }
                                }
                            }
                            else if (nextName.equals("r")) {
                                n = 4;
                                break Label_0155;
                            }
                        }
                        else if (nextName.equals("o")) {
                            n = 2;
                            break Label_0155;
                        }
                    }
                    else if (nextName.equals("c")) {
                        n = 1;
                        break Label_0155;
                    }
                }
                else if (nextName.equals("fillEnabled")) {
                    n = 3;
                    break Label_0155;
                }
                n = -1;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 4: {
                    nextInt = jsonReader.nextInt();
                    continue;
                }
                case 3: {
                    nextBoolean = jsonReader.nextBoolean();
                    continue;
                }
                case 2: {
                    integer = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
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
        Path$FillType path$FillType;
        if (nextInt == 1) {
            path$FillType = Path$FillType.WINDING;
        }
        else {
            path$FillType = Path$FillType.EVEN_ODD;
        }
        return new ShapeFill((String)nextString, nextBoolean, path$FillType, (AnimatableColorValue)color, (AnimatableIntegerValue)integer);
    }
}
