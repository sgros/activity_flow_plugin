package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.model.content.ShapeGroup;
import java.io.IOException;
import java.util.ArrayList;

class ShapeGroupParser {
   static ShapeGroup parse(JsonReader var0, LottieComposition var1) throws IOException {
      ArrayList var2 = new ArrayList();
      String var3 = null;

      while(true) {
         while(var0.hasNext()) {
            String var4 = var0.nextName();
            byte var5 = -1;
            int var6 = var4.hashCode();
            if (var6 != 3371) {
               if (var6 == 3519 && var4.equals("nm")) {
                  var5 = 0;
               }
            } else if (var4.equals("it")) {
               var5 = 1;
            }

            switch(var5) {
            case 0:
               var3 = var0.nextString();
               break;
            case 1:
               var0.beginArray();

               while(var0.hasNext()) {
                  ContentModel var7 = ContentModelParser.parse(var0, var1);
                  if (var7 != null) {
                     var2.add(var7);
                  }
               }

               var0.endArray();
               break;
            default:
               var0.skipValue();
            }
         }

         return new ShapeGroup(var3, var2);
      }
   }
}
