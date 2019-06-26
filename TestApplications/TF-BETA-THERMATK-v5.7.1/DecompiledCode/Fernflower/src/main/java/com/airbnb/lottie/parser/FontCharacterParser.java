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
         while(true) {
            while(true) {
               while(true) {
                  while(true) {
                     while(true) {
                        while(var0.hasNext()) {
                           byte var12;
                           label54: {
                              String var11 = var0.nextName();
                              switch(var11.hashCode()) {
                              case -1866931350:
                                 if (var11.equals("fFamily")) {
                                    var12 = 4;
                                    break label54;
                                 }
                                 break;
                              case 119:
                                 if (var11.equals("w")) {
                                    var12 = 2;
                                    break label54;
                                 }
                                 break;
                              case 3173:
                                 if (var11.equals("ch")) {
                                    var12 = 0;
                                    break label54;
                                 }
                                 break;
                              case 3076010:
                                 if (var11.equals("data")) {
                                    var12 = 5;
                                    break label54;
                                 }
                                 break;
                              case 3530753:
                                 if (var11.equals("size")) {
                                    var12 = 1;
                                    break label54;
                                 }
                                 break;
                              case 109780401:
                                 if (var11.equals("style")) {
                                    var12 = 3;
                                    break label54;
                                 }
                              }

                              var12 = -1;
                           }

                           if (var12 != 0) {
                              if (var12 != 1) {
                                 if (var12 != 2) {
                                    if (var12 != 3) {
                                       if (var12 != 4) {
                                          if (var12 != 5) {
                                             var0.skipValue();
                                          } else {
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
                                                break;
                                             }
                                          }
                                       } else {
                                          var4 = var0.nextString();
                                       }
                                    } else {
                                       var3 = var0.nextString();
                                    }
                                 } else {
                                    var7 = var0.nextDouble();
                                 }
                              } else {
                                 var5 = var0.nextDouble();
                              }
                           } else {
                              char var13 = var0.nextString().charAt(0);
                              var10 = var13;
                           }
                        }

                        var0.endObject();
                        return new FontCharacter(var2, var10, var5, var7, var3, var4);
                     }
                  }
               }
            }
         }
      }
   }
}
