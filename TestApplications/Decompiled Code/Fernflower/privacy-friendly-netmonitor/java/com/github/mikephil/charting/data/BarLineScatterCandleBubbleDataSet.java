package com.github.mikephil.charting.data;

import android.graphics.Color;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import java.util.List;

public abstract class BarLineScatterCandleBubbleDataSet extends DataSet implements IBarLineScatterCandleBubbleDataSet {
   protected int mHighLightColor = Color.rgb(255, 187, 115);

   public BarLineScatterCandleBubbleDataSet(List var1, String var2) {
      super(var1, var2);
   }

   public int getHighLightColor() {
      return this.mHighLightColor;
   }

   public void setHighLightColor(int var1) {
      this.mHighLightColor = var1;
   }
}
