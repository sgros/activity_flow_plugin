package com.github.mikephil.charting.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDataSet implements IDataSet {
   protected YAxis.AxisDependency mAxisDependency;
   protected List mColors;
   protected boolean mDrawIcons;
   protected boolean mDrawValues;
   private Legend.LegendForm mForm;
   private DashPathEffect mFormLineDashEffect;
   private float mFormLineWidth;
   private float mFormSize;
   protected boolean mHighlightEnabled;
   protected MPPointF mIconsOffset;
   private String mLabel;
   protected List mValueColors;
   protected transient IValueFormatter mValueFormatter;
   protected float mValueTextSize;
   protected Typeface mValueTypeface;
   protected boolean mVisible;

   public BaseDataSet() {
      this.mColors = null;
      this.mValueColors = null;
      this.mLabel = "DataSet";
      this.mAxisDependency = YAxis.AxisDependency.LEFT;
      this.mHighlightEnabled = true;
      this.mForm = Legend.LegendForm.DEFAULT;
      this.mFormSize = Float.NaN;
      this.mFormLineWidth = Float.NaN;
      this.mFormLineDashEffect = null;
      this.mDrawValues = true;
      this.mDrawIcons = true;
      this.mIconsOffset = new MPPointF();
      this.mValueTextSize = 17.0F;
      this.mVisible = true;
      this.mColors = new ArrayList();
      this.mValueColors = new ArrayList();
      this.mColors.add(Color.rgb(140, 234, 255));
      this.mValueColors.add(-16777216);
   }

   public BaseDataSet(String var1) {
      this();
      this.mLabel = var1;
   }

   public void addColor(int var1) {
      if (this.mColors == null) {
         this.mColors = new ArrayList();
      }

      this.mColors.add(var1);
   }

   public boolean contains(Entry var1) {
      for(int var2 = 0; var2 < this.getEntryCount(); ++var2) {
         if (this.getEntryForIndex(var2).equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public YAxis.AxisDependency getAxisDependency() {
      return this.mAxisDependency;
   }

   public int getColor() {
      return (Integer)this.mColors.get(0);
   }

   public int getColor(int var1) {
      return (Integer)this.mColors.get(var1 % this.mColors.size());
   }

   public List getColors() {
      return this.mColors;
   }

   public Legend.LegendForm getForm() {
      return this.mForm;
   }

   public DashPathEffect getFormLineDashEffect() {
      return this.mFormLineDashEffect;
   }

   public float getFormLineWidth() {
      return this.mFormLineWidth;
   }

   public float getFormSize() {
      return this.mFormSize;
   }

   public MPPointF getIconsOffset() {
      return this.mIconsOffset;
   }

   public int getIndexInEntries(int var1) {
      for(int var2 = 0; var2 < this.getEntryCount(); ++var2) {
         if ((float)var1 == this.getEntryForIndex(var2).getX()) {
            return var2;
         }
      }

      return -1;
   }

   public String getLabel() {
      return this.mLabel;
   }

   public List getValueColors() {
      return this.mValueColors;
   }

   public IValueFormatter getValueFormatter() {
      return this.needsFormatter() ? Utils.getDefaultValueFormatter() : this.mValueFormatter;
   }

   public int getValueTextColor() {
      return (Integer)this.mValueColors.get(0);
   }

   public int getValueTextColor(int var1) {
      return (Integer)this.mValueColors.get(var1 % this.mValueColors.size());
   }

   public float getValueTextSize() {
      return this.mValueTextSize;
   }

   public Typeface getValueTypeface() {
      return this.mValueTypeface;
   }

   public boolean isDrawIconsEnabled() {
      return this.mDrawIcons;
   }

   public boolean isDrawValuesEnabled() {
      return this.mDrawValues;
   }

   public boolean isHighlightEnabled() {
      return this.mHighlightEnabled;
   }

   public boolean isVisible() {
      return this.mVisible;
   }

   public boolean needsFormatter() {
      boolean var1;
      if (this.mValueFormatter == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void notifyDataSetChanged() {
      this.calcMinMax();
   }

   public boolean removeEntry(int var1) {
      return this.removeEntry(this.getEntryForIndex(var1));
   }

   public boolean removeEntryByXValue(float var1) {
      return this.removeEntry(this.getEntryForXValue(var1, Float.NaN));
   }

   public boolean removeFirst() {
      return this.getEntryCount() > 0 ? this.removeEntry(this.getEntryForIndex(0)) : false;
   }

   public boolean removeLast() {
      return this.getEntryCount() > 0 ? this.removeEntry(this.getEntryForIndex(this.getEntryCount() - 1)) : false;
   }

   public void resetColors() {
      if (this.mColors == null) {
         this.mColors = new ArrayList();
      }

      this.mColors.clear();
   }

   public void setAxisDependency(YAxis.AxisDependency var1) {
      this.mAxisDependency = var1;
   }

   public void setColor(int var1) {
      this.resetColors();
      this.mColors.add(var1);
   }

   public void setColor(int var1, int var2) {
      this.setColor(Color.argb(var2, Color.red(var1), Color.green(var1), Color.blue(var1)));
   }

   public void setColors(List var1) {
      this.mColors = var1;
   }

   public void setColors(int... var1) {
      this.mColors = ColorTemplate.createColors(var1);
   }

   public void setColors(int[] var1, int var2) {
      this.resetColors();
      int var3 = 0;

      for(int var4 = var1.length; var3 < var4; ++var3) {
         int var5 = var1[var3];
         this.addColor(Color.argb(var2, Color.red(var5), Color.green(var5), Color.blue(var5)));
      }

   }

   public void setColors(int[] var1, Context var2) {
      if (this.mColors == null) {
         this.mColors = new ArrayList();
      }

      this.mColors.clear();
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var1[var4];
         this.mColors.add(var2.getResources().getColor(var5));
      }

   }

   public void setDrawIcons(boolean var1) {
      this.mDrawIcons = var1;
   }

   public void setDrawValues(boolean var1) {
      this.mDrawValues = var1;
   }

   public void setForm(Legend.LegendForm var1) {
      this.mForm = var1;
   }

   public void setFormLineDashEffect(DashPathEffect var1) {
      this.mFormLineDashEffect = var1;
   }

   public void setFormLineWidth(float var1) {
      this.mFormLineWidth = var1;
   }

   public void setFormSize(float var1) {
      this.mFormSize = var1;
   }

   public void setHighlightEnabled(boolean var1) {
      this.mHighlightEnabled = var1;
   }

   public void setIconsOffset(MPPointF var1) {
      this.mIconsOffset.x = var1.x;
      this.mIconsOffset.y = var1.y;
   }

   public void setLabel(String var1) {
      this.mLabel = var1;
   }

   public void setValueFormatter(IValueFormatter var1) {
      if (var1 != null) {
         this.mValueFormatter = var1;
      }
   }

   public void setValueTextColor(int var1) {
      this.mValueColors.clear();
      this.mValueColors.add(var1);
   }

   public void setValueTextColors(List var1) {
      this.mValueColors = var1;
   }

   public void setValueTextSize(float var1) {
      this.mValueTextSize = Utils.convertDpToPixel(var1);
   }

   public void setValueTypeface(Typeface var1) {
      this.mValueTypeface = var1;
   }

   public void setVisible(boolean var1) {
      this.mVisible = var1;
   }
}
