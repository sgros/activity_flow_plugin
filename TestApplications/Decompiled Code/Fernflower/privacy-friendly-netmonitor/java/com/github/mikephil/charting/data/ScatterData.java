package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import java.util.Iterator;
import java.util.List;

public class ScatterData extends BarLineScatterCandleBubbleData {
   public ScatterData() {
   }

   public ScatterData(List var1) {
      super(var1);
   }

   public ScatterData(IScatterDataSet... var1) {
      super((IBarLineScatterCandleBubbleDataSet[])var1);
   }

   public float getGreatestShapeSize() {
      Iterator var1 = this.mDataSets.iterator();
      float var2 = 0.0F;

      while(var1.hasNext()) {
         float var3 = ((IScatterDataSet)var1.next()).getScatterShapeSize();
         if (var3 > var2) {
            var2 = var3;
         }
      }

      return var2;
   }
}
