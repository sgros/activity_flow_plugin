package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.util.List;

public class LineData extends BarLineScatterCandleBubbleData {
   public LineData() {
   }

   public LineData(List var1) {
      super(var1);
   }

   public LineData(ILineDataSet... var1) {
      super((IBarLineScatterCandleBubbleDataSet[])var1);
   }
}
