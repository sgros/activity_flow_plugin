package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.model.Font;
import java.io.IOException;

class FontParser {
   static Font parse(JsonReader var0) throws IOException {
      var0.beginObject();
      String var1 = null;
      String var2 = null;
      String var3 = var2;
      float var4 = 0.0F;

      while(var0.hasNext()) {
         String var5 = var0.nextName();
         byte var6 = -1;
         int var7 = var5.hashCode();
         if (var7 != -1866931350) {
            if (var7 != -1408684838) {
               if (var7 != -1294566165) {
                  if (var7 == 96619537 && var5.equals("fName")) {
                     var6 = 1;
                  }
               } else if (var5.equals("fStyle")) {
                  var6 = 2;
               }
            } else if (var5.equals("ascent")) {
               var6 = 3;
            }
         } else if (var5.equals("fFamily")) {
            var6 = 0;
         }

         switch(var6) {
         case 0:
            var1 = var0.nextString();
            break;
         case 1:
            var2 = var0.nextString();
            break;
         case 2:
            var3 = var0.nextString();
            break;
         case 3:
            var4 = (float)var0.nextDouble();
            break;
         default:
            var0.skipValue();
         }
      }

      var0.endObject();
      return new Font(var1, var2, var3, var4);
   }
}
