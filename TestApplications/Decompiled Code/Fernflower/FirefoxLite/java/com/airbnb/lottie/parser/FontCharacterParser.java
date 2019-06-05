package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.FontCharacter;
import com.airbnb.lottie.model.content.ShapeGroup;
import java.io.IOException;
import java.util.ArrayList;

class FontCharacterParser {
   static FontCharacter parse(JsonReader var0, LottieComposition var1) throws IOException {
      ArrayList var2 = new ArrayList();
      var0.beginObject();
      String var3 = null;
      String var4 = var3;
      double var5 = 0.0D;
      double var7 = var5;
      byte var9 = 0;
      char var10 = (char)var9;

      while(true) {
         label64:
         while(var0.hasNext()) {
            byte var13;
            label57: {
               String var11 = var0.nextName();
               int var12 = var11.hashCode();
               if (var12 != -1866931350) {
                  if (var12 != 119) {
                     if (var12 != 3173) {
                        if (var12 != 3076010) {
                           if (var12 != 3530753) {
                              if (var12 == 109780401 && var11.equals("style")) {
                                 var13 = 3;
                                 break label57;
                              }
                           } else if (var11.equals("size")) {
                              var13 = 1;
                              break label57;
                           }
                        } else if (var11.equals("data")) {
                           var13 = 5;
                           break label57;
                        }
                     } else if (var11.equals("ch")) {
                        var13 = 0;
                        break label57;
                     }
                  } else if (var11.equals("w")) {
                     var13 = 2;
                     break label57;
                  }
               } else if (var11.equals("fFamily")) {
                  var13 = 4;
                  break label57;
               }

               var13 = -1;
            }

            switch(var13) {
            case 0:
               char var14 = var0.nextString().charAt(0);
               var10 = var14;
               break;
            case 1:
               var5 = var0.nextDouble();
               break;
            case 2:
               var7 = var0.nextDouble();
               break;
            case 3:
               var3 = var0.nextString();
               break;
            case 4:
               var4 = var0.nextString();
               break;
            case 5:
               var0.beginObject();

               while(true) {
                  while(var0.hasNext()) {
                     if ("shapes".equals(var0.nextName())) {
                        var0.beginArray();

                        while(var0.hasNext()) {
                           var2.add((ShapeGroup)ContentModelParser.parse(var0, var1));
                        }

                        var0.endArray();
                     } else {
                        var0.skipValue();
                     }
                  }

                  var0.endObject();
                  continue label64;
               }
            default:
               var0.skipValue();
            }
         }

         var0.endObject();
         return new FontCharacter(var2, var10, var5, var7, var3, var4);
      }
   }
}
