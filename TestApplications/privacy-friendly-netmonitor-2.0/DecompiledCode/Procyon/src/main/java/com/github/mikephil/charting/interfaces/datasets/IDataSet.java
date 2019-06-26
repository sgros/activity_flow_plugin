// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.Typeface;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import android.graphics.DashPathEffect;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.DataSet;
import java.util.List;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;

public interface IDataSet<T extends Entry>
{
    boolean addEntry(final T p0);
    
    void addEntryOrdered(final T p0);
    
    void calcMinMax();
    
    void calcMinMaxY(final float p0, final float p1);
    
    void clear();
    
    boolean contains(final T p0);
    
    YAxis.AxisDependency getAxisDependency();
    
    int getColor();
    
    int getColor(final int p0);
    
    List<Integer> getColors();
    
    List<T> getEntriesForXValue(final float p0);
    
    int getEntryCount();
    
    T getEntryForIndex(final int p0);
    
    T getEntryForXValue(final float p0, final float p1);
    
    T getEntryForXValue(final float p0, final float p1, final DataSet.Rounding p2);
    
    int getEntryIndex(final float p0, final float p1, final DataSet.Rounding p2);
    
    int getEntryIndex(final T p0);
    
    Legend.LegendForm getForm();
    
    DashPathEffect getFormLineDashEffect();
    
    float getFormLineWidth();
    
    float getFormSize();
    
    MPPointF getIconsOffset();
    
    int getIndexInEntries(final int p0);
    
    String getLabel();
    
    IValueFormatter getValueFormatter();
    
    int getValueTextColor();
    
    int getValueTextColor(final int p0);
    
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
    
    boolean removeEntry(final int p0);
    
    boolean removeEntry(final T p0);
    
    boolean removeEntryByXValue(final float p0);
    
    boolean removeFirst();
    
    boolean removeLast();
    
    void setAxisDependency(final YAxis.AxisDependency p0);
    
    void setDrawIcons(final boolean p0);
    
    void setDrawValues(final boolean p0);
    
    void setHighlightEnabled(final boolean p0);
    
    void setIconsOffset(final MPPointF p0);
    
    void setLabel(final String p0);
    
    void setValueFormatter(final IValueFormatter p0);
    
    void setValueTextColor(final int p0);
    
    void setValueTextColors(final List<Integer> p0);
    
    void setValueTextSize(final float p0);
    
    void setValueTypeface(final Typeface p0);
    
    void setVisible(final boolean p0);
}
