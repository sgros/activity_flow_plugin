package com.airbnb.lottie.parser;

import android.util.JsonReader;
import android.util.JsonToken;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.animation.keyframe.PathKeyframe;
import com.airbnb.lottie.utils.Utils;
import java.io.IOException;

class PathKeyframeParser {
   static PathKeyframe parse(JsonReader var0, LottieComposition var1) throws IOException {
      boolean var2;
      if (var0.peek() == JsonToken.BEGIN_OBJECT) {
         var2 = true;
      } else {
         var2 = false;
      }

      return new PathKeyframe(var1, KeyframeParser.parse(var0, var1, Utils.dpScale(), PathParser.INSTANCE, var2));
   }
}
