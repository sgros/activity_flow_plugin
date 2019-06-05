// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableSplitDimensionPathValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import java.io.IOException;
import com.airbnb.lottie.animation.keyframe.PathKeyframe;
import com.airbnb.lottie.utils.Utils;
import java.util.List;
import android.util.JsonToken;
import android.graphics.PointF;
import com.airbnb.lottie.value.Keyframe;
import java.util.ArrayList;
import com.airbnb.lottie.model.animatable.AnimatablePathValue;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

public class AnimatablePathValueParser
{
    public static AnimatablePathValue parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        final ArrayList<Keyframe<PointF>> endFrames = new ArrayList<Keyframe<PointF>>();
        if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                endFrames.add(PathKeyframeParser.parse(jsonReader, lottieComposition));
            }
            jsonReader.endArray();
            KeyframesParser.setEndFrames(endFrames);
        }
        else {
            endFrames.add(new Keyframe<PointF>(JsonUtils.jsonToPoint(jsonReader, Utils.dpScale())));
        }
        return new AnimatablePathValue(endFrames);
    }
    
    static AnimatableValue<PointF, PointF> parseSplitPath(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        jsonReader.beginObject();
        Object parse = null;
        Object float2;
        Object float1 = float2 = parse;
        boolean b = false;
        while (jsonReader.peek() != JsonToken.END_OBJECT) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n = 0;
            Label_0122: {
                if (hashCode != 107) {
                    switch (hashCode) {
                        case 121: {
                            if (nextName.equals("y")) {
                                n = 2;
                                break Label_0122;
                            }
                            break;
                        }
                        case 120: {
                            if (nextName.equals("x")) {
                                n = 1;
                                break Label_0122;
                            }
                            break;
                        }
                    }
                }
                else if (nextName.equals("k")) {
                    n = 0;
                    break Label_0122;
                }
                n = -1;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 2: {
                    if (jsonReader.peek() == JsonToken.STRING) {
                        jsonReader.skipValue();
                        break;
                    }
                    float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    continue;
                }
                case 1: {
                    if (jsonReader.peek() == JsonToken.STRING) {
                        jsonReader.skipValue();
                        break;
                    }
                    float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    continue;
                }
                case 0: {
                    parse = parse(jsonReader, lottieComposition);
                    continue;
                }
            }
            b = true;
        }
        jsonReader.endObject();
        if (b) {
            lottieComposition.addWarning("Lottie doesn't support expressions.");
        }
        if (parse != null) {
            return (AnimatableValue<PointF, PointF>)parse;
        }
        return new AnimatableSplitDimensionPathValue((AnimatableFloatValue)float1, (AnimatableFloatValue)float2);
    }
}
