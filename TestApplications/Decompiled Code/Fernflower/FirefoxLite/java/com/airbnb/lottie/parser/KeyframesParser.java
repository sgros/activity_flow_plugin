package com.airbnb.lottie.parser;

import android.util.JsonReader;
import android.util.JsonToken;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.value.Keyframe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class KeyframesParser {
   static List parse(JsonReader var0, LottieComposition var1, float var2, ValueParser var3) throws IOException {
      ArrayList var4 = new ArrayList();
      if (var0.peek() == JsonToken.STRING) {
         var1.addWarning("Lottie doesn't support expressions.");
         return var4;
      } else {
         var0.beginObject();

         while(true) {
            while(var0.hasNext()) {
               String var5 = var0.nextName();
               byte var6 = -1;
               if (var5.hashCode() == 107 && var5.equals("k")) {
                  var6 = 0;
               }

               if (var6 != 0) {
                  var0.skipValue();
               } else if (var0.peek() != JsonToken.BEGIN_ARRAY) {
                  var4.add(KeyframeParser.parse(var0, var1, var2, var3, false));
               } else {
                  var0.beginArray();
                  if (var0.peek() == JsonToken.NUMBER) {
                     var4.add(KeyframeParser.parse(var0, var1, var2, var3, false));
                  } else {
                     while(var0.hasNext()) {
                        var4.add(KeyframeParser.parse(var0, var1, var2, var3, true));
                     }
                  }

                  var0.endArray();
               }
            }

            var0.endObject();
            setEndFrames(var4);
            return var4;
         }
      }
   }

   public static void setEndFrames(List var0) {
      int var1 = var0.size();
      int var2 = 0;

      while(true) {
         int var3 = var1 - 1;
         Keyframe var4;
         if (var2 >= var3) {
            var4 = (Keyframe)var0.get(var3);
            if (var4.startValue == null) {
               var0.remove(var4);
            }

            return;
         }

         var4 = (Keyframe)var0.get(var2);
         ++var2;
         var4.endFrame = ((Keyframe)var0.get(var2)).startFrame;
      }
   }
}
