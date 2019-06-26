package com.github.mikephil.charting.data;

import android.graphics.Paint.Style;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class CandleDataSet extends LineScatterCandleRadarDataSet implements ICandleDataSet {
   private float mBarSpace = 0.1F;
   protected int mDecreasingColor;
   protected Style mDecreasingPaintStyle;
   protected int mIncreasingColor;
   protected Style mIncreasingPaintStyle;
   protected int mNeutralColor;
   protected int mShadowColor;
   private boolean mShadowColorSameAsCandle = false;
   private float mShadowWidth = 3.0F;
   private boolean mShowCandleBar = true;

   public CandleDataSet(List var1, String var2) {
      super(var1, var2);
      this.mIncreasingPaintStyle = Style.STROKE;
      this.mDecreasingPaintStyle = Style.FILL;
      this.mNeutralColor = 1122868;
      this.mIncreasingColor = 1122868;
      this.mDecreasingColor = 1122868;
      this.mShadowColor = 1122868;
   }

   protected void calcMinMax(CandleEntry var1) {
      if (var1.getLow() < this.mYMin) {
         this.mYMin = var1.getLow();
      }

      if (var1.getHigh() > this.mYMax) {
         this.mYMax = var1.getHigh();
      }

      this.calcMinMaxX(var1);
   }

   protected void calcMinMaxY(CandleEntry var1) {
      if (var1.getHigh() < this.mYMin) {
         this.mYMin = var1.getHigh();
      }

      if (var1.getHigh() > this.mYMax) {
         this.mYMax = var1.getHigh();
      }

      if (var1.getLow() < this.mYMin) {
         this.mYMin = var1.getLow();
      }

      if (var1.getLow() > this.mYMax) {
         this.mYMax = var1.getLow();
      }

   }

   public DataSet copy() {
      ArrayList var1 = new ArrayList();
      var1.clear();

      for(int var2 = 0; var2 < this.mValues.size(); ++var2) {
         var1.add(((CandleEntry)this.mValues.get(var2)).copy());
      }

      CandleDataSet var3 = new CandleDataSet(var1, this.getLabel());
      var3.mColors = this.mColors;
      var3.mShadowWidth = this.mShadowWidth;
      var3.mShowCandleBar = this.mShowCandleBar;
      var3.mBarSpace = this.mBarSpace;
      var3.mHighLightColor = this.mHighLightColor;
      var3.mIncreasingPaintStyle = this.mIncreasingPaintStyle;
      var3.mDecreasingPaintStyle = this.mDecreasingPaintStyle;
      var3.mShadowColor = this.mShadowColor;
      return var3;
   }

   public float getBarSpace() {
      return this.mBarSpace;
   }

   public int getDecreasingColor() {
      return this.mDecreasingColor;
   }

   public Style getDecreasingPaintStyle() {
      return this.mDecreasingPaintStyle;
   }

   public int getIncreasingColor() {
      return this.mIncreasingColor;
   }

   public Style getIncreasingPaintStyle() {
      return this.mIncreasingPaintStyle;
   }

   public int getNeutralColor() {
      return this.mNeutralColor;
   }

   public int getShadowColor() {
      return this.mShadowColor;
   }

   public boolean getShadowColorSameAsCandle() {
      return this.mShadowColorSameAsCandle;
   }

   public float getShadowWidth() {
      return this.mShadowWidth;
   }

   public boolean getShowCandleBar() {
      return this.mShowCandleBar;
   }

   public void setBarSpace(float var1) {
      float var2 = var1;
      if (var1 < 0.0F) {
         var2 = 0.0F;
      }

      var1 = var2;
      if (var2 > 0.45F) {
         var1 = 0.45F;
      }

      this.mBarSpace = var1;
   }

   public void setDecreasingColor(int var1) {
      this.mDecreasingColor = var1;
   }

   public void setDecreasingPaintStyle(Style var1) {
      this.mDecreasingPaintStyle = var1;
   }

   public void setIncreasingColor(int var1) {
      this.mIncreasingColor = var1;
   }

   public void setIncreasingPaintStyle(Style var1) {
      this.mIncreasingPaintStyle = var1;
   }

   public void setNeutralColor(int var1) {
      this.mNeutralColor = var1;
   }

   public void setShadowColor(int var1) {
      this.mShadowColor = var1;
   }

   public void setShadowColorSameAsCandle(boolean var1) {
      this.mShadowColorSameAsCandle = var1;
   }

   public void setShadowWidth(float var1) {
      this.mShadowWidth = Utils.convertDpToPixel(var1);
   }

   public void setShowCandleBar(boolean var1) {
      this.mShowCandleBar = var1;
   }
}
