package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.content.PolystarShape;
import com.airbnb.lottie.model.content.PolystarShape.Type;
import java.io.IOException;

class PolystarShapeParser {
    static PolystarShape parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        String str = null;
        Type type = str;
        AnimatableFloatValue animatableFloatValue = type;
        AnimatableValue animatableValue = animatableFloatValue;
        AnimatableFloatValue animatableFloatValue2 = animatableValue;
        AnimatableFloatValue animatableFloatValue3 = animatableFloatValue2;
        AnimatableFloatValue animatableFloatValue4 = animatableFloatValue3;
        AnimatableFloatValue animatableFloatValue5 = animatableFloatValue4;
        AnimatableFloatValue animatableFloatValue6 = animatableFloatValue5;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            Object obj = -1;
            switch (nextName.hashCode()) {
                case 112:
                    if (nextName.equals("p")) {
                        obj = 3;
                        break;
                    }
                    break;
                case 114:
                    if (nextName.equals("r")) {
                        obj = 4;
                        break;
                    }
                    break;
                case 3369:
                    if (nextName.equals("ir")) {
                        obj = 7;
                        break;
                    }
                    break;
                case 3370:
                    if (nextName.equals("is")) {
                        obj = 8;
                        break;
                    }
                    break;
                case 3519:
                    if (nextName.equals("nm")) {
                        obj = null;
                        break;
                    }
                    break;
                case 3555:
                    if (nextName.equals("or")) {
                        obj = 5;
                        break;
                    }
                    break;
                case 3556:
                    if (nextName.equals("os")) {
                        obj = 6;
                        break;
                    }
                    break;
                case 3588:
                    if (nextName.equals("pt")) {
                        obj = 2;
                        break;
                    }
                    break;
                case 3686:
                    if (nextName.equals("sy")) {
                        obj = 1;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                    str = jsonReader.nextString();
                    break;
                case 1:
                    type = Type.forValue(jsonReader.nextInt());
                    break;
                case 2:
                    animatableFloatValue = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    break;
                case 3:
                    animatableValue = AnimatablePathValueParser.parseSplitPath(jsonReader, lottieComposition);
                    break;
                case 4:
                    animatableFloatValue2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    break;
                case 5:
                    animatableFloatValue4 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    break;
                case 6:
                    animatableFloatValue6 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    break;
                case 7:
                    animatableFloatValue3 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    break;
                case 8:
                    animatableFloatValue5 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        return new PolystarShape(str, type, animatableFloatValue, animatableValue, animatableFloatValue2, animatableFloatValue3, animatableFloatValue4, animatableFloatValue5, animatableFloatValue6);
    }
}
