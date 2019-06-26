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
        final ArrayList<Object> list = new ArrayList<Object>();
        String nextString = null;
        GradientType gradientType = null;
        AnimatableGradientColorValue gradientColor = null;
        AnimatableIntegerValue integer = null;
        AnimatablePointValue point = null;
        AnimatablePointValue point2 = null;
        AnimatableFloatValue animatableFloatValue = null;
        ShapeStroke.LineCapType lineCapType = null;
        ShapeStroke.LineJoinType lineJoinType = null;
        float n = 0.0f;
        AnimatableFloatValue animatableFloatValue2 = null;
        boolean nextBoolean = false;
    Label_0424_Outer:
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n2 = 0;
            Label_0356: {
                if (hashCode != 100) {
                    if (hashCode != 101) {
                        if (hashCode != 103) {
                            if (hashCode != 111) {
                                if (hashCode != 119) {
                                    if (hashCode != 3324) {
                                        if (hashCode != 3447) {
                                            if (hashCode != 3454) {
                                                if (hashCode != 3487) {
                                                    if (hashCode != 3519) {
                                                        if (hashCode != 115) {
                                                            if (hashCode == 116) {
                                                                if (nextName.equals("t")) {
                                                                    n2 = 3;
                                                                    break Label_0356;
                                                                }
                                                            }
                                                        }
                                                        else if (nextName.equals("s")) {
                                                            n2 = 4;
                                                            break Label_0356;
                                                        }
                                                    }
                                                    else if (nextName.equals("nm")) {
                                                        n2 = 0;
                                                        break Label_0356;
                                                    }
                                                }
                                                else if (nextName.equals("ml")) {
                                                    n2 = 9;
                                                    break Label_0356;
                                                }
                                            }
                                            else if (nextName.equals("lj")) {
                                                n2 = 8;
                                                break Label_0356;
                                            }
                                        }
                                        else if (nextName.equals("lc")) {
                                            n2 = 7;
                                            break Label_0356;
                                        }
                                    }
                                    else if (nextName.equals("hd")) {
                                        n2 = 10;
                                        break Label_0356;
                                    }
                                }
                                else if (nextName.equals("w")) {
                                    n2 = 6;
                                    break Label_0356;
                                }
                            }
                            else if (nextName.equals("o")) {
                                n2 = 2;
                                break Label_0356;
                            }
                        }
                        else if (nextName.equals("g")) {
                            n2 = 1;
                            break Label_0356;
                        }
                    }
                    else if (nextName.equals("e")) {
                        n2 = 5;
                        break Label_0356;
                    }
                }
                else if (nextName.equals("d")) {
                    n2 = 11;
                    break Label_0356;
                }
                n2 = -1;
            }
            ShapeStroke.LineCapType lineCapType2 = null;
            AnimatableFloatValue animatableFloatValue4 = null;
            Label_0943: {
                switch (n2) {
                    default: {
                        jsonReader.skipValue();
                        break;
                    }
                    case 11: {
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            jsonReader.beginObject();
                            String nextString2 = null;
                            AnimatableFloatValue float1 = null;
                            while (jsonReader.hasNext()) {
                                final String nextName2 = jsonReader.nextName();
                                final int hashCode2 = nextName2.hashCode();
                                int n3 = 0;
                                Label_0528: {
                                    if (hashCode2 != 110) {
                                        if (hashCode2 == 118) {
                                            if (nextName2.equals("v")) {
                                                n3 = 1;
                                                break Label_0528;
                                            }
                                        }
                                    }
                                    else if (nextName2.equals("n")) {
                                        n3 = 0;
                                        break Label_0528;
                                    }
                                    n3 = -1;
                                }
                                if (n3 != 0) {
                                    if (n3 != 1) {
                                        jsonReader.skipValue();
                                    }
                                    else {
                                        float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                                    }
                                }
                                else {
                                    nextString2 = jsonReader.nextString();
                                }
                            }
                            jsonReader.endObject();
                            AnimatableFloatValue animatableFloatValue3 = null;
                            Label_0583: {
                                if (!nextString2.equals("o")) {
                                    if (!nextString2.equals("d")) {
                                        animatableFloatValue3 = animatableFloatValue2;
                                        if (!nextString2.equals("g")) {
                                            break Label_0583;
                                        }
                                    }
                                    lottieComposition.setHasDashPattern(true);
                                    list.add(float1);
                                    continue Label_0424_Outer;
                                }
                                animatableFloatValue3 = float1;
                            }
                            animatableFloatValue2 = animatableFloatValue3;
                        }
                        jsonReader.endArray();
                        if (list.size() == 1) {
                            list.add(list.get(0));
                            break;
                        }
                        break;
                    }
                    case 10: {
                        nextBoolean = jsonReader.nextBoolean();
                        break;
                    }
                    case 9: {
                        n = (float)jsonReader.nextDouble();
                        break;
                    }
                    case 8: {
                        lineJoinType = ShapeStroke.LineJoinType.values()[jsonReader.nextInt() - 1];
                        lineCapType2 = lineCapType;
                        animatableFloatValue4 = animatableFloatValue;
                        break Label_0943;
                    }
                    case 7: {
                        lineCapType = ShapeStroke.LineCapType.values()[jsonReader.nextInt() - 1];
                        break;
                    }
                    case 6: {
                        final AnimatableFloatValue float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                        lineCapType2 = lineCapType;
                        animatableFloatValue4 = float2;
                        break Label_0943;
                    }
                    case 5: {
                        point2 = AnimatableValueParser.parsePoint(jsonReader, lottieComposition);
                        break;
                    }
                    case 4: {
                        point = AnimatableValueParser.parsePoint(jsonReader, lottieComposition);
                        break;
                    }
                    case 3: {
                        final ShapeStroke.LineCapType lineCapType3 = lineCapType;
                        GradientType gradientType2;
                        if (jsonReader.nextInt() == 1) {
                            gradientType2 = GradientType.LINEAR;
                        }
                        else {
                            gradientType2 = GradientType.RADIAL;
                        }
                        final GradientType gradientType3 = gradientType2;
                        lineCapType = lineCapType3;
                        gradientType = gradientType3;
                        break;
                    }
                    case 2: {
                        integer = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
                        break;
                    }
                    case 1: {
                        jsonReader.beginObject();
                        int nextInt = -1;
                        while (jsonReader.hasNext()) {
                            final String nextName3 = jsonReader.nextName();
                            final int hashCode3 = nextName3.hashCode();
                            int n4 = 0;
                            Label_0889: {
                                if (hashCode3 != 107) {
                                    if (hashCode3 == 112) {
                                        if (nextName3.equals("p")) {
                                            n4 = 0;
                                            break Label_0889;
                                        }
                                    }
                                }
                                else if (nextName3.equals("k")) {
                                    n4 = 1;
                                    break Label_0889;
                                }
                                n4 = -1;
                            }
                            if (n4 != 0) {
                                if (n4 != 1) {
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
                        break;
                    }
                    case 0: {
                        nextString = jsonReader.nextString();
                        break;
                    }
                }
                while (true) {
                    lineCapType2 = lineCapType;
                    animatableFloatValue4 = animatableFloatValue;
                    break Label_0943;
                    continue;
                }
            }
            animatableFloatValue = animatableFloatValue4;
            lineCapType = lineCapType2;
        }
        return new GradientStroke(nextString, gradientType, gradientColor, integer, point, point2, animatableFloatValue, lineCapType, lineJoinType, n, (List<AnimatableFloatValue>)list, animatableFloatValue2, nextBoolean);
    }
}
