// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import android.graphics.PointF;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.content.RectangleShape;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class RectangleShapeParser
{
    static RectangleShape parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        String nextString = null;
        Object splitPath = null;
        Object float1;
        AnimatableValue<PointF, PointF> point = (AnimatableValue<PointF, PointF>)(float1 = splitPath);
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n = -1;
            final int hashCode = nextName.hashCode();
            if (hashCode != 112) {
                if (hashCode != 3519) {
                    switch (hashCode) {
                        case 115: {
                            if (nextName.equals("s")) {
                                n = 2;
                                break;
                            }
                            break;
                        }
                        case 114: {
                            if (nextName.equals("r")) {
                                n = 3;
                                break;
                            }
                            break;
                        }
                    }
                }
                else if (nextName.equals("nm")) {
                    n = 0;
                }
            }
            else if (nextName.equals("p")) {
                n = 1;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 3: {
                    float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    continue;
                }
                case 2: {
                    point = AnimatableValueParser.parsePoint(jsonReader, lottieComposition);
                    continue;
                }
                case 1: {
                    splitPath = AnimatablePathValueParser.parseSplitPath(jsonReader, lottieComposition);
                    continue;
                }
                case 0: {
                    nextString = jsonReader.nextString();
                    continue;
                }
            }
        }
        return new RectangleShape(nextString, (AnimatableValue<PointF, PointF>)splitPath, (AnimatablePointValue)point, (AnimatableFloatValue)float1);
    }
}
