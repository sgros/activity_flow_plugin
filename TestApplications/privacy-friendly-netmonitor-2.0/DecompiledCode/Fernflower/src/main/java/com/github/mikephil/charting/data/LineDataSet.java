package com.github.mikephil.charting.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.Log;
import com.github.mikephil.charting.formatter.DefaultFillFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class LineDataSet extends LineRadarDataSet implements ILineDataSet {
   private int mCircleColorHole;
   private List mCircleColors;
   private float mCircleHoleRadius;
   private float mCircleRadius;
   private float mCubicIntensity;
   private DashPathEffect mDashPathEffect;
   private boolean mDrawCircleHole;
   private boolean mDrawCircles;
   private IFillFormatter mFillFormatter;
   private LineDataSet.Mode mMode;

   public LineDataSet(List var1, String var2) {
      super(var1, var2);
      this.mMode = LineDataSet.Mode.LINEAR;
      this.mCircleColors = null;
      this.mCircleColorHole = -1;
      this.mCircleRadius = 8.0F;
      this.mCircleHoleRadius = 4.0F;
      this.mCubicIntensity = 0.2F;
      this.mDashPathEffect = null;
      this.mFillFormatter = new DefaultFillFormatter();
      this.mDrawCircles = true;
      this.mDrawCircleHole = true;
      if (this.mCircleColors == null) {
         this.mCircleColors = new ArrayList();
      }

      this.mCircleColors.clear();
      this.mCircleColors.add(Color.rgb(140, 234, 255));
   }

   public DataSet copy() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.mValues.size(); ++var2) {
         var1.add(((Entry)this.mValues.get(var2)).copy());
      }

      LineDataSet var3 = new LineDataSet(var1, this.getLabel());
      var3.mMode = this.mMode;
      var3.mColors = this.mColors;
      var3.mCircleRadius = this.mCircleRadius;
      var3.mCircleHoleRadius = this.mCircleHoleRadius;
      var3.mCircleColors = this.mCircleColors;
      var3.mDashPathEffect = this.mDashPathEffect;
      var3.mDrawCircles = this.mDrawCircles;
      var3.mDrawCircleHole = this.mDrawCircleHole;
      var3.mHighLightColor = this.mHighLightColor;
      return var3;
   }

   public void disableDashedLine() {
      this.mDashPathEffect = null;
   }

   public void enableDashedLine(float var1, float var2, float var3) {
      this.mDashPathEffect = new DashPathEffect(new float[]{var1, var2}, var3);
   }

   public int getCircleColor(int var1) {
      return (Integer)this.mCircleColors.get(var1);
   }

   public int getCircleColorCount() {
      return this.mCircleColors.size();
   }

   public List getCircleColors() {
      return this.mCircleColors;
   }

   public int getCircleHoleColor() {
      return this.mCircleColorHole;
   }

   public float getCircleHoleRadius() {
      return this.mCircleHoleRadius;
   }

   public float getCircleRadius() {
      return this.mCircleRadius;
   }

   @Deprecated
   public float getCircleSize() {
      return this.getCircleRadius();
   }

   public float getCubicIntensity() {
      return this.mCubicIntensity;
   }

   public DashPathEffect getDashPathEffect() {
      return this.mDashPathEffect;
   }

   public IFillFormatter getFillFormatter() {
      return this.mFillFormatter;
   }

   public LineDataSet.Mode getMode() {
      return this.mMode;
   }

   public boolean isDashedLineEnabled() {
      boolean var1;
      if (this.mDashPathEffect == null) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isDrawCircleHoleEnabled() {
      return this.mDrawCircleHole;
   }

   public boolean isDrawCirclesEnabled() {
      return this.mDrawCircles;
   }

   @Deprecated
   public boolean isDrawCubicEnabled() {
      boolean var1;
      if (this.mMode == LineDataSet.Mode.CUBIC_BEZIER) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @Deprecated
   public boolean isDrawSteppedEnabled() {
      boolean var1;
      if (this.mMode == LineDataSet.Mode.STEPPED) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void resetCircleColors() {
      if (this.mCircleColors == null) {
         this.mCircleColors = new ArrayList();
      }

      this.mCircleColors.clear();
   }

   public void setCircleColor(int var1) {
      this.resetCircleColors();
      this.mCircleColors.add(var1);
   }

   public void setCircleColorHole(int var1) {
      this.mCircleColorHole = var1;
   }

   public void setCircleColors(List var1) {
      this.mCircleColors = var1;
   }

   public void setCircleColors(int... var1) {
      this.mCircleColors = ColorTemplate.createColors(var1);
   }

   public void setCircleColors(int[] var1, Context var2) {
      List var3 = this.mCircleColors;
      Object var4 = var3;
      if (var3 == null) {
         var4 = new ArrayList();
      }

      ((List)var4).clear();
      int var5 = var1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         int var7 = var1[var6];
         ((List)var4).add(var2.getResources().getColor(var7));
      }

      this.mCircleColors = (List)var4;
   }

   public void setCircleHoleRadius(float var1) {
      if (var1 >= 0.5F) {
         this.mCircleHoleRadius = Utils.convertDpToPixel(var1);
      } else {
         Log.e("LineDataSet", "Circle radius cannot be < 0.5");
      }

   }

   public void setCircleRadius(float var1) {
      if (var1 >= 1.0F) {
         this.mCircleRadius = Utils.convertDpToPixel(var1);
      } else {
         Log.e("LineDataSet", "Circle radius cannot be < 1");
      }

   }

   @Deprecated
   public void setCircleSize(float var1) {
      this.setCircleRadius(var1);
   }

   public void setCubicIntensity(float var1) {
      float var2 = var1;
      if (var1 > 1.0F) {
         var2 = 1.0F;
      }

      var1 = var2;
      if (var2 < 0.05F) {
         var1 = 0.05F;
      }

      this.mCubicIntensity = var1;
   }

   public void setDrawCircleHole(boolean var1) {
      this.mDrawCircleHole = var1;
   }

   public void setDrawCircles(boolean var1) {
      this.mDrawCircles = var1;
   }

   public void setFillFormatter(IFillFormatter var1) {
      if (var1 == null) {
         this.mFillFormatter = new DefaultFillFormatter();
      } else {
         this.mFillFormatter = var1;
      }

   }

   public void setMode(LineDataSet.Mode var1) {
      this.mMode = var1;
   }

   public static enum Mode {
      CUBIC_BEZIER,
      HORIZONTAL_BEZIER,
      LINEAR,
      STEPPED;
   }
}
