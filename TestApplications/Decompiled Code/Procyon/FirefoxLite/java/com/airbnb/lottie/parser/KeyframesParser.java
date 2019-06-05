// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

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
        final ArrayList<Keyframe<T>> endFrames = new ArrayList<Keyframe<T>>();
        if (jsonReader.peek() == JsonToken.STRING) {
            lottieComposition.addWarning("Lottie doesn't support expressions.");
            return endFrames;
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
        setEndFrames(endFrames);
        return endFrames;
    }
    
    public static void setEndFrames(final List<? extends Keyframe<?>> list) {
        final int size = list.size();
        int n = 0;
        int n2;
        while (true) {
            n2 = size - 1;
            if (n >= n2) {
                break;
            }
            final Keyframe keyframe = (Keyframe)list.get(n);
            ++n;
            keyframe.endFrame = ((Keyframe)list.get(n)).startFrame;
        }
        final Keyframe keyframe2 = (Keyframe)list.get(n2);
        if (keyframe2.startValue == null) {
            list.remove(keyframe2);
        }
    }
}
