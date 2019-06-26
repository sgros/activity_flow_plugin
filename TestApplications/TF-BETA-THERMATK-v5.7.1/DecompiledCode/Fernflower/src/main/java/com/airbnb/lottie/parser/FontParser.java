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
         switch(var5.hashCode()) {
         case -1866931350:
            if (var5.equals("fFamily")) {
               var6 = 0;
            }
            break;
         case -1408684838:
            if (var5.equals("ascent")) {
               var6 = 3;
            }
            break;
         case -1294566165:
            if (var5.equals("fStyle")) {
               var6 = 2;
            }
            break;
         case 96619537:
            if (var5.equals("fName")) {
               var6 = 1;
            }
         }

         if (var6 != 0) {
            if (var6 != 1) {
               if (var6 != 2) {
                  if (var6 != 3) {
                     var0.skipValue();
                  } else {
                     var4 = (float)var0.nextDouble();
                  }
               } else {
                  var3 = var0.nextString();
               }
            } else {
               var2 = var0.nextString();
            }
         } else {
            var1 = var0.nextString();
         }
      }

      var0.endObject();
      return new Font(var1, var2, var3, var4);
   }
}
