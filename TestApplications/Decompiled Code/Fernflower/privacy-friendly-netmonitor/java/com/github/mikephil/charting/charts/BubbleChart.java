package com.github.mikephil.charting.charts;

import android.content.Context;
import android.util.AttributeSet;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
import com.github.mikephil.charting.renderer.BubbleChartRenderer;

public class BubbleChart extends BarLineChartBase implements BubbleDataProvider {
   public BubbleChart(Context var1) {
      super(var1);
   }

   public BubbleChart(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public BubbleChart(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   public BubbleData getBubbleData() {
      return (BubbleData)this.mData;
   }

   protected void init() {
      super.init();
      this.mRenderer = new BubbleChartRenderer(this, this.mAnimator, this.mViewPortHandler);
   }
}
