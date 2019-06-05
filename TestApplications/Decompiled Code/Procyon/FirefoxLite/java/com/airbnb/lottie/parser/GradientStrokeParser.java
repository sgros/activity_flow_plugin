// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableGradientColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import java.util.List;
import com.airbnb.lottie.model.content.GradientType;
import com.airbnb.lottie.model.content.ShapeStroke;
import java.util.ArrayList;
import com.airbnb.lottie.model.content.GradientStroke;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class GradientStrokeParser
{
    static GradientStroke parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        final ArrayList list = new ArrayList<Object>();
        String nextString = null;
        GradientType gradientType = null;
        AnimatableGradientColorValue gradientColor = null;
        AnimatableIntegerValue integer = null;
        AnimatablePointValue point = null;
        AnimatablePointValue point2 = null;
        AnimatableFloatValue float1 = null;
        ShapeStroke.LineCapType lineCapType = null;
        ShapeStroke.LineJoinType lineJoinType = null;
        float n = 0.0f;
        AnimatableFloatValue animatableFloatValue = null;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n2 = 0;
            Label_0343: {
                switch (nextName.hashCode()) {
                    case 3519: {
                        if (nextName.equals("nm")) {
                            n2 = 0;
                            break Label_0343;
                        }
                        break;
                    }
                    case 3487: {
                        if (nextName.equals("ml")) {
                            n2 = 9;
                            break Label_0343;
                        }
                        break;
                    }
                    case 3454: {
                        if (nextName.equals("lj")) {
                            n2 = 8;
                            break Label_0343;
                        }
                        break;
                    }
                    case 3447: {
                        if (nextName.equals("lc")) {
                            n2 = 7;
                            break Label_0343;
                        }
                        break;
                    }
                    case 119: {
                        if (nextName.equals("w")) {
                            n2 = 6;
                            break Label_0343;
                        }
                        break;
                    }
                    case 116: {
                        if (nextName.equals("t")) {
                            n2 = 3;
                            break Label_0343;
                        }
                        break;
                    }
                    case 115: {
                        if (nextName.equals("s")) {
                            n2 = 4;
                            break Label_0343;
                        }
                        break;
                    }
                    case 111: {
                        if (nextName.equals("o")) {
                            n2 = 2;
                            break Label_0343;
                        }
                        break;
                    }
                    case 103: {
                        if (nextName.equals("g")) {
                            n2 = 1;
                            break Label_0343;
                        }
                        break;
                    }
                    case 101: {
                        if (nextName.equals("e")) {
                            n2 = 5;
                            break Label_0343;
                        }
                        break;
                    }
                    case 100: {
                        if (nextName.equals("d")) {
                            n2 = 10;
                            break Label_0343;
                        }
                        break;
                    }
                }
                n2 = -1;
            }
            switch (n2) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 10: {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        String nextString2 = null;
                        AnimatableFloatValue float2 = null;
                        while (jsonReader.hasNext()) {
                            final String nextName2 = jsonReader.nextName();
                            final int hashCode = nextName2.hashCode();
                            int n3 = 0;
                            Label_0504: {
                                if (hashCode != 110) {
                                    if (hashCode == 118) {
                                        if (nextName2.equals("v")) {
                                            n3 = 1;
                                            break Label_0504;
                                        }
                                    }
                                }
                                else if (nextName2.equals("n")) {
                                    n3 = 0;
                                    break Label_0504;
                                }
                                n3 = -1;
                            }
                            switch (n3) {
                                default: {
                                    jsonReader.skipValue();
                                    continue;
                                }
                                case 1: {
                                    float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                                    continue;
                                }
                                case 0: {
                                    nextString2 = jsonReader.nextString();
                                    continue;
                                }
                            }
                        }
                        jsonReader.endObject();
                        if (nextString2.equals("o")) {
                            animatableFloatValue = float2;
                        }
                        else {
                            if (!nextString2.equals("d") && !nextString2.equals("g")) {
                                continue;
                            }
                            list.add(float2);
                        }
                    }
                    jsonReader.endArray();
                    if (list.size() != 1) {
                        continue;
                    }
                    list.add(list.get(0));
                    continue;
                }
                case 9: {
                    n = (float)jsonReader.nextDouble();
                    continue;
                }
                case 8: {
                    lineJoinType = ShapeStroke.LineJoinType.values()[jsonReader.nextInt() - 1];
                    continue;
                }
                case 7: {
                    lineCapType = ShapeStroke.LineCapType.values()[jsonReader.nextInt() - 1];
                    continue;
                }
                case 6: {
                    float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
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
                    GradientType gradientType2;
                    if (jsonReader.nextInt() == 1) {
                        gradientType2 = GradientType.Linear;
                    }
                    else {
                        gradientType2 = GradientType.Radial;
                    }
                    gradientType = gradientType2;
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
                        final String nextName3 = jsonReader.nextName();
                        final int hashCode2 = nextName3.hashCode();
                        int n4 = 0;
                        Label_0825: {
                            if (hashCode2 != 107) {
                                if (hashCode2 == 112) {
                                    if (nextName3.equals("p")) {
                                        n4 = 0;
                                        break Label_0825;
                                    }
                                }
                            }
                            else if (nextName3.equals("k")) {
                                n4 = 1;
                                break Label_0825;
                            }
                            n4 = -1;
                        }
                        switch (n4) {
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
        return new GradientStroke(nextString, gradientType, gradientColor, integer, point, point2, float1, lineCapType, lineJoinType, n, list, animatableFloatValue);
    }
}
