package com.airbnb.lottie.parser;

import android.util.JsonReader;
import android.util.Log;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;
import com.airbnb.lottie.model.content.Mask;
import java.io.IOException;

class MaskParser {
   static Mask parse(JsonReader var0, LottieComposition var1) throws IOException {
      var0.beginObject();
      Mask.MaskMode var2 = null;
      AnimatableShapeValue var3 = null;
      Object var4 = var3;

      while(true) {
         while(var0.hasNext()) {
            String var5;
            int var6;
            byte var7;
            byte var10;
            label47: {
               var5 = var0.nextName();
               var6 = var5.hashCode();
               var7 = 0;
               if (var6 != 111) {
                  if (var6 != 3588) {
                     if (var6 == 3357091 && var5.equals("mode")) {
                        var10 = 0;
                        break label47;
                     }
                  } else if (var5.equals("pt")) {
                     var10 = 1;
                     break label47;
                  }
               } else if (var5.equals("o")) {
                  var10 = 2;
                  break label47;
               }

               var10 = -1;
            }

            switch(var10) {
            case 0:
               label63: {
                  String var8 = var0.nextString();
                  var6 = var8.hashCode();
                  if (var6 != 97) {
                     if (var6 != 105) {
                        if (var6 == 115 && var8.equals("s")) {
                           var10 = 1;
                           break label63;
                        }
                     } else if (var8.equals("i")) {
                        var10 = 2;
                        break label63;
                     }
                  } else if (var8.equals("a")) {
                     var10 = var7;
                     break label63;
                  }

                  var10 = -1;
               }

               switch(var10) {
               case 0:
                  var2 = Mask.MaskMode.MaskModeAdd;
                  continue;
               case 1:
                  var2 = Mask.MaskMode.MaskModeSubtract;
                  continue;
               case 2:
                  var1.addWarning("Animation contains intersect masks. They are not supported but will be treated like add masks.");
                  var2 = Mask.MaskMode.MaskModeIntersect;
                  continue;
               default:
                  StringBuilder var9 = new StringBuilder();
                  var9.append("Unknown mask mode ");
                  var9.append(var5);
                  var9.append(". Defaulting to Add.");
                  Log.w("LOTTIE", var9.toString());
                  var2 = Mask.MaskMode.MaskModeAdd;
                  continue;
               }
            case 1:
               var3 = AnimatableValueParser.parseShapeData(var0, var1);
               break;
            case 2:
               var4 = AnimatableValueParser.parseInteger(var0, var1);
               break;
            default:
               var0.skipValue();
            }
         }

         var0.endObject();
         return new Mask(var2, var3, (AnimatableIntegerValue)var4);
      }
   }
}
