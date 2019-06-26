package com.airbnb.lottie.parser;

import android.graphics.Path.FillType;
import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.content.ShapeFill;
import java.io.IOException;

class ShapeFillParser {
    static ShapeFill parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        String str = null;
        AnimatableColorValue animatableColorValue = str;
        AnimatableIntegerValue animatableIntegerValue = animatableColorValue;
        int i = 1;
        boolean z = false;
        boolean z2 = false;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            int i2 = -1;
            int hashCode = nextName.hashCode();
            if (hashCode != -396065730) {
                if (hashCode != 99) {
                    if (hashCode != 111) {
                        if (hashCode != 114) {
                            if (hashCode != 3324) {
                                if (hashCode == 3519 && nextName.equals("nm")) {
                                    i2 = 0;
                                }
                            } else if (nextName.equals("hd")) {
                                i2 = 5;
                            }
                        } else if (nextName.equals("r")) {
                            i2 = 4;
                        }
                    } else if (nextName.equals("o")) {
                        i2 = 2;
                    }
                } else if (nextName.equals("c")) {
                    i2 = 1;
                }
            } else if (nextName.equals("fillEnabled")) {
                i2 = 3;
            }
            if (i2 == 0) {
                str = jsonReader.nextString();
            } else if (i2 == 1) {
                animatableColorValue = AnimatableValueParser.parseColor(jsonReader, lottieComposition);
            } else if (i2 == 2) {
                animatableIntegerValue = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
            } else if (i2 == 3) {
                z = jsonReader.nextBoolean();
            } else if (i2 == 4) {
                i = jsonReader.nextInt();
            } else if (i2 != 5) {
                jsonReader.skipValue();
            } else {
                z2 = jsonReader.nextBoolean();
            }
        }
        return new ShapeFill(str, z, i == 1 ? FillType.WINDING : FillType.EVEN_ODD, animatableColorValue, animatableIntegerValue, z2);
    }
}
