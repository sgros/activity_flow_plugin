package com.airbnb.lottie.parser;

import android.graphics.Path.FillType;
import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.content.ShapeFill;
import java.io.IOException;

class ShapeFillParser {
   static ShapeFill parse(JsonReader var0, LottieComposition var1) throws IOException {
      String var2 = null;
      Object var3 = var2;
      Object var4 = var2;
      int var5 = 1;
      boolean var6 = false;

      while(var0.hasNext()) {
         byte var10;
         label50: {
            String var7 = var0.nextName();
            int var8 = var7.hashCode();
            if (var8 != -396065730) {
               if (var8 != 99) {
                  if (var8 != 111) {
                     if (var8 != 114) {
                        if (var8 == 3519 && var7.equals("nm")) {
                           var10 = 0;
                           break label50;
                        }
                     } else if (var7.equals("r")) {
                        var10 = 4;
                        break label50;
                     }
                  } else if (var7.equals("o")) {
                     var10 = 2;
                     break label50;
                  }
               } else if (var7.equals("c")) {
                  var10 = 1;
                  break label50;
               }
            } else if (var7.equals("fillEnabled")) {
               var10 = 3;
               break label50;
            }

            var10 = -1;
         }

         switch(var10) {
         case 0:
            var2 = var0.nextString();
            break;
         case 1:
            var3 = AnimatableValueParser.parseColor(var0, var1);
            break;
         case 2:
            var4 = AnimatableValueParser.parseInteger(var0, var1);
            break;
         case 3:
            var6 = var0.nextBoolean();
            break;
         case 4:
            var5 = var0.nextInt();
            break;
         default:
            var0.skipValue();
         }
      }

      FillType var9;
      if (var5 == 1) {
         var9 = FillType.WINDING;
      } else {
         var9 = FillType.EVEN_ODD;
      }

      return new ShapeFill(var2, var6, var9, (AnimatableColorValue)var3, (AnimatableIntegerValue)var4);
   }
}
