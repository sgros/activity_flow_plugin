package com.airbnb.lottie.parser;

import android.graphics.Color;
import android.graphics.Rect;
import android.util.JsonReader;
import android.view.animation.Interpolator;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTextFrame;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LayerParser {
   public static Layer parse(JsonReader var0, LottieComposition var1) throws IOException {
      Layer.MatteType var2 = Layer.MatteType.NONE;
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      var0.beginObject();
      Float var5 = 1.0F;
      Float var6 = 0.0F;
      String var7 = null;
      AnimatableTextFrame var10 = var7;
      AnimatableTextProperties var11 = var7;
      AnimatableFloatValue var12 = var7;
      long var13 = 0L;
      long var15 = -1L;
      float var17 = 0.0F;
      float var18 = 0.0F;
      float var19 = 1.0F;
      int var20 = 0;
      int var21 = 0;
      int var22 = 0;
      int var23 = 0;
      int var24 = 0;
      float var25 = 0.0F;
      boolean var26 = false;
      String var28 = "UNSET";
      AnimatableTransform var29 = var7;
      String var30 = var7;
      Layer.LayerType var9 = var7;
      String var8 = var7;
      String var27 = var28;

      while(true) {
         label235:
         while(var0.hasNext()) {
            byte var31;
            label174: {
               var7 = var0.nextName();
               switch(var7.hashCode()) {
               case -995424086:
                  if (var7.equals("parent")) {
                     var31 = 4;
                     break label174;
                  }
                  break;
               case -903568142:
                  if (var7.equals("shapes")) {
                     var31 = 11;
                     break label174;
                  }
                  break;
               case 104:
                  if (var7.equals("h")) {
                     var31 = 17;
                     break label174;
                  }
                  break;
               case 116:
                  if (var7.equals("t")) {
                     var31 = 12;
                     break label174;
                  }
                  break;
               case 119:
                  if (var7.equals("w")) {
                     var31 = 16;
                     break label174;
                  }
                  break;
               case 3177:
                  if (var7.equals("cl")) {
                     var31 = 21;
                     break label174;
                  }
                  break;
               case 3233:
                  if (var7.equals("ef")) {
                     var31 = 13;
                     break label174;
                  }
                  break;
               case 3324:
                  if (var7.equals("hd")) {
                     var31 = 22;
                     break label174;
                  }
                  break;
               case 3367:
                  if (var7.equals("ip")) {
                     var31 = 18;
                     break label174;
                  }
                  break;
               case 3432:
                  if (var7.equals("ks")) {
                     var31 = 8;
                     break label174;
                  }
                  break;
               case 3519:
                  if (var7.equals("nm")) {
                     var31 = 0;
                     break label174;
                  }
                  break;
               case 3553:
                  if (var7.equals("op")) {
                     var31 = 19;
                     break label174;
                  }
                  break;
               case 3664:
                  if (var7.equals("sc")) {
                     var31 = 7;
                     break label174;
                  }
                  break;
               case 3669:
                  if (var7.equals("sh")) {
                     var31 = 6;
                     break label174;
                  }
                  break;
               case 3679:
                  if (var7.equals("sr")) {
                     var31 = 14;
                     break label174;
                  }
                  break;
               case 3681:
                  if (var7.equals("st")) {
                     var31 = 15;
                     break label174;
                  }
                  break;
               case 3684:
                  if (var7.equals("sw")) {
                     var31 = 5;
                     break label174;
                  }
                  break;
               case 3705:
                  if (var7.equals("tm")) {
                     var31 = 20;
                     break label174;
                  }
                  break;
               case 3712:
                  if (var7.equals("tt")) {
                     var31 = 9;
                     break label174;
                  }
                  break;
               case 3717:
                  if (var7.equals("ty")) {
                     var31 = 3;
                     break label174;
                  }
                  break;
               case 104415:
                  if (var7.equals("ind")) {
                     var31 = 1;
                     break label174;
                  }
                  break;
               case 108390670:
                  if (var7.equals("refId")) {
                     var31 = 2;
                     break label174;
                  }
                  break;
               case 1441620890:
                  if (var7.equals("masksProperties")) {
                     var31 = 10;
                     break label174;
                  }
               }

               var31 = -1;
            }

            int var36;
            switch(var31) {
            case 0:
               var27 = var0.nextString();
               break;
            case 1:
               var13 = (long)var0.nextInt();
               break;
            case 2:
               var30 = var0.nextString();
               break;
            case 3:
               var36 = var0.nextInt();
               if (var36 < Layer.LayerType.UNKNOWN.ordinal()) {
                  var9 = Layer.LayerType.values()[var36];
               } else {
                  var9 = Layer.LayerType.UNKNOWN;
               }
               break;
            case 4:
               var15 = (long)var0.nextInt();
               break;
            case 5:
               var20 = (int)((float)var0.nextInt() * Utils.dpScale());
               break;
            case 6:
               var21 = (int)((float)var0.nextInt() * Utils.dpScale());
               break;
            case 7:
               var22 = Color.parseColor(var0.nextString());
               break;
            case 8:
               var29 = AnimatableTransformParser.parse(var0, var1);
               break;
            case 9:
               var2 = Layer.MatteType.values()[var0.nextInt()];
               var1.incrementMatteOrMaskCount(1);
               break;
            case 10:
               var0.beginArray();

               while(var0.hasNext()) {
                  var3.add(MaskParser.parse(var0, var1));
               }

               var1.incrementMatteOrMaskCount(var3.size());
               var0.endArray();
               break;
            case 11:
               var0.beginArray();

               while(var0.hasNext()) {
                  ContentModel var34 = ContentModelParser.parse(var0, var1);
                  if (var34 != null) {
                     var4.add(var34);
                  }
               }

               var0.endArray();
               break;
            case 12:
               var0.beginObject();

               while(true) {
                  while(true) {
                     while(var0.hasNext()) {
                        label198: {
                           var7 = var0.nextName();
                           var36 = var7.hashCode();
                           if (var36 != 97) {
                              if (var36 == 100 && var7.equals("d")) {
                                 var31 = 0;
                                 break label198;
                              }
                           } else if (var7.equals("a")) {
                              var31 = 1;
                              break label198;
                           }

                           var31 = -1;
                        }

                        if (var31 != 0) {
                           if (var31 != 1) {
                              var0.skipValue();
                           } else {
                              var0.beginArray();
                              if (var0.hasNext()) {
                                 var11 = AnimatableTextPropertiesParser.parse(var0, var1);
                              }

                              while(var0.hasNext()) {
                                 var0.skipValue();
                              }

                              var0.endArray();
                           }
                        } else {
                           var10 = AnimatableValueParser.parseDocumentData(var0, var1);
                        }
                     }

                     var0.endObject();
                     continue label235;
                  }
               }
            case 13:
               var0.beginArray();
               ArrayList var33 = new ArrayList();

               while(var0.hasNext()) {
                  var0.beginObject();

                  while(var0.hasNext()) {
                     var28 = var0.nextName();
                     if (var28.hashCode() == 3519 && var28.equals("nm")) {
                        var31 = 0;
                     } else {
                        var31 = -1;
                     }

                     if (var31 != 0) {
                        var0.skipValue();
                     } else {
                        var33.add(var0.nextString());
                     }
                  }

                  var0.endObject();
               }

               var0.endArray();
               StringBuilder var35 = new StringBuilder();
               var35.append("Lottie doesn't support layer effects. If you are using them for  fills, strokes, trim paths etc. then try adding them directly as contents  in your shape. Found: ");
               var35.append(var33);
               var1.addWarning(var35.toString());
               break;
            case 14:
               var19 = (float)var0.nextDouble();
               break;
            case 15:
               var25 = (float)var0.nextDouble();
               break;
            case 16:
               var23 = (int)((float)var0.nextInt() * Utils.dpScale());
               break;
            case 17:
               var24 = (int)((float)var0.nextInt() * Utils.dpScale());
               break;
            case 18:
               var17 = (float)var0.nextDouble();
               break;
            case 19:
               var18 = (float)var0.nextDouble();
               break;
            case 20:
               var12 = AnimatableValueParser.parseFloat(var0, var1, false);
               break;
            case 21:
               var8 = var0.nextString();
               break;
            case 22:
               var26 = var0.nextBoolean();
               break;
            default:
               var0.skipValue();
            }
         }

         var0.endObject();
         var17 /= var19;
         var18 /= var19;
         ArrayList var32 = new ArrayList();
         if (var17 > 0.0F) {
            var32.add(new Keyframe(var1, var6, var6, (Interpolator)null, 0.0F, var17));
         }

         if (var18 <= 0.0F) {
            var18 = var1.getEndFrame();
         }

         var32.add(new Keyframe(var1, var5, var5, (Interpolator)null, var17, var18));
         var32.add(new Keyframe(var1, var6, var6, (Interpolator)null, var18, Float.MAX_VALUE));
         if (var27.endsWith(".ai") || "ai".equals(var8)) {
            var1.addWarning("Convert your Illustrator layers to shape layers.");
         }

         return new Layer(var4, var1, var27, var13, var9, var15, var30, var3, var29, var20, var21, var22, var19, var25, var23, var24, var10, var11, var32, var2, var12, var26);
      }
   }

   public static Layer parse(LottieComposition var0) {
      Rect var1 = var0.getBounds();
      return new Layer(Collections.emptyList(), var0, "__container", -1L, Layer.LayerType.PRE_COMP, -1L, (String)null, Collections.emptyList(), new AnimatableTransform(), 0, 0, 0, 0.0F, 0.0F, var1.width(), var1.height(), (AnimatableTextFrame)null, (AnimatableTextProperties)null, Collections.emptyList(), Layer.MatteType.NONE, (AnimatableFloatValue)null, false);
   }
}
