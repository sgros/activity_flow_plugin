package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.content.RectangleShape;
import java.io.IOException;

class RectangleShapeParser {
   static RectangleShape parse(JsonReader var0, LottieComposition var1) throws IOException {
      String var2 = null;
      AnimatableValue var3 = null;
      Object var4 = var3;
      Object var5 = var3;

      while(var0.hasNext()) {
         String var6 = var0.nextName();
         byte var7 = -1;
         int var8 = var6.hashCode();
         if (var8 != 112) {
            if (var8 != 3519) {
               switch(var8) {
               case 114:
                  if (var6.equals("r")) {
                     var7 = 3;
                  }
                  break;
               case 115:
                  if (var6.equals("s")) {
                     var7 = 2;
                  }
               }
            } else if (var6.equals("nm")) {
               var7 = 0;
            }
         } else if (var6.equals("p")) {
            var7 = 1;
         }

         switch(var7) {
         case 0:
            var2 = var0.nextString();
            break;
         case 1:
            var3 = AnimatablePathValueParser.parseSplitPath(var0, var1);
            break;
         case 2:
            var4 = AnimatableValueParser.parsePoint(var0, var1);
            break;
         case 3:
            var5 = AnimatableValueParser.parseFloat(var0, var1);
            break;
         default:
            var0.skipValue();
         }
      }

      return new RectangleShape(var2, var3, (AnimatablePointValue)var4, (AnimatableFloatValue)var5);
   }
}
