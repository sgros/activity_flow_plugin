package com.github.mikephil.charting.data;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

public class PieData extends ChartData {
   public PieData() {
   }

   public PieData(IPieDataSet var1) {
      super((IDataSet[])(var1));
   }

   public IPieDataSet getDataSet() {
      return (IPieDataSet)this.mDataSets.get(0);
   }

   public IPieDataSet getDataSetByIndex(int var1) {
      IPieDataSet var2;
      if (var1 == 0) {
         var2 = this.getDataSet();
      } else {
         var2 = null;
      }

      return var2;
   }

   public IPieDataSet getDataSetByLabel(String var1, boolean var2) {
      IPieDataSet var3 = null;
      if (var2) {
         if (var1.equalsIgnoreCase(((IPieDataSet)this.mDataSets.get(0)).getLabel())) {
            var3 = (IPieDataSet)this.mDataSets.get(0);
         }
      } else if (var1.equals(((IPieDataSet)this.mDataSets.get(0)).getLabel())) {
         var3 = (IPieDataSet)this.mDataSets.get(0);
      }

      return var3;
   }

   public Entry getEntryForHighlight(Highlight var1) {
      return this.getDataSet().getEntryForIndex((int)var1.getX());
   }

   public float getYValueSum() {
      float var1 = 0.0F;

      for(int var2 = 0; var2 < this.getDataSet().getEntryCount(); ++var2) {
         var1 += ((PieEntry)this.getDataSet().getEntryForIndex(var2)).getY();
      }

      return var1;
   }

   public void setDataSet(IPieDataSet var1) {
      this.mDataSets.clear();
      this.mDataSets.add(var1);
      this.notifyDataChanged();
   }
}
