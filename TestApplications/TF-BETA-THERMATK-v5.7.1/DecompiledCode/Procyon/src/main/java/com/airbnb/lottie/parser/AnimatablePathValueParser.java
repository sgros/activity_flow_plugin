// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableSplitDimensionPathValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import java.io.IOException;
import android.graphics.PointF;
import com.airbnb.lottie.utils.Utils;
import java.util.List;
import android.util.JsonToken;
import com.airbnb.lottie.value.Keyframe;
import java.util.ArrayList;
import com.airbnb.lottie.model.animatable.AnimatablePathValue;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

public class AnimatablePathValueParser
{
    public static AnimatablePathValue parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        final ArrayList<Keyframe<PointF>> endFrames = (ArrayList<Keyframe<PointF>>)new ArrayList<Keyframe<Object>>();
        if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                endFrames.add(PathKeyframeParser.parse(jsonReader, lottieComposition));
            }
            jsonReader.endArray();
            KeyframesParser.setEndFrames((List<? extends Keyframe<Object>>)endFrames);
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
            int n = -1;
            final int hashCode = nextName.hashCode();
            if (hashCode != 107) {
                if (hashCode != 120) {
                    if (hashCode == 121) {
                        if (nextName.equals("y")) {
                            n = 2;
                        }
                    }
                }
                else if (nextName.equals("x")) {
                    n = 1;
                }
            }
            else if (nextName.equals("k")) {
                n = 0;
            }
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        jsonReader.skipValue();
                        continue;
                    }
                    if (jsonReader.peek() != JsonToken.STRING) {
                        float2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                        continue;
                    }
                    jsonReader.skipValue();
                }
                else {
                    if (jsonReader.peek() != JsonToken.STRING) {
                        float1 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                        continue;
                    }
                    jsonReader.skipValue();
                }
                b = true;
            }
            else {
                parse = parse(jsonReader, lottieComposition);
            }
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
