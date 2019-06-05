package com.airbnb.lottie.parser;

import android.graphics.Color;
import android.util.JsonReader;
import android.util.JsonToken;
import com.airbnb.lottie.model.content.GradientColor;
import com.airbnb.lottie.utils.MiscUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GradientColorParser implements ValueParser {
   private int colorPoints;

   public GradientColorParser(int var1) {
      this.colorPoints = var1;
   }

   private void addOpacityStopsToGradientIfNeeded(GradientColor var1, List var2) {
      int var3 = this.colorPoints * 4;
      if (var2.size() > var3) {
         int var4 = (var2.size() - var3) / 2;
         double[] var5 = new double[var4];
         double[] var6 = new double[var4];
         byte var7 = 0;
         int var8 = 0;

         while(true) {
            var4 = var7;
            if (var3 >= var2.size()) {
               while(var4 < var1.getSize()) {
                  var3 = var1.getColors()[var4];
                  var3 = Color.argb(this.getOpacityAtPosition((double)var1.getPositions()[var4], var5, var6), Color.red(var3), Color.green(var3), Color.blue(var3));
                  var1.getColors()[var4] = var3;
                  ++var4;
               }

               return;
            }

            if (var3 % 2 == 0) {
               var5[var8] = (double)(Float)var2.get(var3);
            } else {
               var6[var8] = (double)(Float)var2.get(var3);
               ++var8;
            }

            ++var3;
         }
      }
   }

   private int getOpacityAtPosition(double var1, double[] var3, double[] var4) {
      for(int var5 = 1; var5 < var3.length; ++var5) {
         int var6 = var5 - 1;
         double var7 = var3[var6];
         double var9 = var3[var5];
         if (var3[var5] >= var1) {
            var1 = (var1 - var7) / (var9 - var7);
            return (int)(MiscUtils.lerp(var4[var6], var4[var5], var1) * 255.0D);
         }
      }

      return (int)(var4[var4.length - 1] * 255.0D);
   }

   public GradientColor parse(JsonReader var1, float var2) throws IOException {
      ArrayList var3 = new ArrayList();
      JsonToken var4 = var1.peek();
      JsonToken var5 = JsonToken.BEGIN_ARRAY;
      byte var6 = 0;
      boolean var7;
      if (var4 == var5) {
         var7 = true;
      } else {
         var7 = false;
      }

      if (var7) {
         var1.beginArray();
      }

      while(var1.hasNext()) {
         var3.add((float)var1.nextDouble());
      }

      if (var7) {
         var1.endArray();
      }

      if (this.colorPoints == -1) {
         this.colorPoints = var3.size() / 4;
      }

      float[] var12 = new float[this.colorPoints];
      int[] var14 = new int[this.colorPoints];
      byte var8 = 0;
      int var9 = 0;
      int var16 = var6;

      for(int var15 = var8; var16 < this.colorPoints * 4; ++var16) {
         int var17 = var16 / 4;
         double var10 = (double)(Float)var3.get(var16);
         switch(var16 % 4) {
         case 0:
            var12[var17] = (float)var10;
            break;
         case 1:
            var15 = (int)(var10 * 255.0D);
            break;
         case 2:
            var9 = (int)(var10 * 255.0D);
            break;
         case 3:
            var14[var17] = Color.argb(255, var15, var9, (int)(var10 * 255.0D));
         }
      }

      GradientColor var13 = new GradientColor(var12, var14);
      this.addOpacityStopsToGradientIfNeeded(var13, var3);
      return var13;
   }
}
