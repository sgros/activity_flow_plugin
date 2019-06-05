// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;
import android.util.Log;
import com.airbnb.lottie.model.content.Mask;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class MaskParser
{
    static Mask parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        jsonReader.beginObject();
        Mask.MaskMode maskMode = null;
        AnimatableIntegerValue integer;
        Object shapeData = integer = null;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            final int n = 0;
            int n2 = 0;
            Label_0110: {
                if (hashCode != 111) {
                    if (hashCode != 3588) {
                        if (hashCode == 3357091) {
                            if (nextName.equals("mode")) {
                                n2 = 0;
                                break Label_0110;
                            }
                        }
                    }
                    else if (nextName.equals("pt")) {
                        n2 = 1;
                        break Label_0110;
                    }
                }
                else if (nextName.equals("o")) {
                    n2 = 2;
                    break Label_0110;
                }
                n2 = -1;
            }
            switch (n2) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 2: {
                    integer = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
                    continue;
                }
                case 1: {
                    shapeData = AnimatableValueParser.parseShapeData(jsonReader, lottieComposition);
                    continue;
                }
                case 0: {
                    final String nextString = jsonReader.nextString();
                    final int hashCode2 = nextString.hashCode();
                    int n3 = 0;
                    Label_0250: {
                        if (hashCode2 != 97) {
                            if (hashCode2 != 105) {
                                if (hashCode2 == 115) {
                                    if (nextString.equals("s")) {
                                        n3 = 1;
                                        break Label_0250;
                                    }
                                }
                            }
                            else if (nextString.equals("i")) {
                                n3 = 2;
                                break Label_0250;
                            }
                        }
                        else if (nextString.equals("a")) {
                            n3 = n;
                            break Label_0250;
                        }
                        n3 = -1;
                    }
                    switch (n3) {
                        default: {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unknown mask mode ");
                            sb.append(nextName);
                            sb.append(". Defaulting to Add.");
                            Log.w("LOTTIE", sb.toString());
                            maskMode = Mask.MaskMode.MaskModeAdd;
                            continue;
                        }
                        case 2: {
                            lottieComposition.addWarning("Animation contains intersect masks. They are not supported but will be treated like add masks.");
                            maskMode = Mask.MaskMode.MaskModeIntersect;
                            continue;
                        }
                        case 1: {
                            maskMode = Mask.MaskMode.MaskModeSubtract;
                            continue;
                        }
                        case 0: {
                            maskMode = Mask.MaskMode.MaskModeAdd;
                            continue;
                        }
                    }
                    break;
                }
            }
        }
        jsonReader.endObject();
        return new Mask(maskMode, (AnimatableShapeValue)shapeData, integer);
    }
}
