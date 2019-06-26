// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import android.graphics.Rect;
import java.util.Collections;
import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import com.airbnb.lottie.model.animatable.AnimatableTextFrame;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import java.util.List;
import android.view.animation.Interpolator;
import com.airbnb.lottie.value.Keyframe;
import android.graphics.Color;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.model.content.Mask;
import java.util.ArrayList;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

public class LayerParser
{
    public static Layer parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        Enum<Layer.MatteType> none = Layer.MatteType.NONE;
        final ArrayList<Mask> list = new ArrayList<Mask>();
        final ArrayList<ContentModel> list2 = new ArrayList<ContentModel>();
        jsonReader.beginObject();
        final Float value = 1.0f;
        final Float value2 = 0.0f;
        final AnimatableFloatValue animatableFloatValue2;
        final AnimatableFloatValue animatableFloatValue = animatableFloatValue2 = null;
        Object documentData;
        final AnimatableFloatValue animatableFloatValue3 = (AnimatableFloatValue)(documentData = animatableFloatValue2);
        Object float1;
        Object parse = float1 = documentData;
        long n = 0L;
        long n2 = -1L;
        float n3 = 0.0f;
        float n4 = 0.0f;
        float n5 = 1.0f;
        int n6 = 0;
        int n7 = 0;
        int color = 0;
        int n8 = 0;
        int n9 = 0;
        float n10 = 0.0f;
        boolean nextBoolean = false;
        final Object o = float1;
        final String s = "UNSET";
        Object parse2 = animatableFloatValue3;
        Object nextString = animatableFloatValue2;
        Object unknown = animatableFloatValue;
        Object nextString2 = o;
        String nextString3 = s;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n11 = 0;
            Label_0731: {
                switch (nextName.hashCode()) {
                    case 1441620890: {
                        if (nextName.equals("masksProperties")) {
                            n11 = 10;
                            break Label_0731;
                        }
                        break;
                    }
                    case 108390670: {
                        if (nextName.equals("refId")) {
                            n11 = 2;
                            break Label_0731;
                        }
                        break;
                    }
                    case 104415: {
                        if (nextName.equals("ind")) {
                            n11 = 1;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3717: {
                        if (nextName.equals("ty")) {
                            n11 = 3;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3712: {
                        if (nextName.equals("tt")) {
                            n11 = 9;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3705: {
                        if (nextName.equals("tm")) {
                            n11 = 20;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3684: {
                        if (nextName.equals("sw")) {
                            n11 = 5;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3681: {
                        if (nextName.equals("st")) {
                            n11 = 15;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3679: {
                        if (nextName.equals("sr")) {
                            n11 = 14;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3669: {
                        if (nextName.equals("sh")) {
                            n11 = 6;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3664: {
                        if (nextName.equals("sc")) {
                            n11 = 7;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3553: {
                        if (nextName.equals("op")) {
                            n11 = 19;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3519: {
                        if (nextName.equals("nm")) {
                            n11 = 0;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3432: {
                        if (nextName.equals("ks")) {
                            n11 = 8;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3367: {
                        if (nextName.equals("ip")) {
                            n11 = 18;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3324: {
                        if (nextName.equals("hd")) {
                            n11 = 22;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3233: {
                        if (nextName.equals("ef")) {
                            n11 = 13;
                            break Label_0731;
                        }
                        break;
                    }
                    case 3177: {
                        if (nextName.equals("cl")) {
                            n11 = 21;
                            break Label_0731;
                        }
                        break;
                    }
                    case 119: {
                        if (nextName.equals("w")) {
                            n11 = 16;
                            break Label_0731;
                        }
                        break;
                    }
                    case 116: {
                        if (nextName.equals("t")) {
                            n11 = 12;
                            break Label_0731;
                        }
                        break;
                    }
                    case 104: {
                        if (nextName.equals("h")) {
                            n11 = 17;
                            break Label_0731;
                        }
                        break;
                    }
                    case -903568142: {
                        if (nextName.equals("shapes")) {
                            n11 = 11;
                            break Label_0731;
                        }
                        break;
                    }
                    case -995424086: {
                        if (nextName.equals("parent")) {
                            n11 = 4;
                            break Label_0731;
                        }
                        break;
                    }
                }
                n11 = -1;
            }
            switch (n11) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 22: {
                    nextBoolean = jsonReader.nextBoolean();
                    continue;
                }
                case 21: {
                    nextString2 = jsonReader.nextString();
                    continue;
                }
                case 20: {
                    float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                }
                case 19: {
                    n4 = (float)jsonReader.nextDouble();
                    continue;
                }
                case 18: {
                    n3 = (float)jsonReader.nextDouble();
                    continue;
                }
                case 17: {
                    n9 = (int)(jsonReader.nextInt() * Utils.dpScale());
                    continue;
                }
                case 16: {
                    n8 = (int)(jsonReader.nextInt() * Utils.dpScale());
                    continue;
                }
                case 15: {
                    n10 = (float)jsonReader.nextDouble();
                    continue;
                }
                case 14: {
                    n5 = (float)jsonReader.nextDouble();
                    continue;
                }
                case 13: {
                    jsonReader.beginArray();
                    final ArrayList<String> obj = new ArrayList<String>();
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            final String nextName2 = jsonReader.nextName();
                            int n12 = 0;
                            Label_1016: {
                                if (nextName2.hashCode() == 3519) {
                                    if (nextName2.equals("nm")) {
                                        n12 = 0;
                                        break Label_1016;
                                    }
                                }
                                n12 = -1;
                            }
                            if (n12 != 0) {
                                jsonReader.skipValue();
                            }
                            else {
                                obj.add(jsonReader.nextString());
                            }
                        }
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Lottie doesn't support layer effects. If you are using them for  fills, strokes, trim paths etc. then try adding them directly as contents  in your shape. Found: ");
                    sb.append(obj);
                    lottieComposition.addWarning(sb.toString());
                    continue;
                }
                case 12: {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        final String nextName3 = jsonReader.nextName();
                        final int hashCode = nextName3.hashCode();
                        int n13 = 0;
                        Label_1167: {
                            if (hashCode != 97) {
                                if (hashCode == 100) {
                                    if (nextName3.equals("d")) {
                                        n13 = 0;
                                        break Label_1167;
                                    }
                                }
                            }
                            else if (nextName3.equals("a")) {
                                n13 = 1;
                                break Label_1167;
                            }
                            n13 = -1;
                        }
                        if (n13 != 0) {
                            if (n13 != 1) {
                                jsonReader.skipValue();
                            }
                            else {
                                jsonReader.beginArray();
                                if (jsonReader.hasNext()) {
                                    parse = AnimatableTextPropertiesParser.parse(jsonReader, lottieComposition);
                                }
                                while (jsonReader.hasNext()) {
                                    jsonReader.skipValue();
                                }
                                jsonReader.endArray();
                            }
                        }
                        else {
                            documentData = AnimatableValueParser.parseDocumentData(jsonReader, lottieComposition);
                        }
                    }
                    jsonReader.endObject();
                    continue;
                }
                case 11: {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        final ContentModel parse3 = ContentModelParser.parse(jsonReader, lottieComposition);
                        if (parse3 != null) {
                            list2.add(parse3);
                        }
                    }
                    jsonReader.endArray();
                    continue;
                }
                case 10: {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        list.add(MaskParser.parse(jsonReader, lottieComposition));
                    }
                    lottieComposition.incrementMatteOrMaskCount(list.size());
                    jsonReader.endArray();
                    continue;
                }
                case 9: {
                    none = Layer.MatteType.values()[jsonReader.nextInt()];
                    lottieComposition.incrementMatteOrMaskCount(1);
                    continue;
                }
                case 8: {
                    parse2 = AnimatableTransformParser.parse(jsonReader, lottieComposition);
                    continue;
                }
                case 7: {
                    color = Color.parseColor(jsonReader.nextString());
                    continue;
                }
                case 6: {
                    n7 = (int)(jsonReader.nextInt() * Utils.dpScale());
                    continue;
                }
                case 5: {
                    n6 = (int)(jsonReader.nextInt() * Utils.dpScale());
                    continue;
                }
                case 4: {
                    n2 = jsonReader.nextInt();
                    continue;
                }
                case 3: {
                    final int nextInt = jsonReader.nextInt();
                    if (nextInt < Layer.LayerType.UNKNOWN.ordinal()) {
                        unknown = Layer.LayerType.values()[nextInt];
                        continue;
                    }
                    unknown = Layer.LayerType.UNKNOWN;
                    continue;
                }
                case 2: {
                    nextString = jsonReader.nextString();
                    continue;
                }
                case 1: {
                    n = jsonReader.nextInt();
                    continue;
                }
                case 0: {
                    nextString3 = jsonReader.nextString();
                    continue;
                }
            }
        }
        jsonReader.endObject();
        final float f = n3 / n5;
        float endFrame = n4 / n5;
        final ArrayList<Keyframe<Float>> list3 = new ArrayList<Keyframe<Float>>();
        if (f > 0.0f) {
            list3.add(new Keyframe<Float>(lottieComposition, value2, value2, null, 0.0f, f));
        }
        if (endFrame <= 0.0f) {
            endFrame = lottieComposition.getEndFrame();
        }
        list3.add(new Keyframe<Float>(lottieComposition, value, value, null, f, endFrame));
        list3.add(new Keyframe<Float>(lottieComposition, value2, value2, null, endFrame, Float.MAX_VALUE));
        if (nextString3.endsWith(".ai") || "ai".equals(nextString2)) {
            lottieComposition.addWarning("Convert your Illustrator layers to shape layers.");
        }
        return new Layer(list2, lottieComposition, nextString3, n, (Layer.LayerType)unknown, n2, (String)nextString, list, (AnimatableTransform)parse2, n6, n7, color, n5, n10, n8, n9, (AnimatableTextFrame)documentData, (AnimatableTextProperties)parse, list3, (Layer.MatteType)none, (AnimatableFloatValue)float1, nextBoolean);
    }
    
    public static Layer parse(final LottieComposition lottieComposition) {
        final Rect bounds = lottieComposition.getBounds();
        return new Layer(Collections.emptyList(), lottieComposition, "__container", -1L, Layer.LayerType.PRE_COMP, -1L, null, Collections.emptyList(), new AnimatableTransform(), 0, 0, 0, 0.0f, 0.0f, bounds.width(), bounds.height(), null, null, Collections.emptyList(), Layer.MatteType.NONE, null, false);
    }
}
