package com.airbnb.lottie.parser;

import android.graphics.Rect;
import android.util.JsonReader;
import androidx.collection.LongSparseArray;
import androidx.collection.SparseArrayCompat;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.model.Font;
import com.airbnb.lottie.model.FontCharacter;
import com.airbnb.lottie.model.Marker;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.utils.Logger;
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
      ArrayList var7 = new ArrayList();
      SparseArrayCompat var8 = new SparseArrayCompat();
      LottieComposition var9 = new LottieComposition();
      var0.beginObject();
      int var10 = 0;
      int var11 = 0;
      float var12 = 0.0F;
      float var13 = 0.0F;
      float var14 = 0.0F;

      while(var0.hasNext()) {
         byte var16;
         label66: {
            String var15 = var0.nextName();
            switch(var15.hashCode()) {
            case -1408207997:
               if (var15.equals("assets")) {
                  var16 = 7;
                  break label66;
               }
               break;
            case -1109732030:
               if (var15.equals("layers")) {
                  var16 = 6;
                  break label66;
               }
               break;
            case 104:
               if (var15.equals("h")) {
                  var16 = 1;
                  break label66;
               }
               break;
            case 118:
               if (var15.equals("v")) {
                  var16 = 5;
                  break label66;
               }
               break;
            case 119:
               if (var15.equals("w")) {
                  var16 = 0;
                  break label66;
               }
               break;
            case 3276:
               if (var15.equals("fr")) {
                  var16 = 4;
                  break label66;
               }
               break;
            case 3367:
               if (var15.equals("ip")) {
                  var16 = 2;
                  break label66;
               }
               break;
            case 3553:
               if (var15.equals("op")) {
                  var16 = 3;
                  break label66;
               }
               break;
            case 94623709:
               if (var15.equals("chars")) {
                  var16 = 9;
                  break label66;
               }
               break;
            case 97615364:
               if (var15.equals("fonts")) {
                  var16 = 8;
                  break label66;
               }
               break;
            case 839250809:
               if (var15.equals("markers")) {
                  var16 = 10;
                  break label66;
               }
            }

            var16 = -1;
         }

         switch(var16) {
         case 0:
            var10 = var0.nextInt();
            break;
         case 1:
            var11 = var0.nextInt();
            break;
         case 2:
            var12 = (float)var0.nextDouble();
            break;
         case 3:
            var13 = (float)var0.nextDouble() - 0.01F;
            break;
         case 4:
            var14 = (float)var0.nextDouble();
            break;
         case 5:
            String[] var17 = var0.nextString().split("\\.");
            if (!Utils.isAtLeastVersion(Integer.parseInt(var17[0]), Integer.parseInt(var17[1]), Integer.parseInt(var17[2]), 4, 4, 0)) {
               var9.addWarning("Lottie only supports bodymovin >= 4.4.0");
            }
            break;
         case 6:
            parseLayers(var0, var9, var3, var2);
            break;
         case 7:
            parseAssets(var0, var9, var4, var5);
            break;
         case 8:
            parseFonts(var0, var6);
            break;
         case 9:
            parseChars(var0, var9, var8);
            break;
         case 10:
            parseMarkers(var0, var9, var7);
            break;
         default:
            var0.skipValue();
         }
      }

      var0.endObject();
      var9.init(new Rect(0, 0, (int)((float)var10 * var1), (int)((float)var11 * var1)), var12, var13, var14, var3, var2, var4, var5, var8, var6, var7);
      return var9;
   }

   private static void parseAssets(JsonReader var0, LottieComposition var1, Map var2, Map var3) throws IOException {
      var0.beginArray();

      label89:
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
            while(true) {
               while(var0.hasNext()) {
                  String var11 = var0.nextName();
                  byte var12 = -1;
                  int var13 = var11.hashCode();
                  if (var13 != -1109732030) {
                     if (var13 != 104) {
                        if (var13 != 112) {
                           if (var13 != 117) {
                              if (var13 != 119) {
                                 if (var13 == 3355 && var11.equals("id")) {
                                    var12 = 0;
                                 }
                              } else if (var11.equals("w")) {
                                 var12 = 2;
                              }
                           } else if (var11.equals("u")) {
                              var12 = 5;
                           }
                        } else if (var11.equals("p")) {
                           var12 = 4;
                        }
                     } else if (var11.equals("h")) {
                        var12 = 3;
                     }
                  } else if (var11.equals("layers")) {
                     var12 = 1;
                  }

                  if (var12 != 0) {
                     if (var12 != 1) {
                        if (var12 != 2) {
                           if (var12 != 3) {
                              if (var12 != 4) {
                                 if (var12 != 5) {
                                    var0.skipValue();
                                 } else {
                                    var8 = var0.nextString();
                                 }
                              } else {
                                 var7 = var0.nextString();
                              }
                           } else {
                              var10 = var0.nextInt();
                           }
                        } else {
                           var9 = var0.nextInt();
                        }
                     } else {
                        var0.beginArray();

                        while(var0.hasNext()) {
                           Layer var15 = LayerParser.parse(var0, var1);
                           var5.put(var15.getId(), var15);
                           var4.add(var15);
                        }

                        var0.endArray();
                     }
                  } else {
                     var6 = var0.nextString();
                  }
               }

               var0.endObject();
               if (var7 != null) {
                  LottieImageAsset var14 = new LottieImageAsset(var9, var10, var6, var7, var8);
                  var3.put(var14.getId(), var14);
               } else {
                  var2.put(var6, var4);
               }
               continue label89;
            }
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
         if (var5.getLayerType() == Layer.LayerType.IMAGE) {
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
            Logger.warning(var7.toString());
            var4 = var6;
         }
      }

      var0.endArray();
   }

   private static void parseMarkers(JsonReader var0, LottieComposition var1, List var2) throws IOException {
      var0.beginArray();

      while(var0.hasNext()) {
         String var8 = null;
         var0.beginObject();
         float var3 = 0.0F;
         float var4 = 0.0F;

         while(var0.hasNext()) {
            String var5 = var0.nextName();
            byte var6 = -1;
            int var7 = var5.hashCode();
            if (var7 != 3178) {
               if (var7 != 3214) {
                  if (var7 == 3705 && var5.equals("tm")) {
                     var6 = 1;
                  }
               } else if (var5.equals("dr")) {
                  var6 = 2;
               }
            } else if (var5.equals("cm")) {
               var6 = 0;
            }

            if (var6 != 0) {
               if (var6 != 1) {
                  if (var6 != 2) {
                     var0.skipValue();
                  } else {
                     var4 = (float)var0.nextDouble();
                  }
               } else {
                  var3 = (float)var0.nextDouble();
               }
            } else {
               var8 = var0.nextString();
            }
         }

         var0.endObject();
         var2.add(new Marker(var8, var3, var4));
      }

      var0.endArray();
   }
}
