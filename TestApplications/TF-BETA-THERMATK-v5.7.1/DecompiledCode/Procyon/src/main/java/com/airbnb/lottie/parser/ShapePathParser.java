// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;
import com.airbnb.lottie.model.content.ShapePath;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class ShapePathParser
{
    static ShapePath parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        String nextString = null;
        AnimatableShapeValue shapeData = null;
        int nextInt = 0;
        boolean nextBoolean = false;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n = -1;
            final int hashCode = nextName.hashCode();
            if (hashCode != 3324) {
                if (hashCode != 3432) {
                    if (hashCode != 3519) {
                        if (hashCode == 104415) {
                            if (nextName.equals("ind")) {
                                n = 1;
                            }
                        }
                    }
                    else if (nextName.equals("nm")) {
                        n = 0;
                    }
                }
                else if (nextName.equals("ks")) {
                    n = 2;
                }
            }
            else if (nextName.equals("hd")) {
                n = 3;
            }
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            jsonReader.skipValue();
                        }
                        else {
                            nextBoolean = jsonReader.nextBoolean();
                        }
                    }
                    else {
                        shapeData = AnimatableValueParser.parseShapeData(jsonReader, lottieComposition);
                    }
                }
                else {
                    nextInt = jsonReader.nextInt();
                }
            }
            else {
                nextString = jsonReader.nextString();
            }
        }
        return new ShapePath(nextString, nextInt, shapeData, nextBoolean);
    }
}
