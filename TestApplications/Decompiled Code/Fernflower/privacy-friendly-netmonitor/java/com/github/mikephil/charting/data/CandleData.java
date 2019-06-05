package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import java.util.List;

public class CandleData extends BarLineScatterCandleBubbleData {
   public CandleData() {
   }

   public CandleData(List var1) {
      super(var1);
   }

   public CandleData(ICandleDataSet... var1) {
      super((IBarLineScatterCandleBubbleDataSet[])var1);
   }
}
