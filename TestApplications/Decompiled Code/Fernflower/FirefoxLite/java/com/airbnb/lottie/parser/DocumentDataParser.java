package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.model.DocumentData;
import java.io.IOException;

public class DocumentDataParser implements ValueParser {
   public static final DocumentDataParser INSTANCE = new DocumentDataParser();

   private DocumentDataParser() {
   }

   public DocumentData parse(JsonReader var1, float var2) throws IOException {
      var1.beginObject();
      String var3 = null;
      String var4 = var3;
      double var5 = 0.0D;
      double var7 = var5;
      double var9 = var5;
      double var11 = var5;
      int var13 = 0;
      int var14 = 0;
      int var15 = 0;
      int var16 = 0;
      boolean var17 = true;

      while(var1.hasNext()) {
         byte var20;
         label80: {
            String var18 = var1.nextName();
            int var19 = var18.hashCode();
            if (var19 != 102) {
               if (var19 != 106) {
                  if (var19 != 3261) {
                     if (var19 != 3452) {
                        if (var19 != 3463) {
                           if (var19 != 3543) {
                              if (var19 != 3664) {
                                 if (var19 != 3684) {
                                    if (var19 != 3710) {
                                       switch(var19) {
                                       case 115:
                                          if (var18.equals("s")) {
                                             var20 = 2;
                                             break label80;
                                          }
                                          break;
                                       case 116:
                                          if (var18.equals("t")) {
                                             var20 = 0;
                                             break label80;
                                          }
                                       }
                                    } else if (var18.equals("tr")) {
                                       var20 = 4;
                                       break label80;
                                    }
                                 } else if (var18.equals("sw")) {
                                    var20 = 9;
                                    break label80;
                                 }
                              } else if (var18.equals("sc")) {
                                 var20 = 8;
                                 break label80;
                              }
                           } else if (var18.equals("of")) {
                              var20 = 10;
                              break label80;
                           }
                        } else if (var18.equals("ls")) {
                           var20 = 6;
                           break label80;
                        }
                     } else if (var18.equals("lh")) {
                        var20 = 5;
                        break label80;
                     }
                  } else if (var18.equals("fc")) {
                     var20 = 7;
                     break label80;
                  }
               } else if (var18.equals("j")) {
                  var20 = 3;
                  break label80;
               }
            } else if (var18.equals("f")) {
               var20 = 1;
               break label80;
            }

            var20 = -1;
         }

         switch(var20) {
         case 0:
            var3 = var1.nextString();
            break;
         case 1:
            var4 = var1.nextString();
            break;
         case 2:
            var5 = var1.nextDouble();
            break;
         case 3:
            var13 = var1.nextInt();
            break;
         case 4:
            var14 = var1.nextInt();
            break;
         case 5:
            var7 = var1.nextDouble();
            break;
         case 6:
            var9 = var1.nextDouble();
            break;
         case 7:
            var15 = JsonUtils.jsonToColor(var1);
            break;
         case 8:
            var16 = JsonUtils.jsonToColor(var1);
            break;
         case 9:
            var11 = var1.nextDouble();
            break;
         case 10:
            var17 = var1.nextBoolean();
            break;
         default:
            var1.skipValue();
         }
      }

      var1.endObject();
      return new DocumentData(var3, var4, var5, var13, var14, var7, var9, var15, var16, var11, var17);
   }
}
