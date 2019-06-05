package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import java.util.List;

public abstract class BarLineScatterCandleBubbleData extends ChartData {
   public BarLineScatterCandleBubbleData() {
   }

   public BarLineScatterCandleBubbleData(List var1) {
      super(var1);
   }

   public BarLineScatterCandleBubbleData(IBarLineScatterCandleBubbleDataSet... var1) {
      super((IDataSet[])var1);
   }
}
