package com.airbnb.lottie.parser;

import android.graphics.Rect;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SparseArrayCompat;
import android.util.JsonReader;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.model.Font;
import com.airbnb.lottie.model.FontCharacter;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LottieCompositionParser {
   public static LottieComposition parse(JsonReader var0) throws IOException {
      float var1 = Utils.dpScale();
      LongSparseArray var2 = new LongSparseArray();
      ArrayList var3 = new ArrayList();
      HashMap var4 = new HashMap();
      HashMap var5 = new HashMap();
      HashMap var6 = new HashMap();
      SparseArrayCompat var7 = new SparseArrayCompat();
      LottieComposition var8 = new LottieComposition();
      var0.beginObject();
      int var9 = 0;
      int var10 = 0;
      float var11 = 0.0F;
      float var12 = 0.0F;
      float var13 = 0.0F;

      while(true) {
         float var16;
         label66:
         while(true) {
            if (!var0.hasNext()) {
               var0.endObject();
               var8.init(new Rect(0, 0, (int)((float)var9 * var1), (int)((float)var10 * var1)), var11, var12, var13, var3, var2, var4, var5, var7, var6);
               return var8;
            }

            byte var15;
            label61: {
               String var14 = var0.nextName();
               switch(var14.hashCode()) {
               case -1408207997:
                  if (var14.equals("assets")) {
                     var15 = 7;
                     break label61;
                  }
                  break;
               case -1109732030:
                  if (var14.equals("layers")) {
                     var15 = 6;
                     break label61;
                  }
                  break;
               case 104:
                  if (var14.equals("h")) {
                     var15 = 1;
                     break label61;
                  }
                  break;
               case 118:
                  if (var14.equals("v")) {
                     var15 = 5;
                     break label61;
                  }
                  break;
               case 119:
                  if (var14.equals("w")) {
                     var15 = 0;
                     break label61;
                  }
                  break;
               case 3276:
                  if (var14.equals("fr")) {
                     var15 = 4;
                     break label61;
                  }
                  break;
               case 3367:
                  if (var14.equals("ip")) {
                     var15 = 2;
                     break label61;
                  }
                  break;
               case 3553:
                  if (var14.equals("op")) {
                     var15 = 3;
                     break label61;
                  }
                  break;
               case 94623709:
                  if (var14.equals("chars")) {
                     var15 = 9;
                     break label61;
                  }
                  break;
               case 97615364:
                  if (var14.equals("fonts")) {
                     var15 = 8;
                     break label61;
                  }
               }

               var15 = -1;
            }

            switch(var15) {
            case 0:
               var9 = var0.nextInt();
               break;
            case 1:
               var10 = var0.nextInt();
               break;
            case 2:
               var11 = (float)var0.nextDouble();
               break;
            case 3:
               var12 = (float)var0.nextDouble() - 0.01F;
               break;
            case 4:
               var16 = (float)var0.nextDouble();
               break label66;
            case 5:
               String[] var17 = var0.nextString().split("\\.");
               var16 = var13;
               if (!Utils.isAtLeastVersion(Integer.parseInt(var17[0]), Integer.parseInt(var17[1]), Integer.parseInt(var17[2]), 4, 4, 0)) {
                  var8.addWarning("Lottie only supports bodymovin >= 4.4.0");
                  var16 = var13;
               }
               break label66;
            case 6:
               parseLayers(var0, var8, var3, var2);
               var16 = var13;
               break label66;
            case 7:
               parseAssets(var0, var8, var4, var5);
               var16 = var13;
               break label66;
            case 8:
               parseFonts(var0, var6);
               var16 = var13;
               break label66;
            case 9:
               parseChars(var0, var8, var7);
               var16 = var13;
               break label66;
            default:
               var0.skipValue();
            }
         }

         var13 = var16;
      }
   }

   private static void parseAssets(JsonReader var0, LottieComposition var1, Map var2, Map var3) throws IOException {
      var0.beginArray();

      while(var0.hasNext()) {
         ArrayList var4 = new ArrayList();
         LongSparseArray var5 = new LongSparseArray();
         var0.beginObject();
         String var6 = null;
         String var7 = var6;
         String var8 = var6;
         int var9 = 0;
         int var10 = 0;

         while(true) {
            while(var0.hasNext()) {
               byte var15;
               label61: {
                  String var11 = var0.nextName();
                  int var12 = var11.hashCode();
                  if (var12 != -1109732030) {
                     if (var12 != 104) {
                        if (var12 != 112) {
                           if (var12 != 117) {
                              if (var12 != 119) {
                                 if (var12 == 3355 && var11.equals("id")) {
                                    var15 = 0;
                                    break label61;
                                 }
                              } else if (var11.equals("w")) {
                                 var15 = 2;
                                 break label61;
                              }
                           } else if (var11.equals("u")) {
                              var15 = 5;
                              break label61;
                           }
                        } else if (var11.equals("p")) {
                           var15 = 4;
                           break label61;
                        }
                     } else if (var11.equals("h")) {
                        var15 = 3;
                        break label61;
                     }
                  } else if (var11.equals("layers")) {
                     var15 = 1;
                     break label61;
                  }

                  var15 = -1;
               }

               switch(var15) {
               case 0:
                  var6 = var0.nextString();
                  break;
               case 1:
                  var0.beginArray();

                  while(var0.hasNext()) {
                     Layer var14 = LayerParser.parse(var0, var1);
                     var5.put(var14.getId(), var14);
                     var4.add(var14);
                  }

                  var0.endArray();
                  break;
               case 2:
                  var9 = var0.nextInt();
                  break;
               case 3:
                  var10 = var0.nextInt();
                  break;
               case 4:
                  var7 = var0.nextString();
                  break;
               case 5:
                  var8 = var0.nextString();
                  break;
               default:
                  var0.skipValue();
               }
            }

            var0.endObject();
            if (var7 != null) {
               LottieImageAsset var13 = new LottieImageAsset(var9, var10, var6, var7, var8);
               var3.put(var13.getId(), var13);
            } else {
               var2.put(var6, var4);
            }
            break;
         }
      }

      var0.endArray();
   }

   private static void parseChars(JsonReader var0, LottieComposition var1, SparseArrayCompat var2) throws IOException {
      var0.beginArray();

      while(var0.hasNext()) {
         FontCharacter var3 = FontCharacterParser.parse(var0, var1);
         var2.put(var3.hashCode(), var3);
      }

      var0.endArray();
   }

   private static void parseFonts(JsonReader var0, Map var1) throws IOException {
      var0.beginObject();

      while(true) {
         while(var0.hasNext()) {
            String var2 = var0.nextName();
            byte var3 = -1;
            if (var2.hashCode() == 3322014 && var2.equals("list")) {
               var3 = 0;
            }

            if (var3 != 0) {
               var0.skipValue();
            } else {
               var0.beginArray();

               while(var0.hasNext()) {
                  Font var4 = FontParser.parse(var0);
                  var1.put(var4.getName(), var4);
               }

               var0.endArray();
            }
         }

         var0.endObject();
         return;
      }
   }

   private static void parseLayers(JsonReader var0, LottieComposition var1, List var2, LongSparseArray var3) throws IOException {
      var0.beginArray();
      int var4 = 0;

      while(var0.hasNext()) {
         Layer var5 = LayerParser.parse(var0, var1);
         int var6 = var4;
         if (var5.getLayerType() == Layer.LayerType.Image) {
            var6 = var4 + 1;
         }

         var2.add(var5);
         var3.put(var5.getId(), var5);
         var4 = var6;
         if (var6 > 4) {
            StringBuilder var7 = new StringBuilder();
            var7.append("You have ");
            var7.append(var6);
            var7.append(" images. Lottie should primarily be used with shapes. If you are using Adobe Illustrator, convert the Illustrator layers to shape layers.");
            L.warn(var7.toString());
            var4 = var6;
         }
      }

      var0.endArray();
   }
}
