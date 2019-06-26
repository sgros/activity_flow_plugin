package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.model.DocumentData;
import java.io.IOException;

public class DocumentDataParser implements ValueParser {
   public static final DocumentDataParser INSTANCE = new DocumentDataParser();

   private DocumentDataParser() {
   }

   public DocumentData parse(JsonReader var1, float var2) throws IOException {
      DocumentData.Justification var3 = DocumentData.Justification.CENTER;
      var1.beginObject();
      String var4 = null;
      String var5 = var4;
      double var6 = 0.0D;
      double var8 = var6;
      double var10 = var6;
      double var12 = var6;
      int var14 = 0;
      int var15 = 0;
      int var16 = 0;
      boolean var17 = true;

      while(true) {
         while(var1.hasNext()) {
            int var19;
            byte var20;
            label84: {
               String var18 = var1.nextName();
               var19 = var18.hashCode();
               if (var19 != 102) {
                  if (var19 != 106) {
                     if (var19 != 3261) {
                        if (var19 != 3452) {
                           if (var19 != 3463) {
                              if (var19 != 3543) {
                                 if (var19 != 3664) {
                                    if (var19 != 3684) {
                                       if (var19 != 3710) {
                                          if (var19 != 115) {
                                             if (var19 == 116 && var18.equals("t")) {
                                                var20 = 0;
                                                break label84;
                                             }
                                          } else if (var18.equals("s")) {
                                             var20 = 2;
                                             break label84;
                                          }
                                       } else if (var18.equals("tr")) {
                                          var20 = 4;
                                          break label84;
                                       }
                                    } else if (var18.equals("sw")) {
                                       var20 = 9;
                                       break label84;
                                    }
                                 } else if (var18.equals("sc")) {
                                    var20 = 8;
                                    break label84;
                                 }
                              } else if (var18.equals("of")) {
                                 var20 = 10;
                                 break label84;
                              }
                           } else if (var18.equals("ls")) {
                              var20 = 6;
                              break label84;
                           }
                        } else if (var18.equals("lh")) {
                           var20 = 5;
                           break label84;
                        }
                     } else if (var18.equals("fc")) {
                        var20 = 7;
                        break label84;
                     }
                  } else if (var18.equals("j")) {
                     var20 = 3;
                     break label84;
                  }
               } else if (var18.equals("f")) {
                  var20 = 1;
                  break label84;
               }

               var20 = -1;
            }

            switch(var20) {
            case 0:
               var4 = var1.nextString();
               break;
            case 1:
               var5 = var1.nextString();
               break;
            case 2:
               var6 = var1.nextDouble();
               break;
            case 3:
               var19 = var1.nextInt();
               if (var19 <= DocumentData.Justification.CENTER.ordinal() && var19 >= 0) {
                  var3 = DocumentData.Justification.values()[var19];
                  break;
               }

               var3 = DocumentData.Justification.CENTER;
               break;
            case 4:
               var14 = var1.nextInt();
               break;
            case 5:
               var8 = var1.nextDouble();
               break;
            case 6:
               var10 = var1.nextDouble();
               break;
            case 7:
               var15 = JsonUtils.jsonToColor(var1);
               break;
            case 8:
               var16 = JsonUtils.jsonToColor(var1);
               break;
            case 9:
               var12 = var1.nextDouble();
               break;
            case 10:
               var17 = var1.nextBoolean();
               break;
            default:
               var1.skipValue();
            }
         }

         var1.endObject();
         return new DocumentData(var4, var5, var6, var3, var14, var8, var10, var15, var16, var12, var17);
      }
   }
}
