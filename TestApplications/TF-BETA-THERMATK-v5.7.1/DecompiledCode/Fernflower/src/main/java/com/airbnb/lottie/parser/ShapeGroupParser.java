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
      boolean var4 = false;

      while(true) {
         while(true) {
            while(true) {
               while(var0.hasNext()) {
                  String var5 = var0.nextName();
                  byte var6 = -1;
                  int var7 = var5.hashCode();
                  if (var7 != 3324) {
                     if (var7 != 3371) {
                        if (var7 == 3519 && var5.equals("nm")) {
                           var6 = 0;
                        }
                     } else if (var5.equals("it")) {
                        var6 = 2;
                     }
                  } else if (var5.equals("hd")) {
                     var6 = 1;
                  }

                  if (var6 != 0) {
                     if (var6 != 1) {
                        if (var6 != 2) {
                           var0.skipValue();
                        } else {
                           var0.beginArray();

                           while(var0.hasNext()) {
                              ContentModel var8 = ContentModelParser.parse(var0, var1);
                              if (var8 != null) {
                                 var2.add(var8);
                              }
                           }

                           var0.endArray();
                        }
                     } else {
                        var4 = var0.nextBoolean();
                     }
                  } else {
                     var3 = var0.nextString();
                  }
               }

               return new ShapeGroup(var3, var2, var4);
            }
         }
      }
   }
}
