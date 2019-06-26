package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.content.CircleShape;
import java.io.IOException;

class CircleShapeParser {
    static CircleShape parse(JsonReader jsonReader, LottieComposition lottieComposition, int i) throws IOException {
        boolean z = i == 3;
        String str = null;
        AnimatableValue animatableValue = str;
        AnimatablePointValue animatablePointValue = animatableValue;
        boolean z2 = false;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            int i2 = -1;
            int hashCode = nextName.hashCode();
            if (hashCode != 100) {
                if (hashCode != 112) {
                    if (hashCode != 115) {
                        if (hashCode != 3324) {
                            if (hashCode == 3519 && nextName.equals("nm")) {
                                i2 = 0;
                            }
                        } else if (nextName.equals("hd")) {
                            i2 = 3;
                        }
                    } else if (nextName.equals("s")) {
                        i2 = 2;
                    }
                } else if (nextName.equals("p")) {
                    i2 = 1;
                }
            } else if (nextName.equals("d")) {
                i2 = 4;
            }
            if (i2 == 0) {
                str = jsonReader.nextString();
            } else if (i2 == 1) {
                animatableValue = AnimatablePathValueParser.parseSplitPath(jsonReader, lottieComposition);
            } else if (i2 == 2) {
                animatablePointValue = AnimatableValueParser.parsePoint(jsonReader, lottieComposition);
            } else if (i2 == 3) {
                z2 = jsonReader.nextBoolean();
            } else if (i2 != 4) {
                jsonReader.skipValue();
            } else {
                z = jsonReader.nextInt() == 3;
            }
        }
        return new CircleShape(str, animatableValue, animatablePointValue, z, z2);
    }
}
