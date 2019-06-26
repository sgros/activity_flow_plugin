package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import java.util.List;

public interface IDataSet {
   boolean addEntry(Entry var1);

   void addEntryOrdered(Entry var1);

   void calcMinMax();

   void calcMinMaxY(float var1, float var2);

   void clear();

   boolean contains(Entry var1);

   YAxis.AxisDependency getAxisDependency();

   int getColor();

   int getColor(int var1);

   List getColors();

   List getEntriesForXValue(float var1);

   int getEntryCount();

   Entry getEntryForIndex(int var1);

   Entry getEntryForXValue(float var1, float var2);

   Entry getEntryForXValue(float var1, float var2, DataSet.Rounding var3);

   int getEntryIndex(float var1, float var2, DataSet.Rounding var3);

   int getEntryIndex(Entry var1);

   Legend.LegendForm getForm();

   DashPathEffect getFormLineDashEffect();

   float getFormLineWidth();

   float getFormSize();

   MPPointF getIconsOffset();

   int getIndexInEntries(int var1);

   String getLabel();

   IValueFormatter getValueFormatter();

   int getValueTextColor();

   int getValueTextColor(int var1);

   float getValueTextSize();

   Typeface getValueTypeface();

   float getXMax();

   float getXMin();

   float getYMax();

   float getYMin();

   boolean isDrawIconsEnabled();

   boolean isDrawValuesEnabled();

   boolean isHighlightEnabled();

   boolean isVisible();

   boolean needsFormatter();

   boolean removeEntry(int var1);

   boolean removeEntry(Entry var1);

   boolean removeEntryByXValue(float var1);

   boolean removeFirst();

   boolean removeLast();

   void setAxisDependency(YAxis.AxisDependency var1);

   void setDrawIcons(boolean var1);

   void setDrawValues(boolean var1);

   void setHighlightEnabled(boolean var1);

   void setIconsOffset(MPPointF var1);

   void setLabel(String var1);

   void setValueFormatter(IValueFormatter var1);

   void setValueTextColor(int var1);

   void setValueTextColors(List var1);

   void setValueTextSize(float var1);

   void setValueTypeface(Typeface var1);

   void setVisible(boolean var1);
}
