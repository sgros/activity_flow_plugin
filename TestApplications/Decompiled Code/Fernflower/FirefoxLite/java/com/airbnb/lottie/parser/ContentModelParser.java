package com.airbnb.lottie.parser;

import android.util.JsonReader;
import android.util.Log;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.model.content.MergePaths;
import java.io.IOException;

class ContentModelParser {
   static ContentModel parse(JsonReader var0, LottieComposition var1) throws IOException {
      var0.beginObject();
      byte var2 = 2;
      int var3 = 2;

      Object var5;
      String var6;
      byte var10;
      label107:
      while(true) {
         boolean var4 = var0.hasNext();
         var5 = null;
         if (!var4) {
            var6 = null;
            break;
         }

         label104: {
            var6 = var0.nextName();
            int var7 = var6.hashCode();
            if (var7 != 100) {
               if (var7 == 3717 && var6.equals("ty")) {
                  var10 = 0;
                  break label104;
               }
            } else if (var6.equals("d")) {
               var10 = 1;
               break label104;
            }

            var10 = -1;
         }

         switch(var10) {
         case 0:
            var6 = var0.nextString();
            break label107;
         case 1:
            var3 = var0.nextInt();
            break;
         default:
            var0.skipValue();
         }
      }

      if (var6 == null) {
         return null;
      } else {
         label93: {
            switch(var6.hashCode()) {
            case 3239:
               if (var6.equals("el")) {
                  var10 = 7;
                  break label93;
               }
               break;
            case 3270:
               if (var6.equals("fl")) {
                  var10 = 3;
                  break label93;
               }
               break;
            case 3295:
               if (var6.equals("gf")) {
                  var10 = 4;
                  break label93;
               }
               break;
            case 3307:
               if (var6.equals("gr")) {
                  var10 = 0;
                  break label93;
               }
               break;
            case 3308:
               if (var6.equals("gs")) {
                  var10 = var2;
                  break label93;
               }
               break;
            case 3488:
               if (var6.equals("mm")) {
                  var10 = 11;
                  break label93;
               }
               break;
            case 3633:
               if (var6.equals("rc")) {
                  var10 = 8;
                  break label93;
               }
               break;
            case 3646:
               if (var6.equals("rp")) {
                  var10 = 12;
                  break label93;
               }
               break;
            case 3669:
               if (var6.equals("sh")) {
                  var10 = 6;
                  break label93;
               }
               break;
            case 3679:
               if (var6.equals("sr")) {
                  var10 = 10;
                  break label93;
               }
               break;
            case 3681:
               if (var6.equals("st")) {
                  var10 = 1;
                  break label93;
               }
               break;
            case 3705:
               if (var6.equals("tm")) {
                  var10 = 9;
                  break label93;
               }
               break;
            case 3710:
               if (var6.equals("tr")) {
                  var10 = 5;
                  break label93;
               }
            }

            var10 = -1;
         }

         Object var8;
         switch(var10) {
         case 0:
            var8 = ShapeGroupParser.parse(var0, var1);
            break;
         case 1:
            var8 = ShapeStrokeParser.parse(var0, var1);
            break;
         case 2:
            var8 = GradientStrokeParser.parse(var0, var1);
            break;
         case 3:
            var8 = ShapeFillParser.parse(var0, var1);
            break;
         case 4:
            var8 = GradientFillParser.parse(var0, var1);
            break;
         case 5:
            var8 = AnimatableTransformParser.parse(var0, var1);
            break;
         case 6:
            var8 = ShapePathParser.parse(var0, var1);
            break;
         case 7:
            var8 = CircleShapeParser.parse(var0, var1, var3);
            break;
         case 8:
            var8 = RectangleShapeParser.parse(var0, var1);
            break;
         case 9:
            var8 = ShapeTrimPathParser.parse(var0, var1);
            break;
         case 10:
            var8 = PolystarShapeParser.parse(var0, var1);
            break;
         case 11:
            MergePaths var11 = MergePathsParser.parse(var0);
            var1.addWarning("Animation contains merge paths. Merge paths are only supported on KitKat+ and must be manually enabled by calling enableMergePathsForKitKatAndAbove().");
            var8 = var11;
            break;
         case 12:
            var8 = RepeaterParser.parse(var0, var1);
            break;
         default:
            StringBuilder var9 = new StringBuilder();
            var9.append("Unknown shape type ");
            var9.append(var6);
            Log.w("LOTTIE", var9.toString());
            var8 = var5;
         }

         while(var0.hasNext()) {
            var0.skipValue();
         }

         var0.endObject();
         return (ContentModel)var8;
      }
   }
}
