package com.airbnb.lottie.parser;

import android.graphics.PointF;
import android.util.JsonReader;
import android.util.JsonToken;
import com.airbnb.lottie.model.CubicCurveData;
import com.airbnb.lottie.model.content.ShapeData;
import com.airbnb.lottie.utils.MiscUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShapeDataParser implements ValueParser {
   public static final ShapeDataParser INSTANCE = new ShapeDataParser();

   private ShapeDataParser() {
   }

   public ShapeData parse(JsonReader var1, float var2) throws IOException {
      if (var1.peek() == JsonToken.BEGIN_ARRAY) {
         var1.beginArray();
      }

      var1.beginObject();
      List var3 = null;
      List var4 = null;
      List var5 = var4;
      boolean var6 = false;

      int var9;
      while(var1.hasNext()) {
         String var7 = var1.nextName();
         byte var8 = -1;
         var9 = var7.hashCode();
         if (var9 != 99) {
            if (var9 != 105) {
               if (var9 != 111) {
                  if (var9 == 118 && var7.equals("v")) {
                     var8 = 1;
                  }
               } else if (var7.equals("o")) {
                  var8 = 3;
               }
            } else if (var7.equals("i")) {
               var8 = 2;
            }
         } else if (var7.equals("c")) {
            var8 = 0;
         }

         if (var8 != 0) {
            if (var8 != 1) {
               if (var8 != 2) {
                  if (var8 == 3) {
                     var5 = JsonUtils.jsonToPoints(var1, var2);
                  }
               } else {
                  var4 = JsonUtils.jsonToPoints(var1, var2);
               }
            } else {
               var3 = JsonUtils.jsonToPoints(var1, var2);
            }
         } else {
            var6 = var1.nextBoolean();
         }
      }

      var1.endObject();
      if (var1.peek() == JsonToken.END_ARRAY) {
         var1.endArray();
      }

      if (var3 != null && var4 != null && var5 != null) {
         if (var3.isEmpty()) {
            return new ShapeData(new PointF(), false, Collections.emptyList());
         } else {
            var9 = var3.size();
            PointF var15 = (PointF)var3.get(0);
            ArrayList var19 = new ArrayList(var9);

            PointF var12;
            int var20;
            for(var20 = 1; var20 < var9; ++var20) {
               PointF var10 = (PointF)var3.get(var20);
               int var11 = var20 - 1;
               var12 = (PointF)var3.get(var11);
               PointF var13 = (PointF)var5.get(var11);
               PointF var14 = (PointF)var4.get(var20);
               var19.add(new CubicCurveData(MiscUtils.addPoints(var12, var13), MiscUtils.addPoints(var10, var14), var10));
            }

            if (var6) {
               var12 = (PointF)var3.get(0);
               var20 = var9 - 1;
               PointF var16 = (PointF)var3.get(var20);
               PointF var18 = (PointF)var5.get(var20);
               PointF var17 = (PointF)var4.get(0);
               var19.add(new CubicCurveData(MiscUtils.addPoints(var16, var18), MiscUtils.addPoints(var12, var17), var12));
            }

            return new ShapeData(var15, var6, var19);
         }
      } else {
         throw new IllegalArgumentException("Shape data was missing information.");
      }
   }
}
