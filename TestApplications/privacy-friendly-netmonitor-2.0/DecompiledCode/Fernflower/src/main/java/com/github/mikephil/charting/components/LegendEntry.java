package com.github.mikephil.charting.components;

import android.graphics.DashPathEffect;

public class LegendEntry {
   public Legend.LegendForm form;
   public int formColor;
   public DashPathEffect formLineDashEffect;
   public float formLineWidth;
   public float formSize;
   public String label;

   public LegendEntry() {
      this.form = Legend.LegendForm.DEFAULT;
      this.formSize = Float.NaN;
      this.formLineWidth = Float.NaN;
      this.formLineDashEffect = null;
      this.formColor = 1122867;
   }

   public LegendEntry(String var1, Legend.LegendForm var2, float var3, float var4, DashPathEffect var5, int var6) {
      this.form = Legend.LegendForm.DEFAULT;
      this.formSize = Float.NaN;
      this.formLineWidth = Float.NaN;
      this.formLineDashEffect = null;
      this.formColor = 1122867;
      this.label = var1;
      this.form = var2;
      this.formSize = var3;
      this.formLineWidth = var4;
      this.formLineDashEffect = var5;
      this.formColor = var6;
   }
}
