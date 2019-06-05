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
      AnimatableValue var5 = null;
      Object var6 = var5;

      while(var0.hasNext()) {
         byte var8;
         label46: {
            String var7 = var0.nextName();
            var2 = var7.hashCode();
            if (var2 != 100) {
               if (var2 != 112) {
                  if (var2 != 115) {
                     if (var2 == 3519 && var7.equals("nm")) {
                        var8 = 0;
                        break label46;
                     }
                  } else if (var7.equals("s")) {
                     var8 = 2;
                     break label46;
                  }
               } else if (var7.equals("p")) {
                  var8 = 1;
                  break label46;
               }
            } else if (var7.equals("d")) {
               var8 = 3;
               break label46;
            }

            var8 = -1;
         }

         switch(var8) {
         case 0:
            var4 = var0.nextString();
            break;
         case 1:
            var5 = AnimatablePathValueParser.parseSplitPath(var0, var1);
            break;
         case 2:
            var6 = AnimatableValueParser.parsePoint(var0, var1);
            break;
         case 3:
            if (var0.nextInt() == 3) {
               var3 = true;
            } else {
               var3 = false;
            }
            break;
         default:
            var0.skipValue();
         }
      }

      return new CircleShape(var4, var5, (AnimatablePointValue)var6, var3);
   }
}
