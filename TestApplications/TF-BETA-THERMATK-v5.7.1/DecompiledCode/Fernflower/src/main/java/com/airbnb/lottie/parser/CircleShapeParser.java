package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.content.CircleShape;
import java.io.IOException;

class CircleShapeParser {
   static CircleShape parse(JsonReader var0, LottieComposition var1, int var2) throws IOException {
      boolean var3;
      if (var2 == 3) {
         var3 = true;
      } else {
         var3 = false;
      }

      String var4 = null;
      Object var5 = var4;
      Object var6 = var4;
      boolean var7 = false;

      while(var0.hasNext()) {
         String var8 = var0.nextName();
         byte var10 = -1;
         int var9 = var8.hashCode();
         if (var9 != 100) {
            if (var9 != 112) {
               if (var9 != 115) {
                  if (var9 != 3324) {
                     if (var9 == 3519 && var8.equals("nm")) {
                        var10 = 0;
                     }
                  } else if (var8.equals("hd")) {
                     var10 = 3;
                  }
               } else if (var8.equals("s")) {
                  var10 = 2;
               }
            } else if (var8.equals("p")) {
               var10 = 1;
            }
         } else if (var8.equals("d")) {
            var10 = 4;
         }

         if (var10 != 0) {
            if (var10 != 1) {
               if (var10 != 2) {
                  if (var10 != 3) {
                     if (var10 != 4) {
                        var0.skipValue();
                     } else if (var0.nextInt() == 3) {
                        var3 = true;
                     } else {
                        var3 = false;
                     }
                  } else {
                     var7 = var0.nextBoolean();
                  }
               } else {
                  var6 = AnimatableValueParser.parsePoint(var0, var1);
               }
            } else {
               var5 = AnimatablePathValueParser.parseSplitPath(var0, var1);
            }
         } else {
            var4 = var0.nextString();
         }
      }

      return new CircleShape(var4, (AnimatableValue)var5, (AnimatablePointValue)var6, var3, var7);
   }
}
