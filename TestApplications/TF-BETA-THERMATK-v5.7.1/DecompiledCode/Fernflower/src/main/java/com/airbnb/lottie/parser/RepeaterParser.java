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
      Object var3 = var2;
      Object var4 = var2;
      Object var5 = var2;
      boolean var6 = false;

      while(var0.hasNext()) {
         String var7 = var0.nextName();
         byte var8 = -1;
         int var9 = var7.hashCode();
         if (var9 != 99) {
            if (var9 != 111) {
               if (var9 != 3324) {
                  if (var9 != 3519) {
                     if (var9 == 3710 && var7.equals("tr")) {
                        var8 = 3;
                     }
                  } else if (var7.equals("nm")) {
                     var8 = 0;
                  }
               } else if (var7.equals("hd")) {
                  var8 = 4;
               }
            } else if (var7.equals("o")) {
               var8 = 2;
            }
         } else if (var7.equals("c")) {
            var8 = 1;
         }

         if (var8 != 0) {
            if (var8 != 1) {
               if (var8 != 2) {
                  if (var8 != 3) {
                     if (var8 != 4) {
                        var0.skipValue();
                     } else {
                        var6 = var0.nextBoolean();
                     }
                  } else {
                     var5 = AnimatableTransformParser.parse(var0, var1);
                  }
               } else {
                  var4 = AnimatableValueParser.parseFloat(var0, var1, false);
               }
            } else {
               var3 = AnimatableValueParser.parseFloat(var0, var1, false);
            }
         } else {
            var2 = var0.nextString();
         }
      }

      return new Repeater(var2, (AnimatableFloatValue)var3, (AnimatableFloatValue)var4, (AnimatableTransform)var5, var6);
   }
}
