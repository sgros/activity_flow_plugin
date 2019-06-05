package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.content.Repeater;
import java.io.IOException;

class RepeaterParser {
   static Repeater parse(JsonReader var0, LottieComposition var1) throws IOException {
      String var2 = null;
      AnimatableFloatValue var3 = null;
      AnimatableFloatValue var4 = var3;
      Object var5 = var3;

      while(var0.hasNext()) {
         String var6 = var0.nextName();
         byte var7 = -1;
         int var8 = var6.hashCode();
         if (var8 != 99) {
            if (var8 != 111) {
               if (var8 != 3519) {
                  if (var8 == 3710 && var6.equals("tr")) {
                     var7 = 3;
                  }
               } else if (var6.equals("nm")) {
                  var7 = 0;
               }
            } else if (var6.equals("o")) {
               var7 = 2;
            }
         } else if (var6.equals("c")) {
            var7 = 1;
         }

         switch(var7) {
         case 0:
            var2 = var0.nextString();
            break;
         case 1:
            var3 = AnimatableValueParser.parseFloat(var0, var1, false);
            break;
         case 2:
            var4 = AnimatableValueParser.parseFloat(var0, var1, false);
            break;
         case 3:
            var5 = AnimatableTransformParser.parse(var0, var1);
            break;
         default:
            var0.skipValue();
         }
      }

      return new Repeater(var2, var3, var4, (AnimatableTransform)var5);
   }
}
