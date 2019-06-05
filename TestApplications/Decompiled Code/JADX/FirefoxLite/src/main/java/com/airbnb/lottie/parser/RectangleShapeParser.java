package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.content.RectangleShape;
import java.io.IOException;

class RectangleShapeParser {
    static RectangleShape parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        String str = null;
        AnimatableValue animatableValue = null;
        AnimatablePointValue animatablePointValue = animatableValue;
        AnimatableFloatValue animatableFloatValue = animatablePointValue;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            Object obj = -1;
            int hashCode = nextName.hashCode();
            if (hashCode != 112) {
                if (hashCode != 3519) {
                    switch (hashCode) {
                        case 114:
                            if (nextName.equals("r")) {
                                obj = 3;
                                break;
                            }
                            break;
                        case 115:
                            if (nextName.equals("s")) {
                                obj = 2;
                                break;
                            }
                            break;
                    }
                } else if (nextName.equals("nm")) {
                    obj = null;
                }
            } else if (nextName.equals("p")) {
                obj = 1;
            }
            switch (obj) {
                case null:
                    str = jsonReader.nextString();
                    break;
                case 1:
                    animatableValue = AnimatablePathValueParser.parseSplitPath(jsonReader, lottieComposition);
                    break;
                case 2:
                    animatablePointValue = AnimatableValueParser.parsePoint(jsonReader, lottieComposition);
                    break;
                case 3:
                    animatableFloatValue = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        return new RectangleShape(str, animatableValue, animatablePointValue, animatableFloatValue);
    }
}
