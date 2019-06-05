package com.google.zxing.datamatrix.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.detector.MathUtils;
import com.google.zxing.common.detector.WhiteRectangleDetector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class Detector {
   private final BitMatrix image;
   private final WhiteRectangleDetector rectangleDetector;

   public Detector(BitMatrix var1) throws NotFoundException {
      this.image = var1;
      this.rectangleDetector = new WhiteRectangleDetector(var1);
   }

   private ResultPoint correctTopRight(ResultPoint var1, ResultPoint var2, ResultPoint var3, ResultPoint var4, int var5) {
      float var6 = (float)distance(var1, var2) / (float)var5;
      int var7 = distance(var3, var4);
      float var8 = (var4.getX() - var3.getX()) / (float)var7;
      float var9 = (var4.getY() - var3.getY()) / (float)var7;
      ResultPoint var10 = new ResultPoint(var4.getX() + var6 * var8, var4.getY() + var6 * var9);
      var8 = (float)distance(var1, var3) / (float)var5;
      var5 = distance(var2, var4);
      var6 = (var4.getX() - var2.getX()) / (float)var5;
      var9 = (var4.getY() - var2.getY()) / (float)var5;
      var4 = new ResultPoint(var4.getX() + var8 * var6, var4.getY() + var8 * var9);
      if (!this.isValid(var10)) {
         if (this.isValid(var4)) {
            var1 = var4;
         } else {
            var1 = null;
         }
      } else if (!this.isValid(var4)) {
         var1 = var10;
      } else {
         var1 = var4;
         if (Math.abs(this.transitionsBetween(var3, var10).getTransitions() - this.transitionsBetween(var2, var10).getTransitions()) <= Math.abs(this.transitionsBetween(var3, var4).getTransitions() - this.transitionsBetween(var2, var4).getTransitions())) {
            var1 = var10;
         }
      }

      return var1;
   }

   private ResultPoint correctTopRightRectangular(ResultPoint var1, ResultPoint var2, ResultPoint var3, ResultPoint var4, int var5, int var6) {
      float var7 = (float)distance(var1, var2) / (float)var5;
      int var8 = distance(var3, var4);
      float var9 = (var4.getX() - var3.getX()) / (float)var8;
      float var10 = (var4.getY() - var3.getY()) / (float)var8;
      ResultPoint var11 = new ResultPoint(var4.getX() + var7 * var9, var4.getY() + var7 * var10);
      var10 = (float)distance(var1, var3) / (float)var6;
      var8 = distance(var2, var4);
      var7 = (var4.getX() - var2.getX()) / (float)var8;
      var9 = (var4.getY() - var2.getY()) / (float)var8;
      var4 = new ResultPoint(var4.getX() + var10 * var7, var4.getY() + var10 * var9);
      if (!this.isValid(var11)) {
         if (this.isValid(var4)) {
            var1 = var4;
         } else {
            var1 = null;
         }
      } else if (!this.isValid(var4)) {
         var1 = var11;
      } else {
         var1 = var4;
         if (Math.abs(var5 - this.transitionsBetween(var3, var11).getTransitions()) + Math.abs(var6 - this.transitionsBetween(var2, var11).getTransitions()) <= Math.abs(var5 - this.transitionsBetween(var3, var4).getTransitions()) + Math.abs(var6 - this.transitionsBetween(var2, var4).getTransitions())) {
            var1 = var11;
         }
      }

      return var1;
   }

   private static int distance(ResultPoint var0, ResultPoint var1) {
      return MathUtils.round(ResultPoint.distance(var0, var1));
   }

   private static void increment(Map var0, ResultPoint var1) {
      Integer var2 = (Integer)var0.get(var1);
      int var3;
      if (var2 == null) {
         var3 = 1;
      } else {
         var3 = var2 + 1;
      }

      var0.put(var1, var3);
   }

   private boolean isValid(ResultPoint var1) {
      boolean var2;
      if (var1.getX() >= 0.0F && var1.getX() < (float)this.image.getWidth() && var1.getY() > 0.0F && var1.getY() < (float)this.image.getHeight()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static BitMatrix sampleGrid(BitMatrix var0, ResultPoint var1, ResultPoint var2, ResultPoint var3, ResultPoint var4, int var5, int var6) throws NotFoundException {
      return GridSampler.getInstance().sampleGrid(var0, var5, var6, 0.5F, 0.5F, (float)var5 - 0.5F, 0.5F, (float)var5 - 0.5F, (float)var6 - 0.5F, 0.5F, (float)var6 - 0.5F, var1.getX(), var1.getY(), var4.getX(), var4.getY(), var3.getX(), var3.getY(), var2.getX(), var2.getY());
   }

   private Detector.ResultPointsAndTransitions transitionsBetween(ResultPoint var1, ResultPoint var2) {
      int var3 = (int)var1.getX();
      int var4 = (int)var1.getY();
      int var5 = (int)var2.getX();
      int var6 = (int)var2.getY();
      boolean var7;
      if (Math.abs(var6 - var4) > Math.abs(var5 - var3)) {
         var7 = true;
      } else {
         var7 = false;
      }

      int var8 = var3;
      int var9 = var4;
      int var10 = var5;
      int var11 = var6;
      if (var7) {
         var8 = var4;
         var9 = var3;
         var11 = var5;
         var10 = var6;
      }

      int var12 = Math.abs(var10 - var8);
      int var13 = Math.abs(var11 - var9);
      int var14 = -var12 / 2;
      byte var20;
      if (var9 < var11) {
         var20 = 1;
      } else {
         var20 = -1;
      }

      byte var21;
      if (var8 < var10) {
         var21 = 1;
      } else {
         var21 = -1;
      }

      byte var15 = 0;
      BitMatrix var16 = this.image;
      if (var7) {
         var5 = var9;
      } else {
         var5 = var8;
      }

      if (var7) {
         var6 = var8;
      } else {
         var6 = var9;
      }

      boolean var17 = var16.get(var5, var6);
      var5 = var8;
      var8 = var9;
      var9 = var5;
      var5 = var15;

      int var22;
      while(true) {
         var22 = var5;
         if (var9 == var10) {
            break;
         }

         var16 = this.image;
         if (var7) {
            var6 = var8;
         } else {
            var6 = var9;
         }

         if (var7) {
            var22 = var9;
         } else {
            var22 = var8;
         }

         boolean var18 = var16.get(var6, var22);
         boolean var19 = var17;
         var6 = var5;
         if (var18 != var17) {
            var6 = var5 + 1;
            var19 = var18;
         }

         var14 += var13;
         var5 = var14;
         var22 = var8;
         if (var14 > 0) {
            var22 = var6;
            if (var8 == var11) {
               break;
            }

            var22 = var8 + var20;
            var5 = var14 - var12;
         }

         var9 += var21;
         var14 = var5;
         var17 = var19;
         var5 = var6;
         var8 = var22;
      }

      return new Detector.ResultPointsAndTransitions(var1, var2, var22);
   }

   public DetectorResult detect() throws NotFoundException {
      ResultPoint[] var1 = this.rectangleDetector.detect();
      ResultPoint var2 = var1[0];
      ResultPoint var3 = var1[1];
      ResultPoint var4 = var1[2];
      ResultPoint var5 = var1[3];
      ArrayList var6 = new ArrayList(4);
      var6.add(this.transitionsBetween(var2, var3));
      var6.add(this.transitionsBetween(var2, var4));
      var6.add(this.transitionsBetween(var3, var5));
      var6.add(this.transitionsBetween(var4, var5));
      Collections.sort(var6, new Detector.ResultPointsAndTransitionsComparator());
      Detector.ResultPointsAndTransitions var15 = (Detector.ResultPointsAndTransitions)var6.get(0);
      Detector.ResultPointsAndTransitions var18 = (Detector.ResultPointsAndTransitions)var6.get(1);
      HashMap var7 = new HashMap();
      increment(var7, var15.getFrom());
      increment(var7, var15.getTo());
      increment(var7, var18.getFrom());
      increment(var7, var18.getTo());
      ResultPoint var19 = null;
      ResultPoint var8 = null;
      ResultPoint var9 = null;
      Iterator var10 = var7.entrySet().iterator();

      ResultPoint var16;
      while(var10.hasNext()) {
         Entry var11 = (Entry)var10.next();
         var16 = (ResultPoint)var11.getKey();
         if ((Integer)var11.getValue() == 2) {
            var8 = var16;
         } else if (var19 == null) {
            var19 = var16;
         } else {
            var9 = var16;
         }
      }

      if (var19 != null && var8 != null && var9 != null) {
         var1 = new ResultPoint[]{var19, var8, var9};
         ResultPoint.orderBestPatterns(var1);
         ResultPoint var20 = var1[0];
         var8 = var1[1];
         ResultPoint var21 = var1[2];
         if (!var7.containsKey(var2)) {
            var16 = var2;
         } else if (!var7.containsKey(var3)) {
            var16 = var3;
         } else if (!var7.containsKey(var4)) {
            var16 = var4;
         } else {
            var16 = var5;
         }

         int var12 = this.transitionsBetween(var21, var16).getTransitions();
         int var13 = this.transitionsBetween(var20, var16).getTransitions();
         int var14 = var12;
         if ((var12 & 1) == 1) {
            var14 = var12 + 1;
         }

         var12 = var14 + 2;
         var14 = var13;
         if ((var13 & 1) == 1) {
            var14 = var13 + 1;
         }

         var14 += 2;
         BitMatrix var17;
         if (var12 * 4 < var14 * 7 && var14 * 4 < var12 * 7) {
            var9 = this.correctTopRight(var8, var20, var21, var16, Math.min(var14, var12));
            var19 = var9;
            if (var9 == null) {
               var19 = var16;
            }

            var13 = Math.max(this.transitionsBetween(var21, var19).getTransitions(), this.transitionsBetween(var20, var19).getTransitions()) + 1;
            var14 = var13;
            if ((var13 & 1) == 1) {
               var14 = var13 + 1;
            }

            var17 = sampleGrid(this.image, var21, var8, var20, var19, var14, var14);
         } else {
            var9 = this.correctTopRightRectangular(var8, var20, var21, var16, var12, var14);
            var19 = var9;
            if (var9 == null) {
               var19 = var16;
            }

            var13 = this.transitionsBetween(var21, var19).getTransitions();
            var12 = this.transitionsBetween(var20, var19).getTransitions();
            var14 = var13;
            if ((var13 & 1) == 1) {
               var14 = var13 + 1;
            }

            var13 = var12;
            if ((var12 & 1) == 1) {
               var13 = var12 + 1;
            }

            var17 = sampleGrid(this.image, var21, var8, var20, var19, var14, var13);
         }

         return new DetectorResult(var17, new ResultPoint[]{var21, var8, var20, var19});
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private static final class ResultPointsAndTransitions {
      private final ResultPoint from;
      private final ResultPoint to;
      private final int transitions;

      private ResultPointsAndTransitions(ResultPoint var1, ResultPoint var2, int var3) {
         this.from = var1;
         this.to = var2;
         this.transitions = var3;
      }

      // $FF: synthetic method
      ResultPointsAndTransitions(ResultPoint var1, ResultPoint var2, int var3, Object var4) {
         this(var1, var2, var3);
      }

      ResultPoint getFrom() {
         return this.from;
      }

      ResultPoint getTo() {
         return this.to;
      }

      int getTransitions() {
         return this.transitions;
      }

      public String toString() {
         return this.from + "/" + this.to + '/' + this.transitions;
      }
   }

   private static final class ResultPointsAndTransitionsComparator implements Serializable, Comparator {
      private ResultPointsAndTransitionsComparator() {
      }

      // $FF: synthetic method
      ResultPointsAndTransitionsComparator(Object var1) {
         this();
      }

      public int compare(Detector.ResultPointsAndTransitions var1, Detector.ResultPointsAndTransitions var2) {
         return var1.getTransitions() - var2.getTransitions();
      }
   }
}
