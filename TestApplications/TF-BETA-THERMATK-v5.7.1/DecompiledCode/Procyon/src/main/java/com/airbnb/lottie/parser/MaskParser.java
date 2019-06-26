// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;
import com.airbnb.lottie.utils.Logger;
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
        boolean nextBoolean = false;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            final int n = -1;
            int n2 = 0;
            Label_0136: {
                if (hashCode != 111) {
                    if (hashCode != 3588) {
                        if (hashCode != 104433) {
                            if (hashCode == 3357091) {
                                if (nextName.equals("mode")) {
                                    n2 = 0;
                                    break Label_0136;
                                }
                            }
                        }
                        else if (nextName.equals("inv")) {
                            n2 = 3;
                            break Label_0136;
                        }
                    }
                    else if (nextName.equals("pt")) {
                        n2 = 1;
                        break Label_0136;
                    }
                }
                else if (nextName.equals("o")) {
                    n2 = 2;
                    break Label_0136;
                }
                n2 = -1;
            }
            if (n2 != 0) {
                if (n2 != 1) {
                    if (n2 != 2) {
                        if (n2 != 3) {
                            jsonReader.skipValue();
                        }
                        else {
                            nextBoolean = jsonReader.nextBoolean();
                        }
                    }
                    else {
                        integer = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
                    }
                }
                else {
                    shapeData = AnimatableValueParser.parseShapeData(jsonReader, lottieComposition);
                }
            }
            else {
                final String nextString = jsonReader.nextString();
                final int hashCode2 = nextString.hashCode();
                int n3;
                if (hashCode2 != 97) {
                    if (hashCode2 != 105) {
                        if (hashCode2 != 115) {
                            n3 = n;
                        }
                        else {
                            n3 = n;
                            if (nextString.equals("s")) {
                                n3 = 1;
                            }
                        }
                    }
                    else {
                        n3 = n;
                        if (nextString.equals("i")) {
                            n3 = 2;
                        }
                    }
                }
                else {
                    n3 = n;
                    if (nextString.equals("a")) {
                        n3 = 0;
                    }
                }
                if (n3 != 0) {
                    if (n3 != 1) {
                        if (n3 != 2) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unknown mask mode ");
                            sb.append(nextName);
                            sb.append(". Defaulting to Add.");
                            Logger.warning(sb.toString());
                            maskMode = Mask.MaskMode.MASK_MODE_ADD;
                        }
                        else {
                            lottieComposition.addWarning("Animation contains intersect masks. They are not supported but will be treated like add masks.");
                            maskMode = Mask.MaskMode.MASK_MODE_INTERSECT;
                        }
                    }
                    else {
                        maskMode = Mask.MaskMode.MASK_MODE_SUBTRACT;
                    }
                }
                else {
                    maskMode = Mask.MaskMode.MASK_MODE_ADD;
                }
            }
        }
        jsonReader.endObject();
        return new Mask(maskMode, (AnimatableShapeValue)shapeData, integer, nextBoolean);
    }
}
