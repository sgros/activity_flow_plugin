// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import android.graphics.PointF;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.content.CircleShape;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class CircleShapeParser
{
    static CircleShape parse(final JsonReader jsonReader, final LottieComposition lottieComposition, int n) throws IOException {
        boolean b = n == 3;
        Object nextString = null;
        Object point;
        Object splitPath = point = nextString;
        boolean nextBoolean = false;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            n = -1;
            final int hashCode = nextName.hashCode();
            if (hashCode != 100) {
                if (hashCode != 112) {
                    if (hashCode != 115) {
                        if (hashCode != 3324) {
                            if (hashCode == 3519) {
                                if (nextName.equals("nm")) {
                                    n = 0;
                                }
                            }
                        }
                        else if (nextName.equals("hd")) {
                            n = 3;
                        }
                    }
                    else if (nextName.equals("s")) {
                        n = 2;
                    }
                }
                else if (nextName.equals("p")) {
                    n = 1;
                }
            }
            else if (nextName.equals("d")) {
                n = 4;
            }
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            if (n != 4) {
                                jsonReader.skipValue();
                            }
                            else {
                                b = (jsonReader.nextInt() == 3);
                            }
                        }
                        else {
                            nextBoolean = jsonReader.nextBoolean();
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
        return new CircleShape((String)nextString, (AnimatableValue<PointF, PointF>)splitPath, (AnimatablePointValue)point, b, nextBoolean);
    }
}
