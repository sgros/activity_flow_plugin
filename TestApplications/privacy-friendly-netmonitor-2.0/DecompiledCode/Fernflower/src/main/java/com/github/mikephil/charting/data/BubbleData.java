package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import java.util.Iterator;
import java.util.List;

public class BubbleData extends BarLineScatterCandleBubbleData {
   public BubbleData() {
   }

   public BubbleData(List var1) {
      super(var1);
   }

   public BubbleData(IBubbleDataSet... var1) {
      super((IBarLineScatterCandleBubbleDataSet[])var1);
   }

   public void setHighlightCircleWidth(float var1) {
      Iterator var2 = this.mDataSets.iterator();

      while(var2.hasNext()) {
         ((IBubbleDataSet)var2.next()).setHighlightCircleWidth(var1);
      }

   }
}
