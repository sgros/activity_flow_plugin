package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import java.io.IOException;

class ShapeTrimPathParser {
   static ShapeTrimPath parse(JsonReader var0, LottieComposition var1) throws IOException {
      String var2 = null;
      Object var3 = var2;
      Object var4 = var2;
      Object var5 = var2;
      Object var6 = var2;

      while(var0.hasNext()) {
         String var7 = var0.nextName();
         byte var8 = -1;
         int var9 = var7.hashCode();
         if (var9 != 101) {
            if (var9 != 109) {
               if (var9 != 111) {
                  if (var9 != 115) {
                     if (var9 == 3519 && var7.equals("nm")) {
                        var8 = 3;
                     }
                  } else if (var7.equals("s")) {
                     var8 = 0;
                  }
               } else if (var7.equals("o")) {
                  var8 = 2;
               }
            } else if (var7.equals("m")) {
               var8 = 4;
            }
         } else if (var7.equals("e")) {
            var8 = 1;
         }

         switch(var8) {
         case 0:
            var4 = AnimatableValueParser.parseFloat(var0, var1, false);
            break;
         case 1:
            var5 = AnimatableValueParser.parseFloat(var0, var1, false);
            break;
         case 2:
            var6 = AnimatableValueParser.parseFloat(var0, var1, false);
            break;
         case 3:
            var2 = var0.nextString();
            break;
         case 4:
            var3 = ShapeTrimPath.Type.forId(var0.nextInt());
            break;
         default:
            var0.skipValue();
         }
      }

      return new ShapeTrimPath(var2, (ShapeTrimPath.Type)var3, (AnimatableFloatValue)var4, (AnimatableFloatValue)var5, (AnimatableFloatValue)var6);
   }
}
