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
        Object splitPath;
        Object nextString = splitPath = null;
        Object float1;
        AnimatableValue<PointF, PointF> point = (AnimatableValue<PointF, PointF>)(float1 = splitPath);
        boolean nextBoolean = false;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n = -1;
            final int hashCode = nextName.hashCode();
            if (hashCode != 112) {
                if (hashCode != 3324) {
                    if (hashCode != 3519) {
                        if (hashCode != 114) {
                            if (hashCode == 115) {
                                if (nextName.equals("s")) {
                                    n = 2;
                                }
                            }
                        }
                        else if (nextName.equals("r")) {
                            n = 3;
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
            else if (nextName.equals("p")) {
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
                            float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                        }
                    }
                    else {
                        point = AnimatableValueParser.parsePoint(jsonReader, lottieComposition);
                    }
                }
                else {
                    splitPath = AnimatablePathValueParser.parseSplitPath(jsonReader, lottieComposition);
                }
            }
            else {
                nextString = jsonReader.nextString();
            }
        }
        return new RectangleShape((String)nextString, (AnimatableValue<PointF, PointF>)splitPath, (AnimatablePointValue)point, (AnimatableFloatValue)float1, nextBoolean);
    }
}
