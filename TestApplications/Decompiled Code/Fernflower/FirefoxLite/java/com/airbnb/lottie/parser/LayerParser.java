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
      Layer.MatteType var2 = Layer.MatteType.None;
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      var0.beginObject();
      String var5 = null;
      AnimatableTransform var7 = var5;
      AnimatableTextFrame var8 = var5;
      AnimatableTextProperties var9 = var5;
      AnimatableFloatValue var10 = var5;
      long var11 = 0L;
      long var13 = -1L;
      float var15 = 0.0F;
      float var16 = 1.0F;
      int var17 = 0;
      int var18 = 0;
      int var19 = 0;
      float var20 = 0.0F;
      int var21 = 0;
      int var22 = 0;
      String var23 = "UNSET";
      String var24 = var5;
      float var25 = 0.0F;
      String var26 = var5;
      Layer.LayerType var6 = var5;
      var5 = var23;

      while(true) {
         label225:
         while(var0.hasNext()) {
            byte var27;
            label168: {
               var23 = var0.nextName();
               switch(var23.hashCode()) {
               case -995424086:
                  if (var23.equals("parent")) {
                     var27 = 4;
                     break label168;
                  }
                  break;
               case -903568142:
                  if (var23.equals("shapes")) {
                     var27 = 11;
                     break label168;
                  }
                  break;
               case 104:
                  if (var23.equals("h")) {
                     var27 = 17;
                     break label168;
                  }
                  break;
               case 116:
                  if (var23.equals("t")) {
                     var27 = 12;
                     break label168;
                  }
                  break;
               case 119:
                  if (var23.equals("w")) {
                     var27 = 16;
                     break label168;
                  }
                  break;
               case 3177:
                  if (var23.equals("cl")) {
                     var27 = 21;
                     break label168;
                  }
                  break;
               case 3233:
                  if (var23.equals("ef")) {
                     var27 = 13;
                     break label168;
                  }
                  break;
               case 3367:
                  if (var23.equals("ip")) {
                     var27 = 18;
                     break label168;
                  }
                  break;
               case 3432:
                  if (var23.equals("ks")) {
                     var27 = 8;
                     break label168;
                  }
                  break;
               case 3519:
                  if (var23.equals("nm")) {
                     var27 = 0;
                     break label168;
                  }
                  break;
               case 3553:
                  if (var23.equals("op")) {
                     var27 = 19;
                     break label168;
                  }
                  break;
               case 3664:
                  if (var23.equals("sc")) {
                     var27 = 7;
                     break label168;
                  }
                  break;
               case 3669:
                  if (var23.equals("sh")) {
                     var27 = 6;
                     break label168;
                  }
                  break;
               case 3679:
                  if (var23.equals("sr")) {
                     var27 = 14;
                     break label168;
                  }
                  break;
               case 3681:
                  if (var23.equals("st")) {
                     var27 = 15;
                     break label168;
                  }
                  break;
               case 3684:
                  if (var23.equals("sw")) {
                     var27 = 5;
                     break label168;
                  }
                  break;
               case 3705:
                  if (var23.equals("tm")) {
                     var27 = 20;
                     break label168;
                  }
                  break;
               case 3712:
                  if (var23.equals("tt")) {
                     var27 = 9;
                     break label168;
                  }
                  break;
               case 3717:
                  if (var23.equals("ty")) {
                     var27 = 3;
                     break label168;
                  }
                  break;
               case 104415:
                  if (var23.equals("ind")) {
                     var27 = 1;
                     break label168;
                  }
                  break;
               case 108390670:
                  if (var23.equals("refId")) {
                     var27 = 2;
                     break label168;
                  }
                  break;
               case 1441620890:
                  if (var23.equals("masksProperties")) {
                     var27 = 10;
                     break label168;
                  }
               }

               var27 = -1;
            }

            int var33;
            switch(var27) {
            case 0:
               var5 = var0.nextString();
               break;
            case 1:
               var11 = (long)var0.nextInt();
               break;
            case 2:
               var26 = var0.nextString();
               break;
            case 3:
               var33 = var0.nextInt();
               if (var33 < Layer.LayerType.Unknown.ordinal()) {
                  var6 = Layer.LayerType.values()[var33];
               } else {
                  var6 = Layer.LayerType.Unknown;
               }
               break;
            case 4:
               var13 = (long)var0.nextInt();
               break;
            case 5:
               var17 = (int)((float)var0.nextInt() * Utils.dpScale());
               break;
            case 6:
               var18 = (int)((float)var0.nextInt() * Utils.dpScale());
               break;
            case 7:
               var19 = Color.parseColor(var0.nextString());
               break;
            case 8:
               var7 = AnimatableTransformParser.parse(var0, var1);
               break;
            case 9:
               var2 = Layer.MatteType.values()[var0.nextInt()];
               break;
            case 10:
               var0.beginArray();

               while(var0.hasNext()) {
                  var3.add(MaskParser.parse(var0, var1));
               }

               var0.endArray();
               break;
            case 11:
               var0.beginArray();

               while(var0.hasNext()) {
                  ContentModel var31 = ContentModelParser.parse(var0, var1);
                  if (var31 != null) {
                     var4.add(var31);
                  }
               }

               var0.endArray();
               break;
            case 12:
               var0.beginObject();

               while(true) {
                  while(var0.hasNext()) {
                     label192: {
                        var23 = var0.nextName();
                        var33 = var23.hashCode();
                        if (var33 != 97) {
                           if (var33 == 100 && var23.equals("d")) {
                              var27 = 0;
                              break label192;
                           }
                        } else if (var23.equals("a")) {
                           var27 = 1;
                           break label192;
                        }

                        var27 = -1;
                     }

                     switch(var27) {
                     case 0:
                        var8 = AnimatableValueParser.parseDocumentData(var0, var1);
                        break;
                     case 1:
                        var0.beginArray();
                        if (var0.hasNext()) {
                           var9 = AnimatableTextPropertiesParser.parse(var0, var1);
                        }

                        while(var0.hasNext()) {
                           var0.skipValue();
                        }

                        var0.endArray();
                        break;
                     default:
                        var0.skipValue();
                     }
                  }

                  var0.endObject();
                  continue label225;
               }
            case 13:
               var0.beginArray();
               ArrayList var30 = new ArrayList();

               while(var0.hasNext()) {
                  var0.beginObject();

                  while(var0.hasNext()) {
                     String var28 = var0.nextName();
                     if (var28.hashCode() == 3519 && var28.equals("nm")) {
                        var27 = 0;
                     } else {
                        var27 = -1;
                     }

                     if (var27 != 0) {
                        var0.skipValue();
                     } else {
                        var30.add(var0.nextString());
                     }
                  }

                  var0.endObject();
               }

               var0.endArray();
               StringBuilder var32 = new StringBuilder();
               var32.append("Lottie doesn't support layer effects. If you are using them for  fills, strokes, trim paths etc. then try adding them directly as contents  in your shape. Found: ");
               var32.append(var30);
               var1.addWarning(var32.toString());
               break;
            case 14:
               var16 = (float)var0.nextDouble();
               break;
            case 15:
               var20 = (float)var0.nextDouble();
               break;
            case 16:
               var21 = (int)((float)var0.nextInt() * Utils.dpScale());
               break;
            case 17:
               var22 = (int)((float)var0.nextInt() * Utils.dpScale());
               break;
            case 18:
               var25 = (float)var0.nextDouble();
               break;
            case 19:
               var15 = (float)var0.nextDouble();
               break;
            case 20:
               var10 = AnimatableValueParser.parseFloat(var0, var1, false);
               break;
            case 21:
               var24 = var0.nextString();
               break;
            default:
               var0.skipValue();
            }
         }

         var0.endObject();
         var25 /= var16;
         var15 /= var16;
         ArrayList var29 = new ArrayList();
         if (var25 > 0.0F) {
            var29.add(new Keyframe(var1, 0.0F, 0.0F, (Interpolator)null, 0.0F, var25));
         }

         if (var15 <= 0.0F) {
            var15 = var1.getEndFrame();
         }

         var29.add(new Keyframe(var1, 1.0F, 1.0F, (Interpolator)null, var25, var15));
         var29.add(new Keyframe(var1, 0.0F, 0.0F, (Interpolator)null, var15, Float.MAX_VALUE));
         if (var5.endsWith(".ai") || "ai".equals(var24)) {
            var1.addWarning("Convert your Illustrator layers to shape layers.");
         }

         return new Layer(var4, var1, var5, var11, var6, var13, var26, var3, var7, var17, var18, var19, var16, var20, var21, var22, var8, var9, var29, var2, var10);
      }
   }

   public static Layer parse(LottieComposition var0) {
      Rect var1 = var0.getBounds();
      return new Layer(Collections.emptyList(), var0, "__container", -1L, Layer.LayerType.PreComp, -1L, (String)null, Collections.emptyList(), new AnimatableTransform(), 0, 0, 0, 0.0F, 0.0F, var1.width(), var1.height(), (AnimatableTextFrame)null, (AnimatableTextProperties)null, Collections.emptyList(), Layer.MatteType.None, (AnimatableFloatValue)null);
   }
}
