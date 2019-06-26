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
      boolean var11 = false;

      while(var0.hasNext()) {
         byte var14;
         label74: {
            String var12 = var0.nextName();
            int var13 = var12.hashCode();
            if (var13 != 112) {
               if (var13 != 114) {
                  if (var13 != 3324) {
                     if (var13 != 3519) {
                        if (var13 != 3588) {
                           if (var13 != 3686) {
                              if (var13 != 3369) {
                                 if (var13 != 3370) {
                                    if (var13 != 3555) {
                                       if (var13 == 3556 && var12.equals("os")) {
                                          var14 = 6;
                                          break label74;
                                       }
                                    } else if (var12.equals("or")) {
                                       var14 = 5;
                                       break label74;
                                    }
                                 } else if (var12.equals("is")) {
                                    var14 = 8;
                                    break label74;
                                 }
                              } else if (var12.equals("ir")) {
                                 var14 = 7;
                                 break label74;
                              }
                           } else if (var12.equals("sy")) {
                              var14 = 1;
                              break label74;
                           }
                        } else if (var12.equals("pt")) {
                           var14 = 2;
                           break label74;
                        }
                     } else if (var12.equals("nm")) {
                        var14 = 0;
                        break label74;
                     }
                  } else if (var12.equals("hd")) {
                     var14 = 9;
                     break label74;
                  }
               } else if (var12.equals("r")) {
                  var14 = 4;
                  break label74;
               }
            } else if (var12.equals("p")) {
               var14 = 3;
               break label74;
            }

            var14 = -1;
         }

         switch(var14) {
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
         case 9:
            var11 = var0.nextBoolean();
            break;
         default:
            var0.skipValue();
         }
      }

      return new PolystarShape(var2, (PolystarShape.Type)var3, (AnimatableFloatValue)var4, (AnimatableValue)var5, (AnimatableFloatValue)var6, (AnimatableFloatValue)var7, (AnimatableFloatValue)var8, (AnimatableFloatValue)var9, (AnimatableFloatValue)var10, var11);
   }
}
