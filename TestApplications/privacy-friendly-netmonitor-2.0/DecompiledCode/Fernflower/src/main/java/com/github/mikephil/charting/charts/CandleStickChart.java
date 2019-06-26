package com.github.mikephil.charting.charts;

import android.content.Context;
import android.util.AttributeSet;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import com.github.mikephil.charting.renderer.CandleStickChartRenderer;

public class CandleStickChart extends BarLineChartBase implements CandleDataProvider {
   public CandleStickChart(Context var1) {
      super(var1);
   }

   public CandleStickChart(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public CandleStickChart(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   public CandleData getCandleData() {
      return (CandleData)this.mData;
   }

   protected void init() {
      super.init();
      this.mRenderer = new CandleStickChartRenderer(this, this.mAnimator, this.mViewPortHandler);
      this.getXAxis().setSpaceMin(0.5F);
      this.getXAxis().setSpaceMax(0.5F);
   }
}
