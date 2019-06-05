package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.content.ShapeTrimPath.Type;
import java.io.IOException;

class ShapeTrimPathParser {
    static ShapeTrimPath parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        String str = null;
        Type type = str;
        AnimatableFloatValue animatableFloatValue = type;
        AnimatableFloatValue animatableFloatValue2 = animatableFloatValue;
        AnimatableFloatValue animatableFloatValue3 = animatableFloatValue2;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            Object obj = -1;
            int hashCode = nextName.hashCode();
            if (hashCode != 101) {
                if (hashCode != 109) {
                    if (hashCode != 111) {
                        if (hashCode != 115) {
                            if (hashCode == 3519 && nextName.equals("nm")) {
                                obj = 3;
                            }
                        } else if (nextName.equals("s")) {
                            obj = null;
                        }
                    } else if (nextName.equals("o")) {
                        obj = 2;
                    }
                } else if (nextName.equals("m")) {
                    obj = 4;
                }
            } else if (nextName.equals("e")) {
                obj = 1;
            }
            switch (obj) {
                case null:
                    animatableFloatValue = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    break;
                case 1:
                    animatableFloatValue2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    break;
                case 2:
                    animatableFloatValue3 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    break;
                case 3:
                    str = jsonReader.nextString();
                    break;
                case 4:
                    type = Type.forId(jsonReader.nextInt());
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        return new ShapeTrimPath(str, type, animatableFloatValue, animatableFloatValue2, animatableFloatValue3);
    }
}
