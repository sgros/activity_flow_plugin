// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import android.graphics.PointF;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.content.CircleShape;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class CircleShapeParser
{
    static CircleShape parse(final JsonReader jsonReader, final LottieComposition lottieComposition, int hashCode) throws IOException {
        boolean b = hashCode == 3;
        String nextString = null;
        AnimatablePointValue point;
        AnimatableValue<PointF, PointF> splitPath = point = null;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            hashCode = nextName.hashCode();
            Label_0131: {
                if (hashCode != 100) {
                    if (hashCode != 112) {
                        if (hashCode != 115) {
                            if (hashCode == 3519) {
                                if (nextName.equals("nm")) {
                                    hashCode = 0;
                                    break Label_0131;
                                }
                            }
                        }
                        else if (nextName.equals("s")) {
                            hashCode = 2;
                            break Label_0131;
                        }
                    }
                    else if (nextName.equals("p")) {
                        hashCode = 1;
                        break Label_0131;
                    }
                }
                else if (nextName.equals("d")) {
                    hashCode = 3;
                    break Label_0131;
                }
                hashCode = -1;
            }
            switch (hashCode) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 3: {
                    b = (jsonReader.nextInt() == 3);
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
        return new CircleShape(nextString, splitPath, point, b);
    }
}
