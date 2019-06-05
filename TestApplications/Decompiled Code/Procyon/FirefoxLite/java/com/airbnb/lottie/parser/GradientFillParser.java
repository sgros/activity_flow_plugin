// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableGradientColorValue;
import com.airbnb.lottie.model.content.GradientType;
import android.graphics.Path$FillType;
import com.airbnb.lottie.model.content.GradientFill;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class GradientFillParser
{
    static GradientFill parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        final AnimatablePointValue animatablePointValue = null;
        final AnimatablePointValue animatablePointValue3;
        final AnimatablePointValue animatablePointValue2 = animatablePointValue3 = animatablePointValue;
        Object integer;
        Object gradientColor = integer = animatablePointValue3;
        Object point2;
        Object point = point2 = integer;
        Object o = animatablePointValue3;
        Object o2 = animatablePointValue2;
        Object nextString = animatablePointValue;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n = 0;
            Label_0220: {
                if (hashCode != 101) {
                    if (hashCode != 103) {
                        if (hashCode != 111) {
                            if (hashCode != 3519) {
                                switch (hashCode) {
                                    case 116: {
                                        if (nextName.equals("t")) {
                                            n = 3;
                                            break Label_0220;
                                        }
                                        break;
                                    }
                                    case 115: {
                                        if (nextName.equals("s")) {
                                            n = 4;
                                            break Label_0220;
                                        }
                                        break;
                                    }
                                    case 114: {
                                        if (nextName.equals("r")) {
                                            n = 6;
                                            break Label_0220;
                                        }
                                        break;
                                    }
                                }
                            }
                            else if (nextName.equals("nm")) {
                                n = 0;
                                break Label_0220;
                            }
                        }
                        else if (nextName.equals("o")) {
                            n = 2;
                            break Label_0220;
                        }
                    }
                    else if (nextName.equals("g")) {
                        n = 1;
                        break Label_0220;
                    }
                }
                else if (nextName.equals("e")) {
                    n = 5;
                    break Label_0220;
                }
                n = -1;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 6: {
                    Path$FillType path$FillType;
                    if (jsonReader.nextInt() == 1) {
                        path$FillType = Path$FillType.WINDING;
                    }
                    else {
                        path$FillType = Path$FillType.EVEN_ODD;
                    }
                    o = path$FillType;
                    continue;
                }
                case 5: {
                    point2 = AnimatableValueParser.parsePoint(jsonReader, lottieComposition);
                    continue;
                }
                case 4: {
                    point = AnimatableValueParser.parsePoint(jsonReader, lottieComposition);
                    continue;
                }
                case 3: {
                    GradientType gradientType;
                    if (jsonReader.nextInt() == 1) {
                        gradientType = GradientType.Linear;
                    }
                    else {
                        gradientType = GradientType.Radial;
                    }
                    o2 = gradientType;
                    continue;
                }
                case 2: {
                    integer = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
                    continue;
                }
                case 1: {
                    jsonReader.beginObject();
                    int nextInt = -1;
                    while (jsonReader.hasNext()) {
                        final String nextName2 = jsonReader.nextName();
                        final int hashCode2 = nextName2.hashCode();
                        int n2 = 0;
                        Label_0426: {
                            if (hashCode2 != 107) {
                                if (hashCode2 == 112) {
                                    if (nextName2.equals("p")) {
                                        n2 = 0;
                                        break Label_0426;
                                    }
                                }
                            }
                            else if (nextName2.equals("k")) {
                                n2 = 1;
                                break Label_0426;
                            }
                            n2 = -1;
                        }
                        switch (n2) {
                            default: {
                                jsonReader.skipValue();
                                continue;
                            }
                            case 1: {
                                gradientColor = AnimatableValueParser.parseGradientColor(jsonReader, lottieComposition, nextInt);
                                continue;
                            }
                            case 0: {
                                nextInt = jsonReader.nextInt();
                                continue;
                            }
                        }
                    }
                    jsonReader.endObject();
                    continue;
                }
                case 0: {
                    nextString = jsonReader.nextString();
                    continue;
                }
            }
        }
        return new GradientFill((String)nextString, (GradientType)o2, (Path$FillType)o, (AnimatableGradientColorValue)gradientColor, (AnimatableIntegerValue)integer, (AnimatablePointValue)point, (AnimatablePointValue)point2, null, null);
    }
}
