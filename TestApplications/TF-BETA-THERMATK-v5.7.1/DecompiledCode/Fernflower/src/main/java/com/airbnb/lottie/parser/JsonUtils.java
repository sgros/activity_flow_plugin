package com.airbnb.lottie.parser;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.JsonReader;
import android.util.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class JsonUtils {
   private static PointF jsonArrayToPoint(JsonReader var0, float var1) throws IOException {
      var0.beginArray();
      float var2 = (float)var0.nextDouble();
      float var3 = (float)var0.nextDouble();

      while(var0.peek() != JsonToken.END_ARRAY) {
         var0.skipValue();
      }

      var0.endArray();
      return new PointF(var2 * var1, var3 * var1);
   }

   private static PointF jsonNumbersToPoint(JsonReader var0, float var1) throws IOException {
      float var2 = (float)var0.nextDouble();
      float var3 = (float)var0.nextDouble();

      while(var0.hasNext()) {
         var0.skipValue();
      }

      return new PointF(var2 * var1, var3 * var1);
   }

   private static PointF jsonObjectToPoint(JsonReader var0, float var1) throws IOException {
      var0.beginObject();
      float var2 = 0.0F;
      float var3 = 0.0F;

      while(var0.hasNext()) {
         String var4 = var0.nextName();
         byte var5 = -1;
         int var6 = var4.hashCode();
         if (var6 != 120) {
            if (var6 == 121 && var4.equals("y")) {
               var5 = 1;
            }
         } else if (var4.equals("x")) {
            var5 = 0;
         }

         if (var5 != 0) {
            if (var5 != 1) {
               var0.skipValue();
            } else {
               var3 = valueFromObject(var0);
            }
         } else {
            var2 = valueFromObject(var0);
         }
      }

      var0.endObject();
      return new PointF(var2 * var1, var3 * var1);
   }

   static int jsonToColor(JsonReader var0) throws IOException {
      var0.beginArray();
      int var1 = (int)(var0.nextDouble() * 255.0D);
      int var2 = (int)(var0.nextDouble() * 255.0D);
      int var3 = (int)(var0.nextDouble() * 255.0D);

      while(var0.hasNext()) {
         var0.skipValue();
      }

      var0.endArray();
      return Color.argb(255, var1, var2, var3);
   }

   static PointF jsonToPoint(JsonReader var0, float var1) throws IOException {
      int var2 = null.$SwitchMap$android$util$JsonToken[var0.peek().ordinal()];
      if (var2 != 1) {
         if (var2 != 2) {
            if (var2 == 3) {
               return jsonObjectToPoint(var0, var1);
            } else {
               StringBuilder var3 = new StringBuilder();
               var3.append("Unknown point starts with ");
               var3.append(var0.peek());
               throw new IllegalArgumentException(var3.toString());
            }
         } else {
            return jsonArrayToPoint(var0, var1);
         }
      } else {
         return jsonNumbersToPoint(var0, var1);
      }
   }

   static List jsonToPoints(JsonReader var0, float var1) throws IOException {
      ArrayList var2 = new ArrayList();
      var0.beginArray();

      while(var0.peek() == JsonToken.BEGIN_ARRAY) {
         var0.beginArray();
         var2.add(jsonToPoint(var0, var1));
         var0.endArray();
      }

      var0.endArray();
      return var2;
   }

   static float valueFromObject(JsonReader var0) throws IOException {
      JsonToken var1 = var0.peek();
      int var2 = null.$SwitchMap$android$util$JsonToken[var1.ordinal()];
      if (var2 == 1) {
         return (float)var0.nextDouble();
      } else if (var2 != 2) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Unknown value for token of type ");
         var4.append(var1);
         throw new IllegalArgumentException(var4.toString());
      } else {
         var0.beginArray();
         float var3 = (float)var0.nextDouble();

         while(var0.hasNext()) {
            var0.skipValue();
         }

         var0.endArray();
         return var3;
      }
   }
}
