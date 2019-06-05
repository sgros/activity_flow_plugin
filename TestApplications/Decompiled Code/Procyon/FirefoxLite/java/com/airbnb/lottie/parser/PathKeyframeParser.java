// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import android.graphics.PointF;
import android.util.JsonToken;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.animation.keyframe.PathKeyframe;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class PathKeyframeParser
{
    static PathKeyframe parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        return new PathKeyframe(lottieComposition, KeyframeParser.parse(jsonReader, lottieComposition, Utils.dpScale(), (ValueParser<PointF>)PathParser.INSTANCE, jsonReader.peek() == JsonToken.BEGIN_OBJECT));
    }
}
