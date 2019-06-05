package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.content.PolystarShape;
import java.io.IOException;

class PolystarShapeParser {
   static PolystarShape parse(JsonReader var0, LottieComposition var1) throws IOException {
      String var2 = null;
      Object var3 = var2;
      Object var4 = var2;
      Object var5 = var2;
      Object var6 = var2;
      Object var7 = var2;
      Object var8 = var2;
      Object var9 = var2;
      Object var10 = var2;

      while(var0.hasNext()) {
         String var11 = var0.nextName();
         byte var12 = -1;
         switch(var11.hashCode()) {
         case 112:
            if (var11.equals("p")) {
               var12 = 3;
            }
            break;
         case 114:
            if (var11.equals("r")) {
               var12 = 4;
            }
            break;
         case 3369:
            if (var11.equals("ir")) {
               var12 = 7;
            }
            break;
         case 3370:
            if (var11.equals("is")) {
               var12 = 8;
            }
            break;
         case 3519:
            if (var11.equals("nm")) {
               var12 = 0;
            }
            break;
         case 3555:
            if (var11.equals("or")) {
               var12 = 5;
            }
            break;
         case 3556:
            if (var11.equals("os")) {
               var12 = 6;
            }
            break;
         case 3588:
            if (var11.equals("pt")) {
               var12 = 2;
            }
            break;
         case 3686:
            if (var11.equals("sy")) {
               var12 = 1;
            }
         }

         switch(var12) {
         case 0:
            var2 = var0.nextString();
            break;
         case 1:
            var3 = PolystarShape.Type.forValue(var0.nextInt());
            break;
         case 2:
            var4 = AnimatableValueParser.parseFloat(var0, var1, false);
            break;
         case 3:
            var5 = AnimatablePathValueParser.parseSplitPath(var0, var1);
            break;
         case 4:
            var6 = AnimatableValueParser.parseFloat(var0, var1, false);
            break;
         case 5:
            var8 = AnimatableValueParser.parseFloat(var0, var1);
            break;
         case 6:
            var10 = AnimatableValueParser.parseFloat(var0, var1, false);
            break;
         case 7:
            var7 = AnimatableValueParser.parseFloat(var0, var1);
            break;
         case 8:
            var9 = AnimatableValueParser.parseFloat(var0, var1, false);
            break;
         default:
            var0.skipValue();
         }
      }

      return new PolystarShape(var2, (PolystarShape.Type)var3, (AnimatableFloatValue)var4, (AnimatableValue)var5, (AnimatableFloatValue)var6, (AnimatableFloatValue)var7, (AnimatableFloatValue)var8, (AnimatableFloatValue)var9, (AnimatableFloatValue)var10);
   }
}
