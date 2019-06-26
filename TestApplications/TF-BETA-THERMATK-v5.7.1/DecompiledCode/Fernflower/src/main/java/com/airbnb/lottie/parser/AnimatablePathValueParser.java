package com.airbnb.lottie.parser;

import android.util.JsonReader;
import android.util.JsonToken;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatablePathValue;
import com.airbnb.lottie.model.animatable.AnimatableSplitDimensionPathValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;
import java.io.IOException;
import java.util.ArrayList;

public class AnimatablePathValueParser {
   public static AnimatablePathValue parse(JsonReader var0, LottieComposition var1) throws IOException {
      ArrayList var2 = new ArrayList();
      if (var0.peek() == JsonToken.BEGIN_ARRAY) {
         var0.beginArray();

         while(var0.hasNext()) {
            var2.add(PathKeyframeParser.parse(var0, var1));
         }

         var0.endArray();
         KeyframesParser.setEndFrames(var2);
      } else {
         var2.add(new Keyframe(JsonUtils.jsonToPoint(var0, Utils.dpScale())));
      }

      return new AnimatablePathValue(var2);
   }

   static AnimatableValue parseSplitPath(JsonReader var0, LottieComposition var1) throws IOException {
      var0.beginObject();
      AnimatablePathValue var2 = null;
      Object var3 = var2;
      Object var4 = var2;
      boolean var5 = false;

      while(true) {
         while(true) {
            while(var0.peek() != JsonToken.END_OBJECT) {
               String var6 = var0.nextName();
               byte var7 = -1;
               int var8 = var6.hashCode();
               if (var8 != 107) {
                  if (var8 != 120) {
                     if (var8 == 121 && var6.equals("y")) {
                        var7 = 2;
                     }
                  } else if (var6.equals("x")) {
                     var7 = 1;
                  }
               } else if (var6.equals("k")) {
                  var7 = 0;
               }

               if (var7 != 0) {
                  if (var7 != 1) {
                     if (var7 != 2) {
                        var0.skipValue();
                        continue;
                     }

                     if (var0.peek() != JsonToken.STRING) {
                        var4 = AnimatableValueParser.parseFloat(var0, var1);
                        continue;
                     }

                     var0.skipValue();
                  } else {
                     if (var0.peek() != JsonToken.STRING) {
                        var3 = AnimatableValueParser.parseFloat(var0, var1);
                        continue;
                     }

                     var0.skipValue();
                  }

                  var5 = true;
               } else {
                  var2 = parse(var0, var1);
               }
            }

            var0.endObject();
            if (var5) {
               var1.addWarning("Lottie doesn't support expressions.");
            }

            if (var2 != null) {
               return var2;
            }

            return new AnimatableSplitDimensionPathValue((AnimatableFloatValue)var3, (AnimatableFloatValue)var4);
         }
      }
   }
}
