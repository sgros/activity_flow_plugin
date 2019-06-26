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
      boolean var7 = false;

      while(var0.hasNext()) {
         byte var10;
         label61: {
            String var8 = var0.nextName();
            int var9 = var8.hashCode();
            if (var9 != 101) {
               if (var9 != 109) {
                  if (var9 != 111) {
                     if (var9 != 115) {
                        if (var9 != 3324) {
                           if (var9 == 3519 && var8.equals("nm")) {
                              var10 = 3;
                              break label61;
                           }
                        } else if (var8.equals("hd")) {
                           var10 = 5;
                           break label61;
                        }
                     } else if (var8.equals("s")) {
                        var10 = 0;
                        break label61;
                     }
                  } else if (var8.equals("o")) {
                     var10 = 2;
                     break label61;
                  }
               } else if (var8.equals("m")) {
                  var10 = 4;
                  break label61;
               }
            } else if (var8.equals("e")) {
               var10 = 1;
               break label61;
            }

            var10 = -1;
         }

         if (var10 != 0) {
            if (var10 != 1) {
               if (var10 != 2) {
                  if (var10 != 3) {
                     if (var10 != 4) {
                        if (var10 != 5) {
                           var0.skipValue();
                        } else {
                           var7 = var0.nextBoolean();
                        }
                     } else {
                        var3 = ShapeTrimPath.Type.forId(var0.nextInt());
                     }
                  } else {
                     var2 = var0.nextString();
                  }
               } else {
                  var6 = AnimatableValueParser.parseFloat(var0, var1, false);
               }
            } else {
               var5 = AnimatableValueParser.parseFloat(var0, var1, false);
            }
         } else {
            var4 = AnimatableValueParser.parseFloat(var0, var1, false);
         }
      }

      return new ShapeTrimPath(var2, (ShapeTrimPath.Type)var3, (AnimatableFloatValue)var4, (AnimatableFloatValue)var5, (AnimatableFloatValue)var6, var7);
   }
}
