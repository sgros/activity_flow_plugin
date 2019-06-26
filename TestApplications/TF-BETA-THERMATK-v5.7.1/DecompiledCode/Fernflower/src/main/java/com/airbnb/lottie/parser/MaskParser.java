package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;
import com.airbnb.lottie.model.content.Mask;
import com.airbnb.lottie.utils.Logger;
import java.io.IOException;

class MaskParser {
   static Mask parse(JsonReader var0, LottieComposition var1) throws IOException {
      var0.beginObject();
      Mask.MaskMode var2 = null;
      AnimatableShapeValue var3 = null;
      Object var4 = var3;
      boolean var5 = false;

      while(var0.hasNext()) {
         String var6;
         int var7;
         byte var8;
         byte var11;
         label70: {
            var6 = var0.nextName();
            var7 = var6.hashCode();
            var8 = -1;
            if (var7 != 111) {
               if (var7 != 3588) {
                  if (var7 != 104433) {
                     if (var7 == 3357091 && var6.equals("mode")) {
                        var11 = 0;
                        break label70;
                     }
                  } else if (var6.equals("inv")) {
                     var11 = 3;
                     break label70;
                  }
               } else if (var6.equals("pt")) {
                  var11 = 1;
                  break label70;
               }
            } else if (var6.equals("o")) {
               var11 = 2;
               break label70;
            }

            var11 = -1;
         }

         if (var11 != 0) {
            if (var11 != 1) {
               if (var11 != 2) {
                  if (var11 != 3) {
                     var0.skipValue();
                  } else {
                     var5 = var0.nextBoolean();
                  }
               } else {
                  var4 = AnimatableValueParser.parseInteger(var0, var1);
               }
            } else {
               var3 = AnimatableValueParser.parseShapeData(var0, var1);
            }
         } else {
            String var9 = var0.nextString();
            var7 = var9.hashCode();
            if (var7 != 97) {
               if (var7 != 105) {
                  if (var7 != 115) {
                     var11 = var8;
                  } else {
                     var11 = var8;
                     if (var9.equals("s")) {
                        var11 = 1;
                     }
                  }
               } else {
                  var11 = var8;
                  if (var9.equals("i")) {
                     var11 = 2;
                  }
               }
            } else {
               var11 = var8;
               if (var9.equals("a")) {
                  var11 = 0;
               }
            }

            if (var11 != 0) {
               if (var11 != 1) {
                  if (var11 != 2) {
                     StringBuilder var10 = new StringBuilder();
                     var10.append("Unknown mask mode ");
                     var10.append(var6);
                     var10.append(". Defaulting to Add.");
                     Logger.warning(var10.toString());
                     var2 = Mask.MaskMode.MASK_MODE_ADD;
                  } else {
                     var1.addWarning("Animation contains intersect masks. They are not supported but will be treated like add masks.");
                     var2 = Mask.MaskMode.MASK_MODE_INTERSECT;
                  }
               } else {
                  var2 = Mask.MaskMode.MASK_MODE_SUBTRACT;
               }
            } else {
               var2 = Mask.MaskMode.MASK_MODE_ADD;
            }
         }
      }

      var0.endObject();
      return new Mask(var2, var3, (AnimatableIntegerValue)var4, var5);
   }
}
