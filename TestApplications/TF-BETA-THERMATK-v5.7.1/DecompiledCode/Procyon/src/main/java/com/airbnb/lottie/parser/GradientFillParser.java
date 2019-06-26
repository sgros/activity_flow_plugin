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
        Path$FillType winding = Path$FillType.WINDING;
        final AnimatablePointValue animatablePointValue;
        Object nextString = animatablePointValue = null;
        Object integer;
        Object gradientColor = integer = animatablePointValue;
        Object point2;
        Object point = point2 = integer;
        boolean nextBoolean = false;
        Object o = animatablePointValue;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n = 0;
            Label_0252: {
                if (hashCode != 101) {
                    if (hashCode != 103) {
                        if (hashCode != 111) {
                            if (hashCode != 3324) {
                                if (hashCode != 3519) {
                                    switch (hashCode) {
                                        case 116: {
                                            if (nextName.equals("t")) {
                                                n = 3;
                                                break Label_0252;
                                            }
                                            break;
                                        }
                                        case 115: {
                                            if (nextName.equals("s")) {
                                                n = 4;
                                                break Label_0252;
                                            }
                                            break;
                                        }
                                        case 114: {
                                            if (nextName.equals("r")) {
                                                n = 6;
                                                break Label_0252;
                                            }
                                            break;
                                        }
                                    }
                                }
                                else if (nextName.equals("nm")) {
                                    n = 0;
                                    break Label_0252;
                                }
                            }
                            else if (nextName.equals("hd")) {
                                n = 7;
                                break Label_0252;
                            }
                        }
                        else if (nextName.equals("o")) {
                            n = 2;
                            break Label_0252;
                        }
                    }
                    else if (nextName.equals("g")) {
                        n = 1;
                        break Label_0252;
                    }
                }
                else if (nextName.equals("e")) {
                    n = 5;
                    break Label_0252;
                }
                n = -1;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 7: {
                    nextBoolean = jsonReader.nextBoolean();
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
                    winding = path$FillType;
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
                        gradientType = GradientType.LINEAR;
                    }
                    else {
                        gradientType = GradientType.RADIAL;
                    }
                    o = gradientType;
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
                        Label_0480: {
                            if (hashCode2 != 107) {
                                if (hashCode2 == 112) {
                                    if (nextName2.equals("p")) {
                                        n2 = 0;
                                        break Label_0480;
                                    }
                                }
                            }
                            else if (nextName2.equals("k")) {
                                n2 = 1;
                                break Label_0480;
                            }
                            n2 = -1;
                        }
                        if (n2 != 0) {
                            if (n2 != 1) {
                                jsonReader.skipValue();
                            }
                            else {
                                gradientColor = AnimatableValueParser.parseGradientColor(jsonReader, lottieComposition, nextInt);
                            }
                        }
                        else {
                            nextInt = jsonReader.nextInt();
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
        return new GradientFill((String)nextString, (GradientType)o, winding, (AnimatableGradientColorValue)gradientColor, (AnimatableIntegerValue)integer, (AnimatablePointValue)point, (AnimatablePointValue)point2, null, null, nextBoolean);
    }
}
