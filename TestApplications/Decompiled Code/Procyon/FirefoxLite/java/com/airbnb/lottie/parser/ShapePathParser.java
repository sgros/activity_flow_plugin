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
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n = 0;
            Label_0104: {
                if (hashCode != 3432) {
                    if (hashCode != 3519) {
                        if (hashCode == 104415) {
                            if (nextName.equals("ind")) {
                                n = 1;
                                break Label_0104;
                            }
                        }
                    }
                    else if (nextName.equals("nm")) {
                        n = 0;
                        break Label_0104;
                    }
                }
                else if (nextName.equals("ks")) {
                    n = 2;
                    break Label_0104;
                }
                n = -1;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 2: {
                    shapeData = AnimatableValueParser.parseShapeData(jsonReader, lottieComposition);
                    continue;
                }
                case 1: {
                    nextInt = jsonReader.nextInt();
                    continue;
                }
                case 0: {
                    nextString = jsonReader.nextString();
                    continue;
                }
            }
        }
        return new ShapePath(nextString, nextInt, shapeData);
    }
}
