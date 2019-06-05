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

      int var8;
      while(var1.hasNext()) {
         byte var20;
         label73: {
            String var7 = var1.nextName();
            var8 = var7.hashCode();
            if (var8 != 99) {
               if (var8 != 105) {
                  if (var8 != 111) {
                     if (var8 == 118 && var7.equals("v")) {
                        var20 = 1;
                        break label73;
                     }
                  } else if (var7.equals("o")) {
                     var20 = 3;
                     break label73;
                  }
               } else if (var7.equals("i")) {
                  var20 = 2;
                  break label73;
               }
            } else if (var7.equals("c")) {
               var20 = 0;
               break label73;
            }

            var20 = -1;
         }

         switch(var20) {
         case 0:
            var6 = var1.nextBoolean();
            break;
         case 1:
            var3 = JsonUtils.jsonToPoints(var1, var2);
            break;
         case 2:
            var4 = JsonUtils.jsonToPoints(var1, var2);
            break;
         case 3:
            var5 = JsonUtils.jsonToPoints(var1, var2);
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
            int var9 = var3.size();
            PointF var15 = (PointF)var3.get(0);
            ArrayList var19 = new ArrayList(var9);

            PointF var13;
            for(var8 = 1; var8 < var9; ++var8) {
               PointF var10 = (PointF)var3.get(var8);
               int var11 = var8 - 1;
               PointF var12 = (PointF)var3.get(var11);
               var13 = (PointF)var5.get(var11);
               PointF var14 = (PointF)var4.get(var8);
               var19.add(new CubicCurveData(MiscUtils.addPoints(var12, var13), MiscUtils.addPoints(var10, var14), var10));
            }

            if (var6) {
               var13 = (PointF)var3.get(0);
               var8 = var9 - 1;
               PointF var16 = (PointF)var3.get(var8);
               PointF var18 = (PointF)var5.get(var8);
               PointF var17 = (PointF)var4.get(0);
               var19.add(new CubicCurveData(MiscUtils.addPoints(var16, var18), MiscUtils.addPoints(var13, var17), var13));
            }

            return new ShapeData(var15, var6, var19);
         }
      } else {
         throw new IllegalArgumentException("Shape data was missing information.");
      }
   }
}
