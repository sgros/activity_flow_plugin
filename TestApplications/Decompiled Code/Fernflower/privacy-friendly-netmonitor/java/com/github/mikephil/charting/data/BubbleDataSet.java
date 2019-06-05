package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class BubbleDataSet extends BarLineScatterCandleBubbleDataSet implements IBubbleDataSet {
   private float mHighlightCircleWidth = 2.5F;
   protected float mMaxSize;
   protected boolean mNormalizeSize = true;

   public BubbleDataSet(List var1, String var2) {
      super(var1, var2);
   }

   protected void calcMinMax(BubbleEntry var1) {
      super.calcMinMax(var1);
      float var2 = var1.getSize();
      if (var2 > this.mMaxSize) {
         this.mMaxSize = var2;
      }

   }

   public DataSet copy() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.mValues.size(); ++var2) {
         var1.add(((BubbleEntry)this.mValues.get(var2)).copy());
      }

      BubbleDataSet var3 = new BubbleDataSet(var1, this.getLabel());
      var3.mColors = this.mColors;
      var3.mHighLightColor = this.mHighLightColor;
      return var3;
   }

   public float getHighlightCircleWidth() {
      return this.mHighlightCircleWidth;
   }

   public float getMaxSize() {
      return this.mMaxSize;
   }

   public boolean isNormalizeSizeEnabled() {
      return this.mNormalizeSize;
   }

   public void setHighlightCircleWidth(float var1) {
      this.mHighlightCircleWidth = Utils.convertDpToPixel(var1);
   }

   public void setNormalizeSizeEnabled(boolean var1) {
      this.mNormalizeSize = var1;
   }
}
