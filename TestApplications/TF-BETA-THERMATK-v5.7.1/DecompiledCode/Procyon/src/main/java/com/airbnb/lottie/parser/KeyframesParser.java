// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import com.airbnb.lottie.animation.keyframe.PathKeyframe;
import java.io.IOException;
import android.util.JsonToken;
import java.util.ArrayList;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class KeyframesParser
{
    static <T> List<Keyframe<T>> parse(final JsonReader jsonReader, final LottieComposition lottieComposition, final float n, final ValueParser<T> valueParser) throws IOException {
        final ArrayList<Keyframe<Object>> endFrames = new ArrayList<Keyframe<Object>>();
        if (jsonReader.peek() == JsonToken.STRING) {
            lottieComposition.addWarning("Lottie doesn't support expressions.");
            return (List<Keyframe<T>>)endFrames;
        }
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n2 = -1;
            if (nextName.hashCode() == 107) {
                if (nextName.equals("k")) {
                    n2 = 0;
                }
            }
            if (n2 != 0) {
                jsonReader.skipValue();
            }
            else if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
                jsonReader.beginArray();
                if (jsonReader.peek() == JsonToken.NUMBER) {
                    endFrames.add(KeyframeParser.parse(jsonReader, lottieComposition, n, valueParser, false));
                }
                else {
                    while (jsonReader.hasNext()) {
                        endFrames.add(KeyframeParser.parse(jsonReader, lottieComposition, n, valueParser, true));
                    }
                }
                jsonReader.endArray();
            }
            else {
                endFrames.add(KeyframeParser.parse(jsonReader, lottieComposition, n, valueParser, false));
            }
        }
        jsonReader.endObject();
        setEndFrames((List<? extends Keyframe<Object>>)endFrames);
        return (List<Keyframe<T>>)endFrames;
    }
    
    public static <T> void setEndFrames(final List<? extends Keyframe<T>> list) {
        final int size = list.size();
        int n = 0;
        int n2;
        while (true) {
            n2 = size - 1;
            if (n >= n2) {
                break;
            }
            final Keyframe keyframe = (Keyframe)list.get(n);
            final int n3 = n + 1;
            final Keyframe keyframe2 = (Keyframe)list.get(n3);
            keyframe.endFrame = keyframe2.startFrame;
            n = n3;
            if (keyframe.endValue != null) {
                continue;
            }
            final T startValue = keyframe2.startValue;
            n = n3;
            if (startValue == null) {
                continue;
            }
            keyframe.endValue = startValue;
            n = n3;
            if (!(keyframe instanceof PathKeyframe)) {
                continue;
            }
            ((PathKeyframe)keyframe).createPath();
            n = n3;
        }
        final Keyframe keyframe3 = (Keyframe)list.get(n2);
        if ((keyframe3.startValue == null || keyframe3.endValue == null) && list.size() > 1) {
            list.remove(keyframe3);
        }
    }
}
