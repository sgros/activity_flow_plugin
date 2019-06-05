package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;
import com.airbnb.lottie.model.content.ShapePath;
import java.io.IOException;

class ShapePathParser {
   static ShapePath parse(JsonReader var0, LottieComposition var1) throws IOException {
      String var2 = null;
      AnimatableShapeValue var3 = null;
      int var4 = 0;

      while(var0.hasNext()) {
         byte var7;
         label32: {
            String var5 = var0.nextName();
            int var6 = var5.hashCode();
            if (var6 != 3432) {
               if (var6 != 3519) {
                  if (var6 == 104415 && var5.equals("ind")) {
                     var7 = 1;
                     break label32;
                  }
               } else if (var5.equals("nm")) {
                  var7 = 0;
                  break label32;
               }
            } else if (var5.equals("ks")) {
               var7 = 2;
               break label32;
            }

            var7 = -1;
         }

         switch(var7) {
         case 0:
            var2 = var0.nextString();
            break;
         case 1:
            var4 = var0.nextInt();
            break;
         case 2:
            var3 = AnimatableValueParser.parseShapeData(var0, var1);
            break;
         default:
            var0.skipValue();
         }
      }

      return new ShapePath(var2, var4, var3);
   }
}
